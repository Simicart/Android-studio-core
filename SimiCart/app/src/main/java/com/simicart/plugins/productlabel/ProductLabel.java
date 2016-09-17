package com.simicart.plugins.productlabel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductLabel {

    protected Context mContext;
    protected int textSize = 0;
    protected int sizeLable = 0;

    public ProductLabel() {

        mContext = SimiManager.getIntance().getCurrentActivity();

        // register event: add label item to product list
        BroadcastReceiver addItemReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable("entity");
                View rootView = (View) data.getData().get(KeyData.PRODUCT_LABEL.PRODUCT_LABEL_VIEW);
                JSONArray array = (JSONArray) data.getData().get(KeyData.PRODUCT_LABEL.PRODUCT_LABEL_JSON);
                String method = (String) data.getData().get(KeyData.PRODUCT_LABEL.PRODUCT_LABEL_METHOD);
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject object = array.getJSONObject(i);
                        String imageUrl = object.getString("image");
                        String imageContent = object.getString("content");
                        String positionLabel = object.getString("position");

                        switch (method) {
                            case ValueData.PRODUCT_LABEL.HORIZONTAL:
                                showLabelOnHorizontalListProduct(rootView, imageContent, imageUrl, positionLabel);
                                break;
                            case ValueData.PRODUCT_LABEL.GRID:
                                showLabelOnGridProduct(rootView, imageContent, imageUrl, positionLabel);
                                break;
                            case ValueData.PRODUCT_LABEL.LIST:
                                showLabelOnListProduct(rootView, imageContent, imageUrl, positionLabel);
                                break;
                            default:
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        SimiEvent.registerEvent(KeyEvent.PRODUCT_LABEL.PRODUCT_LABEL_ADD_ITEM, addItemReceiver);

    }

    protected void showLabelOnHorizontalListProduct(View view, String imagetext,
                                                    String imageLabel, String position) {
        if (DataLocal.isTablet) {
            this.textSize = 14;
            this.sizeLable = 50;
        } else {
            this.textSize = 12;
            this.sizeLable = 40;
        }
        addLabel(view, imagetext, imageLabel, position);
    }

    public void showLabelOnGridProduct(View view, String imagetext,
                                       String imageLabel, String position) {
        if (DataLocal.isTablet) {
            this.textSize = 16;
            this.sizeLable = 50;
        } else {
            this.textSize = 14;
            this.sizeLable = 50;
        }
        this.addLabel(view, imagetext, imageLabel, position);
    }

    public void showLabelOnListProduct(View view, String imagetext,
                                       String imageLabel, String position) {
        if (DataLocal.isTablet) {
            this.textSize = 10;
            this.sizeLable = 30;
        } else {
            this.textSize = 10;
            this.sizeLable = 30;
        }
        this.addLabel(view, imagetext, imageLabel, position);
    }

    protected void addLabel(View view, String imagetext, String imageLabel, String position) {
        TextView label = new TextView(mContext);
        DrawableManager.fetchDrawableOnThread(imageLabel, label);
        label.setText(imagetext);
        label.setMaxLines(1);
        label.setEllipsize(TextUtils.TruncateAt.END);
        label.setTextColor(Color.parseColor("#FFFFFF"));

        label.setGravity(Gravity.CENTER);
        float scale = mContext.getResources().getDisplayMetrics().density;
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        int size = (int) (sizeLable * scale + 0.5f);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(size, size);
        switch (position) {
            case "1":
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case "2":
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                break;
            case "3":
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case "4":
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                break;
            case "5":
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                // lp.addRule(RelativeLayout.CENTER_VERTICAL);
                break;
            case "6":
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.addRule(RelativeLayout.CENTER_VERTICAL);
                break;
            case "7":
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case "8":
                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case "9":
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            default:
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                break;
        }
        label.setLayoutParams(lp); // causes layout update
        ((ViewGroup) view).addView(label);
    }

}
