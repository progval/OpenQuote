/**
 * 
 */
package com.github.progval.openquote.sites;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.progval.openquote.R;
import com.github.progval.openquote.SiteItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Whitelist;


/**
 * @author ProgVal
 *
 */
public class DtcItem extends SiteItem {
	private int positiveRatings;
	private int negativeRatings;
	
	public DtcItem(Element baseElement) {
		this.id = ((Node) baseElement.select("p a").get(0)).attr("href").replaceAll("[^0-9]", "");
		html = baseElement.html();
		
		Whitelist whiteList = Whitelist.none();
		whiteList.addTags("br");
		content = Jsoup.clean(baseElement.select("p.item-content a").html(), whiteList);
		content = content.replaceAll("<br /> *", "");
		content = TextNode.createFromEncoded(content, "/").getWholeText();

		positiveRatings = Integer.parseInt(baseElement.select("p.item-meta a.voteplus").text().replaceAll("[^0-9]", ""));
		negativeRatings = Integer.parseInt(baseElement.select("p.item-meta a.voteminus").text().replaceAll("[^0-9]", ""));
	}

	public void addRatingView(Context context, View parent) {
		LinearLayout row = (LinearLayout) parent.findViewById(R.id.fullquote_ratings_row);
		
		TextView ratingsContent = new TextView(context);
		ratingsContent.setText(String.format("(+) %s (-) %s", positiveRatings, negativeRatings));
		
		row.addView(ratingsContent);
	}
}