package com.ary.mobile.canadafacts.http;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by gamiprashant on 10/02/2015.
 */
public class Communicator {
    public static final String TAG = Communicator.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mContext;
    private static Communicator mInstance;

    //////////////////////////////////////////////////////////////////
    private Communicator(Context context) {
        // To make sure that this is Application Context
        // Otherwise the context will go away when activity is gone
        mContext = context.getApplicationContext();
    }

    //////////////////////////////////////////////////////////////////
    public static synchronized Communicator getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new Communicator(context);
        }
        return mInstance;
    }

    //////////////////////////////////////////////////////////////////
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }

        return mRequestQueue;
    }

    //////////////////////////////////////////////////////////////////
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    //////////////////////////////////////////////////////////////////
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    //////////////////////////////////////////////////////////////////
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    //////////////////////////////////////////////////////////////////
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
