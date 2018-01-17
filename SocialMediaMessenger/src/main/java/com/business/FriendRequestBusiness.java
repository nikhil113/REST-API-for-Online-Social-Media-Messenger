package com.business;

import javax.ws.rs.core.Response;

import com.dao.FriendRequestDao;

public class FriendRequestBusiness {
	
	public Response postFriendRequest(String currentUserId, String profileId) {
		
		FriendRequestDao friendRequestDao=new FriendRequestDao();
		boolean status=friendRequestDao.isAlreadyExist(currentUserId, profileId);
		if(status) {
			return Response.status(400).build();
		}else {
			status=friendRequestDao.postFriendRequest(currentUserId, profileId);
			if(status) {
				return Response.ok().build();
			}else {
				return Response.serverError().build();
			}
		}

	}
	
	public Response confirmfriendRequest(String currentUserId, String profileId) {
		
		FriendRequestDao friendRequestDao=new FriendRequestDao();
		boolean isPresent=friendRequestDao.isPresentInPendingFreindRequestList(currentUserId, profileId);
		if(isPresent) {
			boolean status=friendRequestDao.confirmFriendRequest(currentUserId, profileId);
			if(status) {
				return Response.ok().build();
			}else {
				return Response.serverError().build();
			}
		}else {
			return Response.status(400).build();
		}
		
	}

}
