package com.fedor.pavel.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.fedor.pavel.tespfsoft.R;

/**
 * Created by Pavel on 16.01.2017.
 */

public class ColorModel {

    private String mColorTitle;
    private int mColorRes;
    private boolean mExpand;

    public ColorModel() {
    }

    public ColorModel(String colorTitle, @ColorInt int colorRes) {
        mColorTitle = colorTitle;
        mColorRes = colorRes;
    }

    public String getColorTitle() {
        return mColorTitle;
    }

    @ColorInt
    public int getColorRes() {
        return mColorRes;
    }

    public boolean isExpand() {
        return mExpand;
    }

    public void setExpand(boolean expand) {
        mExpand = expand;
    }

    public void setColorTitle(String colorTitle) {
        mColorTitle = colorTitle;
    }

    public void setColorRes(int colorRes) {
        mColorRes = colorRes;
    }

    @SuppressLint("NewApi")
    public static ColorModel CreateColorModel(Context context, String colorTitle, @ColorRes int colorRes) {
        return new ColorModel(colorTitle
                , Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? context.getColor(colorRes)
                : context.getResources().getColor(colorRes));
    }
}
