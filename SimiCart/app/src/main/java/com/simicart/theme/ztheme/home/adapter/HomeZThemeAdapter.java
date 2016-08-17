package com.simicart.theme.ztheme.home.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.imagesimicart.SimiImageView;
import com.simicart.theme.ztheme.home.entity.ZThemeCatalogEntity;
import com.simicart.theme.ztheme.home.entity.ZThemeSpotEntity;

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
            return catalogEntity.getCategoryZTheme().getListChildCategory().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCategories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ZThemeCatalogEntity catalogEntity = mCategories.get(groupPosition);
        if (catalogEntity.getType().equals("cat")) {
            return catalogEntity.getCategoryZTheme();
        } else {
            return catalogEntity.getZThemeSpotEntity();
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return 0;
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
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(
                    Rconfig.getInstance().layout("theme_z_adapter_home_item"), null);
            holder.img_category = (ImageView) convertView.findViewById(Rconfig
                    .getInstance().id("iv_home"));
            holder.img_category.setScaleType(ScaleType.FIT_XY);
            holder.tv_catename = (TextView) convertView.findViewById(Rconfig
                    .getInstance().id("tv_title"));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ZThemeCatalogEntity catalogEntity = mCategories.get(groupPosition);

        if (holder.tv_catename != null) {
            String title = catalogEntity.getTitle();
            if (!Utils.validateString(title)) {
                holder.tv_catename.setVisibility(View.GONE);
            } else {
                holder.tv_catename.setText(title);
            }
        }

        String image = null;
        if(catalogEntity.getType().equals("cat")) {
            image = catalogEntity.getCategoryZTheme().getCategoryImage();
        } else {
            image = catalogEntity.getZThemeSpotEntity().getImage();
        }
        if(Utils.validateString(image)) {
            new SimiDrawImage().drawImage(holder.img_category, image);
        }

//        String url = "";
//        if (mCategories.get(groupPosition).getType() == ZThemeCatalogEntity.TYPE_CAT) {
//            final Category object = mCategories.get(groupPosition);
//            url = object.getCategoryImage();
//        } else {
//            ZThemeSpotEntity object = mCategories.get(groupPosition)
//                    .getZThemeSpotEntity();
//            url = object.getImage();
//        }
//        if (Utils.validateString(url)) {
//            try {
//                if (holder.img_category != null) {
//                    DrawableManager.fetchDrawableOnThread(url,
//                            holder.img_category);
//                    notifyDataSetChanged();
//                }
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(
                Rconfig.getInstance().layout("theme_z_adapter_item_child"), null);
        TextView txt_category = (TextView) convertView.findViewById(Rconfig
                .getInstance().id("tv_catename"));
        ZThemeCatalogEntity catalogEntity = mCategories.get(groupPosition);
        if(catalogEntity.getType().equals("cat")) {
            Category category = catalogEntity.getCategoryZTheme();
            if(category.hasChild() == true) {
                Category childCategory = category.getListChildCategory().get(childPosition);
                String childName = childCategory.getCategoryName();
                if(Utils.validateString(childName)) {
                    txt_category.setText(childName);
                }
            }
        }
        // }
        return convertView;
    }

    static class ViewHolder {
        TextView tv_catename;
        ImageView img_category;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
