package com.simicart.core.customer.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.GenderConfig;

import java.util.ArrayList;

public class GenderAdapter extends BaseAdapter {
    ArrayList<GenderConfig> genderConfigs;
    LayoutInflater inflater;

    public GenderAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return genderConfigs.size() + 1;
    }

    @Override
    public String getItem(int position) {
        if (position == 0) {
            return "";
        } else {
            return SimiTranslator.getInstance().translate(
                    genderConfigs.get(position - 1).getLabel());
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(
                Rconfig.getInstance()
                        .getId("core_item_gender_layout", "layout"), null);
        TextView textView = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("tv_gendername"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        if (position == 0) {
            textView.setText("");
        } else {
            textView.setText(SimiTranslator.getInstance().translate(
                    genderConfigs.get(position - 1).getLabel()));
        }
        if (AppStoreConfig.getInstance().isRTL()) {
            textView.setGravity(Gravity.RIGHT);
        }
        return convertView;
    }

}
