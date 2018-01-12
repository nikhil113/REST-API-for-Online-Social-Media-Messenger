package com.utility;

import com.beans.Token;
import com.constants.TokenConstants;
import com.dao.TokenDao;

public class TokenDeletionThread extends Thread{
	
	private Token token;
	private TokenDao tokenDao;
	
	public TokenDeletionThread(Token token,TokenDao tokenDao){
		this.token=token;
		this.tokenDao=tokenDao;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(TokenConstants.TOKEN_VALIDITY*60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		tokenDao.deleteToken(token);
	}

}
