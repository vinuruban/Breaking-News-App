package com.example.breakingnews;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.breakingnews.data.NewsContract.NewsEntry;
import com.example.breakingnews.data.NewsDbHelper;

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

        ContentValues values = new ContentValues();
        values.put(NewsEntry.COLUMN_ID, currentNews.getId());
        values.put(NewsEntry.COLUMN_TIME, currentNews.getTime());
        values.put(NewsEntry.COLUMN_DESC, currentNews.getDescription());
        values.put(NewsEntry.COLUMN_URL, currentNews.getUrl());


//          IF IM IN THE "ADD A PET" MODE OF EDITOR_ACTIVITY
        Uri currentUri = null;
        if (currentUri == null) {
            // Insert a new pet into the provider, returning the content URI for the new pet.
            Uri newUri = getContext().getContentResolver().insert(NewsEntry.CONTENT_URI, values);
            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(getContext(), "Error with adding news",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(getContext(), "News added",
                        Toast.LENGTH_SHORT).show();
            }
        }
////        IF IM IN THE "EDIT PET" MODE OF EDITOR_ACTIVITY
//        else {
//            int rowsAffected = getContentResolver().update(currentUri, values, null, null); //NOTE FOR URI, IM PASSING IN "PETS/#"
//            // Show a toast message depending on whether or not the update was successful.
//            if (rowsAffected == 0) {
//                // If no rows were affected, then there was an error with the update.
//                Toast.makeText(this, getString(R.string.editor_update_pet_failed),
//                        Toast.LENGTH_SHORT).show();
//            } else {
//                // Otherwise, the update was successful and we can display a toast.
//                Toast.makeText(this, getString(R.string.editor_update_pet_successful),
//                        Toast.LENGTH_SHORT).show();
//                finish(); //TO CLOSE EDITOR_ACTIVITY (AFTER HITTING SAVE) AND RETURN TO CATALOG_ACTIVITY
//            }
//        }
    }

//    /**
//     * Perform the deletion of the pet in the database.
//     */
//    private void deleteNews() { //CALLED IN showDeleteConfirmationDialog()
//        int rowsAffected = getContentResolver().delete(currentUri, null, null); //currentUri to delete a SPECIFIC pet
//        // Show a toast message depending on whether or not the delete was successful.
//        if (rowsAffected == 0) {
//            // If no rows were affected, then there was an error with the delete.
//            Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            // Otherwise, the delete was successful and we can display a toast.
//            Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        // Since the editor shows all pet attributes, define a projection that contains
//        // all columns from the pet table
//        String[] projection = {
//                PetEntry.COLUMN_ID,
//                PetEntry.COLUMN_NAME,
//                PetEntry.COLUMN_BREED,
//                PetEntry.COLUMN_GENDER,
//                PetEntry.COLUMN_WEIGHT };
//
//        // This loader will execute the ContentProvider's query method on a background thread
//        return new CursorLoader(this,   // Parent activity context
//                currentUri,         // Query the content URI for the current pet
//                projection,             // Columns to include in the resulting Cursor
//                null,                   // No selection clause
//                null,                   // No selection arguments
//                null);                  // Default sort order
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//        //NOW WE WILL DISPLAY DATA. REMEMBER IN CATALOG_ACTIVITY, WE SIMPLY PASSED THE CURSOR INTO THE ADAPTER SINCE THE ADAPTER WILL TEAR IT DOWN AND ACTUALLY DISPLAY THE DATA. IN THIS CASE, WE TEAR IT DOWN IN HERE AND IN onLoaderReset().
//
//        // Proceed with moving to the first row of the cursor and reading data from it
//        // (This should be the only row in the cursor)
//        if (cursor.moveToFirst()) {
//            // Find the columns of pet attributes that we're interested in
//            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_NAME);
//            int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_BREED);
//            int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_GENDER);
//            int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_WEIGHT);
//
//            // Extract out the value from the Cursor for the given column index
//            String name = cursor.getString(nameColumnIndex);
//            String breed = cursor.getString(breedColumnIndex);
//            int gender = cursor.getInt(genderColumnIndex);
//            int weight = cursor.getInt(weightColumnIndex);
//
//            // Update the views on the screen with the values from the database
//            mNameEditText.setText(name);
//            mBreedEditText.setText(breed);
//            mWeightEditText.setText(Integer.toString(weight));
//
//            //GENDER
//            switch (gender) {
//                case PetEntry.GENDER_MALE:
//                    mGenderSpinner.setSelection(1); //SELECT FROM DROP-DOWN LIST
//                    break;
//                case PetEntry.GENDER_FEMALE:
//                    mGenderSpinner.setSelection(2);
//                    break;
//                default:
//                    mGenderSpinner.setSelection(0);
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        // If the loader is invalidated, clear out all the data from the input fields.
//        //        onLoaderReset is called when we leave the page, so its better to clean the cursor here
//        mNameEditText.setText("");
//        mBreedEditText.setText("");
//        mWeightEditText.setText("");
//        mGenderSpinner.setSelection(0); // Select "Unknown" gender
//    }

}
