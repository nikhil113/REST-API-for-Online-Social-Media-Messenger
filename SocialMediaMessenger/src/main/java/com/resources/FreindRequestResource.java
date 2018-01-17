package com.resources;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

import com.business.FriendRequestBusiness;
import com.custom_annotation.AuthenticationNeeded;

@Path("/")
public class FreindRequestResource {
	
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
	
	
	@POST
	@Path("/sendfriendrequest/{profileId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public void postFriendRequest(@PathParam("profileId") final String profileId, @Context final HttpHeaders header, final @Suspended AsyncResponse asyncResponse) {
		
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
				if(profileId!=null) {
					
					String currentUserId=getCurrentUserId(header);
					FriendRequestBusiness business=new FriendRequestBusiness();
					Response response=business.postFriendRequest(currentUserId, profileId);
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
	@Path("/confirmfriendrequest/{profileId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public void confirmFriendRequest(@PathParam("profileId") final String profileId,@Context final HttpHeaders header,final @Suspended AsyncResponse asyncResponse) {
		
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
				if(profileId != null) {
					
				}else {
					if(! asyncResponse.isDone())
						asyncResponse.resume(Response.status(400).build());
				}
			}
			
		}.start();
		
	}

}
