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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.farhatty.user.R;
import com.farhatty.user.Utiliti.MyDividerItemDecoration;
import com.farhatty.user.Utiliti.ObservableStickyScrollView;
import com.farhatty.user.Utiliti.ResourcesHelper;
import com.farhatty.user.adapter.ChefServiceAdapter;
import com.farhatty.user.model.OrdersCH;
import com.farhatty.user.model.ServiceChef;
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

import java.util.ArrayList;
import java.util.List;

import static com.farhatty.user.activity.ViewHallActivity.makeTextViewResizable;

/**
 * Created by user on 12/3/2017.
 */





public class ViewChefActivity extends AppCompatActivity implements ChefServiceAdapter.ServiceChefAdapterListener,OnMapReadyCallback {
    private static final String TAG = MainActivity.class.getSimpleName();
    String view_chef_id;
    String view_chef_name;
    String view_chef_price;
    String view_chef_image;
    String view_chef_location;
    String view_chef_size;
    String view_chef_lat;
    String view_chef_lng;
    String view_chef_desp;
    private MaterialRippleLayout lyt_add_cart;

    TextView order;
    private MapView mMapView;
    Double view_chef_lat_d;
    Double view_chef_lng_d;
    LatLng chef_Location ;
    TextView introTextView ;
    TextView addressTextView;
    TextView priceTextView ;
    TextView sizeTextView ;
    TextView phoneTextView ;
    TextView emailTextView ;
    ProgressDialog progressDialog;
    String TAG_re = "Response";
    int chef_id;
    SoapObject resultString;

