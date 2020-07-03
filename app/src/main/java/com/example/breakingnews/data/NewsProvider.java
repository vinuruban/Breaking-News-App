package com.example.breakingnews.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.breakingnews.data.NewsContract.NewsEntry;

public class NewsProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = NewsProvider.class.getSimpleName();

    /**
     * Initialize the provider and the database helper object.
     */
    private NewsDbHelper dbHelper;

    /**
     * URI matcher code for the content URI for the news table
     */
    private static final int NEWS = 100;

    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int NEWS_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS, NEWS); //MAPS TABLE
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS + "/#", NEWS_ID); //MAPS ROW OF TABLE

    }



    @Override
    public boolean onCreate() {
        dbHelper = new NewsDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(NewsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case NEWS_ID:
                // For the NEWS_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = NewsEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the news table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(NewsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

//        Set notification URI on cursor!
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                return insertNews(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertNews(Uri uri, ContentValues values) {

        // LEFT THE CODE BELOW IN CASE VALIDATION NEEDED IN THE CONTENT PROVIDER IN THE FUTURE. CURRENTLY, VALIDATION IS IN THE insertOrUpdatePet() OF EDITOR ACTIVITY.

//        // Data Validation - Check that the name is not null
//        String name = values.getAsString(PetEntry.COLUMN_NAME);
//        if (name.equals("")) {
//            throw new IllegalArgumentException("Pet requires a name");
//        }
//
//        // Data Validation - Check that the breed is not null
//        String breed = values.getAsString(PetEntry.COLUMN_BREED);
//        if (breed.equals("")) {
//            throw new IllegalArgumentException("Pet requires a breed");
//        }
//
//        // Data Validation - Check that the weight is not null
//        Integer weight = values.getAsInteger(PetEntry.COLUMN_WEIGHT);
//        if (weight == null) {
//            throw new IllegalArgumentException("Pet requires valid weight");
//        }

        // Get writeable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Insert the new news with the given values
        long id = database.insert(NewsEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //NOTIFY ALL LISTENERS THAT THE DATA HAS CHANGED FOR THE NEWS CONTENT URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                return updateNews(uri, contentValues, selection, selectionArgs);
            case NEWS_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = NewsEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateNews(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateNews(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // LEFT THE CODE BELOW IN CASE VALIDATION NEEDED IN THE CONTENT PROVIDER IN THE FUTURE. CURRENTLY, VALIDATION IS IN THE insertOrUpdatePet() OF EDITOR ACTIVITY.

//        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
//        // check that the name value is not null.
//        if (values.containsKey(PetEntry.COLUMN_NAME)) { //CHECKS IF ATTRIBUTE IS PRESENT OR NOT
//            String name = values.getAsString(PetEntry.COLUMN_NAME);
//            if (name.equals("")) {
//                throw new IllegalArgumentException("Pet requires a name");
//            }
//        }
//
//        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
//        // check that the weight value is not null.
//        if (values.containsKey(PetEntry.COLUMN_BREED)) {
//            String breed = values.getAsString(PetEntry.COLUMN_BREED);
//            if (breed.equals("")) {
//                throw new IllegalArgumentException("Pet requires a breed");
//            }
//        }
//        Log.i("PetProvider", "insertOrUpdatePet: " );
//
//        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
//        // check that the weight value is valid.
//        if (values.containsKey(PetEntry.COLUMN_WEIGHT)) {
//            Integer weight = values.getAsInteger(PetEntry.COLUMN_WEIGHT);
//            if (weight == null) {
//                throw new IllegalArgumentException("Pet requires valid weight");
//            }
//        }
//
//        // If there are no values to update, then don't try to update the database
//        if (values.size() == 0) {
//            return 0;
//        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int rowsUpdated = database.update(NewsEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            //NOTIFY ALL LISTENERS THAT THE DATA HAS CHANGED FOR THE PET CONTENT URI
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);

        // Track the number of rows that were deleted
        int rowsDeleted;

        switch (match) {
            case NEWS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NEWS_ID:
                // Delete a single row given by the ID in the URI
                selection = NewsEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(NewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            //NOTIFY ALL LISTENERS THAT THE DATA HAS CHANGED (DECREASED) FOR THE PET CONTENT URI
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                return NewsEntry.CONTENT_LIST_TYPE;
            case NEWS_ID:
                return NewsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI: " + uri + " with match " + match);
        }
    }
}
