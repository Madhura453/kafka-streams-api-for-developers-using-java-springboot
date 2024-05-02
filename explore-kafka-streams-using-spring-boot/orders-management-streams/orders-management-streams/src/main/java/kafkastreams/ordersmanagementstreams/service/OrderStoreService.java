package kafkastreams.ordersmanagementstreams.service;

import kafkastreamsbase.ordersdomain.domain.OrdersRevenuePerStoreByWindowsDTO;
import kafkastreamsbase.ordersdomain.domain.TotalRevenue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStoreService {

    @Autowired
    StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    public ReadOnlyKeyValueStore<String, Long> ordersCountStore(String storeName) {
        return streamsBuilderFactoryBean
                .getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(
                        storeName,
                        QueryableStoreTypes.keyValueStore()
                ));
       /*
       we are storing data as keyAndValue
       key is location id
       Aggregated count of orders will be Long
        */
    }

    public ReadOnlyKeyValueStore<String, TotalRevenue> ordersRevenueStore(String storeName) {
        return streamsBuilderFactoryBean
                .getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(
                        storeName,
                        QueryableStoreTypes.keyValueStore()
                ));
    }

    public ReadOnlyWindowStore<String,Long> ordersWindowCountStore(String storeName) {
        return streamsBuilderFactoryBean
                .getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(
                        storeName,
                        QueryableStoreTypes.windowStore()
                ));
    }

    public  ReadOnlyWindowStore<String,TotalRevenue> ordersWindowRevenueStore(String storeName) {
        return streamsBuilderFactoryBean
                .getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(
                        storeName,
                        QueryableStoreTypes.windowStore()
                ));
    }
}
