package com.simicart.core.catalog.categorydetail.block;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.categorydetail.adapter.CategoryDetailAdapter;
import com.simicart.core.catalog.categorydetail.delegate.CategoryDetailDelegate;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by Martial on 8/19/2016.
 */
public class CategoryDetailBlock extends SimiBlock implements CategoryDetailDelegate {

    protected RelativeLayout rlBottom, rlChangeView, rlSort, rlFilter;
    protected TextView tvToTalItem;
    protected ImageView ivChangeView;
    protected ProgressBar pbLoadMore;
    protected RecyclerView rvListProducts;
    protected CategoryDetailAdapter mAdapter;
    protected ArrayList<Product> listProducts;
    protected String tagView;
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

        pbLoadMore = (ProgressBar) mView.findViewById(Rconfig.getInstance().id("pb_load_more"));
        pbLoadMore.setVisibility(View.GONE);

        if (DataLocal.isTablet) {
            tvToTalItem = (TextView) mView.findViewById(Rconfig.getInstance().id("tv_total_item"));
            numCollums = 4;
        } else {
            ivChangeView = (ImageView) mView.findViewById(Rconfig.getInstance().id("iv_change_view"));
            if (tagView == Constants.TAG_GRIDVIEW) {
                ivChangeView.setBackgroundResource(Rconfig.getInstance()
                        .drawable("ic_to_listview"));
            } else {
                ivChangeView.setBackgroundResource(Rconfig.getInstance()
                        .drawable("ic_to_gridview"));
            }
        }

    }

    @Override
    public void drawView(SimiCollection collection) {
        ArrayList<SimiEntity> entities = collection.getCollection();
        if (entities != null && entities.size() > 0) {
            ArrayList<Product> products = new ArrayList<>();
            for (SimiEntity simiEntity : entities) {
                Product product = new Product();
                product.parse(simiEntity.getJSONObject());
                products.add(product);
            }
            listProducts.clear();
            listProducts.addAll(products);
            if (listProducts.size() > 0) {
                rlBottom.setVisibility(View.VISIBLE);
                drawListProducts();
            }
        } else {
            showWarning();
        }
    }

    protected void drawListProducts() {
        if (mAdapter != null) {
            mAdapter.setListProducts(listProducts);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new CategoryDetailAdapter(listProducts);
            if (tagView.equals(Constants.TAG_GRIDVIEW)) {
                mAdapter.setNumCollums(numCollums);
                rvListProducts.setLayoutManager(new GridLayoutManager(mContext, numCollums));
            } else {
                rvListProducts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            }
            mAdapter.setTagView(tagView);
            rvListProducts.setAdapter(mAdapter);
        }
    }

    protected void showWarning() {
        RelativeLayout rltCate = (RelativeLayout) mView;

        rltCate.removeAllViewsInLayout();

        TextView tvWarming = new TextView(mContext);
        String warming = SimiTranslator.getInstance().translate("No result");
        tvWarming.setText(warming);
        tvWarming.setTextSize(18);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL | RelativeLayout.CENTER_VERTICAL);
        rltCate.addView(tvWarming, params);

    }

    @Override
    public void showLoadMore(boolean isShow) {
        if (isShow == true) {
            pbLoadMore.setVisibility(View.VISIBLE);
        } else {
            pbLoadMore.setVisibility(View.GONE);
        }
    }

    @Override
    public void changeView() {
        mAdapter = new CategoryDetailAdapter(listProducts);
        if (tagView == Constants.TAG_LISTVIEW) {
            ivChangeView.setBackgroundResource(Rconfig.getInstance()
                    .drawable("ic_to_gridview"));

            rvListProducts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        } else {
            ivChangeView.setBackgroundResource(Rconfig.getInstance()
                    .drawable("ic_to_listview"));
            rvListProducts.setLayoutManager(new GridLayoutManager(mContext, numCollums));

            mAdapter.setNumCollums(numCollums);
        }
        mAdapter.setTagView(tagView);
        rvListProducts.setAdapter(mAdapter);

        int lastPosition = 0;
        RecyclerView.LayoutManager manager = rvListProducts.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            lastPosition = ((LinearLayoutManager) rvListProducts.getLayoutManager()).findLastVisibleItemPosition();
        }
        if (manager instanceof GridLayoutManager) {
            lastPosition = ((GridLayoutManager) rvListProducts.getLayoutManager()).findLastVisibleItemPosition();
        }
        rvListProducts.scrollToPosition(lastPosition);

    }

    @Override
    public void showBottomMenu(boolean show) {
        if (show == true) {
            rlBottom.setVisibility(View.VISIBLE);
        } else {
            rlBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public String getTagView() {
        return tagView;
    }

    @Override
    public void setTagView(String tagView) {
        this.tagView = tagView;
    }

    @Override
    public void showTotalQuantity(String quantity) {
        if (checkQtyIsInteger(quantity)) {
            int qty = Integer.parseInt(quantity);
            if (qty > 1) {
                quantity = quantity + " " + SimiTranslator.getInstance().translate("Items");
            } else {
                quantity = quantity + " " + SimiTranslator.getInstance().translate("Item");
            }
            if (DataLocal.isTablet) {
                tvToTalItem.setText(quantity);
            } else {
                SimiNotify.getInstance().showToast(quantity);
            }
        }
    }

    private boolean checkQtyIsInteger(String qty) {
        if (!Utils.validateString(qty)) {
            return false;
        }
        try {
            int result = Integer.parseInt(qty);
            if (result <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void onChangeViewClick(View.OnClickListener listener) {
        rlChangeView.setOnClickListener(listener);
    }

    public void onListScroll(RecyclerView.OnScrollListener listener) {
        rvListProducts.setOnScrollListener(listener);
    }

    public void setSortListener(View.OnClickListener listener) {
        rlSort.setOnClickListener(listener);
    }

    public void setFilterListener(View.OnClickListener listener) {
        rlFilter.setOnClickListener(listener);
    }

}
