package com.simicart.plugins.rewardpoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.core.slidemenu.entity.ItemNavigation.TypeItem;
import com.simicart.plugins.rewardpoint.fragment.MyRewardFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RewardPoint {

    private Context mContext;
    private RelativeLayout rlt_layout_rewardpoint;
    private static ArrayList<ItemNavigation> mItems;
    protected HashMap<String, String> mFragments;
    public static String REWARDPOINT_MENU_ITEM = "My Rewards";
    // cart
    RelativeLayout rlt_layout_cart;
    // review order
    private static RelativeLayout rlt_layout_reviewOrder;
    LinearLayout ll_layout_price;
    protected int mTextSize = 16;
    protected String mColorLabel = "#000000";
    protected String mColorPrice = "red";

    TextView txt_process;
    int point_step = 0;
    static int loy_spend = 0;

    // private static JSONObject jsonObject = new JSONObject();

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
                addItemToSlideMenu();
            }
        };
        SimiEvent.registerEvent(KeyEvent.SLIDE_MENU_EVENT.ADD_ITEM_RELATED_PERSONAL, addItemReceiver);

        // register event: remove navigation item to slide menu
        BroadcastReceiver removeItemReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable("entity");
                mItems = (ArrayList<ItemNavigation>) data.getData().get(KeyData.SLIDE_MENU.LIST_ITEMS);
                mFragments = (HashMap<String, String>) data.getData().get(KeyData.SLIDE_MENU.LIST_FRAGMENTS);
                for (ItemNavigation mItemNavigation : mItems) {
                    if (mItemNavigation.getName().equals(REWARDPOINT_MENU_ITEM)) {
                        mFragments.remove(mItemNavigation.getName());
                        mItems.remove(mItemNavigation);
                    }
                }
            }
        };
        SimiEvent.registerEvent(KeyEvent.SLIDE_MENU_EVENT.REMOVE_ITEM, removeItemReceiver);

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

        // register event: add item to cart
        BroadcastReceiver addItemReviewOrderReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                View view = (View) data.getData().get(KeyData.SIMI_BLOCK.VIEW);
                SimiCollection collection = (SimiCollection) data.getData().get(KeyData.SIMI_BLOCK.COLLECTION);
                addItemToCart(view, collection);
            }
        };
        SimiEvent.registerEvent(KeyEvent.REWARD_POINT_EVENT.REWARD_ADD_ITEM_CART, addItemReviewOrderReceiver);

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
            ll_reward_card.setVisibility(View.GONE);
        }

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
        paramsImageview.gravity = Gravity.CENTER_VERTICAL;
        paramsTextview.setMargins(0, Utils.getValueDp(5), 0,
                Utils.getValueDp(5));
        TextView textView = new TextView(mContext);
        textView.setTextColor(Color.parseColor("#ff033e"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setText("");
        String text = label;
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#ff033e"));
        textView.setLayoutParams(paramsTextview);
        ll_reward_card.addView(textView);
    }

}
