package com.simicart.core.base.manager;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.simicart.MainActivity;
import com.simicart.core.base.event.fragment.SimiEventFragmentEntity;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.network.request.SimiRequestQueue;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.categorydetail.fragment.CategoryDetailFragment;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.checkout.fragment.ReviewOrderFragment;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.fragment.AddressBookDetailFragment;
import com.simicart.core.customer.fragment.AddressBookFragment;
import com.simicart.core.customer.fragment.CustomerFragment;
import com.simicart.core.customer.fragment.ListOfChoiceFragment;
import com.simicart.core.home.fragment.HomeFragment;
import com.simicart.core.menutop.controller.MenuTopController;
import com.simicart.core.slidemenu.controller.CategorySlideMenuController;
import com.simicart.core.slidemenu.controller.PhoneSlideMenuController;
import com.simicart.core.slidemenu.fragment.SlideMenuFragment;
import com.simicart.core.splashscreen.SplashActivity;
import com.simicart.core.style.FragmentDialogHandle;

import java.util.HashMap;
import java.util.List;

public class SimiManager {

    private Activity mCurrentActivity;
    private FragmentManager mManager;
    private static SimiManager instance;
    private PhoneSlideMenuController mSlideMenuController;
    private MenuTopController mMenuTopController;
    private FragmentManager mChildFragmentManager;
    private CategorySlideMenuController mCategorySlideMenuController;
    private SlideMenuFragment mSlideMenu;

    protected SimiRequestQueue mRequestQueue;
    protected boolean isRefreshCart = true;
    protected int mQtyCartPrevious;


    private SimiManager() {
        mRequestQueue = new SimiRequestQueue();
        mRequestQueue.start();
    }

    public static SimiManager getIntance() {
        if (null == instance) {
            instance = new SimiManager();
        }

        return instance;
    }

    public void openHomePage() {
        HomeFragment fragment = HomeFragment.newInstance();
        replaceFragment(fragment);
    }

    public void openCategory(HashMap<String, Object> hm) {
        SimiData data = new SimiData(hm);
        CategoryFragment fragment = CategoryFragment.newInstance(data);
        replaceFragment(fragment);
    }

    public void openCategoryDetail(HashMap<String, Object> hm) {
        SimiData data = new SimiData(hm);
        CategoryDetailFragment fragment = CategoryDetailFragment.newInstance(data);
        replaceFragment(fragment);
    }

    public void openAddressBook(HashMap<String, Object> hm) {
        SimiData data = new SimiData(hm);
        AddressBookFragment fragment = AddressBookFragment.newInstance(data);
        replaceFragment(fragment);
    }

    public void openAddressBookDetail(HashMap<String, Object> hm) {
        SimiData data = new SimiData(hm);
        AddressBookDetailFragment fragment = AddressBookDetailFragment.newInstance(data);
        replaceFragment(fragment);
    }

    public void openListOfChoice(HashMap<String, Object> hm) {
        SimiData data = new SimiData(hm);
        ListOfChoiceFragment fragment = ListOfChoiceFragment.newInstance(data);
        replaceFragment(fragment);
    }

    public void openReviewOrder(HashMap<String, Object> hm) {
        SimiData data = new SimiData(hm);
        ReviewOrderFragment fragment = ReviewOrderFragment.newInstance(data);
        replaceFragment(fragment);
    }


    public void openProductDetail(HashMap<String, Object> hm) {
        SimiData data = new SimiData(hm);
        ProductDetailParentFragment fragment = ProductDetailParentFragment.newInstance(data);
        replaceFragment(fragment);
    }

