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

import com.beans.UserProfile;
import com.business.ProfileBussiness;
import com.custom_annotation.AuthenticationNeeded;

@Path("/profiles")
public class ProfileResource {
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response createProfile(UserProfile user,@Context UriInfo uriInfo) {
		
		ProfileBussiness profileBussiness=new ProfileBussiness();
		Response response=profileBussiness.createProfile(user,uriInfo);
		return response;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response getAllProfiles(@Context HttpHeaders header, @Context UriInfo uriInfo, @Context Request request){
		
		List<String> authHeaderList = header.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String authHeader=null;
		for(String auth:authHeaderList) {
			authHeader=auth;
		}
		
		String authHeaderArr[]=authHeader.split(" ");
		String userDetails[]=authHeaderArr[0].split(":");
		String currentUserId=userDetails[0];
		
		ProfileBussiness profileBusiness=new ProfileBussiness();
		Response response=profileBusiness.getAllProfiles(currentUserId,uriInfo,request);
		
		return response;
	}
	
	@GET
	@Path("/{profileId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response getProfileByProfileId(@PathParam("profileId") String profileId, @Context HttpHeaders header, @Context UriInfo uriInfo, @Context Request request) {
		
		List<String> authHeaderList = header.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String authHeader=null;
		for(String auth:authHeaderList) {
			authHeader=auth;
		}
		
		String authHeaderArr[]=authHeader.split(" ");
		String userDetails[]=authHeaderArr[0].split(":");
		String currentUserId=userDetails[0];
		
		ProfileBussiness profileBusiness=new ProfileBussiness();
		Response response=profileBusiness.getProfileById(currentUserId, profileId, uriInfo, request);
		
		return response;
		
	}
	
	@PUT
	@Path("/{profileId}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response updateProfile(UserProfile userProfile, @PathParam("profileId") String profileId, @Context HttpHeaders header) {
		
		List<String> authHeaderList = header.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String authHeader=null;
		for(String auth:authHeaderList) {
			authHeader=auth;
		}
		
		String authHeaderArr[]=authHeader.split(" ");
		String userDetails[]=authHeaderArr[0].split(":");
		String currentUserId=userDetails[0];
		
		if(profileId == null) {
			return Response.status(400).build();
		}else {
			ProfileBussiness profileBusiness=new ProfileBussiness();
			Response response=profileBusiness.updateProfileByProfileId(currentUserId, profileId, userProfile);
			return response;
		}
		
	}
	
	@DELETE
	@Path("/{profileId}/{username}/{password}")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@AuthenticationNeeded
	public Response deleteProfile(@PathParam("profileId") String profileId, @PathParam("username") String userName, @PathParam("password") String password, @Context HttpHeaders header) {
		
		List<String> authHeaderList = header.getRequestHeader(HttpHeaders.AUTHORIZATION);
		String authHeader=null;
		for(String auth:authHeaderList) {
			authHeader=auth;
		}
		
		String authHeaderArr[]=authHeader.split(" ");
		String userDetails[]=authHeaderArr[0].split(":");
		String currentUserId=userDetails[0];
		
		if(profileId == null) {
			return Response.status(400).build();
		}else {
			ProfileBussiness profileBusiness=new ProfileBussiness();
			Response response = profileBusiness.deleteProfile(currentUserId, profileId,userName,password);
			return response;
		}
		
	}
	
	@Path("/{profileId}/messages")
	public MessageResourceLinkedWithProfile getMessageResource() {
		return new MessageResourceLinkedWithProfile();
	}
	
	
	
	@GET
	@Path("/sayhello/{name}")
	@Produces(MediaType.TEXT_PLAIN)
	@AuthenticationNeeded
	public String sayHello(@PathParam("name") String name) {
		return "helloo "+name+" !!!";
	}
	
}
