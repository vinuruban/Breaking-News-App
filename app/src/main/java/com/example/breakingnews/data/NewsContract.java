package com.example.breakingnews.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class NewsContract { //INCLUDES ALL THE CONSTANTS!

    public static final String CONTENT_AUTHORITY = "com.example.breakingnews";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NEWS = "news";

    public static final class NewsEntry implements BaseColumns { //specific to news table

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NEWS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of news.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
//        ^^ "vnd.android.cursor.dir/com.example.breakingnews/news"
//        for multiple records

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
//        ^^ "vnd.android.cursor.item/com.example.breakingnews/news"
//        for a single record

        public static final String TABLE_NAME = "news";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DESC = "title";
        public static final String COLUMN_URL = "url";
    }
}
