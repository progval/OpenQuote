package com.github.progval.openquote.sites;

// Project specific
import com.github.progval.openquote.sites.VdmItem;
import com.github.progval.openquote.SiteActivity;

// Networking
import java.io.IOException;

// Parsing HTML
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;

/**
 * Activity for displaying facts from VDM.
 *
 * @author ProgVal
 */
public class VdmActivity extends SiteActivity {
	public VdmItem[] getLatest() {
		try {
			return this.parsePage("/");
		}
		catch (IOException e) {
	        return new VdmItem[0];
		}
	}
	public VdmItem[] getTop() {
		try {
			return this.parsePage("/tops");
		}
		catch (IOException e) {
	        return new VdmItem[0];
		}
	}
	public VdmItem[] parsePage(String uri) throws IOException {
		int foundItems = 0;
		Document document = Jsoup.connect("http://m.viedemerde.fr" + uri).get();
		
		/* We use ":contain(VDM)" in order to remove the leading
		 * "Je valide | Tu l'as bien mérité" in Top quotes.
		 */
		Elements elements = document.select("ul.content li:contains(VDM)");
		VdmItem[] items = new VdmItem[elements.size()];
		for (Element element : elements) {
			items[foundItems] = new VdmItem(element);
			foundItems++;
		}
		
		return items;
	}
}
