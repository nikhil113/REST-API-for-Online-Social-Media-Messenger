package com.business;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.beans.ResponseWithHateOAS;
import com.beans.UserProfile;
import com.dao.ProfileDao;
import com.exceptions.InvalidEmailIdException;

public class ProfileBussiness {
	
	public String generateMD5Hash(String string) throws NoSuchAlgorithmException {
		
		MessageDigest md=MessageDigest.getInstance("MD5");
		md.update(string.getBytes());
		byte arr[]=md.digest();
		StringBuffer sb=new StringBuffer();
		for(byte b:arr) {
			sb.append(Integer.toHexString(b & 0xff).toString());
		}
		
		return sb.toString();
		
	}
	
	public Response createProfile(UserProfile user, UriInfo uriInfo) {
		
		System.out.println("absolute path ========== "+uriInfo.getAbsolutePath());
		System.out.println("path ================= "+uriInfo.getPath());
		System.out.println("base path ======================== "+uriInfo.getBaseUri());
		
		ProfileDao profileDao=new ProfileDao();
		boolean availability=profileDao.checkAvailibility(user.getEmail());
		if(availability) {

			Calendar calendar=Calendar.getInstance();
			Date date=calendar.getTime();
			user.setCreated(date);
			
			String password=user.getPassword();
			try {
				password=generateMD5Hash(password);
				user.setPassword(password);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			
			
			UserProfile userProfile=profileDao.createProfile(user);
	
			
			if(userProfile!=null) {
				
				UriBuilder uriBuilder=uriInfo.getAbsolutePathBuilder();
				uriBuilder.path(userProfile.getUserId());
				
				
				return Response.created(uriBuilder.build(uriBuilder))
								.entity(userProfile)
								.build();
			}else {
				return Response.serverError()
								.build();
			}
		}else {
			throw new InvalidEmailIdException("Email id is either empty or already being used by someone else");
		}
	}
	
	
	public Response getAllProfiles(String currentUserId, UriInfo uriInfo, Request request) {
		
		ProfileDao profileDao=new ProfileDao();
		List<ResponseWithHateOAS> responseList=profileDao.getAllProfiles(currentUserId, uriInfo);
		
		CacheControl cc=new CacheControl();
		cc.setPrivate(true);
		cc.setNoTransform(true);
		cc.setMaxAge(60);
		
		EntityTag eTag=new EntityTag(Integer.toString(responseList.hashCode()));
		
		ResponseBuilder builder=request.evaluatePreconditions(eTag);
		Response response=null;
		
		if( builder != null) {
			response=builder.cacheControl(cc).build();
			System.out.println("************ pre conditions met");
		}else {
			response=Response.ok(responseList).cacheControl(cc).tag(eTag).build();
			System.out.println("************ pre conditions not met");
		}
		
		return response;
	}
	
	public Response getProfileById(String currentUserId, String wantedUserId, UriInfo uriInfo, Request request) {
		
		ProfileDao profileDao=new ProfileDao();
		ResponseWithHateOAS hateOASResponse=profileDao.getProfileById(currentUserId, wantedUserId, uriInfo);
		
		CacheControl cc=new CacheControl();
		cc.setPrivate(true);
		cc.setNoTransform(true);
		cc.setMaxAge(60);
		
		EntityTag eTag=new EntityTag(Integer.toString(hateOASResponse.hashCode()));
		Response response=null;
		ResponseBuilder builder=request.evaluatePreconditions(eTag);
		if(builder!=null) {
			response=builder.cacheControl(cc).build();
		}else {
			response=Response.ok(hateOASResponse).cacheControl(cc).tag(eTag).build();
		}
		
		return response;
	}
	
	public Response updateProfileByProfileId(String currentUserId, String targetedUserId,UserProfile userProfile) {
		
		Response response=null;
		
		if(currentUserId.equals(targetedUserId)) {
			userProfile.setUserId(targetedUserId);
			ProfileDao profileDao=new ProfileDao();
			boolean status = profileDao.updateProfileById(userProfile);
			if(status) {
				response=Response.noContent().build();
			}else {
				response=Response.serverError().build();
			}
		}else {
			response=Response.status(403).entity("Not Authorized to perform this action").build();
		}
		
		return response;
	}
	
	public Response deleteProfile(String currentUserId, String targetedUserId,String userName,String password) {
		
		Response response = null;
		if(currentUserId.equals(targetedUserId)) {
			ProfileDao profileDao=new ProfileDao();
			boolean status=profileDao.deleteProfileById(targetedUserId,userName,password);
			if(status) {
				response = Response.noContent().build();
			}else {
				response = Response.serverError().build();
			}
		}else {
			response = Response.status(403).entity("Not authorized to perform this action").build();
		}
		
		return response;
	}
	

}
