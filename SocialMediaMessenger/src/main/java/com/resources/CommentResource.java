package com.resources;

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

import com.beans.Comment;
import com.business.CommentBusiness;
import com.custom_annotation.AuthenticationNeeded;
import com.mysql.cj.core.util.StringUtils;

@Path("/")
public class CommentResource {
	
	
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
	public void getAllCommentsByMessageId(@PathParam("messageId") final String messageId, @Context HttpHeaders header, @Suspended final AsyncResponse asyncResponse) {
		
		final String currentUserId=getCurrentUserId(header);
		
		asyncResponse.setTimeout(5, TimeUnit.MINUTES);
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			
			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				Response response=Response.serverError().build();
				asyncResponse.resume(response);
				
			}
		});
		
		new Thread() {
			@Override
			public void run() {
				
				if(!StringUtils.isNullOrEmpty(messageId)) {
					CommentBusiness commentBusiness=new CommentBusiness();
					Response response=commentBusiness.getCommentsByMessageId(currentUserId, messageId);
					if(!asyncResponse.isDone()) {
						asyncResponse.resume(response);
					}
				}else {
					Response response=Response.status(400).build();
					if(!asyncResponse.isDone()) {
						asyncResponse.resume(response);
					}
				}
				
			}
		}.start();
	
	}
	
	@GET
	@Path("/{commentId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public void getCommentByMessageAndCommentId(@PathParam("messageId") String messageId, @PathParam("commentId") String commentId, @Context HttpHeaders header, @Context Request request, @Suspended AsyncResponse asyncResponse) {
		
		asyncResponse.setTimeout(5, TimeUnit.MINUTES);
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			
			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				
				Response response=Response.serverError().build();
				asyncResponse.resume(response);
			}
		});
		
		if(messageId != null && commentId != null) {
			
			String currentUserId=getCurrentUserId(header);
			CommentBusiness commentBusiness=new CommentBusiness();
			Response response=commentBusiness.getCommentByMessageAndCommentId(currentUserId, messageId, commentId, request);
			if(! asyncResponse.isDone()) {
				asyncResponse.resume(response);
			}
			
		}else {
			if(! asyncResponse.isDone()) {
				asyncResponse.resume(Response.status(400).build());
			}
		}
		
	}
	
	
	@POST
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public void postComment(Comment comment, @PathParam("messageId") String messageId, @Context HttpHeaders header, @Context UriInfo uriInfo, @Suspended AsyncResponse asyncResponse) {
		
		asyncResponse.setTimeout(5, TimeUnit.MINUTES);
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			
			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				
				asyncResponse.resume(Response.serverError().build());
			}
		});
		
		if(messageId != null && comment != null) {
			
			String currentUserId=getCurrentUserId(header);
			CommentBusiness commentBusiness=new CommentBusiness();
			Response response = commentBusiness.postComment(currentUserId, messageId, comment, uriInfo);
			if(! asyncResponse.isDone())
				asyncResponse.resume(response);
			
		}else {
			if(! asyncResponse.isDone())
				asyncResponse.resume(Response.status(400).build());
		}
		
	}
	
	
	@PUT
	@Path("{commentId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public void updateComment(final Comment comment, @PathParam("messageId") final String messageId, @PathParam("commentId") final String commentId, @Context final HttpHeaders header, final @Suspended AsyncResponse asyncResponse) {
		
		asyncResponse.setTimeout(5, TimeUnit.MINUTES);
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			
			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				asyncResponse.resume(Response.serverError().build());
				
			}
		});
		
		new Thread() {
			@Override
			public void run() {
				String currentUserId=getCurrentUserId(header);
				if(comment != null && messageId != null && commentId != null) {
					CommentBusiness commentBusiness=new CommentBusiness();
					Response response=commentBusiness.updateComment(currentUserId, messageId, commentId, comment);
					if(!asyncResponse.isDone())
						asyncResponse.resume(response);
				}else {
					if(!asyncResponse.isDone())
						asyncResponse.resume(Response.status(400).build());
				}
				
			}
		}.start();
		
	}
	
	
	@DELETE
	@Path("/{commentId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public void deleteComment(@PathParam("messageId") final String messageId, @PathParam("commentId") final String commentId, @Context final HttpHeaders header, final @Suspended AsyncResponse asyncResponse) {
		
		asyncResponse.setTimeout(5, TimeUnit.MINUTES);
		asyncResponse.setTimeoutHandler(new TimeoutHandler() {
			
			@Override
			public void handleTimeout(AsyncResponse asyncResponse) {
				asyncResponse.resume(Response.serverError().build());
				
			}
		});
		
		new Thread() {
			@Override
			public void run() {
				
				if(messageId != null && commentId != null) {
					String currentUserId=getCurrentUserId(header);
					CommentBusiness commentBusiness=new CommentBusiness();
					Response response=commentBusiness.deleteComment(currentUserId, messageId, commentId);
					if(!asyncResponse.isDone())
						asyncResponse.resume(response);
				}else {
					if(!asyncResponse.isDone())
						asyncResponse.resume(Response.status(400).build());
				}
				
			}
		}.start();
		
	}
	
}
