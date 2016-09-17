package com.simicart.core.checkout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by frank on 27/08/2016.
 */
public class DateAdapter extends BaseAdapter {

    protected ArrayList<String> mData;
    protected Context mContext;

    public DateAdapter(ArrayList<String> mData) {
        this.mData = mData;
        mContext = SimiManager.getIntance().getCurrentActivity();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int idView = Rconfig.getInstance().layout("core_adapter_date");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(idView, null);

        TextView tvContent = (TextView) convertView.findViewById(Rconfig.getInstance().id("tv_content"));
        String content = mData.get(position);
        if (Utils.validateString(content)) {
            tvContent.setText(content);
        }
        return convertView;
    }
}
