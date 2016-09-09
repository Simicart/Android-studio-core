package com.simicart.core.slidemenu.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.categorydetail.fragment.CategoryDetailFragment;
import com.simicart.core.cms.entity.Cms;
import com.simicart.core.cms.fragment.CMSFragment;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.fragment.MyAccountFragment;
import com.simicart.core.customer.fragment.OrderHistoryFragment;
import com.simicart.core.customer.fragment.SignInFragment;
import com.simicart.core.home.fragment.HomeFragment;
import com.simicart.core.setting.fragment.SettingAppFragment;
import com.simicart.core.slidemenu.delegate.CloseSlideMenuDelegate;
import com.simicart.core.slidemenu.delegate.SlideMenuDelegate;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.core.slidemenu.entity.ItemNavigation.TypeItem;
import com.simicart.core.splashscreen.entity.DeepLinkEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class PhoneSlideMenuController {

    private final String HOME = "Home";
    private final String CATEGORY = "Category";
    private final String ORDER_HISTORY = "Order History";
    private final String MORE = "More";
    private final String SETTING = "Setting";
    protected HashMap<String, String> mPluginFragment;
    protected OnItemClickListener mListener;
    protected OnClickListener onClickPersonal;
    protected ArrayList<ItemNavigation> mItems = new ArrayList<ItemNavigation>();
    protected Context mContext;
    protected SlideMenuDelegate mDelegate;
    protected int DEFAULT_POSITION = 0;
    protected CloseSlideMenuDelegate mCloseDelegate;
    private boolean check_keyboard_first;
    protected ArrayList<ItemNavigation> mItemsAccount;

    public void setCloseDelegate(CloseSlideMenuDelegate delegate) {
        mCloseDelegate = delegate;
    }

    public OnItemClickListener getListener() {
        return mListener;
    }

    public void setListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public OnClickListener getOnClickPersonal() {
        return onClickPersonal;
    }

    public void closeSlideMenuTablet() {
        if (mCloseDelegate != null)
            mCloseDelegate.closeSlideMenu();
    }

    public PhoneSlideMenuController(SlideMenuDelegate delegate, Context context) {
        mDelegate = delegate;
        mContext = context;
        mItems = new ArrayList<ItemNavigation>();
        mPluginFragment = new HashMap<String, String>();
    }

    public void create() {

        mListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onNaviagte(position);
            }

        };

        onClickPersonal = new OnClickListener() {

            @Override
            public void onClick(View v) {
                SimiFragment fragment = null;
                mCloseDelegate.closeSlideMenu();
                if (DataPreferences.isSignInComplete()) {
                    // profile
                    fragment = MyAccountFragment.newInstance();
                } else {
                    // sign in
                    HashMap<String, Object> hmData = new HashMap<>();
                    hmData.put("is_checkout", false);
                    SimiData data = new SimiData(hmData);
                    fragment = SignInFragment.newInstance(data);
                }
                SimiManager.getIntance().replacePopupFragment(fragment);
            }
        };

        initial();
    }

    private void initial() {
        initDataAdapter();
        if (null != mDelegate) {
            mDelegate.setAdapter(mItems);
        }
        // open home page
        if (DeepLinkEntity.getInstance().getName().equals("")) {
            onNaviagte(DEFAULT_POSITION);
        } else {
            openDeepLink();
        }
    }

    private void openDeepLink() {
        SimiFragment fragment = null;

        if (!DeepLinkEntity.getInstance().getID().equals("")
                && DeepLinkEntity.getInstance().getType() != 0) {
            if (DeepLinkEntity.getInstance().getType() == 1) {
                Log.e("Open deep link", "1");
                ArrayList<String> listID = new ArrayList<String>();
                listID.add(DeepLinkEntity.getInstance().getID());
//                fragment = ProductDetailParentFragment.newInstance(
//                        DeepLinkEntity.newInstance().getID(), listID);
//                SimiManager.getIntance().replaceFragment(fragment);
            } else if (DeepLinkEntity.getInstance().getType() == 2) {
                Log.e("Open deep link", "2");
                if (DeepLinkEntity.getInstance().isHasChild() == true) {
                    if (DataLocal.isTablet) {
//                        fragment = CategoryFragment.newInstance(
//                                DeepLinkEntity.newInstance().getID(),
//                                DeepLinkEntity.newInstance().getName());
//                        CateSlideMenuFragment.getIntance()
//                                .replaceFragmentCategoryMenu(fragment);
//                        CateSlideMenuFragment.getIntance().openMenu();
                    } else {
//                        fragment = CategoryFragment.newInstance(
//                                DeepLinkEntity.newInstance().getID(),
//                                DeepLinkEntity.newInstance().getName());
//                        SimiManager.getIntance().replaceFragment(fragment);
                    }
                } else {
//                    fragment = ProductListFragment.newInstance(
//                            DeepLinkEntity.newInstance().getID(), DeepLinkEntity.newInstance().getName(), null, null, null);
//                    SimiManager.getIntance().replaceFragment(fragment);
                }
            }
        }
        DeepLinkEntity.getInstance().setID("");
        DeepLinkEntity.getInstance().setName("");
        DeepLinkEntity.getInstance().setHasChild(false);
        DeepLinkEntity.getInstance().setType(0);
    }

    public void initDataAdapter() {
        addPersonal();
        addHome();
        addCategory();
        if (DataPreferences.isSignInComplete()) {
            addItemRelatedPersonal();
        }
        addMoreItems();
        addSetting();

    }

    public void addPersonal() {

        String name = "";

        if (DataPreferences.isSignInComplete()) {
            name = DataPreferences.getUsername();
        } else {
            name = SimiTranslator.getInstance().translate("Sign in");
            removeItemRelatedPersonal();
        }
        mDelegate.setUpdateSignIn(name);
        mDelegate.setAdapter(mItems);
    }

    public void addItemRelatedPersonal() {

        // order history
        int index = checkElement(ORDER_HISTORY);
        mItemsAccount = new ArrayList<>();
        if (index == -1) {
            ItemNavigation item = new ItemNavigation();
            item.setType(TypeItem.NORMAL);
            item.setName(ORDER_HISTORY);
            int id_icon = Rconfig.getInstance().drawable(
                    "ic_menu_order_history");
            Drawable icon = mContext.getResources().getDrawable(id_icon);
            icon.setColorFilter(Color.parseColor("#ffffff"),
                    PorterDuff.Mode.SRC_ATOP);
            item.setIcon(icon);
            mItemsAccount.add(item);

            dispatchItemEvent(KeyEvent.SLIDE_MENU_EVENT.ADD_ITEM_RELATED_PERSONAL, mItemsAccount);

            int index_category = checkElement(CATEGORY);
            if (DataLocal.isTablet) {
                index_category = checkElement(HOME);
            }
            insertItemAfter(index_category, mItemsAccount);

        }

    }

    public void removeItemRelatedPersonal() {

        if(mItemsAccount != null) {
            for (ItemNavigation item : mItemsAccount) {
                int index = checkElement(item.getName());
                if (index != -1) {
                    mItems.remove(index);
                }
            }
        }

        // order history
//        int index = checkElement(ORDER_HISTORY);
//        if (index != -1) {
//            mItems.remove(index);
//        }

        //dispatchItemEvent(KeyEvent.SLIDE_MENU_EVENT.REMOVE_ITEM);
    }

    public void addHome() {
        int index = checkElement(HOME);
        if (index == -1) {
            ItemNavigation item = new ItemNavigation();
            item.setType(TypeItem.NORMAL);
            item.setName(HOME);
            int id_icon = Rconfig.getInstance().drawable("ic_menu_home");
            Drawable icon = mContext.getResources().getDrawable(id_icon);
            icon.setColorFilter(Color.parseColor("#ffffff"),
                    PorterDuff.Mode.SRC_ATOP);
            item.setIcon(icon);

            mItems.add(item);
        }
    }

    public void addCategory() {
        if (!DataLocal.isTablet) {
            int index = checkElement(CATEGORY);
            if (index == -1) {
                ItemNavigation item = new ItemNavigation();
                item.setType(TypeItem.NORMAL);
                item.setName(CATEGORY);
                int id_icon = Rconfig.getInstance()
                        .drawable("ic_menu_category");
                Drawable icon = mContext.getResources().getDrawable(id_icon);
                icon.setColorFilter(Color.parseColor("#ffffff"),
                        PorterDuff.Mode.SRC_ATOP);
                item.setIcon(icon);

                mItems.add(item);
            }
        }
    }

    public void addMoreItems() {
        // more
        ItemNavigation item = new ItemNavigation();
        item.setType(TypeItem.NORMAL);
        item.setSparator(true);
        item.setName(MORE);
        mItems.add(item);

        // event for add barcode to slidemenu
        dispatchItemEvent(KeyEvent.SLIDE_MENU_EVENT.ADD_ITEM_MORE, mItems);

//        SlideMenuData slideMenuData = new SlideMenuData();
//        slideMenuData.setItemNavigations(mItems);
//        slideMenuData.setPluginFragment(mPluginFragment);
//        EventSlideMenu eventSlideMenu = new EventSlideMenu();
//        eventSlideMenu.dispatchEvent("com.simicart.add.navigation.more",
//                slideMenuData);
//
//        eventSlideMenu.dispatchEvent("com.simicart.add.navigation.more.qrbarcode",
//                slideMenuData);
//        eventSlideMenu.dispatchEvent("com.simicart.add.navigation.more.locator",
//                slideMenuData);
//        eventSlideMenu.dispatchEvent("com.simicart.add.navigation.more.contactus",
//                slideMenuData);

        // CMS
        addCMS();

    }

    public void addCMS() {
        if ((null != DataLocal.listCms) && (DataLocal.listCms.size() > 0)) {
            for (Cms cms : DataLocal.listCms) {
                ItemNavigation item = new ItemNavigation();
                String name = cms.getTitle();
                if (null != name && !name.equals("null")) {
                    item.setName(name);
                }
                String url = cms.getIcon();
                if (null != url && !url.equals("null")) {
                    item.setUrl(url);
                }
                item.setType(TypeItem.CMS);
                mItems.add(item);
            }
        }
    }

    public void addSetting() {
        ItemNavigation item = new ItemNavigation();
        item.setExtended(true);
        item.setType(TypeItem.NORMAL);
        item.setName(SETTING);
        int id_icon = Rconfig.getInstance().drawable("ic_menu_setting");
        Drawable icon = mContext.getResources().getDrawable(id_icon);
        icon.setColorFilter(Color.parseColor("#ffffff"),
                PorterDuff.Mode.SRC_ATOP);
        item.setShowPopup(true);
        item.setIcon(icon);

        mItems.add(item);
    }

    public void onNaviagte(int position) {
        SimiManager.getIntance().showCartLayout(true);
        ItemNavigation item = mItems.get(position);
        if (null != item) {
            if (!item.isSparator()) {
                // event click barcode leftmenu
                dispatchOnClickEvent(KeyEvent.SLIDE_MENU_EVENT.CLICK_ITEM, item.getName());
                TypeItem type = item.getType();
                SimiFragment fragment = null;
                if (type == TypeItem.NORMAL) {
                    fragment = navigateNormal(item);
                } else if (type == TypeItem.PLUGIN) {
                    fragment = navigatePlugin(item);
//                    fragment.setShowPopup(item.isShowPopup());
                } else if (type == TypeItem.CMS) {
                    fragment = navigateCMS(item);
                }
                if (null != fragment) {
                    if (!DataLocal.isTablet) {
                        // replace fragment for phone
                        SimiManager.getIntance().replaceFragment(fragment);
                        if (check_keyboard_first) {
                            SimiManager.getIntance().hideKeyboard();
                        }
                        check_keyboard_first = true;
                    } else {
                        // replace for tablet
                        if(item.isShowPopup()) {
                            SimiManager.getIntance().replacePopupFragment(fragment);
                        } else {
                            SimiManager.getIntance().replaceFragment(fragment);
                        }
                    }

                }
                mDelegate.onSelectedItem(position);
                if (mCloseDelegate != null) {
                    mCloseDelegate.closeSlideMenu();
                }
            }
        }
    }

    public SimiFragment navigateNormal(ItemNavigation item) {
        SimiFragment fragment = null;
        String name = item.getName();
        HashMap<String,Object> hm = null;
        switch (name) {
            case "Home":
                //fragment = HomeFragment.newInstance();
                SimiManager.getIntance().openHomePage();
                break;
            case "Category":
                hm = new HashMap<>();
                hm.put(KeyData.CATEGORY.CATEGORY_NAME, "all categories");
                SimiManager.getIntance().openCategory(hm);

                break;
            case "Order History":
                fragment = OrderHistoryFragment.newInstance();
                break;
            case "Setting":
                fragment = SettingAppFragment.newInstance();
//                fragment.setShowPopup(true);
                break;
            default:
                break;
        }

        return fragment;
    }

    public SimiFragment navigatePlugin(ItemNavigation item) {
        SimiFragment fragment = null;
        String name = item.getName();
        if (null != name) {
            for (String key : mPluginFragment.keySet()) {
                if (name.contains(key)) {
                    String fullname = mPluginFragment.get(key);
                    fragment = (SimiFragment) Fragment.instantiate(mContext,
                            fullname);
                }
            }
        }
        return fragment;
    }

    public SimiFragment navigateCMS(ItemNavigation item) {

        SimiFragment fragment = null;
        String name = item.getName();
        for (Cms cms : DataLocal.listCms) {
            if (name.equals(cms.getTitle())) {
                String content = cms.getContent();
                HashMap<String,Object> hm = new HashMap<>();
                hm.put(KeyData.CMS_PAGE.CONTENT, content);
                SimiManager.getIntance().openCMSPage(hm);
            }
        }
        // initial CMSFragment by using content field.

        return fragment;
    }

    protected int checkElement(String name) {
        if (null != mItems || mItems.size() > 0) {
            for (int i = 0; i < mItems.size(); i++) {
                ItemNavigation item = mItems.get(i);
                if (item.getName().equals(name)) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    protected ItemNavigation getElement(String name) {
        if (null != mItems || mItems.size() > 0) {
            for (int i = 0; i < mItems.size(); i++) {
                ItemNavigation item = mItems.get(i);
                if (item.getName().equals(name)) {
                    return item;
                }
            }
            return null;
        }
        return null;
    }

    public boolean removeItemElement(String name) {
        int position = checkElement(name);
        if (position > -1) {
            mItems.remove(position);
            return true;
        }

        return false;
    }

    public boolean replaceItemElement(String nameOldElement,
                                      ItemNavigation newItem) {
        int position = checkElement(nameOldElement);
        if (position > -1) {
            mItems.set(position, newItem);
            return true;
        }

        return false;
    }

    public boolean insertItemAfter(int index,
                                   ArrayList<ItemNavigation> mItemsAccount) {
        if (index != -1 && mItemsAccount.size() > 0) {
            ArrayList<ItemNavigation> list1 = new ArrayList<ItemNavigation>();
            ArrayList<ItemNavigation> list2 = new ArrayList<ItemNavigation>();

            for (int i = 0; i <= index; i++) {
                list1.add(mItems.get(i));
            }

            for (int i = index + 1; i < mItems.size(); i++) {
                list2.add(mItems.get(i));
            }

            // if (list1.size() == 0 || list2.size() == 0) {
            // return false;
            // }

            mItems.clear();

            for (int i = 0; i < list1.size(); i++) {
                mItems.add(list1.get(i));
            }
            for (ItemNavigation itemNavigation : mItemsAccount) {
                mItems.add(itemNavigation);
            }
            for (int i = 0; i < list2.size(); i++) {
                mItems.add(list2.get(i));
            }

            return true;

        }

        return false;
    }

    public void updateSignIn() {
        String name = SimiTranslator.getInstance().translate("My Account");

        if (DataPreferences.isSignInComplete()) {
            name = DataPreferences.getUsername();
            addItemRelatedPersonal();
        } else {
            name = SimiTranslator.getInstance().translate("Sign in");
            removeItemRelatedPersonal();
        }
        mDelegate.setUpdateSignIn(name);
        mDelegate.setAdapter(mItems);
    }

    protected void dispatchItemEvent(String event_name, ArrayList<ItemNavigation> mItemsAccount) {
        Intent intent = new Intent(event_name);
        Bundle bundle = new Bundle();
//        SimiEventLeftMenuEntity leftEntity = new SimiEventLeftMenuEntity();
//        leftEntity.setMenu(mMenu);
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put(KeyData.SLIDE_MENU.LIST_ITEMS, mItemsAccount);
        hmData.put(KeyData.SLIDE_MENU.LIST_FRAGMENTS, mPluginFragment);
        SimiData data = new SimiData(hmData);
        bundle.putParcelable("entity", data);
        intent.putExtra("data", bundle);
        LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);
    }

    protected void dispatchOnClickEvent(String event_name, String item_name) {
        Intent intent = new Intent(event_name);
        Bundle bundle = new Bundle();
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put(KeyData.SLIDE_MENU.ITEM_NAME, item_name);
        SimiData data = new SimiData(hmData);
        bundle.putParcelable("entity", data);
        intent.putExtra("data", bundle);
        LocalBroadcastManager.getInstance(mContext).sendBroadcastSync(intent);
    }

}
