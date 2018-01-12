package com.business;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
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

import com.beans.Message;
import com.beans.ResponseWithHateOAS;
import com.beans.UserProfile;
import com.dao.MessageLinkedWithProfileDao;

public class MessageLinkedWithProfileBusiness {
	
	public Response getAllMessagesByProfileId(String currentUserId, String targetedProfileId, Request request) {
		
		MessageLinkedWithProfileDao messageDao=new MessageLinkedWithProfileDao();
		List<ResponseWithHateOAS> responseList=messageDao.getAllMessagesByProfileId(currentUserId, targetedProfileId);
		Response response = null;
		
		CacheControl cc=new CacheControl();
		cc.setPrivate(true);
		cc.setNoTransform(true);
		cc.setMaxAge(60);
		
		EntityTag eTag=new EntityTag(Integer.toString(responseList.hashCode()));
		ResponseBuilder builder=request.evaluatePreconditions(eTag);
		if(builder != null) {
			response=builder.cacheControl(cc).build();
		}else{
			response=Response.ok(responseList).cacheControl(cc).tag(eTag).build();
		}
		
		return response;
	}
	
	public Response getMessageByProfileAndMessageId(String currentUserId,String targetedProfileId,String messageId, Request request) {
		
		MessageLinkedWithProfileDao messageDao=new MessageLinkedWithProfileDao();
		ResponseWithHateOAS hateOasResponse=messageDao.getMessageByProfileAndMessageId(currentUserId, targetedProfileId, messageId);
		Response response=null;
		
		CacheControl cc=new CacheControl();
		cc.setPrivate(true);
		cc.setNoTransform(true);
		cc.setMaxAge(60);
		
		EntityTag eTag=new EntityTag(Integer.toString(hateOasResponse.hashCode()));
		ResponseBuilder builder=request.evaluatePreconditions(eTag);
		if(builder != null) {
			response=builder.cacheControl(cc).build();
		}else {
			response=Response.ok(hateOasResponse).cacheControl(cc).tag(eTag).build();
		}
		
		return response;
	}
	
	public Response postMessage(Message message, String currentUserId, UriInfo uriInfo) {
		
		Calendar calendar=Calendar.getInstance();
		Date date=calendar.getTime();
		message.setCreated(date);
		
		message.setPrivacy("PRIVATE");
		
		MessageLinkedWithProfileDao messageDao=new MessageLinkedWithProfileDao();
		Message insertedMessage = messageDao.postMessage(message, currentUserId);
		if(insertedMessage != null) {
			UriBuilder uriBuilder=uriInfo.getAbsolutePathBuilder().path(insertedMessage.getMessageId());
			URI uri=uriBuilder.build(uriBuilder);
			return Response.created(uri).entity(insertedMessage).build();
		}else {
			return Response.serverError().build();
		}
	}
	
	
	
	public Response updateMessage(String currentUserId,String profileId, String messageId,Message message) {
		
		if(currentUserId.equals(profileId)) {
			
			MessageLinkedWithProfileDao messageDao=new MessageLinkedWithProfileDao();
			boolean isAuthorized=messageDao.isAuthorizedToUpdate(profileId, messageId);
			if(isAuthorized) {
				message.setMessageId(messageId);
				UserProfile user=new UserProfile();
				user.setUserId(profileId);
				message.setUser(user);
				if(message.getPrivacy()==null) {
					message.setPrivacy("PRIVATE");
				}
				boolean status=messageDao.updateMessage(message);
				if(status) {
					return Response.noContent().build();
				}else {
					return Response.serverError().build();
				}
				
				
			}else {
				return Response.status(403).build();
			}
			
		}else {
			return Response.status(400).build();
		}
		
	}
	
	
	
	public Response deleteMessageById(String currentUserId, String profileId, String messageId) {
		
		if(currentUserId.equals(profileId)) {
			
			MessageLinkedWithProfileDao messageDao=new MessageLinkedWithProfileDao();
			boolean isAuthorized=messageDao.isAuthorizedToUpdate(profileId, messageId);
			if(isAuthorized) {
				Message message=new Message();
				message.setMessageId(messageId);
				boolean status=messageDao.deleteMessageById(message);
				if(status) {
					return Response.noContent().build();
				}else {
					return Response.serverError().build();
				}
			}
			
		}else {
			return Response.status(400).build();
		}
		
		return null;
	}
	
	
	public Response deleteMessageByProfileId(String currentUserId, String profileId) {
		
		if(currentUserId.equals(profileId)) {
			MessageLinkedWithProfileDao messageDao=new MessageLinkedWithProfileDao();
			messageDao.deleteAllMessageByProfileId(profileId);
			return Response.noContent().build();
		}else {
			return Response.status(400).build();
		}
		
	}
	
	
	
	public Response postMediaFile(File file1,String currentUserId, UriInfo uriInfo) throws IOException {
		
//		String fileName=file1.getName();
//		int index=fileName.lastIndexOf('.');
//		System.out.println("********** filename "+fileName);
//		System.out.println("********** last index "+index);
//		String extension=fileName.substring(index);
//		System.out.println("********** extension = "+extension);
		
		String extension=".pdf";
		String newFileName="sample"+extension;
		String path="E:\\SocialMediaMessenger\\Uploaded Posts\\"+newFileName;
		File file2=new File(path);
		System.out.println("************* file = "+file2.getAbsolutePath());
		
		Message message = new Message();
		message.setContent(path);
		message.setContentType("FILE");
		message.setPrivacy("PRIVATE");
		
		Calendar calendar=Calendar.getInstance();
		Date date=calendar.getTime();
		message.setCreated(date);
		
		MessageLinkedWithProfileDao messageDao=new MessageLinkedWithProfileDao();
		Message insertedMessage=messageDao.postMessage(message, currentUserId);
		if(insertedMessage != null) {
			
			FileInputStream fin=new FileInputStream(file1);
			BufferedInputStream br=new BufferedInputStream(fin);
			
			FileOutputStream fos=new FileOutputStream(file2);
			BufferedOutputStream bw=new BufferedOutputStream(fos);
			
			int i=br.read();
			while(i!=-1) {
				bw.write(i);
				i=br.read();
			}
			
			br.close();
			bw.close();
			fin.close();
			fos.close();
			
			UriBuilder uriBuilder=uriInfo.getAbsolutePathBuilder().path(insertedMessage.getMessageId());
			URI uri=uriBuilder.build(uriBuilder);
			
			return Response.created(uri).entity(insertedMessage).build();
			
			
		}else {
			return Response.serverError().build();
		}
		
	}
	

}
