package com.simicart.core.checkout.controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TextView;

import com.magestore.simicart.R;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.checkout.checkoutwebview.fragment.CheckoutWebviewFragment;
import com.simicart.core.checkout.delegate.CartAdapterDelegate;
import com.simicart.core.checkout.delegate.CartDelegate;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.model.EditCartItemModel;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

@SuppressLint("ClickableViewAccessibility")
public class CartListenerController implements CartAdapterDelegate {
    protected OnClickListener mCheckoutClicker;
    protected ArrayList<Cart> mCarts;
    protected CartDelegate mBlockDelegate;
    protected String mMessage;
    protected String mWebviewUrl;

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public CartListenerController() {
        mCheckoutClicker = new OnClickListener() {

            @Override
            public void onClick(View v) {
                onCheckOut();
            }
        };
    }

    protected void onCheckOut() {
        if (!mMessage.contains("NOT CHECKOUT")) {
            if (mWebviewUrl != null && !mWebviewUrl.equals("")) {
                CheckoutWebviewFragment fragment = CheckoutWebviewFragment
                        .newInstanse(mWebviewUrl, true);
                SimiManager.getIntance().replaceFragment(fragment);
            } else {
//                if (DataPreferences.isSignInComplete()) {
//                    AddressBookCheckoutFragment fragment = AddressBookCheckoutFragment
//                            .newInstance(0, Constants.KeyAddress.ALL_ADDRESS,
//                                    null, null);
//                    if (DataLocal.isTablet) {
//                        SimiManager.getIntance().replacePopupFragment(fragment);
//                    } else {
//                        SimiManager.getIntance().replaceFragment(fragment);
//                    }
//                } else {
//                    mBlockDelegate.showPopupCheckout();
//                }
            }
        } else {
            SimiNotify.getInstance().showNotify(null, mMessage, "Ok");
        }
    }

    protected void showProductDetail(int position, ArrayList<String> listID) {
        Cart cart = mCarts.get(position);
        if (null != cart) {
            String id = cart.getProduct_id();
            if (Utils.validateString(id)) {
                ProductDetailParentFragment fragment = ProductDetailParentFragment
                        .newInstance(id, listID);
                // fragment.setProductID(id);
                // fragment.setListIDProduct(listID);
                SimiManager.getIntance().replaceFragment(fragment);
                SimiManager.getIntance().removeDialog();
            }
        }
    }

    @Override
    public OnTouchListener getOnTouchListener(final int position) {
        OnTouchListener onTouch = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setBackgroundColor(Color.parseColor("#EBEBEB"));
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        deleteItemCart(position);
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        v.setBackgroundColor(0);
                        break;
                    }
                    default:
                        break;
                }
                return true;
            }
        };

        return onTouch;
    }

    public OnClickListener getClickItemCartListener(final int position,
                                                    final ArrayList<String> listID) {
        OnClickListener clicker = new OnClickListener() {

            @Override
            public void onClick(View v) {
                showProductDetail(position, listID);

            }
        };

        return clicker;
    }

    private void deleteItemCart(final int position) {
        new AlertDialog.Builder(SimiManager.getIntance().getCurrentActivity())
                .setMessage(
                        SimiTranslator.getInstance()
                                .translate(
                                        "Are you sure you want to delete this product?"))
                .setPositiveButton(SimiTranslator.getInstance().translate("Yes"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                editItemCart(position, "0");
                            }
                        })
                .setNegativeButton(SimiTranslator.getInstance().translate("No"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                            }
                        }).show();

    }

    protected void editItemCart(final int position, String qty) {
        try {
            Cart cart = mCarts.get(position);
            String cartID = cart.getId();

            JSONObject jsonParam = null;
            try {
                jsonParam = new JSONObject();
                jsonParam.put("cart_item_id", cartID);
                jsonParam.put("product_qty", qty);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (null != jsonParam) {
                JSONArray arrParams = new JSONArray();
                arrParams.put(jsonParam);
                editItem(arrParams);
            }
        } catch (Exception e) {
        }
    }

    protected void editItem(JSONArray arrParams) {
        mBlockDelegate.showDialogLoading();

        final EditCartItemModel model = new EditCartItemModel();
        model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mBlockDelegate.dismissDialogLoading();
//					mBlockDelegate.setMessage(message);
                int cartQty = model.getQty();
                SimiManager.getIntance().onUpdateCartQty(
                        String.valueOf(cartQty));
                DataLocal.listCarts.clear();
                mBlockDelegate.updateView(((EditCartItemModel) model)
                        .getCollection());
                mBlockDelegate
                        .onUpdateTotalPrice(((EditCartItemModel) model)
                                .getTotalPrice());
            }
        });

        model.addBody("cart_items", arrParams);
        model.request();
    }

    protected void onEditQty(final int position, String qty) {
        mCarts.get(position).setQty(Integer.parseInt(qty));
    }

    public void setListCart(ArrayList<Cart> carts) {
        mCarts = carts;
    }

    public OnClickListener getCheckoutClicker() {
        return mCheckoutClicker;
    }

    public void setCartDelegate(CartDelegate delegate) {
        mBlockDelegate = delegate;
    }

    private void showDialogNumberPicker(final int position, final int qty,
                                        int min, int max) {
        Context context = SimiManager.getIntance().getCurrentActivity();
        final Dialog dialoglayout = new Dialog(context);
        dialoglayout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoglayout.setContentView(Rconfig.getInstance().layout(
                "core_cart_dialog_layout"));
        if (!DataLocal.isTablet) {
            dialoglayout.getWindow().setLayout(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            dialoglayout.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            dialoglayout.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        } else {
            dialoglayout.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }
        dialoglayout.show();

        final WheelView wheel = (WheelView) dialoglayout.findViewById(Rconfig
                .getInstance().id("select_quantity"));

        if (!Utils.validateString("" + min)) {
            min = 1;
        }
        if (!Utils.validateString("" + max)) {
            max = 1;
        }
        final NumericWheelAdapter minAdapter = new NumericWheelAdapter(
                context, min, max);
        wheel.setViewAdapter(minAdapter);
        if (qty > 0) {
            wheel.setCurrentItem((qty - 1));
        }

        TextView bt_apply = (TextView) dialoglayout.findViewById(Rconfig
                .getInstance().id("bt_apply"));
        bt_apply.setText(SimiTranslator.getInstance().translate("Done"));
        bt_apply.setTextColor(Color.parseColor("#0173F2"));
        bt_apply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String value = String.valueOf(minAdapter.getItemText(wheel
                        .getCurrentItem()));
                if (Integer.parseInt(value) != qty) {
                    editItemCart(position, String.valueOf(minAdapter
                            .getItemText(wheel.getCurrentItem())));
                }
                dialoglayout.dismiss();
            }
        });

        TextView bt_cancel = (TextView) dialoglayout.findViewById(Rconfig
                .getInstance().id("bt_cancel"));
        bt_cancel.setText(SimiTranslator.getInstance().translate("Cancel"));
        bt_cancel.setTextColor(Color.parseColor("#0173F2"));
        bt_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialoglayout.dismiss();
            }
        });
    }

    @Override
    public OnClickListener getClickQtyItem(final int position, final int qty,
                                           final int min, final int max) {
        OnClickListener onclick = new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogNumberPicker(position, qty, min, max);
            }
        };
        return onclick;
    }

    public void setWebviewUrl(String url) {
        this.mWebviewUrl = url;
    }

}
