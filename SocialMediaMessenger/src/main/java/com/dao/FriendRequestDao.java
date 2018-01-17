package com.dao;



import java.util.List;



import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.beans.UserProfile;
import com.initializer.DatabaseInitializer;

public class FriendRequestDao {
	
	public boolean isAlreadyExist(String currentUserId, String profileId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		
		String hql="from UserProfile where userId=:userId";
		Query query=session.createQuery(hql);
		query.setParameter("userId", profileId);
		UserProfile user=(UserProfile) query.uniqueResult();
		
		List<String> friendList=user.getFriendList();
		boolean isPresentInFriendList=false;
		for(String id:friendList) {
			if(id.equals(currentUserId)) {
				isPresentInFriendList=true;
				break;
			}
		}
		
		List<String> pendingFriendRequestList=user.getPendingFriendRequestList();
		boolean isPresentInPendingFriendRequestList=false;
		for(String id:pendingFriendRequestList) {
			if(id.equals(currentUserId)) {
				isPresentInPendingFriendRequestList=true;
				break;
			}
		}
		
		session.close();
		if(isPresentInFriendList || isPresentInPendingFriendRequestList) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public boolean postFriendRequest(String currentUserId, String profileId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		String hql="from UserProfile where userId=:userId";
		Query query=session.createQuery(hql);
		query.setParameter("userId", profileId);
		UserProfile user=(UserProfile) query.uniqueResult();
		
		List<String> pendingFriendRequestList=user.getPendingFriendRequestList();
		pendingFriendRequestList.add(currentUserId);
		user.setPendingFriendRequestList(pendingFriendRequestList);
		session.merge(user);
		
		String hql2="from UserProfile where userId=:userId";
		Query query2=session.createQuery(hql2);
		query2.setParameter("userId", currentUserId);
		UserProfile user2=(UserProfile) query.uniqueResult();
		
		List<String> sentFriendRequestList=user2.getFriendRequestSentList();
		sentFriendRequestList.add(profileId);
		user2.setFriendRequestSentList(sentFriendRequestList);
		session.merge(user2);
		
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
	
	public boolean isPresentInPendingFreindRequestList(String currentUserId, String profileId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		String hql="from UserProfile userId=:userId";
		Query query=session.createQuery(hql);
		query.setParameter("userId", currentUserId);
		UserProfile user=(UserProfile) query.uniqueResult();
		
		List<String> pendingFriendRequestList=user.getPendingFriendRequestList();
		for(String id:pendingFriendRequestList) {
			if(id.equals(profileId)) {
				status=true;
				break;
			}
		}
		
		return status;
	}
	
	public boolean confirmFriendRequest(String currentUserId, String profileId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		String hql1="from UserProfile where userId=:userId";
		Query query1=session.createQuery(hql1);
		query1.setParameter("userId", currentUserId);
		UserProfile user1=(UserProfile) query1.uniqueResult();
		
		List<String> pendingFriendRequestList=user1.getPendingFriendRequestList();
		pendingFriendRequestList.remove(profileId);
		user1.setPendingFriendRequestList(pendingFriendRequestList);
		
		List<String> friendList1=user1.getFriendList();
		friendList1.add(profileId);
		user1.setFriendList(friendList1);
		
		session.merge(user1);
		
		String hql2="from UserProfile where userId=:userId";
		Query query2=session.createQuery(hql2);
		query2.setParameter("userId", profileId);
		UserProfile user2=(UserProfile) query2.uniqueResult();
		
		List<String> friendRequestSentList=user2.getFriendRequestSentList();
		friendRequestSentList.remove(currentUserId);
		user2.setFriendRequestSentList(friendRequestSentList);
		
		List<String> friendList2=user2.getFriendList();
		friendList2.add(currentUserId);
		user2.setFriendList(friendList2);
		
		session.merge(user2);
		
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

}
