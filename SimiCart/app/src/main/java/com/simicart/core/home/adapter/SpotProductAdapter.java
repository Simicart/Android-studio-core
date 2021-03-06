package com.simicart.core.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.common.price.ProductPriceView;
import com.simicart.core.common.price.ProductPriceViewProductGridV03;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Rconfig;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by frank on 17/08/2016.
 */
public class SpotProductAdapter extends RecyclerView.Adapter<SpotProductAdapter.ViewHolder> {

    protected ArrayList<Product> mProducts;
    protected ArrayList<String> mListID;
    protected boolean isHomePage;

    public SpotProductAdapter(ArrayList<Product> products) {
        mProducts = products;
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int idView = Rconfig.getInstance().layout("core_adapter_horizontal_products");
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(idView, null);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Product product = mProducts.get(position);

        if (isHomePage) {
            if (AppStoreConfig.getInstance().isRTL()) {
                holder.tv_name.setGravity(Gravity.RIGHT);
                holder.ll_price.setGravity(Gravity.RIGHT);
            } else {
                holder.tv_name.setGravity(Gravity.LEFT);
                holder.ll_price.setGravity(Gravity.LEFT);
            }
        }

        // name
        String name = product.getName();
        if (Utils.validateString(name)) {
            holder.tv_name.setText(name);
            holder.tv_name.setTextColor(AppColorConfig.getInstance().getContentColor());
        }

        // image
        String urlImage = product.getImage();
        if (urlImage != null) {
            DrawableManager.fetchDrawableOnThread(urlImage,
                    holder.img_avartar);
        }

        // price
        if (product.getPriceV2() != null) {
            ProductPriceViewProductGridV03 viewPrice = new ProductPriceViewProductGridV03(
                    product);
            viewPrice.setShowOnePrice(true);
            View view = viewPrice.getViewPrice();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            if (null != view) {
                holder.ll_price.removeAllViewsInLayout();
                holder.ll_price.addView(view, params);
            }
        } else {
            ProductPriceView viewPrice = new ProductPriceView(product);
            View view = viewPrice.getViewPriceHome();
            if (null != view) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                if (AppStoreConfig.getInstance().isRTL()) {
                    holder.ll_price.setGravity(Gravity.RIGHT);
                } else {
                    holder.ll_price.setGravity(Gravity.LEFT);
                }
                holder.ll_price.removeAllViewsInLayout();
                holder.ll_price.addView(view, params);
            }
        }

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = product.getId();
                if (null == mListID) {
                    mListID = new ArrayList<>();
                    for (int i = 0; i < mProducts.size(); i++) {
                        mListID.add(mProducts.get(i).getId());
                    }
                }
                HashMap<String, Object> hmData = new HashMap<>();
                hmData.put(KeyData.PRODUCT_DETAIL.PRODUCT_ID, id);
                hmData.put(KeyData.PRODUCT_DETAIL.LIST_PRODUCT_ID, mListID);
                SimiManager.getIntance().openProductDetail(hmData);
            }
        });

        // dispatch event for product label
        if (product.getJSONObject().has("product_label")) {
            try {
                JSONArray array = product.getJSONObject().getJSONArray("product_label");
                dispatchEventForProductLabel(holder.rlImage, array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    protected void dispatchEventForProductLabel(View view, JSONArray array) {
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put(KeyData.PRODUCT_LABEL.PRODUCT_LABEL_VIEW, view);
        hmData.put(KeyData.PRODUCT_LABEL.PRODUCT_LABEL_JSON, array);
        hmData.put(KeyData.PRODUCT_LABEL.PRODUCT_LABEL_METHOD, ValueData.PRODUCT_LABEL.HORIZONTAL);
        SimiEvent.dispatchEvent(KeyEvent.PRODUCT_LABEL.PRODUCT_LABEL_ADD_ITEM, hmData);
    }

    public void setHomePage(boolean isHome) {
        isHomePage = isHome;
    }

    public void setListID(ArrayList<String> ids) {
        mListID = ids;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_avartar;
        TextView tv_name;
        LinearLayout ll_price, llItem;
        RelativeLayout rlImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("product_list_name"));
            ll_price = (LinearLayout) itemView.findViewById(Rconfig
                    .getInstance().id("layout_price"));
            img_avartar = (ImageView) itemView.findViewById(Rconfig
                    .getInstance().id("product_list_image"));
            llItem = (LinearLayout) itemView.findViewById(Rconfig.getInstance().id("product_list_details"));
            rlImage = (RelativeLayout) itemView.findViewById(Rconfig.getInstance().id("rel_product_list_spot"));
        }
    }
}
