- So far we have been running a single instance of the Kafka streams application.
  What this means is that all the data that is read from the source topic is completely processed by the
  single instance. This means you have one single instance has the complete data of orders.

- But in today's day and age of software development, we normally run the applications in the cloud,
  which also means that it's pretty common to run multiple instances of the same application to support
  high availability and scalability.
- So when you have a setup like this(muliple instances running), what will happen is that so the data is
  going to be split across these instances of the application.So in this case, some of the challenges we might
  run into are we need to get the data from both the instances to look at the overall data.
- So in this case, let's say, for example, this is a spring boot application.
  We are running it in our local and these are the different running ports, Port 8080 and port 8081.
  Now we are going to initiate a call to retrieve the count of the orders by the order type.
  So now we are initiating the call to retrieve the count of the orders by the type general orders.
  So in this case, what we are going to do, the app is going to make the call to the port 8080, because
  that's the URL that's given in the curl command.
  And this is the first step followed by that a call needs to be made to the other instance of the app
  that's running in Port 8081 to get the complete view of the data.
  The reason this is needed is because the data that's tied to the general order type is located in
  both the instance of the app.
- So in order to facilitate this functionality, we have some challenges to tackle.
  Number one is we need to know the machines, address and port where the other instances of the Kafka
  streams application is located.
  Number two is we need to aggregate the data from multiple instances.
  So this requires us to build rest clients using which we can fetch the data from the other instances,
  and then we can aggregate the data.

