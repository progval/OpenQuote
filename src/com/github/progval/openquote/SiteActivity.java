/**
 * 
 */
package com.github.progval.openquote;

// Project specific
import com.github.progval.openquote.SiteItem;

// User interface
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

import java.lang.Void;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

// Android
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Abstract class for a source site.
 * 
 * @author ProgVal
 *
 */

public abstract class SiteActivity extends ListActivity implements OnClickListener {
	/* *******************************
	 *  Site-specific data
	 ********************************/
	public abstract String getName();
	public abstract int getLowestPageNumber();  // 1 for most of the sites, but 0 for VDM.

	/* ************************************
	 *  State
	 *************************************/
	public enum Mode {
	    LATEST, TOP
	}
	protected Mode mode;
	protected int page;

	/* ************************************
	 *  Storage
	 *************************************/
	private ArrayList<String> listItems = new ArrayList<String>();
	ArrayAdapter<String> adapter;



	/* ************************************
	 *  User interface building and handling
	 *************************************/
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.page = this.getLowestPageNumber();
		this.setAdapter();
		this.bindButtons();
		this.onClick(findViewById(R.id.buttonLatest));
	}
	private void setAdapter() {
		setContentView(R.layout.siteactivity);
		adapter=new ArrayAdapter<String>(this,
				R.layout.siteitem,
				listItems);
		setListAdapter(adapter);
	}
	/** Set onClickListener for the buttons */
	private void bindButtons() {
		findViewById(R.id.buttonLatest).setOnClickListener(this);
		findViewById(R.id.buttonTop).setOnClickListener(this);
		findViewById(R.id.buttonPrevious).setOnClickListener(this);
		findViewById(R.id.buttonNext).setOnClickListener(this);
	}
	/** Called when any button is clicked. */
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.buttonLatest: // Display latest quotes
				this.mode = Mode.LATEST;
				this.page = this.getLowestPageNumber();
				this.refresh();
				break;
			case R.id.buttonTop: // Display top quotes
				this.mode = Mode.TOP;
				this.page = this.getLowestPageNumber();
				this.refresh();
				break;
			case R.id.buttonPrevious: // Open previous page
				if (this.page > this.getLowestPageNumber()) {
					this.page--;
				}
				this.refresh();
				break;
			case R.id.buttonNext: // Open next page
				this.page++;
				this.refresh();
				break;
		}
	}

	/* ************************************
	 *  Error display
	 *************************************/
	/** Same as showErrorDialog() with a generic error message for network issues */
	public void showIOExceptionDialog() {
		this.showErrorDialog("The quotes could not be loaded due to a network issue.");
	}
	/** Display an error dialog */
	public void showErrorDialog(String message) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle("Error");
		adb.setMessage(message);
		adb.setPositiveButton("Ok", null);
		adb.show();
	}

	/* ************************************
	 *  Fetch quotes
	 *************************************/
	private SiteItem[] getQuotes() throws IOException {
		switch(this.mode) {
			case LATEST:
				return this.getLatest(this.page);
			case TOP:
				return this.getTop(this.page);
		}
		return new SiteItem[0];
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

	/* ************************************
	 *  Display quotes
	 *************************************/
	private class AsyncQuotesFetcher extends AsyncTask<Void, Void, Void> {
		private SiteItem[] items;
		private String errorLog;
		ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = ProgressDialog.show(SiteActivity.this, "", "Loading the quotes. Please wait...", true);
		}

		protected Void doInBackground(Void... foo) {
			try {
				items = SiteActivity.this.getQuotes();
			}
			catch (Exception e) {
				if (e instanceof IOException) {
					items = new SiteItem[0];
				}
				else {
					errorLog = e.toString();
				}
			}
			return null;
		}
		protected void onPostExecute(Void foo) {
			dialog.dismiss();
			if (errorLog != null) {
				SiteActivity.this.showErrorDialog("Unknown error: " + errorLog);
			}
			else if (items == null) {
				SiteActivity.this.showErrorDialog("This is strange... there is no results, but this is not an error.");
			}
			else if (items.length > 0) {
				SiteActivity.this.clearList();
				for (SiteItem item : items) {
					SiteActivity.this.addItem(item.toString(), false);
				}
			}
			else {
				SiteActivity.this.showIOExceptionDialog();
			}
		}
	}
	/** Load the quotes, and add them to the ListView */
	public void refresh() {
		this.updateTitle();
		new AsyncQuotesFetcher().execute();
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

	/* ************************************
	 *  Other user interface handling
	 *************************************/
	/** Format and set the title according to the activity state. */
	public void updateTitle() {
		int humanReadablePage = page - this.getLowestPageNumber() + 1;
		setTitle(this.getName() + " > " + this.mode.toString().toLowerCase() + " page " + String.valueOf(humanReadablePage));
	}
}
