package com.simicart.plugins.download.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;

public class DownloadingListProductFragmentTablet extends SimiFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(Rconfig.getInstance().layout("plugins_download_product"), container, false);


        return v;
    }


}
