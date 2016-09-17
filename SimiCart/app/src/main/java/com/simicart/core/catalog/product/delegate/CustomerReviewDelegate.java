package com.simicart.core.catalog.product.delegate;

import com.simicart.core.base.delegate.SimiDelegate;

import java.util.ArrayList;

public interface CustomerReviewDelegate extends SimiDelegate {

    public void onUpdateHeaderView(ArrayList<Integer> mRatingStar);

    public void addFooterView();

    public void removeFooterView();
}
