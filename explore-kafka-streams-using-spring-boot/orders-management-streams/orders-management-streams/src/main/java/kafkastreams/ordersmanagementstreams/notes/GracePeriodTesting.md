# Testing
- First publish records at  time interval 2024-02-09T15:06:00
  in GraceOrdersMockDataProducer only publishOrders(objectMapper, buildOrders()); and comment
  publishOrdersToTestGrace(objectMapper, buildOrdersToTestGrace("2024-02-09T15:06:58"));

The o/p will be
http://localhost:8080/v1/orders/windows/count
````
[
  {
    "locationId": "store_4567",
    "orderCount": 1,
    "orderType": "GENERAL",
    "startWindow": "2024-02-09T15:06:00",
    "endWindow": "2024-02-09T15:07:00"
  }
  {
    "locationId": "store_1234",
    "orderCount": 1,
    "orderType": "GENERAL",
    "startWindow": "2024-02-09T15:06:00",
    "endWindow": "2024-02-09T15:07:00"
  }
  {
    "locationId": "store_1234",
    "orderCount": 1,
    "orderType": "RESTAURANT",
    "startWindow": "2024-02-09T15:06:00",
    "endWindow": "2024-02-09T15:07:00"
  },
  {
    "locationId": "store_4567",
    "orderCount": 1,
    "orderType": "RESTAURANT",
    "startWindow": "2024-02-09T15:06:00",
    "endWindow": "2024-02-09T15:07:00"
  }
]
````
- Then at second time execute both
- at  time interval 2024-02-09T15:07:00 but before 2024-02-09T15:07:15
- 15 seconds grace period time window. make sure publish record before 15 seconds window
  publishOrdersToTestGrace(objectMapper, buildOrdersToTestGrace("2024-02-09T15:06:58")) and
  publishOrders(objectMapper, buildOrders());
- http://localhost:8080/v1/orders/windows/count
- 
- In Below output we notice that 
````
"startWindow": "2024-02-09T15:06:00",
  "endWindow": "2024-02-09T15:07:00"
````
- time interval the count increased 2. 
- because buildOrdersToTestGrace records need to send at 2024-02-09T15:06:00-2024-02-09T15:07:00 time 
   interval. But it was sent at  2024-02-09T15:07:00 but before 2024-02-09T15:07:15 this time interval.
- we mention in our code graceWindowSize 15 seconds. so 2024-02-09T15:07:15 it is time .
- so 2024-02-09T15:07:00-2024-02-09T15:07:15 followed window of "startWindow": "2024-02-09T15:06:00",
  "endWindow": "2024-02-09T15:07:00"
- so record count was increase 2
- So in this case, the order count is updated to for all the events, even though we had a data delay.
So this is only possible because we have added the grace period over here
````
Duration windowSize = Duration.ofSeconds(60);
        Duration graceWindowSize = Duration.ofSeconds(15);
        TimeWindows timeWindows = TimeWindows.ofSizeAndGrace(windowSize, graceWindowSize);
````
````
  [
  {
    "locationId": "store_4567",
    "orderCount": 2,
    "orderType": "GENERAL",
    "startWindow": "2024-02-09T15:06:00",
    "endWindow": "2024-02-09T15:07:00"
  }
  {
    "locationId": "store_1234",
    "orderCount": 2,
    "orderType": "GENERAL",
    "startWindow": "2024-02-09T15:06:00",
    "endWindow": "2024-02-09T15:07:00"
  }
  {
    "locationId": "store_1234",
    "orderCount": 2,
    "orderType": "RESTAURANT",
    "startWindow": "2024-02-09T15:06:00",
    "endWindow": "2024-02-09T15:07:00"
  },
  {
    "locationId": "store_4567",
    "orderCount": 2,
    "orderType": "RESTAURANT",
    "startWindow": "2024-02-09T15:06:00",
    "endWindow": "2024-02-09T15:07:00"
  },
   {
    "locationId": "store_4567",
    "orderCount": 1,
    "orderType": "GENERAL",
    "startWindow": "2024-02-09T15:07:00",
    "endWindow": "2024-02-09T15:08:00"
  }
  {
    "locationId": "store_1234",
    "orderCount": 1,
    "orderType": "GENERAL",
    "startWindow": "2024-02-09T15:07:00",
    "endWindow": "2024-02-09T15:08:00"
  }
  {
    "locationId": "store_1234",
    "orderCount": 1,
    "orderType": "RESTAURANT",
    "startWindow": "2024-02-09T15:07:00",
    "endWindow": "2024-02-09T15:08:00"
  },
  {
    "locationId": "store_4567",
    "orderCount": 1,
    "orderType": "RESTAURANT",
    "startWindow": "2024-02-09T15:07:00",
    "endWindow": "2024-02-09T15:08:00"
  }
]
  ````

- Publish records 2024-02-09T15:07:45 seconds  at buildOrdersToTestGrace("2024-02-09T15:06:58")
- This records will skip because the records not in preceding window. not in 15 seconds window of
  2024-02-09T15:06:00.
- The records will skip.
- - Okay, so now what I'm going to do, I'm going to still publish more data, but you will notice a different

logger in the console because this record that we are going to publish with the Grace period, that's

going to be way older, right?

- Which is actually way older than the 15 second grace period that we have provided.

- So in this case, let's go ahead and execute this function(publishOrdersToTestGrace(objectMapper, buildOrdersToTestGrace("2024-02-09T15:06:58"));) one more time.

- In the logger, you will notice the records are being skipped.

So if you go to the orders management stream application, you can see we have a lot of records that

are skipped because they are outside the which means the data is delayed beyond the grace period that

we have provided.

So we have a lot of records that are ignored.

So this is a clear signal.

Grace period only considers the record with a delay timestamp if it's just within the 15 second, even

though we pass the timestamp as two second before because that's a delay rate, it can go until the

15 second.

That should still be considered for the actual time window