package com.initializer;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DatabaseInitializer {
	
	static SessionFactory sf;
	
	static {
		Configuration configuration=new Configuration();
		configuration.configure("resources/hibernate-cfg.xml");
		sf=configuration.buildSessionFactory();
	}
	
	public Session getSession() {
		Session session=sf.openSession();
		return session;
	}

}
