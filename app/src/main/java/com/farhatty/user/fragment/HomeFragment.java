package com.farhatty.user.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.farhatty.user.R;
import com.farhatty.user.Utiliti.ParallaxPageTransformer;
import com.farhatty.user.adapter.HomePagerAdapter;
import com.farhatty.user.model.Project;
import com.farhatty.user.model.Tag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.farhatty.user.Utiliti.UtilMethods.getDrawableIdFromFileName;
import static com.farhatty.user.Utiliti.UtilMethods.loadJSONFromAsset;


public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private ViewPager homeViewPager;
    private List<Tag> tagList;
    private List<Project> projectList;
    String home;
//    private ParallaxPagerTransformer parallaxPagerTransformer;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param) {
        HomeFragment fragment = new HomeFragment();
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
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        homeViewPager = (ViewPager)view.findViewById( R.id.homeViewPager);
//        parallaxPagerTransformer = new ParallaxPagerTransformer(R.id.sliderImageView);
//        homeViewPager.setPageTransformer(true, parallaxPagerTransformer);
        ParallaxPageTransformer pageTransformer = new ParallaxPageTransformer()
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.sliderImageView, 2, 2))
                .addViewToParallax(new ParallaxPageTransformer.ParallaxTransformInformation(R.id.tagGroupHome, -0.65f,
                        ParallaxPageTransformer.ParallaxTransformInformation.PARALLAX_EFFECT_DEFAULT));
        homeViewPager.setPageTransformer(true, pageTransformer);
        return view;
    }

    @Override
    public void onResume() {
        getHomeData();
        homeViewPager.setAdapter(new HomePagerAdapter (getActivity(),projectList));
        super.onResume();
    }

    private void getHomeData() {

        String lang = Locale.getDefault().toString();
       // Toast.makeText(getApplicationContext(), lang, Toast.LENGTH_LONG).show();

        if(lang.contains ("eng")){
            home = "home_en";
        }else  if(lang.contains ("ar")){
            home = "home_ar";
        }else {
            home = "home_en";
        }

        String jsonString = loadJSONFromAsset(getActivity(),home );
        try {
            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("projects");
            projectList = new ArrayList<Project>();
            for(int i=0; i<jsonArray.length();i++) {
                Project project = new Project();
                String name = jsonArray.getJSONObject(i).getString("name");
                String image = jsonArray.getJSONObject(i).getString("image");
                JSONArray infoArray= jsonArray.getJSONObject(i).getJSONArray("project_info");
                tagList = new ArrayList<Tag>();
                for(int j=0; j< infoArray.length(); j++) {
                    Tag tag = new Tag();
                    tag.setPlatform(infoArray.getJSONObject(j).getString("platform"));
                    tag.setUrl(infoArray.getJSONObject(j).getString("url"));
                    tagList.add(tag);
                }
                project.setTitle(name);
                project.setImage(getDrawableIdFromFileName(getActivity(),image));
                project.setTagList(tagList);
                projectList.add(project);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
