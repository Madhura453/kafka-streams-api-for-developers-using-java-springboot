package kafkastreams.ordersmanagementstreams.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.errors.StreamsException;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;

@Slf4j
public class StreamsProcessorCustomErrorHandler implements StreamsUncaughtExceptionHandler {
    @Override
    public StreamThreadExceptionResponse handle(Throwable exception) {
        log.error("Exception in the Application : {} ",exception.getMessage(), exception);
       if(exception instanceof StreamsException)
       {
          if(exception.getCause().getMessage().equals("Transient Error"))
           {
               log.error("shout down client");
              // return StreamThreadExceptionResponse.REPLACE_THREAD;
               return StreamThreadExceptionResponse.SHUTDOWN_CLIENT;
           }
       }
        log.error("Shutdown the application");
        return StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
    }
}
/*
This for exception handling for topology. stream processing logic exception.
return type is StreamThreadExceptionResponse
        REPLACE_THREAD(0, "REPLACE_THREAD")=
        going to provide a new thread for that error. this is going to constantly retry that failed message until that becomes
      recovered.
        SHUTDOWN_CLIENT(1, "SHUTDOWN_KAFKA_STREAMS_CLIENT"),=
        going to shut down that particular thread that is causing the problem.
        SHUTDOWN_APPLICATION(2, "SHUTDOWN_KAFKA_STREAMS_APPLICATION");= SHUTDOWN_KAFKA_STREAMS_APPLICATION


 */