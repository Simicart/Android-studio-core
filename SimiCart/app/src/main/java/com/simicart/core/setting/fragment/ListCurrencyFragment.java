package com.simicart.core.setting.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.setting.block.ListViewIndexableBlock;
import com.simicart.core.setting.controller.ListCurrencyController;
import com.simicart.core.setting.entity.CurrencyEntity;

import java.util.ArrayList;

public class ListCurrencyFragment extends SimiFragment {
    protected ListViewIndexableBlock mBlock;
    protected ListCurrencyController mController;
    protected ArrayList<String> mList;
    protected String current_item;

    public static ListCurrencyFragment newInstance(SimiData data) {
        ListCurrencyFragment fragment = new ListCurrencyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DATA, data);
        fragment.setArguments(bundle);
        fragment.setListLanguage(DataPreferences.listCurrency);
        return fragment;
    }

    public String getCurrent_item() {
        return current_item;
    }

    public void setListLanguage(ArrayList<CurrencyEntity> listCurrency) {
        ArrayList<String> _list = new ArrayList<>();
        for (CurrencyEntity currency : listCurrency) {
            String title = currency.getTitle();
            _list.add(title);
        }
        this.mList = _list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setScreenName("List Currency Screen");
        View view = inflater.inflate(
                Rconfig.getInstance().layout("core_fragment_list_choice"), container,
                false);
        Context context = getActivity();
        if (mData != null) {
            current_item = (String) getValueWithKey(Constants.KeyData.CURRENT_ITEM);
        }

        mBlock = new ListViewIndexableBlock(view, context);
        mBlock.setList(mList);
        mBlock.setItemChecked(current_item);
        mController = new ListCurrencyController();
        mController.setDelegate(mBlock);
        mController.onStart();
        mController.setListCurrency(mList);
        mBlock.initView();
        mBlock.setOnItemClicker(mController.getClicker());

        return view;
    }
}
