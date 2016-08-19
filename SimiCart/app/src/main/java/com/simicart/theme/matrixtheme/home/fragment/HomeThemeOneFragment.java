package com.simicart.theme.matrixtheme.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;

public class HomeThemeOneFragment extends SimiFragment {


    public static HomeThemeOneFragment newInstance() {
        HomeThemeOneFragment fragment = new HomeThemeOneFragment();
        return fragment;
    }

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


        return rootView;
    }
}
