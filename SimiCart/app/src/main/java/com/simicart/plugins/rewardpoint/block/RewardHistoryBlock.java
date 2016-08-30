package com.simicart.plugins.rewardpoint.block;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.adapter.AdapterListviewHistory;
import com.simicart.plugins.rewardpoint.entity.ItemHistory;

import java.util.ArrayList;

/**
 * Created by Martial on 8/29/2016.
 */
public class RewardHistoryBlock extends SimiBlock {

    protected ListView listView;
    protected TextView txt_message;
    protected ArrayList<ItemHistory> listHistories;

    public RewardHistoryBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        listView = (ListView) mView.findViewById(Rconfig.getInstance().id(
                "reward_listview_history"));
        txt_message = (TextView) mView.findViewById(Rconfig.getInstance().id("txt_message"));
    }

    @Override
    public void drawView(SimiCollection collection) {
        ArrayList<SimiEntity> entities = collection.getCollection();
        listHistories = new ArrayList<>();
        for(int i=0;i<entities.size();i++) {
            ItemHistory entity = (ItemHistory) entities.get(i);
            listHistories.add(entity);
        }
        if(listHistories.size() > 0){
            listView.setVisibility(View.VISIBLE);
            txt_message.setVisibility(View.GONE);
            AdapterListviewHistory adapter = new AdapterListviewHistory(
                    mContext, listHistories);
            listView.setAdapter(adapter);
        }else{
            txt_message.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }
}
