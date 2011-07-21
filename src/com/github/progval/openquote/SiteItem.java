package com.github.progval.openquote;

public abstract class SiteItem {
	protected String id;
	protected String content;
	
	public String getId() {
		return this.id;
	}
	public String getContent() {
		return this.content;
	}
	public String toString() {
		return this.getContent();
	}
}
