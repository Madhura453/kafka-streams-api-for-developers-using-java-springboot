-Look into PDF
- Let's assume that we have two instances of the Kafka streams application and each instance holding the
copy of the streams metadata, which holds the information about the other Kafka stream instances.
What do we mean by that?Is that so?

The Kafka Stream instance one has the copy of the streams metadata.

This has information about the address and the port to find the other instance.

In this case it is instance one must knows the instance two.

I hope the explanation is clear in order to aggregate the data.

1) The first step here is that the client will initiate a call to one of the instance.

In reality, there might be a load balancer which takes care of routing the traffic to the appropriate

instances.

But in here we have a very simple setup where we are going to make the call to the instance directly

by using the port.

So in this case, if instance one is port 8081, then the client will make the call to the port 8082.

Once the call is made, then the app will use the streams metadata to learn about the other Kafka stream

instances.

So in this case it's going to be just instance two.

In reality, if we have more, the streams metadata will have information about all other instances.

Once it knows about the instances, it will make a call over the network to get the related data.

We'll build a rest client to facilitate this particular call.

Once a call is received by the other instance, it will provide the necessary data as a response.

Let's say if you are performing a retrieve order, count by order type, then in that case you can get

that particular information through that rest call.

Note:
And one important Thing is that this particular instance should not make the call to the other instances,

because if that happens, this will end up making call in circles and the calls will never complete.

So we need to make sure that no network call is made from the instance two the other instances, because

instance one is basically the orchestrator, which orchestrates a call to all other instances, fetch

the data and aggregate the data.

Okay.

4) So step four is to aggregate the data once a response is received from the other instances.

And the last step is to send the aggregated data or the combined data as a response to the client.

So basically this is the end to end flow of how this whole thing is going to work

When you have multiple instances of the Kafka streams up and running.


Now let me quickly highlight the different tasks that are needed to make this work so we can use the

Kafka Streams instance from the Stream Builder Factory Bean to find out the metadata information about

the other Kafka streams instances.

Right.

The next step is to build a rest client to interact with the other Kafka stream instances.

I'm going to be using a spring web client, which is a rest client using which we can make the call

to the other instance.

The next thing is we need to build the logic to aggregate the data, right?

Because this is needed when we have to send the complete view of the data to the client request.

So these are the three steps that's needed in order to implement the logic that we are looking for