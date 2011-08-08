/**
 * 
 */
package com.github.progval.openquote;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Abstract class for a source site.
 * 
 * @author ProgVal
 *
 */

public abstract class SiteActivity extends ListActivity implements OnClickListener {
	public static final String PREFS_NAME = "preferences";
	
	/* *******************************
	 *  Site-specific data
	 ********************************/
	public abstract String getName();
	public abstract int getLowestPageNumber();  // 1 for most of the sites, but 0 for VDM.

	/* ************************************
	 *  State
	 *************************************/

	/**
	 * A mode of a quotes activity.
	 */
	public enum Mode {
		LATEST, TOP, RANDOM
	}
	protected static Mode previouslyLoadedMode = Mode.LATEST; // Restored if page load failed.
	protected static Mode mode = Mode.LATEST;
	protected static int previouslyLoadedPage; // Restored if page load failed.
	protected static int page = -1;
	/**
	 * Determinates whether or not the user is allowed to use the previous/next button.
	 */
	protected boolean enablePageChange = true;

	/* ************************************
	 *  Storage
	 *************************************/
	public static ArrayList<SiteItem> listItems = new ArrayList<SiteItem>();
	public static ItemAdapter adapter;



	/* ************************************
	 *  User interface building and handling
	 *************************************/
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setAdapter();
		this.bindButtons();
		this.initializeContextMenu();
		if (page == -1) {
			this.page = this.getLowestPageNumber();
			this.previouslyLoadedPage = this.page;
			this.onClick(findViewById(R.id.buttonLatest));
		}
		else {
			if (this.page == this.getLowestPageNumber()) {
				findViewById(R.id.buttonPrevious).setEnabled(false); // We open the first page
			}
			enablePageChange(this.enablePageChange);
			if (this.page == this.getLowestPageNumber()) {
				findViewById(R.id.buttonPrevious).setEnabled(false); // We open the first page
			}
			this.updateTitle();
		}
	}
	// Called only be the constructor.
	private void setAdapter() {
		setContentView(R.layout.siteactivity);
		adapter=new ItemAdapter(this,
				R.layout.siteitem,
				listItems);
		setListAdapter(adapter);
	}
	// Called only be the constructor.
	private void bindButtons() {
		findViewById(R.id.buttonLatest).setOnClickListener(this);
		findViewById(R.id.buttonTop).setOnClickListener(this);
		findViewById(R.id.buttonRandom).setOnClickListener(this);
		findViewById(R.id.buttonPrevious).setOnClickListener(this);
		findViewById(R.id.buttonNext).setOnClickListener(this);
	}
	// Called only be the constructor.
	private void initializeContextMenu() {
		ListView listView = getListView();
		registerForContextMenu(listView);
	}
	/**
	 * Called when any button (not in a Dialog) is clicked.
	 *
	 * @param v The clicked view.
	 */
	public void onClick(View v) {
		enablePageChange(true);
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
			case R.id.buttonRandom: // Display random quotes
				this.mode = Mode.RANDOM;
				this.page = this.getLowestPageNumber();
				enablePageChange(false);
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
		if (this.page == this.getLowestPageNumber()) {
			findViewById(R.id.buttonPrevious).setEnabled(false); // We open the first page
		}
	}

	/**
	 * Sets the 'enablePageChange' flag to the given mode and enable/disable the buttons according to this mode.
	 *
	 * @param mode The new mode to be set.
	 */
	public void enablePageChange(boolean mode) {
		findViewById(R.id.buttonPrevious).setEnabled(mode);
		findViewById(R.id.buttonNext).setEnabled(mode);
		enablePageChange = mode;
	}
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.quote_context_menu, menu);
	}
	public boolean onContextItemSelected(MenuItem item) {
		int clickedQuotePosition = ((AdapterContextMenuInfo)item.getMenuInfo()).position;
		SiteItem clickedQuote = listItems.get(clickedQuotePosition);
		switch (item.getItemId()) {
			case R.id.siteactivity_context_copy:
				ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
				clipboard.setText(clickedQuote.getContent());
				return true;
			case R.id.siteactivity_context_share:
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.siteactivity_share_subject));
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, clickedQuote.getContent());

				startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.siteactivity_share_window_title)));
				return true;
			case R.id.siteactivity_context_fulldisplay:
				this.displayFullQuote(clickedQuote);
				return true;
		}
		return false;
	}

	/* ************************************
	 *  Full quote display
	 *************************************/

	/**
	 * Open a dialog and display the given quote.
	 *
	 * @param quote The quote to be fully displayed.
	 */
	protected void displayFullQuote(SiteItem quote) {
		AlertDialog.Builder adb;
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.full_quote_display, (ViewGroup) findViewById(R.id.full_quote_display));

		((TextView) layout.findViewById(R.id.fullquote_content)).setText(quote.getContent());
		quote.addRatingView(this, layout.findViewById(R.id.full_quote_display));

		adb = new AlertDialog.Builder(this);
		adb.setView(layout);
		adb.setTitle(String.format(getResources().getString(R.string.fullquote_title), quote.getId()));
		adb.create();
		adb.show();
	}

	/* ************************************
	 *  Context menu
	 *************************************/
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.siteactivity, menu);
		return true;
	}

	/**
	 * Extract the page number from the given EditText instance, set it to the current page if valid, refreshes the page, and dismiss the dialog.
	 *
	 * @param dialog The dialog to be dismissed.
	 * @param pageNumber The EditText where the user entered the page number.
	 */
	void validateDialog(DialogInterface dialog, EditText pageNumber)  {
		try {
			page = Integer.parseInt(pageNumber.getText().toString()) - 1 + SiteActivity.this.getLowestPageNumber();
			refresh();
			dialog.dismiss();
		}
		catch (NumberFormatException e) {
			// Never trust user input
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.siteactivity_menu_gotopage:
				if (enablePageChange) {
					// Create TextEdit
					final EditText pageNumber = new EditText(this);
					pageNumber.setKeyListener(new NumberKeyListener(){
							@Override
							protected char[] getAcceptedChars() {
								return new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
							}
	
						public int getInputType() {
							return InputType.TYPE_CLASS_NUMBER; // Set keyboard to numeric mode.
						}
						});

					// Create listener
					DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									validateDialog(dialog, pageNumber);
									break;
								case DialogInterface.BUTTON_NEGATIVE:
									dialog.dismiss();
									break;
							}
						}
					};

					// Build dialog
					AlertDialog.Builder adb = new AlertDialog.Builder(this);
					adb.setTitle(getResources().getString(R.string.siteactivity_gotopage_window_title));
					adb.setPositiveButton(getResources().getString(R.string.siteactivity_gotopage_button_go), listener);
					adb.setNegativeButton(getResources().getString(R.string.siteactivity_gotopage_button_cancel), listener);
					adb.setView(pageNumber);
					adb.show();
				}
				else {
					this.showErrorDialog(getResources().getString(R.string.siteactivity_gotopage_error_disabled));
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/* ************************************
	 *  Error display
	 *************************************/
	/** Same as showErrorDialog() with a generic error message for network issues */
	public void showIOExceptionDialog() {
		this.showErrorDialog(getResources().getString(R.string.siteactivity_network_error_message));
	}
	/** Same as showErrorDialog() with a generic error message for not supported features */
	public void showNonSupportedFeatureDialog() {
		this.showErrorDialog(getResources().getString(R.string.siteactivity_not_supported_error_message));
	}
	/**
	 * Display an error dialog
	 *
	 * @param message The error message that will be displayed.
	 */
	public void showErrorDialog(String message) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(getResources().getString(R.string.siteactivity_error_title));
		adb.setMessage(message);
		adb.setPositiveButton(getResources().getString(R.string.siteactivity_error_button), null);
		adb.show();
	}

	/* ************************************
	 *  Fetch quotes
	 *************************************/

	/**
	 * Get the quotes for the current mode and page.
	 *
	 * @return An array of the quotes.
	 * @throws IOException If something bad occurred with the network.
	 */
	public SiteItem[] getQuotes() throws IOException {
		switch(this.mode) {
			case LATEST:
				return this.getLatest(this.page);
			case TOP:
				return this.getTop(this.page);
			case RANDOM:
				return this.getRandom();
		}
		return new SiteItem[0];
	}
	/**
	 * Returns the latest quotes
	 *
	 * @param page The number of the page that will be loaded.
	 * @return An array containing the latest quotes.
	 * @throws IOException If something bad occurred with the network.
	 */
	public abstract SiteItem[] getLatest(int page) throws IOException;
	/**
	 * Returns the top quotes
	 *
	 * @param page The number of the page that will be loaded.
	 * @return An array containing the top quotes.
	 * @throws IOException If something bad occurred with the network.
	 */
	public abstract SiteItem[] getTop(int page) throws IOException;
	/**
	 * Returns random quotes
	 *
	 * @return An array containing random quotes.
	 * @throws IOException If something bad occurred with the network.
	 */
	public abstract SiteItem[] getRandom() throws IOException;

	/* ************************************
	 *  Display quotes
	 *************************************/

	/** Load the quotes asynchronously, and add them to the ListView */
	public void refresh() {
		this.updateTitle();
		new AsyncQuotesFetcher(this).execute();
	}
	/**
	 * Add an item to the list
	 *
	 * @param item The item to be added to the list
	 * @param top If the item will be prepended to the list instead of appended.
	 */
	public void addItem(SiteItem item, boolean top) {
		if (top) {
			listItems.add(0, item);
		}
		else {
			listItems.add(item);
		}
		adapter.notifyDataSetChanged();
	}
	/**
	 * Alias for addItem(item, true).
	 *
	 * @param item The item to be added to the list
	 */
	public void addItem(SiteItem item) {
		addItem(item, true);
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
		setTitle(String.format(getResources().getString(R.string.siteactivity_title), this.getName(), this.getModeString(), humanReadablePage));
	}
	/**
	 * Returns the mode in the current locale
	 *
	 * @return A human-readable string that represents the mode.
	 */
	public String getModeString() {
		switch (this.mode) {
			case LATEST:
				return getResources().getString(R.string.siteactivity_mode_latest);
			case TOP:
				return getResources().getString(R.string.siteactivity_mode_top);
			case RANDOM:
				return getResources().getString(R.string.siteactivity_mode_random);
			default:
				return getResources().getString(R.string.siteactivity_mode_unknown);
		}
	}
}
