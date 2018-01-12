package com.business;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.beans.Message;
import com.beans.ResponseWithHateOAS;
import com.beans.UserProfile;
import com.dao.MessageDao;
import com.exceptions.DataNotFoundException;

public class MessageBusiness {

	public Response getAllMessages(String currentUserId) {
		
		MessageDao messageDao=new MessageDao();
		List<ResponseWithHateOAS> responseList=messageDao.getAllMessages(currentUserId);
		Response response=Response.ok(responseList).build();
		return response;
		
	}
	
	public Response getMessageById(String currentUserId, String messageId, Request request) {
		
		MessageDao messageDao=new MessageDao();
		Message message=messageDao.getMessageById(currentUserId, messageId);
		if(message == null) {
			throw new DataNotFoundException("Either message id is wrong or message is private");
		}else {
			CacheControl cc=new CacheControl();
			cc.setPrivate(true);
			cc.setNoTransform(true);
			cc.setMaxAge(60);
			
			EntityTag eTag=new EntityTag(Integer.toString(message.hashCode()));
			ResponseBuilder builder=request.evaluatePreconditions(eTag);
			if(builder != null) {
				return builder.cacheControl(cc).build();
			}else {
				return Response.ok(message).cacheControl(cc).tag(eTag).build();
			}
		}
	
	}
	
	public Response postMessage(String currentUserId, Message message, UriInfo uriInfo) {
		
		UserProfile user=new UserProfile();
		user.setUserId(currentUserId);
		message.setUser(user);
		
		Calendar calendar=Calendar.getInstance();
		Date created=calendar.getTime();
		message.setCreated(created);
		
		MessageDao messageDao=new MessageDao();
		Message insertedMessage=messageDao.postMessage(message);
		
		UriBuilder uriBuilder=uriInfo.getAbsolutePathBuilder().path(message.getMessageId());
		URI uri=uriBuilder.build(uriBuilder);
		
		Response response=Response.created(uri).entity(insertedMessage).build();
		return response;
		
	}
	
	public Response updateMessage(String currentUserId, String messageId, Message message) {
		
		MessageDao messageDao=new MessageDao();
		boolean isValidToUpdate=messageDao.isValidToUpdate(currentUserId, messageId);
		if(isValidToUpdate) {
			
			message.setMessageId(messageId);
			UserProfile user=new UserProfile();
			user.setUserId(currentUserId);
			message.setUser(user);
			Calendar calendar=Calendar.getInstance();
			Date updated=calendar.getTime();
			message.setCreated(updated);
			boolean status=messageDao.updateMessage(message);
			if(status) {
				return Response.noContent().build();
			}else {
				return Response.serverError().build();
			}
			
		}else {
			throw new ForbiddenException("Not allowed to perform this update operation");
		}
		
	}
	
	public Response deleteMessage(String currentUserId, String messageId) {
		
		MessageDao messageDao=new MessageDao();
		boolean isValidateToUpdate=messageDao.isValidToUpdate(currentUserId, messageId);
		if(isValidateToUpdate) {
			boolean status = messageDao.deleteMessage(messageId);
			if(status) {
				return Response.noContent().build();
			}else {
				return Response.serverError().build();
			}
		}else {
			System.out.println("############################## forbidden");
			throw new ForbiddenException("Not allowed to perform this delete peartion");
		}
		
	}
	
}
