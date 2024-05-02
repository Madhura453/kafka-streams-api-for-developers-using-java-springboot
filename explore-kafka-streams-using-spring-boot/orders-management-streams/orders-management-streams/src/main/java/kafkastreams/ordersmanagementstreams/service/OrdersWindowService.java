package kafkastreams.ordersmanagementstreams.service;

import kafkastreamsbase.ordersdomain.domain.OrderType;
import kafkastreamsbase.ordersdomain.domain.OrdersCountPerStoreByWindowsDTO;
import kafkastreamsbase.ordersdomain.domain.OrdersRevenuePerStoreByWindowsDTO;
import kafkastreamsbase.ordersdomain.domain.TotalRevenue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static kafkastreams.ordersmanagementstreams.service.OrderService.mapOrderType;
import static kafkastreams.ordersmanagementstreams.topology.OrdersTopology.*;
import static kafkastreams.ordersmanagementstreams.topology.OrdersTopology.RESTAURANT_ORDERS_REVENUE;

@Service
@Slf4j
public class OrdersWindowService {

    @Autowired
    OrderStoreService orderStoreService;

    public List<OrdersCountPerStoreByWindowsDTO> getOrdersCountWindowsByType(String orderType) {
        ReadOnlyWindowStore<String, Long> countWindowsStore = getCountWindowsStore(orderType);
        OrderType orderTypeEnum = mapOrderType(orderType);
        KeyValueIterator<Windowed<String>, Long> countWindowsIterator = countWindowsStore.all();
        return mapToOrdersCountPerStoreByWindowsDTO(orderTypeEnum, countWindowsIterator);
    }

    public static List<OrdersCountPerStoreByWindowsDTO> mapToOrdersCountPerStoreByWindowsDTO(OrderType orderTypeEnum, KeyValueIterator<Windowed<String>, Long> countWindowsIterator) {
        Spliterator<KeyValue<Windowed<String>, Long>> spliterator =
                Spliterators.spliteratorUnknownSize(countWindowsIterator, 0);
        return StreamSupport.stream(spliterator, false)
                .map(keyValue -> new OrdersCountPerStoreByWindowsDTO(
                        keyValue.key.key(),
                        keyValue.value,
                        orderTypeEnum,
                        LocalDateTime.ofInstant(keyValue.key.window().startTime(), ZoneId.of("GMT")),
                        LocalDateTime.ofInstant(keyValue.key.window().endTime(), ZoneId.of("GMT"))
                ))
                .collect(Collectors.toList());
    }

    private ReadOnlyWindowStore<String, Long> getCountWindowsStore(String orderType) {
        return switch (orderType) {
            case GENERAL_ORDERS -> orderStoreService.ordersWindowCountStore(GENERAL_ORDERS_COUNT_WINDOWS_STORE);
            case RESTAURANT_ORDERS -> orderStoreService.ordersWindowCountStore(RESTAURANT_ORDERS_COUNT_WINDOWS_STORE);
            default -> throw new IllegalStateException("Not a Valid Option");
        };
    }

    public List<OrdersCountPerStoreByWindowsDTO> getAllOrdersCountByWindows() {
        List<OrdersCountPerStoreByWindowsDTO> generalOrderCountByWindows =
                getOrdersCountWindowsByType(GENERAL_ORDERS);
        List<OrdersCountPerStoreByWindowsDTO> restaurantOrderCountByWindows =
                getOrdersCountWindowsByType(RESTAURANT_ORDERS);
        return Stream.of(generalOrderCountByWindows, restaurantOrderCountByWindows)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<OrdersCountPerStoreByWindowsDTO> getAllOrdersCountByWindowsInTimeRange(LocalDateTime fromTime, LocalDateTime toTime) {
    /*
     fetchAll going to take the from time and to time return all records with in this time range
     generalOrdersWindowStore have many methods like backwardAll etcS
     pass the value as zone offset dot UTC because all the windowed buckets are actually
      created in the UTC time zone
     */
        Instant fromTimeInstant = fromTime.toInstant(ZoneOffset.UTC);
        Instant toTimeInstant = toTime.toInstant(ZoneOffset.UTC);

        ReadOnlyWindowStore<String, Long> generalOrdersWindowStore = getCountWindowsStore(GENERAL_ORDERS);
        KeyValueIterator<Windowed<String>, Long> generalOrderCountByWindows =
                generalOrdersWindowStore
                        //.fetchAll(fromTimeInstant, toTimeInstant);
                        .backwardFetchAll(fromTimeInstant,toTimeInstant);

        //backwardFetchAll  many other function will be there
        // It's going to take the from and to timeframe and then gives you the records that matches with this data
        List<OrdersCountPerStoreByWindowsDTO> generalOrdersCountByWindowsDTO =
                mapToOrdersCountPerStoreByWindowsDTO(OrderType.GENERAL, generalOrderCountByWindows);

        ReadOnlyWindowStore<String, Long> restaurantOrdersWindowStore = getCountWindowsStore(RESTAURANT_ORDERS);
        KeyValueIterator<Windowed<String>, Long> restaurantOrdersCountByWindows =
                restaurantOrdersWindowStore.fetchAll(fromTimeInstant, toTimeInstant);
        List<OrdersCountPerStoreByWindowsDTO> restaurantOrdersCountByWindowsDTO =
                mapToOrdersCountPerStoreByWindowsDTO(OrderType.RESTAURANT, restaurantOrdersCountByWindows);

        return Stream.of(generalOrdersCountByWindowsDTO, restaurantOrdersCountByWindowsDTO)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public ReadOnlyWindowStore<String, TotalRevenue> getRevenueWindowStore(String orderType) {
        return switch (orderType) {
            case GENERAL_ORDERS -> orderStoreService.ordersWindowRevenueStore(GENERAL_ORDERS_REVENUE_WINDOWS_STORE);
            case RESTAURANT_ORDERS ->
                    orderStoreService.ordersWindowRevenueStore(RESTAURANT_ORDERS_REVENUE_WINDOWS_STORE);
            default -> throw new IllegalStateException("Not a Valid Option");
        };
    }

    public List<OrdersRevenuePerStoreByWindowsDTO> getOrdersRevenueWindowsByType(String orderType) {
        ReadOnlyWindowStore<String, TotalRevenue> revenueWindowStore = getRevenueWindowStore(orderType);
        OrderType orderTypeEnum = mapOrderType(orderType);
        KeyValueIterator<Windowed<String>, TotalRevenue> revenueWindowsIterator = revenueWindowStore.all();
        Spliterator<KeyValue<Windowed<String>, TotalRevenue>> spliterator =
                Spliterators.spliteratorUnknownSize(revenueWindowsIterator, 0);
        return StreamSupport.stream(spliterator, false)
                .map(keyValue -> new OrdersRevenuePerStoreByWindowsDTO(
                        keyValue.key.key(),
                        keyValue.value,
                        mapOrderType(orderType),
                        LocalDateTime.ofInstant(keyValue.key.window().startTime(), ZoneId.of("GMT")),
                        LocalDateTime.ofInstant(keyValue.key.window().endTime(), ZoneId.of("GMT"))
                )).collect(Collectors.toList());
    }
}
