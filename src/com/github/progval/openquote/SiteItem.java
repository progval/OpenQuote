package com.github.progval.openquote;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public abstract class SiteItem {
	protected String id;
	protected String content;
	protected String html;
	
	public String getId() {
		return this.id;
	}
	public String getContent() {
		return this.content;
	}
	public abstract void addRatingView(Context context, View parent);
	public String toString() {
		return this.getContent();
	}
	public String getHtml() {
		return this.html;
	}
	public TextView formatView(TextView view) {
		view.setText(this.getContent());
		return view;
	}
}
