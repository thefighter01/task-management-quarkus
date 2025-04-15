package org.learning.exceptions;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<IllegalArgumentException> {

    @Context
    UriInfo uriInfo;


    @Override
    public Response toResponse(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(   Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                ex.getMessage(),
                uriInfo.getPath()
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .build();
    }
}
