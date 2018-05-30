package com.farhatty.user.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.shawnlin.numberpicker.NumberPicker;

import com.bumptech.glide.Glide;
import com.farhatty.user.R;
import com.farhatty.user.Utiliti.ObservableStickyScrollView;
import com.farhatty.user.Utiliti.ResourcesHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Locale;

import static com.farhatty.user.activity.ViewHallActivity.makeTextViewResizable;

/**
 * Created by user on 12/3/2017.
 */





public class ViewHotelActivity  extends AppCompatActivity  implements OnMapReadyCallback {
    private static final String TAG = MainActivity.class.getSimpleName();
    String view_hotel_id;
    String view_hotel_name;

    String view_hotel_image;
    String view_hotel_location;
    String view_hotel_size;
    String view_hotel_lat;
    String view_hotel_lng;
    String view_hotel_desp;
    String view_hotel_price;
    private MaterialRippleLayout lyt_add_cart;
    TextView order;
    private MapView mMapView;
    Double view_hotel_lat_d;
    Double view_hotel_lng_d;
    LatLng hotel_Location ;
    TextView introTextView ;
    TextView addressTextView;
    TextView priceTextView ;
    TextView sizeTextView ;
    TextView phoneTextView ;
    TextView dayTextView ;
    int number_of_day;
    String TAG_re = "Response";
    int hotel_id;
    SoapObject resultString;
    ProgressDialog progressDialog;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_hotel_detail);

        view_hotel_id =  getIntent().getStringExtra("hotel_id_s") ;
        view_hotel_name = getIntent().getStringExtra("hotel_name_s");
        view_hotel_image = getIntent().getStringExtra("hotel_image_s");

        hotel_id = Integer.parseInt(view_hotel_id);

        final NumberPicker numberPicker = (NumberPicker) findViewById(R.id.horizontal_number_picker);
        dayTextView = (TextView) findViewById ( R.id.txt_number_picker );

      //  mMapView = (MapView) findViewById(R.id.fragment_map_mapview);
       // mMapView.onCreate(savedInstanceState);

        renderViewToolbar();
        renderOrder();


        numberPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click on current value");
                number_of_day =  numberPicker.getValue ();

            }
        });

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d("TAG", String.format( Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
                number_of_day = newVal;
                dayTextView.setText (String.valueOf ( number_of_day ) + " " + getString ( R.string.days ) );


            }
        });

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

        if(view_hotel_name!=null) {
            titleTextView.setText(String.valueOf(view_hotel_name));
        }else {
            titleTextView.setVisibility(View.GONE);
        }
        // image

        if(view_hotel_image!=null) {
            Glide.with(this)
                    .load(view_hotel_image)
                    .into(imageView);
        }else{
            view_hotel_image = "http://apps.napta-tech.com/apps/app_icon.jpg";
        }

        observableStickyScrollView.setOnScrollViewListener(new ObservableStickyScrollView.ScrollViewListener() {
            private final int THRESHOLD = ViewHotelActivity.this.getResources().getDimensionPixelSize(R.dimen.toolbar_image_gap_height);
            private final int PADDING_LEFT = ViewHotelActivity.this.getResources().getDimensionPixelSize(R.dimen.toolbar_image_title_padding_right);
            private final int PADDING_BOTTOM = ViewHotelActivity.this.getResources().getDimensionPixelSize(R.dimen.global_spacing_xs);
            private final float SHADOW_RADIUS = 16;

            private int mPreviousY = 0;
            private ColorDrawable mTopColorDrawable = new ColorDrawable();
            private ColorDrawable mBottomColorDrawable = new ColorDrawable();


            @Override
            public void onScrollChanged(ObservableStickyScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y > THRESHOLD && mPreviousY > THRESHOLD) return;

                // calculate panel alpha
                int alpha = (int) (y * (255f / (float) THRESHOLD));
                if (alpha > 255) alpha = 255;

                // set color drawables
                mTopColorDrawable.setColor(ResourcesHelper.getValueOfAttribute(ViewHotelActivity.this, R.attr.colorPrimary));
                mTopColorDrawable.setAlpha(alpha);
                mBottomColorDrawable.setColor(ResourcesHelper.getValueOfAttribute(ViewHotelActivity.this, R.attr.colorPrimary));
                mBottomColorDrawable.setAlpha(alpha);

                // set panel background
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

        if (view_hotel_desp != null){

            introTextView.setText ( Html.fromHtml ( view_hotel_desp ) );
            introTextView.setVisibility ( View.VISIBLE );
            makeTextViewResizable(introTextView, 1, "View More", true);

        }else {

            introTextView.setVisibility(View.GONE);
        }


        if(view_hotel_price!=null) {

            priceTextView.setText(view_hotel_price + " " + getResources ().getString ( R.string.currency ));
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


                if(number_of_day ==0){

                    Snackbar.make(findViewById(android.R.id.content), R.string.number_days_error, Snackbar.LENGTH_SHORT).show();

                }else {

                    Intent o = new Intent ( ViewHotelActivity.this, OrderHotelActivity.class );

                    o.putExtra ( "hotel_id", view_hotel_id );
                    o.putExtra ( "price", view_hotel_price );
                    o.putExtra ( "number_of_d", String.valueOf ( number_of_day ) );

                    startActivity ( o );

                }
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
            Request.addProperty ( "id", hotel_id );
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

                    view_hotel_lat = (String) transDetail.getProperty ( "latitude" ).toString ();
                    view_hotel_lng = (String) transDetail.getProperty ( "longitude" ).toString ();



                }
            }

            Log.i ( TAG_re, "Result  " + view_hotel_lat );

            if (view_hotel_lat == null || view_hotel_lng == null){

                view_hotel_lng_d = 0.0;
                view_hotel_lat_d = 0.0;

            }else {
                view_hotel_lat_d = Double.parseDouble ( view_hotel_lat );
                view_hotel_lng_d = Double.parseDouble ( view_hotel_lng );
            }

            Log.i ( TAG_re, " double  " + view_hotel_lat_d + view_hotel_lng_d );
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
            progressDialog = new ProgressDialog(ViewHotelActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle(R.string.title_please_wait);
            progressDialog.setMessage(getResources ().getString ( R.string.title_getting_data ));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false); // this will disable the back button
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
            //setupMap();
         Marker spot = mMap.addMarker ( new MarkerOptions ()
                    .title ( view_hotel_name )
                    .position ( new LatLng (  view_hotel_lat_d ,
                            view_hotel_lng_d ) ));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(spot.getPosition(), 14));

        }

    }


    public void getData() {
        String SOAP_ACTION = "http://farhatty.sd/GetViewhotels";
        String METHOD_NAME = "GetViewhotels";
        String NAMESPACE = "http://farhatty.sd/";
        String URL = "http://farhatty.sd/WebService.asmx";

        try {
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
                if (pi.name.equals ( "Viewhotels" ) && property instanceof SoapObject) {
                    SoapObject transDetail = (SoapObject) property;

                    view_hotel_desp = (String) transDetail.getProperty ( "Des_ar" ).toString ();
                    view_hotel_price = (String) transDetail.getProperty ( "Price" ).toString ();

                }
            }

            Log.i ( TAG_re, "Result  " + resultString );
            Log.i ( TAG_re, "Result  " + view_hotel_price );

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








