package com.kafka.orderskafkastreamsapp.notes;

public class JoinInOrderManagement {

    /*
    task 1
    we have aggregateOrdersByRevenue
     [general_orders_revenue_store]: store_4567, TotalRevenue[locationId=store_4567, runnuingOrderCount=7, runningRevenue=189.00]
       [restaurant_orders_revenue_store]: store_1234, TotalRevenue[locationId=store_1234, runnuingOrderCount=7, runningRevenue=105.00]
  we need to add store information to aggregateOrdersByRevenue result
  store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
  The store contains store id address
  The join result will be KTable-KTale
  Because store doesn't change unless we change contact info or anything.
  join will be happened based on store_id
  store_id is key

  TotalRevenueWithAddress = join output
   join o/p it need to look like this
[general-orders-revenue]: store_1234, TotalRevenueWithAddress[totalRevenue=TotalRevenue[locationId=store_1234, runnuingOrderCount=6, runningRevenue=90.00],
store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]

     */

    /*
    task 2
    we have  aggregateOrdersByCount
   restaurant_orders_count_store]: store_4567, 4
          [restaurant_orders_count_store]: store_1234, 4
          [general_orders_count_store]: store_1234, 4
          [general_orders_count_store]: store_4567, 4
  we need to add store information to aggregateOrdersByCount
  store=Store[locationId=store_1234, address=Address[addressLine1=1234 Street 1 , addressLine2=, city=City1, state=State1, zip=12345], contactNum=1234567890]]
  The store contains store id address
  The join result will be KTable-KTale
  Because store doesn't change unless we change contact info or anything.
  join will be happened based on store_id
  store_id is key

  TotalRevenueWithAddress = join output
  join o/p it need to look like
     */
}
