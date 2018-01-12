package com.business;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;

import com.beans.Token;
import com.beans.UserProfile;
import com.constants.TokenConstants;
import com.dao.TokenDao;
import com.exceptions.UnAuthorizedException;
import com.utility.TokenDeletionThread;

public class TokenBusiness {
	
	public String generateMD5Hash(String string) throws NoSuchAlgorithmException {
		
		MessageDigest md=MessageDigest.getInstance("MD5");
		md.update(string.getBytes());
		byte arr[]=md.digest();
		StringBuffer sb=new StringBuffer();
		for(byte b:arr) {
			sb.append(Integer.toHexString(b & 0xff).toString());
		}
		
		return sb.toString();
		
	}
	
	public Response getToken(String userId, String password) {
		try {
			password=generateMD5Hash(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		TokenDao tokenDao=new TokenDao();
		boolean value=tokenDao.validate(userId,password);
		if(value) {
			
			Token alreadyPresentToken=tokenDao.getTokenByUserId(userId);
			if(alreadyPresentToken==null) {
			
				Calendar calendar=Calendar.getInstance();
				calendar.add(Calendar.MINUTE, TokenConstants.TOKEN_VALIDITY);
				long time=calendar.getTimeInMillis();
				
				String token=userId+":"+password+" "+String.valueOf(time);
				Token tokenToInsert=new Token();
				tokenToInsert.setToken(token);
				UserProfile user=new UserProfile();
				user.setUserId(userId);
				tokenToInsert.setUser(user);
				
				Token returnedToken=tokenDao.insertToken(tokenToInsert);
				TokenDeletionThread thread=new TokenDeletionThread(returnedToken,tokenDao);
				thread.start();
				
				CacheControl cc=new CacheControl();
				cc.setPrivate(true);
				cc.setNoTransform(true);
				cc.setMaxAge(TokenConstants.TOKEN_VALIDITY*60);
				
				return Response.ok(token).cacheControl(cc).build();
			}else {
				
				String token1[]=alreadyPresentToken.getToken().split(" ");
				long tokenTimeStamp=Long.parseLong(token1[1]);
				long curTimeStamp=System.currentTimeMillis();
				long timeToCache=(tokenTimeStamp-curTimeStamp)/1000;
				CacheControl cc=new CacheControl();
				cc.setPrivate(true);
				cc.setNoTransform(true);
				cc.setMaxAge((int) timeToCache);
				
				return Response.status(200).entity(alreadyPresentToken.getToken()).cacheControl(cc).build();
			}
		}else {
			throw new UnAuthorizedException("UserId and Password does not match !!!");
		}
		
	}

}
