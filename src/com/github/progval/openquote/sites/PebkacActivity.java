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

	public PebkacItem[] getLatest(AsyncQuotesFetcher task, int page) throws IOException {
		return this.parsePage(task, "/index.php?page=" + String.valueOf(page));
	}
	public PebkacItem[] getTop(AsyncQuotesFetcher task, int page) throws IOException {
		return this.parsePage(task, "/index.php?p=top&page=" + String.valueOf(page));
	}
	public PebkacItem[] getRandom(AsyncQuotesFetcher task) throws IOException {
		return this.parsePage(task, "/pebkac-aleatoires.html");
	}
	public PebkacItem[] parsePage(AsyncQuotesFetcher task, String uri) throws IOException {
		int foundItems = 0;
		Document document = Jsoup.connect("http://www.pebkac.fr" + uri).get();
		Elements elements = document.select("table.pebkacMiddle");
		PebkacItem[] items = new PebkacItem[elements.size()];
		for (Element element : elements) {
			if (task.isCancelled()) {
				return new PebkacItem[0];
			}
			items[foundItems] = new PebkacItem(element);
			foundItems++;
		}

		return items;
	}
}
