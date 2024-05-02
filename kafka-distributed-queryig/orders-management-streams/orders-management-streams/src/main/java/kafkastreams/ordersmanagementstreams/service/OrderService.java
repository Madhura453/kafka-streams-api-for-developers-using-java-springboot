package kafkastreams.ordersmanagementstreams.service;

import kafkastreams.ordersmanagementstreams.client.OrdersServiceClient;
import kafkastreamsbase.ordersdomain.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static kafkastreams.ordersmanagementstreams.topology.OrdersTopology.*;

@Service
@Slf4j
public class OrderService {

    private OrderStoreService orderStoreService;

    private OrdersServiceClient ordersServiceClient;

    private MetaDataService metaDataService;
    @Value("${server.port}")
    private Integer port;
    /*
    It will get current instance running port
     */

    public OrderService(OrderStoreService orderStoreService, MetaDataService metaDataService
            , OrdersServiceClient ordersServiceClient) {
        this.metaDataService = metaDataService;
        this.orderStoreService = orderStoreService;
        this.ordersServiceClient = ordersServiceClient;
    }


    public List<OrderCountPerStoreDTO> getOrdersCount(String orderType, String queryOtherHosts) {

        ReadOnlyKeyValueStore<String, Long> orderCountStore = getOrderStore(orderType);
        KeyValueIterator<String, Long> orders = orderCountStore.all();
        Spliterator<KeyValue<String, Long>> spliterators =
                Spliterators.spliteratorUnknownSize(orders, 0);
        Stream<KeyValue<String, Long>> ordersStream = StreamSupport.stream(spliterators, false);

        List<OrderCountPerStoreDTO> orderCountPerStoreDTOListCurrentInstance =
                ordersStream.map(keyValue -> new OrderCountPerStoreDTO(keyValue.key, keyValue.value))
                        .toList();
        //1. fetch the metadata about other instances

        //2. make the restcall to get the data from other instance
        // make sure the other instance is not going to make any network calls to other instances

        List<OrderCountPerStoreDTO> orderCountPerStoreDTOList = retrieveDataFromOtherInstances
                (orderType, Boolean.parseBoolean(queryOtherHosts));
        log.info("orderTye:{},queryOtherHosts:{}", orderType, Boolean.parseBoolean(queryOtherHosts));

        log.info("orderCountPerStoreDTOList :  {} , orderCountPerStoreDTOListOtherInstance : {} ", orderCountPerStoreDTOListCurrentInstance,
                orderCountPerStoreDTOList);
        //3. aggregate the data.

        return Stream.of(orderCountPerStoreDTOListCurrentInstance, orderCountPerStoreDTOList)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .toList();
             /*
                 there is a possibility this can still return null. If this flag is false, then we will return null.
                So we want to filter that one out
              */
    }

    private List<OrderCountPerStoreDTO> retrieveDataFromOtherInstances(String orderType,

                                                                       boolean queryOtherHosts) {
        List<HostInfoDTO> otherHosts = otherHosts();
        log.info("otherHosts: {}", otherHosts);

        log.info("queryOtherHosts: {},otherHosts: {}", queryOtherHosts, otherHosts);
        if (queryOtherHosts && otherHosts != null && !otherHosts.isEmpty()) {
/*
           if it's not empty, what we can do is we can basically write the logic to make the rest
          call to fetch the information about the data that's sitting based on the order type
         step 2.  make the restcall to get the data from other instance
            make sure the other instance is not going to make any network calls to other instances

            queryOtherHosts = if true then only call other instances
 */

            return otherHosts.stream()
                    .map(hostInfoDTO -> ordersServiceClient
                            .retrieveOrdersCountByOrderType(hostInfoDTO, orderType))
                    .flatMap(Collection::stream)
                    .toList();


        }
        return null;
    }

    private List<HostInfoDTO> otherHosts() {

        try {
            String currentMachineAddress = InetAddress.getLocalHost().getHostAddress();
            List<HostInfoDTO> hostInfoDTOS = metaDataService.getStreamMetaData();
            return hostInfoDTOS
                    .stream()
                    // we don't need current Instance.
                    // We don't need to know about the current instance which the request is going to be received
                    // currentMachineAddress will equal other instance host
                    .filter(hostInfoDTO -> currentMachineAddress.equals(hostInfoDTO.host())
                            && hostInfoDTO.port() != port)
                    .toList();
        } catch (UnknownHostException e) {
            log.error("exception in other hosts", e.getMessage());
        }

        // It will give Other instances metadata info
        return null;
    }

    private ReadOnlyKeyValueStore<String, Long> getOrderStore(String orderType) {
        return switch (orderType) {
            case GENERAL_ORDERS -> orderStoreService.ordersCountStore(GENERAL_ORDERS_COUNT);
            case RESTAURANT_ORDERS -> orderStoreService.ordersCountStore(RESTAURANT_ORDERS_COUNT);
            default -> throw new IllegalStateException("Not a Valid Option");
        };
    }

