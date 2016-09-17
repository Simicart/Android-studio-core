package com.simicart.plugins.instantcontact.block;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.instantcontact.adapter.ContactUsAdapter;
import com.simicart.plugins.instantcontact.entity.ContactUsEntity;

import java.util.ArrayList;

public class ContactUsBlock extends SimiBlock {
    protected ContactUsEntity baseContact;
    protected ArrayList<ContactUsEntity> listContactUs;
    protected RecyclerView rvContactUs;
    protected ContactUsAdapter adapter;

    public ContactUsBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        listContactUs = new ArrayList<>();
        rvContactUs = (RecyclerView) mView.findViewById(Rconfig.getInstance()
                .id("rv_list_contact"));
    }

    @Override
    public void drawView(SimiCollection collection) {
        baseContact = (ContactUsEntity) collection.getCollection().get(0);
        if (null != baseContact) {
            if (baseContact.getStyle().equals("2")) {
                rvContactUs.setLayoutManager(new GridLayoutManager(mContext, 2));
            } else {
                rvContactUs.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            }
            createListContact();
            adapter = new ContactUsAdapter(listContactUs, baseContact.getStyle());
            rvContactUs.setAdapter(adapter);
        } else {
            ((LinearLayout) mView).removeAllViewsInLayout();
            TextView tv_message = new TextView(mView.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            tv_message.setLayoutParams(params);
            tv_message.setText(SimiTranslator.getInstance().translate(
                    "No information about contact"));
            ((LinearLayout) mView).addView(tv_message);
        }
    }

    protected void createListContact() {
        if (baseContact.getStyle().equals("2")) {
            createItemContact("plugins_contactus_email", "Email");
            createItemContact("plugins_contactus_message", "Message");
            createItemContact("plugins_contactus_call", "Call");
            createItemContact("plugins_contactus_web", "Website");
        } else {
            createItemContact("plugins_contactusemail_list", "Email");
            createItemContact("plugins_contactusmessage_list", "Message");
            createItemContact("plugins_contactusphone_list", "Call");
            createItemContact("plugins_contactusweb_list", "Website");
        }
    }

    protected void createItemContact(String icon, String name) {
        ContactUsEntity item = new ContactUsEntity(baseContact, icon, name);
        listContactUs.add(item);
    }

}
