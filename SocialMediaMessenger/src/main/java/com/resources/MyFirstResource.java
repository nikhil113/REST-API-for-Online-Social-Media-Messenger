package com.resources;



import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/myfirstresource")
public class MyFirstResource {
	
	@GET
	@Path("/sayhello")
    @Produces(MediaType.TEXT_PLAIN)
	public String sayHello() {
		return "hello world";
	}

}
