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
	public PebkacItem[] getLatest() throws IOException {
		return this.parsePage("/");
	}
	public PebkacItem[] getTop() throws IOException {
		return this.parsePage("/index.php?p=top");
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
