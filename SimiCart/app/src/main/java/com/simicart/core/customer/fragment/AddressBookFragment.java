package com.simicart.core.customer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.AddressBookBlock;
import com.simicart.core.customer.controller.AddressBookController;

public class AddressBookFragment extends SimiFragment {

    protected int openFor = -1;
    protected AddressBookController mController;

    public static AddressBookFragment newInstance(SimiData data) {
        AddressBookFragment fragment = new AddressBookFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DATA, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                Rconfig.getInstance().layout("core_fragment_address_book"),
                container, false);

        if (mData != null) {
            openFor = (int) getValueWithKey(KeyData.ADDRESS_BOOK.OPEN_FOR);
        }

        AddressBookBlock block = new AddressBookBlock(view, getActivity());
        block.setOpenFor(openFor);
        block.initView();

        if (null == mController) {
            mController = new AddressBookController();
            mController.setData(mHashMapData);
            mController.setDelegate(block);
            mController.onStart();
        } else {
            mController.setDelegate(block);
            mController.onResume();
        }
        block.setCreateNewListener(mController.getCreateNewListener());

        return view;

    }

}
