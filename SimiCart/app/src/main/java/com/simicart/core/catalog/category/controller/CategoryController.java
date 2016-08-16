package com.simicart.core.catalog.category.controller;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.category.model.CategoryModel;
import com.simicart.core.catalog.listproducts.fragment.ProductListFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.slidemenu.fragment.CateSlideMenuFragment;

public class CategoryController extends SimiController {
    protected SimiDelegate mDelegate;
    protected String mID;
    protected OnItemClickListener mClicker;

    @Override
    public void onStart() {
        requestListCategories();

        mClicker = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selecteItem(position);
            }

        };
    }

    private void requestListCategories() {
        mDelegate.showLoading();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                mDelegate.updateView(mModel.getCollection());
            }
        });
        mModel = new CategoryModel();
        ((CategoryModel) mModel).setCategoryID(mID);
        if (!mID.equals("-1")) {
            ((CategoryModel) mModel).setCategoryID(mID);
            mModel.addBody("category_id", mID);
        }
        mModel.request();

    }

    protected void selecteItem(int position) {
        Category category = (Category) mModel.getCollection().getCollection()
                .get(position);
        SimiFragment fragment = null;
        if (category.hasChild()) {
            if (DataLocal.isTablet) {
                fragment = CategoryFragment.newInstance(
                        category.getCategoryId(), category.getCategoryName());
                CateSlideMenuFragment.getIntance().replaceFragmentCategoryMenu(
                        fragment);
            } else {
                fragment = CategoryFragment.newInstance(
                        category.getCategoryId(), category.getCategoryName());
                SimiManager.getIntance().addFragment(fragment);
            }
        } else {
            String urlSearch = "";
            if (category.getCategoryId().equals("-1")) {
                urlSearch = Constants.GET_ALL_PRODUCTS;
            } else {
                urlSearch = Constants.GET_CATEGORY_PRODUCTS;
            }
            ProductListFragment searchFragment = ProductListFragment.newInstance(category.getCategoryId(), category.getCategoryName(), null, null, null);
            SimiManager.getIntance().replaceFragment(searchFragment);
        }
    }

    public OnItemClickListener getClicker() {
        return mClicker;
    }

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }

    public void setDelegate(SimiDelegate delegate) {
        mDelegate = delegate;
    }

    public void setCategoryID(String id) {
        mID = id;
    }

}
