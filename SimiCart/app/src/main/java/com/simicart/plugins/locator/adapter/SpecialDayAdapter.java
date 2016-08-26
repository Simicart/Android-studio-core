package com.simicart.plugins.locator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.entity.SpecialObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Martial on 8/26/2016.
 */
public class SpecialDayAdapter extends RecyclerView.Adapter<SpecialDayAdapter.SpecialDayHolder> {

    protected ArrayList<SpecialObject> listSpecialObjects;
    protected boolean isHoliday = false;
    protected Context mContext;

    public SpecialDayAdapter(ArrayList<SpecialObject> listSpecialObjects, boolean isHoliday) {
        this.listSpecialObjects = listSpecialObjects;
        this.isHoliday = isHoliday;
    }

    @Override
    public SpecialDayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(Rconfig.getInstance().layout("plugins_store_locator_item_list_special"), null, false);
        SpecialDayHolder holder = new SpecialDayHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(SpecialDayHolder holder, int position) {
        SpecialObject specialObject = listSpecialObjects.get(position);

        Calendar cal = Calendar.getInstance();
        String[] date = specialObject.getDate().split("-");
        cal.set(Integer.parseInt(date[0]), (Integer.parseInt(date[1]) - 1), Integer.parseInt(date[2]));
        SimpleDateFormat mFormat = new SimpleDateFormat("EEE MMM dd");
        if(isHoliday == false) {
            holder.txt.setText(mFormat.format(cal.getTime()) + "  " + specialObject.getTime_open() + " - " + specialObject.getTime_close());
        } else {
            holder.txt.setText(mFormat.format(cal.getTime()) + "  " + SimiTranslator.getInstance().translate("Close"));
        }
        holder.txt.setTextSize(15);

    }

    @Override
    public int getItemCount() {
        return listSpecialObjects.size();
    }

    public static class SpecialDayHolder extends RecyclerView.ViewHolder {
        private TextView txt;

        public SpecialDayHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(Rconfig.getInstance().getIdLayout("txt_special"));
        }
    }
}
