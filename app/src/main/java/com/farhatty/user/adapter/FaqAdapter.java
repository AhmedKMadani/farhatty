package com.farhatty.user.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farhatty.user.R;
import com.farhatty.user.model.Faq;

import java.util.List;

/**
 * Created by AhmedKamal on 8/23/2017.
 */
public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqViewHolder> implements View.OnClickListener  {

    private Context mContext;
    private List<Faq> faqList;
    private ValueAnimator mAnimator;
    private int updatePosition = -1;
    private int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    private int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

    public FaqAdapter(Context context, List<Faq> faqList) {
        mContext = context;
        this.faqList = faqList;
    }


    @Override
    public FaqViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_faq, null);
        FaqViewHolder vh = new FaqViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FaqViewHolder holder, final int position) {

        holder.questionTv.setText(faqList.get(position).getQuestion());
        holder.answerTextView.setText(faqList.get(position).getAnswer());
//        if(faqList.get(position).getViewStatus()==0) {
//            holder.extendedView.setVisibility(View.GONE);
//        } else {
//            holder.extendedView.setVisibility(View.VISIBLE);
//        }
        holder.holderPosition = position;
        holder.itemView.setTag(holder);
//        holder.itemView.setOnClickListener(this);
    }



    @Override
    public int getItemCount() {
        return faqList.size();
    }

    @Override
    public void onClick(View v) {
        FaqViewHolder holder = (FaqViewHolder) v.getTag();
        if(faqList.get(holder.holderPosition).getViewStatus()==0) {
            expand(holder);
        } else {
            collapse(holder);
        }
    }

    class FaqViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout faqItemView;
        private RelativeLayout extendedView;
        private TextView questionTv;
        private TextView answerTextView;
        private ImageView arrowImgView;
        private int holderPosition;

        public FaqViewHolder(View itemView) {
            super(itemView);
            faqItemView = (RelativeLayout) itemView.findViewById(R.id.faqItemView);
            extendedView = (RelativeLayout) itemView.findViewById(R.id.extendedView);
            questionTv = (TextView)itemView.findViewById(R.id.questionTv);
            answerTextView = (TextView)itemView.findViewById(R.id.answerTextView);
            arrowImgView = (ImageView)itemView.findViewById(R.id.arrowImgView);
        }
    }

    private void expand(FaqViewHolder holder) {
        holder.extendedView.measure(widthSpec,heightSpec);
        holder.extendedView.setVisibility(View.VISIBLE);
        int lineCount = holder.answerTextView.getLineCount();
        int lineHeight = holder.answerTextView.getLineHeight();
        Log.v("logCheck","expand count: "+holder.answerTextView.getLineCount()+ " height: "+holder.answerTextView.getLineHeight());
        mAnimator = slideAnimator(holder.extendedView, 0, lineCount * lineHeight);
        faqList.get(holder.holderPosition).setViewStatus(1);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    private int getWidthSpec(int width) {
        return View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.UNSPECIFIED);
    }

    private int getHeightSpec(int height) {
        return View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.UNSPECIFIED);
    }

    private void collapse(final FaqViewHolder holder) {
        int finalHeight = holder.extendedView.getHeight();
        //Log.v("logCheck","count: "+holder.answerTextView.getLineCount()+ " height: "+holder.answerTextView.getLineHeight());

        ValueAnimator mAnimator = slideAnimator(holder.extendedView, finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                faqList.get(holder.holderPosition).setViewStatus(0);
                holder.extendedView.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(final View view, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
                view.requestLayout();
            }
        });
        return animator;
    }
}
