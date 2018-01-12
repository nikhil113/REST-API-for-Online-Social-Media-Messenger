package com.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.beans.Links;
import com.beans.Permission;
import com.beans.ResponseWithHateOAS;
import com.beans.UserProfile;
import com.initializer.DatabaseInitializer;

public class ProfileDao {
	
	public UserProfile createProfile(UserProfile user) {
		
		
		UserProfile userProfile=null;
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		session.save(user);
		
		List<String> friendList=new ArrayList<>();
		friendList.add(user.getUserId());
		user.setFriendList(friendList);
		
		Permission permission=new Permission();
		permission.setUser(user);
		permission.setUserNamePrivacy("PRIVATE");
		permission.setEmailIdPrivacy("PRIVATE");
		permission.setMobileNoPrivacy("PRIVATE");
		permission.setAddressPrivacy("PRIVATE");
		permission.setCreatedPrivacy("PRIVATE");
		permission.setFriendsListPrivacy("PRIVATE");
		permission.setFriendRequestSentListPrivacy("PRIVATE");
		permission.setPendingFriendRequestListPrivacy("PRIVATE");
		
		session.save(permission);
		
		Transaction transaction=session.beginTransaction();
		try {
			transaction.commit();
			userProfile=user;
		}catch(Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		finally {
			session.close();
		}
		
		return userProfile;
	}
	
	public boolean checkAvailibility(String emailId) {
		boolean status=true;
		if(emailId==null || emailId.isEmpty()) {
			status=false;
		}else {
			DatabaseInitializer initializer=new DatabaseInitializer();
			Session session=initializer.getSession();
			String hql="select email from UserProfile where email=:email";
			Query query=session.createQuery(hql);
			query.setParameter("email",emailId);
			query.setCacheable(true);
			List<Object> list=query.list();
			if(list.size()==1) {
				status=false;
			}
			session.close();
		}
		
		return status;
	}
	
	public List<ResponseWithHateOAS> getAllProfiles(String currentUserId, UriInfo uriInfo){
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		String getAllPermissions="from Permission";
		Query permissionQuery=session.createQuery(getAllPermissions);
		List<Object> permissionList=permissionQuery.list();
		List<ResponseWithHateOAS> responseList=new ArrayList<>();
		for(Object object:permissionList) {
			Permission permission=(Permission) object;
			UserProfile user=permission.getUser();
			String userNamePrivacy=permission.getUserNamePrivacy();
			String emailIdPrivacy=permission.getEmailIdPrivacy();
			String mobileNoPrivacy=permission.getMobileNoPrivacy();
			String addressPrivacy=permission.getAddressPrivacy();
			String createdPrivacy=permission.getCreatedPrivacy();
			String friendsListPrivacy=permission.getFriendsListPrivacy();
			String pendingFriendRequestListPrivacy=permission.getPendingFriendRequestListPrivacy();
			String friendRequestSentListPrivacy=permission.getFriendRequestSentListPrivacy();
			
			List<String> friendList=user.getFriendList();
			boolean isFriend=false;
			for(String id:friendList) {
				if(id.equals(currentUserId)) {
					isFriend=true;
					break;
				}
			}
			
			UserProfile userProfile=new UserProfile();
			userProfile.setUserId(user.getUserId());
			boolean isSet=false;
			
			if(userNamePrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
				userProfile.setUserName(user.getUserName());
				isSet=true;
			}
			
			if(emailIdPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
				userProfile.setEmail(user.getEmail());
				isSet=true;
			}
			
			if(mobileNoPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
				userProfile.setMobileNo(user.getMobileNo());
				isSet=true;
			}
			
			if(addressPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
				userProfile.setAddress(user.getAddress());
				isSet=true;
			}
			
			if(createdPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
				userProfile.setCreated(user.getCreated());
				isSet=true;
			}
			
			if(friendsListPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
				userProfile.setFriendList(user.getFriendList());
				isSet=true;
			}
			
			if(pendingFriendRequestListPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
				userProfile.setPendingFriendRequestList(user.getPendingFriendRequestList());
				isSet=true;
			}
			if(friendRequestSentListPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
				userProfile.setFriendRequestSentList(user.getFriendRequestSentList());
				isSet=true;
			}
			
			if(isSet) {

				ResponseWithHateOAS response=new ResponseWithHateOAS();
				response.setObject(userProfile);
				
				UriBuilder uriBuilder=uriInfo.getAbsolutePathBuilder().path("messages/").path(userProfile.getUserId());
				URI uri=uriBuilder.build(uriBuilder);
				Links links=new Links();
				links.setUri(uri);
				links.setRel("link to all messages of cuurent user");
				List<Links> linksList=new ArrayList<>();
				linksList.add(links);
				
				response.setLinksList(linksList);
				
				responseList.add(response);
			}
			
		}
		
		session.close();
		return responseList;
	}
	
	
	public ResponseWithHateOAS getProfileById(String currentUserId, String wantedUserId, UriInfo uriInfo) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		UserProfile user=new UserProfile();
		user.setUserId(wantedUserId);
		String hql="from Permission where user=:user";
		Query query=session.createQuery(hql);
		query.setParameter("user", user);
		Permission permission=(Permission) query.uniqueResult();
		
		user=permission.getUser();
		String userNamePrivacy=permission.getUserNamePrivacy();
		String emailIdPrivacy=permission.getEmailIdPrivacy();
		String mobileNoPrivacy=permission.getMobileNoPrivacy();
		String addressPrivacy=permission.getAddressPrivacy();
		String createdPrivacy=permission.getCreatedPrivacy();
		String friendsListPrivacy=permission.getFriendsListPrivacy();
		String pendingFriendRequestListPrivacy=permission.getPendingFriendRequestListPrivacy();
		String friendRequestSentListPrivacy=permission.getFriendRequestSentListPrivacy();
		
		List<String> friendList=user.getFriendList();
		boolean isFriend=false;
		for(String id:friendList) {
			if(id.equals(currentUserId)) {
				isFriend=true;
				break;
			}
		}
		
		UserProfile userProfile=new UserProfile();
		userProfile.setUserId(wantedUserId);
		
		if(userNamePrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
			userProfile.setUserName(user.getUserName());
			
		}
		
		if(emailIdPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
			userProfile.setEmail(user.getEmail());
			
		}
		
		if(mobileNoPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
			userProfile.setMobileNo(user.getMobileNo());
			
		}
		
		if(addressPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
			userProfile.setAddress(user.getAddress());
			
		}
		
		if(createdPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
			userProfile.setCreated(user.getCreated());
	
		}
		
		if(friendsListPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
			userProfile.setFriendList(user.getFriendList());
			
		}
		
		if(pendingFriendRequestListPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
			userProfile.setPendingFriendRequestList(user.getPendingFriendRequestList());
		}
		
		if(friendRequestSentListPrivacy.equalsIgnoreCase("PUBLIC") || isFriend) {
			userProfile.setFriendRequestSentList(user.getFriendRequestSentList());
		}
		
		System.out.println("absolute path ============ "+uriInfo.getAbsolutePath());
		System.out.println("base path ============= "+uriInfo.getBaseUri());
		
		ResponseWithHateOAS response=new ResponseWithHateOAS();
		response.setObject(userProfile);
		
		// have to add hateoas
		
		session.close();
		return response;
	}
	
