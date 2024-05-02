package kafkastreams.ordersmanagementstreams.service;

import kafkastreamsbase.ordersdomain.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static kafkastreams.ordersmanagementstreams.topology.OrdersTopology.*;

@Service
@Slf4j
public class OrderService {

    @Autowired
    OrderStoreService orderStoreService;


    public List<OrderCountPerStoreDTO> getOrdersCount(String orderType) {
        /*
        we have the order type.We need to identify what the store that we are going to interact with
         */
        ReadOnlyKeyValueStore<String, Long> orderCountStore = getOrderStore(orderType);
        /*
        orderCountStore have many functions.
        all() will return all records
         */
        KeyValueIterator<String, Long> orders = orderCountStore.all();
        Spliterator<KeyValue<String, Long>> spliterators =
                Spliterators.spliteratorUnknownSize(orders, 0);
       /*
        not going to do any parallelism in here, so I'm going to pass the flag as false
        If you want to access or process this in parallel, if you pass this flag as true, then it will
        happen automatically for you
        */
        Stream<KeyValue<String, Long>> ordersStream = StreamSupport.stream(spliterators, false);
        return ordersStream.map(keyValue -> new OrderCountPerStoreDTO(keyValue.key, keyValue.value))
                .toList();
    }

    private ReadOnlyKeyValueStore<String, Long> getOrderStore(String orderType) {
        /*
        * we need to identify from the order type what is the store that we are going to retrieve this
      data from.
      *  GENERAL_ORDERS_COUNT=So with the store name, how do we get the actual store data
      *  our topology actually, if we go and take a look at it, it writes it as a materialized
        store. So it basically writes this to a materialized store
      * The next step is to access this materialized store, which is basically our data,
          which is in the rocksdb
       * So we need to interact with the Rocksdb
         */
        return switch (orderType) {
            case GENERAL_ORDERS -> orderStoreService.ordersCountStore(GENERAL_ORDERS_COUNT);
            case RESTAURANT_ORDERS -> orderStoreService.ordersCountStore(RESTAURANT_ORDERS_COUNT);
            default -> throw new IllegalStateException("Not a Valid Option");
        };
    }

    public OrderCountPerStoreDTO getOrdersCountByLocationId(String orderType, String locationId) {
        ReadOnlyKeyValueStore<String, Long> orderCountStore = getOrderStore(orderType);
        /*
        get single record based on location id
         */
        Long orderCount = orderCountStore.get(locationId);
        if (orderCount != null) {
            return new OrderCountPerStoreDTO(locationId, orderCount);
        }

        return null;
    }

    public List<AllOrdersCountPerStoreDTO> getAllOrdersCount() {
        BiFunction<OrderCountPerStoreDTO, OrderType, AllOrdersCountPerStoreDTO>
                mapper = (orderCountPerStoreDTO, orderType) ->
                new AllOrdersCountPerStoreDTO(orderCountPerStoreDTO.locationId()
                        , orderCountPerStoreDTO.orderCount(), orderType);
        List<OrderCountPerStoreDTO> generalOrderCountStore = getOrdersCount(GENERAL_ORDERS);
        List<OrderCountPerStoreDTO> restaurantOrderStore = getOrdersCount(RESTAURANT_ORDERS);
        List<AllOrdersCountPerStoreDTO> generalOrderCount =
                generalOrderCountStore.stream()
                        .map(orderCountPerStoreDTO -> mapper.apply(orderCountPerStoreDTO, OrderType.GENERAL))
                        .toList();
        List<AllOrdersCountPerStoreDTO> restaurantOrderCount =
                restaurantOrderStore.stream()
                        .map(orderCountPerStoreDTO -> mapper.apply(orderCountPerStoreDTO, OrderType.RESTAURANT))
                        .toList();
        return Stream.of(generalOrderCount,restaurantOrderCount)
                .flatMap(List::stream)
                .toList();
    }

    public List<OrderRevenueDTO> revenueByOrderType(String orderType) {
        ReadOnlyKeyValueStore<String, TotalRevenue> revenueStoreByType=
                getRevenueStore(orderType);
        KeyValueIterator<String, TotalRevenue> revenueIterator=revenueStoreByType.all();
      Spliterator<KeyValue<String, TotalRevenue>> spliterator = Spliterators.spliteratorUnknownSize(revenueIterator,0);
      return StreamSupport.stream(spliterator,false)
              .map(keyValue->new OrderRevenueDTO(keyValue.key,mapOrderType(orderType),keyValue.value))
              .collect(Collectors.toList());
    }

    public static OrderType mapOrderType(String orderType) {
        return switch (orderType){
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
        ReadOnlyKeyValueStore<String, TotalRevenue> revenueStoreByType=
                getRevenueStore(orderType);
       TotalRevenue totalRevenue= revenueStoreByType.get(locationId);
       if (ObjectUtils.isNotEmpty(totalRevenue))
       {
          return new OrderRevenueDTO(locationId,mapOrderType(orderType),totalRevenue);
       }
        return null;
    }
}


