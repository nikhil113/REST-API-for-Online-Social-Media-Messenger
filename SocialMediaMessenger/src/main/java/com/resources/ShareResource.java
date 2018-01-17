package com.resources;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.business.ShareBusiness;
import com.custom_annotation.AuthenticationNeeded;

@Path("/")
public class ShareResource {
	
	
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
	public void getShareInfoByMessageId(@PathParam("messageId") final String messageId, final @Suspended AsyncResponse asyncResponse) {
		
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
				if(messageId!=null) {
					ShareBusiness shareBusiness=new ShareBusiness();
					Response response=shareBusiness.getShareInformationByMessageId(messageId);
					if(! asyncResponse.isDone())
						asyncResponse.resume(response);
				}else {
					if(! asyncResponse.isDone())
						asyncResponse.resume(Response.status(400).build());
				}
			}
			
		}.start();
		
	}
	
	
	@GET
	@Path("/{shareId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public void getShareInfoByMessageAndShareId(@PathParam("messageId") final String messageId, @PathParam("shareId") final String shareId, final @Suspended AsyncResponse asyncResponse) {
		
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
				
				if(messageId!=null && shareId!=null) {
					
					ShareBusiness shareBusiness=new ShareBusiness();
					Response response=shareBusiness.getShareInformationByMessageAndShareId(messageId, shareId);
					if(!asyncResponse.isDone())
						asyncResponse.resume(response);
					
				}else {
					if(!asyncResponse.isDone())
						asyncResponse.resume(Response.status(400).build());
				}
				
			}
			
		}.start();
		
	}
	
	
	@POST
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public void postShare(@PathParam("messageId") final String messageId, @Context final HttpHeaders header, @Context final UriInfo uriInfo, final @Suspended AsyncResponse asyncResponse) {
		
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
				
				if(messageId != null) {
					
					String currentUserId=getCurrentUserId(header);
					ShareBusiness shareBusiness=new ShareBusiness();
					Response response=shareBusiness.postShare(currentUserId, messageId, uriInfo);
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
	@Path("{shareId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public void deleteShare(@PathParam("messageId") final String messageId, @PathParam("shareId") final String shareId, @Context final HttpHeaders header, final @Suspended AsyncResponse asyncResponse) {
		
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
				if(messageId != null && shareId != null) {
					String currentUserId=getCurrentUserId(header);
					ShareBusiness shareBusiness=new ShareBusiness();
					Response response=shareBusiness.deleteShare(currentUserId, messageId, shareId);
					if(! asyncResponse.isDone())
						asyncResponse.resume(response);
					
				}else {
					if(!asyncResponse.isDone())
						asyncResponse.resume(Response.status(400).build());
				}
			}
			
		}.start();
		
	}

}
