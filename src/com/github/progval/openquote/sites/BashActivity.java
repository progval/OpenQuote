/**
 * 
 */
package com.github.progval.openquote.sites;

import com.github.progval.openquote.SiteActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author ProgVal
 *
 */
public class BashActivity extends SiteActivity {
	public String getName() { return "Bash.org"; }
	public int getLowestPageNumber() { return 1; }

	public BashItem[] getLatest(int page) throws IOException {
		enablePageChange(false);
		if (page != this.getLowestPageNumber()) {
			this.showNonSupportedFeatureDialog();
			this.page = this.getLowestPageNumber();
			return new BashItem[0];
		}
		else {
			return this.parsePage("/?latest");
		}
	}
	public BashItem[] getTop(int page) throws IOException {
		enablePageChange(false);
		if (page != this.getLowestPageNumber()) {
			this.showNonSupportedFeatureDialog();
			this.page = this.getLowestPageNumber();
			return new BashItem[0];
		}
		else {
			return this.parsePage("?top");
		}
	}
	public BashItem[] getRandom() throws IOException {
		enablePageChange(false);
		return this.parsePage("?random");
	}
	public BashItem[] parsePage(String uri) throws IOException {
		int foundItems = 0;
		Document document = Jsoup.connect("http://bash.org" + uri).get();
		String[] elements = document.select("td[valign=top]").html().split("</p> <p class=\"quote\">");
		BashItem[] items = new BashItem[elements.length];
		for (String element : elements) {
			if (foundItems != 0) {
				element = "<p class=\"quote\">" + element;
			}
			items[foundItems] = new BashItem(Jsoup.parse(element));
			foundItems++;
		}
		
		return items;
	}
}