	public boolean updateProfileById(UserProfile userProfile) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		session.merge(userProfile);
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
	
	public boolean deleteProfileById(String userProfileId,String userName,String password) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		
		UserProfile userProfile=new UserProfile();
		userProfile.setUserId(userProfileId);
		userProfile.setUserName("dummy");
		userProfile.setPassword("dummy");
		session.merge(userProfile);
		
		String hql1="delete Permission where user=:user";
		Query query1=session.createQuery(hql1);
		query1.setParameter("user", userProfile);
		
		String hql2="delete Token where user=:user";
		Query query2=session.createQuery(hql2);
		query2.setParameter("user", userProfile);
		
		String hql3="delete Message where user=:user";
		Query query3=session.createQuery(hql3);
		query3.setParameter("user", userProfile);
		
		String hql4="delete Comment where user=:user";
		Query query4=session.createQuery(hql4);
		query4.setParameter("user", userProfile);
		
		String hql5="delete Like where user=:user";
		Query query5=session.createQuery(hql5);
		query5.setParameter("user", userProfile);
		
		String hql6="delete Share where user=:user";
		Query query6=session.createQuery(hql6);
		query6.setParameter("user", userProfile);
		
		
		Transaction transaction=session.beginTransaction();
		try {
			query1.executeUpdate();
			query2.executeUpdate();
			query3.executeUpdate();
			query4.executeUpdate();
			query5.executeUpdate();
			query6.executeUpdate();
			
			transaction.commit();
			session.close();
			
			session=initializer.getSession();
			session.delete(userProfile);
			session.beginTransaction().commit();
			
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
