So the only thing that we did in order to make sure the Spring boot application is going to work as

a Kafka Streams application is by adding this annotation=@EnableKafkaStreams.

But what happens behind the scenes when you add this annotation?

Let's quickly jump into the implementation of this annotation.

If you go to this, enable Kafka streams annotation.

````
So there is this art import class.
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({KafkaStreamsDefaultConfiguration.class})
public @interface EnableKafkaStreams {
}
````
So this is a class defaultKafkaStreamsBuilder which is responsible for creating the Stream Builder factory bean that I talked about

in the reference section.(https://docs.spring.io/spring-kafka/docs/3.0.14-SNAPSHOT/reference/html/#streams-kafka-streams =Spring Management)

in 
```
KafkaStreamsDefaultConfiguration.class

public StreamsBuilderFactoryBean defaultKafkaStreamsBuilder(@Qualifier("defaultKafkaStreamsConfig") ObjectProvider<KafkaStreamsConfiguration> streamsConfigProvider, ObjectProvider<StreamsBuilderFactoryBeanConfigurer> configurerProvider) {
KafkaStreamsConfiguration streamsConfig = (KafkaStreamsConfiguration)streamsConfigProvider.getIfAvailable();
if (streamsConfig != null) {
StreamsBuilderFactoryBean fb = new StreamsBuilderFactoryBean(streamsConfig);
configurerProvider.orderedStream().forEach((configurer) -> {
configurer.configure(fb);
});
return fb;
} else {
throw new UnsatisfiedDependencyException(KafkaStreamsDefaultConfiguration.class.getName(), "defaultKafkaStreamsBuilder", "streamsConfig", "There is no 'defaultKafkaStreamsConfig' " + KafkaStreamsConfiguration.class.getName() + " bean in the application context.\nConsider declaring one or don't use @EnableKafkaStreams.");
}
}
```

the Stream Builder Factory Bean. This is the one which is responsible for supplying you the stream Builder, right?

So if you go to the code again, so this Kafka streams default configuration.

As you can see, this is the one which creates this stream Builders factory bean for you.

Okay.

And the StreamsBuilderFactoryBean takes in something called the streamsConfig.

StreamsBuilderFactoryBean fb = new StreamsBuilderFactoryBean(streamsConfig);

So where do we get this information from?

So we get this information from the stream config provider.

KafkaStreamsConfiguration streamsConfig = (KafkaStreamsConfiguration)streamsConfigProvider.getIfAvailable();

So if you go to the streamsConfigProvider it is of type KafkaStreamsConfiguration.

ObjectProvider<KafkaStreamsConfiguration> streamsConfigProvider

Okay.

And where does this come from?(search for KafkaAutoConfiguration)

So this comes from another class named KafkaAnnotationDrivenConfiguration.

I'll quickly show you that.

So if you go to this class, you will notice something called KafkaAutoConfiguration if you go here.

So we have this KafkaAnnotationDrivenConfiguration.

So this is actually responsible for supplying this streams configuration.

public class KafkaAutoConfiguration {

	private final KafkaProperties properties;

	KafkaAutoConfiguration(KafkaProperties properties) {
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean(KafkaConnectionDetails.class)
	PropertiesKafkaConnectionDetails kafkaConnectionDetails(KafkaProperties properties) {
		return new PropertiesKafkaConnectionDetails(properties);
	}

	@Bean
	@ConditionalOnMissingBean(KafkaTemplate.class)
	public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory,
			ProducerListener<Object, Object> kafkaProducerListener,
			ObjectProvider<RecordMessageConverter> messageConverter) {
		PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
		KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
		messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
		map.from(kafkaProducerListener).to(kafkaTemplate::setProducerListener);
		map.from(this.properties.getTemplate().getDefaultTopic()).to(kafkaTemplate::setDefaultTopic);
		map.from(this.properties.getTemplate().getTransactionIdPrefix()).to(kafkaTemplate::setTransactionIdPrefix);
		return kafkaTemplate;
	}

So if you go to the KafkaStreamsAnnotationDrivenConfiguration, so it creates this particular bean.
````
@ConditionalOnMissingBean
@Bean(KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
KafkaStreamsConfiguration defaultKafkaStreamsConfig(Environment environment,
KafkaConnectionDetails connectionDetails) {
Map<String, Object> properties = this.properties.buildStreamsProperties();

	}

````

With this name( DEFAULT_STREAMS_CONFIG_BEAN_NAME=public static final String DEFAULT_STREAMS_CONFIG_BEAN_NAME = "defaultKafkaStreamsConfig";

, the name is going to be default Kafka streams config, and if you take a look at it,

it returns an object named return new KafkaStreamsConfiguration(properties);

It does this by reading the properties, and if you go and take a look at the  KafkaProperties.

So this one is going to have this spring Kafka configuration properties and it reads all the properties.

We have the consumer properties and we have the producer properties and we have the stream properties

also.

Okay, so this is where the application that we supplied is going to be added to it.

And the bootstrap server that we supplied is going to be added to it.

If you open the application.yml, you might be able to relate what I am talking about.

So in the application.yml, so we have supplied the bootstrap servers and application ID,

all those things get added to it.

So another thing is like the application got started as soon as we started our spring boot application

because by default the auto startup property is set to true.

That means if the spring application detects a topology, then it's going to automatically start up

that application.

So this is something which happens behind the scenes for you and this is the one which is responsible

for creating that stream builder instance.

So if you go to the StreamsBuilderFactoryBean, what does it mean?

It extends another class named extends AbstractFactoryBean<StreamsBuilder> of type stream Builder.

And if you go to this one AbstractFactoryBean, so this is basically a generic class, but this class is going to supply

any type that you pass to it.

So in this case, we are supplying the StreamsBuilder and this is how the StreamsBuilder bean gets injected

into the context.

```
StreamsBuilderFactoryBean defaultKafkaStreamsBuilder(@Qualifier("defaultKafkaStreamsConfig")

public class StreamsBuilderFactoryBean extends AbstractFactoryBean<StreamsBuilder>
```

And that's how we are building the topology right?

If you all can recall in our topology class, we just use the auto wired and then pass the stream builder

as a function argument.

It takes care of automatically injecting that stream builder been that's available in the context to

this particular function.

And then we were able to build the topology on top of it.






- Adding the annotation @EnableKafkaStreams is going to invoke the **KafkaStreamsDefaultConfiguration** class
    - **KafkaStreamsAnnotationDrivenConfiguration** supplies the **KafkaStreamsConfiguration** bean
    - This class takes care of building the **StreamsBuilderFactoryBean** which is responsible for supplying the StreamsBuilder instance.
        - This **StreamsBuilderFactoryBean** class also takes care of managing the Lifecycle of the **KafkaStreams** App.
        - And this is the one which takes care of managing the whole lifecycle of the Kafka Stream app to basically
      taking care of shutting it down. When you stop the application, taking care of starting it up, 
        when you start up the spring boot application


