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

public class MessageLinkedWithProfileDao {
	
	public List<ResponseWithHateOAS> getAllMessagesByProfileId(String currentUserId, String profileId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		UserProfile userProfile=new UserProfile();
		userProfile.setUserId(profileId);
		
		String hql="from Message where user=:user";
		Query query=session.createQuery(hql);
		query.setParameter("user", userProfile);
		List<Object> list=query.list();
		
		List<ResponseWithHateOAS> responseList=new ArrayList<>();
		for(Object object:list) {
			Message message=(Message) object;
			
			UserProfile user=message.getUser();
			List<String> friendList=user.getFriendList();
			boolean isFriend=false;
			for(String userId:friendList) {
				if(userId.equals(currentUserId)) {
					isFriend=true;
					break;
				}
			}
			
			if(message.getPrivacy().equalsIgnoreCase("PUBLIC") || isFriend) {
				ResponseWithHateOAS response=new ResponseWithHateOAS();
				response.setObject(message);
				// have to add hateoas...
				responseList.add(response);
			}
			
		}
		
		session.close();
		return responseList;
		
	}
	
	public ResponseWithHateOAS getMessageByProfileAndMessageId(String currentUserId, String profileId, String messageId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		UserProfile userProfile=new UserProfile();
		userProfile.setUserId(profileId);
		
		String hql="from Message where user=:user and messageId=:messageId";
		Query query=session.createQuery(hql);
		query.setParameter("user", userProfile);
		query.setParameter("messageId", messageId);
		
		Message message=(Message) query.uniqueResult();
		
		ResponseWithHateOAS response=new ResponseWithHateOAS();
		if(message!=null){
			
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
				response.setObject(message);
				// have to add hateoas
			}
			
		}
		
		session.close();
		return response;
	}
	
	
	public Message postMessage(Message message, String currentUserId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		UserProfile userProfile=new UserProfile();
		userProfile.setUserId(currentUserId);
		message.setUser(userProfile);
		session.save(message);
		Transaction transaction=session.beginTransaction();
		Message messageToReturn=null;
		try {
			transaction.commit();
			messageToReturn=message;
		}catch(Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}
		finally {
			session.close();
		}
		
		return messageToReturn;
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
	
	public boolean isAuthorizedToUpdate(String profileId, String messageId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		boolean isAuthorized=false;
		UserProfile user=new UserProfile();
		user.setUserId(profileId);
		String hql="from Message where user=:user and messageId=:messageId";
		Query query=session.createQuery(hql);
		query.setParameter("user", user);
		query.setParameter("messageId", messageId);
		Message message=(Message) query.uniqueResult();
		if(message != null) {
			isAuthorized=true;
		}else {
			isAuthorized=false;
		}
		
		session.close();
		return isAuthorized;
	}
	
	public boolean deleteMessageById(Message message) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		String hql1="from Comment where message=:message";
		Query query1=session.createQuery(hql1);
		query1.setParameter("message", message);
		List list=query1.list();
		int size=list.size();
		Query query2[]=new Query[size];
		Query query3[]=new Query[size];
		Query query4[]=new Query[size];
		int i=-1;
		for(Object object:list) {
			Comment comment=(Comment) object;
			i++;
			String hql2="delete Like where comment=:comment";
			query2[i]=session.createQuery(hql2);
			query2[i].setParameter("comment", comment);
			
			String hql3="delete Share where comment=:comment";
			query3[i]=session.createQuery(hql3);
			query3[i].setParameter("comment", comment);
			
			String hql4="delete Comment where commentId=:commentId";
			query4[i]=session.createQuery(hql4);
			query4[i].setParameter("commentId", comment.getCommentId());
		}
		
		String hql5="delete Like where message=:message";
		Query query5=session.createQuery(hql5);
		query5.setParameter("message", message);
		
		String hql6="delete Share where message=:message";
		Query query6=session.createQuery(hql6);
		query6.setParameter("message", message);
		
		String hql7="delete Message where messageId=:messageId";
		Query query7=session.createQuery(hql7);
		query7.setParameter("messageId", message.getMessageId());
		
		Transaction transaction=session.beginTransaction();
		try {
			for(i=0;i<size;i++) {
				query2[i].executeUpdate();
				query3[i].executeUpdate();
				query4[i].executeUpdate();
			}
			query5.executeUpdate();
			query6.executeUpdate();
			query7.executeUpdate();
			
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
	
	public void deleteAllMessageByProfileId(String profileId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		UserProfile user=new UserProfile();
		user.setUserId(profileId);
		
		String hql="from Message where user=:user";
		Query query=session.createQuery(hql);
		query.setParameter("user", user);
		List messageList=query.list();
		for(Object object:messageList) {
			Message message=(Message) object;
			deleteMessageById(message);
		}
		
		session.close();
	}

}
