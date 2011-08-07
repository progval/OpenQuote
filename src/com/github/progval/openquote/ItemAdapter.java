package com.github.progval.openquote;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<SiteItem> {

	public ItemAdapter(Context context, int textViewResourceId,
			List<SiteItem> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	private Context context;

	/**
	 * Get a view containing the requested item.
	 *
	 * @param position The position of the item.
	 * @param convertView
	 * @param parent
	 * @return The view.
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = new TextView(this.context);
		view.setText(this.getItem(position).getContent());
		return view;
	}

}