    public OrderCountPerStoreDTO getOrdersCountByLocationId(String orderType, String locationId) {
        String storeName = mapOrderCountStoreName(orderType);
        HostInfoDTOWithKey hostMetaData = metaDataService.getStreamsMetaDataForKey
                (storeName, locationId);
        // we are getting info about for given key which instance having the data=hostMetaData
        log.info("hostMetaData : {} ", hostMetaData);
        if (hostMetaData != null) {
            if (hostMetaData.port() == port) {
                log.info("Fetching the data from the current instance");
                // Then go ahead and get the data from the current instance itself.
                ReadOnlyKeyValueStore<String, Long> orderCountStore = getOrderStore(orderType);
                Long orderCount = orderCountStore.get(locationId);
                if (orderCount != null) {
                    OrderCountPerStoreDTO orderCountPerStoreDTO = new OrderCountPerStoreDTO(locationId, orderCount);
                    log.info("Fetching the data from the current instance: OrderCountPerStoreDTO:{}",orderCountPerStoreDTO );
                    return orderCountPerStoreDTO;
                }
                return null;
            }
            // If this value is not equal, then we need to get this data from a remote instance
            else {
                // remote instance
                // this is where we are going to be implementing the data to retrieve the
                // key value from the remote instance
                OrderCountPerStoreDTO orderCountPerStoreDTO = ordersServiceClient
                        .retrieveOrdersCountByOrderTypeAndLocationId(new HostInfoDTO(
                                        hostMetaData.host(), hostMetaData.port()),
                                orderType, locationId
                        );
                log.info("Fetching the data from the remote instance: orderCountPerStoreDTO{}",orderCountPerStoreDTO);
                return orderCountPerStoreDTO;
            }

        }


        return null;
    }

    private String mapOrderCountStoreName(String orderType) {
        return switch (orderType) {
            case GENERAL_ORDERS -> GENERAL_ORDERS_COUNT;
            case RESTAURANT_ORDERS -> RESTAURANT_ORDERS_COUNT;
            default -> throw new IllegalStateException("Not a Valid Option");
        };
    }

    public List<AllOrdersCountPerStoreDTO> getAllOrdersCount(String queryOtherHosts) {
        BiFunction<OrderCountPerStoreDTO, OrderType, AllOrdersCountPerStoreDTO>
                mapper = (orderCountPerStoreDTO, orderType) ->
                new AllOrdersCountPerStoreDTO(orderCountPerStoreDTO.locationId()
                        , orderCountPerStoreDTO.orderCount(), orderType);
        List<OrderCountPerStoreDTO> generalOrderCountStore = getOrdersCount(GENERAL_ORDERS, queryOtherHosts);
        List<OrderCountPerStoreDTO> restaurantOrderStore = getOrdersCount(RESTAURANT_ORDERS, queryOtherHosts);
        List<AllOrdersCountPerStoreDTO> generalOrderCount =
                generalOrderCountStore.stream()
                        .map(orderCountPerStoreDTO -> mapper.apply(orderCountPerStoreDTO, OrderType.GENERAL))
                        .toList();
        List<AllOrdersCountPerStoreDTO> restaurantOrderCount =
                restaurantOrderStore.stream()
                        .map(orderCountPerStoreDTO -> mapper.apply(orderCountPerStoreDTO, OrderType.RESTAURANT))
                        .toList();
        return Stream.of(generalOrderCount, restaurantOrderCount)
                .flatMap(List::stream)
                .toList();
    }

    public List<OrderRevenueDTO> revenueByOrderType(String orderType) {
        ReadOnlyKeyValueStore<String, TotalRevenue> revenueStoreByType =
                getRevenueStore(orderType);
        KeyValueIterator<String, TotalRevenue> revenueIterator = revenueStoreByType.all();
        Spliterator<KeyValue<String, TotalRevenue>> spliterator = Spliterators.spliteratorUnknownSize(revenueIterator, 0);
        return StreamSupport.stream(spliterator, false)
                .map(keyValue -> new OrderRevenueDTO(keyValue.key, mapOrderType(orderType), keyValue.value))
                .collect(Collectors.toList());
    }

    public static OrderType mapOrderType(String orderType) {
        return switch (orderType) {
            case GENERAL_ORDERS -> OrderType.GENERAL;
            case RESTAURANT_ORDERS -> OrderType.RESTAURANT;
            default -> throw new IllegalStateException("Not a valid option");
        };
    }

    private ReadOnlyKeyValueStore<String, TotalRevenue> getRevenueStore(String orderType) {
        return switch (orderType) {
            case GENERAL_ORDERS -> orderStoreService.ordersRevenueStore(GENERAL_ORDERS_REVENUE);
            case RESTAURANT_ORDERS -> orderStoreService.ordersRevenueStore(RESTAURANT_ORDERS_REVENUE);
            default -> throw new IllegalStateException("Not a Valid Option");
        };
    }

    public OrderRevenueDTO getRevenueByLocationId(String orderType, String locationId) {
        ReadOnlyKeyValueStore<String, TotalRevenue> revenueStoreByType =
                getRevenueStore(orderType);
        TotalRevenue totalRevenue = revenueStoreByType.get(locationId);
        if (ObjectUtils.isNotEmpty(totalRevenue)) {
            return new OrderRevenueDTO(locationId, mapOrderType(orderType), totalRevenue);
        }
        return null;
    }
}


