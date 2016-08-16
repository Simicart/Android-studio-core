package com.simicart.core.config;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

/**
 * Created by Martial on 8/15/2016.
 */
public class AppColorConfig extends SimiEntity {

    public static AppColorConfig instance;
    protected Context mContext;

    // basic color
    protected String mKeyColor;
    protected String mTopMenuIconColor;
    protected String mButtonBackground;
    protected String mButtonTextColor;
    protected String mMenuBackground;
    protected String mMenuTextColor;
    protected String mMenuLineColor;
    protected String mMenuIconColor;
    // more option color
    protected String mSearchBoxBackground;
    protected String mSearchTextColor;
    protected String mAppBackground;
    protected String mContentColor;
    protected String mImageBorderColor;
    protected String mLineColor;
    protected String mPriceColor;
    protected String mSpecialPriceColor;
    protected String mIconColor;
    protected String mSectionColor;

    private String key_color = "key_color";
    private String top_menu_icon_color = "top_menu_icon_color";
    private String button_background = "button_background";
    private String button_text_color = "button_text_color";
    private String menu_background = "menu_background";
    private String menu_text_color = "menu_text_color";
    private String menu_line_color = "menu_line_color";
    private String menu_icon_color = "menu_icon_color";

    private String search_box_background = "search_box_background";
    private String search_text_color = "search_text_color";
    private String app_background = "app_background";
    private String content_color = "content_color";
    private String image_border_color = "image_border_color";
    private String line_color = "line_color";
    private String price_color = "price_color";
    private String special_price_color = "special_price_color";
    private String icon_color = "icon_color";
    private String section_color = "section_color";

    public static AppColorConfig getInstance() {
        if (instance == null) {
            instance = new AppColorConfig();
        }
        return instance;
    }

    public AppColorConfig() {
        parse();
        mContext = SimiManager.getIntance().getCurrentActivity();
    }

    @Override
    public void parse() {
        if (mJSON != null) {


            if (mJSON.has(key_color)) {
                mKeyColor = getData(key_color);
            }

            if (mJSON.has(button_background)) {
                mButtonBackground = getData(button_background);
            }

            if (mJSON.has(button_text_color)) {
                mButtonTextColor = getData(button_text_color);
            }

            if (mJSON.has(menu_background)) {
                mMenuBackground = getData(menu_background);
            }

            if (mJSON.has(menu_text_color)) {
                mMenuTextColor = getData(menu_text_color);
            }

            if (mJSON.has(menu_line_color)) {
                mMenuLineColor = getData(menu_line_color);
            }

            if (mJSON.has(menu_icon_color)) {
                mMenuIconColor = getData(menu_icon_color);
            }

            if (mJSON.has(top_menu_icon_color)) {
                mTopMenuIconColor = getData(top_menu_icon_color);
            }

            if (mJSON.has(app_background)) {
                mAppBackground = getData(app_background);
            }

            if (mJSON.has(content_color)) {
                mContentColor = getData(content_color);
            }

            if (mJSON.has(line_color)) {
                mLineColor = getData(line_color);
            }

            if (mJSON.has(image_border_color)) {
                mImageBorderColor = getData(image_border_color);
            }

            if (mJSON.has(icon_color)) {
                mIconColor = getData(icon_color);
            }

            if (mJSON.has(price_color)) {
                mPriceColor = getData(price_color);
            }

            if (mJSON.has(special_price_color)) {
                mSpecialPriceColor = getData(special_price_color);
            }

            if (mJSON.has(section_color)) {
                mSectionColor = getData(section_color);
            }

            if (mJSON.has(search_box_background)) {
                mSearchBoxBackground = getData(search_box_background);
            }

            if (mJSON.has(search_text_color)) {
                mSearchTextColor = getData(search_text_color);
            }
        }
    }

    protected int getColor(String codeColor) {
        if (!Utils.validateString(codeColor)) {
            return 0;
        }

        try {
            return Color.parseColor(codeColor);
        } catch (Exception e) {
            return 0;
        }
    }

