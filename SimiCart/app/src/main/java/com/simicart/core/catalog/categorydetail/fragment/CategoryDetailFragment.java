package com.simicart.core.catalog.categorydetail.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;

/**
 * Created by Martial on 8/19/2016.
 */
public class CategoryDetailFragment extends SimiFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(Rconfig.getInstance().layout("core_product_list_layout"), container, false);
        return rootView;
    }
}
