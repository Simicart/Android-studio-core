package com.simicart.plugins.addressautofill;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;

import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.CountryEntity;
import com.simicart.core.customer.fragment.AddressBookDetailFragment;
import com.simicart.plugins.addressautofill.fragment.AddressAutoFillFragment;

import java.util.ArrayList;

public class AddressAutoFill {

    protected Context mContext;
    protected FragmentManager childFragmentManager;
    protected AddressAutoFillFragment fragment;
    protected ArrayList<SimiRowComponent> mListRowComponent;
    protected ArrayList<CountryEntity> mListCountry;
    protected NestedScrollView scrollView;

    public AddressAutoFill() {

        mContext = SimiManager.getIntance().getCurrentActivity();

        BroadcastReceiver blockReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                mListRowComponent = (ArrayList<SimiRowComponent>) data.getData().get(KeyData.ADDRESS_AUTO_FILL.LIST_COMPONENTS);
                mListCountry = (ArrayList<CountryEntity>) data.getData().get(KeyData.ADDRESS_AUTO_FILL.LIST_COUNTRIES);
                initChildFragmentManager();
                addMapToAddress();
            }
        };
        SimiEvent.registerEvent(KeyEvent.ADDRESS_AUTO_FILL.ADDRESS_AUTO_FILL_ADD_MAP, blockReceiver);

        BroadcastReceiver requestPermissionResultReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                fragment.requestGetCurrentLocation();
            }
        };
        SimiEvent.registerEvent(KeyEvent.ADDRESS_AUTO_FILL.ADDRESS_AUTO_FILL_ON_REQUEST_PERMISSION_RESULT, requestPermissionResultReceiver);

    }

    protected void addMapToAddress() {
        fragment = AddressAutoFillFragment.newInstance();
        fragment.setListRowComponent(mListRowComponent);
        fragment.setListCountry(mListCountry);
        childFragmentManager.beginTransaction().replace(Rconfig.getInstance().id("map_container"), fragment).commit();
    }

    protected void initChildFragmentManager() {
        if (DataLocal.isTablet) {
            Fragment prev = SimiManager.getIntance().getManager().findFragmentByTag("dialog");
            if (prev != null) {
                childFragmentManager = prev.getChildFragmentManager();
            }
        } else {
            for (Fragment fragment : SimiManager.getIntance()
                    .getManager().getFragments()) {
                if (fragment == null) {
                    break;
                } else {
                    if (fragment instanceof AddressBookDetailFragment) {
                        childFragmentManager = ((AddressBookDetailFragment) fragment).getChildFragmentManager();
                    }
                }
            }
        }
    }

}
