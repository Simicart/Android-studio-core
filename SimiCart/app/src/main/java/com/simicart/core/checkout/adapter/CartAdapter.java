package com.simicart.core.checkout.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.delegate.CartDelegate;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.entity.Option;
import com.simicart.core.checkout.model.CartModel;
import com.simicart.core.checkout.model.EditCartItemModel;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Crabby PC on 6/22/2016.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    protected ArrayList<Cart> listCarts;
    protected Context mContext;
    protected SimiDelegate mDelegate;
    protected EditCartItemModel editModel;

    public CartAdapter(ArrayList<Cart> listCarts, SimiDelegate delegate) {
        this.listCarts = listCarts;
        mDelegate = delegate;
        mContext = SimiManager.getIntance().getCurrentActivity();
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = null;
        if (AppStoreConfig.getInstance().isRTL()) {
            itemView = inflater.inflate(Rconfig.getInstance().layout("rtl_core_adapter_cart_item"), parent, false);
        } else {
            itemView = inflater.inflate(Rconfig.getInstance().layout("core_adapter_cart_item"), parent, false);
        }
        CartViewHolder holder = new CartViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {
        final Cart cartEntity = listCarts.get(position);

        holder.tvItemCartName.setText(cartEntity.getProduct_name());

        holder.tvItemCartId.setText(cartEntity.getProduct_id());

        SimiDrawImage drawImage = new SimiDrawImage();
        drawImage.drawImage(holder.ivItemCartImage, cartEntity.getProduct_image());

        holder.tvQuantity.setText(SimiTranslator.getInstance().translate("Quantity"));
        holder.tvItemCartQty.setText(String.valueOf(cartEntity.getQty()));

        Drawable icon = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("ic_delete"));
        icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        holder.ivItemCartDelete.setImageDrawable(icon);
        holder.ivItemCartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestEditQuantity(position, "0");
            }
        });

        holder.tvItemCartPrice.setText(AppStoreConfig.getInstance().getPrice(
                Float.toString(cartEntity.getProduct_price())));
        holder.tvItemCartPrice.setTextColor(AppColorConfig.getInstance().getPriceColor());

        holder.tvItemCartOption.setTextColor(AppColorConfig.getInstance().getContentColor());
        ArrayList<Option> options = cartEntity.getOptions();
        if (options != null) {
            displayOptions(holder.tvItemCartOption, options);
        } else {
            holder.tvItemCartOption.setVisibility(View.GONE);
        }

        holder.llStock.setBackgroundColor(AppColorConfig.getInstance().getKeyColor());
        holder.tvOutStock.setText(SimiTranslator.getInstance().translate("Quantity"));
        if (cartEntity.getStock().equals(SimiTranslator.getInstance().translate("Out Stock"))) {
            holder.llStock.setVisibility(View.VISIBLE);
        } else {
            holder.llStock.setVisibility(View.GONE);
        }

        holder.vCart.setBackgroundColor(AppColorConfig.getInstance()
                .getLineColor());

        holder.rlCartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hmData = new HashMap<String, Object>();
                hmData.put(KeyData.PRODUCT_DETAIL.PRODUCT_ID, cartEntity.getProduct_id());
                ArrayList<String> listID = new ArrayList<String>();
                listID.add(cartEntity.getProduct_id());
                hmData.put(KeyData.PRODUCT_DETAIL.LIST_PRODUCT_ID, listID);
                SimiManager.getIntance().openProductDetail(hmData);
            }
        });

        final int quantity = cartEntity.getQty();
        final int min = cartEntity.getMinQtyAllow();
        final int max = cartEntity.getMaxQtyAllow();
        holder.rItemCartlQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQtyPicker(position, quantity, min, max);
            }
        });
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

    protected void showQtyPicker(final int position, final int qty, int min, int max) {
        final Dialog dialoglayout = new Dialog(SimiManager.getIntance().getCurrentActivity());
        dialoglayout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoglayout.setContentView(Rconfig.getInstance().layout(
                "core_dialog_cart_qty_picker"));
        if (!DataLocal.isTablet) {
            dialoglayout.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            dialoglayout.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
//            dialoglayout.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        } else {
            dialoglayout.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }
        dialoglayout.show();

        final NumberPicker numberPicker = (NumberPicker) dialoglayout.findViewById(Rconfig
                .getInstance().id("np_quantity"));

        if (!Utils.validateString("" + min)) {
            min = 1;
        }
        if (!Utils.validateString("" + max)) {
            max = 1;
        }

        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        if (qty > 0) {
            numberPicker.setValue(qty);
        }
        numberPicker.setWrapSelectorWheel(false);

        TextView bt_apply = (TextView) dialoglayout.findViewById(Rconfig
                .getInstance().id("bt_apply"));
        bt_apply.setText(SimiTranslator.getInstance().translate("Done"));
        bt_apply.setTextColor(Color.parseColor("#0173F2"));
        bt_apply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String qtyPicked = String.valueOf(numberPicker.getValue());
                requestEditQuantity(position, qtyPicked);
                dialoglayout.dismiss();
            }
        });

        TextView bt_cancel = (TextView) dialoglayout.findViewById(Rconfig
                .getInstance().id("bt_cancel"));
        bt_cancel.setText(SimiTranslator.getInstance().translate("Cancel"));
        bt_cancel.setTextColor(Color.parseColor("#0173F2"));
        bt_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialoglayout.dismiss();
            }
        });
    }

    protected void requestEditQuantity(int position, String quantity) {
        mDelegate.showDialogLoading();
        editModel = new EditCartItemModel();
        editModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissDialogLoading();
                ArrayList<Cart> listQuotes = ((EditCartItemModel) editModel).getListCarts();
                if (listQuotes.size() > 0) {
                    createListProducts(listQuotes);
                    ((CartDelegate) mDelegate).onUpdateTotalPrice(((EditCartItemModel) editModel)
                            .getTotalPrice());
                    int carQty = ((CartModel) editModel).getQty();
                    SimiManager.getIntance().onUpdateCartQty(
                            String.valueOf(carQty));
                } else {
                    ((CartDelegate) mDelegate).visibleAllView();
                }
            }
        });
        JSONObject jsonParam = null;
        try {
            jsonParam = new JSONObject();
            jsonParam.put("cart_item_id", listCarts.get(position).getId());
            jsonParam.put("product_qty", quantity);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (null != jsonParam) {
            JSONArray arrParams = new JSONArray();
            arrParams.put(jsonParam);
            editModel.addBody("cart_items", arrParams);
        }
        editModel.request();
    }

    protected void createListProducts(ArrayList<Cart> listQuotes) {
        ((CartDelegate) mDelegate).showListProductsView(listQuotes);
    }

    public void setListCarts(ArrayList<Cart> listCarts) {
        this.listCarts = listCarts;
    }

    @Override
    public int getItemCount() {
        return listCarts.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItemCartId, tvOutStock, tvItemCartName, tvItemCartOption,
                tvItemCartPrice, tvQuantity, tvItemCartQty;
        private ImageView ivItemCartImage, ivItemCartDelete;
        private RelativeLayout rItemCartlQty, rlItemCart, rlCartImage;
        private LinearLayout llStock;
        private View vCart;

        public CartViewHolder(View itemView) {
            super(itemView);
            rlItemCart = (RelativeLayout) itemView.findViewById(Rconfig.getInstance().id("rl_cart"));
            tvOutStock = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_out_stock"));
            tvItemCartName = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_item_cart_name"));
            tvItemCartOption = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_item_cart_option"));
            tvItemCartPrice = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_item_cart_price"));
            tvQuantity = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_quantity_label"));
            tvItemCartQty = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_item_cart_qty"));
            tvItemCartId = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_item_cart_id"));
            ivItemCartImage = (ImageView) itemView.findViewById(Rconfig.getInstance().id("iv_item_cart_image"));
            ivItemCartDelete = (ImageView) itemView.findViewById(Rconfig.getInstance().id("iv_item_cart_del"));
            rItemCartlQty = (RelativeLayout) itemView.findViewById(Rconfig.getInstance().id("rl_item_cart_quantity"));
            llStock = (LinearLayout) itemView.findViewById(Rconfig.getInstance().id("ll_stock"));
            vCart = (View) itemView.findViewById(Rconfig.getInstance().id("v_cart"));
            rlCartImage = (RelativeLayout) itemView.findViewById(Rconfig.getInstance().id("rl_item_cart_image"));
        }
    }
}
