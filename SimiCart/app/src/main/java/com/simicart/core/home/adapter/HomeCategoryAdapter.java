package com.simicart.core.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

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
        Category category = mListCate.get(position);

        String name = category.getCategoryName();

        if (Utils.validateString(name)) {
            holder.tvName.setText(name);
        }

        String url = category.getCategoryImage();
        if (Utils.validateString(url)) {
            DrawableManager.fetchDrawableOnThread(url, holder.imgCate);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView imgCate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(Rconfig.getInstance().id("txt_category_item_home"));
            imgCate = (ImageView) itemView.findViewById(Rconfig.getInstance().id("img_category_item_home"));

        }
    }

}
