package com.simicart.core.catalog.product.block;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.categorydetail.adapter.CategoryDetailAdapter;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

public class RelatedProductBlock extends SimiBlock {

    protected RecyclerView rv_relatedProduct;
    protected CategoryDetailAdapter mAdapter;

    public RelatedProductBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        rv_relatedProduct = (RecyclerView) mView.findViewById(Rconfig.getInstance()
                .id("rv_relatedProduct"));
        rv_relatedProduct.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void drawView(SimiCollection collection) {
        ArrayList<SimiEntity> entity = collection.getCollection();
        ArrayList<Product> products = new ArrayList<Product>();
        if (null != entity && entity.size() > 0) {
            for (SimiEntity simiEntity : entity) {
                Product product = (Product) simiEntity;
                products.add(product);
            }

            if (products.size() > 0) {
                showRelatedProduct(products);
            } else {
                visiableView();
                return;
            }
        } else {
            visiableView();
            return;
        }
    }

    public void showRelatedProduct(ArrayList<Product> products) {

        if (null == mAdapter) {
            mAdapter = new CategoryDetailAdapter(products);
            rv_relatedProduct.setAdapter(mAdapter);
        } else {
            mAdapter.setListProducts(products);
            mAdapter.notifyDataSetChanged();
        }

    }

    public void visiableView() {
        ((ViewGroup) mView).removeAllViewsInLayout();
        TextView tv_notify = new TextView(mContext);
        tv_notify.setText(SimiTranslator.getInstance().translate(
                "Related product is empty"));
        tv_notify.setTextColor(AppColorConfig.getInstance().getContentColor());
        tv_notify.setTypeface(null, Typeface.BOLD);
        if (DataLocal.isTablet) {
            tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        } else {
            tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        tv_notify.setGravity(Gravity.CENTER);
        tv_notify.setLayoutParams(params);
        ((ViewGroup) mView).addView(tv_notify);
    }

}