    public Drawable getIcon(String id) {
        int idIcon = Rconfig.getInstance().drawable(id);
        Drawable icon = mContext.getResources().getDrawable(idIcon);
        icon.setColorFilter(Color.parseColor(mIconColor), PorterDuff.Mode.SRC_ATOP);
        return icon;
    }

    public String getAppBackground() {
        return mAppBackground;
    }

    public void setAppBackground(String mAppBackground) {
        this.mAppBackground = mAppBackground;
    }

    public ColorStateList getButtonBackground() {
        return new ColorStateList(new int[][]{{0}}, new int[]{Color.parseColor(mButtonBackground)});
    }

    public ColorStateList getButtonBackground(String color) {
        return new ColorStateList(new int[][]{{0}}, new int[]{Color.parseColor(color)});
    }

    public ColorStateList getButtonBackground(int color) {
        return new ColorStateList(new int[][]{{0}}, new int[]{color});
    }

    public void setButtonBackground(String mButtonBackground) {
        this.mButtonBackground = mButtonBackground;
    }

    public String getButtonTextColor() {
        return mButtonTextColor;
    }

    public void setButtonTextColor(String mButtonTextColor) {
        this.mButtonTextColor = mButtonTextColor;
    }

    public int getContentColor() {
        return Color.parseColor(mContentColor);
    }

    public void setContentColor(String mContentColor) {
        this.mContentColor = mContentColor;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getIconColor() {
        return mIconColor;
    }

    public void setIconColor(String mIconColor) {
        this.mIconColor = mIconColor;
    }

    public String getImageBorderColor() {
        return mImageBorderColor;
    }

    public void setImageBorderColor(String mImageBorderColor) {
        this.mImageBorderColor = mImageBorderColor;
    }

    public String getKeyColor() {
        return mKeyColor;
    }

    public void setKeyColor(String mKeyColor) {
        this.mKeyColor = mKeyColor;
    }

    public int getLineColor() {
        return Color.parseColor(mLineColor);
    }

    public void setLineColor(String mLineColor) {
        this.mLineColor = mLineColor;
    }

    public String getMenuBackground() {
        return mMenuBackground;
    }

    public void setMenuBackground(String mMenuBackground) {
        this.mMenuBackground = mMenuBackground;
    }

    public String getMenuIconColor() {
        return mMenuIconColor;
    }

    public void setMenuIconColor(String mMenuIconColor) {
        this.mMenuIconColor = mMenuIconColor;
    }

    public String getMenuLineColor() {
        return mMenuLineColor;
    }

    public void setMenuLineColor(String mMenuLineColor) {
        this.mMenuLineColor = mMenuLineColor;
    }

    public String getMenuTextColor() {
        return mMenuTextColor;
    }

    public void setMenuTextColor(String mMenuTextColor) {
        this.mMenuTextColor = mMenuTextColor;
    }

    public String getPriceColor() {
        return mPriceColor;
    }

    public void setPriceColor(String mPriceColor) {
        this.mPriceColor = mPriceColor;
    }

    public String getSearchBoxBackground() {
        return mSearchBoxBackground;
    }

    public void setSearchBoxBackground(String mSearchBoxBackground) {
        this.mSearchBoxBackground = mSearchBoxBackground;
    }

    public String getSearchTextColor() {
        return mSearchTextColor;
    }

    public void setSearchTextColor(String mSearchTextColor) {
        this.mSearchTextColor = mSearchTextColor;
    }

    public String getSectionColor() {
        return mSectionColor;
    }

    public void setSectionColor(String mSectionColor) {
        this.mSectionColor = mSectionColor;
    }

    public String getSpecialPriceColor() {
        return mSpecialPriceColor;
    }

    public void setSpecialPriceColor(String mSpecialPriceColor) {
        this.mSpecialPriceColor = mSpecialPriceColor;
    }

    public String getTopMenuIconColor() {
        return mTopMenuIconColor;
    }

    public void setTopMenuIconColor(String mTopMenuIconColor) {
        this.mTopMenuIconColor = mTopMenuIconColor;
    }
}
