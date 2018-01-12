package com.dao;



import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.beans.Message;
import com.beans.ResponseWithHateOAS;
import com.initializer.DatabaseInitializer;

public class LikeDao {
	
	public List<ResponseWithHateOAS> getLikeInformationByMessageId(String messageId) {
		
		DatabaseInitializer initializer=new DatabaseInitializer();
		Session session=initializer.getSession();
		
		Message message=new Message();
		message.setMessageId(messageId);
		
		String hql="from Like where message=:message";
		Query query=session.createQuery(hql);
		query.setParameter("message", message);
		
		List likeList=query.list();

		
		return null;
	}

}
