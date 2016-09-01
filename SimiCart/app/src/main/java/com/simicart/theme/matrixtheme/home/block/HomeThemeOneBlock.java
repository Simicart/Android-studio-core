package com.simicart.theme.matrixtheme.home.block;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.theme.matrixtheme.home.delegate.HomeThemeOneDelegate;

/**
 * Created by frank on 19/08/2016.
 */
public class HomeThemeOneBlock extends SimiBlock implements HomeThemeOneDelegate {

    protected LinearLayout llSearch;
    protected LinearLayout llBanner;
    protected LinearLayout llCate1;
    protected LinearLayout llCate2;
    protected LinearLayout llCate3;
    protected LinearLayout llCate4;
    protected LinearLayout llSpotProduct;

    public HomeThemeOneBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        llSearch = (LinearLayout) id("ll_search");
        llBanner = (LinearLayout) id("ll_banner");
        llCate1 = (LinearLayout) id("ll_cate1");
        llCate2 = (LinearLayout) id("ll_cate2");
        llCate3 = (LinearLayout) id("ll_cate3");
        llCate4 = (LinearLayout) id("ll_cate4");
        llSpotProduct = (LinearLayout) id("ll_spot_product");

    }

    @Override
    public void showSearch(View searchView) {
        llSearch.removeAllViewsInLayout();
        llSearch.addView(searchView);
    }

    @Override
    public void showBanner(View bannerView) {
        llBanner.removeAllViewsInLayout();
        llBanner.addView(bannerView);
    }

    @Override
    public void showCate(View cateView, int typeCate) {
        switch (typeCate) {
            case 1:
                llCate1.removeAllViewsInLayout();
                llCate1.addView(cateView);
                break;
            case 2:
                llCate2.removeAllViewsInLayout();
                llCate2.addView(cateView);
                break;
            case 3:
                llCate3.removeAllViewsInLayout();
                llCate3.addView(cateView);
                break;
            case 4:
                llCate4.removeAllViewsInLayout();
                llCate4.addView(cateView);
                break;
        }
    }

    @Override
    public void showSpotProduct(View spotProductView) {
        llSpotProduct.removeAllViewsInLayout();
        llSpotProduct.addView(spotProductView);
    }
}
