package com.kafka.orderskafkastreamsapp.notes;

public class ReKeyingKafkaRecords {
    /*
    Skipping record due to null key or value. topic=[orders] partition=[0] offset=[2]
    = if key is null kafka will skip the record
  .map((key, value) -> KeyValue.pair(value.locationId(), value))
  * map operation take cares of adding new key
  * when use map it won't trigger partition. Because map is terminal operation.
  * when use map along with group by it will trigger partition. create partition topic
  orders-app-restaurant_orders_count_store-repartition.
  * when rekeying partition topic will created internal topic orders-app-restaurant_orders_count_store-repartition. whole
  reconstruct key value pair happens in this topic.
  * orders-app-restaurant_orders_count_store-changelog = when Materialized used
  // Rekeying 2 ways
  1) map 2) select key

  select key
  * cost expensive operation
  * Don't required aggregation operation group by operation. whenever it will call it will do the repartition.
  *

  Difference b/w map and select key
  1) map executes repartition when group by= use when we need re keying along with group by operation
  2) select don't require group by do the repartition.= use when re keying records irrespective of  aggregation

     */
}
