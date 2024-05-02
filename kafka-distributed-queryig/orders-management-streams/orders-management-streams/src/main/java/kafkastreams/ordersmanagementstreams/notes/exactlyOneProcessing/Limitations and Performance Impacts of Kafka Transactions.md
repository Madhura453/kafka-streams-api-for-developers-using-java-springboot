In this lecture, I'm going to cover the limitations and performance impacts of enabling.

Exactly once processing.

The first one is the limitations.

So in limitations, the first concept is deduplication is not a transaction feature.

So let's say if a duplicate record is published into the source topic that the Kafka Streams app is

streaming from, then the Kafka streams have has no clue that the record was processed already, for

example, in the streams processing logic.

If we are sending a text or email for each record that we read from the source topic, then an email

or text will be sent for that duplicate record to.

So we need to have some additional logic in place if we want to enable deduplication in your Kafka streams

application.

So that's one of the limitations.

And Kafka transactions are not applicable for consumers that just read from the Kafka topic and write

the data into a database, because there is not going to be any Kafka producer involved in this whole

flow because the producer and transaction pretty much does the job of providing the exactly once feature.

The next one is what are the performance implications of enabling Kafka transactions?

So enabling transactions does add a performance overhead to the Kafka producer.

So the reason being there needs to be an additional call that will be made behind the scenes for you

to register the producer ID or generate the producer ID for that given transaction ID, That's number

one.

And number two was additional call to register the partitions to the transaction coordinator.

When we are about to write the data into the respective Kafka topics.

And the next one is transaction initialization and commit operations or synchronous, the app needs

to be wait and blocked until these operations complete on the consumer end.

The consumer needs to wait for the records until the transaction is complete and committed because of

the read committed configuration.

So if a transaction takes a longer time, then the wait period for the consumer goes a little up.

So these are the performance impacts when we enable Kafka transactions that you need to be aware of.

So this marks the end of the section, which is exactly once Processing and Kafka Streams app.

I hope you all have a pretty good idea about transactions item, potent producer and the behavior of

exactly once processing in Kafka Streams application.