package com.farhatty.user.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.farhatty.user.R;
import com.farhatty.user.Utiliti.ObservableStickyScrollView;
import com.farhatty.user.Utiliti.ResourcesHelper;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.farhatty.user.activity.ViewHallActivity.makeTextViewResizable;

/**
 * Created by user on 12/3/2017.
 */





public class ViewCarActivity  extends AppCompatActivity  implements OnMapReadyCallback {
    private static final String TAG = MainActivity.class.getSimpleName();
    String view_car_id;
    String view_car_name;
    private MaterialRippleLayout lyt_add_cart;
    String view_car_image;
    String view_car_location;
    String view_car_size;
    String view_car_lat;
    String view_car_lng;
    String view_car_desp;
    String view_car_price;

    TextView order;
    private MapView mMapView;
    Double view_car_lat_d;
    Double view_car_lng_d;
    LatLng car_Location ;
    TextView introTextView ;
    TextView addressTextView;
    TextView priceTextView ;
    TextView sizeTextView ;
    TextView phoneTextView ;
    TextView emailTextView ;

    String TAG_re = "Response";
    int car_id;
    SoapObject resultString;
    ProgressDialog progressDialog;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_car_detail);

       // mMapView = (MapView) findViewById(R.id.fragment_map_mapview);
      //  mMapView.onCreate(savedInstanceState);


        view_car_id =  getIntent().getStringExtra("car_id_s") ;
        view_car_name = getIntent().getStringExtra("car_name_s");
        view_car_image = getIntent().getStringExtra("car_image_s");

        car_id = Integer.parseInt(view_car_id);


        renderViewToolbar();
        renderOrder();


    }


    private void renderViewToolbar() {

        new getService().execute();

        final ObservableStickyScrollView observableStickyScrollView = (ObservableStickyScrollView) findViewById(R.id.container_content);
        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        final View panelTopView = findViewById(R.id.toolbar_image_panel_top);
        final View panelBottomView = findViewById(R.id.toolbar_image_panel_bottom);
        final ImageView imageView = (ImageView) findViewById(R.id.toolbar_image_imageview);
        final TextView titleTextView = (TextView) findViewById(R.id.toolbar_image_title);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(view_car_name!=null) {

            titleTextView.setText(String.valueOf(view_car_name));

        }else {

            titleTextView.setVisibility(View.GONE);
        }

        if(view_car_image!=null) {
            Glide.with(this)
                    .load(view_car_image)
                    .into(imageView);
        }else{
            view_car_image = "http://apps.napta-tech.com/apps/app_icon.jpg";
        }


        observableStickyScrollView.setOnScrollViewListener(new ObservableStickyScrollView.ScrollViewListener() {
            private final int THRESHOLD = ViewCarActivity.this.getResources().getDimensionPixelSize(R.dimen.toolbar_image_gap_height);
            private final int PADDING_LEFT = ViewCarActivity.this.getResources().getDimensionPixelSize(R.dimen.toolbar_image_title_padding_right);
            private final int PADDING_BOTTOM = ViewCarActivity.this.getResources().getDimensionPixelSize(R.dimen.global_spacing_xs);
            private final float SHADOW_RADIUS = 16;

            private int mPreviousY = 0;
            private ColorDrawable mTopColorDrawable = new ColorDrawable();
            private ColorDrawable mBottomColorDrawable = new ColorDrawable();


            @Override
            public void onScrollChanged(ObservableStickyScrollView scrollView, int x, int y, int oldx, int oldy) {

                if (y > THRESHOLD && mPreviousY > THRESHOLD) return;

                int alpha = (int) (y * (255f / (float) THRESHOLD));
                if (alpha > 255) alpha = 255;

                mTopColorDrawable.setColor(ResourcesHelper.getValueOfAttribute(ViewCarActivity.this, R.attr.colorPrimary));
                mTopColorDrawable.setAlpha(alpha);
                mBottomColorDrawable.setColor(ResourcesHelper.getValueOfAttribute(ViewCarActivity.this, R.attr.colorPrimary));
                mBottomColorDrawable.setAlpha(alpha);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    panelTopView.setBackground(mTopColorDrawable);
                    panelBottomView.setBackground(mBottomColorDrawable);
                } else {
                    panelTopView.setBackgroundDrawable(mTopColorDrawable);
                    panelBottomView.setBackgroundDrawable(mBottomColorDrawable);
                }

                float translation = y / 2;

                imageView.setTranslationY(translation);

                int paddingLeft = (int) (y * (float) PADDING_LEFT / (float) THRESHOLD);
                if (paddingLeft > PADDING_LEFT) paddingLeft = PADDING_LEFT;

                int paddingRight = PADDING_LEFT - paddingLeft;

                int paddingBottom = (int) ((THRESHOLD - y) * (float) PADDING_BOTTOM / (float) THRESHOLD);
                if (paddingBottom < 0) paddingBottom = 0;

                titleTextView.setPadding(paddingLeft, 0, paddingRight, paddingBottom);

                float radius = ((THRESHOLD - y) * SHADOW_RADIUS / (float) THRESHOLD);

                titleTextView.setShadowLayer(radius, 0f, 0f, getResources().getColor(android.R.color.black));

                mPreviousY = y;
            }
        });



        observableStickyScrollView.post(new Runnable() {
            @Override
            public void run() {
                observableStickyScrollView.scrollTo(0, observableStickyScrollView.getScrollY() - 1);
            }
        });
    }


    private void renderViewInfo() {

        introTextView = (TextView) findViewById(R.id.fragment_intro);
        priceTextView = (TextView) findViewById(R.id.fragment_price);

        if (view_car_desp != null){

            introTextView.setText ( Html.fromHtml ( view_car_desp ) );
            introTextView.setVisibility ( View.VISIBLE );
            makeTextViewResizable(introTextView, 1, "View More", true);

        }else {

            introTextView.setVisibility(View.GONE);
        }


        if(view_car_price!=null) {
            priceTextView.setText(view_car_price + " " + getResources ().getString ( R.string.currency ));
            priceTextView.setVisibility(View.VISIBLE);
        }else{
            priceTextView.setVisibility(View.GONE);
        }


    }


    private void renderOrder(){

        lyt_add_cart = (MaterialRippleLayout) findViewById(R.id.lyt_add_cart);
        lyt_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent o = new Intent ( ViewCarActivity.this, OrderCarActivity.class );
                o.putExtra ( "car_id",view_car_id );
                o.putExtra ( "price", view_car_price );
                startActivity ( o );

            }
        });
    }



    private void initMap() {

        try {
            MapsInitializer.initialize(this);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }






    public void getLocation() {
        String SOAP_ACTION = "http://farhatty.sd/GetlocationMaps";
        String METHOD_NAME = "GetlocationMaps";
        String NAMESPACE = "http://farhatty.sd/";
        String URL = "http://farhatty.sd/WebService.asmx";

        try {
            SoapObject Request = new SoapObject ( NAMESPACE, METHOD_NAME );
            Request.addProperty ( "id", car_id );

            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope ( SoapEnvelope.VER11 );
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject ( Request );
            HttpTransportSE transport = new HttpTransportSE ( URL );
            transport.call ( SOAP_ACTION, soapEnvelope );


            resultString = (SoapObject) soapEnvelope.getResponse ();

            for (int i = 0; i < resultString.getPropertyCount (); i++) {

                PropertyInfo pi = new PropertyInfo ();
                resultString.getPropertyInfo ( i, pi );
                Object property = resultString.getProperty ( i );
                if (pi.name.equals ( "locationMaps" ) && property instanceof SoapObject) {
                    SoapObject transDetail = (SoapObject) property;

                    view_car_lat = (String) transDetail.getProperty ( "latitude" ).toString ();
                    view_car_lng = (String) transDetail.getProperty ( "longitude" ).toString ();

                }
            }
            Log.i ( TAG_re, "Result  " + view_car_lat );

            if (view_car_lat == null || view_car_lng == null){

                view_car_lng_d = 0.0;
                view_car_lat_d = 0.0;

            }else {
                view_car_lat_d = Double.parseDouble ( view_car_lat );
                view_car_lng_d = Double.parseDouble ( view_car_lng );
            }

            Log.i ( TAG_re, " double  " + view_car_lat_d + view_car_lng_d );
        } catch (Exception ex) {
            Log.e ( TAG_re, "Error: " + ex.getMessage () );
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

    }

    private class getService extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ViewCarActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle(R.string.title_please_wait);
            progressDialog.setMessage(getResources ().getString ( R.string.title_getting_data ));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            getData();
            getLocation ();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            renderViewInfo();
            progressDialog.dismiss();
           // initMap();
           // setupMap();
           Marker spot = mMap.addMarker ( new MarkerOptions ()
                    .title ( view_car_name )
                    .position ( new LatLng (  view_car_lat_d ,
                            view_car_lng_d ) ));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(spot.getPosition(), 14));

        }

    }

    public void getData() {
        String SOAP_ACTION = "http://farhatty.sd/GetViewCars";
        String METHOD_NAME = "GetViewCars";
        String NAMESPACE = "http://farhatty.sd/";
        String URL = "http://farhatty.sd/WebService.asmx";

        try {
            SoapObject Request = new SoapObject ( NAMESPACE, METHOD_NAME );
            Request.addProperty ( "id", car_id );
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope ( SoapEnvelope.VER11 );
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject ( Request );
            HttpTransportSE transport = new HttpTransportSE ( URL );

            try {

            transport.call ( SOAP_ACTION, soapEnvelope );

            } catch (Exception e) {
                e.printStackTrace ();
                dialogFailedRetry ();

            }

            try {

            resultString = (SoapObject) soapEnvelope.getResponse ();

            } catch (Exception e) {
                e.printStackTrace ();
                dialogFailedRetry ();

            }

            for (int i = 0; i < resultString.getPropertyCount (); i++) {

                PropertyInfo pi = new PropertyInfo ();
                resultString.getPropertyInfo ( i, pi );
                Object property = resultString.getProperty ( i );
                if (pi.name.equals ( "ViewCars" ) && property instanceof SoapObject) {
                    SoapObject transDetail = (SoapObject) property;

                    view_car_desp = (String) transDetail.getProperty ( "Des_ar" ).toString ();
                    view_car_price = (String) transDetail.getProperty ( "Price" ).toString ();

                }
            }
            Log.i ( TAG_re, "Result  " + resultString );
            Log.i ( TAG_re, "Result  " + view_car_price );
        } catch (Exception ex) {
            Log.e ( TAG_re, "Error: " + ex.getMessage () );
            dialogFailedRetry ();
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        progressDialog.dismiss();
    }

    public void dialogFailedRetry() {
        progressDialog.dismiss ();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.failed);
        builder.setMessage(getString(R.string.failed_getting));
        builder.setPositiveButton(R.string.TRY_AGAIN, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new getService ().execute (  );
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








