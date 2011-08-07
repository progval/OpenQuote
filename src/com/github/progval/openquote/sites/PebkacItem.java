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
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.Jsoup;

/**
 * @author ProgVal
 *
 */
public class PebkacItem extends SiteItem {
	protected String ratings;
	
	public PebkacItem(Element baseElement) {
		this.ratings = baseElement.select("td.pebkacLeft span").get(0).ownText();
		this.id = baseElement.select("a.permalink").attr("href").replaceAll("[^0-9]", "");

		content = baseElement.select("td.pebkacContent").html().split("<br /><br /><a class=")[0];
		Whitelist whiteList = Whitelist.none();
		whiteList.addTags("br");
		content = Jsoup.clean(content, whiteList);
		content = content.replaceAll("<br /> *", "");
		content = TextNode.createFromEncoded(content, "/").getWholeText();
	}
	
	public String toString() {
		return this.content;
	}

	public void addRatingView(Context context, View parent) {
		LinearLayout row = (LinearLayout) parent.findViewById(R.id.fullquote_ratings_row);
		
		TextView ratingsContent = new TextView(context);
		ratingsContent.setText(ratings);
		
		row.addView(ratingsContent);
	}
}