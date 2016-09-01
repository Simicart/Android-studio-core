package com.simicart.core.base.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.GenderConfig;

import java.util.ArrayList;

/**
 * Created by frank on 01/09/2016.
 */
public class GenderAdapter extends BaseAdapter {

    protected ArrayList<GenderConfig> mGenders;
    int idView = Rconfig.getInstance().layout("core_item_gender_layout");
    protected LayoutInflater mInflater;


    public GenderAdapter(ArrayList<GenderConfig> genders) {
        mGenders = genders;
        Context context = SimiManager.getIntance().getCurrentActivity();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mGenders.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(idView, null);

        TextView tvGender = (TextView) convertView.findViewById(Rconfig.getInstance().id("tv_gendername"));
        String label = mGenders.get(position).getLabel();
        if (Utils.validateString(label)) {
            tvGender.setText(label);
        }


        return convertView;
    }
}
