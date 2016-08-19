package com.simicart.core.catalog.categorydetail.block;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.categorydetail.adapter.CategoryDetailAdapter;
import com.simicart.core.catalog.listproducts.adapter.ProductListAdapter;
import com.simicart.core.catalog.listproducts.entity.TagSearch;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by Martial on 8/19/2016.
 */
public class CategoryDetailBlock extends SimiBlock {

    protected RelativeLayout rlBottom, rlChangeView, rlSort, rlFilter;
    protected ImageView ivChangeView;
    protected ProgressBar pbLoadMore;
    protected RecyclerView rvListProducts;
    protected CategoryDetailAdapter mAdapter;
    protected ArrayList<Product> listProducts;
    protected String tagView = "";
    protected int numCollums = 2;

    public CategoryDetailBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        rvListProducts = (RecyclerView) mView.findViewById(Rconfig.getInstance().id("rv_list_products"));
        listProducts = new ArrayList<>();

        rlBottom = (RelativeLayout) mView.findViewById(Rconfig.getInstance().id("rl_menu_bottom"));
        rlChangeView = (RelativeLayout) mView.findViewById(Rconfig.getInstance().id("rl_change_view_data"));
        rlSort = (RelativeLayout) mView.findViewById(Rconfig.getInstance().id("rl_to_sort"));
        rlFilter = (RelativeLayout) mView.findViewById(Rconfig.getInstance().id("rl_to_filter"));
        rlBottom.setVisibility(View.VISIBLE);

        ivChangeView = (ImageView) mView.findViewById(Rconfig.getInstance().id("iv_change_view"));
        if(DataLocal.isTablet) {
            ivChangeView.setVisibility(View.INVISIBLE);
        }

        pbLoadMore = (ProgressBar) mView.findViewById(Rconfig.getInstance().id("pb_load_more"));
        pbLoadMore.setVisibility(View.GONE);

        if (DataLocal.isTablet) {
            numCollums = 4;
        }

    }

    @Override
    public void drawView(SimiCollection collection) {
        ArrayList<SimiEntity> entities = collection.getCollection();
        if(entities != null) {
            ArrayList<Product> products = new ArrayList<>();
            for(int i=0;i<entities.size();i++) {
                Product product = (Product) entities.get(i);
                products.add(product);
            }
            listProducts.clear();
            listProducts.addAll(products);

            if (listProducts.size() > 0) {
                rlBottom.setVisibility(View.VISIBLE);
                drawListProducts();
            }
        }
    }

    protected void drawListProducts() {
        if (mAdapter != null) {
            mAdapter.setListProducts(listProducts);
            mAdapter.notifyDataSetChanged();
        } else {
            if(tagView.equals(TagSearch.TAG_LISTVIEW)) {
                rvListProducts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            } else {
                rvListProducts.setLayoutManager(new GridLayoutManager(mContext, numCollums));
            }
            mAdapter = new CategoryDetailAdapter(listProducts);
            mAdapter.setTagView(tagView);
            rvListProducts.setAdapter(mAdapter);
        }
    }

}