    public static List<OrdersCH> orderch;
    private List<ServiceChef> chefService;
    private ChefServiceAdapter mAdapter;
    private RecyclerView recyclerView;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_hall_detail);


        view_chef_id =   getIntent().getStringExtra("chef_id_s") ;
        view_chef_name = getIntent().getStringExtra("chef_name_s");
        view_chef_image = getIntent().getStringExtra("chef_image_s");
        view_chef_location = getIntent().getStringExtra("chef_location_s");
        view_chef_size = getIntent().getStringExtra("chef_size_s");
        view_chef_desp = getIntent().getStringExtra("chef_desp_s");


        chef_id = Integer.parseInt(view_chef_id);
        orderch = new ArrayList<> ();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        chefService = new ArrayList<> ();
        mAdapter = new ChefServiceAdapter(this, chefService, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager (getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator ());
        recyclerView.addItemDecoration(new MyDividerItemDecoration (this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

       // mMapView = (MapView) findViewById(R.id.fragment_map_mapview);
        //mMapView.onCreate(savedInstanceState);

        renderViewToolbar();
        renderViewInfo();
        renderOrder();

    }





    private void renderViewToolbar() {

        new getService ().execute();

        final ObservableStickyScrollView observableStickyScrollView = (ObservableStickyScrollView) findViewById(R.id.container_content);
        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        final View panelTopView = findViewById(R.id.toolbar_image_panel_top);
        final View panelBottomView = findViewById(R.id.toolbar_image_panel_bottom);
        final ImageView imageView = (ImageView) findViewById(R.id.toolbar_image_imageview);
        final TextView titleTextView = (TextView) findViewById(R.id.toolbar_image_title);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if(view_chef_name!=null) {

            titleTextView.setText(String.valueOf(view_chef_name));

        }else {
            titleTextView.setVisibility(View.GONE);
        }


        if(view_chef_image!=null) {
            Glide.with(this)
                    .load(view_chef_image)
                    .into(imageView);
        }else{
            view_chef_image = "http://apps.napta-tech.com/apps/app_icon.jpg";
        }

        observableStickyScrollView.setOnScrollViewListener(new ObservableStickyScrollView.ScrollViewListener() {
            private final int THRESHOLD = ViewChefActivity.this.getResources().getDimensionPixelSize(R.dimen.toolbar_image_gap_height);
            private final int PADDING_LEFT = ViewChefActivity.this.getResources().getDimensionPixelSize(R.dimen.toolbar_image_title_padding_right);
            private final int PADDING_BOTTOM = ViewChefActivity.this.getResources().getDimensionPixelSize(R.dimen.global_spacing_xs);
            private final float SHADOW_RADIUS = 16;

            private int mPreviousY = 0;
            private ColorDrawable mTopColorDrawable = new ColorDrawable();
            private ColorDrawable mBottomColorDrawable = new ColorDrawable();


            @Override
            public void onScrollChanged(ObservableStickyScrollView scrollView, int x, int y, int oldx, int oldy) {

                if (y > THRESHOLD && mPreviousY > THRESHOLD) return;

                int alpha = (int) (y * (255f / (float) THRESHOLD));
                if (alpha > 255) alpha = 255;

                // set color drawables
                mTopColorDrawable.setColor(ResourcesHelper.getValueOfAttribute(ViewChefActivity.this, R.attr.colorPrimary));
                mTopColorDrawable.setAlpha(alpha);
                mBottomColorDrawable.setColor(ResourcesHelper.getValueOfAttribute(ViewChefActivity.this, R.attr.colorPrimary));
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
        addressTextView = (TextView) findViewById(R.id.fragment_address);
        priceTextView = (TextView) findViewById(R.id.fragment_price);
        sizeTextView = (TextView) findViewById(R.id.fragment_size);

        if(view_chef_desp != null) {

            introTextView.setText ( Html.fromHtml ( view_chef_desp ) );
            introTextView.setVisibility ( View.VISIBLE );
            makeTextViewResizable(introTextView, 1, "View More", true);

        }else {

            introTextView.setVisibility(View.GONE);
        }

        if (view_chef_location != null) {
            addressTextView.setText(view_chef_location);
            addressTextView.setVisibility(View.VISIBLE);
        } else{
            addressTextView.setVisibility(View.GONE);
        }
        if(view_chef_price!=null) {
            priceTextView.setText(view_chef_price + " " + getResources ().getString ( R.string.currency ));
            priceTextView.setVisibility(View.VISIBLE);
        }else{
            priceTextView.setVisibility(View.GONE);
        }


        if(view_chef_size!=null) {
            sizeTextView.setText(view_chef_size);
            sizeTextView.setVisibility(View.VISIBLE);
        }else {
            sizeTextView.setVisibility(View.GONE);
        }


    }


    private void renderOrder(){

        lyt_add_cart = (MaterialRippleLayout) findViewById(R.id.lyt_add_cart);

        lyt_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "";

                List<ServiceChef> stList = ((ChefServiceAdapter) mAdapter).getServicechefist ();

                orderch.clear ();

                for (int i = 0; i < stList.size(); i++) {
                    ServiceChef singlService = stList.get(i);
                    if (singlService.isSelected() == true) {

                        String service_title = singlService.getTitle_ar ();
                        String service_price = singlService.getPrice ();

                        OrdersCH od = new OrdersCH (  );
                        od.setTitle ( service_title );
                        od.setService_Price ( service_price );

                        orderch.add(od);

                    }

                }

                Intent o = new Intent ( ViewChefActivity.this,OrderChefActivity.class );
                o.putExtra("chef_id_s", view_chef_id);
                startActivity ( o );

            }
            /**
             Intent o = new Intent(ViewHallActivity.this, OrderHallActivity.class);

             o.putExtra("price",view_hall_price);

             startActivity(o);
             **/
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





    @Override
    public void onServiceChefelected(ServiceChef serviceChef) {

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
            progressDialog = new ProgressDialog (ViewChefActivity.this);
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
            progressDialog.dismiss();
            mAdapter.notifyDataSetChanged();
           // initMap();
            //setupMap();
          Marker spot = mMap.addMarker ( new MarkerOptions ()
                    .title ( view_chef_name )
                    .position ( new LatLng (  view_chef_lat_d ,
                            view_chef_lng_d ) ));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(spot.getPosition(), 14));

        }

    }



    public void getLocation() {
        String SOAP_ACTION = "http://farhatty.sd/GetlocationMaps";
        String METHOD_NAME = "GetlocationMaps";
        String NAMESPACE = "http://farhatty.sd/";
        String URL = "http://farhatty.sd/WebService.asmx";

        try {
            SoapObject Request = new SoapObject ( NAMESPACE, METHOD_NAME );
            Request.addProperty ( "id", chef_id );
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

                    view_chef_lat = (String) transDetail.getProperty ( "latitude" ).toString ();
                    view_chef_lng = (String) transDetail.getProperty ( "longitude" ).toString ();


                }
            }
            Log.i ( TAG_re, "Result  " + view_chef_lat );

            if (view_chef_lat == null || view_chef_lng == null) {

                view_chef_lng_d = 0.0;
                view_chef_lat_d = 0.0;

            } else {
                view_chef_lat_d = Double.parseDouble ( view_chef_lat );
                view_chef_lng_d = Double.parseDouble ( view_chef_lng );
            }

            Log.i ( TAG_re, " double  " + view_chef_lat_d + view_chef_lng_d );
        } catch (Exception ex) {
            Log.e ( TAG_re, "Error: " + ex.getMessage () );
        }

    }

    public void getData() {
        String SOAP_ACTION = "http://farhatty.sd/GetService";
        String METHOD_NAME = "GetService";
        String NAMESPACE = "http://farhatty.sd/";
        String URL = "http://farhatty.sd/WebService.asmx";

        try {
            SoapObject Request = new SoapObject ( NAMESPACE, METHOD_NAME );
            Request.addProperty ( "id", chef_id );
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
                if (pi.name.equals ( "service" ) && property instanceof SoapObject) {
                    SoapObject transDetail = (SoapObject) property;

                    String service_price = (String) transDetail.getProperty ( "price" ).toString ();
                    String service_title_ar = (String) transDetail.getProperty ( "title_ar" ).toString ();
                    String service_title_eng = (String) transDetail.getProperty ( "title_en" ).toString ();
                    String service_descp_ar = (String) transDetail.getProperty ( "Details_ar" ).toString ();
                    String service_descp_eng = (String) transDetail.getProperty ( "Details_en" ).toString ();

                    ServiceChef serv = new ServiceChef (  );

                    serv.setPrice ( service_price );
                    serv.setTitle_ar ( service_title_ar );
                    serv.setTitle_en ( service_title_eng );
                    serv.setDetails_ar (service_descp_ar);
                    serv.setDetails_en (service_descp_eng);

                    chefService.add(serv);

                }
            }
            Log.i ( TAG_re, "Result : " + resultString );
        } catch (Exception ex) {
            Log.e ( TAG_re, "Error: " + ex.getMessage () );
            dialogFailedRetry ();
        }
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











