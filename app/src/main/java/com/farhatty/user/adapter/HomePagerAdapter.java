package com.farhatty.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.farhatty.user.R;
import com.farhatty.user.Utiliti.TagGroup;
import com.farhatty.user.model.Project;
import com.farhatty.user.model.Tag;

import java.util.List;


import static com.farhatty.user.Utiliti.UtilMethods.browseUrl;
import static com.farhatty.user.Utiliti.UtilMethods.getActionBarSize;
import static com.farhatty.user.Utiliti.UtilMethods.getWindowSize;

/**
 * Created by tusharaits on 10/15/15.
 */
public class HomePagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ImageView sliderImageView = null;
    private TextView headlineTextView = null;
    private RelativeLayout tagView = null;
    private TagGroup mTagGroup = null;
    private final static double TAG_VIEW_ASPECT_RATIO = 1d / 9d;
    private int displaySize;
    private List<Project> projectList = null;

    public HomePagerAdapter(Activity context, List<Project> projectList) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        displaySize = getWindowSize(context).y - getActionBarSize(context);
        this.projectList = projectList;
    }


    @Override
    public int getCount() {
        return projectList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.layout_home, container, false);
        sliderImageView = (ImageView) itemView.findViewById(R.id.sliderImageView);
        headlineTextView = (TextView) itemView.findViewById(R.id.headingTv);
        tagView = (RelativeLayout) itemView.findViewById(R.id.tagView);
        tagView.getLayoutParams().height = (int) (displaySize * TAG_VIEW_ASPECT_RATIO);
        mTagGroup = (TagGroup) itemView.findViewById( R.id.tagGroupHome);
//        mTagGroup.getLayoutParams().height = scrollTagView.getLayoutParams().height/2;
//        mTagGroup.getLayoutParams().height = (int) (displaySize * TAG_VIEW_ASPECT_RATIO);
        mTagGroup.setTags(projectList.get(position).getTagList());
        mTagGroup.setOnTagClickListener(mTagClickListener);
        //sliderImageView.setImageResource(projectList.get(position).getImage());

        Glide.with(mContext)
                .load(projectList.get(position).getImage())
                .into(sliderImageView);


        headlineTextView.setText(projectList.get(position).getTitle());
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    private TagGroup.OnTagClickListener mTagClickListener = new TagGroup.OnTagClickListener() {
        @Override
        public void onTagClick(String text) {

        }

        @Override
        public void onTagClick(Tag tag) {
            browseUrl(mContext, tag.getUrl());
        }
    };
}
