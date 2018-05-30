package com.farhatty.user.tracker;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.farhatty.user.Utiliti.ConnectivityReceiver;

import static com.android.volley.VolleyLog.TAG;


public class AppInitializer extends Application {

    public static final String EVENT_ACTION = "ACTIONS";
    public static final String EVENT_USER_INFO = "USER INFO";

    private RequestQueue mRequestQueue;

    private static AppInitializer appInstance;
    private static AppInitializer cInstance;
    @Override
    public void onCreate() {
        super.onCreate();


    }
    private static AppInitializer mInstance;


    public AppInitializer()
    {
        mInstance = this;
        appInstance = this;
        cInstance = this;
    }
    public static Context getContext()
    {
        return mInstance;
    }


    public static synchronized AppInitializer getInstance() {
        return  appInstance ;

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }




    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


    }




