package com.exception_mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.beans.ErrorMessage;
import com.exceptions.DataNotFoundException;

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException>{

	@Override
	public Response toResponse(DataNotFoundException exception) {
		
		ErrorMessage errorMessage=new ErrorMessage();
		errorMessage.setErrorCode(404);
		errorMessage.setErrorMessage(exception.getMessage());
		
		return Response.status(404).entity(errorMessage).build();
		
	}

}
