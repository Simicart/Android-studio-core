package com.simicart.core.customer.controller;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.customer.delegate.ListOfChoiceDelegate;

import java.util.ArrayList;
import java.util.Collections;

public class ListOfChoiceController extends SimiController {
    protected OnItemClickListener mSelectItemListener;
    protected ArrayList<String> mListData;
    protected ListOfChoiceDelegate mDelegate;

    public void setDelegate(ListOfChoiceDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    public void setData(ArrayList<String> data) {
        mListData = data;
    }


    @Override
    public void onStart() {
        mSelectItemListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectItem(position);
            }
        };
    }

    protected void selectItem(int position) {
        Collections.sort(mListData);
        String country = mListData.get(position).toString();
        mDelegate.chooseItem(country);
        SimiManager.getIntance().backPreviousFragment();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
    }


    public OnItemClickListener getSelectItemListener() {
        return mSelectItemListener;
    }
}
