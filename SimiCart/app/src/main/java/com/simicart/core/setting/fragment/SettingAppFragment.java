package com.simicart.core.setting.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.core.setting.block.SettingAppBlock;
import com.simicart.core.setting.controller.SettingAppController;

public class SettingAppFragment extends SimiFragment {

    protected SettingAppBlock settingAppBlock;
    protected SettingAppController settingAppController;
    protected Context mContext;

    View rootView = null;

    public static SettingAppFragment newInstance() {
        SettingAppFragment fragment = new SettingAppFragment();
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setScreenName("Setting Screen");
        rootView = inflater.inflate(
                Rconfig.getInstance().layout("core_setting_layout"), container,
                false);

        mContext = getActivity();
        settingAppBlock = new SettingAppBlock(rootView, mContext);
        settingAppBlock.initView();
        if (settingAppController == null) {
            settingAppController = new SettingAppController();
            settingAppController.setDelegate(settingAppBlock);
            settingAppController.onStart();
        } else {
            settingAppController.setDelegate(settingAppBlock);
            settingAppController.onResume();
        }

        return rootView;
    }
}
