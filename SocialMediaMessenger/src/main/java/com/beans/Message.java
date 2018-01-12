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
import org.hibernate.annotations.NotFound;

@XmlRootElement
@Entity
@Table(name="message")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Message {
	
	@Id
	@GenericGenerator(name="messageprimarykeygenerator",strategy="com.utility.MessagePrimaryKeyGenerator")
	@GeneratedValue(generator="messageprimarykeygenerator")
	@Column(name="message_id",nullable=false)
	private String messageId;
	
	@Column(name="content",nullable=false)
	private String content;
	
	@Column(name="content_type",nullable=false)
	private String contentType;
	
	@Column(name="created",nullable=true)
	private Date created;
	
	@Column(name="privacy",nullable=false)
	private String privacy;
	
	@ManyToOne
	@JoinColumn(name="user_profile_id")
	private UserProfile user;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", content=" + content + ", contentType=" + contentType
				+ ", created=" + created + ", privacy=" + privacy + ", user=" + user + "]";
	}
	
}
