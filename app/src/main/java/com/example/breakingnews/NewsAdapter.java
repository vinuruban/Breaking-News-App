package com.example.breakingnews;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.breakingnews.data.NewsContract.NewsEntry;

public class NewsAdapter extends ArrayAdapter<NewsObject> {

    NewsObject currentNews;

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
        currentNews = getItem(position);

        TextView time = (TextView) listItemView.findViewById(R.id.timeTextView);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        time.setText(currentNews.getTime());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView description = (TextView) listItemView.findViewById(R.id.descTextView);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        description.setText(currentNews.getDescription());

        //USING THE ID, CHECK IF IT EXISTS IN THE DB, IF NOT, ADD THE NEWS.
        checkAndInsertNews();

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }

    private void checkAndInsertNews() {

        Cursor cursor = getContext().getContentResolver().query(NewsEntry.CONTENT_URI, null, null, null, null);

        //IF DB IS EMPTY AND NO DATA IS FOUND, CURSOR WILL BE EMPTY, THUS STORE ALL DATA
        if (cursor == null) {
            insertNews();
        }
        else {
            //TO CHECK FOR DATA EXISTENCE
            boolean dataExists = false;

            //GET INDEX OF ID COLUMN
            int nameColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_ID);

            //LOOP THROUGH EACH ROW AND COMPARE ID TO currentNews.getId()
            while (cursor.moveToNext()) {
                String id = cursor.getString(nameColumnIndex);
                //IF IDs MATCH, DATA EXISTS
                if (id.equals("" + currentNews.getId())) {
                    dataExists = true;
                }
            }
            //CLEAN CURSOR
            cursor.close();

            //IF DATA ALREADY EXISTS, DON'T ADD AGAIN. ELSE, ADD DATA.
            if (dataExists == false) {
                insertNews();
            }
            else{
                Toast.makeText(getContext(), "Not a new news - won't be stored in SQLite database again",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertNews() {
        ContentValues values = new ContentValues();
        values.put(NewsEntry.COLUMN_ID, currentNews.getId());
        values.put(NewsEntry.COLUMN_TIME, currentNews.getTime());
        values.put(NewsEntry.COLUMN_DESC, currentNews.getDescription());
        values.put(NewsEntry.COLUMN_URL, currentNews.getUrl());

        // Insert a new pet into the provider, returning the content URI for the new pet.
        Uri newUri = getContext().getContentResolver().insert(NewsEntry.CONTENT_URI, values);
        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(getContext(), "Error with adding news",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(getContext(), "New news found - stored in the SQLite database",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
