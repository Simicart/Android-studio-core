package com.simicart.core.catalog.listproducts.block;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.filter.FilterEvent;
import com.simicart.core.catalog.filter.common.FilterConstant;
import com.simicart.core.catalog.listproducts.adapter.ProductGridAdapter;
import com.simicart.core.catalog.listproducts.adapter.ProductListAdapter;
import com.simicart.core.catalog.listproducts.delegate.ProductListDelegate;
import com.simicart.core.catalog.listproducts.entity.TagSearch;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Simi on 12-Apr-16.
 */
public class ProductListBlock extends SimiBlock implements ProductListDelegate {
    //data
    protected String mCatID = "-1";
    protected String mCatName = "";
    protected String tagView = "";
    protected int numColumType1 = 2;

    //View
    protected RelativeLayout rlt_menu_bottom, rlt_change_view,
            rlt_filter, rlt_sort;
    protected View layout_toastQty;
    protected TextView tv_toastQty;
    protected RecyclerView rv_products;
    protected Animation zoomin;
    protected Animation zoomout;
    protected TextView txt_total_item;
    protected ProgressBar progressbar;
    protected ImageView img_change_view;
//    protected View v_footer;

    //more
    private boolean is_loadmore = false;
    protected ProductListAdapter mAdapterList;
    protected ProductGridAdapter mAdapterGrid;
    private ArrayList<Product> listProduct = new ArrayList<>();
    private ArrayList<String> listProductIds = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private FilterEvent mFilterEvent;

    public void setCatID(String mCatID) {
        this.mCatID = mCatID;
    }

    public void setCatName(String mCatName) {
        this.mCatName = mCatName;
    }

    public void setTagView(String tagView) {
        this.tagView = tagView;
    }

    public void setOnTourchChangeView(View.OnTouchListener touchListener) {
        if (!DataLocal.isTablet) {
            rlt_change_view.setOnTouchListener(touchListener);
        }
    }

    public void setOnTourchToFilter(View.OnTouchListener touchListener) {
        rlt_filter.setOnTouchListener(touchListener);
    }

    public void setOnTourchToSort(View.OnTouchListener touchListener) {
        rlt_sort.setOnTouchListener(touchListener);
    }

    public void setScrollListProduct(RecyclerView.OnScrollListener scroller) {
        rv_products.setOnScrollListener(scroller);
    }

