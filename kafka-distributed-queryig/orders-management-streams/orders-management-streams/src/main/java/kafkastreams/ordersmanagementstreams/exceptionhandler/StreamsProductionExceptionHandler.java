package kafkastreams.ordersmanagementstreams.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.errors.ProductionExceptionHandler;

import java.util.Map;

@Slf4j
public class StreamsProductionExceptionHandler implements ProductionExceptionHandler {
    @Override
    public ProductionExceptionHandlerResponse handle(ProducerRecord<byte[], byte[]> record, Exception exception) {
        log.error("Exception in handle : {}  and the record is : {} ", exception.getMessage(), record, exception);
       return ProductionExceptionHandlerResponse.CONTINUE;
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
/*
same theory of StreamsDeserialization.
I don't have any problem if any failed record. constantly publish the new records
 */