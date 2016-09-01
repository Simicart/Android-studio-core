package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.CustomerBlock;
import com.simicart.core.customer.controller.CustomerController;

/**
 * Created by frank on 01/09/2016.
 */
public class CustomerFragment extends SimiFragment {

    public static CustomerFragment newInstance(SimiData data) {
        CustomerFragment fragment = new CustomerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }


    protected CustomerController mController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("core_fragment_customer");
        rootView = inflater.inflate(idView, null, false);
        Context context = getActivity();

        int mOpenFor = -1;
        if (mHashMapData.containsKey(KeyData.CUSTOMER_PAGE.OPEN_FOR)) {
            mOpenFor = (int) mHashMapData.get(KeyData.CUSTOMER_PAGE.OPEN_FOR);
        }

        CustomerBlock block = new CustomerBlock(rootView, context);
        block.setOpenFor(mOpenFor);
        block.initView();

        if (null == mController) {
            mController = new CustomerController();
            mController.setData(mHashMapData);
            mController.setDelegate(block);
            mController.onStart();

        } else {
            mController.setDelegate(block);
            mController.onResume();
        }

        block.setRegisterListener(mController.getRegisterListener());

        return rootView;
    }

}
