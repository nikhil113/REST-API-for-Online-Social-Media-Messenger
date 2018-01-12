package com.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.business.TokenBusiness;

@Path("/getToken")
public class TokenResource {
	
	@GET
	@Path("/{userId}/{password}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response sayHello(@PathParam("userId") String userId, @PathParam("password") String password) {
		
		TokenBusiness tokenBusiness=new TokenBusiness();
		Response response=tokenBusiness.getToken(userId, password);
		
		return response;
	}
	

}
