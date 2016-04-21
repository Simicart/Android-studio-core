package com.simicart.core.catalog.listproducts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.simicart.R;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.common.price.ProductPriceViewProductGridV03;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.EventBlock;

import java.util.ArrayList;

/**
 * Created by Simi on 12-Apr-16.
 */
public class ProductGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Product> listProducts;
    private ArrayList<String> listProductIds;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private int footerCount = 0;
    private int headercount;
    private float withScreen;

    public ProductGridAdapter(Context context, ArrayList<Product> list_products, ArrayList<String> list_product_IDs, int numcolum) {
        listProducts = list_products;
        mContext = context;
        headercount = numcolum;
        listProductIds = list_product_IDs;
        getDimension();
    }

    public void setListProductIds(ArrayList<String> listProductIds) {
        this.listProductIds = listProductIds;
    }

    void getDimension() {
        Display display = SimiManager.getIntance().getCurrentActivity()
                .getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = SimiManager.getIntance().getCurrentActivity()
                .getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth = outMetrics.widthPixels / density;
        this.withScreen = dpWidth;
    }

    public void setListProducts(ArrayList<Product> listProducts) {
        this.listProducts = listProducts;
    }

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {

        public ViewHolderHeader(View itemView) {
            super(itemView);
        }
    }

    public static class ViewHolderFooter extends RecyclerView.ViewHolder {

        public ViewHolderFooter(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView tv_name;
        private ImageView img_avartar;
        private LinearLayout layout_stock;
        private LinearLayout ll_price;
        private TextView txt_outstock;
        private RelativeLayout rl_product_list;

        public ViewHolderItem(View v) {
            super(v);
            tv_name = (TextView) v.findViewById(Rconfig
                    .getInstance().id("tv_name"));
            tv_name
                    .setTextColor(Config.getInstance().getContent_color());
            ll_price = (LinearLayout) v.findViewById(Rconfig
                    .getInstance().id("ll_price"));
            img_avartar = (ImageView) v.findViewById(Rconfig
                    .getInstance().id("img_avartar"));
            layout_stock = (LinearLayout) v
                    .findViewById(Rconfig.getInstance().id("layout_stock"));
            layout_stock.setBackgroundColor(Config.getInstance()
                    .getOut_stock_background());

            txt_outstock = (TextView) v.findViewById(Rconfig
                    .getInstance().id("txt_out_stock"));
            txt_outstock.setTextColor(Config.getInstance()
                    .getOut_stock_text());
            rl_product_list = (RelativeLayout) v
                    .findViewById(Rconfig.getInstance().id("rel_product_list"));
            LinearLayout.LayoutParams paramsLayout2 = new LinearLayout.LayoutParams(
                    Utils.getValueDp((int) ((withScreen - 22) / 2)),
                    Utils.getValueDp((int) ((withScreen - 22) / 2)));
            LinearLayout.LayoutParams paramsLayout4Phone = new LinearLayout.LayoutParams(
                    Utils.getValueDp((int) ((withScreen - 20) / 4)),
                    Utils.getValueDp((int) ((withScreen - 20) / 4)));
            LinearLayout.LayoutParams paramsLayout4Tablet = new LinearLayout.LayoutParams(
                    Utils.getValueDp((int) ((withScreen - 20) / 4)),
                    Utils.getValueDp((int) ((withScreen - 20) / 4))
                            + Utils.getValueDp(100));
            LinearLayout.LayoutParams paramsLayout6 = new LinearLayout.LayoutParams(
                    Utils.getValueDp((int) ((withScreen - 20) / 6)),
                    Utils.getValueDp((int) ((withScreen - 20) / 6)));
            if (headercount == 2) {
                rl_product_list.setLayoutParams(paramsLayout2);
            } else if (headercount == 4) {
                if (DataLocal.isTablet) {
                    rl_product_list.setLayoutParams(paramsLayout4Phone);
                } else {
                    rl_product_list.setLayoutParams(paramsLayout4Phone);
                }
            } else {
                rl_product_list.setLayoutParams(paramsLayout6);
            }
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = new View(parent.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.getValueDp(30));
            v.setLayoutParams(lp);
            return new ViewHolderHeader(v);
        } else if (viewType == TYPE_ITEM) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(Rconfig.getInstance().layout(
                    "core_item_gridview_productcategory"), parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolderItem vh = new ViewHolderItem(v);
            return vh;
        } else {
//            View v = new View(parent.getContext());
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.getValueDp(40));
//            v.setLayoutParams(lp);
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.core_base_loading, parent, false);
            return new ViewHolderFooter(v);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderItem) {
            final Product product = listProducts.get(position - headercount);

            ViewHolderItem vhItem = (ViewHolderItem) holder;

            if (product.getStock() == true) {
                vhItem.layout_stock.setVisibility(View.GONE);
            } else {
                vhItem.layout_stock.setVisibility(View.VISIBLE);
                vhItem.txt_outstock.setText(Config.getInstance().getText(
                        "Out Stock"));
            }

            ProductPriceViewProductGridV03 price_view = new ProductPriceViewProductGridV03(
                    product);
            price_view.setShowOnePrice(false);
            View view = price_view.getViewPrice();
            if (null != view && null != vhItem.ll_price) {
                vhItem.ll_price.removeAllViewsInLayout();
                vhItem.ll_price.addView(view);
            }

            String name = product.getName();
            if (null != name) {
                name.trim();
                vhItem.tv_name.setText(name);
            } else {
                vhItem.tv_name.setText("SimiCart");
            }

            if ((headercount == 6) || (headercount == 4 && !DataLocal.isTablet)) {
                vhItem.ll_price.setVisibility(View.GONE);
                vhItem.tv_name.setVisibility(View.GONE);
            }

            if (product.getImage() != null) {
                DrawableManager.fetchDrawableOnThread(product.getImage(),
                        vhItem.img_avartar);
            }

            vhItem.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String productId = product.getData("product_id");
                    if (productId != null) {
                        ProductDetailParentFragment fragment = ProductDetailParentFragment
                                .newInstance(productId, listProductIds);
                        SimiManager.getIntance().replaceFragment(fragment);
                    }
                    SimiManager.getIntance().hideKeyboard();
                }

            });

            EventBlock eventBlock = new EventBlock();
            if (headercount == 4) {
                eventBlock.dispatchEvent("com.simicart.image.product.grid4col",
                        vhItem.rl_product_list, product);
            } else {
                eventBlock.dispatchEvent("com.simicart.image.product.grid",
                        vhItem.rl_product_list, product);
            }
        }

    }

    public void addFooterView() {
        footerCount = 1;
        notifyItemRangeInserted(getItemCount() - footerCount, getItemCount() - 1);
    }

    public void removeFooterView() {
        footerCount = 0;
        notifyItemRemoved(getItemCount() - footerCount);
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isPositionFooter(int position) {
        return position >= getItemCount() - footerCount;
    }

    private boolean isPositionHeader(int position) {
        return position < headercount;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (listProducts != null) {
            size = headercount + listProducts.size() + footerCount;
        }
        return size;
    }

    public Product getItem(int position) {
        Product product = new Product();
        if (listProducts != null) {
            product = listProducts.get(position);
        }
        return product;
    }

}
