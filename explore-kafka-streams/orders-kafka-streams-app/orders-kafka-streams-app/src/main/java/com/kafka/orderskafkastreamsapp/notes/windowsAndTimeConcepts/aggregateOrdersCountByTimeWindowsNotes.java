package com.kafka.orderskafkastreamsapp.notes.windowsAndTimeConcepts;

public class aggregateOrdersCountByTimeWindowsNotes {
    /*
    timestamp extractor is It is able to extract the record time and convert that record time to a long value, which is the timestamp
    value, and then send that value to the Kafka Streams app.

    TimeStamp in extractor : 2024-01-25T19:14:39.104453300

 19:14:39.112 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T19:14:39.104453300
19:14:39.112 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706229879104
[orders]: 12345, Order[orderId=12345, locationId=store_1234, finalAmount=27.00, orderType=GENERAL, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T19:14:39.104453300]
19:14:39.119 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T19:14:39.104453300
19:14:39.120 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706229879104
[orders]: 54321, Order[orderId=54321, locationId=store_1234, finalAmount=15.00, orderType=RESTAURANT, orderLineItems=[OrderLineItem[item=Pizza, count=2, amount=12.00], OrderLineItem[item=Coffee, count=1, amount=3.00]], orderedDateTime=2024-01-25T19:14:39.104453300]
19:14:39.132 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T19:14:39.104453300
19:14:39.132 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706229879104
[orders]: 12345, Order[orderId=12345, locationId=store_4567, finalAmount=27.00, orderType=GENERAL, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T19:14:39.104453300]
19:14:39.136 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T19:14:39.104453300
19:14:39.136 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706229879104
[orders]: 12345, Order[orderId=12345, locationId=store_4567, finalAmount=27.00, orderType=RESTAURANT, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T19:14:39.104453300]
19:14:40.156 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T19:14:40.149035500
19:14:40.156 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706229880149
[orders]: 12345, Order[orderId=12345, locationId=store_1234, finalAmount=27.00, orderType=GENERAL, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T19:14:40.149035500]
19:14:40.160 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T19:14:40.149035500
19:14:40.161 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706229880149
[orders]: 54321, Order[orderId=54321, locationId=store_1234, finalAmount=15.00, orderType=RESTAURANT, orderLineItems=[OrderLineItem[item=Pizza, count=2, amount=12.00], OrderLineItem[item=Coffee, count=1, amount=3.00]], orderedDateTime=2024-01-25T19:14:40.149035500]
19:14:40.166 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T19:14:40.149035500
19:14:40.166 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706229880149
[orders]: 12345, Order[orderId=12345, locationId=store_4567, finalAmount=27.00, orderType=GENERAL, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T19:14:40.149035500]
19:14:40.170 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T19:14:40.149035500
19:14:40.170 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706229880149
[orders]: 12345, Order[orderId=12345, locationId=store_4567, finalAmount=27.00, orderType=RESTAURANT, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T19:14:40.149035500]
19:14:41.184 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T19:14:41.175929200
19:14:41.184 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706229881175
[orders]: 12345, Order[orderId=12345, locationId=store_1234, finalAmount=27.00, orderType=GENERAL, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T19:14:41.175929200]
19:14:41.193 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T19:14:41.175929200
19:14:41.193 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706229881175
[orders]: 54321, Order[orderId=54321, locationId=store_1234, finalAmount=15.00, orderType=RESTAURANT, orderLineItems=[OrderLineItem[item=Pizza, count=2, amount=12.00], OrderLineItem[item=Coffee, count=1, amount=3.00]], orderedDateTime=2024-01-25T19:14:41.175929200]
19:14:41.198 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T19:14:41.175929200
19:14:41.198 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706229881175
[orders]: 12345, Order[orderId=12345, locationId=store_4567, finalAmount=27.00, orderType=GENERAL, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T19:14:41.175929200]
19:14:41.200 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T19:14:41.175929200
19:14:41.200 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706229881175
[orders]: 12345, Order[orderId=12345, locationId=store_4567, finalAmount=27.00, orderType=RESTAURANT, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T19:14:41.175929200]
19:14:42.213 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeS





  startTime : 2024-01-26T00:44:30Z , endTime : 2024-01-26T00:44:45Z, Count : 11= record timestamp in GMT
  that the windows are created in GMT
  startLDT : 2024-01-26T06:14:30 , endLDT : 2024-01-26T06:14:45, Count : 11= converting to our local system time IST

  =  printLocalDateTimes

  Window size is 15 seconds= startTime : 2024-01-26T00:44:30Z , endTime : 2024-01-26T00:44:45Z=
  00:44:45Z-windows time(15 seconds) = 00:44:30Z
  45-15=30
  startTime : 2024-01-26T00:45:00Z , endTime : 2024-01-26T00:45:15Z, Count : 14 = 15 difference gap
  [general_orders_count_window]: [store_4567@1706229870000/1706229885000], 11
19:14:48.973 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store general_orders_count_window.1706229840000 in regular mode
19:14:48.984 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology --  general_orders_count_window : tumblingWindow : key : [store_1234@1706229870000/1706229885000], value : 11
19:14:48.984 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startTime : 2024-01-26T00:44:30Z , endTime : 2024-01-26T00:44:45Z, Count : 11
19:14:48.984 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startLDT : 2024-01-26T06:14:30 , endLDT : 2024-01-26T06:14:45, Count : 11
[general_orders_count_window]: [store_1234@1706229870000/1706229885000], 11
19:14:49.063 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store restaurant_orders_count_window.1706229840000 in regular mode
19:14:49.074 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology --  restaurant_orders_count_window : tumblingWindow : key : [store_1234@1706229870000/1706229885000], value : 11
19:14:49.075 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startTime : 2024-01-26T00:44:30Z , endTime : 2024-01-26T00:44:45Z, Count : 11
19:14:49.075 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startLDT : 2024-01-26T06:14:30 , endLDT : 2024-01-26T06:14:45, Count : 11
[restaurant_orders_count_window]: [store_1234@1706229870000/1706229885000], 11
19:14:49.161 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store restaurant_orders_count_window.1706229840000 in regular mode
19:14:49.171 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology --  restaurant_orders_count_window : tumblingWindow : key : [store_4567@1706229870000/1706229885000], value : 11
19:14:49.171 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startTime : 2024-01-26T00:44:30Z , endTime : 2024-01-26T00:44:45Z, Count : 11
19:14:49.172 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startLDT : 2024-01-26T06:14:30 , endLDT : 2024-01-26T06:14:45, Count : 11
[restaurant_orders_count_window]: [store_4567@1706229870000/1706229885000], 11

     */
}
