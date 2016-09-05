package com.simicart.core.customer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.AddressBookDetailBlock;
import com.simicart.core.customer.controller.AddressBookDetailController;

import java.util.HashMap;

public class AddressBookDetailFragment extends SimiFragment {


    public static AddressBookDetailFragment newInstance(SimiData data) {
        AddressBookDetailFragment fragment = new AddressBookDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
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

        block.setSaveListener(mController.getSaveListener());

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        SimiEvent.dispatchEvent(KeyEvent.ADDRESS_AUTO_FILL.ADDRESS_AUTO_FILL_ON_REQUEST_PERMISSION_RESULT, null);
    }
}
