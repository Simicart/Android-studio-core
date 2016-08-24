package com.simicart.core.customer.block;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.adapter.IndexableListAdapter;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.IndexableListView;

public class ListOfChoiceBlock extends SimiBlock {
    protected IndexableListView list_country;
    protected IndexableListAdapter mAdapter;
    ArrayList<String> mListData;

    public ListOfChoiceBlock(View view, Context context) {
        super(view, context);
    }

    public void setOnItemClicker(OnItemClickListener clicker) {
        list_country.setOnItemClickListener(clicker);
    }

    @Override
    public void initView() {
        list_country = (IndexableListView) mView.findViewById(Rconfig
                .getInstance().id("listview"));

        ColorDrawable sage = new ColorDrawable(AppColorConfig.getInstance().getLineColor());
        list_country.setDivider(sage);
        list_country.setDividerHeight(1);
        if (mListData.size() > 0) {
            if (null == mAdapter) {
                Collections.sort(mListData);
                mAdapter = new IndexableListAdapter(mContext, mListData, "");
                list_country.setAdapter(mAdapter);
            }
        }
        list_country.setFastScrollEnabled(true);
    }

    @Override
    public void drawView(SimiCollection collection) {
    }

    public void setListContry(ArrayList<String> list_country) {
        this.mListData = list_country;
    }
}
