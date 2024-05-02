This is the typical behavior of a Kafka streams application.

The first step The app consumes the records from a source.

Topic number two, it processes the records.

And this is where the data enrichment or aggregate or data joining happens, and then it updates the

state store.

The next step is to publish the records to an internal topic or if there is a downstream system that

looks for this data, then the results will be published to an output topic.

The next step is to commit the offsets.

So once we publish the data into the output or internal topic, you'll get an acknowledgment that data

is successfully published and the next step is to commit the offsets so that the app won't read the

same record again.

So this is the happy path where everything works as expected.

Now I would like to talk about some of the failure scenarios that can happen in this flow.

Let me start with the error scenario Number one, there is an app crash after the step three.

So in this case, after the Step two completes, we successfully write the aggregated results to an

internal Kafka topic or the downstream output topic.

So in this case, the app received an acknowledgement that the record was produced successfully.

But just after the app received the acknowledgement message, the app crashed down.

So in this case, step four committing the offsets won't happen when the application comes up again.

So in this case, what will happen is that the same record is going to be processed by our app and a

duplicate record will be published into the internal Kafka topic or the output topic.

And your state store is also going to read the same message again and update the state store accordingly.

So this is a scenario where we reprocess the same record and this ends up in duplicate writes.

And also the state store also gets updated twice.

So the next scenario is error number two duplicate writes due to network partition.

Let's go through the same scenario again.

So in this case we produce the output of the Kafka topic and the data is written successfully into the

layer partition and replicated successfully to the followers.

But just before sending the acknowledgement, the connection was not stable and the call timed out,

but the records were successfully added to the Kafka topic.

So in this case, the producer is going to retry sending the same record again because by default,

the retry value is greater than zero.

This ends up sending the duplicate record to the output topic or the internal state store Kafka topic.

So in this scenario, we end up with duplicates again, even though we didn't process the same record

through the whole Kafka streams logic, we end up writing the duplicates into the output topics.

So duplicate processing and having duplicates produced to the output or internal Kafka topic or updating

the state store twice is a big problem in Kafka Streams application.

But before even we go further, what are the issues that can happen if we have duplicate?

Now the next big question is are there any issues with duplicates?

Well, it depends upon the use case that we are dealing with.

Let me start with some use cases where the duplicates are not allowed because it may cause some issues

to the application that we are building.

So if your application is finance related, right, So and then duplicate processing may cause confusion

and issues.

Let's say if you are building a banking related app, then we need to make sure we are debiting or creating

the right amount because you don't want to debit or credit twice for a single transaction.

Another example is if you are building an app that is about aggregating revenue, then you have to make

sure you are processing the event only once.

So these are the scenarios where you need to make sure you are processing the record exactly once.

Now let's talk about use cases where duplicates are allowed or okay.

To have an example would be aggregating the number of likes for a post or a video.

Having one or more extra likes is not going to cause any business impact.

Another example is counting the number of viewers on a live stream event.

Having one or more extra actual number is not going to cause any confusion or impact.

So as I mentioned in the beginning of this slide, having duplicates or processing the same record twice

is basically depends upon the use case.

Now, in this slide, I'm going to cover how to achieve exactly once processing in a Kafka Streams app

in order to make sure we are not going to be producing duplicate records in the event of a retry because

of a network partition, we need to configure our producer to be idempotent.

This means the retries shouldn't cause duplicate writes to the destination topic.

So this is number one.

Number two was transactions.

This means producing the record to an internal topic and output topics and committing the offsets should

happen together.

Atomically.

So this means all or nothing behavior, which is nothing.

But all of these operations should happen together and succeed or nothing happens.

So this is equivalent to the transactions that we do in a DB that writes to multiple tables, but the

transactions concept is a little bit tricky.

In a distributed system like Kafka, we will dive really deeper into understanding how these concepts

work in the following lectures.

But at a high level, we need these two features in order to enable exactly once processing in Kafka

streams.