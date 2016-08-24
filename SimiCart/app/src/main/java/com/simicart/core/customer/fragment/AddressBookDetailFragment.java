package com.simicart.core.customer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.AddressBookDetailBlock;
import com.simicart.core.customer.controller.AddressBookDetailController;

public class AddressBookDetailFragment extends SimiFragment {

    public static int OPEN_FOR_CUSTOMER = 100;
    public static int OPEN_FOR_CHECKOUT = 111;
    public static int ACTION_NEW = 0;
    public static int ACTION_EDIT = 1;
    public static int ACTION_GUEST = 2;
    public static int ACTION_NEW_CUSTOMER = 3;

    public static AddressBookDetailFragment newInstance() {
        AddressBookDetailFragment fragment = new AddressBookDetailFragment();
        return fragment;
    }

    protected AddressBookDetailController mController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int idView = Rconfig.getInstance().layout("core_fragment_address_book_detail");

        rootView = inflater.inflate(idView, null, false);

        AddressBookDetailBlock block = new AddressBookDetailBlock(rootView, getActivity());
        block.initView();

        if (null == mController) {
            mController = new AddressBookDetailController();
            mController.setData(mHashMapData);
            mController.setDelegate(block);
            mController.onStart();
        } else {
            mController.setDelegate(block);
            mController.onResume();
        }

        return rootView;
    }


}
