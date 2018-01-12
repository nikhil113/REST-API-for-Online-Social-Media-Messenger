package com.beans;

import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@XmlRootElement
@Entity
@Table(name="comment")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comment {
	
	@Id
	@GenericGenerator(name="commentprimarykeygenerator",strategy="com.utility.CommentPrimaryKeyGenerator")
	@GeneratedValue(generator="commentprimarykeygenerator")
	@Column(name="comment_id",nullable=false)
	private String commentId;
	
	@Column(name="content",nullable=false)
	private String content;
	
	@Column(name="privacy",nullable=false)
	private String privacy;
	
	@Column(name="content_type",nullable=false)
	private String contentType;
	
	@Column(name="created",nullable=true)
	private Date created;
	
	@ManyToOne
	@JoinColumn(name="message_id")
	private Message message;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserProfile user;
	
//	@OneToMany
//	@JoinColumn(name="comment_id")
//	private List<Like> likeList;
//	
//	@OneToMany
//	@JoinColumn(name="comment_id")
//	private List<Share> shareList;

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public Object getContent() {
		return content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContent(String content) {
		this.content = content;
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

//	public List<Like> getLikeList() {
//		return likeList;
//	}
//
//	public void setLikeList(List<Like> likeList) {
//		this.likeList = likeList;
//	}
//
//	public List<Share> getShareList() {
//		return shareList;
//	}
//
//	public void setShareList(List<Share> shareList) {
//		this.shareList = shareList;
//	}

	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", content=" + content + ", privacy=" + privacy + ", contentType="
				+ contentType + ", created=" + created + ", message=" + message + ", user=" + user + "]";
	}

}
