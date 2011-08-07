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
public class BashItem extends SiteItem {
	protected String ratings;
	
	public BashItem(Element baseElement) {
		this.ratings = baseElement.select("p.quote").get(0).ownText(); //.replace("[^0-9-]", "");
		this.id = baseElement.select("a b").text().replaceAll("[^0-9]", "");
		html = baseElement.select("p.qt").html();
		
		Whitelist whiteList = Whitelist.none();
		whiteList.addTags("br");
		content = Jsoup.clean(html, whiteList);
		content = content.replaceAll("<br /> *", "");
		content = TextNode.createFromEncoded(content, "/").getWholeText();
	}

	public void addRatingView(Context context, View parent) {
		LinearLayout row = (LinearLayout) parent.findViewById(R.id.fullquote_ratings_row);
		
		TextView ratingsContent = new TextView(context);
		ratingsContent.setText(ratings);
		
		row.addView(ratingsContent);
	}
}