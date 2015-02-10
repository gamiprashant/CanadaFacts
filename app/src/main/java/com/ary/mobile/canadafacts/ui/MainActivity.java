package com.ary.mobile.canadafacts.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ary.mobile.canadafacts.R;
import com.ary.mobile.canadafacts.http.Communicator;
import com.ary.mobile.canadafacts.model.Fact;
import com.ary.mobile.canadafacts.model.FactResponse;
import com.ary.mobile.canadafacts.util.AryCommons;
import com.ary.mobile.canadafacts.util.FactAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = MainActivity.class
            .getSimpleName();
    private FactAdapter mAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private TextView mTextView;
    private Menu mTopMenu;
    private Context mContext;

    //////////////////////////////////////////////////////////////////
    //Activity Methods
    //////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);

        mTextView = ((TextView)findViewById(R.id.emptyText));

        ListView lv = (ListView) findViewById(R.id.factList);
        mAdapter = new FactAdapter(this);
        lv.setAdapter(mAdapter);
    }

    //////////////////////////////////////////////////////////////////
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadFacts();
    }

    //////////////////////////////////////////////////////////////////
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //////////////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        mTopMenu = menu;

        return true;
    }

    //////////////////////////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                loadFacts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //////////////////////////////////////////////////////////////////
    //OnRefreshListener Methods
    //////////////////////////////////////////////////////////////////
    @Override
    public void onRefresh() {
        loadFacts();
    }


    //////////////////////////////////////////////////////////////////
    //Data Refresh methods
    //////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////
    private void loadFacts() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        //Ideally this should be placed in Shared Preferences to handle different configurations
        String url = "https://dl.dropboxusercontent.com/u/746330/facts.json";

        //Set Refreshing visible and disable refresh menu button
        mSwipeLayout.setRefreshing(true);
        mTextView.setText(R.string.loading);
        if (mTopMenu != null) {
            mTopMenu.findItem(R.id.refresh).setEnabled(false);
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        mSwipeLayout.setRefreshing(false);
                        mTextView.setText(R.string.no_data);
                        if (mTopMenu != null) {
                            mTopMenu.findItem(R.id.refresh).setEnabled(true);
                        }

                        //Parsing response in background
                        JSONTask task = new JSONTask();
                        task.execute(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mSwipeLayout.setRefreshing(false);
                mTextView.setText(R.string.no_data);
                ((TextView)findViewById(R.id.emptyText)).setText(R.string.no_data);
                if (mTopMenu != null) {
                    mTopMenu.findItem(R.id.refresh).setEnabled(true);
                }
                Toast.makeText(mContext, R.string.error_refresh, Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        // As we are using Volley, it will take care of creating a background thread for us
        Communicator.getInstance(this).addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    //////////////////////////////////////////////////////////////////
    //AsyncTask for JSON Parsing
    //////////////////////////////////////////////////////////////////
    private class JSONTask extends AsyncTask<JSONObject, String, FactResponse> {

        //////////////////////////////////////////////////////////////////
        @Override
        protected FactResponse doInBackground(JSONObject... params) {
            if (params == null)
                return null;
            String title = null;
            List<Fact> facts = new ArrayList<Fact>();

            try {
                //Parse and Update Title
                title = AryCommons.safe(params[0].getString("title"));

                //Parse Facts
                JSONArray products = params[0].getJSONArray("rows");

                for (int i = 0; i < products.length(); i++) {
                    JSONObject p = products.getJSONObject(i);

                    String factTitle = AryCommons.safe(p.getString("title"));
                    String description = AryCommons.safe(p.getString("description"));
                    String imageUrl = AryCommons.safe(p.getString("imageHref"));

                    if (!(factTitle.isEmpty() && description.isEmpty() && imageUrl.isEmpty()))
                        facts.add(new Fact(factTitle, description, imageUrl));
                }

                return new FactResponse(title, facts);
            } catch (Exception e) {
                Log.e(TAG, "Error parsing Response", e);
                Toast.makeText(mContext, R.string.error_refresh, Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        //////////////////////////////////////////////////////////////////
        @Override
        protected void onPostExecute(FactResponse response) {
            super.onPostExecute(response);

            if (response == null)
                return;

            //Update Title
            if (response.title != null)
                setTitle(response.title);

            //Update Data in ListView
            mAdapter.clear();
            mAdapter.addAll(response.facts);
            mAdapter.notifyDataSetChanged();
        }
    }
}
