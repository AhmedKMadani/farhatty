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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.farhatty.user.R;
import com.farhatty.user.Utiliti.ConnectivityReceiver;
import com.farhatty.user.Utiliti.MyDividerItemDecoration;
import com.farhatty.user.adapter.HallsAdapter;
import com.farhatty.user.model.Halls;

import java.util.ArrayList;
import java.util.List;

public class WeddingHallsActivity extends AppCompatActivity implements HallsAdapter.HallAdapterListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Halls> hallsList;
    private HallsAdapter mAdapter;
    private SearchView searchView;
    private ProgressBar pbar;
    private CoordinatorLayout coordinatorLayout;

    SoapObject result = null;

    private static String SOAP_ACTION = "http://farhatty.sd/GetHalls";
    private static String NAMESPACE = "http://farhatty.sd/";
    private static String METHOD_NAME = "GetHalls";
    private static String URL = "http://farhatty.sd/WebService.asmx";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        ActionBar actionBar;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pbar = (ProgressBar) findViewById(R.id.pbar_wedding);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.service_text_wedding_halls);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        hallsList = new ArrayList<>();
        mAdapter = new HallsAdapter(this, hallsList, this);



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        checkConnection();

    }



    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {

        if (isConnected) {

            new fetchHalls().execute (  );

        }else{

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkConnection();
                        }
                    });


            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();

            pbar.setVisibility(View.GONE);


        }

    }



       public class fetchHalls extends AsyncTask<String, Void, SoapObject> {
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
                   if (pi.name.equals ( "hall" ) && property instanceof SoapObject) {
                       SoapObject transDetail = (SoapObject) property;


                       String hall_id = ((String) transDetail.getProperty ( "ID" ).toString ());
                       String hall_image = (String) transDetail.getProperty ( "Image" ).toString ();
                       String hall_name = (String) transDetail.getProperty ( "Name_ar" ).toString ();
                       String hall_location = (String) transDetail.getProperty ( "Loacation_ar" ).toString ();
                       String hall_price = (String) transDetail.getProperty ( "Price" ).toString ();
                       String hall_size = (String) transDetail.getProperty ( "Size" ).toString ();
                       String hall_desp = (String) transDetail.getProperty ( "Des_ar" ).toString ();

                       Halls hall = new Halls ();

                       hall.setId ( hall_id );
                       hall.setName ( hall_name );
                       hall.setImage ( "http://farhatty.com/" + hall_image );
                       hall.setLocation ( hall_location );
                       hall.setPrice ( hall_price );
                       hall.setSize ( hall_size );
                       hall.setDesp ( hall_desp );

                       hallsList.add ( hall );

                   }
               }
               return request;
           }

           @Override
           protected void onPostExecute(SoapObject response) {
               super.onPostExecute ( response );

               if (response == null) {

                   dialogFailedRetry ();

               } else {

                   pbar.setVisibility ( View.GONE );
                   mAdapter.notifyDataSetChanged ();

               }
           }

       }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed ();
                return true;
        }

        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);

        }
    }

    @Override
    public void onHallSelected(Halls halls) {

        Intent i = new Intent(this,ViewHallActivity.class);

        i.putExtra("hall_id_s", halls.getId());
        i.putExtra("hall_name_s", halls.getName());
        i.putExtra("hall_price_s",halls.getPrice ());
        i.putExtra("hall_image_s", halls.getImage());
        i.putExtra("hall_size_s", halls.getSize());
        i.putExtra("hall_location_s",halls.getLocation());
        i.putExtra("hall_desp_s",halls.getDesp ());
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
                new fetchHalls().execute (  );
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
