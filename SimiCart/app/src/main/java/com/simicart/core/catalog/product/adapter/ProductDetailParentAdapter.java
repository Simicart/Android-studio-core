package com.simicart.core.catalog.product.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.product.controller.ProductDetailParentController;
import com.simicart.core.catalog.product.delegate.ProductDetailAdapterDelegate;
import com.simicart.core.catalog.product.fragment.ProductDetailChildFragment;
import com.simicart.core.common.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductDetailParentAdapter extends FragmentPagerAdapter implements
        ProductDetailAdapterDelegate {
    protected ArrayList<String> mListID;
    protected String mCurrentID = "";
    protected ProductDetailParentController mController;


    public ProductDetailParentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        String id = mListID.get(position);
        if (!Utils.validateString(mCurrentID)) {
            mCurrentID = id;
        } else if (!mCurrentID.equals(id)) {
            mCurrentID = id;
            ((ProductDetailChildFragment) object).onUpdateTopBottom();
        }
    }

    @Override
    public Fragment getItem(int position) {

        String id = mListID.get(position);
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("id", id);
        SimiData data = new SimiData(hm);

        ProductDetailChildFragment fragment = ProductDetailChildFragment
                .newInstance(data);
        fragment.setAdapterDelegate(this);
        fragment.setController(mController);
        return fragment;
    }

    @Override
    public int getCount() {
        return mListID.size();
    }

    @Override
    public String getCurrentID() {
        return mCurrentID;
    }

    public void setController(ProductDetailParentController controller) {
        mController = controller;
    }

    public void setListID(ArrayList<String> id) {
        mListID = id;
    }
}
