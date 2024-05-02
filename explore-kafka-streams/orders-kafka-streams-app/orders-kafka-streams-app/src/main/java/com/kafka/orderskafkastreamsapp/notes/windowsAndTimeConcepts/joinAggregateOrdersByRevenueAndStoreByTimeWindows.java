package com.kafka.orderskafkastreamsapp.notes.windowsAndTimeConcepts;

public class joinAggregateOrdersByRevenueAndStoreByTimeWindows {
    /*
[orders]: 12345, Order[orderId=12345, locationId=store_4567, finalAmount=27.00, orderType=GENERAL, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T21:59:14.932406100]
21:59:14.938 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- TimeStamp in extractor : 2024-01-25T21:59:14.932406100
21:59:14.938 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.util.OrderTimeStampExtractor -- instant in extractor : 1706239754932
[orders]: 12345, Order[orderId=12345, locationId=store_4567, finalAmount=27.00, orderType=RESTAURANT, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-01-25T21:59:14.932406100]
21:59:15.109 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology --  restaurant_orders_revenue_window : tumblingWindow : key : [store_1234@1706239725000/1706239740000], value : TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=225.00]
21:59:15.109 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startTime : 2024-01-26T03:28:45Z , endTime : 2024-01-26T03:29:00Z, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=225.00]
21:59:15.109 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startLDT : 2024-01-26T08:58:45 , endLDT : 2024-01-26T08:59, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=225.00]
[restaurant_orders_revenue_window]: [store_1234@1706239725000/1706239740000], TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=225.00]
21:59:15.244 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store restaurant_orders_revenue_window.1706239740000 in regular mode
21:59:15.244 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology --  restaurant_orders_revenue_window : tumblingWindow : key : [store_1234@1706239740000/1706239755000], value : TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=225.00]
21:59:15.244 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startTime : 2024-01-26T03:29:00Z , endTime : 2024-01-26T03:29:15Z, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=225.00]
21:59:15.244 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startLDT : 2024-01-26T08:59 , endLDT : 2024-01-26T08:59:15, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=225.00]
[restaurant_orders_revenue_window]: [store_1234@1706239740000/1706239755000], TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=225.00]
21:59:15.246 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology --  general_orders_revenue_window : tumblingWindow : key : [store_1234@1706239725000/1706239740000], value : TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.246 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startTime : 2024-01-26T03:28:45Z , endTime : 2024-01-26T03:29:00Z, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.247 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startLDT : 2024-01-26T08:58:45 , endLDT : 2024-01-26T08:59, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=405.00]
[general_orders_revenue_window]: [store_1234@1706239725000/1706239740000], TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.317 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store general_orders_revenue_window.1706239740000 in regular mode
21:59:15.317 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology --  general_orders_revenue_window : tumblingWindow : key : [store_1234@1706239740000/1706239755000], value : TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.318 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startTime : 2024-01-26T03:29:00Z , endTime : 2024-01-26T03:29:15Z, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.318 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startLDT : 2024-01-26T08:59 , endLDT : 2024-01-26T08:59:15, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=405.00]
[general_orders_revenue_window]: [store_1234@1706239740000/1706239755000], TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.320 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology --  restaurant_orders_revenue_window : tumblingWindow : key : [store_4567@1706239725000/1706239740000], value : TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.320 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startTime : 2024-01-26T03:28:45Z , endTime : 2024-01-26T03:29:00Z, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.320 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startLDT : 2024-01-26T08:58:45 , endLDT : 2024-01-26T08:59, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
[restaurant_orders_revenue_window]: [store_4567@1706239725000/1706239740000], TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.391 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store restaurant_orders_revenue_window.1706239740000 in regular mode
21:59:15.392 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology --  restaurant_orders_revenue_window : tumblingWindow : key : [store_4567@1706239740000/1706239755000], value : TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.392 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startTime : 2024-01-26T03:29:00Z , endTime : 2024-01-26T03:29:15Z, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.392 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startLDT : 2024-01-26T08:59 , endLDT : 2024-01-26T08:59:15, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
[restaurant_orders_revenue_window]: [store_4567@1706239740000/1706239755000], TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.398 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology --  general_orders_revenue_window : tumblingWindow : key : [store_4567@1706239725000/1706239740000], value : TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.398 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startTime : 2024-01-26T03:28:45Z , endTime : 2024-01-26T03:29:00Z, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.398 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startLDT : 2024-01-26T08:58:45 , endLDT : 2024-01-26T08:59, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
[general_orders_revenue_window]: [store_4567@1706239725000/1706239740000], TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.465 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO org.apache.kafka.streams.state.internals.RocksDBTimestampedStore -- Opening store general_orders_revenue_window.1706239740000 in regular mode
21:59:15.466 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology --  general_orders_revenue_window : tumblingWindow : key : [store_4567@1706239740000/1706239755000], value : TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.466 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startTime : 2024-01-26T03:29:00Z , endTime : 2024-01-26T03:29:15Z, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
21:59:15.466 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.JoinOnWindowedAggregatedTopology -- startLDT : 2024-01-26T08:59 , endLDT : 2024-01-26T08:59:15, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
[general_orders_revenue_window]: [store_4567@1706239740000/1706239755000], TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00]
[restaurant_orders_revenue_window-by Store]: store_1234, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=225.00], store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
[general_orders_revenue_window-by Store]: store_1234, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=405.00], store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
[restaurant_orders_revenue_window-by Store]: store_1234, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=225.00], store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
[general_orders_revenue_window-by Store]: store_1234, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_1234, runnuingOrderCount=15, runningRevenue=405.00], store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
[restaurant_orders_revenue_window-by Store]: store_4567, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00], store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
[general_orders_revenue_window-by Store]: store_4567, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00], store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
[restaurant_orders_revenue_window-by Store]: store_4567, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00], store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
[general_orders_revenue_window-by Store]: store_4567, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_4567, runnuingOrderCount=15, runningRevenue=405.00], store=Store[locationId=store_4567, address=Address[addressLine1=1234 Street 2 , addressLine2=, city=City2, state=State2, zip=541321], contactNum=0987654321]]
     */
}