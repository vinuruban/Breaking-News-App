package com.example.breakingnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NewsAdapter adapter;
    private TextView emptyStateTextView;

    private String newsListUrl = "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty";
    private String newsUrl1 = "https://hacker-news.firebaseio.com/v0/item/";
    private String newsUrl2 = ".json?print=pretty";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View loadingIndicator = findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(View.VISIBLE);

        // Create a new adapter that takes an EMPTY list of books as input
        adapter = new NewsAdapter(MainActivity.this, new ArrayList<NewsObject>());

        final ListView listView = (ListView) findViewById(R.id.list);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(adapter);

        //Below is the view that will be viewed if there is no internet connection or no data retrieved
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(emptyStateTextView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            // Start the AsyncTask to fetch the earthquake data
            NewsAsyncTask task = new NewsAsyncTask();
            task.execute(newsListUrl, newsUrl1, newsUrl2);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
//                intent.putExtra("notesPosition", position);
//                startActivity(intent);
//            }
//        });

//        final EditText editText = (EditText) findViewById(R.id.editText);
//        Button button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(new AdapterView.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                String text = editText.getText().toString();
//                String convertText = text.replaceAll(" ", "+").toLowerCase();
//                url = urlAttachment + convertText;
//
//                View loadingIndicator = findViewById(R.id.loading_indicator);
//                loadingIndicator.setVisibility(View.VISIBLE);
//
//                // Create a new adapter that takes an EMPTY list of books as input
//                adapter = new BookAdapter(MainActivity.this, new ArrayList<BookObject>());
//
//                final ListView bookListView = (ListView) findViewById(R.id.list);
//
//                // Set the adapter on the {@link ListView}
//                // so the list can be populated in the user interface
//                bookListView.setAdapter(adapter);
//
//                //Below is the view that will be viewed if there is no internet connection or no data retrieved
//                emptyStateTextView = (TextView) findViewById(R.id.empty_view);
//                bookListView.setEmptyView(emptyStateTextView);
//
//                // Get a reference to the ConnectivityManager to check state of network connectivity
//                ConnectivityManager connMgr = (ConnectivityManager)
//                        getSystemService(Context.CONNECTIVITY_SERVICE);
//                // Get details on the currently active default data network
//                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//
//                // If there is a network connection, fetch data
//                if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
//                    // Start the AsyncTask to fetch the earthquake data
//                    BookAsyncTask task = new BookAsyncTask();
//                    task.execute(url);
//                    Log.e(LOG_TAG, "Passes BookAsyncTask call");
//                } else {
//                    // Otherwise, display error
//                    // First, hide loading indicator so error message will be visible
//                    loadingIndicator.setVisibility(View.GONE);
//
//                    // Update empty state with no connection error message
//                    emptyStateTextView.setText(R.string.no_internet_connection);
//                }
//
//            }
//        });

    }

    private class NewsAsyncTask extends AsyncTask<String, Void, List<NewsObject>> {

        @Override
        protected List<NewsObject> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || (urls[0] == null && urls[1] == null && urls[2] == null)) {
                return null;
            }

            // Perform the HTTP request for earthquake data and process the response.
            List<NewsObject> result = QueryUtils.handleMultipleUrls(urls[0], urls[1], urls[2]);
            return result;
        }

        @Override
        protected void onPostExecute(List<NewsObject> newsObjects) {

            // Hide loading indicator because the data has been loaded
            View loadingIndicator = findViewById(R.id.progressBar);
            loadingIndicator.setVisibility(View.GONE);

            // Set empty state text to display "No books found."
            emptyStateTextView.setText(R.string.no_news);

            // Clear the adapter of previous earthquake data
            adapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (newsObjects != null && !newsObjects.isEmpty()) {
                adapter.addAll(newsObjects); //remember, we earlier passed an empty list into the adapter (" adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>()); "). Now, we add all the data into the adapter.
            }
        }
    }
}