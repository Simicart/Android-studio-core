package com.simicart.theme.matrixtheme.home.component;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.theme.matrixtheme.home.adapter.ThemeOneSpotProductAdapter;
import com.simicart.theme.matrixtheme.home.entity.OrderProduct;

import java.util.ArrayList;

/**
 * Created by frank on 8/20/16.
 */
public class SpotProductHomeComponent extends SimiComponent {

    protected RecyclerView rcvSpotProduct;
    protected ArrayList<OrderProduct> mListProduct;

    public SpotProductHomeComponent(ArrayList<OrderProduct> products) {
        super();
        mListProduct = products;
    }


    @Override
    public View createView() {
        rootView = findLayout("theme_one_component_spot_product");
        rcvSpotProduct = (RecyclerView) findView("rcv_spot_product");
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rcvSpotProduct.setLayoutManager(manager);
        ThemeOneSpotProductAdapter adapter = new ThemeOneSpotProductAdapter(mListProduct);
        rcvSpotProduct.setAdapter(adapter);
        return rootView;
    }
}
