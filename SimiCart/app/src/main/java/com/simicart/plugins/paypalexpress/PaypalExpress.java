package com.simicart.plugins.paypalexpress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.model.AddToCartModel;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.ColorButton;
import com.simicart.plugins.paypalexpress.fragment.ExpressWebviewFragment;
import com.simicart.plugins.paypalexpress.model.StartExpressModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by frank on 9/7/16.
 */
public class PaypalExpress {

    public PaypalExpress() {

        BroadcastReceiver placeOrderReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onPlaceOrder();
            }
        };
        SimiEvent.registerEvent(KeyEvent.REVIEW_ORDER.FOR_PAYMENT_TYPE_WEBVIEW + "PAYPAL_EXPRESS", placeOrderReceiver);

        BroadcastReceiver productDetailReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                View productView = (View) data.getData().get(KeyData.SIMI_BLOCK.VIEW);
                SimiCollection collection = (SimiCollection) data.getData().get(KeyData.SIMI_BLOCK.COLLECTION);
                if (null != collection) {
                    ArrayList<SimiEntity> entities = collection.getCollection();
                    if (null != entities && entities.size() > 0) {
                        Product product = new Product();
                        SimiEntity entity = entities.get(0);
                        product.parse(entity.getJSONObject());
                        addExpressToProduct(productView, product);
                    }
                }

            }
        };
        SimiEvent.registerEvent("com.simicart.core.catalog.product.block.ProductDetailParentBlock", productDetailReceiver);

        BroadcastReceiver cartReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                View cartView = (View) data.getData().get(KeyData.SIMI_BLOCK.VIEW);
                addExpressToCart(cartView);
            }
        };
        SimiEvent.registerEvent("com.simicart.core.checkout.block.CartBlock", cartReceiver);
    }

    protected void onPlaceOrder() {
        final StartExpressModel model = new StartExpressModel();

        model.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {

                if (null != error) {
                    String msg = error.getMessage();
                    if (Utils.validateString(msg)) {
                        SimiNotify.getInstance().showNotify(msg);
                        SimiManager.getIntance().backToHomeFragment();
                    }
                }
            }
        });

        model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                String url = model.getUrl();
                boolean isReviewAddress = model.isReviewAddress();

                HashMap<String, Object> hm = new HashMap<>();
                hm.put("url", url);
                hm.put("review_address", isReviewAddress);

                SimiData data = new SimiData(hm);
                ExpressWebviewFragment fragment = ExpressWebviewFragment.newInstance(data);
                SimiManager.getIntance().addPopupFragment(fragment);


            }
        });

        model.request();


    }

    protected void addExpressToCart(View cartView) {

        Context context = cartView.getContext();

        RelativeLayout rltPlugin = (RelativeLayout) cartView.findViewById(Rconfig.getInstance().id("fcart_rl_bottom"));
        ColorButton btnExpress = new ColorButton(context);
        btnExpress.setStyle(4, Color.YELLOW);
        btnExpress.setBackgroundResource(Rconfig.getInstance().drawable(
                "plugin_paypal_express_product"));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, Utils.toDp(55));
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.setMargins(0, Utils.toDp(5), 0, 0);
        btnExpress.setLayoutParams(params);
        rltPlugin.removeAllViewsInLayout();
        rltPlugin.addView(btnExpress);

        btnExpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlaceOrder();
            }
        });


    }

    protected void addExpressToProduct(final View productView, final Product product) {
        Context context = productView.getContext();
        Button b_express = new Button(context);
        GradientDrawable gdDefault = new GradientDrawable();
        b_express.setBackgroundDrawable(gdDefault);
        b_express.setBackgroundResource(Rconfig.getInstance().drawable(
                "plugin_paypal_express_product"));
        int size = Utils.toDp(40);
        LinearLayout.LayoutParams lp_express = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, size);
        lp_express.setMargins(0, 0, 0, Utils.toDp(5));
        if (DataLocal.isTablet) {
            size = Utils.toDp(49);
            lp_express = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, size);
            lp_express.gravity = Gravity.CENTER_VERTICAL;
            lp_express.setMargins(0, 0, 0, Utils.toDp(10));
        }
        lp_express.gravity = Gravity.CENTER_HORIZONTAL;

        b_express.setLayoutParams(lp_express);
        LinearLayout ll_botton = (LinearLayout) productView.findViewById(Rconfig
                .getInstance().id("ll_paypal_express"));
        ll_botton.removeAllViews();
        ll_botton.addView(b_express);
        b_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkOption(product)) {
                    addToCart(product);
                } else {
                    showDialog(productView);
                }
            }
        });
    }

    public boolean checkOption(Product product) {
        ArrayList<CacheOption> options = product.getOptions();
        for (CacheOption option : options) {
            if (option.isRequired()) {
                if (!option.isCompleteRequired()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void showDialog(View productDetailView) {
        AlertDialog.Builder alertboxDowload = new AlertDialog.Builder(
                productDetailView.getContext());
        alertboxDowload.setTitle(SimiTranslator.getInstance().translate("REQUIRED")
                .toUpperCase());
        alertboxDowload.setMessage(SimiTranslator.getInstance().translate(
                "Required options are not selected."));
        alertboxDowload.setCancelable(false);
        alertboxDowload.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        alertboxDowload.show();
    }

    public void addToCart(Product product) {
        if (product.getStock()) {
            ArrayList<CacheOption> options = product.getOptions();

            AddToCartModel mModel = new AddToCartModel();
            mModel.setSuccessListener(new ModelSuccessCallBack() {
                @Override
                public void onSuccess(SimiCollection collection) {
                    onPlaceOrder();
                }
            });
            mModel.addBody("product_id", product.getId());
            mModel.addBody("product_qty", String.valueOf(product.getQty()));
            mModel.addBody("product_type", product.getType());

            if (null != options) {
                try {
                    JSONArray array = convertCacheOptionToParams(options);
                    mModel.addBody("options", array);
                } catch (JSONException e) {

                    return;
                }
            }
            mModel.request();
        }
    }

    protected JSONArray convertCacheOptionToParams(
            ArrayList<CacheOption> options) throws JSONException {
        JSONArray array = new JSONArray();
        for (CacheOption cacheOption : options) {
            if (cacheOption.toParameter() != null) {
                array.put(cacheOption.toParameter());
            }
        }
        return array;
    }

}
