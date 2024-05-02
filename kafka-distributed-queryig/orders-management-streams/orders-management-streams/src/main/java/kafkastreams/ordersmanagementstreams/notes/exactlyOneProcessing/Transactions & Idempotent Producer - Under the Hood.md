- In this lecture, I will explain the behavior of transactions and idempotent producer behind the scenes.

Let me start with the transactions first.

So this is our Kafka Streams application, which streams from the source topic.

We have the topology here, which takes care of processing the record and then writes the aggregated

results to the state store, and it also writes the data to the internal Kafka Changelog topic.

So when we enable the property which is processing guarantee equal to exactly once v2, this will take

care of enabling the transactions in our app.

And what I have here is a pseudocode of what's happening behind the scenes.

So behind the scenes there will be a call to begin transaction from the app during the startup.

This app will take care of creating a transaction ID for you and assign it to the app.

If you have multiple instances, then each app will get its own ID to facilitate transactions in Kafka

streams.

There will be a transaction coordinator, so during the application startup there will be a call made

from the app to the transaction coordinator.

So the transaction coordinator is nothing but one of the broker that's part of the Kafka cluster.

So in our local setup we just have one broker so that one broker will behave as a transaction coordinator.

So you can think of transaction coordinator as very similar to the consumer coordinator.

So the transaction coordinator maintains a special topic named Transaction State to manage the state

of the transactions.

So what it does is that it creates a producer ID that's what paid here mean.

And then it will also create an epoch which represents what time transaction and the producer ID and

the epoch got created.

So there is a concept tied to the epoch which I'll cover in a bit.

So once this step is completed, the consumer will start pulling for records, as you can see, right?

The consumer will start pulling records and the record will be read from the Kafka topic and then passed

on to the topology.

Once the app completes its processing, the next step is to start writing the data to the state store

and then to the output Kafka topics.

So in this case, what we have is a change log topic.

So this is the one which has the exact data, whatever that's represented in the state store.

And if we have a use case of publishing the aggregated data to the output topic, then that data will

be sent in this call and the next call is to commit the offsets to the consumer offsets topic.

This is to make sure our Kafka streams application process the records, and then it's going to commit

the record to the consumer offsets so that it won't read the record again.

So in this case, it's ready to write to all these topics.

But just before it writes to these topics, the app needs to inform the transaction coordinator about

the different partitions that the records are going to be produced into these topics.

Okay.

So in this case, as you can see, the data will be produced to the PS0, which is partition zero of

the change log topic and partition, one of the output topic and partition three of the consumer offset

topic.

So all these data will be sent to the transaction coordinator.

So transaction coordinator has a metadata about the Kafka topics and the partition that this particular

record is going to be produced to.

So once that step is completed, then the data will be produced to this appropriate Kafka topics.

As you can see, the data is produced to these Kafka topics.

I'm just using D as a identifier of data, so the data is successfully published.

So to make it really clear there will be a commit marker.

So that's where the commit transaction call will be made.

And this is a synchronous call which is going to make sure the commit marker is added to this transaction

coordinator.

So this is to make sure all the data that's been produced into this Kafka topic going to be committed.

So in this case we add the C as a commit marker from the application and then the same commit marker

will be added across all the related Kafka topics.

So this is how a transactions work behind the scenes.

All the work is taken care for you behind the scenes by the Kafka Streams library by just adding this

property in this case, which is the processing guarantee equal to exactly once V2.

So from a developer perspective, we don't even have to do any of these things.

All the heavy lifting is done for you behind the scenes by the Kafka Streams app.

So let's say you have a consumer app that's reading from this output topic and that also has transactional

data in it.

So the normal expectation of the consumer app reading from these kind of Kafka topic is that you need

to set the isolation level equal to read committed.

So when we enable the processing guarantee equal to exactly once processing for a Kafka Streams app,

it will automatically enable this property.

So now let's say this consumer app is not a Streams app, this is a regular consumer app.

So in this kind of scenarios you need to set this property value isolation level equal to read committed.

What this means is that it's going to receive the records that have the commit marker in it.

So transactions that are pending or yet to be committed won't be visible to the consumer until it is

committed.

So what we saw until now is a happy path.

I feel that there is a lot of information that I have shared as part of this lecture, if you have questions

or clarifications.

Then please watch this lecture one more time and don't hesitate to raise your clarifications or questions

in Q&A.

But in reality, if you think about it, there can be failures in this whole flow.

Let's discuss an error scenario in this whole flow.

Let's say after step four, in this case, once this data is sent over here and we have a pending transaction

waiting to be confirmed right by the transaction coordinator.

Now, let's say after this, after this request is sent, which is a produce call, the application

failed over here right now with the transaction coordinator.

What's going to happen is that this application is trying to come up.

Basically, if we are running in a Kubernetes environment, there is going to be a new pod that gets

created automatically for you and then the step one is going to continue again, right?

So because this is going to make the call to produce a process ID and the epoch, in this case, the

transaction coordinator will notice there is a pending transaction from the previous instances of the

application.

So the transaction coordinator will abort the previous transaction, as you can see in the exception

block, we have this abort call and then what it is going to do, it's going to add the abort marker,

and then the same thing will be added across all of the respective topics.

As you can see, all of the respective topics also have the abort marker.

So the epoch that gets generated along with the producer ID comes in handy to fence off any transaction

with an old epoch.

So in this case, our application restarted, right?

And that's one of the way for transaction coordinator to identify a previous transaction and then add

the abort marker.

Let's say for some reason the old instance did not completely shut down, it froze for a bit and then

it tries to make the call again to the transaction coordinator with the old epoch, but with a new epoch

which is greater than the old epoch.

So in that way it can fence off the old producer and completely reject the transaction.

So this is one of the scenario where error can comes into play and the way the transaction coordinator

is going to deal with it is by adding this abort markers.

I hope the explanation is clear about errors involved and the context of a transactions.

Now let me talk about the Idempotent producer.

So anytime we enable the processing or guarantee equal to exactly once we do the behavior of the producer

that's involved in the Kafka Streams app is idempotent.

What this means is that the producer is going to get a producer ID right as soon as the application

comes up by interacting with the transaction coordinator as a first step.

Now let's say the app is going to publish a message to partition zero for a topic.

So in this case a broker, one is a leader of the partition zero and the producer will attempt to send

the message to broker one.

Since broker two is a follower here, the data will be replicated once the data replication is done.

Just before communicating that to the app, the broker went down, let's say, and the data, as you

can see, that data will have a producer ID and then it also have a sequence number for each and every

message, right?

So in this case, the data is also replicated.

And for some reason the broker one went down just before communicating the record to the application,

which means the just before it was trying to send an acknowledgement to the application.

So in this case, the broker two is going to be the leader, right?

So from the follower, this is going to be switched to leader and then we have a retry is enabled right

by default.

So the producer is going to attempt to retry the same record.

So it will have the data and then it has the producer ID and then the same sequence number.

Since the data is already replicated over here, the sequence number is going to be already present

in this new leader for this partition P zero.

So in this case, the same message will be retried from the producer, but the message is going to be

rejected because message with the same producer ID and the sequence number is already present in the

topic Partition zero.

So the producer retries are not going to cause message duplicates with the behavior of idempotent producer.

So the key thing is the data is going to have process ID and then the sequence number.

So using that sequence number, the broker have a way to reject the duplicates.

So this is how the Idempotent producer works