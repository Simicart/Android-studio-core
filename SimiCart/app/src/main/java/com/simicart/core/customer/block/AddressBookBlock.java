package com.simicart.core.customer.block;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.adapter.AddressBookAdapter;
import com.simicart.core.customer.entity.AddressEntity;

import java.util.ArrayList;

public class AddressBookBlock extends SimiBlock {

    protected TextView tv_addAddress;
    protected RecyclerView rvAddress;
    protected AddressBookAdapter mAdapter;
    private RelativeLayout rlt_layout_addadress;
    protected int openFor = -1;
    protected ArrayList<AddressEntity> listAddress;

    @Override
    public void initView() {
        // Choose an address for editing
        TextView tv_chooseAddress = (TextView) mView.findViewById(Rconfig
                .getInstance().id("tv_chooseAddress"));
        tv_chooseAddress.setBackgroundColor(AppColorConfig.getInstance().getSectionColor());
        tv_chooseAddress.setTextColor(AppColorConfig.getInstance().getSectionTextColor());
        if (openFor == ValueData.ADDRESS_BOOK.OPEN_FOR_CHECKOUT) {
            tv_chooseAddress.setText(SimiTranslator.getInstance().translate(
                    "Or choose an address"));
        } else {
            tv_chooseAddress.setText(SimiTranslator.getInstance().translate(
                    "Or choose an address for editing"));
        }
        tv_addAddress = (TextView) mView.findViewById(Rconfig.getInstance().id(
                "addAddress"));
        tv_addAddress.setText(SimiTranslator.getInstance().translate("Add an address"));

        rvAddress = (RecyclerView) mView.findViewById(Rconfig.getInstance().id(
                "rv_list_address"));
        rvAddress.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        rlt_layout_addadress = (RelativeLayout) mView.findViewById(Rconfig
                .getInstance().id("layout_addadress"));
        rlt_layout_addadress.setBackgroundColor(AppColorConfig.getInstance().getColorButtonBackground());
        tv_addAddress.setBackgroundColor(AppColorConfig.getInstance().getColorButtonBackground());

        tv_addAddress.setTextColor(AppColorConfig.getInstance().getButtonTextColor());

    }

    @Override
    public void drawView(SimiCollection collection) {
        ArrayList<SimiEntity> entities = collection.getCollection();
        if (entities != null && entities.size() > 0) {
            listAddress = new ArrayList<>();
            for (int i = 0; i < entities.size(); i++) {
                AddressEntity entity = (AddressEntity) entities.get(i);
                listAddress.add(entity);
            }
            mAdapter = new AddressBookAdapter(listAddress, openFor);
            rvAddress.setAdapter(mAdapter);
        }
    }

    public AddressBookBlock(View view, Context context) {
        super(view, context);
    }

    public void setCreateNewListener(View.OnClickListener listener) {
        tv_addAddress.setOnClickListener(listener);
    }

    public void setOpenFor(int openFor) {
        this.openFor = openFor;
    }

}
