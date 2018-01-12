package com.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.beans.Token;
import com.beans.UserProfile;
import com.initializer.DatabaseInitializer;

public class TokenDao {
	
	public boolean validate(String userId,String password) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		String hql="select userId from UserProfile where userId=:userId and password=:password";
		Query query=session.createQuery(hql);
		query.setParameter("userId", userId);
		query.setParameter("password", password);
		query.setCacheable(true);
		Object user=query.uniqueResult();
		session.close();
		if(user!=null) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public Token insertToken(Token token) {
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		Token tokenToReturn=null;
		session.save(token);
		try {
			Transaction transaction=session.beginTransaction();
			transaction.commit();
			tokenToReturn=token;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		session.close();
		return tokenToReturn;
	}
	
	public void deleteToken(Token token) {
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		session.delete(token);
		Transaction transaction=session.beginTransaction();
		try {
			transaction.commit();
		}catch(Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}finally {
			session.close();
		}
	}
	
	public Token getTokenByUserId(String userId) {
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		Token token=null;
		UserProfile user=new UserProfile();
		user.setUserId(userId);
	
		String hql="from Token where user=:user";
		Query query=session.createQuery(hql);
		query.setParameter("user", user);
		query.setCacheable(true);
		token=(Token) query.uniqueResult();
	
		session.close();
		return token;
	}

}
