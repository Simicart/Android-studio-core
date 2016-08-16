package com.simicart.core.catalog.category.controller;

import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.category.model.ListProductModel;
import com.simicart.core.catalog.listproducts.fragment.ProductListFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;

public class CategoryDetailController extends SimiController {
    protected SimiDelegate mDelegate;
    protected String mID;
    protected OnClickListener mClicker;
    protected String mCatename;
    protected String urlSearch;

    @Override
    public void onStart() {
        if (DataLocal.isTablet) {
            mDelegate.updateView(null);
        } else {
            requestListProducts();
        }

        mClicker = new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mID.equals("-1")) {
                    urlSearch = Constants.GET_ALL_PRODUCTS;
                } else {
                    urlSearch = Constants.GET_CATEGORY_PRODUCTS;
                }
                ProductListFragment searchFragment = ProductListFragment
                        .newInstance(mID, mCatename, null, null, null);
                SimiManager.getIntance().replaceFragment(searchFragment);
            }
        };

    }

    private void requestListProducts() {
        // mDelegate.showDialogLoading();
        mModel = new ListProductModel();
        ((ListProductModel) mModel).setCategoryID(mID);
        mModel.addBody("category_id", mID);
        mModel.addBody("offset", "0");
        mModel.addBody("limit", "10");
        // mModel.addParam("sort_option", mSortType);
        mModel.addBody("width", "300");
        mModel.addBody("height", "300");
        // if (null != jsonFilter) {
        // mModel.addParam("filter", jsonFilter);
        // }
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.updateView(mModel.getCollection());
            }
        });
        mModel.request();

    }

    public OnClickListener getClicker() {
        return mClicker;
    }

    @Override
    public void onResume() {
        if (DataLocal.isTablet) {
            mDelegate.updateView(null);
        } else {
            mDelegate.updateView(mModel.getCollection());
        }
    }

    public void setDelegate(SimiDelegate delegate) {
        mDelegate = delegate;
    }

    public void setCategoryID(String id) {
        mID = id;
    }

    public void setCatename(String mCatename) {
        this.mCatename = mCatename;
    }

}
