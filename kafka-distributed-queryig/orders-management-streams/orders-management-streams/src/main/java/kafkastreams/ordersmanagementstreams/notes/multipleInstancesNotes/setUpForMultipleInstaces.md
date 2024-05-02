````
 streamProperties.put(StreamsConfig.APPLICATION_SERVER_CONFIG, 
 InetAddress.getLocalHost().getHostAddress()+":"+port);
````
- So this is one of the config which we need to set up to make sure what the address of this particular
instance is going to be.
So in this case, let's go ahead and set up the address of this particular application.
So you have two options.
You can either use a host name or you can use a host address.
So in this case, since we are running it in local, what I'm going to do, InetAddress.getLocalHost().getHostAddress()
because since we are running in localhost I'm going to get the address from the localhost.
What this is going to provide.
This is going to provide the IP address. Okay.
And then you need to provide the port also.
So because this is a spring boot application, if you have two instances up and running, you need to
know which port you need to interact with.
So if you have two instances, if the request comes to this one, we need to make sure we are able to
communicate with the other instance to get the complete data.
So in our case, the address is going to be the same localhost
The port is a one which is going to be different from one another.

- we need run 2 instances on 2 different port.

# Caused by: org.apache.kafka.streams.errors.StreamsException: Unable to initialize state, this 
#can happen if multiple instances of Kafka Streams are running in the same state directory 
#(current state directory is [C:\Users\-\AppData\Local\Temp\kafka-streams\orders-kafka-streams]
# at org.apache.kafka.streams.processor.internals.StateDirectory.initializeProcessId(StateDirectory.java:191)

way to Resolve the error
- we can provide the application name and port value. So that's going to uniquely identify each and every instance
````
 streamProperties.put(StreamsConfig.STATE_DIR_CONFIG,String.format("%s%s",applicationName,port));
````
- this is going to uniquely identify a state directory as orders. Kafka streams 8080 and orders Kafka streams 8081

````
java -jar D:\kafka-streams-API-Projects\distributed-queryig\orders-management-streams\target\orders-management-streams-0.0.1-SNAPSHOT.jar --server.port=8081
````

For this  state.dir = orders-kafka-streams8081

````
java -jar D:\kafka-streams-API-Projects\distributed-queryig\orders-management-streams\target\orders-management-streams-0.0.1-SNAPSHOT.jar --server.port=8082
````
Fir this  state.dir = orders-kafka-streams8082
````
TopicBuilder.name(OrdersTopology.ORDERS_TOPIC)
                .partitions(2)
````
- another important thing is that the orders Kafka topic that we are dealing with has two partitions.
So that means there is going to be two tasks that are going to be created or more than two tasks that
are going to be created
- So in this case, if you have two instances of the application, the tasks that are created will be
  split across these two instances
- And if you search for tasks over here.
o in this case, let's search for task.
The new active task with the value is
````
  New active tasks: [2_1, 3_1, 4_1, 5_1, 0_1, 1_1]
  
````
These are different tasks, but all these tasks are assigned to the partition 1

````
 New active tasks: [1_0, 2_0, 3_0, 4_0, 5_0, 0_0]
````
These are different tasks, but all these tasks are assigned to the partition 0

- when you have multiple instances, you have task split.
Basically we are introducing parallelism into the actual applications workflow

# After publishing orders what the behaviour

for 8082 port when publishing 4 orders it was split into 2 instances

[orders]: 100, Order[orderId=100, locationId=store_1234, finalAmount=15.00, orderType=RESTAURANT, orderLineItems=[OrderLineItem[item=Pizza, count=2, amount=12.00], OrderLineItem[item=Coffee, count=1, amount=3.00]], orderedDateTime=2024-02-01T22:22:56.694725600]
[orders]: 100, Order[orderId=100, locationId=store_4567, finalAmount=27.00, orderType=RESTAURANT, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-02-01T22:22:56.694725600]
For 8081 port

[orders]: 12345, Order[orderId=12345, locationId=store_1234, finalAmount=27.00, orderType=GENERAL, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-02-01T22:22:56.694725600]
[orders]: 12345, Order[orderId=12345, locationId=store_4567, finalAmount=27.00, orderType=GENERAL, orderLineItems=[OrderLineItem[item=Bananas, count=2, amount=2.00], OrderLineItem[item=Iphone Charger, count=1, amount=25.00]], orderedDateTime=2024-02-01T22:22:56.694725600]

# I want to have the data aggregated for the general order. How to see ?

http://localhost:8082/v1/orders/count/general_orders
[
{
"locationId": "store_1234",
"orderCount": 1
}
]


http://localhost:8081/v1/orders/count/general_orders


[
{
"locationId": "store_4567",
"orderCount": 1
}
]


- But as a whole, when I make the call, I want both the data,So in that case, we need to be 
making the call from one instance to the other and other instance to the this one.
- currently the data is just fetched from 8081 particular instance, it's not making the call to 8082
  instance and get the data and aggregating the data because we are not passing a location ID or something
  like that
  http://localhost:8081/v1/orders/count/general_orders
  We are just issuing a general orders type as part of the Curl command(request).
  It should give you the data for both.
  Since the data is distributed, you need to be building a rest client that's going to be making the
  call and then build the aggregate logic to aggregate the data and then send that as a response.
So irrespective of which instance you make the call, it should give you the complete view of the data.
So in this case, we are dealing with two locations.
Both of the locations data should be sent as a response.(we will do in next lectures)
Kafka streams run as multiple instances so you get the
data distributed across those two instances.
You need to combine both the data.
Anytime you issue a request to view the complete data.








