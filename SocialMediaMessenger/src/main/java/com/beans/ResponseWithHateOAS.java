package com.beans;

import java.util.List;

public class ResponseWithHateOAS {
	
	private Object object;
	private List<Links> linksList;
	
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public List<Links> getLinksList() {
		return linksList;
	}
	public void setLinksList(List<Links> linksList) {
		this.linksList = linksList;
	}

}
