package com.beans;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@XmlRootElement
@Entity
@Table(name="share")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Share {
	
	@Id
	@GenericGenerator(name="shareprimarykeygenerator",strategy="com.utility.SharePrimaryKeyGenerator")
	@GeneratedValue(generator="shareprimarykeygenerator")
	@Column(name="share_id",nullable=false)
	private String shareId;
	
	@ManyToOne
	@JoinColumn(name="message_id")
	private Message message;
	
	@ManyToOne
	@JoinColumn(name="user_profile_id")
	private UserProfile user;
	
	@ManyToOne
	@JoinColumn(name="comment_id")
	private Comment comment;
	
	@Column(name="created",nullable=true)
	private Date created;

	public String getShareId() {
		return shareId;
	}

	public void setShareId(String shareId) {
		this.shareId = shareId;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Share [shareId=" + shareId + ", message=" + message + ", user=" + user + ", comment=" + comment
				+ ", created=" + created + "]";
	}

}
