package com.simicart.core.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.delegate.CartAdapterDelegate;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.entity.Option;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

@SuppressLint({"DefaultLocale", "ClickableViewAccessibility", "ViewHolder"})
public class CartListAdapter extends BaseAdapter {

    protected ArrayList<Cart> mCarts;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected CartAdapterDelegate mDelegate;

    public CartListAdapter(Context context, ArrayList<Cart> CartList) {
        this.mContext = context;
        this.mCarts = CartList;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setDelegate(CartAdapterDelegate delegate) {
        mDelegate = delegate;
    }

    public ArrayList<Cart> getCartList() {
        return this.mCarts;
    }

    public void setListCart(ArrayList<Cart> carts) {
        mCarts = carts;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (DataLocal.isLanguageRTL) {
            convertView = mInflater.inflate(
                    Rconfig.getInstance().layout("rtl_item_cart_layout"), null);
        } else {
            convertView = mInflater
                    .inflate(
                            Rconfig.getInstance().layout(
                                    "core_item_cart_layout"), null);
        }
        convertView.setBackgroundColor(AppColorConfig.getInstance().getAppBackground());

        final Cart cart = (Cart) getItem(position);

        // name
        TextView tv_name = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("item_cart_name"));
        tv_name.setTextColor(AppColorConfig.getInstance().getContentColor());
        tv_name.setText(cart.getProduct_name());

        // id
        TextView tv_id = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("item_cart_id"));
        tv_id.setTextColor(AppColorConfig.getInstance().getContentColor());
        tv_id.setText("" + cart.getId());

        // image
        ImageView img_item = (ImageView) convertView.findViewById(Rconfig
                .getInstance().id("item_cart_image"));
        String img = cart.getProduct_image();
        DrawableManager.fetchDrawableOnThread(img, img_item);

        // quantity
        TextView tv_quantity = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("tv_quantity"));
        tv_quantity.setTextColor(AppColorConfig.getInstance().getContentColor());
        tv_quantity.setText(SimiTranslator.getInstance().translate("Quantity"));
        RelativeLayout rl_quanity = (RelativeLayout) convertView
                .findViewById(Rconfig.getInstance().id("rl_item_cart_quantity"));

        TextView tv_Qty = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("item_cart_qty"));
        tv_Qty.setTextColor(AppColorConfig.getInstance().getContentColor());
        tv_Qty.setText("" + cart.getQty());
        if (null != mDelegate) {
            rl_quanity.setOnClickListener(mDelegate.getClickQtyItem(position,
                    cart.getQty(), cart.getMinQtyAllow(), cart.getMaxQtyAllow()));
        }

        // delete item
        ImageView tv_delete = (ImageView) convertView.findViewById(Rconfig
                .getInstance().id("item_cart_del"));
        Drawable icon = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("ic_delete"));
        icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        tv_delete.setImageDrawable(icon);

        if (null != mDelegate) {
            ArrayList<String> listID = new ArrayList<String>();
            for (int i = 0; i < mCarts.size(); i++) {
                listID.add(mCarts.get(i).getProduct_id());
            }
            tv_delete
                    .setOnTouchListener(mDelegate.getOnTouchListener(position));
            img_item.setOnClickListener(mDelegate.getClickItemCartListener(
                    position, listID));
        }

        // option
        TextView tv_options = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("item_cart_option"));
        tv_options.setTextColor(AppColorConfig.getInstance().getContentColor());
        ArrayList<Option> options = cart.getOptions();
        if (options != null) {
            displayOptions(tv_options, options);
        } else {
            tv_options.setVisibility(View.GONE);
        }

        // price
        TextView tv_price = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("item_cart_price"));
        tv_price.setText(AppStoreConfig.getInstance().getPrice(
                Float.toString(cart.getProduct_price())));
        tv_price.setTextColor(AppColorConfig.getInstance()
                .getPriceColor());

        // stock
        LinearLayout ll_stock = (LinearLayout) convertView.findViewById(Rconfig
                .getInstance().id("ll_stock"));
        ll_stock.setBackgroundColor(AppColorConfig.getInstance().getKeyColor());
        TextView txt_out_stock = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("txt_out_stock"));
        txt_out_stock.setText(SimiTranslator.getInstance().translate("Quantity"));
        if (cart.getStock().equals(SimiTranslator.getInstance().translate("Out Stock"))) {
            ll_stock.setVisibility(View.VISIBLE);
        } else {
            ll_stock.setVisibility(View.GONE);
        }

        // if (DataLocal.isTablet) {
        View line_cart_bottom = (View) convertView.findViewById(Rconfig
                .getInstance().id("line_cart_bottom"));
        line_cart_bottom.setBackgroundColor(AppColorConfig.getInstance()
                .getLineColor());
        // }

        return convertView;
    }

    @Override
    public int getCount() {
        return this.mCarts.size();
    }

    @Override
    public Object getItem(int position) {
        return mCarts.get(position);
    }

    @Override
    public long getItemId(int psosition) {
        return 0;
    }

    protected void displayOptions(TextView tv_options, ArrayList<Option> options) {
        int total = 0;
        if (options != null) {
            total = options.size();
        }
        if (total > 0) {
            String html = "<dl>";
            for (int i = 0; i < total; i++) {
                Option option = options.get(i);
                html += "<b>";
                html += option.getOption_title();
                html += "</b>";
                html += "<br/>";
                html += option.getOption_value();
                html += "<br/>";
            }
            html += "</dl>";
            tv_options.setText(Html.fromHtml(html));
        } else {
            tv_options.setText("");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0);
            lp.weight = 3;
            tv_options.setLayoutParams(lp);
        }
    }
}
