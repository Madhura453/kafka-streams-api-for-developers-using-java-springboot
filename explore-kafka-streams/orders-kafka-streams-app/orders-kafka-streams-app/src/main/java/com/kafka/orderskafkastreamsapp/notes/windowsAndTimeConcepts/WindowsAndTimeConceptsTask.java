package com.kafka.orderskafkastreamsapp.notes.windowsAndTimeConcepts;

public class WindowsAndTimeConceptsTask {
    /*
    {
 "orderId": 12345,
 "locationId": "store_1234",
 "finalAmount": 27.00,
 "orderType": "GENERAL",
 "orderLineItems": [
 {
 "item": "Bananas",
 "count": 2,
 "amount": 2.00
 },
 {
 "item": "Iphone Charger",
 "count": 1,
 "amount": 25.00
 }
 ],
 "orderedDateTime": "2022-12-05T08:55:27"
}
    1. So this is the JSON that we are going to receive in our Kafka topic.One key thing to note here is that the timestamp is going to
be embedded in the message itself.So in this case we need to build our very own custom timestamp extractor to extract the timestamp from
the message and supply it to our app so that the time windows will be created appropriately based on
the time that's in this message instead of the Kafka record published timestamp.
2. So in this case, this is going to be our JSON and the ordered date time is the actual field, which
is going to hold the orders timestamp, and we are going to extract this and using this, our windowed
logic is going to define the time windows.= OrderTimeStampExtractor


 properties.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, OrderTimeStampExtractor.class);


* this should take care of configuring the Timestep instructor for you and then all the windows will
be created based on the extracted value from this actual implementation.
And then time windows will be created and the records will be aggregated accordingly.
     */
}
