/**
 * 
 */
package com.github.progval.openquote;

// Project specific
import com.github.progval.openquote.SiteItem;

// User interface
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;

// Android
import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;

/**
 * Abstract class for a source site.
 * 
 * @author ProgVal
 *
 */

public abstract class SiteActivity extends ListActivity implements OnClickListener {
	// Site-specific data
	public abstract String getName();
	public abstract int getLowestPageNumber();  // 1 for most of the sites, but 0 for VDM.

	// State.
	public enum Mode {
	    LATEST, TOP
	}
	protected Mode mode;
	protected int page;

	private ArrayList<String> listItems = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.page = this.getLowestPageNumber();
		setContentView(R.layout.siteactivity);
		adapter=new ArrayAdapter<String>(this,
				R.layout.siteitem,
				listItems);
		setListAdapter(adapter);

		Button buttonLatest = (Button)findViewById(R.id.buttonLatest);
		buttonLatest.setOnClickListener(this);
		Button buttonTop = (Button)findViewById(R.id.buttonTop);
		buttonTop.setOnClickListener(this);
		Button buttonPrevious = (Button)findViewById(R.id.buttonPrevious);
		buttonPrevious.setOnClickListener(this);
		Button buttonNext = (Button)findViewById(R.id.buttonNext);
		buttonNext.setOnClickListener(this);

		this.onClick(buttonLatest);
	}

	public void onClick(View v) {
		try {
			switch (v.getId()) {
				case R.id.buttonLatest:
					this.mode = Mode.LATEST;
					this.page = this.getLowestPageNumber();
					this.populate(this.getLatest());
					break;
				case R.id.buttonTop:
					this.mode = Mode.TOP;
					this.page = this.getLowestPageNumber();
					this.populate(this.getTop());
					break;
				case R.id.buttonPrevious:
					if (this.page > this.getLowestPageNumber()) {
						this.page--;
					}
					this.refresh();
					break;
				case R.id.buttonNext:
					this.page++;
					this.refresh();
					break;
			}
		}
		catch (IOException e) {
			this.showIOExceptionDialog();
		}
	}
	public void showIOExceptionDialog() {
		this.showErrorDialog("The quotes could not be loaded due to a network issue.");
	}
	public void showErrorDialog(String message) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle("Error");
		adb.setMessage(message);
		adb.setPositiveButton("Ok", null);
		adb.show();
	}

	public void refresh() {
		this.updateTitle();
		try {
			switch(this.mode) {
				case LATEST:
					this.populate(this.getLatest(page));
					break;
				case TOP:
					this.populate(this.getTop(page));
					break;
			}
		}
		catch (IOException e) {
			this.showIOExceptionDialog();
		}
	}
	
	/** Populate the activity interface with latest quotes */
	public SiteItem[] getLatest() throws IOException {
		return this.getLatest(this.getLowestPageNumber());
	}
	/** Populate the activity interface with the n-th page of latest quotes */
	public abstract SiteItem[] getLatest(int page) throws IOException;
	/** Populate the activity interface with top quotes */
	public SiteItem[] getTop() throws IOException {
		return this.getTop(this.getLowestPageNumber());
	}
	/** Populate the activity interface with the n-th page of top quotes */
	public abstract SiteItem[] getTop(int page) throws IOException;

	/** Takes a list of items, and add them to the ListView */
	public void populate(SiteItem[] latest) {
		this.updateTitle();
		if (latest.length > 0) {
			this.clearList();
			for (SiteItem item : latest) {
				this.addItem(item.toString(), false);
			}
		}
	}
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
	/** Clear the list */
	public void clearList() {
		listItems.clear();
		 adapter.notifyDataSetChanged();
	}

	/** Format and set the title according to the activity state. */
	public void updateTitle() {
		int humanReadablePage = page - this.getLowestPageNumber() + 1;
		setTitle(this.getName() + " > " + this.mode.toString().toLowerCase() + " page " + String.valueOf(humanReadablePage));
	}
}
