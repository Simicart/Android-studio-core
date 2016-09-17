package com.simicart.core.checkout.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by Martial on 9/1/2016.
 */
public class ListProductCheckoutAdapter extends RecyclerView.Adapter<ListProductCheckoutAdapter.ProductCheckoutHolder> {

    protected ArrayList<Cart> listProducts;
    protected Context mContext;
    protected String mCurrecySymbol;

    public ListProductCheckoutAdapter(ArrayList<Cart> listProducts) {
        this.listProducts = listProducts;
    }

    public void setCurrencySymbol(String symbol) {
        mCurrecySymbol = symbol;
    }

    @Override
    public ProductCheckoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(Rconfig.getInstance().layout("core_adapter_list_products_checkout"), parent, false);
        ProductCheckoutHolder holder = new ProductCheckoutHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(ProductCheckoutHolder holder, int position) {
        Cart cart = listProducts.get(position);

        holder.name.setTextColor(AppColorConfig.getInstance().getContentColor());
        if (AppStoreConfig.getInstance().isRTL()) {
            holder.name.setGravity(Gravity.RIGHT);
        }
        holder.name.setText(cart.getProduct_name());

        if (AppStoreConfig.getInstance().isRTL()) {
            holder.tv_price.setGravity(Gravity.RIGHT);
        }
        holder.tv_price.setTextColor(AppColorConfig.getInstance().getPriceColor());
        String price = AppStoreConfig.getInstance().getPrice(
                Float.toString(cart.getProduct_price()));
        if (null != mCurrecySymbol) {
            price = AppStoreConfig.getInstance().getPrice(
                    Float.toString(cart.getProduct_price()), mCurrecySymbol);
        }

        holder.tv_price.setText(price);
        holder.qty.setTextColor(AppColorConfig.getInstance().getContentColor());
        holder.qty.setText("" + cart.getQty());

        String img = cart.getProduct_image();
        SimiDrawImage drawImage = new SimiDrawImage();
        drawImage.drawImage(holder.image, img);
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public static class ProductCheckoutHolder extends RecyclerView.ViewHolder {

        private TextView name, tv_price, qty;
        private ImageView image;

        public ProductCheckoutHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("name_product"));
            tv_price = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("price_product"));
            qty = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("qty_product"));
            image = (ImageView) itemView.findViewById(Rconfig
                    .getInstance().id("image_product"));
        }
    }
}
