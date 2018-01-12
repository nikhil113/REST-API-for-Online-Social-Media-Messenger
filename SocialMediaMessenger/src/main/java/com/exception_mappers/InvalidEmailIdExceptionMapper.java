package com.exception_mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.beans.ErrorMessage;
import com.exceptions.InvalidEmailIdException;

@Provider
public class InvalidEmailIdExceptionMapper implements ExceptionMapper<InvalidEmailIdException>{

	@Override
	public Response toResponse(InvalidEmailIdException exception) {
		ErrorMessage errorMessage=new ErrorMessage();
		errorMessage.setErrorCode(400);
		errorMessage.setErrorMessage(exception.getMessage());
		return Response.status(400)
						.entity(errorMessage)
						.build();
	}

}
