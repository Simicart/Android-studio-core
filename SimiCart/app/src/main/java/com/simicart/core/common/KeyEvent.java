package com.simicart.core.common;

/**
 * Created by Martial on 8/27/2016.
 */
public class KeyEvent {

    public static class SLIDE_MENU_EVENT {
        public static final String ADD_ITEM_MORE = "com.simicart.menuleft.additem.more";
        public static final String ADD_ITEM_RELATED_PERSONAL = "com.simicart.menuleft.additem.personal";
        public static final String CLICK_ITEM = "com.simicart.menuleft.onnavigate.clickitem";
        public static final String REMOVE_ITEM = "com.simicart.menuleft.removeitem";
    }

    public static class MY_ACCOUNT_EVENT {
        public static final String MY_ACCOUNT_ADD_ITEM = "simicart.myaccount.additem";
    }

    public static class REVIEW_ORDER {
        public static final String FOR_PAYMENT_BEFORE_PLACE = "simicart.before.placeorder";
        public static final String FOR_PAYMENT_TYPE_SDK = "simicart.placeorder.sdk";
        public static final String FOR_PAYMENT_TYPE_WEBVIEW = "simciart.placeorder.webview";
        public static final String FOR_PAYMENT_AFTER_PLACE = "simciart.after.placeorder";
    }

    public static class FACEBOOK_EVENT {
        public static final String FACEBOOK_LOGIN_BLOCK = "com.simicart.core.customer.block.SignInBlock";
        public static final String FACEBOOK_LOGIN_FRAGMENT = "com.simicart.core.customer.fragment.SignInFragment";
        public static final String FACEBOOK_LOGIN_AUTO = "simicart.facebook.autologin";
    }

    public static class REWARD_POINT_EVENT {
        public static final String REWARD_ADD_ITEM_CART = "com.simicart.core.checkout.block.CartBlock";
        public static final String REWARD_ADD_ITEM_REVIEW_ORDER = "com.simicart.core.checkout.block.ReviewOrderBlock";
    }

    public static class WISH_LIST {
        public static final String WISH_LIST_ADD_BUTTON_MORE = "com.simicart.core.catalog.product.block.ProductMorePluginBlock";
    }

    public static class BAR_CODE {
        public static final String BAR_CODE_ON_RESULT = "simicart.menuleft.onactivityresult.resultbarcode";
        public static final String BAR_CODE_ON_BACK = "simicart.menuleft.mainactivity.backtoscan";
    }

    public static class ZOPIM {
        public static final String ZOPIM_ADD_ICON_TOP_MENU = "com.simicart.core.menutop.block.MenuTopBlock";
    }

}
