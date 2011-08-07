/**
 * 
 */
package com.github.progval.openquote.sites;
// Project specific

import com.github.progval.openquote.sites.BashItem;
import com.github.progval.openquote.SiteActivity;

//Networking
import java.io.IOException;

//Parsing HTML
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

/**
 * @author ProgVal
 *
 */
public class BashActivity extends SiteActivity {
	public String getName() { return "Bash.org"; }
	public int getLowestPageNumber() { return 1; }

	public BashItem[] getLatest(AsyncQuotesFetcher task, int page) throws IOException {
		enablePageChange(false);
		if (page != this.getLowestPageNumber()) {
			this.showNonSupportedFeatureDialog();
			this.page = this.getLowestPageNumber();
			return new BashItem[0];
		}
		else {
			return this.parsePage(task, "/?latest");
		}
	}
	public BashItem[] getTop(AsyncQuotesFetcher task, int page) throws IOException {
		enablePageChange(false);
		if (page != this.getLowestPageNumber()) {
			this.showNonSupportedFeatureDialog();
			this.page = this.getLowestPageNumber();
			return new BashItem[0];
		}
		else {
			return this.parsePage(task, "?top");
		}
	}
	public BashItem[] getRandom(AsyncQuotesFetcher task) throws IOException {
		enablePageChange(false);
		return this.parsePage(task, "?random");
	}
	public BashItem[] parsePage(AsyncQuotesFetcher task, String uri) throws IOException {
		int foundItems = 0;
		Document document = Jsoup.connect("http://bash.org" + uri).get();
		String[] elements = document.select("td[valign=top]").html().split("</p> <p class=\"quote\">");
		BashItem[] items = new BashItem[elements.length];
		for (String element : elements) {
			if (task.isCancelled()) {
				return new BashItem[0];
			if (foundItems != 0) {
				element = "<p class=\"quote\">" + element;
			}
			items[foundItems] = new BashItem(Jsoup.parse(element));
			foundItems++;
		}
		
		return items;
	}
}
