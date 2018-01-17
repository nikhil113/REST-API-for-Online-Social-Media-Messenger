package com.business;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.beans.Like;
import com.beans.Message;
import com.beans.ResponseWithHateOAS;
import com.beans.UserProfile;
import com.dao.LikeDao;

public class LikeBusiness {
	
	public Response getLikeInformationByMessageId(String messageId) {
		
		LikeDao likeDao=new LikeDao();
		ResponseWithHateOAS hateOASResponse=likeDao.getLikeInformationByMessageId(messageId);
		Response response=Response.ok(hateOASResponse).build();
		
		return response;
	}
	
	public Response getLikeInfoByMessgaeIdAndLikeId(String messageId, String likeId) {
		
		LikeDao likeDao=new LikeDao();
		ResponseWithHateOAS hateOASResponse=likeDao.getLikeInfoByMessageIdAndLikeId(likeId, messageId);
		Response response=Response.ok(hateOASResponse).build();
		
		return response;
	}
	
	public Response postLike(String currentUserId, String messageId, UriInfo uriInfo) {
		
		LikeDao likeDao=new LikeDao();
		Like like=likeDao.isAlreadyExist(currentUserId, messageId);
		if(like == null) {
			like = new Like();
			
			UserProfile user=new UserProfile();
			user.setUserId(currentUserId);
			
			Message message=new Message();
			message.setMessageId(messageId);
			
			Calendar calendar=Calendar.getInstance();
			Date date=calendar.getTime();
			
			like.setUser(user);
			like.setMessage(message);
			like.setCreated(date);
			
			Like insertedLike=likeDao.postLike(like);
			UriBuilder uriBuilder=uriInfo.getAbsolutePathBuilder().path(insertedLike.getLikeId());
			URI uri=uriBuilder.build(uriBuilder);
			
			return Response.created(uri).entity(insertedLike).build();
					
		}else {
			return Response.ok(like).build();
		}
		
	}
	
	public Response deleteLike(String currentUserId, String messageId, String likeId) {
		
		LikeDao likeDao=new LikeDao();
		boolean isValidToDelete=likeDao.isValidToDelete(currentUserId, likeId);
		if(isValidToDelete) {
			boolean status=likeDao.deleteLike(likeId);
			if(status) {
				return Response.noContent().build();
			}else {
				return Response.serverError().build();
			}
		}else {
			throw new ForbiddenException("Not authorize to perform this delete operation");
		}

	}

}
