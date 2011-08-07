/**
 * 
 */
package com.github.progval.openquote.sites;

import com.github.progval.openquote.SiteActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @author ProgVal
 *
 */
public class DtcActivity extends SiteActivity {
	public String getName() { return "DTC"; }
	public int getLowestPageNumber() { return 1; }

	public DtcItem[] getLatest(int page) throws IOException {
		return this.parsePage("/latest/" + String.valueOf(page) +".html");
	}
	public DtcItem[] getTop(int page) throws IOException {
		if (page != this.getLowestPageNumber()) {
			this.showNonSupportedFeatureDialog();
			this.page = this.getLowestPageNumber();
			return new DtcItem[0];
		}
		else {
			return this.parsePage("/top50.html");
		}
	}
	public DtcItem[] getRandom() throws IOException {
		return this.parsePage("/random.html");
	}
	public DtcItem[] parsePage(String uri) throws IOException {
		int foundItems = 0;
		Document document = Jsoup.connect("http://danstonchat.com" + uri).get();
		Elements elements = document.select("div.item");
		DtcItem[] items = new DtcItem[elements.size()];
		for (Element element : elements) {
			items[foundItems] = new DtcItem(element);
			foundItems++;
		}
		
		return items;
	}
}
