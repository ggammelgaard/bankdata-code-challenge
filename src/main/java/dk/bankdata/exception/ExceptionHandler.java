package dk.bankdata.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof AccountNotFoundException) {
            return buildResponse(Response.Status.NOT_FOUND, exception);
        }
        if (exception instanceof IncorrectBalanceFormatException) {
            return buildResponse(Response.Status.BAD_REQUEST, exception);
        }
        if (exception instanceof ExchangeServiceUnavailableException) {
            return buildResponse(Response.Status.SERVICE_UNAVAILABLE, exception);
        }
        if (exception instanceof IllegalArgumentException) {
            return buildResponse(Response.Status.BAD_REQUEST, exception);
        }
        return buildResponse(Response.Status.INTERNAL_SERVER_ERROR,
                new Exception("Something unexpected happened. Try again"));
    }

    private Response buildResponse(Response.Status status, Exception exception) {
        return Response.status(status)
                .entity(new ErrorResponseBody(exception.getMessage()))
                .build();
    }

    public static final class ErrorResponseBody {

        private final String message;

        public ErrorResponseBody(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}