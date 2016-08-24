package com.simicart.core.common;

/**
 * Created by frank on 24/08/2016.
 */
public class ValueData {

    public static class ADDRESS_BOOK {
        public final static int CUSTOMER_ADDRESS = 100;
        public final static int CHECKOUT_ADDRESS = 111;
    }

    public static class ADDRESS_BOOK_DETAIL {
        public static int OPEN_FOR_CUSTOMER = 100;
        public static int OPEN_FOR_CHECKOUT = 111;
        public static int ACTION_NEW = 0;
        public static int ACTION_EDIT = 1;
        public static int ACTION_GUEST = 2;
        public static int ACTION_NEW_CUSTOMER = 3;
    }
}
