package com.simicart.theme.matrixtheme.home.delegate;

import android.view.View;

import com.simicart.core.base.delegate.SimiDelegate;

/**
 * Created by frank on 8/20/16.
 */
public interface HomeThemeOneDelegate extends SimiDelegate {

    public void showBanner(View bannerView);

    public void showCate(View cateView, int typeCate);

    public void showSpotProduct(View spotProductView);

}
