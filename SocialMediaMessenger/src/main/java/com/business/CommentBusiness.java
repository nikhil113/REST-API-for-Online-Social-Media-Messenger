package com.business;

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

import com.beans.Comment;
import com.beans.Message;
import com.beans.ResponseWithHateOAS;
import com.beans.UserProfile;
import com.dao.CommentDao;
import com.exceptions.ForbiddenException;

public class CommentBusiness {
	
	public Response getCommentsByMessageId(String currentUserId, String messageId) {
		
		CommentDao commentDao=new CommentDao();
		List<ResponseWithHateOAS> responseList=commentDao.getAllCommentsByMessageId(currentUserId, messageId);
		Response response=Response.ok(responseList).build();
		
		return response;
	}
	
	public Response getCommentByMessageAndCommentId(String currentUserId, String messageId, String commentId, Request request) {
		
		CommentDao commentDao=new CommentDao();
		ResponseWithHateOAS hateOASResponse=commentDao.getCommentByMessageAndCommentId(currentUserId, messageId, commentId);
		CacheControl cc=new CacheControl();
		cc.setPrivate(true);
		cc.setNoTransform(true);
		cc.setMaxAge(60);
		
		EntityTag eTag=new EntityTag(Integer.toString(hateOASResponse.hashCode()));
		ResponseBuilder builder=request.evaluatePreconditions(eTag);
		if(builder!=null) {
			return builder.cacheControl(cc).build();
		}else {
			return Response.ok(hateOASResponse).cacheControl(cc).tag(eTag).build();
		}

	}
	
	public Response postComment(String currentUserId, String messageId, Comment comment, UriInfo uriInfo) {
		
		UserProfile user=new UserProfile();
		user.setUserId(currentUserId);
		
		Message message=new Message();
		message.setMessageId(messageId);
		
		Calendar calendar=Calendar.getInstance();
		Date date=calendar.getTime();
		
		comment.setUser(user);
		comment.setMessage(message);
		comment.setCreated(date);
		
		CommentDao commentDao=new CommentDao();
		Comment insertedComment=commentDao.postComment(comment);
		
		UriBuilder uriBuilder=uriInfo.getAbsolutePathBuilder().path(comment.getCommentId());
		URI uri=uriBuilder.build(uriBuilder);
		
		Response response=Response.created(uri).entity(insertedComment).build();
		
		return response;
	}
	
	public Response updateComment(String currentUserId,String messageId, String commentId, Comment comment) {
		
		CommentDao commentDao=new CommentDao();
		boolean isValidateToUpdate=commentDao.isValidateToUpdateComment(currentUserId, commentId);
		if(isValidateToUpdate) {
			Calendar calendar=Calendar.getInstance();
			Date date=calendar.getTime();
			
			UserProfile user=new UserProfile();
			user.setUserId(currentUserId);
			
			Message message=new Message();
			message.setMessageId(messageId);
			
			comment.setCommentId(commentId);
			comment.setUser(user);
			comment.setMessage(message);
			comment.setCreated(date);
			
			boolean status=commentDao.updateComment(comment);
			if(status) {
				return Response.noContent().build();
			}else {
				return Response.serverError().build();
			}
					
		}else {
			throw new ForbiddenException("Not authorized to perform this update operation");
		}
		
	}
	
	public Response deleteComment(String currentUserId,String messageId, String commentId) {
		
		CommentDao commentDao=new CommentDao();
		boolean isValidateToDelete=commentDao.isValidateToUpdateComment(currentUserId, commentId);
		if(isValidateToDelete) {
			boolean status=commentDao.deleteComment(commentId);
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
