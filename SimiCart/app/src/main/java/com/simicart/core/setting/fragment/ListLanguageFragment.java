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
import com.simicart.core.setting.controller.ListLanguageController;
import com.simicart.core.store.entity.Stores;

import java.util.ArrayList;

public class ListLanguageFragment extends SimiFragment {
    protected ListViewIndexableBlock mBlock;
    protected ListLanguageController mController;
    protected ArrayList<String> mList;
    protected String current_item;

    public static ListLanguageFragment newInstance(SimiData data) {
        ListLanguageFragment fragment = new ListLanguageFragment();
        Bundle bundle = new Bundle();
//		setData(Constants.KeyData.CURRENT_ITEM, currentItem, Constants.KeyData.TYPE_STRING, bundle);
        bundle.putParcelable(KEY_DATA, data);
        fragment.setArguments(bundle);
        fragment.setListLanguage(DataPreferences.listStores);
        return fragment;
    }

    public String getCurrent_item() {
        return current_item;
    }

    public void setListLanguage(ArrayList<Stores> listStores) {
        ArrayList<String> _list = new ArrayList<>();
        for (Stores stores : listStores) {
            _list.add(stores.getStoreName());
        }
        this.mList = _list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setScreenName("List Language Screen");
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
        mController = new ListLanguageController();
        mController.onStart();
        mController.setListLanguage(mList);
        mBlock.initView();
        mBlock.setOnItemClicker(mController.getClicker());

        return view;
    }
}
