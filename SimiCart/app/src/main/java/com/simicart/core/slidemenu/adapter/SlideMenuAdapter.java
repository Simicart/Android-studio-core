package com.simicart.core.slidemenu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.entity.ItemNavigation;

import java.util.ArrayList;

@SuppressLint("DefaultLocale")
public class SlideMenuAdapter extends BaseAdapter {

    protected ArrayList<ItemNavigation> mItems;
    protected Context mContext;

    public SlideMenuAdapter(ArrayList<ItemNavigation> items, Context context) {
        mContext = context;
        mItems = items;
    }

    public void setItems(ArrayList<ItemNavigation> items) {
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemNavigation item = (ItemNavigation) getItem(position);

        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(
                    Rconfig.getInstance().layout("core_phone_slide_menu_item"),
                    null, false);

            holder.img_icon = (ImageView) convertView.findViewById(Rconfig
                    .getInstance().id("img_icon"));
            holder.tv_name = (TextView) convertView.findViewById(Rconfig
                    .getInstance().id("tv_name"));
            holder.tv_name.setTextColor(AppColorConfig.getInstance()
                    .getMenuTextColor());
            if (AppStoreConfig.getInstance().isRTL()) {
                holder.tv_name.setGravity(Gravity.RIGHT
                        | Gravity.CENTER_VERTICAL);
            }
            holder.img_extended = (ImageView) convertView.findViewById(Rconfig
                    .getInstance().id("img_extended"));

            convertView.setTag(holder);

        }
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img_extended.setVisibility(View.GONE);
        holder.tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        int id_icon = Rconfig.getInstance().drawable("ic_menu_extended");
        Drawable icon = mContext.getResources().getDrawable(id_icon);
        icon.setColorFilter(AppColorConfig.getInstance().getMenuIconColor(),
                PorterDuff.Mode.SRC_ATOP);
        holder.img_extended.setImageDrawable(icon);

        holder.img_icon.setVisibility(View.VISIBLE);

        if (item.isExtended()) {
            holder.img_extended.setVisibility(View.VISIBLE);
        }
        if (item.isSparator()) {
            convertView.setOnClickListener(null);
            if (AppStoreConfig.getInstance().isRTL()) {
                holder.img_icon.setVisibility(View.INVISIBLE);
            } else {
                holder.img_icon.setVisibility(View.GONE);
            }
            convertView.setBackgroundColor(Color.parseColor("#4D000000"));
        }

        String name = item.getName();
        if (Utils.validateString(name)) {
            name = SimiTranslator.getInstance().translate(name);
            if (item.isSparator()) {

                name = name.toUpperCase();
            }
            holder.tv_name.setText(SimiTranslator.getInstance().translate(name));
        }

        String url = item.getUrl();
        if (Utils.validateString(url)) {
            // DrawableManager.fetchDrawableIConOnThread(url, holder.img_icon,
            // mContext, Color.parseColor("#ffffff"));
            DrawableManager.fetchDrawableIConOnThread(url, holder.img_icon,
                    mContext, AppColorConfig.getInstance().getMenuIconColor());
        } else {
            Drawable drawable = item.getIcon();
            if (null != drawable) {
                drawable.setColorFilter(AppColorConfig.getInstance()
                        .getMenuIconColor(), PorterDuff.Mode.SRC_ATOP);
                holder.img_icon.setImageDrawable(drawable);
            }
        }

        return convertView;
    }

    class ViewHolder {
        ImageView img_icon;
        TextView tv_name;
        ImageView img_extended;
    }

}