    public ProductListBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        //menutop
        rlt_menu_bottom = (RelativeLayout) mView.findViewById(Rconfig
                .getInstance().id("menu_bottom_search"));
        rlt_menu_bottom.setVisibility(View.GONE);
        rlt_filter = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
                .id("ll_to_filter"));
        rlt_sort = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
                .id("ll_to_sort"));
        rlt_change_view = (RelativeLayout) mView.findViewById(Rconfig
                .getInstance().id("ll_change_view_data"));

        if (DataLocal.isTablet) {
            txt_total_item = (TextView) mView.findViewById(Rconfig
                    .getInstance().id("txt_totalitem"));
            numColumType1 = 4;
        } else {
            img_change_view = (ImageView) mView.findViewById(Rconfig
                    .getInstance().id("img_change_view"));
            img_change_view.setBackgroundResource(Rconfig.getInstance()
                    .drawable("ic_to_gridview"));
            numColumType1 = 2;
        }

        //recycle view product
        rv_products = (RecyclerView) mView.findViewById(Rconfig.getInstance().id(
                "rv_list_products"));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv_products.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_products.setLayoutManager(mLayoutManager);

        zoomin = AnimationUtils.loadAnimation(mContext, Rconfig.getInstance()
                .getId("zoomin", "anim"));
        zoomout = AnimationUtils.loadAnimation(mContext, Rconfig.getInstance()
                .getId("zoomout", "anim"));

        progressbar = (ProgressBar) mView.findViewById(Rconfig.getInstance()
                .id("progressBar_load"));
        progressbar.setVisibility(View.GONE);

        createToast();
    }

    protected void drawListView() {
        // specify an adapter (see also next example)
        if (mAdapterList != null) {
            mAdapterList.setListProducts(listProduct);
            mAdapterList.setListProductIds(listProductIds);
            mAdapterList.notifyDataSetChanged();
//            mAdapterList.notifyItemRangeInserted(listProduct.size() - moreCount, listProduct.size());
//            mAdapterList.notifyItemRangeChanged(listProduct.size() - moreCount, listProduct.size());
        } else {
            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(mContext);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_products.setLayoutManager(mLayoutManager);

            mAdapterList = new ProductListAdapter(mContext, listProduct, listProductIds);
            rv_products.setAdapter(mAdapterList);
        }

    }

    protected void drawGridView() {
        // specify an adapter (see also next example)
        if (mAdapterGrid != null) {
            mAdapterGrid.setListProducts(listProduct);
            mAdapterGrid.setListProductIds(listProductIds);
            mAdapterGrid.notifyDataSetChanged();
        } else {
            // use a grid layout manager
            mGridLayoutManager = new GridLayoutManager(mContext, numColumType1);
            rv_products.setLayoutManager(mGridLayoutManager);

            // specify an adapter (see also next example)
            mAdapterGrid = new ProductGridAdapter(mContext, listProduct, listProductIds, numColumType1);
            rv_products.setAdapter(mAdapterGrid);
        }
    }

    //chnageview from list to grid
    @Override
    public void changeDataView() {
        if (tagView.equals(TagSearch.TAG_GRIDVIEW)) {
            img_change_view.setBackgroundResource(
                    Rconfig.getInstance().drawable("ic_to_listview"));
            if (listProduct.size() > 0) {
                // use a grid layout manager
                mGridLayoutManager = new GridLayoutManager(mContext, numColumType1);
                mGridLayoutManager.scrollToPositionWithOffset(mLayoutManager.findFirstVisibleItemPosition(), 0);
                rv_products.setLayoutManager(mGridLayoutManager);

                // specify an adapter (see also next example)
                mAdapterGrid = new ProductGridAdapter(mContext, listProduct, listProductIds, numColumType1);
                rv_products.setAdapter(mAdapterGrid);
            }
        } else {
            img_change_view.setBackgroundResource(
                    Rconfig.getInstance().drawable("ic_to_gridview"));
            if (listProduct.size() > 0) {
                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(mContext);
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mLayoutManager.scrollToPositionWithOffset(mGridLayoutManager.findFirstVisibleItemPosition(), 0);
                rv_products.setLayoutManager(mLayoutManager);

                mAdapterList = new ProductListAdapter(mContext, listProduct, listProductIds);
                rv_products.setAdapter(mAdapterList);
            }
        }
    }

    void createToast() {
        LayoutInflater inflater = SimiManager.getIntance().getCurrentActivity()
                .getLayoutInflater();
        layout_toastQty = inflater
                .inflate(
                        Rconfig.getInstance().layout(
                                "core_custom_toast_productlist"),
                        (ViewGroup) SimiManager
                                .getIntance()
                                .getCurrentActivity()
                                .findViewById(
                                        Rconfig.getInstance().id(
                                                "custom_toast_layout")));
        tv_toastQty = (TextView) layout_toastQty.findViewById(Rconfig.getInstance()
                .id("txt_custom_toast"));
    }

    @Override
    public void setQty(String qty) {
        if (checkQtyIsInteger(qty)) {
            int i_qty = Integer.parseInt(qty);
            if (DataLocal.isTablet) {
                txt_total_item.setText("");
                if (i_qty > 1) {
                    qty = qty + " " + Config.getInstance().getText("Items");
                } else {
                    qty = qty + " " + Config.getInstance().getText("Item");
                }
                txt_total_item.setText(qty);
            } else {
                if (is_loadmore == false && mView.getContext() != null) {
                    Toast toast = new Toast(mView.getContext());
                    if (i_qty > 1) {
                        qty = qty + " " + Config.getInstance().getText("Items");
                    } else {
                        qty = qty + " " + Config.getInstance().getText("item");
                    }
                    tv_toastQty.setText(qty);
                    toast.setView(layout_toastQty);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                            0, Utils.getValueDp(100));
                    toast.show();
                }
            }
        } else {
            rv_products.setVisibility(View.GONE);
            rlt_menu_bottom.setVisibility(View.GONE);
            visiableView();
            return;
        }
    }

    public void visiableView() {
        ((ViewGroup) mView).removeAllViewsInLayout();
        TextView tv_notify = new TextView(mContext);
        tv_notify.setText(Config.getInstance().getText(
                "Result products is empty"));
        tv_notify.setTypeface(null, Typeface.BOLD);
        tv_notify.setTextColor(Config.getInstance().getContent_color());
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

    int moreCount;

    public void setFilterEvent(FilterEvent mFilterEvent) {
        this.mFilterEvent = mFilterEvent;
    }

    @Override
    public void drawView(SimiCollection collection) {
        ArrayList<SimiEntity> entityProducts = collection.getCollection();
        ArrayList<Product> products = new ArrayList<>();
        if (null != entityProducts && entityProducts.size() > 0) {
            for (SimiEntity simiEntity : entityProducts) {
                Product product = (Product) simiEntity;
                products.add(product);
            }
            moreCount = products.size() - listProduct.size();
            listProduct.clear();
            listProduct.addAll(products);
        }

        if (null != mFilterEvent) {
            JSONObject json = collection.getJSON();
            if (null != json && json.has(FilterConstant.LAYEREDNAVIGATION)) {
                mFilterEvent.setJSON(json);
            }
            Button btn_filter = (Button) mFilterEvent.initView(mContext, mCatName);
            addFilterButton(btn_filter);
        }
        if (listProduct.size() > 0) {
            rlt_menu_bottom.setVisibility(View.VISIBLE);
            if (tagView.equals(TagSearch.TAG_LISTVIEW)) {
                drawListView();
            } else {
                drawGridView();
            }
        }
    }

    @Override
    public void setListProductIds(ArrayList<String> listProductIds) {
        this.listProductIds = listProductIds;
    }

    protected void addFilterButton(Button btn_filter) {
        if (null != btn_filter) {
            // add filter button right of sort button
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    Utils.getValueDp(25), Utils.getValueDp(25));
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            btn_filter.setLayoutParams(params);
            btn_filter.setBackgroundResource(Rconfig.getInstance().drawable(
                    "ic_filter"));
            rlt_filter.addView(btn_filter);
        }

    }

    @Override
    public void onZoomIn() {
        if (tagView == TagSearch.TAG_GRIDVIEW && numColumType1 == 4) {
            numColumType1 = 2;
            if (DataLocal.isTablet) {
                numColumType1 = 6;
            }
            // use a grid layout manager
            GridLayoutManager mGridLayoutManager = new GridLayoutManager(mContext, numColumType1);
            rv_products.setLayoutManager(mGridLayoutManager);
            rv_products.startAnimation(zoomin);

            // specify an adapter (see also next example)
            mAdapterGrid = new ProductGridAdapter(mContext, listProduct, listProductIds, numColumType1);
            mAdapterGrid.notifyDataSetChanged();
            rv_products.setAdapter(mAdapterGrid);
        }
    }

    @Override
    public void onZoomOut() {
        if (tagView == TagSearch.TAG_GRIDVIEW && numColumType1 == 2) {
            numColumType1 = 4;
            // use a grid layout manager
            GridLayoutManager mGridLayoutManager = new GridLayoutManager(mContext, numColumType1);
            rv_products.setLayoutManager(mGridLayoutManager);
            rv_products.startAnimation(zoomout);

            // specify an adapter (see also next example)
            mAdapterGrid = new ProductGridAdapter(mContext, listProduct, listProductIds, numColumType1);
            mAdapterGrid.notifyDataSetChanged();
            rv_products.setAdapter(mAdapterGrid);
        }
    }

    @Override
    public void removeFooterView() {
        progressbar.setVisibility(View.GONE);
        if (mAdapterList != null) {
            mAdapterList.removeFooterView();
        }
        if (mAdapterGrid != null) {
            mAdapterGrid.removeFooterView();
        }
    }

    @Override
    public void addFooterView() {
//        if (tagView == TagSearch.TAG_GRIDVIEW) {
//            progressbar.setVisibility(View.VISIBLE);
//        }
        if (mAdapterList != null) {
            mAdapterList.addFooterView();
        }
        if (mAdapterGrid != null) {
            mAdapterGrid.addFooterView();
        }
    }

    @Override
    public void setVisibilityMenuBotton(boolean visible) {
        if (null != rlt_menu_bottom && !DataLocal.isTablet) {
            if (visible) {
                rlt_menu_bottom.setVisibility(View.VISIBLE);
            } else {
                rlt_menu_bottom.setVisibility(View.GONE);
            }
        }
    }

    public void setOnTouchListenerGridview(RecyclerView.OnTouchListener onTouchListener) {
        rv_products.setOnTouchListener(onTouchListener);
    }
}
