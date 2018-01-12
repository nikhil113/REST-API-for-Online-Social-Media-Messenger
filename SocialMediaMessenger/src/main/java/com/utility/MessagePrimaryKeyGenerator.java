package com.utility;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class MessagePrimaryKeyGenerator implements IdentifierGenerator{

	static long value=0l;

	private static synchronized String getId() {
		
		String id="MESSAGE_";
		value++;
		id=id+String.valueOf(value);
		return id;
		
	}


	@Override
	public Serializable generate(SharedSessionContractImplementor arg0, Object arg1) throws HibernateException {
		return getId();
	}
	
}
