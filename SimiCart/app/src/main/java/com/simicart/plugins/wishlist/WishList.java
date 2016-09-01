package com.simicart.plugins.wishlist;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.component.SimiMenuRowComponent;
import com.simicart.core.base.component.callback.MenuRowCallBack;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.block.ProductMorePluginBlock;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.MyAccountDelegate;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionButton;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionsMenu;
import com.simicart.plugins.wishlist.block.MyWistListBlock;
import com.simicart.plugins.wishlist.common.WishListConstants;
import com.simicart.plugins.wishlist.controller.ControllerAddWishList;
import com.simicart.plugins.wishlist.entity.ButtonAddWishList;
import com.simicart.plugins.wishlist.entity.ProductWishList;
import com.simicart.plugins.wishlist.fragment.MyWishListFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class WishList {
    public static final String MY_WISHLIST = "My WishList";
    public String MY_WISH_LIST = WishListConstants.MY_WISHLIST;
    public static String MY_WISH_LIST_OLD = WishListConstants.MY_WISHLIST;
    public static ButtonAddWishList bt_addWish;
    public static String product_ID = "";
    protected ArrayList<ItemNavigation> mItems;
    protected TextView tv_qtyWishList;
    protected Context mContext;
    protected HashMap<String, String> mFragments;

    public WishList() {

        mContext = SimiManager.getIntance().getCurrentActivity();

        // register event: add navigation item to slide menu
        BroadcastReceiver addItemReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable("entity");
                mItems = (ArrayList<ItemNavigation>) data.getData().get(KeyData.SLIDE_MENU.LIST_ITEMS);
                mFragments = (HashMap<String, String>) data.getData().get(KeyData.SLIDE_MENU.LIST_FRAGMENTS);
                if(isExistWishList() == false) {
                    addItemToSlideMenu();
                }
            }
        };
        SimiEvent.registerEvent(KeyEvent.SLIDE_MENU_EVENT.ADD_ITEM_RELATED_PERSONAL, addItemReceiver);

        // register event: add button to more
        BroadcastReceiver addButtonReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra(Constants.DATA);
                SimiData data = bundle.getParcelable(Constants.ENTITY);
                View view = (View) data.getData().get(KeyData.SIMI_BLOCK.VIEW);
                SimiBlock mBlock = (SimiBlock) data.getData().get(KeyData.SIMI_BLOCK.BLOCK);
                Product product = (Product) ((ProductMorePluginBlock) mBlock).getProduct();
                ArrayList<FloatingActionButton> mListButtons = ((ProductMorePluginBlock) mBlock).getListButton();
                addButtonMyWishList(product, view, mListButtons);
            }
        };
        SimiEvent.registerEvent(KeyEvent.WISH_LIST.WISH_LIST_ADD_BUTTON_MORE, addButtonReceiver);

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

    protected boolean isExistWishList() {
        for (ItemNavigation item : mItems) {
            if (item.getName().equals(SimiTranslator.getInstance().translate(
                    MY_WISHLIST))) {
                return true;
            }
        }
        return false;
    }

    protected void addItemToSlideMenu() {
        ItemNavigation mItemNavigation = new ItemNavigation();
        mItemNavigation.setType(ItemNavigation.TypeItem.PLUGIN);
        Drawable icon = SimiManager
                .getIntance()
                .getCurrentActivity()
                .getResources()
                .getDrawable(
                        Rconfig.getInstance().drawable("plugins_wishlist_iconmenu"));
        icon.setColorFilter(AppColorConfig.getInstance().getMenuIconColor(),
                PorterDuff.Mode.SRC_ATOP);
        mItemNavigation.setName(MY_WISHLIST);
        mItemNavigation.setIcon(icon);
        mItems.add(mItemNavigation);

        Fragment fragment = MyWishListFragment.newInstance();
        mFragments.put(mItemNavigation.getName(),
                fragment.getClass().getName());
    }

    protected void addButtonMyWishList(Product product, View view, ArrayList<FloatingActionButton> mListButtons) {
        FloatingActionsMenu mMultipleActions = (FloatingActionsMenu) view.findViewById(Rconfig.getInstance().id("more_plugins_action"));
        bt_addWish = new ButtonAddWishList(mContext);
        for (int i = 0; i < mListButtons.size(); i++) {
            mMultipleActions.removeButton(mListButtons.get(i));
        }
        mListButtons.add((mListButtons.size() - 1), bt_addWish.getImageAddWishList());
        for (int i = 0; i < mListButtons.size(); i++) {
            mMultipleActions.addButton(mListButtons.get(i));
        }

        ProductWishList productWishList = new ProductWishList(product);

        if (!productWishList.getWishlist_item_id().equals("0")) {
            bt_addWish.setEnable(true);
        }
        ControllerAddWishList controllerAddWishList = new ControllerAddWishList(
                mContext, bt_addWish, productWishList);
        MyWistListBlock mDelegate = new MyWistListBlock(view, mContext);
        controllerAddWishList.setDelegate(mDelegate);
        controllerAddWishList.onAddToWishList();
    }

    protected void addItemMyAccount(MyAccountDelegate mDelegate) {
        SimiMenuRowComponent wishlistRowComponent = new SimiMenuRowComponent();
        wishlistRowComponent.setIcon("plugins_wishlist_iconadd2");
        wishlistRowComponent.setLabel(SimiTranslator.getInstance().translate(MY_WISH_LIST));
        wishlistRowComponent.setmCallBack(new MenuRowCallBack() {
            @Override
            public void onClick() {
                MyWishListFragment fragment = MyWishListFragment.newInstance();
                if (DataLocal.isTablet) {
                    SimiManager.getIntance().replacePopupFragment(fragment);
                } else {
                    SimiManager.getIntance().replaceFragment(fragment);
                }
            }
        });
        mDelegate.addItemRow(wishlistRowComponent.createView());
    }

}
