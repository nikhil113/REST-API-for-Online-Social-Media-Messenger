package com.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LikeInfo {
	
	private List<String> userProfileId;
	private long likeCount;
	
	public List<String> getUserProfileId() {
		return userProfileId;
	}
	public void setUserProfileId(List<String> userProfileId) {
		this.userProfileId = userProfileId;
	}
	public long getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(long likeCount) {
		this.likeCount = likeCount;
	}
	
	@Override
	public String toString() {
		return "LikeInfo [userProfileId=" + userProfileId + ", likeCount=" + likeCount + "]";
	}

}
