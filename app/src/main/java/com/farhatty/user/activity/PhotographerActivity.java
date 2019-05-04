package com.farhatty.user.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.farhatty.user.R;
import com.farhatty.user.Utiliti.ConnectivityReceiver;
import com.farhatty.user.Utiliti.MyDividerItemDecoration;
import com.farhatty.user.adapter.PhotographerAdapter;
import com.farhatty.user.model.Artist;
import com.farhatty.user.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed K Madani on 12/5/2017.
 */

public class PhotographerActivity extends AppCompatActivity implements PhotographerAdapter.PhotographerAdapterListener {
    private static final String TAG = MainActivity.class.getSimpleName ();
    private RecyclerView recyclerView;
    private List <Photo> photoList;
    private PhotographerAdapter mAdapter;
    private SearchView searchView;
    private ProgressBar pbar;
    private CoordinatorLayout coordinatorLayout;


    private static String URL = "http://devandroid.pixels-sd.com/weddingApp/photo.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_photo );

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder ().permitAll ().build ();
        StrictMode.setThreadPolicy ( policy );

        Toolbar toolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );

        pbar = (ProgressBar) findViewById ( R.id.pbar );
        coordinatorLayout = (CoordinatorLayout) findViewById ( R.id
                .coordinatorLayout );


        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setTitle ( R.string.service_text_photo_studio );

        recyclerView = (RecyclerView) findViewById ( R.id.recycler_view );
        photoList = new ArrayList <> ();
        mAdapter = new PhotographerAdapter ( this, photoList, this );


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager ( getApplicationContext () );
        recyclerView.setLayoutManager ( mLayoutManager );
        recyclerView.setItemAnimator ( new DefaultItemAnimator () );
        recyclerView.addItemDecoration ( new MyDividerItemDecoration ( this, DividerItemDecoration.VERTICAL, 36 ) );
        recyclerView.setAdapter ( mAdapter );


        checkConnection ();

    }


    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected ();
        showSnack ( isConnected );
    }

    private void showSnack(boolean isConnected) {

        if (isConnected) {

            loadPhotoList();


        } else {

            Snackbar snackbar = Snackbar
                    .make ( coordinatorLayout, R.string.no_internet_connection, Snackbar.LENGTH_LONG )
                    .setAction ( R.string.retry, new View.OnClickListener () {
                        @Override
                        public void onClick(View view) {

                            checkConnection ();
                        }
                    } );

            snackbar.setActionTextColor ( Color.RED );

            View sbView = snackbar.getView ();
            TextView textView = (TextView) sbView.findViewById ( android.support.design.R.id.snackbar_text );
            textView.setTextColor ( Color.WHITE );
            snackbar.show ();

            pbar.setVisibility ( View.GONE );

        }

    }



    private void loadPhotoList() {

        pbar.setVisibility ( View.VISIBLE );
        pbar.setIndeterminate ( false );

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray photoArray = obj.getJSONArray("photo");
                            Log.d("Json Array", "" +photoArray);

                            for (int i=0; i < photoArray.length(); i++){

                                JSONObject photoObject = photoArray.getJSONObject(i);

                                String photo_id =  photoObject.getString("photo_id");
                                String photo_image = photoObject.getString("photo_image");
                                String photo_name = photoObject.getString("photo_name");
                                String photo_location = photoObject.getString("photo_location");
                                String photo_desp = photoObject.getString("photo_desp");

                                Photo photo = new Photo ();

                                photo.setId ( photo_id );
                                photo.setName ( photo_name );
                                photo.setImage ( photo_image );
                                photo.setLocation ( photo_location );
                                photo.setDesp ( photo_desp );

                                photoList.add ( photo );

                                pbar.setVisibility ( View.GONE );
                                mAdapter.notifyDataSetChanged ();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        dialogFailedRetry ();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Error " , " " +error.getMessage());
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater ().inflate ( R.menu.menu_main, menu );
//
//        SearchManager searchManager = (SearchManager) getSystemService ( Context.SEARCH_SERVICE );
//        searchView = (SearchView) menu.findItem ( R.id.action_search )
//                .getActionView ();
//        searchView.setSearchableInfo ( searchManager
//                .getSearchableInfo ( getComponentName () ) );
//        searchView.setMaxWidth ( Integer.MAX_VALUE );
//
//        searchView.setOnQueryTextListener ( new SearchView.OnQueryTextListener () {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                mAdapter.getFilter ().filter ( query );
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                // filter recycler view when text is changed
//                mAdapter.getFilter ().filter ( query );
//                return false;
//            }
//        } );
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId ();
//
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed ();
//                return true;
//        }
//
//        if (id == R.id.action_search) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected ( item );
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        if (!searchView.isIconified ()) {
//            searchView.setIconified ( true );
//            return;
//        }
//        super.onBackPressed ();
//    }


    @Override
    public void onPhotographerSelected(Photo Photo) {

        Intent i = new Intent ( this, ViewPhotographerActivity.class );

        i.putExtra ( "photo_id_s", Photo.getId () );
        i.putExtra ( "photo_name_s", Photo.getName () );
        i.putExtra ( "photo_image_s", Photo.getImage () );
        i.putExtra ( "photo_lat_s", Photo.getLat () );
        i.putExtra ( "photo_lng_s", Photo.getlng () );
        i.putExtra ( "photo_location_s", Photo.getLocation () );
        i.putExtra ( "photo_desp_s", Photo.getDesp () );
        startActivity ( i );

    }

    public void dialogFailedRetry() {

        pbar.setVisibility ( View.GONE );
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.failed);
        builder.setMessage(getString(R.string.failed_getting));
        builder.setPositiveButton(R.string.TRY_AGAIN, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loadPhotoList();
            }
        });
        builder.setNegativeButton(R.string.Close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent (getApplicationContext(), MainActivity.class));
            }
        });
        builder.show();
    }

}


