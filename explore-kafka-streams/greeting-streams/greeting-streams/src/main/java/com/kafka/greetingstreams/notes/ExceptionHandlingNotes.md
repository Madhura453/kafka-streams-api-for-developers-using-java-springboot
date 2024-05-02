
1. if you don't handle the exception the kafka streams application itself will shut down.
For deserialization default exception handling is 'LogAndFailExceptionHandler'.
here it returns DeserializationHandlerResponse.FAIL; so whole application will shut down.
if any poison record or invalid record publish in topic. it
will come the whole application will shut down.
fail the processing and stop = FAIL(1, "FAIL");
Custom Exception Handling for deserialization
its having    continue with processing =CONTINUE(0, "CONTINUE"),
LogAndContinueExceptionHandler=it will not shut down for invalid record. The app will still
continue to un
we can override default kafka default exception handler LogAndFailExceptionHandler
to LogAndContinueExceptionHandler.
````
    properties.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
              LogAndContinueExceptionHandler.class)
````

   