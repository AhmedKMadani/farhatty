package com.farhatty.user.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farhatty.user.R;
import com.farhatty.user.adapter.FaqAdapter;
import com.farhatty.user.model.Faq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.farhatty.user.Utiliti.UtilMethods.loadJSONFromAsset;


/**
 * Created by AhmedKamal on 8/23/2017.
 */
public class FaqFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private RecyclerView rvFaqs;
    private List<Faq> faqList;
    String faq;

    public FaqFragment() {

    }

    public static FaqFragment newInstance(String param) {
        FaqFragment fragment = new FaqFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        rvFaqs = (RecyclerView) view.findViewById(R.id.rvFaq);
        rvFaqs.setHasFixedSize(true);
        rvFaqs.addItemDecoration(new SpacesItemDecoration(20,20,12));
        rvFaqs.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFaqs.setAdapter(new FaqAdapter(getActivity(),getFaqData()));
        getFaqData();
        return view;
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int leftSpaceing;
        private int rightSpaceing;
        private int bottomSpacing;

        public SpacesItemDecoration(int leftSpaceing, int rightSpaceing, int bottomSpacing) {
            this.leftSpaceing = leftSpaceing;
            this.rightSpaceing = rightSpaceing;
            this.bottomSpacing = bottomSpacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = leftSpaceing;
            outRect.right = rightSpaceing;
            if(parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount()-1) {
                outRect.bottom = bottomSpacing;
            }
        }
    }

    private List<Faq> getFaqData() {

        String lang = Locale.getDefault().toString();
        // Toast.makeText(getApplicationContext(), lang, Toast.LENGTH_LONG).show();

        if(lang.contains ("eng")){
            faq = "faq_en";
        }else  if(lang.contains ("ar")){
            faq = "faq_ar";
        }else {
            faq = "faq_en";
        }


        String jsonString = loadJSONFromAsset(getActivity(), faq);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray faqArray = jsonObject.getJSONArray("faq");
            faqList = new ArrayList<Faq>();
            for(int i=0; i< faqArray.length(); i++) {
                faqList.add(new Faq(faqArray.getJSONObject(i).getString("q"),
                        faqArray.getJSONObject(i).getString("a"),0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(faqList!=null && faqList.size()>0) {
            return faqList;
        } else {
            return null;
        }
    }

    @Override
    public void onResume() {

        super.onResume();
    }
}