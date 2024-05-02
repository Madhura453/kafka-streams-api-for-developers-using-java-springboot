- Data delays are a common concern in streaming applications, so the delay in event could be because
  the app that's producing the event is down or in the worst case scenario, the Kafka broker could also
  be down.In both of these scenarios, there is a possibility to experience a data delay

# Example
- The first window starts from 8 a.m. and ends at 8:01 a.m. and the second window starts from 8:01 a.m.
 and 8:02 a.m. So let's assume that we have few events received in these times.(Look at PDF)
- One at 8 a.m., 44 second.

Another one is at 8 a.m. and 54th second for the first window.

And for the second window we have another event at 8:01 a.m. at fifth second.

Now, what we are going to get is a delayed event.

If you take a look at this event, this event's timestamp is 8 a.m. and 58 second, which means this

event is supposed to come in the first window because the first Windows timestamp ends at 8:01 a.m.

But for some reason this event got delayed from the app that's producing it and it and it reached the

Kafka cluster with some delay.

So in these kind of scenarios, the common behavior is to ignore these events and then move on to process

the next event.

And this is how the windowing concept works in Kafka streams.

Let's say if you want to still consider this event into the appropriate window.

So in this case, this is supposed to be part of the 8 a.m. to 8:01 a.m. time window, right.

If you want to have a behavior like this.

So this is where the grace period comes into rescue.
````
Duration windowSize = Duration.ofSeconds(60);
Duration graceWindowsSize = Duration.ofSeconds(15);
TimeWindows hoppingWindow = TimeWindows.ofSizeAndGrace(windowSize, graceWindowsSize);
````
Let me quickly show you how to define a grace period and then how it impacts the overall windowing logic.

So this is how we define a grace period.

In this case, you have the actual window size and followed by the grace window size and use the function

name time, windows, dot of size and grace.(TimeWindows.ofSizeAndGrace(windowSize, graceWindowsSize);)

And you pass these two windows as part of that actual call.

And what this means is that so in this case, let's say the next event, the delayed event comes within

the 15 second window, right?

So in this case, the 15 second window is a grace window size.

If you receive a delayed event within the 15 second window, then that event will be considered into

the actual window that it needs to be part of.

So this is how the grace period works.

````
 Duration windowSize = Duration.ofSeconds(60);
        Duration graceWindowSize = Duration.ofSeconds(15);
        TimeWindows timeWindows = TimeWindows.ofSizeAndGrace(windowSize, graceWindowSize);
````
- So what this means is that any delayed event, if it falls within the 15 second time window in the following
window, then this will be considered to be part of the actual window that it is supposed to be part of.

# Testing

So what this means is that any delayed event, if it falls within the 15 second time window in the following

window, then this will be considered to be part of the actual window that it is supposed to be part

of.

Okay.

So now with this code, what I'm going to do, I'm going to restart the application.

But before I do this, what I'm going to do, I'm going to restart my whole Kafka cluster.

So in this case, I'm going to go to the Kafka cluster.

As you can see, I brought this down already just to save some time.

All you got to do is run the docker, compose down command.

That should take care of bringing the application down.

The next thing, what I'm going to do, I'm going to call Docker, compose up.

This should bring our application up.

Once this is up, what we can do is we can go ahead and bring our Kafka Streams application.

I'm going to pause the video for a bit.

There we go.

The Kafka cluster is up now.

What we will do, we'll go back to the application.

So in this case, let's go back to the Kafka Streams application and then start this one.

So in this case, I'm going to go to the orders management stream application and then start this one.

So this should take care of starting up the application in our local.

There we go.

The application is up and running.

Let's go ahead and search for the running text.

There you go.

The application is ready before I go ahead and start testing this because we need to go to the producer.

I've added some test data into our orders mock data producer, which means I've added some functions

that's going to help test this behavior.

So now if we go and take a look at it, you will see the section where just above the publish orders

to test Grace.

So what we need to do in order to test this grace period is that we are going to follow the instructions

that are given over here.

Number one is we are going to be executing the publish order function.

So which is basically this one.

Okay?

And during that time we need to have this  publishOrdersToTestGrace(objectMapper, buildOrdersToTestGrace()); one commented out, okay,
what we are going to do, we are going to have the data publish in a certain window, right?

