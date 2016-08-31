package com.simicart.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.entity.Sort;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

public class SortAdapter extends BaseAdapter {
    protected ArrayList<Sort> mListSort;
    protected Context mContext;
    protected LayoutInflater inflater;
    protected int mCurrentValue = 0;

    public SortAdapter(ArrayList<Sort> listSort) {
        this.mContext = SimiManager.getIntance().getCurrentActivity();
        this.inflater = LayoutInflater.from(mContext);
        this.mListSort = listSort;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListSort.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        convertView = this.inflater.inflate(
                Rconfig.getInstance().layout("core_item_sort"), null);
        TextView title = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("sort_text"));
        Sort sort = mListSort.get(position);

        title.setText(sort.getTitle());
        int id = sort.getId();
        if (id != mCurrentValue) {
            CheckedTextView check = (CheckedTextView) convertView
                    .findViewById(Rconfig.getInstance().id("sort_check"));
            check.setBackgroundColor(0);
        }
        return convertView;
    }

    public void setCurrentValue(int currentValue) {
        mCurrentValue = currentValue;
    }

    public void setListSort(ArrayList<Sort> listSort) {
        this.mListSort = listSort;
    }
}
