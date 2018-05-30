package com.farhatty.user.activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.farhatty.user.R;
import com.farhatty.user.Utiliti.ObservableStickyScrollView;
import com.farhatty.user.Utiliti.ResourcesHelper;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by user on 12/3/2017.
 */

public class ViewOffersActivity  extends AppCompatActivity  {
    private static final String TAG = MainActivity.class.getSimpleName();
    String view_offer_id;
    String view_offer_date;
    String view_offer_title;
    String view_offer_details;
    String view_offer_image;

    TextView order;
    private MapView mMapView;
    Double view_hall_lat_d;
    Double view_hall_lng_d;
    LatLng hal_Location ;
    TextView introTextView ;
    TextView addressTextView;
    TextView priceTextView ;
    TextView sizeTextView ;
    TextView phoneTextView ;
    TextView emailTextView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_offer_detail);



        view_offer_id =  getIntent().getStringExtra("offer_id_s") ;
        view_offer_date = getIntent().getStringExtra("offer_date_s");
        view_offer_title = getIntent().getStringExtra("offer_title_s");
        view_offer_image = getIntent().getStringExtra("offer_image_s");
        view_offer_details = getIntent().getStringExtra("offer_detail_s");

        Log.e ( "TAG","Response : " + view_offer_details );

        renderViewToolbar();
        renderViewInfo();


    }


    private void renderViewToolbar() {

        final ObservableStickyScrollView observableStickyScrollView = (ObservableStickyScrollView) findViewById(R.id.container_content);
        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        final View panelTopView = findViewById(R.id.toolbar_image_panel_top);
        final View panelBottomView = findViewById(R.id.toolbar_image_panel_bottom);
        final ImageView imageView = (ImageView) findViewById(R.id.toolbar_image_imageview);
        final TextView titleTextView = (TextView) findViewById(R.id.toolbar_image_title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nav_text_offer);

        if(view_offer_title!=null) {
            titleTextView.setText(String.valueOf(view_offer_title));
        }else {
            titleTextView.setVisibility(View.GONE);
        }

        if(view_offer_image!=null) {
            Glide.with(this)
                    .load(view_offer_image)
                    .into(imageView);
        }else{

            view_offer_image = "http://apps.napta-tech.com/apps/app_icon.jpg";
        }

        observableStickyScrollView.setOnScrollViewListener(new ObservableStickyScrollView.ScrollViewListener() {
            private final int THRESHOLD = ViewOffersActivity.this.getResources().getDimensionPixelSize(R.dimen.toolbar_image_gap_height);
            private final int PADDING_LEFT = ViewOffersActivity.this.getResources().getDimensionPixelSize(R.dimen.toolbar_image_title_padding_right);
            private final int PADDING_BOTTOM = ViewOffersActivity.this.getResources().getDimensionPixelSize(R.dimen.global_spacing_xs);
            private final float SHADOW_RADIUS = 16;

            private int mPreviousY = 0;
            private ColorDrawable mTopColorDrawable = new ColorDrawable();
            private ColorDrawable mBottomColorDrawable = new ColorDrawable();


            @Override
            public void onScrollChanged(ObservableStickyScrollView scrollView, int x, int y, int oldx, int oldy) {

                if (y > THRESHOLD && mPreviousY > THRESHOLD) return;
                int alpha = (int) (y * (255f / (float) THRESHOLD));
                if (alpha > 255) alpha = 255;


                mTopColorDrawable.setColor(ResourcesHelper.getValueOfAttribute(ViewOffersActivity.this, R.attr.colorPrimary));
                mTopColorDrawable.setAlpha(alpha);
                mBottomColorDrawable.setColor(ResourcesHelper.getValueOfAttribute(ViewOffersActivity.this, R.attr.colorPrimary));
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

        if (view_offer_details != null){

            introTextView.setText (String.valueOf (view_offer_details  ));
            introTextView.setVisibility ( View.VISIBLE );
           // makeTextViewResizable(introTextView, 4, "View More", true);

        }else {

            introTextView.setVisibility(View.GONE);
        }



    }






    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener () {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod( LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan () {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        makeTextViewResizable(tv, 4, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed ();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


}










