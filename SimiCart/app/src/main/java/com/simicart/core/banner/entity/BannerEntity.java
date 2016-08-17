package com.simicart.core.banner.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

public class BannerEntity extends SimiEntity {
    protected String mImagePath;
    protected String uURL;
    protected String mType;
    protected String mCategoryId;
    protected String mCategoryName;
    protected String hasChild;
    protected String mProductId;


    @Override
    public void parse() {
        mProductId = getData("productID");
        hasChild = getData("has_child");
        uURL = getData("url");
        mType = getData("type");
        mImagePath = getData("image_path");
        mCategoryId = getData("categoryID");
        mCategoryName = getData("categoryName");
    }

    public String getProductId() {
        return this.mProductId;
    }

    public void setProductId(String mProductId) {
        this.mProductId = mProductId;
    }

    public boolean hasChild() {
        return Utils.TRUE(hasChild);
    }

    public void setHasChild(String hasChild) {
        this.hasChild = hasChild;
    }

    public String getUrl() {
        return this.uURL;
    }

    public void setUrl(String url) {
        this.uURL = url;
    }

    public String getImage() {
        return this.mImagePath;
    }

    public void setImage(String image_path) {
        this.mImagePath = image_path;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getCategoryName() {
        return this.mCategoryName;
    }

    public void setCategoryName(String mCategoryName) {
        this.mCategoryName = mCategoryName;
    }

    public String getCategoryId() {
        return this.mCategoryId;
    }

    public void setCategoryId(String mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

}
