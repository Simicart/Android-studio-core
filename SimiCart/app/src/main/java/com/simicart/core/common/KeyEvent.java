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

}
