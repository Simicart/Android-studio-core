package com.simicart.plugins.locator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.entity.SearchObject;
import com.simicart.plugins.locator.fragment.StoreLocatorMainPageFragment;
import com.simicart.plugins.locator.fragment.StoreLocatorMainPageTabletFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Martial on 8/26/2016.
 */
public class SearchTagAdapter extends RecyclerView.Adapter<SearchTagAdapter.SearchTagHolder> {

    protected ArrayList<String> listTags;
    protected Context mContext;
    protected int count_tag = 0;

    public SearchTagAdapter(ArrayList<String> listTags, int count_tag) {
        this.listTags = listTags;
        this.count_tag = count_tag;
    }

    @Override
    public SearchTagHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(Rconfig.getInstance().layout("plugins_storelocator_item_tag"), null, false);
        SearchTagHolder holder = new SearchTagHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(SearchTagHolder holder, final int position) {
        String tag = listTags.get(position);

        if (position == count_tag) {
            holder.item_tag.setBackgroundDrawable(mContext.getResources().getDrawable(
                    Rconfig.getInstance().getIdDraw(
                            "plugins_locator_drawble_selec")));
            holder.icon_item.setImageDrawable(mContext.getResources().getDrawable(
                    Rconfig.getInstance().getIdDraw(
                            "plugins_locator_ic_store_selec")));
            holder.txt_item.setTextColor(mContext.getResources().getColor(
                    android.R.color.holo_orange_dark));
        } else {
            holder.item_tag.setBackgroundDrawable(mContext.getResources().getDrawable(
                    Rconfig.getInstance().getIdDraw(
                            "plugins_locator_drawble_search")));
            holder.icon_item.setImageDrawable(mContext.getResources().getDrawable(
                    Rconfig.getInstance().getIdDraw(
                            "plugins_locator_ic_store")));
            holder.txt_item.setTextColor(mContext.getResources().getColor(
                    android.R.color.black));
        }

        holder.item_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchObject search = new SearchObject();
                search.setTag(position);
                onSearchAction(search);
            }
        });

    }

    protected void onSearchAction(SearchObject searchObject) {
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put(Constants.KeyData.SEARCH_OBJECT, searchObject);
        if(DataLocal.isTablet) {
            StoreLocatorMainPageTabletFragment fragment = StoreLocatorMainPageTabletFragment.newInstance(new SimiData(hmData));
            SimiManager.getIntance().replaceFragment(fragment);
        } else {
            StoreLocatorMainPageFragment fragment = StoreLocatorMainPageFragment.newInstance(new SimiData(hmData));
            SimiManager.getIntance().replaceFragment(fragment);
        }
        SimiManager.getIntance().removeDialog();
    }

    @Override
    public int getItemCount() {
        return listTags.size();
    }

    public static class SearchTagHolder extends RecyclerView.ViewHolder {
        private ImageView icon_item;
        private TextView txt_item;
        private LinearLayout item_tag;

        public SearchTagHolder(View itemView) {
            super(itemView);
            icon_item = (ImageView) itemView
                    .findViewById(Rconfig.getInstance()
                            .getIdLayout("icon_item"));
            txt_item = (TextView) itemView
                    .findViewById(Rconfig.getInstance().getIdLayout("txt_item"));
            item_tag = (LinearLayout) itemView
                    .findViewById(Rconfig.getInstance().getIdLayout("item_tag"));
        }
    }
}
