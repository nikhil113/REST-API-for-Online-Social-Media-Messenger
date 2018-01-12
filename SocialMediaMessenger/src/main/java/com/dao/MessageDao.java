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

public class MessageDao {
	
	public List<ResponseWithHateOAS> getAllMessages(String currentUserId){
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		String hql="from Message";
		Query query=session.createQuery(hql);
		List messageList=query.list();
		List<ResponseWithHateOAS> responseList=new ArrayList<>();
		
		for(Object object:messageList) {
			Message message=(Message) object;
			
			UserProfile user=message.getUser();
			List<String> friendList=user.getFriendList();
			boolean isFriend=false;
			for(String id:friendList) {
				if(id.equals(currentUserId)) {
					isFriend=true;
					break;
				}
			}
			
			if(message.getPrivacy().equalsIgnoreCase("PUBLIC") || isFriend) {
				ResponseWithHateOAS response=new ResponseWithHateOAS();
				response.setObject(message);
				
				// have to add hateoas
				
				responseList.add(response);
			}
			
		}
		
		session.close();
		return responseList;
	}
	
	public Message getMessageById(String currentUserId, String messageId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		String hql="from Message where messageId=:messageId";
		Query query=session.createQuery(hql);
		query.setParameter("messageId", messageId);
		Message message=(Message) query.uniqueResult();
		session.close();
		if(message != null) {
			UserProfile user=message.getUser();
			List<String> friendList=user.getFriendList();
			boolean isFriend=false;
			for(String id:friendList) {
				if(id.equals(currentUserId)) {
					isFriend=true;
					break;
				}
			}
			
			if(message.getPrivacy().equalsIgnoreCase("PUBLIC") || isFriend) {
				return message;
			}else {
				return null;
			}
		}else {
			return null;
		}
	}
	
	
	public Message postMessage(Message message) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		session.save(message);
		
		Transaction transaction=session.beginTransaction();
		try {
			transaction.commit();
			status=true;
		}catch(Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}
		finally {
			session.close();
		}
		
		Message finalMessage=message;
		return message;
	}
	
	public boolean isValidToUpdate(String currentUserId, String messageId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		UserProfile user=new UserProfile();
		user.setUserId(currentUserId);
		
		String hql="from Message where messageId=:messageId and user=:user";
		Query query=session.createQuery(hql);
		query.setParameter("messageId", messageId);
		query.setParameter("user",user);
		
		Object object=query.uniqueResult();
		session.close();
		if(object != null) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public boolean updateMessage(Message message) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		session.merge(message);
		Transaction transaction=session.beginTransaction();
		try {
			transaction.commit();
			status=true;
		}catch(Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}
		finally {
			session.close();
		}
		return status;
	}
	
	public boolean deleteMessage(String messageId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		Message message=new Message();
		message.setMessageId(messageId);
		
		String hql1="from Comment where message=:message";
		Query query1=session.createQuery(hql1);
		query1.setParameter("message", message);
		List commentList=query1.list();
		List<Query> queryList=new ArrayList<>();
		for(Object object:commentList) {
			Comment comment=(Comment) object;
			
			String hql2="delete Like where comment=:comment";
			Query query2=session.createQuery(hql2);
			query2.setParameter("comment", comment);
			queryList.add(query2);
			
			String hql3="delete Share where comment=:comment";
			Query query3=session.createQuery(hql3);
			query3.setParameter("comment", comment);
			queryList.add(query3);
			
			String hql4="delete Comment where commentId=:commentId";
			Query query4=session.createQuery(hql4);
			query4.setParameter("commentId", comment.getCommentId());
			queryList.add(query4);
		}
		
		String hql5="delete Like where message=:message";
		Query query5=session.createQuery(hql5);
		query5.setParameter("message", message);
		queryList.add(query5);
		
		String hql6="delete Share where message=:message";
		Query query6=session.createQuery(hql6);
		query6.setParameter("message", message);
		queryList.add(query6);
		
		String hql7="delete Message where messageId=:messageId";
		Query query7=session.createQuery(hql7);
		query7.setParameter("messageId", messageId);
		queryList.add(query7);
		
		Transaction transaction=session.beginTransaction();
		try {
			for(Query query:queryList) {
				query.executeUpdate();
			}
			
			transaction.commit();
			status=true;
		}catch(Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}
		
		return status;
	}
	

}
