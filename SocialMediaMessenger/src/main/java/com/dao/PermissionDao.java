package com.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.beans.Permission;
import com.initializer.DatabaseInitializer;

public class PermissionDao {
	
	public boolean updatePermissionOfProfile(Permission permission) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		boolean status=false;
		
		session.merge(permission);
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
