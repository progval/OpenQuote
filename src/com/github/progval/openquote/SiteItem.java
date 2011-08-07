package com.github.progval.openquote;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public abstract class SiteItem {
	protected String id;
	protected String content;
	protected String html;

	/**
	 * Returns the unique ID representing the quote.
	 *
	 * @return The ID.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Returns the content of the quote.
	 *
	 * @return The content.
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Adds a view to the parent, containing the ratings.
	 *
	 * @param context The context from which strings are extracted.
	 * @param parent The layout to which the view will be added.
	 */
	public abstract void addRatingView(Context context, View parent);
	public String toString() {
		return this.getContent();
	}
}
