package com.exception_mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.beans.ErrorMessage;
import com.exceptions.UnAuthorizedException;

@Provider
public class UnAuthorizedExceptionMapper implements ExceptionMapper<UnAuthorizedException>{

	@Override
	public Response toResponse(UnAuthorizedException exception) {
		ErrorMessage errorMessage=new ErrorMessage();
		errorMessage.setErrorCode(401);
		errorMessage.setErrorMessage(exception.getMessage());
		
		return Response.status(401)
						.entity(errorMessage)
						.build();
	}

}
