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

import com.farhatty.user.R;
import com.farhatty.user.Utiliti.ConnectivityReceiver;
import com.farhatty.user.Utiliti.MyDividerItemDecoration;
import com.farhatty.user.adapter.PhotographerAdapter;
import com.farhatty.user.model.Photo;

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

    SoapObject result = null;

    private static String SOAP_ACTION = "http://farhatty.sd/GetPhotography";
    private static String NAMESPACE = "http://farhatty.sd/";
    private static String METHOD_NAME = "GetPhotography";
    private static String URL = "http://farhatty.sd/WebService.asmx";

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

            new fetchphotos ().execute ();


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


    public class fetchphotos extends AsyncTask <String, Void, SoapObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
            pbar.setVisibility ( View.VISIBLE );
            pbar.setIndeterminate ( false );

        }


        @Override
        protected SoapObject doInBackground(String... params) {

            SoapObject request = new SoapObject ( NAMESPACE, METHOD_NAME );
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope ( SoapEnvelope.VER11 );
            envelope.setOutputSoapObject ( request );
            HttpTransportSE androidHttpTransport = new HttpTransportSE ( URL );

            try {

                androidHttpTransport.call ( SOAP_ACTION, envelope );
            } catch (Exception e) {
                e.printStackTrace ();

                return null;
            }


            try {
                result = (SoapObject) envelope.getResponse ();
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace ();

                return null;

            }

            for (int i = 0; i < result.getPropertyCount (); i++) {

                PropertyInfo pi = new PropertyInfo ();
                result.getPropertyInfo ( i, pi );
                Object property = result.getProperty ( i );
                if (pi.name.equals ( "photography" ) && property instanceof SoapObject) {
                    SoapObject transDetail = (SoapObject) property;

                    String photo_id = ((String) transDetail.getProperty ( "ID" ).toString ());
                    String photo_image = (String) transDetail.getProperty ( "Image" ).toString ();
                    String photo_name = (String) transDetail.getProperty ( "Name_ar" ).toString ();
                    String photo_location = (String) transDetail.getProperty ( "Loacation_ar" ).toString ();
                    String photo_desp = (String) transDetail.getProperty ( "Des_ar" ).toString ();

                    Photo photo = new Photo ();

                    photo.setId ( photo_id );
                    photo.setName ( photo_name );
                    photo.setImage ( "http://farhatty.com/" + photo_image );
                    photo.setLocation ( photo_location );
                    photo.setDesp ( photo_desp );

                    photoList.add ( photo );

                    Log.d ( "Farhatty", "Image : " + photo_image );
                    Log.d ( "Farhatty", "hall name : " + photo_name );

                }


            }
            return request;
        }

        @Override
        protected void onPostExecute(SoapObject response) {
            super.onPostExecute ( response );

            if(response == null){

                dialogFailedRetry ();

            }else {

                pbar.setVisibility ( View.GONE );
                mAdapter.notifyDataSetChanged ();

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.menu_main, menu );

        SearchManager searchManager = (SearchManager) getSystemService ( Context.SEARCH_SERVICE );
        searchView = (SearchView) menu.findItem ( R.id.action_search )
                .getActionView ();
        searchView.setSearchableInfo ( searchManager
                .getSearchableInfo ( getComponentName () ) );
        searchView.setMaxWidth ( Integer.MAX_VALUE );

        searchView.setOnQueryTextListener ( new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mAdapter.getFilter ().filter ( query );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter ().filter ( query );
                return false;
            }
        } );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId ();

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed ();
                return true;
        }

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected ( item );
    }

    @Override
    public void onBackPressed() {

        if (!searchView.isIconified ()) {
            searchView.setIconified ( true );
            return;
        }
        super.onBackPressed ();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility ();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility ( flags );
            // getWindow().setStatusBarColor(Color.WHITE);
        }
    }

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
                new fetchphotos ().execute (  );
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


