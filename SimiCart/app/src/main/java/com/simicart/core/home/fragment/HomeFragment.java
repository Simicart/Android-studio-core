package com.simicart.core.home.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.banner.block.BannerBlock;
import com.simicart.core.banner.controller.BannerController;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.block.CategoryHomeBlock;
import com.simicart.core.home.block.HomeBlock;
import com.simicart.core.home.block.ProductListHomeBlock;
import com.simicart.core.home.block.SearchHomeBlock;
import com.simicart.core.home.controller.CategoryHomeController;
import com.simicart.core.home.controller.HomeController;
import com.simicart.core.home.controller.ProductListHomeController;

public class HomeFragment extends SimiFragment {

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    protected HomeController mController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setScreenName("Home Screen");
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("core_fragment_home");
        rootView = inflater.inflate(idView, null, false);
        Context context = getActivity();
        HomeBlock block = new HomeBlock(rootView, context);
        block.initView();

        if (null == mController) {
            mController = new HomeController();
            mController.setDelegate(block);
            mController.onStart();
        } else {
            mController.setDelegate(block);
            mController.onResume();
        }


        return rootView;
    }
}
