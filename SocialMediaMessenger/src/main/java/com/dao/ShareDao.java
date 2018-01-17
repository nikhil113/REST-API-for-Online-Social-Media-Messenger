package com.dao;



import java.util.ArrayList;
import java.util.List;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.beans.Message;
import com.beans.ResponseWithHateOAS;
import com.beans.Share;
import com.beans.ShareInfo;
import com.beans.UserProfile;
import com.initializer.DatabaseInitializer;

public class ShareDao {
	
	public ResponseWithHateOAS getShareInformationByMessageId(String messageId){
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		Message message=new Message();
		message.setMessageId(messageId);
		
		String hql="from Share where message=:message";
		Query query=session.createQuery(hql);
		query.setParameter("message", message);
		List shareList=query.list();
		ResponseWithHateOAS response=new ResponseWithHateOAS();
		List<String> userIdList=new ArrayList<>();
		long shareCount=0;
		for(Object object:userIdList) {
			Share share=(Share) object;
			
			UserProfile user=share.getUser();
			userIdList.add(user.getUserId());
			shareCount++;
		}
		
		ShareInfo shareInfo=new ShareInfo();
		shareInfo.setUserProfileIdList(userIdList);
		shareInfo.setShareCount(shareCount);
		response.setObject(shareInfo);
		//have to add hateoas
		
		session.close();
		return response;
	}
	
	
	public ResponseWithHateOAS getShareInformationByMessageAndShareId(String messageId, String shareId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		Message message=new Message();
		message.setMessageId(messageId);
		
		String hql="from Share where shareId=:shareId and message=:message";
		Query query=session.createQuery(hql);
		query.setParameter("shareId", shareId);
		query.setParameter("message", message);
		
		Share share=(Share) query.uniqueResult();
		ResponseWithHateOAS response=new ResponseWithHateOAS();
		List<String> userIdList=new ArrayList<>();
		long count=0;
		if(share != null) {
			UserProfile user=share.getUser();
			userIdList.add(user.getUserId());
			count++;
			
			ShareInfo shareInfo=new ShareInfo();
			shareInfo.setUserProfileIdList(userIdList);
			shareInfo.setShareCount(count);
			
			response.setObject(shareInfo);
			//have to add hateoas
		}
		
		session.close();
		return response;
	}
	
	public Share isAlreadyExist(String currentUserId, String messageId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		UserProfile user=new UserProfile();
		user.setUserId(currentUserId);
		
		Message message=new Message();
		message.setMessageId(messageId);
		
		String hql="from Share where user=:user and message=:message";
		Query query=session.createQuery(hql);
		query.setParameter("user", user);
		query.setParameter("message", message);
		Object object=query.uniqueResult();
		Share share=null;
		if(object != null) {
			share=(Share) object;
		}
		
		session.close();
		return share;
	}
	
	public Share postShare(Share share) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		Share insertedShare=null;
		
		session.save(share);
		Transaction transaction=session.beginTransaction();
		try {
			transaction.commit();
			insertedShare=share;
		}catch(Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}finally {
			session.close();
		}
		
		return insertedShare;
	}
	
	public boolean isValidToDelete(String currentUserId, String shareId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		UserProfile user=new UserProfile();
		user.setUserId(currentUserId);
		
		String hql="from Share where shareId=:shareId and user=:user";
		Query query=session.createQuery(hql);
		query.setParameter("shareId", shareId);
		query.setParameter("user", user);
		Object object=query.uniqueResult();
		if(object != null) {
			status=true;
		}
		
		return status;
	}
	
	public boolean deleteShare(String shareId) {
	
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		String hql="delete Share where shareId=:shareId";
		Query query=session.createQuery(hql);
		query.setParameter("shareId", shareId);
		Transaction transaction=session.beginTransaction();
		try {
			query.executeUpdate();
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