So our window topology, if we go and take a look at it for the count, it is 60 seconds.

Now what I'm going to do, I'm going to start publishing the data in the start of the window.

And this.

It could be starting from the zero one second.

Okay.

So I'm going to publish some data and then I'm going to wait until the actual time goes to the next

window.

In this case, the following window is going to be the next 62nd window.

So if you go to the Smart data producer and if you go to the build orders disgrace function.

So in this function, what I'm going to do, I'm going to hardcode the actual timestamps over here.

Okay?

So that's what I'm going to do.

So we need to match the times in our local machine so that it will be easy to test.

So in this case, what I'm going to do, I'm going to use this time utility.

And as you can see, the time currently is 842.

So I'm going to have this test run at 845.

So in this case, this window time frame is going to be eight 4558 So let me give that value all over

the place.

845 4058, 845, 58 and eight 4558 So this function will execute at the eight.

I forgot to change this time to eight.

It was still pointing to 5 a.m. so let me change it to eight.

Let me change this to eight.

What we are going to do is at the eight 45th minute.

So I'm going to start publishing the data.

But during the time frame, I won't be invoking any call to this function.

So in this case, this function is going to be commented out.

So in this case, at 845, regular windows are going to be created.

So the build orders, as you can see, it, is just going to publish one order for each store with the

type one type as general, another one is going to be resident.

And the next eight 46th minute, I'm going to be calling this function.

Okay, let's go to the top.

So for now, have this commented out.

We'll wait until this time becomes 845, because if we go and take a look at the build orders to Grace

function, this function is going to hold the eight 4558 timestamp, but this is going to go at the

eight 46th minute actually.

So at the eight 46 minute we are going to publish this, but we need to publish this before the 846

15 seconds.

So I'm going to wait until this becomes 840 500 and then launch this order.

Smart data producer And to test this out, what we can do, we can use a curl commands that we have

over here.

So in this case, if you go here, you can open the curl commands.

So in here, all we got to do is invoke this orders.

Okay, The time is at 845, a little over 845.

We can start publishing this, go and execute this.

This is going to take care of publishing the data and our windowing logic is going to happen in our

case, if we go to the streaming application logs.

So you might have received all these events or now if you hit the curl commands endpoint.

So in this case this is our endpoint.

So we have built this endpoint which is orders Windows count.

If we press enter, you should be able to see some data over here.

So in this case we have the windows created for 845 and 846 and you can see each and every order count

is one over here.

Okay?

So I'll wait until this becomes 846 and then I'm going to publish orders for both of these windows.

So this is going to publish with a timestamp of 846, but this one is going to have the timestamp as

eight 4558 So we'll wait until this goes a little over 846 and then we'll publish this one.

So currently the order count is one as you can see.

So now I'm going to publish this one.

So currently we just have one window.

So once this is published, so all the records are published before the 15 second.

Now if I hit the end point, let's go and hit the end point.

You will see the order count value updated to two.

So in this case, so for this particular window, let's copy this one.

Right.

So for this window, the order count was one.

And then we published this grace function, which is going to hold the timestamp as eight 4558 right.

Which is before window.

The question is, did it update the order count or not?

If we go a little bit down, the order count got updated to two.

So basically the order count was one.

And even though we published an event with a delay which went in the next window, which is 846, which

is local time, but in reality it is 1446.

So there are windows created for that one.

But the key thing is we were able to get the updated value of this ordered count.

So in this case, the order count is updated to for all the events, even though we had a data delay.

So this is only possible because we have added the grace period over here.

Okay, so now what I'm going to do, I'm going to still publish more data, but you will notice a different

logger in the console because this record that we are going to publish with the Grace period, that's

going to be way older, right?

Which is actually way older than the 15 second grace period that we have provided.

So in this case, let's go ahead and execute this function one more time.

In the logger, you will notice the records are being skipped.

So if you go to the orders management stream application, you can see we have a lot of records that

are skipped because they are outside the which means the data is delayed beyond the grace period that

we have provided.

So we have a lot of records that are ignored.

So this is a clear signal.

Grace period only considers the record with a delay timestamp if it's just within the 15 second, even

though we pass the timestamp as two second before because that's a delay rate, it can go until the

15 second.

That should still be considered for the actual time window.