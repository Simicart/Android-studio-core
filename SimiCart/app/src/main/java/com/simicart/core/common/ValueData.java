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
        public static int EDIT_FOR_NEW_CUSTOMER = 4;
        public static  int EDIT_FOR_GUEST = 5;
    }

    public static class REVIEW_ORDER {
        public static int PLACE_FOR_NEW_CUSTOMER = 999;
        public static int PLACE_FOR_GUEST = 996;
        public static  int PLACE_FOR_EXISTING_CUSTOMER = 995;
    }

    public static class CUSTOMER_PAGE{
        public static int OPEN_FOR_REGISTER = 994;
        public static int OPEN_FOR_EDIT = 993;
    }

    public static class CATEGORY_DETAIL{
        public static String ALL = "all";
        public static String SEARCH = "search";
        public static String CATE = "cate";
        public static String CUSTOM = "custom";
    }

    public static class ANALYTICS{
        public static String SCREEN_TYPE = "screen_type";
        public static String BANNER_TYPE = "banner_type";
        public static String ORDER_TYPE = "order_type";
    }

    public static class PRODUCT_LABEL {
        public static final String HORIZONTAL = "horizontal";
        public static final String LIST = "list";
        public static final String GRID = "grid";
    }

}
