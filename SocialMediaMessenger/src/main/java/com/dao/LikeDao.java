package com.dao;



import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.beans.Like;
import com.beans.LikeInfo;
import com.beans.Message;
import com.beans.ResponseWithHateOAS;
import com.beans.UserProfile;
import com.initializer.DatabaseInitializer;

public class LikeDao {
	
	public ResponseWithHateOAS getLikeInformationByMessageId(String messageId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		Message message=new Message();
		message.setMessageId(messageId);
		
		String hql="from Like where message=:message";
		Query query=session.createQuery(hql);
		query.setParameter("message", message);
		
		List likeList=query.list();
		ResponseWithHateOAS response=new ResponseWithHateOAS();
		List<String> userIdList=new ArrayList<>();
		long likesCount=0;
		for(Object object:likeList) {
			Like like=(Like) object;
			UserProfile user=like.getUser();
			userIdList.add(user.getUserId());
			likesCount++;
		}
		
		LikeInfo likeInfo=new LikeInfo();
		likeInfo.setUserProfileId(userIdList);
		likeInfo.setLikeCount(likesCount);
		response.setObject(likeInfo);
		// have to add hateoas.
		
		session.close();
		return response;
	}
	
	public ResponseWithHateOAS getLikeInfoByMessageIdAndLikeId(String likeId, String messageId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		Message message=new Message();
		message.setMessageId(messageId);
		
		String hql="from Like where likeId=:likeId and message=:message";
		Query query=session.createQuery(hql);
		query.setParameter("likeId", likeId);
		query.setParameter("message", message);
		Like like=(Like) query.uniqueResult();
		ResponseWithHateOAS response=new ResponseWithHateOAS();
		
		if(like != null) {
			List<String> userProfileIdList=new ArrayList<>();
			userProfileIdList.add(like.getUser().getUserId());
			
			LikeInfo likeInfo=new LikeInfo();
			likeInfo.setUserProfileId(userProfileIdList);
			likeInfo.setLikeCount(1);
			
			
			response.setObject(likeInfo);
			//have to add hateoas
		}
		
		session.close();
		return response;
	}
	
	
	public Like isAlreadyExist(String currentUserId, String messageId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		UserProfile user=new UserProfile();
		user.setUserId(currentUserId);
		
		Message message=new Message();
		message.setMessageId(messageId);
		
		String hql="from Like where user=:user and message=:message";
		Query query=session.createQuery(hql);
		query.setParameter("user", user);
		query.setParameter("message", message);
		Object object=query.uniqueResult();
		Like like=null;
		if(object != null) {
			like=(Like) object;
		}
		
		session.close();
		return like;
	}
	
	public Like postLike(Like like) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		session.save(like);
		Like insertedLike=null;
		Transaction transaction=session.beginTransaction();
		try {
			transaction.commit();
			insertedLike=like;
		}catch(Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}
		finally {
			session.close();
		}
		
		return insertedLike;
	}
	
	public boolean isValidToDelete(String currentUserId,String likeId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		UserProfile user=new UserProfile();
		user.setUserId(currentUserId);
		
		String hql="from Like where likeId=:likeId and user=:user";
		Query query=session.createQuery(hql);
		query.setParameter("likeId", likeId);
		query.setParameter("user", user);
		Object object=query.uniqueResult();
		if(object != null) {
			status=true;
		}
		
		session.close();
		return status;
	}
	
	public boolean deleteLike(String likeId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		String hql="delete Like where likeId=:likeId";
		Query query=session.createQuery(hql);
		query.setParameter("likeId", likeId);
		Transaction transaction=session.beginTransaction();
		try {
			query.executeUpdate();
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

}
