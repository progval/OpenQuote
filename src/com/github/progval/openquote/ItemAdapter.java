package com.github.progval.openquote;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<SiteItem> {

	public ItemAdapter(Context context, int textViewResourceId,
			List<SiteItem> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	private Context context;
	
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = new TextView(this.context);
		view.setText(this.getItem(position).getContent());
		return view;
	}

}
