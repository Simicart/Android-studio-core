package com.simicart.plugins.paypalexpress.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.AddressBookDetailBlock;
import com.simicart.plugins.paypalexpress.controller.ExpressEditAddressController;

/**
 * Created by frank on 9/7/16.
 */
public class ExpressEditAddressFragment extends SimiFragment {

    public static int EDIT_BILLING_ADDRESS = 0;
    public static int EDIT_SHIPPING_ADDRESS = 1;
    protected ExpressEditAddressController mController;

    public static ExpressEditAddressFragment newInstance(SimiData data) {
        ExpressEditAddressFragment fragment = new ExpressEditAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int idView = Rconfig.getInstance().layout("core_fragment_address_book_detail");

        rootView = inflater.inflate(idView, null, false);

        AddressBookDetailBlock block = new AddressBookDetailBlock(rootView, getActivity());
        block.initView();

        if (null == mController) {
            mController = new ExpressEditAddressController();
            mController.setData(mHashMapData);
            mController.setDelegate(block);
            mController.onStart();

        } else {
            mController.setDelegate(block);
            mController.onResume();
        }

        block.setSaveListener(mController.getSaveListener());

        return rootView;
    }


}
