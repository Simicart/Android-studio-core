package com.simicart.plugins.instantcontact.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.instantcontact.block.ContactUsBlock;
import com.simicart.plugins.instantcontact.controller.ContactUsController;

public class ContactUsFragment extends SimiFragment {

    protected ContactUsBlock mBlock;
    protected ContactUsController mController;

    public static ContactUsFragment newInstance() {
        ContactUsFragment fragment = new ContactUsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                Rconfig.getInstance().layout("plugins_contactus_fragment"),
                container, false);

        mBlock = new ContactUsBlock(view, getActivity());
        mBlock.initView();

        if (null == mController) {
            mController = new ContactUsController();
            mController.setDelegate(mBlock);
            mController.onStart();
        } else {
            mController.setDelegate(mBlock);
            mController.onResume();
        }

        return view;
    }

}
