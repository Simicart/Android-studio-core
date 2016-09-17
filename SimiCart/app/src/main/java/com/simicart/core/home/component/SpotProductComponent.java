package com.simicart.core.home.component;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.entity.ProductList;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.home.adapter.SpotProductAdapter;

import java.util.ArrayList;

/**
 * Created by frank on 17/08/2016.
 */
public class SpotProductComponent extends SimiComponent {

    protected ProductList mProductList;
    protected TextView tvTitle;
    protected RecyclerView rcvCate;
    protected ArrayList<String> mListID;

    public SpotProductComponent(ProductList productList) {
        mProductList = productList;
    }

    @Override
    public View createView() {
        rootView = findLayout("core_component_cate_home");
        tvTitle = (TextView) findView("tv_title");
        tvTitle.setTextColor(AppColorConfig.getInstance().getContentColor());
        rcvCate = (RecyclerView) findView("rcv_cate");
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvCate.setLayoutManager(manager);
        showTitle();
        showCate();
        return rootView;
    }

    protected void showTitle() {
        String title = mProductList.getTitle();
        if (Utils.validateString(title)) {
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    protected void showCate() {
        ArrayList<Product> products = mProductList.getSpotProduct();
        SpotProductAdapter adapter = new SpotProductAdapter(products);
        adapter.setListID(mListID);
        rcvCate.setAdapter(adapter);
    }

    public void setListID(ArrayList<String> ids) {
        mListID = ids;
    }
}
