package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.RegisterCustomerBlock;
import com.simicart.core.customer.controller.RegisterCustomerController;

public class RegisterCustomerFragment extends SimiFragment {
    protected RegisterCustomerController mController;

    public static RegisterCustomerFragment newInstance() {
        RegisterCustomerFragment fragment = new RegisterCustomerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("core_register_customer");
        rootView = inflater.inflate(idView, null, false);
        Context context = getActivity();
        RegisterCustomerBlock block = new RegisterCustomerBlock(rootView, context);
        block.initView();
        if (null == mController) {
            mController = new RegisterCustomerController();
            mController.setDelegate(block);
            mController.onStart();
        } else {
            mController.setDelegate(block);
            mController.onResume();
        }
        return rootView;
    }
}
