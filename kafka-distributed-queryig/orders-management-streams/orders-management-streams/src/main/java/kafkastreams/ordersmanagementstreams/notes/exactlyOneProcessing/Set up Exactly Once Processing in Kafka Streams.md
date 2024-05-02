- Let me start with a quick recap from the previous lecture before we proceed further to achieve exactly

once processing in Kafka streams, we need this behavior.

Number one is we need to have an idempotent producer.

This means retries shouldn't have duplicate rights in the destination.

Topic number two is we need to enable transactions.

This means producing the data to the internal topic and to the result output topic and committing the

offsets should happen together as one single atomic operation.

So this is necessary to enable all or nothing behavior.

So the next question is how do we implement them in a Kafka Streams application?

Enabling them is one of the very simple process in a Kafka Streams application.

All you got to do is you need to pass the processing dot guarantee property with the value as exactly

once v2 and this is a config value for the latest version of the Kafka streams application.

The idea is that to keep this behavior very simple for the developers to enable or disable it by enabling

this, this is going to guarantee you are going to get the exactly once behavior that I talked about

in the previous lecture, the Kafka Streams library that we are using to build the Kafka streams application.

Does the heavy lifting behind the scenes in order to enable the exactly once processing in Kafka streams.

With this information in place, let's go ahead and enable this property in our Kafka Streams application.

So I'm going to go back to IntelliJ, and here I am in the initial app that we have been working on

so far.

And then I'm going to go to the Greetings Streams application.

So if you go to the launcher package and then if you open the Greetings Stream app.

So in here what we are going to do is we are going to be adding a property.

So I'm going to add properties dot put and the property name is going to be streams config dot processing

guarantee config.

So this is a config which we are going to set the value for.

So the value is processing dot guarantee.

So for this property we are going to pass the exactly once v2.

So in this case I believe this is part of the streams config class itself.

So all you got to do is pass this one.

So this should take care of setting up this value for you.

So as you can see in here, this is for exactly once processing guarantees.

Enabling exactly once requires a broker version to be 2.5 or higher.

Okay.

So now if we go back, let's add the semicolon over here.

And this is all you need in order to enable the exactly once processing configuration in your Kafka

Streams app.

But there are a lot of internal work that happens behind the scenes, which I will cover in the next

lecture.

But for now, this is all you need in order to enable the exactly once processing in your Kafka streams

application.

So now let's quickly start this application.

Before we do that, I want to make sure my local Kafka cluster is up and running.

As you can see, I have the local Kafka cluster up and running.

So now let's go back to our IntelliJ and then start this application.

I'm going to show you a couple of interesting properties that get set because of this property in the

application console.

So in this case, our application is getting started.

As you can see, the application is in running status.

So this is a clear signal that our application is up and running.

So in here, the very first property that I'm going to show you is let's go to the streams config.

So in the stream's config, if you go and take a look at the property named transactional ID, I believe

it will be part of the producer.

So as you can see the transaction ID, right?

So this is something which gets enabled whenever you enable this processing guarantee to be exactly

once because by default it is always at least once by setting this up, this is going to make sure the

transaction ID gets added to it.

So transaction ID is primarily important for both of the behaviors in this case, which is the Idempotent

producer and then the transactional behavior that's going to make sure we're going to handle the writing

to the internal and output topics.

And then the commit offset part happens as a single atomic transaction.

So this gets enabled as soon as you add this property.

And the next thing is if you go and take a look at the enable Idempotence property.

So this is a producer config.

As you can see, this will be part of the producer config and this gets set to true.

So this is the one which is going to help us with making sure when the producer re tries the same record,

it's not going to end up as duplicates in the destination topic.

Okay.

And then let's go ahead and take a look at the processing guarantee.

Processing dot.

Guarantee.

And this value is the one which we are setting up over here.

So from an implementation standpoint, like how do we enable the exactly once processing?

All you got to do is you just need to add this property and the exactly once processing behavior is

automatically enabled for your application