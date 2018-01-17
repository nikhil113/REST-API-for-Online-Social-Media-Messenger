package com.resources;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.beans.Message;
import com.business.MessageBusiness;
import com.custom_annotation.AuthenticationNeeded;

@Path("/messages")
public class MessageResource {
	
	
	public String getCurrentUserId(HttpHeaders header) {
		
		List<String> authHeaderList = header.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String authHeader=null;
		for(String auth:authHeaderList) {
			authHeader=auth;
		}
		
		String authHeaderArr[]=authHeader.split(" ");
		String userDetails[]=authHeaderArr[0].split(":");
		String currentUserId=userDetails[0];
		
		return currentUserId;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response getAllMessages(@Context HttpHeaders header) {

		String currentUserId = getCurrentUserId(header);
		MessageBusiness messageBusiness=new MessageBusiness();
		Response response=messageBusiness.getAllMessages(currentUserId);
		return response;
	}
	
	
	@GET
	@Path("/{messageId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response getMessageById(@PathParam("messageId") String messageId, @Context HttpHeaders header, @Context Request request) {
		
		String currentUserId=getCurrentUserId(header);
		MessageBusiness messageBusiness=new MessageBusiness();
		if(messageId == null) {
			return Response.status(400).build();
		}else {
			Response response=messageBusiness.getMessageById(currentUserId, messageId, request);
			return response;
		}
	}
	
	
	@POST
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response postMessage(Message message,@Context HttpHeaders header, @Context UriInfo uriInfo) {
		
		if(message == null) {
			return Response.status(400).build();
		}else {
			String currentUserId = getCurrentUserId(header);
			MessageBusiness messageBusiness=new MessageBusiness();
			Response response=messageBusiness.postMessage(currentUserId, message, uriInfo);
			return response;
		}
		
	}
	
	
	@PUT
	@Path("/{messageId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response updateMessage(Message message, @PathParam("messageId") String messageId, @Context HttpHeaders header) {
		
		if(messageId != null && message != null) {
			
			String currentUserId=getCurrentUserId(header);
			MessageBusiness messageBusiness=new MessageBusiness();
			Response response=messageBusiness.updateMessage(currentUserId, messageId, message);
			return response;
			
		}else {
			return Response.status(400).build();
		}
		
	}
	
	
	@DELETE
	@Path("/{messageId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response deleteMessage(@PathParam("messageId") String messageId, @Context HttpHeaders header) {
		
		if(messageId != null) {
			String currentUserId=getCurrentUserId(header);
			MessageBusiness messageBusiness=new MessageBusiness();
			Response response=messageBusiness.deleteMessage(currentUserId, messageId);
			return response;
		}else {
			return Response.status(400).build();
		}
		
	}
	
	@Path("/{messageId}/comments")
	public CommentResource getCommentResource() {
		return new CommentResource();
	}
	
	@Path("/{messageId}/likes")
	public LikeResource getLikeResource() {
		return new LikeResource();
	}
	
	@Path("{messageId}/shares")
	public ShareResource getShareResource() {
		return new ShareResource();
	}

}
