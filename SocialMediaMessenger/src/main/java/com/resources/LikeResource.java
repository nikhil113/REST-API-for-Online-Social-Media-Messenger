package com.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.custom_annotation.AuthenticationNeeded;

@Path("/")
public class LikeResource {
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public void getLikeInformationByMessageId(@PathParam("messageId") String messageId, @Context HttpHeaders header, final @Suspended AsyncResponse asyncResponse) {
		
	}
	
	
	
	
	
	@GET
	@Path("/sayhello")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayhello(@PathParam("messageId") String messageId) {
		return "hello world "+messageId;
	}

}
