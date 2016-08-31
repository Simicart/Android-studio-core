package com.simicart.plugins.zopimchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.entity.ItemNavigation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by truongtechno on 05/04/2016.
 */
public class ZopimChat {

    Context mContext;
    ArrayList<ItemNavigation> mItems;
    protected HashMap<String, String> mFragments;

    public ZopimChat() {

        mContext = SimiManager.getIntance().getCurrentActivity();
        configZopim();

        // register event: add navigation item to slide menu
        BroadcastReceiver addItemReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable("entity");
                mItems = (ArrayList<ItemNavigation>) data.getData().get(KeyData.SLIDE_MENU.LIST_ITEMS);
                mFragments = (HashMap<String, String>) data.getData().get(KeyData.SLIDE_MENU.LIST_FRAGMENTS);
                if (ConstantZopim.ZOPIM_ENABLE.equals("1") && !DataLocal.isTablet) {
                    addItemToSlideMenu();
                }
            }
        };
        SimiEvent.registerEvent(KeyEvent.SLIDE_MENU_EVENT.ADD_ITEM_MORE, addItemReceiver);

        // register event: click item from left menu
        BroadcastReceiver onClickItemReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData mData = bundle.getParcelable(Constants.ENTITY);
                String item_name = (String) mData.getData().get(KeyData.SLIDE_MENU.ITEM_NAME);
                clickItemLeftMenu(item_name);
            }
        };
        SimiEvent.registerEvent(KeyEvent.SLIDE_MENU_EVENT.CLICK_ITEM, onClickItemReceiver);

        // register event: add icon to menutop
        BroadcastReceiver blockReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                View view = (View) data.getData().get(KeyData.SIMI_BLOCK.VIEW);
                if (ConstantZopim.ZOPIM_ENABLE.equals("1")) {
                    addIconMenutop(view);
                }
            }
        };
        SimiEvent.registerEvent(KeyEvent.ZOPIM.ZOPIM_ADD_ICON_TOP_MENU, blockReceiver);

    }

    protected void addItemToSlideMenu() {
        removeExistZopim();
        ItemNavigation item = new ItemNavigation();
        item.setType(ItemNavigation.TypeItem.PLUGIN);
        item.setName(ConstantZopim.ZOPIM);
        int id_icon = Rconfig.getInstance().drawable("ic_livechat");
        Drawable icon = mContext.getResources().getDrawable(id_icon);
        icon.setColorFilter(Color.parseColor("#ffffff"),
                PorterDuff.Mode.SRC_ATOP);
        item.setIcon(icon);
        mItems.add(item);
    }

    protected void removeExistZopim() {
        for (ItemNavigation item : mItems) {
            if (item.getName().equals(SimiTranslator.getInstance().translate(
                    ConstantZopim.ZOPIM))) {
                mItems.remove(item);
            }
        }
    }

    protected void clickItemLeftMenu(String itemName) {
        if (itemName.equals(ConstantZopim.ZOPIM)) {
            Intent intent = new Intent(SimiManager.getIntance().getCurrentActivity(), SimiChatActivity.class);
            SimiManager.getIntance().getCurrentActivity().startActivity(intent);
        }
    }

    protected void addIconMenutop(View rootView) {
        try {
            LinearLayout layoutSearch = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("layout_plugin"));

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    100, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            RelativeLayout layout = new RelativeLayout(SimiManager.getIntance()
                    .getCurrentActivity());
            layout.setPadding(0, 0, 0, 14);
            layout.setLayoutParams(layoutParams);
            layoutSearch.addView(layout);

            ImageView imageView = new ImageView(SimiManager.getIntance()
                    .getCurrentActivity());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    Utils.getValueDp(25), Utils.getValueDp(25));
            params.setMargins(0, 0, 5, 0);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            imageView.setLayoutParams(params);
            imageView.setImageResource(Rconfig.getInstance().drawable(
                    "ic_livechat"));
            imageView.setColorFilter(AppColorConfig.getInstance().getMenuIconColor());
            layout.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SimiManager.getIntance().getCurrentActivity(), SimiChatActivity.class);
                    SimiManager.getIntance().getCurrentActivity().startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e("Exception:", e.getMessage());
        }
    }

    protected void configZopim() {
        try {
            JSONObject storeViewObj = AppStoreConfig.getInstance().getJSONObject();
            if(storeViewObj.has("zopim_config")) {
                JSONObject object = storeViewObj.getJSONObject("zopim_config");
                if (object.has("enable")) {
                    ConstantZopim.ZOPIM_ENABLE = object.getString("enable");
                }
                if (object.has("phone")) {
                    ConstantZopim.ZOPIM_PHONE = object.getString("phone");
                }
                if (object.has("account_key")) {
                    ConstantZopim.ZOPIM_ACCOUNT_KEY = object.getString("account_key");
                }
                if (object.has("email")) {
                    ConstantZopim.ZOPIM_EMAIL = object.getString("email");
                }
                if (object.has("show_profile")) {
                    ConstantZopim.ZOPIM_SHOWPROFILE = object.getString("show_profile");
                }
                if (object.has("name")) {
                    ConstantZopim.ZOPIM_NAME = object.getString("name");
                }
            }
        } catch (Exception e) {
        }
    }


}
