package kiva.exercise.com.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kiva.exercise.com.core.JsonUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class GenericExceptionMapper  implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        ObjectNode response= JsonUtils.createObjectNode();
        response.put("response",e.getMessage());
        return Response
                .status(BAD_REQUEST)
                .entity(response)
                .build();
    }
}
