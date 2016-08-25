package com.simicart.core.customer.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.OrderHistory;
import com.simicart.core.customer.fragment.OrderHistoryDetailFragment;

import java.util.ArrayList;

/**
 * Created by Crabby PC on 7/4/2016.
 */
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryHolder> {

    protected ArrayList<OrderHistory> listOrders;
    protected Context mContext;

    public OrderHistoryAdapter(ArrayList<OrderHistory> listOrders) {
        this.listOrders = listOrders;
    }

    @Override
    public OrderHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(Rconfig.getInstance().layout("core_adapter_order_history"), null, false);
        OrderHistoryHolder holder = new OrderHistoryHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(OrderHistoryHolder holder, int position) {
        final OrderHistory orderHistoryEntity = listOrders.get(position);

        holder.tvStatusLabel.setText(SimiTranslator.getInstance().translate("Order Status"));
        holder.tvStatusLabel.setTextColor(AppColorConfig.getInstance().getContentColor());
        holder.tvStatus.setText(orderHistoryEntity.getOrder_status());
        holder.tvStatus.setTextColor(AppColorConfig.getInstance().getContentColor());

        holder.tvDateLabel.setText(SimiTranslator.getInstance().translate("Order Date"));
        holder.tvDateLabel.setTextColor(AppColorConfig.getInstance().getContentColor());
        holder.tvDate.setText(orderHistoryEntity.getOrder_date());
        holder.tvDate.setTextColor(AppColorConfig.getInstance().getContentColor());

        holder.tvRecipientLabel.setText(SimiTranslator.getInstance().translate("Recipient"));
        holder.tvRecipientLabel.setTextColor(AppColorConfig.getInstance().getContentColor());
        holder.tvRecipient.setText(orderHistoryEntity.getRecipient());
        holder.tvRecipient.setTextColor(AppColorConfig.getInstance().getContentColor());

        holder.tvItemsLabel.setText(SimiTranslator.getInstance().translate("Items"));
        holder.tvItemsLabel.setTextColor(AppColorConfig.getInstance().getContentColor());

        Drawable bullet = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("core_bullet"));
        bullet.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        holder.tvItem1.setCompoundDrawablesWithIntrinsicBounds(bullet, null, null, null);
        holder.tvItem2.setCompoundDrawablesWithIntrinsicBounds(bullet, null, null, null);
        holder.tvItem3.setCompoundDrawablesWithIntrinsicBounds(bullet, null, null, null);
        holder.tvItem1.setTextColor(AppColorConfig.getInstance().getContentColor());
        holder.tvItem2.setTextColor(AppColorConfig.getInstance().getContentColor());
        holder.tvItem3.setTextColor(AppColorConfig.getInstance().getContentColor());
        ArrayList<String> orderItems = orderHistoryEntity.getOrder_items();
        if(orderItems != null && orderItems.size() > 0) {
            if(orderItems.size() == 1) {
                holder.tvItem1.setText(orderItems.get(0));
                holder.tvItem2.setVisibility(View.GONE);
                holder.tvItem3.setVisibility(View.GONE);
            } else if(orderItems.size() == 2) {
                holder.tvItem1.setText(orderItems.get(0));
                holder.tvItem2.setText(orderItems.get(1));
                holder.tvItem3.setVisibility(View.GONE);
            } else if(orderItems.size() == 3) {
                holder.tvItem1.setText(orderItems.get(0));
                holder.tvItem2.setText(orderItems.get(1));
                holder.tvItem3.setText(orderItems.get(2));
            }
        } else {
            holder.llItems.setVisibility(View.GONE);
        }

        holder.rlItemOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SimiData data = new SimiData();
//                data.addData("order_history", orderHistoryEntity);
//                OrderHistoryDetailFragment fragment = OrderHistoryDetailFragment.newInstance();
//                SimiManager.getIntance().replaceFragment(fragment);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }

    public static class OrderHistoryHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlItemOrder;
        private TextView tvStatusLabel, tvDateLabel, tvRecipientLabel, tvItemsLabel;
        private TextView tvStatus, tvDate, tvRecipient, tvItem1, tvItem2, tvItem3;
        private LinearLayout llItems;

        public OrderHistoryHolder(View itemView) {
            super(itemView);
            rlItemOrder = (RelativeLayout) itemView.findViewById(Rconfig.getInstance().id("rl_item_order"));
            tvStatusLabel = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_status_label"));
            tvStatus = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_status"));
            tvDateLabel = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_date_label"));
            tvDate = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_date"));
            tvRecipientLabel = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_recipient_label"));
            tvRecipient = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_recipient"));
            tvItemsLabel = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_items_label"));
            tvItem1 = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_item1"));
            tvItem2 = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_item2"));
            tvItem3 = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_item3"));
            llItems = (LinearLayout) itemView.findViewById(Rconfig.getInstance().id("ll_items"));
        }
    }

    public void setListOrders(ArrayList<OrderHistory> listOrders) {
        this.listOrders = listOrders;
    }
}
