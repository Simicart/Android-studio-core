package com.simicart.core.customer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.OrderHistoryDetailBlock;
import com.simicart.core.customer.controller.OrderHistoryDetailController;

public class OrderHistoryDetailFragment extends SimiFragment {

    protected String mID;
    protected int target;
    protected OrderHistoryDetailBlock mBlock;
    protected OrderHistoryDetailController mController;

    public static OrderHistoryDetailFragment newInstance(SimiData data) {
        OrderHistoryDetailFragment fragment = new OrderHistoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setScreenName("Order History Detail Screen");

        if (mData != null) {
            mID = (String) getValueWithKey(KeyData.ORDER_HISTORY_DETAIL.ORDER_ID);
            if (mHashMapData.containsKey(KeyData.ORDER_HISTORY_DETAIL.TARGET)) {
                target = (int) getValueWithKey(KeyData.ORDER_HISTORY_DETAIL.TARGET);
            }
        }

        if (target != 0) {
            setTargetFragment(this, target);
        }

        if (DataLocal.isLanguageRTL) {
            rootView = inflater
                    .inflate(
                            Rconfig.getInstance().layout(
                                    "rtl_fragment_order_history_detail"), container,
                            false);
        } else {
            rootView = inflater.inflate(
                    Rconfig.getInstance()
                            .layout("core_fragment_order_history_detail"), container,
                    false);
        }

        mBlock = new OrderHistoryDetailBlock(rootView, getActivity());
        mBlock.initView();
        if (null == mController) {
            mController = new OrderHistoryDetailController();
            mController.setID(mID);
            mController.setDelegate(mBlock);
            mController.onStart();
        } else {
            mController.setDelegate(mBlock);
            mController.onResume();
        }
        mBlock.onReorderClick(mController.getReOrderClicker());
        return rootView;
    }

}
