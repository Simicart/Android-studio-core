package com.simicart.core.common;

/**
 * Created by frank on 8/19/16.
 */
public class KeyData {
    public static class CATEGORY_DETAIL {
        public static String TYPE = "type";
        public static String KEY_WORD = "key_word";
        public static String CATE_ID = "category_id";
        public static String CATE_NAME = "category_name";
        public static String OFFSET = "offset";
        public static String LIMIT = "limit";
        public static String SORT_OPTION = "sort_option";
        public static String IMAGE_WIDTH = "width";
        public static String IMAGE_HEIGHT = "height";
        public static String FILTER = "filter";
        public static String CUSTOM_URL = "custom_url";
    }

    public static class CATEGORY {
        public static String CATEGORY_ID = "category_id";
        public static String CATEGORY_NAME = "category_name";
    }

    public static class ADDRESS_BOOK_DETAIL {
        public static String OPEN_FOR = "open_for";
        public static String ACTION = "action";
        public static String BILLING_ADDRESS = "billing_address";
        public static String SHIPPING_ADDRESS = "shipping_address";
        public static String ADDRESS_FOR_EDIT = "address_for_edit";
    }

    public static class ADDRESS_BOOK {
        public static String OPEN_FOR = "open_for";
        public static String BILLING_ADDRESS = "billing_address";
        public static String SHIPPING_ADDRESS = "shipping_address";
        public static String ACTION = "action";
    }

    public static class LIST_OF_CHOICE {
        public static String DELEGATE = "delegate";
        public static String LIST_DATA = "list_data";
    }


    public static class ORDER_HISTORY_DETAIL {
        public static String ORDER_ID = "order_id";
        public static String TARGET = "target";
    }

    public static class REVIEW_ORDER {
        public static String PLACE_FOR = "place_for";
        public static String SHIPPING_ADDRESS = "shipping_address";
        public static String BILLING_ADDRESS = "billing_address";
    }

    public static class SLIDE_MENU {
        public static String LIST_ITEMS = "list_items";
        public static String LIST_FRAGMENTS = "list_fragments";
        public static final String ITEM_NAME = "item_name";
    }

    public static class REQUEST_PERMISSIONS {
        public static final int LOCATION_REQUEST = 123;
    }

    public static class BAR_CODE {
        public static final String INTENT = "intent";
        public static final String REQUEST_CODE = "request_code";
        public static final String RESULT_CODE = "result_code";
    }

    public static class WEBVIEW_PAYMENT_COMPONENT {
        public static final String URL = "url";
        public static final String KEY_SUCCESS = "success";
        public static final String KEY_FAIL = "fail";
        public static final String KEY_ERROR = "error";
        public static final String MESSAGE_SUCCESS = "message_success";
        public static final String MESSAGE_FAIL = "message_fail";
        public static final String MESSAGE_ERROR = "message_error";
        public static final String KEY_REVIEW = "review";
        public static final String MESSAGE_REVIEW = "message_review";
    }

}
