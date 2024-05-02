package com.kafka.orderskafkastreamsapp.serdes;

import com.kafka.orderskafkastreamsapp.domain.Order;
import com.kafka.orderskafkastreamsapp.domain.Revenue;
import com.kafka.orderskafkastreamsapp.domain.Store;
import com.kafka.orderskafkastreamsapp.domain.TotalRevenue;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class SerdesFactory {
    public static Serde<Order> orderSerdes()
    {
        JsonSerializer<Order> jsonSerializer=new JsonSerializer<>();
        JsonDeSerializer<Order> jsonDeSerializer=new JsonDeSerializer<>(Order.class);
        return Serdes.serdeFrom(jsonSerializer,jsonDeSerializer);
    }

    public static Serde<Revenue> revenueSerdes()
    {
        JsonSerializer<Revenue> jsonSerializer=new JsonSerializer<>();
        JsonDeSerializer<Revenue> jsonDeSerializer=new JsonDeSerializer<>(Revenue.class);
        return Serdes.serdeFrom(jsonSerializer,jsonDeSerializer);
    }
    public static Serde<TotalRevenue> totalRevenueSerdes()
    {
        JsonSerializer<TotalRevenue> jsonSerializer=new JsonSerializer<>();
        JsonDeSerializer<TotalRevenue> jsonDeSerializer=new JsonDeSerializer<>(TotalRevenue.class);
        return Serdes.serdeFrom(jsonSerializer,jsonDeSerializer);
    }

    public static Serde<Store> storeSerdes()
    {
        JsonSerializer<Store> jsonSerializer=new JsonSerializer<>();
        JsonDeSerializer<Store> jsonDeSerializer=new JsonDeSerializer<>(Store.class);
        return Serdes.serdeFrom(jsonSerializer,jsonDeSerializer);
    }

}
