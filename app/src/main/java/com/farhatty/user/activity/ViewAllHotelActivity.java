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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.DefaultItemAnimator;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.farhatty.user.R;
import com.farhatty.user.Utiliti.ConnectivityReceiver;
import com.farhatty.user.Utiliti.MyDividerItemDecoration;
import com.farhatty.user.adapter.AllHotelAdapter;
import com.farhatty.user.model.AllHotel;

import java.util.ArrayList;
import java.util.List;

public class ViewAllHotelActivity extends AppCompatActivity implements AllHotelAdapter.AllHotelAdapterListener {
    private static final String TAG = MainActivity.class.getSimpleName ();
    private RecyclerView recyclerView;
    private List <AllHotel> allhotelList;
    private AllHotelAdapter mAdapter;
    private SearchView searchView;
    private ProgressBar pbar;
    private CoordinatorLayout coordinatorLayout;

    SoapObject result = null;
    String TAG_re = "Response";
    int hotel_id;
    SoapObject resultString;
    String view_hotel_id;
    String view_hotel_name;

    private static String SOAP_ACTION = "http://farhatty.sd/GethotelsDetails";
    private static String NAMESPACE = "http://farhatty.sd/";
    private static String METHOD_NAME = "GethotelsDetails";
    private static String URL = "http://farhatty.sd/WebService.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_all_car );

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        view_hotel_id = getIntent ().getStringExtra ( "hotel_id_s" );
        view_hotel_name = getIntent ().getStringExtra ( "hotel_name_s" );
        hotel_id = Integer.parseInt ( view_hotel_id );


        ActionBar actionBar;
        Toolbar toolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );

        pbar = (ProgressBar) findViewById ( R.id.pbar_wedding );
        coordinatorLayout = (CoordinatorLayout) findViewById ( R.id
                .coordinatorLayout );

        actionBar = getSupportActionBar ();
        actionBar.setDisplayHomeAsUpEnabled ( true );
        actionBar.setHomeButtonEnabled ( true );
        getSupportActionBar ().setTitle ( view_hotel_name );

        recyclerView = (RecyclerView) findViewById ( R.id.recycler_view );
        allhotelList = new ArrayList <> ();
        mAdapter = new AllHotelAdapter ( this, allhotelList, this );

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

            new fetchAllHotels ().execute ();


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


    public class fetchAllHotels extends AsyncTask <String, Void, SoapObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
            pbar.setVisibility ( View.VISIBLE );
            pbar.setIndeterminate ( false );

        }


        @Override
        protected SoapObject doInBackground(String... params) {
            Log.i ( TAG, "doInBackground" );

             String SOAP_ACTION = "http://farhatty.sd/GethotelsDetails";
             String NAMESPACE = "http://farhatty.sd/";
             String METHOD_NAME = "GethotelsDetails";
             String URL = "http://farhatty.sd/WebService.asmx";

                SoapObject Request = new SoapObject ( NAMESPACE, METHOD_NAME );
                Request.addProperty ( "id", hotel_id );
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope ( SoapEnvelope.VER11 );
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject ( Request );
                HttpTransportSE transport = new HttpTransportSE ( URL );


                try {

                transport.call ( SOAP_ACTION, soapEnvelope );
                } catch (Exception e) {

                    e.printStackTrace ();

                    return null;
                }

                try {

                resultString = (SoapObject) soapEnvelope.getResponse ();

                } catch (Exception e) {
                    e.printStackTrace ();

                    return null;
                }

                for (int i = 0; i < resultString.getPropertyCount (); i++) {

                    PropertyInfo pi = new PropertyInfo ();
                    resultString.getPropertyInfo ( i, pi );
                    Object property = resultString.getProperty ( i );
                    if (pi.name.equals ( "hotelsDetails" ) && property instanceof SoapObject) {
                        SoapObject transDetail = (SoapObject) property;

                        String all_hotel_id = ((String) transDetail.getProperty ( "ID" ).toString ());
                        String all_hotel_image = (String) transDetail.getProperty ( "Image" ).toString ();
                        String all_hotel_name = (String) transDetail.getProperty ( "title_ar" ).toString ();

                        AllHotel allhotel = new AllHotel ();

                        allhotel.setId ( all_hotel_id );
                        allhotel.setName ( all_hotel_name );
                        allhotel.setImage ( "http://farhatty.com/" + all_hotel_image );

                        allhotelList.add ( allhotel );

                    }
                }
              return Request;
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

                mAdapter.getFilter ().filter ( query );
                return false;
            }
        } );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId ();

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

        }
    }

    @Override
    public void onAllHotelSelected(AllHotel allhotel) {

            Intent i = new Intent(this,ViewHotelActivity.class);

               i.putExtra("hotel_id_s", allhotel.getId());
               i.putExtra ( "hotel_name_s", allhotel.getName () );
               i.putExtra ( "hotel_image_s",allhotel.getImage () );
                startActivity(i);

    }

    public void dialogFailedRetry() {

        pbar.setVisibility ( View.GONE );
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.failed);
        builder.setMessage(getString(R.string.failed_getting));
        builder.setPositiveButton(R.string.TRY_AGAIN, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new fetchAllHotels ().execute (  );
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


