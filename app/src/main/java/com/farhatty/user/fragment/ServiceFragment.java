package com.farhatty.user.fragment;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.farhatty.user.R;
import com.farhatty.user.adapter.ServiceAdapter;
import com.farhatty.user.model.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AhmedKamal on 8/23/2017.
 */
public class ServiceFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private List<service> serviceList;
    CollapsingToolbarLayout collapsingToolbar;
    private ImageView imgHeader;




    public ServiceFragment() {

    }

    public static ServiceFragment newInstance(String param) {
        ServiceFragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.service_main, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);



        serviceList = new ArrayList<>();
        adapter = new ServiceAdapter(getActivity(), serviceList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareService();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void  prepareService() {
        int[] covers = new int[]{
                R.mipmap.app_icon,
                R.mipmap.app_icon,
                R.mipmap.app_icon,
                R.mipmap.app_icon,
                R.mipmap.app_icon,
                R.mipmap.app_icon,
                R.mipmap.app_icon,
                R.mipmap.app_icon,
                R.mipmap.app_icon,
                R.mipmap.app_icon,
                R.mipmap.app_icon,
                R.mipmap.app_icon,
                R.mipmap.app_icon,};

        service a = new service(getString(R.string.service_text_wedding_halls), covers[0]);
        serviceList.add(a);

        a = new service(getString(R.string.service_text_artists), covers[1]);
        serviceList.add(a);

        a = new service(getString(R.string.service_text_hotel_apartments), covers[2]);
        serviceList.add(a);

        a = new service(getString(R.string.service_text_coiffure), covers[3]);
        serviceList.add(a);

        a = new service(getString(R.string.service_text_ms), covers[4]);
        serviceList.add(a);

        a = new service(getString(R.string.service_text_cras), covers[5]);
        serviceList.add(a);

        a = new service(getString(R.string.service_text_photo_studio), covers[6]);
        serviceList.add(a);

        a = new service(getString(R.string.service_text_chefs), covers[7]);
        serviceList.add(a);



        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



}
