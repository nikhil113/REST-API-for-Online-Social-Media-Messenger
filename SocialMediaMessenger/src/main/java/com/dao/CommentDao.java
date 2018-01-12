package com.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.beans.Comment;
import com.beans.Message;
import com.beans.ResponseWithHateOAS;
import com.beans.UserProfile;
import com.initializer.DatabaseInitializer;

public class CommentDao {
	
	public List<ResponseWithHateOAS> getAllCommentsByMessageId(String currentUserId, String messageId){
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		Message message=new Message();
		message.setMessageId(messageId);
		
		String hql="from Comment where message=:message";
		Query query=session.createQuery(hql);
		query.setParameter("message", message);
		List commentList=query.list();
		List<ResponseWithHateOAS> responseList=new ArrayList<>();
		for(Object object:commentList) {
			Comment comment=(Comment) object;
			
			UserProfile user=comment.getUser();
			List<String> friendList=new ArrayList<>();
			boolean isFriend=false;
			for(String id:friendList) {
				if(id.equals(currentUserId)) {
					isFriend=true;
					break;
				}
			}
			
			if(comment.getPrivacy().equalsIgnoreCase("PUBLIC") || isFriend) {
				ResponseWithHateOAS response=new ResponseWithHateOAS();
				response.setObject(comment);
				// have to add hateoas
				responseList.add(response);
			}
		}
		
		session.close();
		return responseList;
	}

	
	public ResponseWithHateOAS getCommentByMessageAndCommentId(String currentUserId, String messageId, String commentId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		Message message=new Message();
		message.setMessageId(messageId);
		
		String hql="from Comment where commentId=:commentId and message=:message";
		Query query=session.createQuery(hql);
		query.setParameter("commentId", commentId);
		query.setParameter("message", message);
		Comment comment=(Comment) query.uniqueResult();
		
		UserProfile user=comment.getUser();
		List<String> friendList=user.getFriendList();
		boolean isFriend=false;
		for(String id:friendList) {
			if(id.equals(currentUserId)) {
				isFriend=true;
				break;
			}
		}
		
		ResponseWithHateOAS response=new ResponseWithHateOAS();
		if(comment.getPrivacy().equalsIgnoreCase("PUBLIC") || isFriend) {
			response.setObject(comment);
			// have to add hateoas
		}
		
		session.close();
		return response;
	}
	
	public Comment postComment(Comment comment) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		session.save(comment);
		Transaction transaction=session.beginTransaction();
		Comment insertedComment=new Comment();
		try {
			transaction.commit();
			insertedComment=comment;
		}catch(Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}
		finally {
			session.close();
		}
		
		return insertedComment;
	}
	
	public boolean isValidateToUpdateComment(String currentUserId, String commentId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean isValidate=false;
		
		UserProfile user=new UserProfile();
		user.setUserId(currentUserId);
		
		String hql="from Comment where commentId=:commentId and user=:user";
		Query query=session.createQuery(hql);
		query.setParameter("commentId", commentId);
		query.setParameter("user", user);
		Object object=query.uniqueResult();
		
		if(object!=null) {
			isValidate=true;
		}
		
		session.close();
		return isValidate;
	}
	
	public boolean updateComment(Comment comment) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		session.merge(comment);
		Transaction transaction=session.beginTransaction();
		try {
			transaction.commit();
			status=true;
		}catch(Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}finally {
			session.close();
		}
		
		return status;
	}
	
	public boolean deleteComment(String commentId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		Comment comment=new Comment();
		comment.setCommentId(commentId);
		
		String hql1="delete Like where comment=:comment";
		Query query1=session.createQuery(hql1);
		query1.setParameter("comment", comment);
		
		String hql2="delete Share where comment=:comment";
		Query query2=session.createQuery(hql2);
		query2.setParameter("comment", comment);
		
		String hql3="delete Comment where commentId=:commentId";
		Query query3=session.createQuery(hql3);
		query3.setParameter("commentId",commentId);
		
		Transaction transaction=session.beginTransaction();
		try {
			query1.executeUpdate();
			query2.executeUpdate();
			query3.executeUpdate();
			transaction.commit();
			status=true;
		}catch(Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}finally {
			session.close();
		}
		
		return status;
	}
	
}
