package com.simicart.core.checkout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.checkout.block.ReviewOrderBlock;
import com.simicart.core.checkout.controller.ReviewOrderController;
import com.simicart.core.config.Rconfig;

public class ReviewOrderFragment extends SimiFragment {


    protected ReviewOrderController mController;

    public static ReviewOrderFragment newInstance(SimiData data) {
        ReviewOrderFragment fragment = new ReviewOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setScreenName("Review Order Screen");
        SimiManager.getIntance().showCartLayout(false);
        int idView = Rconfig.getInstance().layout("core_fragment_review_order");
        rootView = inflater.inflate(idView, container, false);
        Context context = getActivity();
        ReviewOrderBlock block = new ReviewOrderBlock(rootView, context);
        block.initView();

        if (null == mController) {
            mController = new ReviewOrderController();
            mController.setData(mHashMapData);
            mController.setDelegate(block);
            mController.onStart();
        } else {
            mController.setDelegate(block);
            mController.onResume();
        }

        block.setPlaceOrderListener(mController.getPlaceOrderListener());

        return rootView;
    }


}
