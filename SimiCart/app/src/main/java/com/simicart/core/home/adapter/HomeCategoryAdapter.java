package com.simicart.core.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.catalog.categorydetail.fragment.CategoryDetailFragment;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    protected ArrayList<Category> mListCate;

    public HomeCategoryAdapter(ArrayList<Category> categorys) {
        this.mListCate = categorys;
    }

    @Override
    public int getItemCount() {
        return mListCate.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int idView = Rconfig.getInstance().layout("core_adapter_cate_home");
        View itemView = inflater.inflate(idView, null);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Category category = mListCate.get(position);

        String name = category.getCategoryName();
        if (Utils.validateString(name)) {
            holder.tvName.setText(name);
        }

        String url = category.getCategoryImage();
        if (Utils.validateString(url)) {
            DrawableManager.fetchDrawableOnThread(url, holder.imgCate);
        }

        holder.llItemCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(category.hasChild() == true) {
                    openCate(category);
                } else {
                    openListProduct(category);
                }
            }
        });

    }

    protected void openCate(Category cate) {
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put(KeyData.CATEGORY.CATEGORY_ID, cate.getCategoryId());
        hmData.put(KeyData.CATEGORY.CATEGORY_NAME, cate.getCategoryName());
        SimiManager.getIntance().openCategory(hmData);
    }

    protected void openListProduct(Category cate) {
        HashMap<String,Object> hm = new HashMap<>();
        hm.put(KeyData.CATEGORY_DETAIL.TYPE, ValueData.CATEGORY_DETAIL.CATE);
        hm.put(KeyData.CATEGORY_DETAIL.CATE_NAME, cate.getCategoryName());
        hm.put(KeyData.CATEGORY_DETAIL.CATE_ID, cate.getCategoryId());
        SimiManager.getIntance().openCategoryDetail(hm);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView imgCate;
        LinearLayout llItemCate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(Rconfig.getInstance().id("txt_category_item_home"));
            tvName.setTextColor(AppColorConfig.getInstance().getContentColor());
            imgCate = (ImageView) itemView.findViewById(Rconfig.getInstance().id("img_category_item_home"));
            llItemCate = (LinearLayout) itemView.findViewById(Rconfig.getInstance().id("ll_item_cate"));
        }
    }

}
