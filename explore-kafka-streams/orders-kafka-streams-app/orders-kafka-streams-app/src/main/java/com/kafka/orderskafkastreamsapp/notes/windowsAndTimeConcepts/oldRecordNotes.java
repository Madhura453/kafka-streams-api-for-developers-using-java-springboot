package com.kafka.orderskafkastreamsapp.notes.windowsAndTimeConcepts;

public class oldRecordNotes {
    /*
    publish a record that is lesser than the timestamp of the current window

    Mock data producer records
    current date is= 26-10-2024 03.07 pm
    15:06:37.139 [main] INFO com.kafka.orderskafkastreamsapp.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=orders, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=12345, value={"orderId":12345,"locationId":"store_1234","finalAmount":27.00,"orderType":"GENERAL","orderLineItems":[{"item":"Bananas","count":2,"amount":2.00},{"item":"Iphone Charger","count":1,"amount":25.00}],"orderedDateTime":"2024-01-25T15:06:36.4523828"}, timestamp=null)
15:06:37.777 [kafka-producer-network-thread | producer-1] INFO org.apache.kafka.clients.Metadata -- [Producer clientId=producer-1] Cluster ID: Zv997eriQq2Kmjvc9Kn-EQ
15:06:37.785 [kafka-producer-network-thread | producer-1] INFO org.apache.kafka.clients.producer.internals.TransactionManager -- [Producer clientId=producer-1] ProducerId set to 4 with epoch 0
15:06:37.851 [main] INFO com.kafka.orderskafkastreamsapp.producer.OrdersMockDataProducer -- Published the order message : orders-0@8
15:06:37.854 [main] INFO com.kafka.orderskafkastreamsapp.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=orders, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=54321, value={"orderId":54321,"locationId":"store_1234","finalAmount":15.00,"orderType":"RESTAURANT","orderLineItems":[{"item":"Pizza","count":2,"amount":12.00},{"item":"Coffee","count":1,"amount":3.00}],"orderedDateTime":"2024-01-25T15:06:36.4523828"}, timestamp=null)
15:06:37.859 [main] INFO com.kafka.orderskafkastreamsapp.producer.OrdersMockDataProducer -- Published the order message : orders-0@9
15:06:37.859 [main] INFO com.kafka.orderskafkastreamsapp.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=orders, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=12345, value={"orderId":12345,"locationId":"store_4567","finalAmount":27.00,"orderType":"GENERAL","orderLineItems":[{"item":"Bananas","count":2,"amount":2.00},{"item":"Iphone Charger","count":1,"amount":25.00}],"orderedDateTime":"2024-01-25T15:06:36.4523828"}, timestamp=null)
15:06:37.862 [main] INFO com.kafka.orderskafkastreamsapp.producer.OrdersMockDataProducer -- Published the order message : orders-0@10
15:06:37.863 [main] INFO com.kafka.orderskafkastreamsapp.producer.ProducerUtil -- producerRecord : ProducerRecord(topic=orders, partition=null, headers=RecordHeaders(headers = [], isReadOnly = false), key=12345, value={"orderId":12345,"locationId":"store_4567","finalAmount":27.00,"orderType":"RESTAURANT","orderLineItems":[{"item":"Bananas","count":2,"amount":2.00},{"item":"Iphone Charger","count":1,"amount":25.00}],"orderedDateTime":"2024-01-25T15:06:36.4523828"}, timestamp=null)
15:06:37.867 [main] INFO com.kafka.orderskafkastreamsapp.producer.OrdersMockDataProducer -- Published the order message : orders-0@11

"orderedDateTime":"2024-01-25T15:06:36.4523828" = past time = older record

joinAggregateOrdersByRevenueAndStoreByTimeWindows = o/p


15:06:37.848 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T15:06:36.452382800
15:06:37.848 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706214996452
[orders]: 12345, Order[orderId=12345, locationId=store_1234, finalAmount=27.00, orderType=GENERAL, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T15:06:36.452382800]
15:06:37.860 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T15:06:36.452382800
15:06:37.860 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706214996452
[orders]: 54321, Order[orderId=54321, locationId=store_1234, finalAmount=15.00, orderType=RESTAURANT, orderLineItems=[OrderLineItem[item=Pizza, count=2, amount=12.00], OrderLineItem[item=Coffee, count=1, amount=3.00]], orderedDateTime=2024-01-25T15:06:36.452382800]
15:06:37.865 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T15:06:36.452382800
15:06:37.865 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706214996452
[orders]: 12345, Order[orderId=12345, locationId=store_4567, finalAmount=27.00, orderType=GENERAL, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T15:06:36.452382800]
15:06:37.869 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T15:06:36.452382800
15:06:37.869 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706214996452
[orders]: 12345, Order[orderId=12345, locationId=store_4567, finalAmount=27.00, orderType=RESTAURANT, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T15:06:36.452382800]
15:06:37.974 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] WARN org.apache.kafka.streams.kstream.internals.KStreamWindowAggregate -- Skipping record for expired window. topic=[orders-app-general_orders_revenue_window-repartition] partition=[0] offset=[1] timestamp=[1706214996452] window=[1706214990000,1706215005000) expiration=[1706387053325] streamTime=[1706387053325]
15:06:37.974 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] WARN org.apache.kafka.streams.kstream.internals.KStreamWindowAggregate -- Skipping record for expired window. topic=[orders-app-general_orders_revenue_window-repartition] partition=[1] offset=[1] timestamp=[1706214996452] window=[1706214990000,1706215005000) expiration=[1706387053325] streamTime=[1706387053325]
15:06:37.974 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] WARN org.apache.kafka.streams.kstream.internals.KStreamWindowAggregate -- Skipping record for expired window. topic=[orders-app-restaurant_orders_revenue_window-repartition] partition=[0] offset=[1] timestamp=[1706214996452] window=[1706214990000,1706215005000) expiration=[1706387053325] streamTime=[1706387053325]
15:06:37.974 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] WARN org.apache.kafka.streams.kstream.internals.KStreamWindowAggregate -- Skipping record for expired window. topic=[orders-app-restaurant_orders_revenue_window-repartition] partition=[1] offset=[1] timestamp=[1706214996452] window=[1706214990000,1706215005000) expiration=[1706387053325] streamTime=[1706387053325]
15:07:07.882 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO org.apache.kafka.streams.processor.internals.StreamThread -- stream-thread [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] Processed 8 total records, ran 0 punctuators, and committed 5 total tasks since the last update

We will it was Skipping record for expired window.
So any records that's going to come prior to this window will be skipped for the expired window

The current window=   startTime : 2024-01-27T20:24:00Z , endTime : 2024-01-27T20:24:15Z

the record now "orderedDateTime":"2024-01-25T15:06:36.4523828" = It is older than current window called
 older record.
 older records are skipped
     */
}
