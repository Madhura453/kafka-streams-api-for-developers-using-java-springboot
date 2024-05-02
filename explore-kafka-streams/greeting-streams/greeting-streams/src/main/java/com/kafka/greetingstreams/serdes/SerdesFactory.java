package com.kafka.greetingstreams.serdes;

import com.kafka.greetingstreams.domain.Greeting;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class SerdesFactory {

    public static Serde<Greeting> greetingSerde()
    {
        return new GreetingSerdes();
    }
    static public Serde<Greeting> greetingSerdeUsingGenerics()
    {
        JsonSerializer<Greeting> jsonSerializer=new JsonSerializer<>();
        JsonDeSerializer<Greeting> jsonDeSerializer=new JsonDeSerializer<>(Greeting.class);
        return Serdes.serdeFrom(jsonSerializer,jsonDeSerializer);
    }
}
