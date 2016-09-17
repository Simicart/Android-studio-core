package com.simicart.theme.ztheme.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;
import com.simicart.theme.ztheme.home.entity.ZThemeCatalogEntity;

import java.util.ArrayList;

public class HomeZThemeAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private ArrayList<ZThemeCatalogEntity> mCategories;

    public HomeZThemeAdapter(Context context, ArrayList<ZThemeCatalogEntity> list) {
        mContext = context;
        this.mCategories = list;
    }

    @Override
    public int getGroupCount() {
        return mCategories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ZThemeCatalogEntity catalogEntity = mCategories.get(groupPosition);
        if (catalogEntity.getType().equals("cat")) {
            Category category = catalogEntity.getCategoryZTheme();
            if (category.hasChild() == true) {
                return catalogEntity.getCategoryZTheme().getListChildCategory().size();
            }
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCategories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ZThemeCatalogEntity catalogEntity = mCategories.get(groupPosition);
        if (catalogEntity.getType().equals("cat")) {
            Category category = catalogEntity.getCategoryZTheme();
            if (category.hasChild() == true) {
                return category.getListChildCategory().get(childPosition);
            }
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(
                Rconfig.getInstance().layout("theme_z_adapter_home_item"), parent, false);
        ImageView img_category = (ImageView) convertView.findViewById(Rconfig
                .getInstance().id("iv_home"));
        img_category.setScaleType(ScaleType.FIT_XY);
        TextView tv_catename = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("tv_title"));

        ZThemeCatalogEntity catalogEntity = mCategories.get(groupPosition);

        if (tv_catename != null) {
            String title = catalogEntity.getTitle();
            if (!Utils.validateString(title)) {
                tv_catename.setVisibility(View.GONE);
            } else {
                tv_catename.setText(title);
            }
        }

        String image = null;
        if (catalogEntity.getType().equals("cat")) {
            image = catalogEntity.getCategoryZTheme().getCategoryImage();
        } else {
            image = catalogEntity.getZThemeSpotEntity().getImage();
        }
        if (Utils.validateString(image)) {
            DrawableManager.fetchDrawableOnThread(image, img_category, Utils.getScreenWidth(), Utils.toPixel(230));
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(
                Rconfig.getInstance().layout("theme_z_adapter_item_child"), null);
        TextView txt_category = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("tv_name"));
        txt_category.setTextColor(Color.parseColor("#000000"));
        ZThemeCatalogEntity catalogEntity = mCategories.get(groupPosition);
        if (catalogEntity.getType().equals("cat")) {
            Category category = catalogEntity.getCategoryZTheme();
            if (category.hasChild() == true) {
                Category childCategory = category.getListChildCategory().get(childPosition);
                String childName = childCategory.getCategoryName();
                if (Utils.validateString(childName)) {
                    txt_category.setText(childName);
                }
            }
        }
        return convertView;
    }

    public class ViewHolder {
        TextView tv_catename;
        ImageView img_category;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
