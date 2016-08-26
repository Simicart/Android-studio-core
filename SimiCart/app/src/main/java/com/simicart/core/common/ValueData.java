package com.simicart.core.common;

/**
 * Created by frank on 24/08/2016.
 */
public class ValueData {

    public static class ADDRESS_BOOK {
        public final static int OPEN_FOR_CUSTOMER = 100;
        public final static int OPEN_FOR_CHECKOUT = 111;
        public final static int ACTION_EDIT_SHIPPING_ADDRESS = 998;
        public final static int ACTION_EDIT_BILLING_ADDRESS = 997;
    }

    public static class ADDRESS_BOOK_DETAIL {
        public static int OPEN_FOR_CUSTOMER = 100;
        public static int OPEN_FOR_CHECKOUT = 111;
        public static int ACTION_NEW = 0;
        public static int ACTION_EDIT = 1;
        public static int ACTION_GUEST = 2;
        public static int ACTION_NEW_CUSTOMER = 3;
    }

    public static class REVIEW_ORDER {
        public static int PLACE_FOR_NEW_CUSTOMER = 999;
    }

}
