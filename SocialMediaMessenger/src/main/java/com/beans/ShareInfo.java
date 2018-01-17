package com.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ShareInfo {
	
	private List<String> userProfileIdList;
	private long shareCount;
	
	public List<String> getUserProfileIdList() {
		return userProfileIdList;
	}
	public void setUserProfileIdList(List<String> userProfileIdList) {
		this.userProfileIdList = userProfileIdList;
	}
	public long getShareCount() {
		return shareCount;
	}
	public void setShareCount(long shareCount) {
		this.shareCount = shareCount;
	}
	
	@Override
	public String toString() {
		return "ShareInfo [userProfileIdList=" + userProfileIdList + ", shareCount=" + shareCount + "]";
	}

}
