package com.simicart.core.notification;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

/**
 * Created by frank on 7/19/16.
 */
public class NotificationEntity extends SimiEntity {

    protected String mTitle;
    protected String mUrl;
    protected String mType;
    protected String mCategoryID;
    protected String mProductID;
    protected String mImageUrl;
    protected boolean isShowPopup;
    protected String mMessage;
    protected String mCategoryName;
    protected boolean hasChild;

    private String notice_title = "title";
    private String notice_url = "notice_url";
    private String type = "type";
    private String category_id = "categoryID";
    private String product_id = "productID";
    private String image_url = "url";
    private String show_popup = "show_popup";
    private String has_child = "has_child";
    private String message = "message";
    private String categoryName = "categoryName";


    @Override
    public void parse() {

        // message
        if (hasKey(message)) {
            mMessage = getData(message);
        }

        // has child
        if (hasKey(has_child)) {
            String hasChild = getData(has_child);
            if (Utils.TRUE(hasChild)) {
                setHasChild(true);
            }
        }

        // category name
        if (hasKey(categoryName)) {
            mCategoryName = getData(categoryName);
        }


        // title
        if (hasKey(notice_title)) {
            mTitle = getData(notice_title);
        }

        // url
        if (hasKey(notice_url)) {
            mUrl = getData(notice_url);
        }


        // type
        if (hasKey(type)) {
            mType = getData(type);
        }

        // category id
        if (hasKey(category_id)) {
            mCategoryID = getData(category_id);
        }

        // product id
        if (hasKey(product_id)) {
            mProductID = getData(product_id);
        }

        // image url
        if (hasKey(image_url)) {
            mImageUrl = getData(image_url);
        }

        // is show popup
        if (hasKey(show_popup)) {
            String showPopup = getData(show_popup);
            if (Utils.TRUE(showPopup)) {
                setShowPopup(true);
            }
        }


    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }


    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getCategoryID() {
        return mCategoryID;
    }

    public void setCategoryID(String mCategoryID) {
        this.mCategoryID = mCategoryID;
    }

    public String getProductID() {
        return mProductID;
    }

    public void setProductID(String mProductID) {
        this.mProductID = mProductID;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public boolean isShowPopup() {
        return isShowPopup;
    }

    public void setShowPopup(boolean showPopup) {
        isShowPopup = showPopup;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public void setmCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
