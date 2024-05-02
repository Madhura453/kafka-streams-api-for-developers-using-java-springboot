package com.kafka.orderskafkastreamsapp.notes.windowsAndTimeConcepts;

public class aggregateOrdersByRevenueByTimeWindows {
    /*

    [general_orders_revenue_window]: [store_4567@1706234580000/1706234595000],
    TotalRevenue[locationId=store_4567, runnuingOrderCount=14, runningRevenue=378.00]
      var order3 = new Order(12345, "store_4567",
                new BigDecimal("27.00"),
                OrderType.GENERAL,
                orderItems,
                LocalDateTime.now()
                //LocalDateTime.now(ZoneId.of("UTC"))
        );
    14 * 27.0 = 378.00
    same for restaurant_orders_revenue_window.
    we are grouping orders based on location id

startTime : 2024-01-26T02:03:15Z , endTime : 2024-01-26T02:03:30Z, Count :
TotalRevenue[locationId=store_4567, runnuingOrderCount=4, runningRevenue=108.00]

startTime : 2024-01-26T02:03:15Z , endTime : 2024-01-26T02:03:30Z=
GMT time window time was 15 seconds

02:03:30Z-windowSize(15seconds)=2024-01-26T02:03:15Z

startLDT : 2024-01-26T07:33:15 , endLDT : 2024-01-26T07:33:30, Count :
TotalRevenue[locationId=store_4567, runnuingOrderCount=4, runningRevenue=108.00]
= This our IST time

 [general_orders_revenue_window]: [store_4567@1706234580000/1706234595000], TotalRevenue[locationId=store_4567, runnuingOrderCount=14, runningRevenue=378.00]
20:33:36.370 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology --  general_orders_revenue_window : tumblingWindow : key : [store_4567@1706234595000/1706234610000], value : TotalRevenue[locationId=store_4567, runnuingOrderCount=4, runningRevenue=108.00]
20:33:36.370 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startTime : 2024-01-26T02:03:15Z , endTime : 2024-01-26T02:03:30Z, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=4, runningRevenue=108.00]
20:33:36.370 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startLDT : 2024-01-26T07:33:15 , endLDT : 2024-01-26T07:33:30, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=4, runningRevenue=108.00]
[general_orders_revenue_window]: [store_4567@1706234595000/1706234610000], TotalRevenue[locationId=store_4567, runnuingOrderCount=4, runningRevenue=108.00]
20:33:36.373 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology --  restaurant_orders_revenue_window : tumblingWindow : key : [store_1234@1706234580000/1706234595000], value : TotalRevenue[locationId=store_1234, runnuingOrderCount=14, runningRevenue=210.00]
20:33:36.373 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startTime : 2024-01-26T02:03:00Z , endTime : 2024-01-26T02:03:15Z, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=14, runningRevenue=210.00]
20:33:36.373 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startLDT : 2024-01-26T07:33 , endLDT : 2024-01-26T07:33:15, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=14, runningRevenue=210.00]
[restaurant_orders_revenue_window]: [store_1234@1706234580000/1706234595000], TotalRevenue[locationId=store_1234, runnuingOrderCount=14, runningRevenue=210.00]
20:33:36.378 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology --  restaurant_orders_revenue_window : tumblingWindow : key : [store_1234@1706234595000/1706234610000], value : TotalRevenue[locationId=store_1234, runnuingOrderCount=4, runningRevenue=60.00]
20:33:36.378 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startTime : 2024-01-26T02:03:15Z , endTime : 2024-01-26T02:03:30Z, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=4, runningRevenue=60.00]
20:33:36.378 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startLDT : 2024-01-26T07:33:15 , endLDT : 2024-01-26T07:33:30, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=4, runningRevenue=60.00]
[restaurant_orders_revenue_window]: [store_1234@1706234595000/1706234610000], TotalRevenue[locationId=store_1234, runnuingOrderCount=4, runningRevenue=60.00]
20:33:36.379 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology --  restaurant_orders_revenue_window : tumblingWindow : key : [store_4567@1706234580000/1706234595000], value : TotalRevenue[locationId=store_4567, runnuingOrderCount=14, runningRevenue=378.00]
20:33:36.379 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startTime : 2024-01-26T02:03:00Z , endTime : 2024-01-26T02:03:15Z, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=14, runningRevenue=378.00]
20:33:36.379 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startLDT : 2024-01-26T07:33 , endLDT : 2024-01-26T07:33:15, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=14, runningRevenue=378.00]
[restaurant_orders_revenue_window]: [store_4567@1706234580000/1706234595000], TotalRevenue[locationId=store_4567, runnuingOrderCount=14, runningRevenue=378.00]
20:33:36.385 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology --  restaurant_orders_revenue_window : tumblingWindow : key : [store_4567@1706234595000/1706234610000], value : TotalRevenue[locationId=store_4567, runnuingOrderCount=4, runningRevenue=108.00]
20:33:36.385 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startTime : 2024-01-26T02:03:15Z , endTime : 2024-01-26T02:03:30Z, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=4, runningRevenue=108.00]
20:33:36.385 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startLDT : 2024-01-26T07:33:15 , endLDT : 2024-01-26T07:33:30, Count : TotalRevenue[locationId=store_4567, runnuingOrderCount=4, runningRevenue=108.00]
[restaurant_orders_revenue_window]: [store_4567@1706234595000/1706234610000], TotalRevenue[locationId=store_4567, runnuingOrderCount=4, runningRevenue=108.00]
20:33:36.387 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology --  general_orders_revenue_window : tumblingWindow : key : [store_1234@1706234580000/1706234595000], value : TotalRevenue[locationId=store_1234, runnuingOrderCount=14, runningRevenue=378.00]
20:33:36.387 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startTime : 2024-01-26T02:03:00Z , endTime : 2024-01-26T02:03:15Z, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=14, runningRevenue=378.00]
20:33:36.387 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startLDT : 2024-01-26T07:33 , endLDT : 2024-01-26T07:33:15, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=14, runningRevenue=378.00]
[general_orders_revenue_window]: [store_1234@1706234580000/1706234595000], TotalRevenue[locationId=store_1234, runnuingOrderCount=14, runningRevenue=378.00]
20:33:36.393 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology --  general_orders_revenue_window : tumblingWindow : key : [store_1234@1706234595000/1706234610000], value : TotalRevenue[locationId=store_1234, runnuingOrderCount=4, runningRevenue=108.00]
20:33:36.393 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startTime : 2024-01-26T02:03:15Z , endTime : 2024-01-26T02:03:30Z, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=4, runningRevenue=108.00]
20:33:36.393 [orders-app-68a1305c-6ff5-4196-a75e-f79c0adf93c6-StreamThread-1] INFO com.kafka.orderskafkastreamsapp.topology.windowsAndTimeConcepts.AggregateWithWindowsTopology -- startLDT : 2024-01-26T07:33:15 , endLDT : 2024-01-26T07:33:30, Count : TotalRevenue[locationId=store_1234, runnuingOrderCount=4, runningRevenue=108.00]
[general_orders_revenue_window]: [store_1234@1706234595000/1706234610000], TotalRevenue[locationId=store_1234, runnuingOrderCount=4, runningRevenue=108.00]

     */
}
