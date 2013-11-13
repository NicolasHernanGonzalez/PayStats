package ar.com.gl.paystadistics.exceptions;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.jboss.resteasy.api.validation.ResteasyViolationException;


public class ValidationExceptionHandlerREST implements ExceptionMapper<ResteasyViolationException> {

    @Override
    public Response toResponse(ResteasyViolationException exception) {
        
        Map<String, String> violations = new HashMap<String, String>();

        for (ResteasyConstraintViolation violation : exception.getViolations()) {
             violations.put(violation.getPath(), violation.getMessage());
        }
        
        return Response.status(Status.BAD_REQUEST).entity(violations.toString()).build();
    }
}