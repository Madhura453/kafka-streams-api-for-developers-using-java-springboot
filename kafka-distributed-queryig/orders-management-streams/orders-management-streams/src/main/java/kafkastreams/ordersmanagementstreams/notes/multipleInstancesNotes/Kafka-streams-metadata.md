## what is metadata
-the techniques to have one instance, learn about the other Kafka streams instances that 
share the data
# why metadata required
- we saw that data is distributed across the other two instances. We need result from both the instances
# How to know
- we need to know is that each instance needs to know the address of the
other instance so that it can fetch the data from that other instance and aggregate the data and send
the response to the client.
````
  public MetaDataService(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }
````
we use this bean to access the Kafka streams instance
so that we can actually query the store to get the actual aggregated data
````
 KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
 ````
- So this is an instance which holds information about the other Kafka instances that are up and running
in the same server or different server
````
OrdersStreamsConfiguration
 streamProperties.put(StreamsConfig.APPLICATION_SERVER_CONFIG, 
 InetAddress.getLocalHost().getHostAddress()+":"+port);
 streamProperties.put(StreamsConfig.STATE_DIR_CONFIG,String.format("%s%s",applicationName,port));
````
- So the way they communicate with each other is using this particular configuration
````
 Collection<StreamsMetadata> metadataForAllStreamsClients = kafkaStreams.metadataForAllStreamsClients();
````
- this is the metadata information which has the information about the address, and it also has the
information about the port using which we can connect to that particular instance

````
  streamProperties.put(StreamsConfig.APPLICATION_SERVER_CONFIG, 
  InetAddress.getLocalHost().getHostAddress()+":"+port);
````
- From here you will all host information host address and port etc

http://localhost:8081/v1/metadata/all

````
[
    {
        "host": "127.0.0.1",
        "port": 8081
    },
    {
        "host": "127.0.0.1",
        "port": 8082
    }
]
````

http://localhost:8082/v1/metadata/all

````
[
    {
        "host": "127.0.0.1",
        "port": 8081
    },
    {
        "host": "127.0.0.1",
        "port": 8082
    }
]
````