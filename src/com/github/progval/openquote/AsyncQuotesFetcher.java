package com.github.progval.openquote;


import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Download and extract the quotes while displaying a ProgressDialog, and then dismiss the dialog and populate the current activity.
 *
 * @author ProgVal
 */
class AsyncQuotesFetcher extends AsyncTask<Void, Void, Void> {
	private SiteItem[] items;
	private String errorLog;
	ProgressDialog dialog;
	SiteActivity activity;

	private void dismissDialog() {
		try {
			dialog.dismiss();
		}
		catch (IllegalArgumentException e) {
			// Window has leaked
		}
	}

	public AsyncQuotesFetcher(SiteActivity activity) {
		super();
		this.activity = activity;
	}

	protected void onPreExecute() {
		dialog = ProgressDialog.show(activity, "", activity.getResources().getString(R.string.siteactivity_loading_quotes), true);
	}

	protected Void doInBackground(Void... foo) {
		try {
			items = activity.getQuotes();
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
		dismissDialog();
		if (errorLog != null) {
			activity.showErrorDialog(String.format(activity.getResources().getString(R.string.siteactivity_unknown_error), errorLog));
		}
		else if (items == null) {
			activity.showErrorDialog(activity.getResources().getString(R.string.siteactivity_no_errors_no_results));
		}
		else if (items.length > 0) {
			activity.clearList();
			for (SiteItem item : items) {
				activity.addItem(item, false);
			}
			activity.adapter.notifyDataSetChanged();
			activity.getListView().setSelectionAfterHeaderView();
			activity.previouslyLoadedMode = activity.mode;
			activity.previouslyLoadedPage = activity.page;
		}
		else {
			activity.showIOExceptionDialog();
		}
		activity.page = activity.previouslyLoadedPage;
		activity.mode = activity.previouslyLoadedMode;
		if (activity.page == activity.getLowestPageNumber()) {
			activity.findViewById(R.id.buttonPrevious).setEnabled(false); // We open the first page
		}
		activity.updateTitle();
	}
}