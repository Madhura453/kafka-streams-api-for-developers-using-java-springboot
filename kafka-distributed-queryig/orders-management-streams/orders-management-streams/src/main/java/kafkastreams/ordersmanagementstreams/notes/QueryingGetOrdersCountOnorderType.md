````
public List<OrderCountPerStoreDTO> getOrdersCount(String orderType) {
````
- we have the order type.We need to identify what the store that we are going to interact with


getOrderStore(orderType);
}

````

    private ReadOnlyKeyValueStore<String,Long> getOrderStore(String orderType) {
       return switch (orderType)
        {
            case  GENERAL_ORDERS->orderStoreService.ordersCountStore(GENERAL_ORDERS_COUNT);
            case RESTAURANT_ORDERS->orderStoreService.ordersCountStore(RESTAURANT_ORDERS_COUNT);
            default -> throw new IllegalStateException("Not a Valid Option");
        };
    }
````

- we need to identify from the order type what is the store that we are going to retrieve this
data from.
- GENERAL_ORDERS_COUNT=So with the store name, how do we get the actual store data
- our topology actually, if we go and take a look at it, it writes it as a materialized
store. So it basically writes this to a materialized store
-The next step is to access this materialized store, which is basically our data,
which is in the rocksdb
- So we need to interact with the Rocksdb

# How to interact with RocksDB
1. we need is a Kafka Streams instance
 -  Spring Boot, everything is being managed by the actual bean, which is StreamsBuilderFactoryBean
````
public class KafkaAutoConfiguration {

	private final KafkaProperties properties;
````
- StreamsBuilderFactoryBean the actual bean, which manages a complete instance of the Kafka stream in Spring Boot.
  Behind the scenes, it has an instance of Kafka streams.
  So what we need is a Kafka Streams instance using which we can actually interact with the store
- Grace period only considers the record with a delay timestamp if it's just within the 15 second, even
  though we pass the timestamp as two second before because that's a delay rate, it can go until the
  15 second. That should still be considered for the actual time window.

# Testing

