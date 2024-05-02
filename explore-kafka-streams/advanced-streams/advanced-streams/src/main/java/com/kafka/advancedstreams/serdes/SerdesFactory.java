package com.kafka.advancedstreams.serdes;

import com.kafka.advancedstreams.domain.Alphabet;
import com.kafka.advancedstreams.domain.AlphabetWordAggregate;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class SerdesFactory {

    public static Serde<AlphabetWordAggregate> AlphabetWordAggregateSerde()
    {
        JsonSerializer<AlphabetWordAggregate> jsonSerializer=new JsonSerializer<>();
        JsonDeSerializer<AlphabetWordAggregate> jsonDeSerializer=new JsonDeSerializer<>(AlphabetWordAggregate.class);
        return Serdes.serdeFrom(jsonSerializer,jsonDeSerializer);
    }

    public static Serde<Alphabet> alphabetSerde()
    {
        JsonSerializer<Alphabet> jsonSerializer=new JsonSerializer<>();
        JsonDeSerializer<Alphabet> jsonDeSerializer=new JsonDeSerializer<>(Alphabet.class);
        return Serdes.serdeFrom(jsonSerializer,jsonDeSerializer);
    }
}
