additional properties when we launched into ExactlyOneProcessingController

````
 properties.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG,StreamsConfig.EXACTLY_ONCE_V2);
````
ProducerConfig values:
transaction.timeout.ms = 10000
transactional.id = grretins-app-68f02561-37eb-4de4-adc2-ba3d148972b7-1

- So transaction ID is primarily important for both of the behaviors in this case, which is the Idempotent

producer and then the transactional behavior that's going to make sure we're going to handle the writing

to the internal and output topics.

And then the commit offset part happens as a single atomic transaction.

So this gets enabled as soon as you add this property.

And the next thing is if you go and take a look at the enable Idempotence property.

So this is a producer config.

As you can see, this will be part of the producer config and this gets set to true.
````
enable.idempotence = true
````

So this is the one which is going to help us with making sure when the producer re tries the same record,

it's not going to end up as duplicates in the destination topic.

Okay.

And then let's go ahead and take a look at the processing guarantee.

StreamsConfig values:
````
processing.guarantee = exactly_once_v2
````

Processing dot.

Guarantee.

And this value is the one which we are setting up over here.

So from an implementation standpoint, like how do we enable the exactly once processing?

All you got to do is you just need to add this property and the exactly once processing behavior is

automatically enabled for your application.
