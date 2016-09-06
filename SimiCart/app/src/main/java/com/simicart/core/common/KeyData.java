package com.simicart.core.common;

/**
 * Created by frank on 8/19/16.
 */
public class KeyData {

    public static class SIMI_BLOCK {
        public static String BLOCK = "block";
        public static String COLLECTION = "collection";
        public static String VIEW = "view";
    }

    public static class SIMI_FRAGMENT {
        public static String FRAGMENT = "fragment";
        public static String METHOD = "method";
    }

    public static class SIMI_CONTROLLER {
        public static String DELEGATE = "delegate";
        public static String JSON_DATA = "json_data";
    }

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
        public static String LIST_COMPONENTS = "list_components";
        public static String JSON_DATA = "json_data";
    }

    public static class TOTAL_PRICE {
        public static String LIST_ROWS = "list_rows";
        public static String JSON_DATA = "json_data";
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

    public static class PRODUCT_DETAIL{
        public static final String PRODUCT_ID = "id";
        public static final String LIST_PRODUCT_ID = "list_id";
        public static final String IS_FROM_SCAN = "from_scan";
    }

    public static class FACEBOOK {
        public static final String VIEW = "view";
        public static final String REQUEST_CODE = "request_code";
        public static final String RESULT_CODE = "result_code";
        public static final String INTENT = "intent";
    }

    public static class REWARD_POINT {
        public static final String PASSBOOK_LOGO = "passbook_logo";
        public static final String LOYALTY_REDEEM = "loyalty_redeem";
        public static final String PASSBOOK_TEXT = "passbook_text";
        public static final String PASSBOOK_BACKGROUND = "passbook_background";
        public static final String PASSBOOK_FOREGROUND = "passbook_foreground";
        public static final String PASSBOOK_BARCODE = "passbook_barcode";
        public static final String EXPIRE_NOTIFICATION = "expire_notification";
        public static final String IS_NOTIFICATION = "is_notification";
        public static final String VIEW = "view";
        public static final String ENTITY_JSON = "json_entity";
    }

    public static class CUSTOMER_PAGE{
        public static final  String OPEN_FOR = "openfor";
    }

    public static class SIGN_IN{
        public static final  String EMAIL = "email";
        public static final  String PASSWORD = "pass";
        public static final  String IS_CHECKOUT = "is_checkout";
    }

    public static class ADDRESS_AUTO_FILL {
        public static final String LIST_COMPONENTS = "list_components";
        public static final String LIST_COUNTRIES = "list_countries";
    }

    public static class WEBVIEW_PAGE{
        public static final String URL = "url";
    }

}
