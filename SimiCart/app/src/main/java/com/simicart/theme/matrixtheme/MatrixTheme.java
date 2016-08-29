package com.simicart.theme.matrixtheme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.simicart.core.base.event.fragment.SimiEventFragmentEntity;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.Constants;
import com.simicart.theme.matrixtheme.home.fragment.HomeThemeOneFragment;

public class MatrixTheme {


    public MatrixTheme() {
        Context context = SimiManager.getIntance().getCurrentActivity();
        IntentFilter intentFilter = new IntentFilter("com.simicart.core.home.fragment.HomeFragment");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                SimiEventFragmentEntity entity = (SimiEventFragmentEntity) data.getData().get(KeyData.SIMI_FRAGMENT.FRAGMENT);
                HomeThemeOneFragment fragment = HomeThemeOneFragment.newInstance();
                entity.setmFragment(fragment);
            }
        };

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);
    }
}
