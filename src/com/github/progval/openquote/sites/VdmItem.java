/**
 * 
 */
package com.github.progval.openquote.sites;

// Project specific
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.progval.openquote.R;
import com.github.progval.openquote.SiteItem;

// Parsing HTML
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author ProgVal
 */
public class VdmItem extends SiteItem {
	private int positiveRatings;
	private int negativeRatings;
	
	public VdmItem(Element baseElement) {
		html = baseElement.html();
        Element content = (Element) baseElement.child(0);
    	this.id = baseElement.id().replaceAll("[^0-9]", "");
    	this.content = content.text();
    	
    	Elements votes = baseElement.select("p.vote span.vote-button");
    	positiveRatings = Integer.parseInt(votes.get(0).ownText().replaceAll("[()]", ""));
    	negativeRatings = Integer.parseInt(votes.get(1).ownText().replaceAll("[()]", ""));
	}
	
	public String toString() {
		return this.content;
	}

	public void addRatingView(Context context, View parent) {
		LinearLayout row = (LinearLayout) parent.findViewById(R.id.fullquote_ratings_row);
		
		TextView ratingsContent = new TextView(context);
		ratingsContent.setText(String.format("(+) %s (-) %s", positiveRatings, negativeRatings));
		
		row.addView(ratingsContent);
	}
}
