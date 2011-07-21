/**
 * 
 */
package com.github.progval.openquote.sites;

// Project specific
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
	public BashItem(Element baseElement) {
		this.id = baseElement.select("a b").text().replaceAll("[^0-9]", "");
		String content = baseElement.select("p.qt").html();
		
		Whitelist whiteList = Whitelist.none();
		whiteList.addTags("br");
		content = Jsoup.clean(content, whiteList);
		content = content.replaceAll("<br /> *", "");
		content = TextNode.createFromEncoded(content, "/").getWholeText();
		this.content = content;
	}
	
	public String toString() {
		return this.content;
	}
}