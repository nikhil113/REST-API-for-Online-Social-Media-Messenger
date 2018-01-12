package com.beans;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@XmlRootElement
@Entity
@Table(name="permission")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Permission {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long permissionId;
	
	@OneToOne
	@JoinColumn(name="user_profile_id")
	private UserProfile user;
	
	@Column(name="user_name_privacy",nullable=false)
	private String userNamePrivacy;
	
	@Column(name="email_id_privacy",nullable=false)
	private String emailIdPrivacy;
	
	@Column(name="mobile_no_privacy",nullable=false)
	private String mobileNoPrivacy;
	
	@Column(name="address_privacy",nullable=false)
	private String addressPrivacy;
	
	@Column(name="created_privacy",nullable=false)
	private String createdPrivacy;
	
	@Column(name="friends_list_privacy",nullable=false)
	private String friendsListPrivacy;
	
	@Column(name="pending_friend_request_list_privacy",nullable=false)
	private String pendingFriendRequestListPrivacy;
	
	@Column(name="freind_request_sent_list_privacy",nullable=false)
	private String friendRequestSentListPrivacy;

	public long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public String getUserNamePrivacy() {
		return userNamePrivacy;
	}

	public void setUserNamePrivacy(String userNamePrivacy) {
		this.userNamePrivacy = userNamePrivacy;
	}

	public String getEmailIdPrivacy() {
		return emailIdPrivacy;
	}

	public void setEmailIdPrivacy(String emailIdPrivacy) {
		this.emailIdPrivacy = emailIdPrivacy;
	}

	public String getMobileNoPrivacy() {
		return mobileNoPrivacy;
	}

	public void setMobileNoPrivacy(String mobileNoPrivacy) {
		this.mobileNoPrivacy = mobileNoPrivacy;
	}

	public String getAddressPrivacy() {
		return addressPrivacy;
	}

	public void setAddressPrivacy(String addressPrivacy) {
		this.addressPrivacy = addressPrivacy;
	}

	public String getCreatedPrivacy() {
		return createdPrivacy;
	}

	public void setCreatedPrivacy(String createdPrivacy) {
		this.createdPrivacy = createdPrivacy;
	}

	public String getFriendsListPrivacy() {
		return friendsListPrivacy;
	}

	public void setFriendsListPrivacy(String friendsListPrivacy) {
		this.friendsListPrivacy = friendsListPrivacy;
	}

	public String getPendingFriendRequestListPrivacy() {
		return pendingFriendRequestListPrivacy;
	}

	public void setPendingFriendRequestListPrivacy(String pendingFriendRequestListPrivacy) {
		this.pendingFriendRequestListPrivacy = pendingFriendRequestListPrivacy;
	}

	public String getFriendRequestSentListPrivacy() {
		return friendRequestSentListPrivacy;
	}

	public void setFriendRequestSentListPrivacy(String friendRequestSentListPrivacy) {
		this.friendRequestSentListPrivacy = friendRequestSentListPrivacy;
	}

	@Override
	public String toString() {
		return "Permission [permissionId=" + permissionId + ", user=" + user + ", userNamePrivacy=" + userNamePrivacy
				+ ", emailIdPrivacy=" + emailIdPrivacy + ", mobileNoPrivacy=" + mobileNoPrivacy + ", addressPrivacy="
				+ addressPrivacy + ", createdPrivacy=" + createdPrivacy + ", friendsListPrivacy=" + friendsListPrivacy
				+ ", pendingFriendRequestListPrivacy=" + pendingFriendRequestListPrivacy
				+ ", friendRequestSentListPrivacy=" + friendRequestSentListPrivacy + "]";
	}

}
