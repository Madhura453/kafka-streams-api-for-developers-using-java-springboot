So if you go and take a look at the JsonSerde behind the scenes, what it does is that it uses the
enhanced object mapper and it dynamically registers different modules of object mapper based on what

that is available in the classpath.
```
public JsonSerde() {
        this((JavaType)null, JacksonUtils.enhancedObjectMapper());
    }

	 public static ObjectMapper enhancedObjectMapper() {
        ObjectMapper objectMapper = ((JsonMapper.Builder)((JsonMapper.Builder)JsonMapper.builder().configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).build();
        registerWellKnownModulesIfAvailable(objectMapper);
        return objectMapper;
    }

	 private static void registerWellKnownModulesIfAvailable(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JacksonMimeTypeModule());
        if (JDK8_MODULE_PRESENT) {
            objectMapper.registerModule(JacksonUtils.Jdk8ModuleProvider.MODULE);
        }

        if (JAVA_TIME_MODULE_PRESENT) {
            objectMapper.registerModule(JacksonUtils.JavaTimeModuleProvider.MODULE);
        }

        if (JODA_MODULE_PRESENT) {
            objectMapper.registerModule(JacksonUtils.JodaModuleProvider.MODULE);
        }
````

So if the JDK eight module is present, then it's going to register that module.

The reason why I'm explaining this is because our greeting domain, if you go and take a look at it,

it has a value which is timestamp, right?

Which is a local date time type.

And if you go and take a look at the messages that we have been producing so far, so have the consumer

up and running here.

The timestamp is displayed in a different format.

Okay.

So the format that we're expecting is this one diffrent

the timestamp to be in this our excepted format in order to make sure we publish the message in the expected time

format.

We need to provide a little bit of customization for the object mapper that is constructed by the JSON

served behind the scenes, .

So JsonSerde dynamically registers these things behind the scenes for you, but if you want to take

more control, we can still do that.

````
private static void registerWellKnownModulesIfAvailable(ObjectMapper objectMapper) {
````
here it's going to take the objectMapper that we are supplying instead of the one that it 
is automatically built by the JsonSerde class