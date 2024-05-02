package kafkastreams.ordersmanagementstreams.exceptionhandler;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalStateException(IllegalStateException exception)
    {
      ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
              HttpStatusCode.valueOf(400),exception.getMessage());
      problemDetail.setProperty("additionalInfo","Please pass a Valid Order Type: general_orders" +
              "or restaurant_orders");
      return problemDetail;
    }
}
/*
problem detail is something which got introduced in Spring Framework six, using which we can provide
custom error messages
 */