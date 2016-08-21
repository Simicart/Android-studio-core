package com.simicart.theme.matrixtheme.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.theme.matrixtheme.home.block.HomeThemeOneBlock;
import com.simicart.theme.matrixtheme.home.controller.HomeThemeOneController;

public class HomeThemeOneFragment extends SimiFragment {


    public static HomeThemeOneFragment newInstance() {
        HomeThemeOneFragment fragment = new HomeThemeOneFragment();
        return fragment;
    }

    protected HomeThemeOneController mController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenName("Home Screen");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int idView = Rconfig.getInstance().layout("theme_one_fragment_home");
        rootView = inflater.inflate(idView, null, false);
        Context context = getActivity();
        HomeThemeOneBlock block = new HomeThemeOneBlock(rootView,context);
        block.initView();

        if(null == mController){
            mController = new HomeThemeOneController();
            mController.setDelegate(block);
            mController.onStart();
        }else{
            mController.setDelegate(block);
            mController.onResume();
        }


        return rootView;
    }
}
