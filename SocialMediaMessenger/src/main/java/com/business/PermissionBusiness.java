package com.business;

import javax.ws.rs.core.Response;

import com.beans.Permission;
import com.beans.UserProfile;
import com.dao.PermissionDao;

public class PermissionBusiness {

	public Response updatePermissionOfProfile(String currentUserId, Permission permission) {
		
		UserProfile user=new UserProfile();
		user.setUserId(currentUserId);
		permission.setUser(user);
		
		PermissionDao permissionDao=new PermissionDao();
		boolean status=permissionDao.updatePermissionOfProfile(permission);
		if(status) {
			return Response.noContent().build();
		}else {
			return Response.serverError().build();
		}
		
	}
	
	
}
