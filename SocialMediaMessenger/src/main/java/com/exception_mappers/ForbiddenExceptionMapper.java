package com.exception_mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.beans.ErrorMessage;
import com.exceptions.ForbiddenException;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException>{

	@Override
	public Response toResponse(ForbiddenException exception) {
		
		ErrorMessage errorMessage=new ErrorMessage();
		errorMessage.setErrorCode(403);
		errorMessage.setErrorMessage(exception.getMessage());
		
		return Response.status(403).entity(errorMessage).build();
	}

}
