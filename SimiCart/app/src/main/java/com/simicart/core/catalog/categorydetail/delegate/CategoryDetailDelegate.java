package com.simicart.core.catalog.categorydetail.delegate;

import com.simicart.core.base.delegate.SimiDelegate;

/**
 * Created by Martial on 8/19/2016.
 */
public interface CategoryDetailDelegate extends SimiDelegate {

    public void showLoadMore(boolean isShow);

    public void changeView();

    public void showBottomMenu(boolean show);

    public String getTagView();

    public void showTotalQuantity(String quantity);

    public void setTagView(String tagView);

}
