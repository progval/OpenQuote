/**
 * 
 */
package com.github.progval.openquote;

// Project specific
import com.github.progval.openquote.SiteItem;

// User interface
import android.widget.ArrayAdapter;
import java.util.ArrayList;

// Android
import android.app.ListActivity;
import android.os.Bundle;

/**
 * Abstract class for a source site.
 * 
 * @author ProgVal
 *
 */
public abstract class SiteActivity extends ListActivity {
	private ArrayList<String> listItems = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.siteactivity);
		adapter=new ArrayAdapter<String>(this,
				R.layout.siteitem,
				listItems);
		setListAdapter(adapter);
		SiteItem[] latest = this.getLatest();
		for (SiteItem item : latest) {
			this.addItem(item.toString(), false);
		}
	}
	/** Populate the activity interface with latest quotes */
	public abstract SiteItem[] getLatest();

	/** Add an item to the list */
	private void addItem(String item, boolean top) {
		if (top) {
			listItems.add(0, item);
		}
		else {
			listItems.add(item);
		}
		adapter.notifyDataSetChanged();
	}
	/** Prepend an item to the list */
	public void addItem(String item) {
		 listItems.add(0, item);
		 adapter.notifyDataSetChanged();
	}
}
