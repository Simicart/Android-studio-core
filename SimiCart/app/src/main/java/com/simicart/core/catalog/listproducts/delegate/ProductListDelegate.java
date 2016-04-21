package com.simicart.core.catalog.listproducts.delegate;

import com.simicart.core.base.delegate.SimiDelegate;

import java.util.ArrayList;

/**
 * Created by Simi on 07-Apr-16.
 */
public interface ProductListDelegate extends SimiDelegate {

    void setQty(String qty);

    void removeFooterView();

    void addFooterView();

    void setVisibilityMenuBotton(boolean visible);

    void changeDataView();

    void setTagView(String tag_view);

    void onZoomIn();

    void onZoomOut();

    void setListProductIds(ArrayList<String> listIDs);
}
