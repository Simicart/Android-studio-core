package com.simicart.core.checkout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.entity.CreditCard;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by MSI on 27/08/2016.
 */
public class CardAdapter extends BaseAdapter {

    protected ArrayList<CreditCard> mListCard;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected int idConvertView;

    public CardAdapter(ArrayList<CreditCard> cards) {
        mListCard = cards;
        mContext = SimiManager.getIntance().getCurrentActivity();
        mInflater = LayoutInflater.from(mContext);
        idConvertView = Rconfig.getInstance().layout("core_adapter_card");
    }

    @Override
    public int getCount() {
        return mListCard.size();
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
        convertView = mInflater.inflate(idConvertView, null);
        ImageView imgCard = (ImageView) convertView.findViewById(Rconfig.getInstance().id("img_card"));
        TextView tvCard = (TextView) convertView.findViewById(Rconfig.getInstance().id("tv_card"));

        CreditCard creditCard = mListCard.get(position);

        String name = creditCard.getName();
        if (Utils.validateString(name)) {
            tvCard.setText(name);
        }


        String code = creditCard.getCode();
        if (Utils.validateString(code)) {
            code = code.trim().toUpperCase();
            int iconCard = getIcon(code);
            imgCard.setImageResource(iconCard);
        }


        return convertView;
    }

    public int getIcon(String code) {
        int back = 0;
        switch (code) {
            case "VI":
                back = Rconfig.getInstance().drawable("ic_card_visa");
                break;
            case "AE":
                back = Rconfig.getInstance().drawable("ic_card_american_express");
                break;
            case "MC":
                back = Rconfig.getInstance().drawable("ic_card_mastercard");
                break;
            case "JCB":
                back = Rconfig.getInstance().drawable("ic_card_jcb");
                break;
            case "DI":
                back = Rconfig.getInstance().drawable("ic_card_discover");
                break;
            default:
                break;
        }
        return back;
    }

}