    public void  openCustomerPage(HashMap<String,Object> hm){
        SimiData data = new SimiData(hm);
        CustomerFragment fragment = CustomerFragment.newInstance(data);
        replaceFragment(fragment);
    }


    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void hideKeyboard() {
        try {
            if (mCurrentActivity != null) {
                InputMethodManager imm = (InputMethodManager) mCurrentActivity
                        .getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mCurrentActivity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        } catch (Exception e) {
        }
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }


    public FragmentManager getManager() {
        return mManager;
    }

    public void setManager(FragmentManager manager) {
        mManager = manager;
    }

    public void setSlideMenuController(PhoneSlideMenuController controller) {
        mSlideMenuController = controller;
    }

    public void setMenuTopController(MenuTopController controller) {
        mMenuTopController = controller;
    }

    public void onUpdateCartQty(String qty) {
        int i_qty = 0;
        try {
            qty = qty.trim();
            i_qty = Integer.parseInt(qty);

        } catch (Exception e) {
            Log.e("SimiManager ", "onUpdateCartQty " + e.getMessage());
        }


        if (mQtyCartPrevious != i_qty) {
            mQtyCartPrevious = i_qty;
            Log.e("SimiManager ", "onUpdateCartQty  TRUEEEEEEEEEEE");
            isRefreshCart = true;
        } else {
            isRefreshCart = false;
        }

        mMenuTopController.updateCartQty(qty);
    }

    public boolean isRereshCart() {
        return isRefreshCart;
    }

    public void setIsRefreshCart(boolean is_refresh_cart) {
        isRefreshCart = is_refresh_cart;
    }

    public void showCartLayout(boolean show) {
        if (mMenuTopController != null) {
            mMenuTopController.showCartLayout(show);
        }
    }

    public void onUpdateItemSignIn() {
        if (null != mSlideMenuController) {
            mSlideMenuController.updateSignIn();
        }
    }

    public void setChildFragment(FragmentManager childFragment) {
        mChildFragmentManager = childFragment;
    }

    public FragmentManager getChilFragmentManager() {
        return mChildFragmentManager;
    }

    public void toMainActivity() {
        Intent i = new Intent(mCurrentActivity, MainActivity.class);
        Bundle extras = mCurrentActivity.getIntent().getExtras();
        if (extras != null) {
            i.putExtras(extras);
        }
        mCurrentActivity.startActivity(i);
        mCurrentActivity.finish();
    }

    public void changeStoreView() {
        Intent intent = new Intent(mCurrentActivity.getApplicationContext(),
                SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mCurrentActivity.finish();
        mCurrentActivity.startActivity(intent);
    }

    public Fragment getCurrentFragment() {
        List<Fragment> fragments = mManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }

    public SimiFragment eventFragment(SimiFragment fragment) {
        return eventFragment(fragment, "createFragment");
    }

    public SimiFragment eventFragment(SimiFragment fragment, String method) {

        String nameFragment = fragment.getClass().getName();
        Intent intent = new Intent(nameFragment);
        SimiEventFragmentEntity entity = new SimiEventFragmentEntity();
        entity.setmFragment(fragment);
        if (!Utils.validateString(method)) {
            method = "";
        }
        Bundle bundle = new Bundle();
        HashMap<String,Object> hmData = new HashMap<>();
        hmData.put(KeyData.SIMI_FRAGMENT.FRAGMENT, entity);
        hmData.put(KeyData.SIMI_FRAGMENT.METHOD, method);
        bundle.putParcelable(Constants.ENTITY, new SimiData(hmData));
        intent.putExtra(Constants.DATA, bundle);
        LocalBroadcastManager.getInstance(mCurrentActivity).sendBroadcastSync(intent);
        fragment = entity.getFragment();
        return fragment;

    }

    public void addFragment(SimiFragment fragment) {
        if (null != mManager) {
            String nameFragment = fragment.getClass().getName();
            FragmentTransaction ft = mManager.beginTransaction();
            ft.setCustomAnimations(
                    Rconfig.getInstance().getId("in_from_right", "anim"),
                    Rconfig.getInstance().getId("out_to_left", "anim"), Rconfig
                            .getInstance().getId("in_from_left", "anim"),
                    Rconfig.getInstance().getId("out_to_right", "anim"));
            ft.replace(Rconfig.getInstance().id("container"), fragment);
            ft.addToBackStack(nameFragment);
            ft.commit();
            mManager.executePendingTransactions();
        }
    }

    public void replaceFragment(SimiFragment fragment) {
        if (null != mManager) {
            String nameFragment = fragment.getClass().getName();
            fragment = eventFragment(fragment);

            boolean isHome = false;
            String screen_name = fragment.getScreenName();
            if (null != screen_name && screen_name.equals("Home Screen")) {
                isHome = true;
            }

            mManager.popBackStack(nameFragment,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = mManager.beginTransaction();

            if (!isHome) {
                ft.setCustomAnimations(
                        Rconfig.getInstance().getId("in_from_right", "anim"),
                        Rconfig.getInstance().getId("out_to_left", "anim"),
                        Rconfig.getInstance().getId("in_from_left", "anim"),
                        Rconfig.getInstance().getId("out_to_right", "anim"));

            }
            ft.replace(Rconfig.getInstance().id("container"), fragment);
            ft.addToBackStack(nameFragment);
            ft.commit();
            if (mSlideMenuController != null) {
                mSlideMenuController.closeSlideMenuTablet();
            }
        }

    }

    public void replaceFragmentRightToLeft(SimiFragment fragment) {
        if (null != mManager) {
            String nameFragment = fragment.getClass().getName();
            Constants.NAME_FRAGMENT = nameFragment;

            fragment = eventFragment(fragment);

            boolean isHome = false;
            String screen_name = fragment.getScreenName();
            if (null != screen_name && screen_name.equals("Home Screen")) {
                isHome = true;
            }

            mManager.popBackStack(nameFragment,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = mManager.beginTransaction();

            if (!isHome) {

                // enter, exit, pop enter, pop exit
                ft.setCustomAnimations(
                        Rconfig.getInstance().getId("in_from_left", "anim"),
                        Rconfig.getInstance().getId("out_to_right", "anim"),
                        Rconfig.getInstance().getId("in_from_right", "anim"),
                        Rconfig.getInstance().getId("out_to_left", "anim"));
            }
            ft.replace(Rconfig.getInstance().id("container"), fragment);
            ft.addToBackStack(nameFragment);
            ft.commit();
            // mManager.executePendingTransactions();
        }
    }

    public void addFragmentSub(final SimiFragment fragment) {

        new Handler().post(new Runnable() {
            public void run() {
                SimiFragment fragment1 = eventFragment(fragment);
                if (DataLocal.isTablet) {
                    FragmentTransaction ft2;
                    if (null != mChildFragmentManager) {
                        ft2 = mChildFragmentManager.beginTransaction();
                    } else {
                        ft2 = mManager.beginTransaction();
                    }
                    ft2.setCustomAnimations(
                            Rconfig.getInstance()
                                    .getId("in_from_right", "anim"),
                            Rconfig.getInstance().getId("out_to_left", "anim"),
                            Rconfig.getInstance().getId("in_from_left", "anim"),
                            Rconfig.getInstance().getId("out_to_right", "anim"));
                    int idContainer2 = Rconfig.getInstance().id("container2");
                    if (idContainer2 != 0) {
                        ft2.replace(idContainer2, fragment1);
                        ft2.commit();
                        mChildFragmentManager.executePendingTransactions();
                    }
                } else {
                    FragmentTransaction ft = mManager.beginTransaction();
                    ft.setCustomAnimations(
                            Rconfig.getInstance()
                                    .getId("in_from_right", "anim"),
                            Rconfig.getInstance().getId("out_to_left", "anim"),
                            Rconfig.getInstance().getId("in_from_left", "anim"),
                            Rconfig.getInstance().getId("out_to_right", "anim"));
                    ft.replace(Rconfig.getInstance().id("container"), fragment1);
                    ft.addToBackStack(null).commit();
                }

            }
        });
    }

    public void removeFragmentSub(SimiFragment fragment) {
        FragmentTransaction ft2;
        if (null != mChildFragmentManager) {
            ft2 = mChildFragmentManager.beginTransaction();
        } else {
            ft2 = mManager.beginTransaction();
        }

        ft2.remove(fragment).commit();
    }

    public void addFragmentSubTab(final SimiFragment fragment) {
        new Handler().post(new Runnable() {
            public void run() {
                SimiFragment fragment1 = eventFragment(fragment);
                if (DataLocal.isTablet) {
                    FragmentTransaction ft2;
                    if (null != mChildFragmentManager) {
                        ft2 = mChildFragmentManager.beginTransaction();
                    } else {
                        ft2 = mManager.beginTransaction();
                    }
                    ft2.setCustomAnimations(
                            Rconfig.getInstance()
                                    .getId("in_from_right", "anim"),
                            Rconfig.getInstance().getId("out_to_left", "anim"),
                            Rconfig.getInstance().getId("in_from_left", "anim"),
                            Rconfig.getInstance().getId("out_to_right", "anim"));

                    int idContainer2 = Rconfig.getInstance().id("container2");
                    if (idContainer2 != 0) {
                        ft2.replace(idContainer2, fragment1);
                        ft2.addToBackStack(fragment.getClass().getName());
                        ft2.commit();
                        mChildFragmentManager.executePendingTransactions();
                    }
                }
            }
        });

    }

    public void replacePopupFragment(SimiFragment fragment) {
        fragment = eventFragment(fragment);
        if (null != mManager) {
            if (DataLocal.isTablet) {
                fragment = eventFragment(fragment);
                Fragment prev = mManager.findFragmentByTag("dialog");
                if (prev != null) {
                    DialogFragment df = (DialogFragment) prev;
                    ImageView back = (ImageView) df.getView().findViewById(
                            Rconfig.getInstance().id("bt_back"));
                    back.setVisibility(View.VISIBLE);
                    FragmentTransaction ft = df.getChildFragmentManager()
                            .beginTransaction();
                    ft.replace(Rconfig.getInstance().id("popup_container"),
                            fragment);
                    String nameFragment = fragment.getClass().getName();
                    ft.addToBackStack(nameFragment).commit();
                } else {
                    removeDialog();
                    DialogFragment newFragment = FragmentDialogHandle
                            .newInstance(fragment);
                    FragmentTransaction ft = mManager.beginTransaction();
                    newFragment.show(ft, "dialog");
                }
            } else {
                replaceFragment(fragment);
            }
        }
    }

    public void addPopupFragment(SimiFragment fragment) {
        fragment = eventFragment(fragment);
        if (null != mManager) {
            if (DataLocal.isTablet) {
                Fragment prev = mManager.findFragmentByTag("dialog");
                if (prev != null) {
                    DialogFragment df = (DialogFragment) prev;
                    ImageView back = (ImageView) df.getView().findViewById(
                            Rconfig.getInstance().id("bt_back"));
                    back.setVisibility(View.VISIBLE);

                    FragmentTransaction ft = df.getChildFragmentManager()
                            .beginTransaction();
                    ft.add(Rconfig.getInstance().id("popup_container"),
                            fragment);
                    String nameFragment = fragment.getClass().getName();
                    ft.addToBackStack(nameFragment).commit();
                } else {
                    removeDialog();
                    DialogFragment newFragment = FragmentDialogHandle
                            .newInstance(fragment);
                    FragmentTransaction ft = mManager.beginTransaction();
                    newFragment.show(ft, "dialog");
                }
            } else {
                addFragment(fragment);
            }
        }
    }

    public void removeDialog() {
        Fragment prev = mManager.findFragmentByTag("dialog");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            Log.e("SimiManager ", "removeDialog" + df.getClass().getName());
            df.dismiss();
        }
    }

    public void backPreviousFragment() {
        if (DataLocal.isTablet) {
            popFragmentDialog();
        } else {
            mManager.popBackStack();
        }
    }

    public void backToHomeFragment() {
        mManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (DataLocal.isTablet) {
            removeDialog();
        }
        HomeFragment fragment = HomeFragment.newInstance();
        replaceFragment(fragment);
    }

    public void clearAllFragment() {
        mManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (DataLocal.isTablet) {
            removeDialog();
        }
    }

    public void popFragmentDialog() {
        Fragment prev = mManager.findFragmentByTag("dialog");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.getChildFragmentManager().popBackStack();
            if (df.getChildFragmentManager().getBackStackEntryCount() == 1) {
                ImageView back = (ImageView) df.getView().findViewById(
                        Rconfig.getInstance().id("bt_back"));
                back.setVisibility(View.GONE);
            }
        }
    }

    public void clearAllChidFragment() {
        Fragment prev = mManager.findFragmentByTag("dialog");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.getChildFragmentManager().popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public SimiRequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public void setCategorySlideMenuController(CategorySlideMenuController categorySlideMenuController) {
        this.mCategorySlideMenuController = categorySlideMenuController;
    }

    public void backToPreviousSlideCategory() {
        mCategorySlideMenuController.backToPreviousCat();
    }

    public void openSubCategory(String id, String name) {
        mCategorySlideMenuController.openSubCategory(id, name);
        mSlideMenu.openCategoryMenu();
    }

    public void openRootCateogry() {
        mCategorySlideMenuController.showRootCategory();
        mSlideMenu.openCategoryMenu();
    }

    public void setSlideMenu(SlideMenuFragment slideMenu) {
        this.mSlideMenu = slideMenu;
    }
}
