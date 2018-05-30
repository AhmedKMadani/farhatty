package com.farhatty.user.fragment;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.farhatty.user.Utiliti.ConnectivityReceiver;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.farhatty.user.R;
import com.farhatty.user.Utiliti.LocationUtility;
import com.farhatty.user.geolocation.GPSTracker;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.farhatty.user.Utiliti.UtilMethods.browseUrl;
import static com.farhatty.user.Utiliti.UtilMethods.mailTo;
import static com.farhatty.user.Utiliti.UtilMethods.phoneCall;

/**
 * Created by AhmedKamal on 8/23/2017.
 */
public class ContactFragment extends Fragment implements OnMapReadyCallback {
    private static final String ARG_PARAM1 = "param1";
    private MapView mMapView;
    private View view ;
    private Location mLocation = null;
    LatLng compLocation ;
    LatLng myLocation ;
    GPSTracker gps;
    private ProgressBar pbar;
    private CoordinatorLayout coordinatorLayout;

    TextView introTextView ;
    TextView addressTextView ;
    TextView distanceTextView ;
    TextView linkTextView ;
    TextView phoneTextView ;
    TextView emailTextView ;

    String contact_address ;
    String contact_phone  ;
    String contact_email ;
    String contact_into ;
    String contact_lat ;
    String contact_lng ;
    private GoogleMap mMap;
    SoapObject result = null;

    private static String SOAP_ACTION = "http://farhatty.sd/GetContactUs";
    private static String NAMESPACE = "http://farhatty.sd/";
    private static String METHOD_NAME = "GetContactUs";
    private static String URL = "http://farhatty.sd/WebService.asmx";



    public ContactFragment() {

    }

    public static ContactFragment newInstance(String param) {
        ContactFragment fragment = new ContactFragment ();
        Bundle args = new Bundle ();
        args.putString ( ARG_PARAM1, param );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_contact_detail, null, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


          pbar = (ProgressBar) view.findViewById(R.id.pbar);



          gps = new GPSTracker(getActivity());

         pbar.setVisibility ( View.VISIBLE );
         pbar.setIndeterminate(false);

        introTextView = (TextView) view.findViewById(R.id.fragment_poi_detail_info_intro);
        addressTextView = (TextView) view.findViewById(R.id.fragment_poi_detail_info_address);
        distanceTextView = (TextView) view.findViewById(R.id.fragment_poi_detail_info_distance);
        linkTextView = (TextView) view.findViewById(R.id.fragment_poi_detail_info_link);
        phoneTextView = (TextView) view.findViewById(R.id.fragment_poi_detail_info_phone);
        emailTextView = (TextView) view.findViewById(R.id.fragment_poi_detail_info_email);


        introTextView.setVisibility ( View.GONE );
        addressTextView.setVisibility ( View.GONE );
        distanceTextView.setVisibility ( View.GONE );
        linkTextView.setVisibility ( View.GONE );
        phoneTextView.setVisibility ( View.GONE );
        emailTextView.setVisibility ( View.GONE );

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);

        checkConnection();

            emailTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mailTo(getActivity(), contact_email);
                }
            });


        phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneCall(getActivity(), contact_phone);
            }
        });


        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browseUrl(getActivity(), getString(R.string.company_web_page));
            }
        });


        return view;

    }

    private void checkConnection() {

        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {

        if (isConnected) {

            new fetchContactUs ().execute ();


        } else {


            pbar.setVisibility ( View.GONE );
            addressTextView.setText ( R.string.company_address );
            phoneTextView.setText ( R.string.company_phone);
            emailTextView.setText (R.string.company_mail);
            introTextView.setText ( R.string.company_intro );
            linkTextView.setText ( R.string.company_web_page );

            addressTextView.setVisibility ( View.VISIBLE );
            phoneTextView.setVisibility ( View.VISIBLE );
            emailTextView.setVisibility ( View.VISIBLE );
            introTextView.setVisibility ( View.VISIBLE );
            linkTextView.setVisibility ( View.VISIBLE );

            myLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
            compLocation = new LatLng ( 15.88888388,32.39393993 );
            String distance = LocationUtility.getDistanceString(LocationUtility.getDistance(myLocation, compLocation), LocationUtility.isMetricSystem());
            distanceTextView.setText(distance);
            distanceTextView.setVisibility ( View.VISIBLE );
            //initMap();
            //setupMap();


        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        UiSettings settings = mMap.getUiSettings();
        settings.setAllGesturesEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        settings.setZoomControlsEnabled(true);

        Marker spot = mMap.addMarker ( new MarkerOptions ()
                .title ("Farhatty" )
                .position ( new LatLng (  15.88888388 ,
                        32.39393993  ) ));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(spot.getPosition(), 14));

    }


    public class fetchContactUs extends AsyncTask<String, Void, SoapObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
            }

            SoapObject result = null;
            try {
                result = (SoapObject) envelope.getResponse ();
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace ();
            }

            for (int i = 0; i < result.getPropertyCount(); i++) {

                PropertyInfo pi = new PropertyInfo ();
                result.getPropertyInfo ( i, pi );
                Object property = result.getProperty ( i );
                if (pi.name.equals ( "contact" ) && property instanceof SoapObject) {
                    SoapObject transDetail = (SoapObject) property;

                    contact_address = (String) transDetail.getProperty ( "address_ar" ).toString ();
                    contact_phone = (String) transDetail.getProperty ( "phone" ).toString ();
                    contact_email = (String) transDetail.getProperty ( "email" ).toString ();
                    contact_into = (String) transDetail.getProperty ( "descriptionLoc" ).toString ();
                    contact_lat = (String) transDetail.getProperty ( "latitude" ).toString ();
                    contact_lng = (String) transDetail.getProperty ( "longitude" ).toString ();


                }

            }
            return request;
        }

        @Override
        protected void onPostExecute(SoapObject response) {
            super.onPostExecute ( response );
             pbar.setVisibility ( View.GONE );


                addressTextView.setText ( contact_address );
                phoneTextView.setText ( contact_phone );
                emailTextView.setText ( contact_email );
                introTextView.setText ( contact_into );
                linkTextView.setText ( R.string.company_web_page );


            addressTextView.setVisibility ( View.VISIBLE );
            phoneTextView.setVisibility ( View.VISIBLE );
            emailTextView.setVisibility ( View.VISIBLE );
            introTextView.setVisibility ( View.VISIBLE );
            linkTextView.setVisibility ( View.VISIBLE );

            myLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
            compLocation = new LatLng(Double.parseDouble (contact_lat),Double.parseDouble ( contact_lng ));
            String distance = LocationUtility.getDistanceString(LocationUtility.getDistance(myLocation, compLocation), LocationUtility.isMetricSystem());

            distanceTextView.setText(distance);
            distanceTextView.setVisibility ( View.VISIBLE );

           // initMap();
            //setupMap();


            Marker spot = mMap.addMarker ( new MarkerOptions ()
                    .title ( "Farhatty" )
                    .position ( new LatLng ( Double.parseDouble (contact_lat) ,
                            Double.parseDouble ( contact_lng ) )));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(spot.getPosition(), 14));
            }

        }



        private void initMap() {

        try
        {
            MapsInitializer.initialize(getActivity());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }





            @Override
    public void onResume () {
        super.onResume();
    }


}


