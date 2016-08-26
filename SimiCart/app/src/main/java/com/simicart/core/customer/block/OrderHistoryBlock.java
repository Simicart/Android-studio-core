package com.simicart.core.customer.block;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.adapter.OrderHistoryAdapter;
import com.simicart.core.customer.delegate.OrderHistoryDelegate;
import com.simicart.core.customer.entity.OrderHistory;

import java.util.ArrayList;

public class OrderHistoryBlock extends SimiBlock implements
        OrderHistoryDelegate {

    protected RecyclerView rv_order_history;
    protected OrderHistoryAdapter mAdapter;
    protected ProgressBar pbLoadMore;
    protected ArrayList<OrderHistory> orderHistoryList;

    public OrderHistoryBlock(View view, Context context) {
        super(view, context);
    }

    public void setScrollListener(RecyclerView.OnScrollListener listener) {
        rv_order_history.setOnScrollListener(listener);
    }

    @Override
    public void initView() {
        orderHistoryList = new ArrayList<>();

        rv_order_history = (RecyclerView) mView.findViewById(Rconfig
                .getInstance().id("rv_list_order"));
        rv_order_history.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        pbLoadMore = (ProgressBar) mView.findViewById(Rconfig.getInstance().id("pb_load_more"));
        pbLoadMore.setVisibility(View.GONE);
    }

    @Override
    public void drawView(SimiCollection collection) {
        ArrayList<SimiEntity> entity = collection.getCollection();
        ArrayList<OrderHistory> orderHis = new ArrayList<OrderHistory>();
        if (null != entity && entity.size() > 0) {
            for (SimiEntity simiEntity : entity) {
                OrderHistory order = (OrderHistory) simiEntity;
                orderHis.add(order);
            }
            orderHistoryList.clear();
            orderHistoryList.addAll(orderHis);

            if (null == mAdapter) {
                mAdapter = new OrderHistoryAdapter(orderHis);
                rv_order_history.setAdapter(mAdapter);
            } else {
                mAdapter.setListOrders(orderHis);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void isShowLoadMore(boolean isShow) {
        if (isShow == true) {
            pbLoadMore.setVisibility(View.VISIBLE);
        } else {
            pbLoadMore.setVisibility(View.GONE);
        }
    }
}
