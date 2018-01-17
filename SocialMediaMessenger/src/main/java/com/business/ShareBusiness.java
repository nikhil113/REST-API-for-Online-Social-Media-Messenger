package com.business;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.beans.Message;
import com.beans.ResponseWithHateOAS;
import com.beans.Share;
import com.beans.UserProfile;
import com.dao.ShareDao;

public class ShareBusiness {
	
	public Response getShareInformationByMessageId(String messageId) {
		
		ShareDao shareDao=new ShareDao();
		ResponseWithHateOAS hateOASResponse=shareDao.getShareInformationByMessageId(messageId);
		Response response=Response.ok(hateOASResponse).build();
		
		return response;
	}
	
	public Response getShareInformationByMessageAndShareId(String messageId, String shareId) {
		
		ShareDao shareDao=new ShareDao();
		ResponseWithHateOAS hateOASResponse=shareDao.getShareInformationByMessageAndShareId(messageId, shareId);
		Response response=Response.ok(hateOASResponse).build();
		
		return response;
	}
	
	public Response postShare(String currentUserId, String messageId, UriInfo uriInfo) {
		
		ShareDao shareDao=new ShareDao();
		Share share=shareDao.isAlreadyExist(currentUserId, messageId);
		if(share == null) {
			
			UserProfile user=new UserProfile();
			user.setUserId(currentUserId);
			
			Message message=new Message();
			message.setMessageId(messageId);
			
			Calendar calendar=Calendar.getInstance();
			Date date=calendar.getTime();
			
			share=new Share();
			share.setUser(user);
			share.setMessage(message);
			share.setCreated(date);
			
			Share insertedShare=shareDao.postShare(share);
			if(insertedShare != null) {
				
				UriBuilder uriBuilder=uriInfo.getAbsolutePathBuilder().path(insertedShare.getShareId());
				URI uri=uriBuilder.build(uriBuilder);
				return Response.created(uri).entity(share).build();
				
			}else {
				return Response.serverError().build();
			}
			
		}else {
			return Response.ok(share).build();
		}

	}
	
	public Response deleteShare(String currentUserId, String messageId, String shareId) {
		
		ShareDao shareDao=new ShareDao();
		boolean isValidToDelete=shareDao.isValidToDelete(currentUserId, shareId);
		if(isValidToDelete) {
			
			boolean status=shareDao.deleteShare(shareId);
			if(status) {
				return Response.noContent().build();
			}else {
				return Response.serverError().build();
			}
			
		}else {
			throw new ForbiddenException("Not authorized to perform this delete operation");
		}
		
	}
	

}
