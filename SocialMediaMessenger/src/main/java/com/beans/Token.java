package com.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table(name="token")
public class Token {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="token_id",nullable=false)
	private long tokenId;
	
	@Column(name="token",nullable=false)
	private String token;
	
	@OneToOne
	@JoinColumn(name="user_profile_id")
	private UserProfile user;

	public long getTokenId() {
		return tokenId;
	}

	public void setTokenId(long tokenId) {
		this.tokenId = tokenId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Token [tokenId=" + tokenId + ", token=" + token + ", user=" + user + "]";
	}
	
}
