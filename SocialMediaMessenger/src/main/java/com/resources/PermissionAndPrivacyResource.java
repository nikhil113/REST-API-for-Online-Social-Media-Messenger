package com.resources;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.beans.Permission;
import com.business.PermissionBusiness;

@Path("/permission")
public class PermissionAndPrivacyResource {
	
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
	
	
	@PUT
	@Path("/profile")
	public void updatePermissionOfUserProfile(final Permission permission, @Context final HttpHeaders header, final @Suspended AsyncResponse asyncResponse) {
	
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
				if(permission!=null) {
					
					String currentUserId=getCurrentUserId(header);
					PermissionBusiness permissionBusiness=new PermissionBusiness();
					Response response=permissionBusiness.updatePermissionOfProfile(currentUserId, permission);
					if(!asyncResponse.isDone())
						asyncResponse.resume(response);
					
				}else {
					if(!asyncResponse.isDone())
						asyncResponse.resume(Response.status(400).build());
				}
			}
			
		}.start();
		
	}
	
	
	@PUT
	@Path("/messages/{messageId}/{privacy}")
	public void updatePrivacyOfMessage(@PathParam("messageId") final String messageId, @PathParam("privacy") final String privacy, @Context final HttpHeaders header, final @Suspended AsyncResponse asyncResponse) {
		
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
				if(messageId != null && privacy != null) {
					
					String currentUserId=getCurrentUserId(header);
					
				}else {
					if(!asyncResponse.isDone())
						asyncResponse.resume(Response.status(400).build());
				}
			}
			
		}.start();
		
	}

}
