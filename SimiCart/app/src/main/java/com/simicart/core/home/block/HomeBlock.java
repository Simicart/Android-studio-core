package com.simicart.core.home.block;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.home.delegate.HomeDelegate;

import java.util.ArrayList;

/**
 * Created by frank on 17/08/2016.
 */
public class HomeBlock extends SimiBlock implements HomeDelegate {

    protected LinearLayout llBanner;
    protected LinearLayout llCategory;
    protected LinearLayout llSportProduct;

    public HomeBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        llBanner = (LinearLayout) id("ll_banner");
        llCategory = (LinearLayout) id("ll_category");
        llSportProduct = (LinearLayout) id("ll_spotproduct");
    }


    @Override
    public void showBanner(View bannerView) {
        Log.e("HomeBlock", "showBanner");
        llBanner.removeAllViewsInLayout();
        llBanner.addView(bannerView);
    }

    @Override
    public void showCateHome(View cateView) {
        Log.e("HomeBlock", "showCateHome");
        llCategory.removeAllViewsInLayout();
        llCategory.addView(cateView);
    }

    @Override
    public void showSpotProduct(ArrayList<View> views) {
        llSportProduct.removeAllViewsInLayout();
        for (View view : views) {
            llSportProduct.addView(view);
        }
    }
}
