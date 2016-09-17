package com.simicart.theme.ztheme.home.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.category.entity.Category;

public class ZThemeCatalogEntity extends SimiEntity {

    protected String mType;
    protected String mTitle;
    protected Category categoryZTheme;
    protected ZThemeSpotEntity ZThemeSpotEntity;

    private String type = "type";
    private String title = "title";

    @Override
    public void parse() {

        mType = getData(type);

        mTitle = getData(title);

        if (mType != null) {
            if (mType.equals("cat")) {
                categoryZTheme = new Category();
                categoryZTheme.setJSONObject(mJSON);
                categoryZTheme.parse();
            } else {
                ZThemeSpotEntity = new ZThemeSpotEntity();
                ZThemeSpotEntity.setJSONObject(mJSON);
                ZThemeSpotEntity.parse();
            }
        }

    }

    public Category getCategoryZTheme() {
        return categoryZTheme;
    }

    public void setCategoryZTheme(Category categoryZTheme) {
        this.categoryZTheme = categoryZTheme;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public ZThemeSpotEntity getZThemeSpotEntity() {
        return ZThemeSpotEntity;
    }

    public void setZThemeSpotEntity(ZThemeSpotEntity ZThemeSpotEntity) {
        this.ZThemeSpotEntity = ZThemeSpotEntity;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }
}
