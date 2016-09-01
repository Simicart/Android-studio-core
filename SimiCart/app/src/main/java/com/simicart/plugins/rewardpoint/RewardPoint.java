package com.simicart.plugins.rewardpoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.component.SimiMenuRowComponent;
import com.simicart.core.base.component.callback.MenuRowCallBack;
import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.component.PaymentMethodComponent;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.MyAccountDelegate;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.core.slidemenu.entity.ItemNavigation.TypeItem;
import com.simicart.plugins.rewardpoint.component.RewardPointComponent;
import com.simicart.plugins.rewardpoint.fragment.MyRewardFragment;
import com.simicart.plugins.rewardpoint.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RewardPoint {

    private Context mContext;
    private static ArrayList<ItemNavigation> mItems;
    protected HashMap<String, String> mFragments;
    public static String REWARDPOINT_MENU_ITEM = "My Rewards";

    public RewardPoint() {

        mContext = SimiManager.getIntance().getCurrentActivity();

        // register event: add navigation item to slide menu
        BroadcastReceiver addItemReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable("entity");
                mItems = (ArrayList<ItemNavigation>) data.getData().get(KeyData.SLIDE_MENU.LIST_ITEMS);
                mFragments = (HashMap<String, String>) data.getData().get(KeyData.SLIDE_MENU.LIST_FRAGMENTS);
                if(isExistReward() == false) {
                    addItemToSlideMenu();
                }
            }
        };
        SimiEvent.registerEvent(KeyEvent.SLIDE_MENU_EVENT.ADD_ITEM_RELATED_PERSONAL, addItemReceiver);

        // register event: add item to cart
        BroadcastReceiver addItemCartReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                View view = (View) data.getData().get(KeyData.SIMI_BLOCK.VIEW);
                SimiCollection collection = (SimiCollection) data.getData().get(KeyData.SIMI_BLOCK.COLLECTION);
                addItemToCart(view, collection);
            }
        };
        SimiEvent.registerEvent(KeyEvent.REWARD_POINT_EVENT.REWARD_ADD_ITEM_CART, addItemCartReceiver);

        // register event: add item to review order
        BroadcastReceiver addItemReviewOrderReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                ArrayList<SimiComponent> listComponents = (ArrayList<SimiComponent>) data.getData().get(KeyData.REVIEW_ORDER.LIST_COMPONENTS);
                JSONObject jsonData = (JSONObject) data.getData().get(KeyData.REVIEW_ORDER.JSON_DATA);
                addItemReviewOrder(listComponents, jsonData);
            }
        };
        SimiEvent.registerEvent(KeyEvent.REVIEW_ORDER.FOR_ADD_PLUGIN_COMPONENT, addItemReviewOrderReceiver);

        // register event: add item to basic info
        BroadcastReceiver addItemBasicInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                JSONObject object = (JSONObject) data.getData().get(KeyData.REWARD_POINT.ENTITY_JSON);
                View view = (View) data.getData().get(KeyData.REWARD_POINT.VIEW);
                addItemBasicInfo(view, object);
            }
        };
        SimiEvent.registerEvent(KeyEvent.REWARD_POINT_EVENT.REWARD_ADD_ITEM_BASIC_INFO, addItemBasicInfoReceiver);

        // register event: add row to total price
        BroadcastReceiver addRowTotalPriceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                ArrayList<TableRow> listRows = (ArrayList<TableRow>) data.getData().get(KeyData.TOTAL_PRICE.LIST_ROWS);
                JSONObject jsonObject = (JSONObject) data.getData().get(KeyData.TOTAL_PRICE.JSON_DATA);
                addRowTotalPrice(jsonObject, listRows);
            }
        };
        SimiEvent.registerEvent(KeyEvent.TOTAL_PRICE_EVENT.TOTAL_PRICE_ADD_ROW, addRowTotalPriceReceiver);

        // register event: update after sign in
        BroadcastReceiver UpdateRewardAfterSignInReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                JSONObject jsonObject = (JSONObject) data.getData().get(KeyData.SIMI_CONTROLLER.JSON_DATA);
                updateRewardAfterSignIn(jsonObject);
            }
        };
        SimiEvent.registerEvent(KeyEvent.SIGN_IN_EVENT.SIGN_IN_COMPLETE, UpdateRewardAfterSignInReceiver);

        // register event: add item to myaccount
        BroadcastReceiver addItemMyAccountReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                MyAccountDelegate mDelegate = (MyAccountDelegate) data.getData().get(KeyData.SIMI_CONTROLLER.DELEGATE);
                addItemMyAccount(mDelegate);
            }
        };
        SimiEvent.registerEvent(KeyEvent.MY_ACCOUNT_EVENT.MY_ACCOUNT_ADD_ITEM, addItemMyAccountReceiver);

    }

    protected boolean isExistReward() {
        for (ItemNavigation item : mItems) {
            if (item.getName().equals(SimiTranslator.getInstance().translate(
                    REWARDPOINT_MENU_ITEM))) {
                return true;
            }
        }
        return false;
    }

    protected void addItemToSlideMenu() {
        ItemNavigation mItemNavigation = new ItemNavigation();
        mItemNavigation.setType(TypeItem.PLUGIN);
        Drawable iconContactUs = mContext.getResources()
                .getDrawable(
                        Rconfig.getInstance().drawable(
                                "plugin_reward_ic_menu"));
        iconContactUs.setColorFilter(AppColorConfig.getInstance().getMenuIconColor(),
                PorterDuff.Mode.SRC_ATOP);
        mItemNavigation.setName(REWARDPOINT_MENU_ITEM);
        mItemNavigation.setIcon(iconContactUs);
        mItems.add(mItemNavigation);

        MyRewardFragment rewardPointFragment = new MyRewardFragment();
        mFragments.put(mItemNavigation.getName(),
                rewardPointFragment.getClass().getName());
    }

    protected void addItemToCart(View rootView, SimiCollection collection) {
        String label = "";
        String image = "";
        JSONObject jsonObject = collection.getJSON();
        LinearLayout ll_reward_card = (LinearLayout) rootView.findViewById(Rconfig
                .getInstance().id("ll_reward_card"));
        try {
            JSONArray array = jsonObject.getJSONArray("data");
            JSONObject object = array.getJSONObject(0);
            if (object.has("loyalty_label")) {
                label = object.getString("loyalty_label");
                ll_reward_card.setVisibility(View.VISIBLE);
            } else {
                ll_reward_card.setVisibility(View.GONE);
            }
            if (object.has("loyalty_image")) {
                image = object.getString("loyalty_image");
                ll_reward_card.setVisibility(View.VISIBLE);
            } else {
                ll_reward_card.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("REWARD POINT:", "Exception add item to cart ===>" + e.getMessage());
        }

        if(ll_reward_card != null) {
            ll_reward_card.removeAllViews();
            LinearLayout.LayoutParams paramsImageview = new LinearLayout.LayoutParams(
                    Utils.getValueDp(20), Utils.getValueDp(20));
            paramsImageview.gravity = Gravity.CENTER_VERTICAL;
            paramsImageview.setMargins(Utils.getValueDp(10), 0,
                    Utils.getValueDp(10), 0);
            ImageView img_point = new ImageView(mContext);
            img_point.setLayoutParams(paramsImageview);
            SimiDrawImage drawImage = new SimiDrawImage();
            drawImage.drawImage(img_point, image);
            ll_reward_card.addView(img_point);

            LinearLayout.LayoutParams paramsTextview = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsTextview.gravity = Gravity.CENTER_VERTICAL;
            paramsTextview.setMargins(0, Utils.getValueDp(5), 0,
                    Utils.getValueDp(5));
            TextView textView = new TextView(mContext);
            textView.setTextColor(Color.parseColor("#ff033e"));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setText(Html.fromHtml(label));
            textView.setTextColor(Color.parseColor("#ff033e"));
            textView.setLayoutParams(paramsTextview);
            ll_reward_card.addView(textView);
        }
    }

    protected void addItemBasicInfo(View rootView, JSONObject jsonObject) {
        String label = "";
        String image = "";
        LinearLayout ll_rewardpoint = (LinearLayout) rootView.findViewById(Rconfig
                .getInstance().id("ll_rewardpoint"));
        ll_rewardpoint.setGravity(Gravity.CENTER_VERTICAL);
        try {
            if (jsonObject.has("loyalty_label")) {
                label = jsonObject.getString("loyalty_label");
                ll_rewardpoint.setVisibility(View.VISIBLE);
            } else {
                ll_rewardpoint.setVisibility(View.GONE);
            }
            if (jsonObject.has("loyalty_image")) {
                image = jsonObject.getString("loyalty_image");
                ll_rewardpoint.setVisibility(View.VISIBLE);
            } else {
                ll_rewardpoint.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e("REWARD POINT:", "Exception add item to basic info ===>" + e.getMessage());
        }

        LinearLayout.LayoutParams paramsImageview;
        if (DataLocal.isTablet) {
            paramsImageview = new LinearLayout.LayoutParams(40, 40);
        } else {
            paramsImageview = new LinearLayout.LayoutParams(50, 50);
        }
        paramsImageview.gravity = Gravity.CENTER_VERTICAL;

        ImageView img_point = new ImageView(mContext);
        if (DataLocal.isTablet) {
            paramsImageview.setMargins(0, 10, 0, 0);
            img_point.setPadding(0, 0, 0, 10);
        } else {
            paramsImageview.setMargins(0, 30, 0, 0);
        }
        img_point.setLayoutParams(paramsImageview);
        SimiDrawImage drawImage = new SimiDrawImage();
        drawImage.drawImage(img_point, image);
        ll_rewardpoint.addView(img_point);

        @SuppressWarnings("unused")
        LinearLayout.LayoutParams paramsTextview = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsTextview.gravity = Gravity.CENTER_VERTICAL;
        if (DataLocal.isTablet) {
            paramsTextview.setMargins(10, 10, 0, 0);
        } else {
            paramsTextview.setMargins(10, 20, 0, 0);
        }

        TextView textView = new TextView(mContext);
        textView.setText(Html.fromHtml(label));
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setLayoutParams(paramsTextview);
        ll_rewardpoint.addView(textView);
    }

    protected void addItemReviewOrder(ArrayList<SimiComponent> listComponents, JSONObject jsonObject) {

        RewardPointComponent rewardPointComponent = new RewardPointComponent();
        JSONObject feeJson = null;
        if (jsonObject.has("fee")) {
            try {
                feeJson = jsonObject.getJSONObject("fee");
                if(feeJson.has("loyalty_spend") && feeJson.has("loyalty_rules")) {
                    rewardPointComponent.setJSONData(feeJson);
                    rewardPointComponent.setListComponents(listComponents);
                    for (SimiComponent component : listComponents) {
                        if (component instanceof PaymentMethodComponent) {
                            int index = listComponents.indexOf(component);
                            listComponents.add(index + 1, rewardPointComponent);
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void addRowTotalPrice(JSONObject objectResult, ArrayList<TableRow> listRows) {
        String loyalty_spend = "";
        String loyalty_discount = "";
        try {
            loyalty_spend = objectResult.getString("loyalty_spend");
            loyalty_discount = objectResult.getString("loyalty_discount");

        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        }

        if (!loyalty_spend.equals("") && !loyalty_spend.equals("0")) {
            TableRow tableRowPointDiscount = new TableRow(mContext);
            tableRowPointDiscount.setGravity(Gravity.RIGHT);
            String labelDiscount = "<font color='" + "#000000" + "'>"
                    + loyalty_spend
                    + SimiTranslator.getInstance().translate(" Points Discount")
                    + ": </font>";
            TextView textview_label_discount = (TextView) showView(labelDiscount);
            String priceDiscount = "<font color='" + AppColorConfig.getInstance().getPriceColor() + "'>"
                    + AppStoreConfig.getInstance().getPrice(loyalty_discount)
                    + "</font>";
            TextView textview_price_discount = (TextView) showView(priceDiscount);
            if (DataLocal.isLanguageRTL) {
                tableRowPointDiscount.addView(textview_price_discount);
                tableRowPointDiscount.addView(textview_label_discount);
            } else {
                tableRowPointDiscount.addView(textview_label_discount);
                tableRowPointDiscount.addView(textview_price_discount);
            }
            listRows.add(0, tableRowPointDiscount);
        }
    }

    protected View showView(String content) {
        TextView tv_price = new TextView(mContext);
        tv_price.setGravity(Gravity.RIGHT);
        tv_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv_price.setText(Html.fromHtml(content));
        return tv_price;
    }

    protected void updateRewardAfterSignIn(JSONObject jsonObject) {
        try {
            if (jsonObject.has(Constants.DATA)) {
                JSONArray array = jsonObject.getJSONArray(Constants.DATA);
                JSONObject js_signIn = array.getJSONObject(0);
                if (js_signIn.has("loyalty_balance")) {
                    String loyalty_balance = js_signIn
                            .getString("loyalty_balance");
                    if (!loyalty_balance.equals("0 Point")) {
                        updateItemLeftMenu(mItems, loyalty_balance);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void updateItemLeftMenu(ArrayList<ItemNavigation> mItems,
                                      String loyalty_redeem) {
        for (ItemNavigation mItemNavigation : mItems) {
            if (mItemNavigation.getName().equals(
                    SimiTranslator.getInstance()
                            .translate(REWARDPOINT_MENU_ITEM)
                            + Constant.REWARDPOINT_REDEEM)) {
                Constant.REWARDPOINT_REDEEM = loyalty_redeem;
                mItemNavigation.setName(SimiTranslator.getInstance()
                        .translate(REWARDPOINT_MENU_ITEM + " ") + Constant.REWARDPOINT_REDEEM);
                return;
            }
        }
    }

    protected void addItemMyAccount(MyAccountDelegate mDelegate) {
        SimiMenuRowComponent wishlistRowComponent = new SimiMenuRowComponent();
        wishlistRowComponent.setIcon("plugin_reward_ic_myacc");
        wishlistRowComponent.setLabel(SimiTranslator.getInstance().translate("Reward Point"));
        wishlistRowComponent.setmCallBack(new MenuRowCallBack() {
            @Override
            public void onClick() {
                MyRewardFragment fragmentRe = new MyRewardFragment();
                SimiManager.getIntance().replacePopupFragment(fragmentRe);
            }
        });
        mDelegate.addItemRow(wishlistRowComponent.createView());
    }

}
