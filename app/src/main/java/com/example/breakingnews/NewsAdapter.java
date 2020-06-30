package com.example.breakingnews;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsObject> {
    public NewsAdapter(Activity context, ArrayList<NewsObject> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Here we are overriding a method from the ArrayAdapter class!
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        NewsObject currentNews = getItem(position);

        TextView time = (TextView) listItemView.findViewById(R.id.timeTextView);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        time.setText(currentNews.getTime());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView description = (TextView) listItemView.findViewById(R.id.descTextView);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        description.setText(currentNews.getDescription());

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
