package com.simicart.core.catalog.product.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.product.block.ProductDetailChildBlock;
import com.simicart.core.catalog.product.controller.ProductDetailChildController;
import com.simicart.core.catalog.product.controller.ProductDetailParentController;
import com.simicart.core.catalog.product.delegate.ProductDetailAdapterDelegate;
import com.simicart.core.config.Rconfig;

public class ProductDetailChildFragment extends SimiFragment {
    protected ProductDetailChildBlock mBlock;
    protected ProductDetailChildController mController;
    protected String mID;
    protected ProductDetailAdapterDelegate mAdapterDelegate;
    protected ProductDetailParentController mParentController;

    public static ProductDetailChildFragment newInstance(SimiData data) {
        ProductDetailChildFragment fragment = new ProductDetailChildFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("core_fragment_product_child");
        View view = inflater.inflate(idView, null, false);
        SimiManager.getIntance().setChildFragment(getChildFragmentManager());
        if (getArguments() != null) {
            mID = (String) getValueWithKey("id");
            super.setScreenName("Product Detail " + mID);
        }

        mBlock = new ProductDetailChildBlock(view, getActivity(),
                getChildFragmentManager());

        mBlock.initView();
        mBlock.setDelegate(mParentController);

        if (null == mController) {
            mController = new ProductDetailChildController();
            mController.setAdapterDelegate(mAdapterDelegate);
            mController.setProductID(mID);
            mController.setDelegate(mBlock);
            mController.setParentController(mParentController);
            mController.onStart();
        } else {
            mController.setDelegate(mBlock);
            mController.onResume();
        }

        return view;
    }

    public void onUpdateTopBottom() {
        if (null != mController) {
            mController.onUpdateTopBottom();
        }
        if (null != mBlock) {
            mBlock.updateIndicator();
        }
    }


    public void setAdapterDelegate(ProductDetailAdapterDelegate delegate) {
        mAdapterDelegate = delegate;
    }

    public void setController(ProductDetailParentController controller) {
        mParentController = controller;
    }

}
