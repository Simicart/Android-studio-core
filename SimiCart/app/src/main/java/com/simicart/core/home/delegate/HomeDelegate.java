package com.simicart.core.home.delegate;

import android.view.View;

import com.simicart.core.base.delegate.SimiDelegate;

import java.util.ArrayList;

/**
 * Created by frank on 17/08/2016.
 */
public interface HomeDelegate extends SimiDelegate{

    public void showBanner(View bannerView);

    public void showCateHome(View cateView);

    public void showSearch(View searchView);

    public void showSpotProduct(ArrayList<View> views);

}
