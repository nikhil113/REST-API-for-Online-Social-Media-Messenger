package com.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.beans.Token;
import com.constants.FilterConstants;
import com.custom_annotation.AuthenticationNeeded;
import com.dao.TokenDao;

@Provider
@AuthenticationNeeded
@Priority(FilterConstants.AUTENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter{

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		String string[]=authorizationHeader.split(" ");
		String userDetails[]=string[0].split(":");
		String userId=userDetails[0];
		
		long validTime=Long.parseLong(string[1]);
		long curTime=System.currentTimeMillis();
		if(validTime>curTime) {
			TokenDao tokenDao=new TokenDao();
			Token token=tokenDao.getTokenByUserId(userId);
			if(token!=null) {
				String tokenString=token.getToken();
				if(authorizationHeader.equals(tokenString)) {
					System.out.println("*********************  token validated   ********************");
				}else {
					
					requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
				}
			}else {
				
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
		}else {
			
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
		
		
	}

}
