package com.simicart.plugins.locator.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.common.StoreLocatorConfig;
import com.simicart.plugins.locator.entity.StoreObject;
import com.simicart.plugins.locator.fragment.MapViewFragment;
import com.simicart.plugins.locator.fragment.StoreDetailFragment;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Martial on 8/26/2016.
 */
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreHolder> {

    protected ArrayList<StoreObject> listStores;
    protected Context mContext;

    public StoreAdapter(ArrayList<StoreObject> listStores) {
        this.listStores = listStores;
    }

    @Override
    public StoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(Rconfig.getInstance().layout("plugins_store_locator_item_list_store"), null, false);
        StoreHolder holder = new StoreHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(StoreHolder holder, int position) {
        final StoreObject object = listStores.get(position);

        holder.txt_name.setText(object.getName());
        holder.txt_adress.setText(StoreLocatorConfig.convertAddress(object));

        if (object.getDistance() != null) {
            holder.txt_distan.setText(String.format("%.2f", Double.parseDouble(object.getDistance())) + " "
                    + SimiTranslator.getInstance().translate("km"));
        }
        if (object.getImage() != null && !object.getImage().equals("")
                && !object.getImage().equals("null")) {
            SimiDrawImage drawImage = new SimiDrawImage();
            drawImage.drawImage(holder.img, object.getImage());
            // circle.setBackground(getResources().getDrawable(R.drawable.circle));
            holder.img_default.setVisibility(View.GONE);
            holder.img.setVisibility(View.VISIBLE);
        } else {
            holder.img_default.setImageDrawable(mContext.getResources().getDrawable(
                    Rconfig.getInstance().getIdDraw(
                            "plugins_locator_ic_store_android")));
            holder.img_default.setVisibility(View.VISIBLE);
            holder.img.setVisibility(View.GONE);
        }
        if (object.getPhone() != null && !object.getPhone().equals("null")
                && !object.getPhone().equals("")) {
            holder.phone.setEnabled(true);
            holder.phone.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (!object.getPhone().equals("null")
                            && !object.getPhone().equals("")
                            && object.getPhone() != null) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL,
                                    Uri.parse("tel:" + object.getPhone()));
                            mContext.startActivity(intent);
                        } catch (Exception e) {

                        }
                    }
                }
            });
            holder.image_phone.setImageResource(Rconfig.getInstance().getIdDraw(
                    "plugins_locator_phone"));
        } else {
            holder.phone.setEnabled(false);
            holder.image_phone.setImageResource(Rconfig.getInstance().getIdDraw(
                    "plugins_locator_phone_disable"));
        }

        if (object.getEmail() != null && !object.getEmail().equals("null")
                && !object.getEmail().equals("")) {
            holder.email.setEnabled(true);
            holder.email.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    try {
                        String mail = object.getEmail();
                        Intent gmail = new Intent(Intent.ACTION_SEND);
                        // gmail.setClassName("com.google.android.gm",
                        // "com.google.android.gm.ComposeActivityGmail");
                        gmail.putExtra(Intent.EXTRA_EMAIL,
                                new String[] { mail });
                        gmail.setData(Uri.parse(mail));
                        gmail.putExtra(Intent.EXTRA_SUBJECT, "");
                        gmail.setType("plain/text");
                        gmail.putExtra(Intent.EXTRA_TEXT, "");
                        mContext.startActivity(gmail);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.image_email.setImageResource(Rconfig.getInstance().getIdDraw(
                    "plugins_locator_mail"));
        }

        if (object.getLatitude() != null
                && !object.getLatitude().equals("null")
                && !object.getLatitude().equals("")
                && !object.getLongtitude().equals("null")
                && !object.getLongtitude().equals("")) {
            holder.map.setEnabled(true);
            holder.map.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    HashMap<String, Object> hmData = new HashMap<String, Object>();
                    hmData.put(Constants.KeyData.STORE_OBJECT, object);
                    MapViewFragment fragment = MapViewFragment
                            .newInstance(new SimiData(hmData));
                    if (DataLocal.isTablet) {
                        SimiManager.getIntance().addPopupFragment(fragment);
                    } else {
                        SimiManager.getIntance().replaceFragment(fragment);
                    }

                }
            });
            holder.image_map.setImageResource(Rconfig.getInstance().getIdDraw(
                    "plugins_locator_map"));
        } else {
            holder.map.setEnabled(false);
            holder.image_map.setImageResource(Rconfig.getInstance().getIdDraw(
                    "plugins_locator_map_disable"));
        }

        holder.llItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> hmData = new HashMap<String, Object>();
                hmData.put(Constants.KeyData.STORE_OBJECT, object);
                StoreDetailFragment detail = StoreDetailFragment.newInstance(new SimiData(hmData));
                if (DataLocal.isTablet) {
                    SimiManager.getIntance().addPopupFragment(detail);
                } else {
                    SimiManager.getIntance().replaceFragment(detail);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listStores.size();
    }

    public static class StoreHolder extends RecyclerView.ViewHolder {
        private TextView txt_name, txt_distan, txt_adress, tvItemsLabel;
        private CircleImageView img;
        private ImageView img_default, image_phone, image_email, image_map;
        private LinearLayout llItems, phone, email, map, item;

        public StoreHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(Rconfig
                    .getInstance().getIdLayout("txt_name"));
            txt_distan = (TextView) itemView.findViewById(Rconfig
                    .getInstance().getIdLayout("txt_distan"));
            txt_adress = (TextView) itemView.findViewById(Rconfig
                    .getInstance().getIdLayout("txt_adress"));
            img = (CircleImageView) itemView
                    .findViewById(Rconfig.getInstance().getIdLayout("img"));
            img_default = (ImageView) itemView
                    .findViewById(Rconfig.getInstance().getIdLayout(
                            "img_default"));
            llItems = (LinearLayout) itemView
                    .findViewById(Rconfig.getInstance().getIdLayout("ll_item_store"));
            phone = (LinearLayout) itemView
                    .findViewById(Rconfig.getInstance().getIdLayout("phone"));
            email = (LinearLayout) itemView
                    .findViewById(Rconfig.getInstance().getIdLayout("email"));
            map = (LinearLayout) itemView.findViewById(Rconfig
                    .getInstance().getIdLayout("map"));
            item = (LinearLayout) itemView.findViewById(Rconfig
                    .getInstance().getIdLayout("item_list_store"));

            image_phone = (ImageView) itemView
                    .findViewById(Rconfig.getInstance().getIdLayout(
                            "image_phone"));
            image_email = (ImageView) itemView
                    .findViewById(Rconfig.getInstance().getIdLayout(
                            "image_email"));
            image_map = (ImageView) itemView.findViewById(Rconfig
                    .getInstance().getIdLayout("image_map"));
        }
    }
}
