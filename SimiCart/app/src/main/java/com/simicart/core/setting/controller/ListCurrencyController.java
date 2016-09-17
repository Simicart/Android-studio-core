package com.simicart.core.setting.controller;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.customer.entity.AddressEntity;
import com.simicart.core.setting.entity.CurrencyEntity;

import java.util.ArrayList;
import java.util.Collections;

public class ListCurrencyController extends SimiController {
    protected OnItemClickListener mClicker;
    protected ArrayList<String> list_currency;
    protected AddressEntity addressBookDetail;
    protected SimiDelegate mDelegate;

    public void setDelegate(SimiDelegate delegate) {
        mDelegate = delegate;
    }

    public OnItemClickListener getClicker() {
        return mClicker;
    }

    @Override
    public void onStart() {
        mClicker = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectItem(position);
            }
        };
    }

    protected void selectItem(int position) {
        Collections.sort(list_currency);
        String currency = list_currency.get(position).toString();
        String id = DataPreferences.getCurrencyID();
        for (CurrencyEntity entity : DataPreferences.listCurrency) {
            if (currency.equals(entity.getTitle())) {

                String currency_id = entity.getValue();

                if (!id.equals(currency_id)) {

                    DataPreferences.saveCurrencyID(currency_id);
                    SimiManager.getIntance().changeStoreView();
                    // saveCurrency(currency_id);
                }
            }
        }
        SimiManager.getIntance().backPreviousFragment();
    }

    // private void saveCurrency(String id) {
    // mDelegate.showLoading();
    //
    // SaveCurrencyModel model = new SaveCurrencyModel();
    // model.setDelegate(new ModelDelegate() {
    //
    // @Override
    // public void callBack(String message, boolean isSuccess) {
    // mDelegate.dismissLoading();
    // if (isSuccess) {
    // SimiManager.getIntance().changeStoreView();
    // }
    //
    // }
    // });
    //
    // model.addParam("currency", id);
    //
    // model.request();
    //
    // }

    @Override
    public void onResume() {
    }

    public void setListCurrency(ArrayList<String> list_currency) {
        this.list_currency = list_currency;
    }
}
