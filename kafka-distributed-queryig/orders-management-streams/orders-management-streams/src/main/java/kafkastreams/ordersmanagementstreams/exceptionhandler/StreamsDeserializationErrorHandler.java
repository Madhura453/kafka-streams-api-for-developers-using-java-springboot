package kafkastreams.ordersmanagementstreams.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.errors.DeserializationExceptionHandler;
import org.apache.kafka.streams.processor.ProcessorContext;

import java.util.Map;

@Slf4j
public class StreamsDeserializationErrorHandler implements DeserializationExceptionHandler {
    int errorCounter=0;
    @Override
    public DeserializationHandlerResponse handle(ProcessorContext context, ConsumerRecord<byte[], byte[]> record, Exception exception) {
        log.error("Exception is : {} and the Kafka Record is : {} " , exception.getMessage(), record,  exception);
        log.error("errorCounter is : {} " , errorCounter);
        if(errorCounter<2)
        {
            errorCounter++;
            return DeserializationHandlerResponse.CONTINUE;
        }
        return DeserializationHandlerResponse.FAIL;
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
/*
This for deserialization exception handling
if failed records<2 the application will continue.if not the application will shut down
num.stream.threads=2.
then error count will be each thread processed failed records=errorCounter
if one thread processed invalid records =1  . another thread processed invalid records =1.
then error count still =1 only
 */
/*
Error count will dependents on no.of tasks and no.threads.
Error count will vary on no.of tasks and no.threads.

if error count =2

State transition from PENDING_ERROR to ERROR=application shutdown
 */