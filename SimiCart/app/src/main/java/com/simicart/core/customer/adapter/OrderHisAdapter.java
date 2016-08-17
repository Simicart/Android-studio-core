package com.simicart.core.customer.adapter;

import java.util.ArrayList;

import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.OrderHistory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderHisAdapter extends BaseAdapter {

    protected ArrayList<OrderHistory> mOrderHis;
    protected Context context;
    protected LayoutInflater inflater;

    public OrderHisAdapter(Context context,
                           ArrayList<OrderHistory> orderHistories) {
        this.context = context;
        this.mOrderHis = orderHistories;
        this.inflater = LayoutInflater.from(context);
        orderHistories = new ArrayList<>();
    }

    public ArrayList<OrderHistory> getOrderHisList() {
        return this.mOrderHis;
    }

    public void setOrderHis(ArrayList<OrderHistory> orderhis) {
        mOrderHis = orderhis;
    }

    @SuppressLint({"DefaultLocale", "ViewHolder"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        convertView = this.inflater.inflate(
                Rconfig.getInstance().layout("core_listitem_order_layout"),
                null);
        if (DataLocal.isLanguageRTL) {
            convertView = this.inflater.inflate(
                    Rconfig.getInstance().layout("rtl_listitem_order_layout"),
                    null);
        }

        // order status
        createValue("lb_status", convertView, "Order Status");

        // order date
        createValue("lb_date", convertView, "Order Date");

        // recipient
        createValue("lb_recipient", convertView, "Recipient");


        // items

        createValue("lb_items", convertView, "Items");


        OrderHistory orderHistory = mOrderHis.get(position);

        // order status
        String status = orderHistory.getOrder_status().toUpperCase();
        createValue("tv_status", convertView, status);


        // order date
        String orderDate = orderHistory.getOrder_date();
        createValue("tv_date", convertView, orderDate);


        // recipent
        String recipient = orderHistory.getRecipient();
        createValue("tv_recipient", convertView, recipient);


        // item
        TextView item1 = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("item1"));
        TextView item2 = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("item2"));
        TextView item3 = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("item3"));
        Drawable bullet = context.getResources().getDrawable(
                Rconfig.getInstance().drawable("core_bullet"));
        bullet.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        item1.setCompoundDrawablesWithIntrinsicBounds(bullet, null, null, null);
        item2.setCompoundDrawablesWithIntrinsicBounds(bullet, null, null, null);
        item3.setCompoundDrawablesWithIntrinsicBounds(bullet, null, null, null);
        item1.setTextColor(AppColorConfig.getInstance().getContentColor());
        item2.setTextColor(AppColorConfig.getInstance().getContentColor());
        item3.setTextColor(AppColorConfig.getInstance().getContentColor());
        if (mOrderHis.get(position).getOrder_items().size() < 1) {
            item1.setVisibility(View.GONE);
            item2.setVisibility(View.GONE);
            item3.setVisibility(View.GONE);
        } else if (mOrderHis.get(position).getOrder_items().size() == 1) {
            item1.setText(mOrderHis.get(position).getOrder_items().get(0));
            item2.setVisibility(View.GONE);
            item3.setVisibility(View.GONE);
        } else if (mOrderHis.get(position).getOrder_items().size() == 2) {
            item1.setText(mOrderHis.get(position).getOrder_items().get(0));
            item2.setText(mOrderHis.get(position).getOrder_items().get(1));
            item3.setVisibility(View.GONE);
        } else {
            item1.setText(mOrderHis.get(position).getOrder_items().get(0));
            item2.setText(mOrderHis.get(position).getOrder_items().get(1));
            item3.setText(".....");
        }

        ImageView im_extend = (ImageView) convertView.findViewById(Rconfig
                .getInstance().id("im_extend"));
        Drawable icon = context.getResources().getDrawable(
                Rconfig.getInstance().drawable("ic_extend"));
        icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        im_extend.setImageDrawable(icon);

        return convertView;
    }

    protected void createValue(String idView, View convertView, String label) {
        int id = Rconfig.getInstance().id(idView);
        TextView tvLabel = (TextView) convertView.findViewById(id);
        tvLabel.setTextColor(AppColorConfig.getInstance().getContentColor());
        String labelTranslated = SimiTranslator.getInstance().translate(label);
        tvLabel.setText(labelTranslated);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.mOrderHis.size();
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

}
