package com.github.progval.openquote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.github.progval.openquote.sites.BashActivity;
import com.github.progval.openquote.sites.DtcActivity;
import com.github.progval.openquote.sites.PebkacActivity;
import com.github.progval.openquote.sites.VdmActivity;

/**
 * Home screen.
 *
 * @author ProgVal
 */
public class Home extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		Button buttonVdm = (Button)findViewById(R.id.buttonVdm);
		buttonVdm.setOnClickListener(openVdm);
		Button buttonDtc = (Button)findViewById(R.id.buttonDtc);
		buttonDtc.setOnClickListener(openDtc);
		Button buttonPebkac = (Button)findViewById(R.id.buttonPebkac);
		buttonPebkac.setOnClickListener(openPebkac);
		Button buttonBash = (Button)findViewById(R.id.buttonBash);
		buttonBash.setOnClickListener(openBash);
	}

	public OnClickListener openVdm = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Home.this, VdmActivity.class);
			startActivity(intent);
		}
	};
	public OnClickListener openDtc = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Home.this, DtcActivity.class);
			startActivity(intent);
		}
	};
	public OnClickListener openPebkac = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Home.this, PebkacActivity.class);
			startActivity(intent);
		}
	};
	public OnClickListener openBash = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Home.this, BashActivity.class);
			startActivity(intent);
		}
	};

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.home_menu_about:
				LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.about, (ViewGroup) findViewById(R.id.home_menu_about));
				Builder adb = new AlertDialog.Builder(this);
				adb.setTitle(getResources().getString(R.string.about_title));
				adb.setView(layout);
				adb.create();
				adb.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}