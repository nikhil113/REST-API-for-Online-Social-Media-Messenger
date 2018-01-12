package com.resources;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.beans.Message;
import com.business.MessageLinkedWithProfileBusiness;
import com.custom_annotation.AuthenticationNeeded;

@Path("/")
public class MessageResourceLinkedWithProfile {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public void getAllMessages(@PathParam("profileId") final String profileId, @Context HttpHeaders header, final @Suspended AsyncResponse asyncResponse, @Context final Request request){
		
		List<String> authHeaderList = header.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String authHeader=null;
		for(String auth:authHeaderList) {
			authHeader=auth;
		}
		
		String authHeaderArr[]=authHeader.split(" ");
		String userDetails[]=authHeaderArr[0].split(":");
		final String currentUserId=userDetails[0];
		
		asyncResponse.setTimeout(2, TimeUnit.MINUTES);
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			
			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				asyncResponse.resume(Response.serverError().build());
				
			}
		});
		
		new Thread() {
			@Override
			public void run() {
				MessageLinkedWithProfileBusiness messageBusiness=new MessageLinkedWithProfileBusiness();
				Response response=messageBusiness.getAllMessagesByProfileId(currentUserId, profileId,request);
				if(! asyncResponse.isDone()) {
					asyncResponse.resume(response);
				}
			}
		}.start();
		
	}
	
	
	@GET
	@Path("/{messageId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public void getMessageByProfileAndMessageId(@PathParam("profileId") final String profileId, @PathParam("messageId") final String messageId,@Context HttpHeaders header, @Context final Request request, final @Suspended AsyncResponse asyncResponse) {
		
		List<String> authHeaderList = header.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String authHeader=null;
		for(String auth:authHeaderList) {
			authHeader=auth;
		}
		
		String authHeaderArr[]=authHeader.split(" ");
		String userDetails[]=authHeaderArr[0].split(":");
		final String currentUserId=userDetails[0];
		
		asyncResponse.setTimeout(2, TimeUnit.MINUTES);
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			
			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				asyncResponse.resume(Response.serverError().build());
				
			}
		});
		
		new Thread() {
			@Override
			public void run() {
				
				MessageLinkedWithProfileBusiness messageBusiness=new MessageLinkedWithProfileBusiness();
				Response response=messageBusiness.getMessageByProfileAndMessageId(currentUserId, profileId, messageId, request);
				if(! asyncResponse.isDone()) {
					asyncResponse.resume(response);
				}
				
			}
		}.start();
		
	}
	
	
	@POST
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response postMessage(Message message, @PathParam("profileId") String profileId, @Context UriInfo uriInfo) {
		
		MessageLinkedWithProfileBusiness messageBusiness=new MessageLinkedWithProfileBusiness();
		Response response = messageBusiness.postMessage(message, profileId, uriInfo);
		
		return response;
	}
	
	
	@PUT
	@Path("/{messageId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response updateMessage(@PathParam("profileId") String profileId, @PathParam("messageId") String messageId, Message message, @Context HttpHeaders header) {
		
		List<String> authHeaderList = header.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String authHeader=null;
		for(String auth:authHeaderList) {
			authHeader=auth;
		}
		
		String authHeaderArr[]=authHeader.split(" ");
		String userDetails[]=authHeaderArr[0].split(":");
		String currentUserId=userDetails[0];
		
		MessageLinkedWithProfileBusiness messageBusiness=new MessageLinkedWithProfileBusiness();
		if(profileId!=null && messageId!=null && message!=null) {
		
			Response response=messageBusiness.updateMessage(currentUserId, profileId, messageId, message);
			return response;
			
		}else {
			return Response.status(400).build();
		}
		
	}
	
	
	@DELETE
	@Path("/{messageId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response deleteMessageById(@PathParam("profileId") String profileId, @PathParam("messageId") String messageId, @Context HttpHeaders header) {
		
		List<String> authHeaderList = header.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String authHeader=null;
		for(String auth:authHeaderList) {
			authHeader=auth;
		}
		
		String authHeaderArr[]=authHeader.split(" ");
		String userDetails[]=authHeaderArr[0].split(":");
		String currentUserId=userDetails[0];
		
		MessageLinkedWithProfileBusiness messageBusiness=new MessageLinkedWithProfileBusiness();
		if(profileId!=null && messageId!=null) {
			Response response=messageBusiness.deleteMessageById(currentUserId, profileId, messageId);
			return response;
		}else {
			return Response.status(400).build();
		}
		
	}
	
	@DELETE
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response deleteMessageByProfileId(@PathParam("profileId") String profileId, @Context HttpHeaders header) {
		
		List<String> authHeaderList = header.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String authHeader=null;
		for(String auth:authHeaderList) {
			authHeader=auth;
		}
		
		String authHeaderArr[]=authHeader.split(" ");
		String userDetails[]=authHeaderArr[0].split(":");
		String currentUserId=userDetails[0];
		
		MessageLinkedWithProfileBusiness messageBusiness=new MessageLinkedWithProfileBusiness();
		if(profileId!=null) {
			Response response=messageBusiness.deleteMessageByProfileId(currentUserId, profileId);
			return response;
		}else {
			return Response.status(400).build();
		}
		
	}
	
	
	@POST
	@Path("/postmediafile")
	@Produces(MediaType.TEXT_PLAIN)
	@AuthenticationNeeded
	public Response postMediaFile(File file1,@Context HttpHeaders header, @Context UriInfo uriInfo) throws IOException {
		
		List<String> authHeaderList = header.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String authHeader=null;
		for(String auth:authHeaderList) {
			authHeader=auth;
		}
		
		String authHeaderArr[]=authHeader.split(" ");
		String userDetails[]=authHeaderArr[0].split(":");
		String currentUserId=userDetails[0];
		
		if(file1==null) {
			return Response.status(400).build();
		}else {
		
			MessageLinkedWithProfileBusiness messageBusiness=new MessageLinkedWithProfileBusiness();
			Response response=messageBusiness.postMediaFile(file1, currentUserId, uriInfo);
			return response;
			
		}
		
	}
	
	@GET
	@Path("/sayhello")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello(@PathParam("profileId") String profileId) {
		return "hello world !!!"+profileId;
	}
	
}
