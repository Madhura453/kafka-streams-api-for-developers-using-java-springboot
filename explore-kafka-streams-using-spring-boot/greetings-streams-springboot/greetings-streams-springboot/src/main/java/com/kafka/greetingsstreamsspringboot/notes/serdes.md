````
 Consumed.with(Serdes.String(),
                                new JsonSerde<>(Greeting.class)
````
The above new JsonSerde<>(Greeting.class)

- So this is a class which takes a specific type, and then it's going to automatically apply the serialization
and deserialization to pass that appropriate type
````
  public class JsonSerde<T> implements Serde<T> {
  private final JsonSerializer<T> jsonSerializer;
  private final JsonDeserializer<T> jsonDeserializer;

````
- see above in java section we manually provide. Now spring provide us.