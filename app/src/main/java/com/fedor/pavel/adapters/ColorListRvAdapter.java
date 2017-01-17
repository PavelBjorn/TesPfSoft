package com.fedor.pavel.adapters;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fedor.pavel.models.ColorModel;
import com.fedor.pavel.tespfsoft.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Pavel on 16.01.2017.
 */
public class ColorListRvAdapter extends RecyclerView.Adapter<ColorListRvAdapter.ColorViewHolder> {

    private List<ColorModel> mColors = new ArrayList<>();
    private LayoutInflater mInflater;
    @ColorInt
    private int mDefaultColor;

    public ColorListRvAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mDefaultColor = context.getColor(R.color.default_color);
        } else {
            mDefaultColor = context.getResources().getColor(R.color.default_color);
        }
    }

    public void addAll(List<ColorModel> colorModels) {
        int starPos = mColors.size();
        mColors.addAll(colorModels);
        notifyItemRangeInserted(starPos, colorModels.size());
    }

    @Override
    public ColorListRvAdapter.ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColorViewHolder(mInflater.inflate(R.layout.item_rv_color_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ColorListRvAdapter.ColorViewHolder holder, int position) {
        ColorModel color = mColors.get(position);
        holder.toggleHeight(color);
        holder.toggleColors(color);
    }

    @Override
    public int getItemCount() {
        return mColors.size();
    }

    class ColorViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.color_container)
        CardView mCvColorContainer;

        @Bind(R.id.tv_color_title)
        TextView mTvColorTitle;

        private boolean mAnimating;
        private static final long mExpandAnimDuration = 500;

        ColorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mCvColorContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!mAnimating) {
                        final ColorModel colorModel = mColors.get(getAdapterPosition());

                        float scale;
                        if (colorModel.isExpand()) {
                            scale = 0.25f;
                        } else {
                            scale = 4;
                        }

                        ValueAnimator animator = ValueAnimator.ofInt(view.getHeight(), (int) (view.getHeight() * scale));
                        animator.setDuration(mExpandAnimDuration);

                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                ViewGroup.LayoutParams params = view.getLayoutParams();
                                params.height = (int) valueAnimator.getAnimatedValue();
                                view.setLayoutParams(params);
                            }
                        });

                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                                mAnimating = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                mColors.get(getAdapterPosition()).setExpand(!colorModel.isExpand());
                                toggleColors(colorModel);
                                mAnimating = false;
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {
                                mAnimating = false;
                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        animator.start();
                    }
                }
            });
        }

        void toggleHeight(ColorModel color) {
            int height = itemView.getContext().getResources().getDimensionPixelOffset(R.dimen.color_container_height);
            ViewGroup.LayoutParams params = mCvColorContainer.getLayoutParams();
            if (color.isExpand()) {
                params.height = params.height > height ? params.height : height * 4;
                mCvColorContainer.setLayoutParams(params);
            } else {
                params.height = params.height > height ? height : params.height;
            }

        }

        void toggleColors(ColorModel color) {
            mCvColorContainer.setCardBackgroundColor(color.isExpand() ? color.getColorRes() : mDefaultColor);
            mTvColorTitle.setTextColor(color.isExpand() ? mDefaultColor : color.getColorRes());
            mTvColorTitle.setText(color.getColorTitle());
        }

    }

}
