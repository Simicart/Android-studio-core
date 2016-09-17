package com.simicart.core.catalog.product.delegate;

import android.view.View;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.catalog.product.adapter.ProductDetailParentAdapter;
import com.simicart.core.style.VerticalViewPager2;

public interface ProductDelegate extends SimiDelegate {


    public void onUpdatePriceView(View view);


    public void onVisibleTopBottom(boolean isVisible);

    public void updateViewPager(VerticalViewPager2 viewpager);

    public void setListenerBack(View.OnKeyListener listenerBack);

    public void updateAdapterProduct(ProductDetailParentAdapter adapter, int currentPosition);
}