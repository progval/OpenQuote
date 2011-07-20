/**
 * 
 */
package com.github.progval.openquote.sites;

// Project specific
import com.github.progval.openquote.sites.PebkacItem;
import com.github.progval.openquote.SiteActivity;

//Networking
import java.io.IOException;

//Parsing HTML
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;

/**
 * @author ProgVal
 *
 */
public class PebkacActivity extends SiteActivity {
	public String getName() { return "PEBKAC"; }
	public int getLowestPageNumber() { return 1; }

	public PebkacItem[] getLatest(int page) throws IOException {
		return this.parsePage("/index.php?page=" + String.valueOf(page));
	}
	public PebkacItem[] getTop(int page) throws IOException {
		return this.parsePage("/index.php?p=top&page=" + String.valueOf(page));
	}
	public PebkacItem[] parsePage(String uri) throws IOException {
		int foundItems = 0;
		Document document = Jsoup.connect("http://www.pebkac.fr" + uri).get();
		Elements elements = document.select("td.pebkacContent");
		PebkacItem[] items = new PebkacItem[elements.size()];
		for (Element element : elements) {
			items[foundItems] = new PebkacItem(element);
			foundItems++;
		}

		return items;
	}
}
