package com.simicart.core.catalog.category.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Martial on 8/18/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    protected ArrayList<Category> listCategories;
    protected Context mContext;

    public CategoryAdapter(ArrayList<Category> listCategories) {
        this.listCategories = listCategories;
        mContext = SimiManager.getIntance().getCurrentActivity();
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(Rconfig.getInstance().layout("core_adapter_item_list_category"), null, false);
        CategoryHolder holder = new CategoryHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {

        Category category = listCategories.get(position);

        String cateName = category.getCategoryName();
        if(Utils.validateString(cateName)) {
            holder.tv_name.setText(cateName);
            if (DataLocal.isLanguageRTL) {
                holder.tv_name.setGravity(Gravity.RIGHT);
            }
        }

        if (DataLocal.isTablet) {
            holder.tv_name.setTextColor(AppColorConfig.getInstance().getMenuTextColor());
            holder.tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            Drawable icon = mContext.getResources().getDrawable(
                    Rconfig.getInstance().drawable("ic_menu_extended"));
            icon.setColorFilter(AppColorConfig.getInstance().getMenuIconColor(),
                    PorterDuff.Mode.SRC_ATOP);
            holder.iv_showmore.setImageDrawable(icon);

            holder.line.setBackgroundColor(AppColorConfig.getInstance().getMenuLineColor());
        } else {
            holder.tv_name.setTextColor(Color.parseColor("#000000"));
            Drawable icon = mContext.getResources().getDrawable(
                    Rconfig.getInstance().drawable("ic_extend"));
            icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                    PorterDuff.Mode.SRC_ATOP);
            holder.iv_showmore.setImageDrawable(icon);

            holder.line.setBackgroundColor(AppColorConfig.getInstance().getLineColor());
        }

        holder.rl_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return listCategories.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private ImageView iv_showmore;
        private View line;
        private RelativeLayout rl_category;

        public CategoryHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_catename"));
            iv_showmore = (ImageView) itemView.findViewById(Rconfig.getInstance().id("iv_showmore"));
            line = (View) itemView.findViewById(Rconfig.getInstance().id("v_line"));
            rl_category = (RelativeLayout) itemView.findViewById(Rconfig.getInstance().id("rl_category"));
        }
    }
}
