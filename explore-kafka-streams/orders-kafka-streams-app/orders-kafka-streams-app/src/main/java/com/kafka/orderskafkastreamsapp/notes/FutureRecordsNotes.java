package com.kafka.orderskafkastreamsapp.notes;

public class FutureRecordsNotes {
    /*
producer Records
14:54:14.148 [main] INFO com.kafka.orderskafkastreamsapp.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=orders, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=12345, value={"orderId":12345,"locationId":"store_1234","finalAmount":27.00,"orderType":"GENERAL","orderLineItems":[{"item":"Bananas","count":2,"amount":2.00},{"item":"Iphone Charger","count":1,"amount":25.00}],"orderedDateTime":"2024-01-27T14:54:13.3259922"}, timestamp=null)
14:54:14.927 [kafka-producer-network-thread | producer-1] INFO org.apache.kafka.clients.Metadata -- [Producer clientId=producer-1] Cluster ID: Zv997eriQq2Kmjvc9Kn-EQ
14:54:14.933 [kafka-producer-network-thread | producer-1] INFO org.apache.kafka.clients.producer.internals.TransactionManager -- [Producer clientId=producer-1] ProducerId set to 3 with epoch 0
14:54:14.992 [main] INFO com.kafka.orderskafkastreamsapp.producer.OrdersMockDataProducer -- Published the order message : orders-0@4
14:54:14.993 [main] INFO com.kafka.orderskafkastreamsapp.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=orders, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=54321, value={"orderId":54321,"locationId":"store_1234","finalAmount":15.00,"orderType":"RESTAURANT","orderLineItems":[{"item":"Pizza","count":2,"amount":12.00},{"item":"Coffee","count":1,"amount":3.00}],"orderedDateTime":"2024-01-27T14:54:13.3259922"}, timestamp=null)
14:54:14.997 [main] INFO com.kafka.orderskafkastreamsapp.producer.OrdersMockDataProducer -- Published the order message : orders-0@5
14:54:14.998 [main] INFO com.kafka.orderskafkastreamsapp.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=orders, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=12345, value={"orderId":12345,"locationId":"store_4567","finalAmount":27.00,"orderType":"GENERAL","orderLineItems":[{"item":"Bananas","count":2,"amount":2.00},{"item":"Iphone Charger","count":1,"amount":25.00}],"orderedDateTime":"2024-01-27T14:54:13.3259922"}, timestamp=null)
14:54:15.002 [main] INFO com.kafka.orderskafkastreamsapp.producer.OrdersMockDataProducer -- Published the order message : orders-0@6
14:54:15.002 [main] INFO com.kafka.orderskafkastreamsapp.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=orders, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=12345, value={"orderId":12345,"locationId":"store_4567","finalAmount":27.00,"orderType":"RESTAURANT","orderLineItems":[{"item":"Bananas","count":2,"amount":2.00},{"item":"Iphone Charger","count":1,"amount":25.00}],"orderedDateTime":"2024-01-27T14:54:13.3259922"}, timestamp=null)
14:54:15.006 [main] INFO com.kafka.orderskafkastreamsapp.producer.OrdersMockDataProducer -- Published the order message : orders-0@7


joinAggregateOrdersByRevenueAndStoreByTimeWindows o/p
//

Today date is 26-01-2024 02.50 PM

startTime : 2024-01-27T20:24:00Z , endTime : 2024-01-27T20:24:15Z= 15 seconds difference=
Windows will be created for future records also
*  it creates a windows even for the future records, even though the current timestamp is lesser
than the record timestamp

"orderedDateTime":"2024-01-27T14:54:13.3259922" = for this timestamp there must be window
[general_orders_revenue_window]: [store_1234@1706387040000/1706387055000], TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=27.00]
14:54:40.501 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store general_orders_revenue_window.1706387040000 in regular mode
14:54:40.506 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology --  general_orders_revenue_window : tumblingWindow : key : [store_4567@1706387040000/1706387055000], value : TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00]
14:54:40.506 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startTime : 2024-01-27T20:24:00Z , endTime : 2024-01-27T20:24:15Z, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00]
14:54:40.506 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startLDT : 2024-01-28T01:54 , endLDT : 2024-01-28T01:54:15, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00]
[general_orders_revenue_window]: [store_4567@1706387040000/1706387055000], TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00]
14:54:40.557 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store restaurant_orders_revenue_window.1706387040000 in regular mode
14:54:40.565 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology --  restaurant_orders_revenue_window : tumblingWindow : key : [store_4567@1706387040000/1706387055000], value : TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00]
14:54:40.565 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startTime : 2024-01-27T20:24:00Z , endTime : 2024-01-27T20:24:15Z, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00]
14:54:40.565 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startLDT : 2024-01-28T01:54 , endLDT : 2024-01-28T01:54:15, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00]
[restaurant_orders_revenue_window]: [store_4567@1706387040000/1706387055000], TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00]
14:54:40.622 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store restaurant_orders_revenue_window.1706387040000 in regular mode
14:54:40.630 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology --  restaurant_orders_revenue_window : tumblingWindow : key : [store_1234@1706387040000/1706387055000], value : TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=15.00]
14:54:40.630 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startTime : 2024-01-27T20:24:00Z , endTime : 2024-01-27T20:24:15Z, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=15.00]
14:54:40.630 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startLDT : 2024-01-28T01:54 , endLDT : 2024-01-28T01:54:15, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=15.00]
[restaurant_orders_revenue_window]: [store_1234@1706387040000/1706387055000], TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=15.00]
[general_orders_revenue_window-by Store]: store_1234, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=27.00], store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
[restaurant_orders_revenue_window-by Store]: store_1234, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_1234, runnuingOrderCount=1, runningRevenue=15.00], store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
[restaurant_orders_revenue_window-by Store]: store_4567, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00], store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
[general_orders_revenue_window-by Store]: store_4567, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_4567, runnuingOrderCount=1, runningRevenue=27.00], store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
     */
}
