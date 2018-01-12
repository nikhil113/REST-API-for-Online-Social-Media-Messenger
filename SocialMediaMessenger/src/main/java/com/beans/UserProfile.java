package com.beans;

import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@XmlRootElement
@Entity
@Table(name="user_profile")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class UserProfile {
	
	@Id
	@GenericGenerator(name="userprimarykeygenerator",strategy="com.utility.UserProfilePrimaryKeyGenerator")
	@GeneratedValue(generator="userprimarykeygenerator")
	@Column(name="user_id",nullable=false)
	private String userId;
	
	@Column(name="user_name",nullable=false)
	private String userName;
	
	@Column(name="password",nullable=false)
	private String password;
	
	@Column(name="email",nullable=true)
	private String email;
	
	@Column(name="mobile_no",nullable=true)
	private long mobileNo;
	
	@Embedded
	@AttributeOverrides({@AttributeOverride(name="street",column=@Column(name="street",nullable=true)),
						@AttributeOverride(name="city",column=@Column(name="city",nullable=true)),
						@AttributeOverride(name="state",column=@Column(name="state",nullable=true)),
						@AttributeOverride(name="pincode",column=@Column(name="pincode",nullable=true))})
	private Address address;
	
	@Column(nullable=true)
	private Date created;
	
	@ElementCollection
	private List<String> friendList;
	
	@ElementCollection
	private List<String> pendingFriendRequestList;
	
	@ElementCollection
	private List<String> friendRequestSentList;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public List<String> getFriendList() {
		return friendList;
	}

	public void setFriendList(List<String> friendList) {
		this.friendList = friendList;
	}

	public List<String> getPendingFriendRequestList() {
		return pendingFriendRequestList;
	}

	public void setPendingFriendRequestList(List<String> pendingFriendRequestList) {
		this.pendingFriendRequestList = pendingFriendRequestList;
	}

	public List<String> getFriendRequestSentList() {
		return friendRequestSentList;
	}

	public void setFriendRequestSentList(List<String> friendRequestSentList) {
		this.friendRequestSentList = friendRequestSentList;
	}

	@Override
	public String toString() {
		return "UserProfile [userId=" + userId + ", userName=" + userName + ", password=" + password + ", email="
				+ email + ", mobileNo=" + mobileNo + ", address=" + address + ", created=" + created + ", friendList="
				+ friendList + ", pendingFriendRequestList=" + pendingFriendRequestList + ", friendRequestSentList="
				+ friendRequestSentList + "]";
	}

}
