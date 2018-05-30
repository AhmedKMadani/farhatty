package com.farhatty.user.fragment;

import android.support.v4.app.Fragment;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farhatty.user.R;
import com.farhatty.user.Utiliti.ConnectivityReceiver;
import com.farhatty.user.Utiliti.MyDividerItemDecoration;
import com.farhatty.user.activity.MainActivity;
import com.farhatty.user.activity.ViewOffersActivity;
import com.farhatty.user.adapter.OffersAdapter;
import com.farhatty.user.model.Offers;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/4/2018.
 */

public class OffersFragment extends Fragment implements OffersAdapter.OffersAdapterListener {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    View view;
    private RecyclerView recyclerView;
    private List<Offers> offersList;
    private OffersAdapter mAdapter;
    private SearchView searchView;
    private ProgressBar pbar;
    private CoordinatorLayout coordinatorLayout;

    SoapObject result = null;

    private static String SOAP_ACTION = "http://farhatty.sd/blog";
    private static String NAMESPACE = "http://farhatty.sd/";
    private static String METHOD_NAME = "blog";
    private static String URL = "http://farhatty.sd/WebService.asmx";



    public OffersFragment() {

    }


    public static OffersFragment newInstance(String param) {
        OffersFragment fragment = new OffersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate ( R.layout.fragment_offer, container, false );

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        pbar = (ProgressBar) view.findViewById(R.id.pbar_wedding);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id
                .coordinatorLayout);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        offersList = new ArrayList<> ();
        mAdapter = new OffersAdapter  (getActivity () , offersList ,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (getActivity ());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator ());
        recyclerView.addItemDecoration(new MyDividerItemDecoration (getActivity (), DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        checkConnection();

        return view ;

    }


    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {

        if (isConnected) {

            new fetchOffers ().execute (  );

        }else{

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkConnection();
                        }
                    });


            snackbar.setActionTextColor( Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();

            pbar.setVisibility(View.GONE);


        }

    }

    @Override
    public void onOffersSelected(Offers Offers) {
        Intent i = new Intent(getActivity (),ViewOffersActivity.class);

        i.putExtra("offer_id_s", Offers.getId());
        i.putExtra("offer_date_s", Offers.getDate ());
        i.putExtra("offer_title_s",Offers.getTitle_ar ());
        i.putExtra("offer_image_s", Offers.getImage());
        i.putExtra("offer_detail_s", Offers.getDetails_ar ());

        startActivity(i);
    }


    public class fetchOffers extends AsyncTask<String, Void, SoapObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbar.setVisibility ( View.VISIBLE );
            pbar.setIndeterminate(false);

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
                Log.e("TAG","response : "  + result);
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace ();
                return null;
            }

            for (int i = 0; i < result.getPropertyCount(); i++) {

                PropertyInfo pi = new PropertyInfo ();
                result.getPropertyInfo ( i, pi );
                Object property = result.getProperty ( i );
                if (pi.name.equals ( "blogclass" ) && property instanceof SoapObject) {
                    SoapObject transDetail = (SoapObject) property;


                    String blog_id =  ( (String) transDetail.getProperty ( "ID" ).toString () );
                    String blog_image = (String) transDetail.getProperty ( "Image" ).toString ();
                    String blog_date = (String) transDetail.getProperty ( "Date" ).toString ();
                    String blog_details_en = (String) transDetail.getProperty ( "Details_en" ).toString ();
                    String blog_details_ar = (String) transDetail.getProperty ( "Details_ar" ).toString ();
                    String blog_title_en = (String) transDetail.getProperty ( "title_en" ).toString ();
                    String blog_title_ar = (String) transDetail.getProperty ( "title_ar" ).toString ();

                    Offers offers = new Offers ();

                    offers.setId ( blog_id);
                    offers.setDate ( blog_date );
                    offers.setImage ("http://farhatty.com/"+blog_image);
                    offers.setDetails_ar ( blog_details_ar );
                    offers.setDetails_en ( blog_details_en );
                    offers.setTitle_ar ( blog_title_ar);
                    offers.setTitle_en ( blog_title_en);

                    offersList.add(offers);

                }
            }
            return request;
        }

        @Override
        protected void onPostExecute(SoapObject response) {
            super.onPostExecute(response);

            if(response == null){

                dialogFailedRetry ();

            }else {

                pbar.setVisibility ( View.GONE );
                mAdapter.notifyDataSetChanged ();

            }
        }
    }




    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }

    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);

        }
    }




    public void dialogFailedRetry() {

        pbar.setVisibility ( View.GONE );
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity ());
        builder.setTitle(R.string.failed);
        builder.setMessage(getString(R.string.failed_getting));
        builder.setPositiveButton(R.string.TRY_AGAIN, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new fetchOffers ().execute (  );
            }
        });
        builder.setNegativeButton(R.string.Close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent (getActivity (), MainActivity.class));
            }
        });
        builder.show();
    }




}

