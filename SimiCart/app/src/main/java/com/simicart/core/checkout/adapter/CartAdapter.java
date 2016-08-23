package com.simicart.core.checkout.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.component.ListProductCheckoutComponent;
import com.simicart.core.checkout.delegate.CartDelegate;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.model.EditCartItemModel;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by Crabby PC on 6/22/2016.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    protected ArrayList<Cart> listCarts;
    protected Context mContext;
    protected SimiDelegate mDelegate;
    protected int layoutID;
    protected EditCartItemModel editModel;

    public CartAdapter(ArrayList<Cart> listCarts, int layoutID, SimiDelegate delegate) {
        this.listCarts = listCarts;
        this.layoutID = layoutID;
        mDelegate = delegate;
        mContext = SimiManager.getIntance().getCurrentActivity();
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(layoutID, null, false);
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

        holder.tvItemCartPrice.setText(String.valueOf(cartEntity.getProduct_price()));

        holder.rlItemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SimiData data = new SimiData();
//                data.setData(quoteItemEntity.getProductID());
//                ArrayList<String> listID = new ArrayList<String>();
//                listID.add(quoteItemEntity.getProductID());
//                data.setListString(listID);
//                ProductFragment fragment = ProductFragment.newInstance(data);
//                SimiManager.getIntance().replaceFragment(fragment);
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
        if(qty > 0) {
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
                createListProducts();
            }
        });
        editModel.addBody(listCarts.get(position).getId(), quantity);
        editModel.request();
    }

    protected void createListProducts() {
        ArrayList<Cart> listQuotes = ((EditCartItemModel) editModel).getListCarts();
        ListProductCheckoutComponent listProductCheckoutComponent =
                new ListProductCheckoutComponent(listQuotes, mDelegate, layoutID);
        View view = listProductCheckoutComponent.createView();
        ((CartDelegate)mDelegate).showListProductsView(view);
    }

    @Override
    public int getItemCount() {
        return listCarts.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItemCartId, tvOutStock, tvItemCartName, tvItemCartOption,
                tvItemCartPrice, tvQuantity, tvItemCartQty;
        private ImageView ivItemCartImage, ivItemCartDelete, ivItemCartQty;
        private RelativeLayout rItemCartlQty, rlItemCart;

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
            ivItemCartQty = (ImageView) itemView.findViewById(Rconfig.getInstance().id("iv_quantity"));
            rItemCartlQty = (RelativeLayout) itemView.findViewById(Rconfig.getInstance().id("rl_quantity"));
        }
    }
}
