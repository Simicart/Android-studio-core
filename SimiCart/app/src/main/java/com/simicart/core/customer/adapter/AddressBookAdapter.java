package com.simicart.core.customer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.AddressEntity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Martial on 8/23/2016.
 */
public class AddressBookAdapter extends RecyclerView.Adapter<AddressBookAdapter.AddressBookHolder> {

    protected ArrayList<AddressEntity> listAddress;
    protected int openFor = -1;
    protected int actionEdit = -1;
    protected HashMap<String, Object> mData;

    public AddressBookAdapter(ArrayList<AddressEntity> listAddress, HashMap<String, Object> data) {
        this.listAddress = listAddress;
        mData = data;

        if (mData.containsKey(KeyData.ADDRESS_BOOK.OPEN_FOR)) {
            openFor = ((Integer) mData.get(KeyData.ADDRESS_BOOK.OPEN_FOR)).intValue();
        }

        if (mData.containsKey(KeyData.ADDRESS_BOOK.ACTION)) {
            actionEdit = ((Integer) mData.get(KeyData.ADDRESS_BOOK.ACTION)).intValue();
        }

    }

    @Override
    public AddressBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(Rconfig.getInstance().layout("core_adapter_item_address_book"), null, false);
        AddressBookHolder holder = new AddressBookHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(AddressBookHolder holder, int position) {
        final AddressEntity addressEntity = listAddress.get(position);

        // name
        String name = addressEntity.getName();
        String prefix = addressEntity.getPrefix();
        String suffix = addressEntity.getSuffix();

        if (!Utils.validateString(prefix)) {
            holder.tvName.setText(name + " " + suffix);
        } else {
            holder.tvName.setText(prefix + " " + name + " " + suffix);
        }

        // Street
        String street = addressEntity.getStreet();
        if (Utils.validateString(street)) {
            holder.tvStreet.setText(street);
        }

        // city
        String state = addressEntity.getStateName();
        String city = addressEntity.getCity();
        String zip_code = addressEntity.getZipCode();
        if (!Utils.validateString(state)) {
            holder.tvCity.setText(city + ", " + zip_code);
        } else {
            holder.tvCity.setText(city + ", " + state + ", " + zip_code);
        }

        // country
        String country = addressEntity.getCountryName();
        if (Utils.validateString(country)) {
            holder.tvCountry.setText(country);
        }

        // phone
        String phone = addressEntity.getPhone();
        if (Utils.validateString(phone)) {
            holder.tvPhone.setText(phone);
        }

        // email
        String email = addressEntity.getEmail();
        if (Utils.validateString(email)) {
            holder.tvEmail.setText(email);
        }

        if (openFor == ValueData.ADDRESS_BOOK.OPEN_FOR_CHECKOUT) {
            holder.ivExtend.setVisibility(View.GONE);
        }

        holder.vAddress.setBackgroundColor(AppColorConfig.getInstance().getLineColor());

        holder.rlItemAddressBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openFor == ValueData.ADDRESS_BOOK.OPEN_FOR_CUSTOMER) {
                    onChooseAddressEdit(addressEntity);
                } else if (openFor == ValueData.ADDRESS_BOOK.OPEN_FOR_CHECKOUT) {
                    onChooseAddressCheckout(addressEntity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAddress.size();
    }

    protected void onChooseAddressEdit(AddressEntity addressEntity) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(KeyData.ADDRESS_BOOK_DETAIL.OPEN_FOR, ValueData.ADDRESS_BOOK_DETAIL.OPEN_FOR_CUSTOMER);
        hm.put(KeyData.ADDRESS_BOOK_DETAIL.ACTION, ValueData.ADDRESS_BOOK_DETAIL.ACTION_EDIT);
        hm.put(KeyData.ADDRESS_BOOK_DETAIL.ADDRESS_FOR_EDIT, addressEntity);
        SimiManager.getIntance().openAddressBookDetail(hm);
    }

    protected void onChooseAddressCheckout(AddressEntity addressEntity) {
        HashMap<String, Object> hm = new HashMap<>();
        if (actionEdit == ValueData.ADDRESS_BOOK.ACTION_EDIT_BILLING_ADDRESS) {
            hm.put(KeyData.REVIEW_ORDER.BILLING_ADDRESS, addressEntity);
            AddressEntity oldShippingAddress = (AddressEntity) mData.get(KeyData.ADDRESS_BOOK.SHIPPING_ADDRESS);
            hm.put(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS, oldShippingAddress);
        } else if (actionEdit == ValueData.ADDRESS_BOOK.ACTION_EDIT_SHIPPING_ADDRESS) {
            hm.put(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS, addressEntity);
            AddressEntity oldBillingAddress = (AddressEntity) mData.get(KeyData.ADDRESS_BOOK.BILLING_ADDRESS);
            hm.put(KeyData.REVIEW_ORDER.BILLING_ADDRESS, oldBillingAddress);
        } else {
            hm.put(KeyData.REVIEW_ORDER.SHIPPING_ADDRESS, addressEntity);
            hm.put(KeyData.REVIEW_ORDER.BILLING_ADDRESS, addressEntity);
        }
        SimiManager.getIntance().openReviewOrder(hm);
    }

    public static class AddressBookHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlItemAddressBook;
        private TextView tvName, tvStreet, tvCity, tvCountry, tvPhone, tvEmail;
        private ImageView ivExtend;
        private View vAddress;

        public AddressBookHolder(View itemView) {
            super(itemView);
            rlItemAddressBook = (RelativeLayout) itemView.findViewById(Rconfig.getInstance().id("rl_item_address_book"));

            tvName = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_name"));
            tvName.setTextColor(AppColorConfig.getInstance().getContentColor());

            tvStreet = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_street"));
            tvStreet.setTextColor(AppColorConfig.getInstance().getContentColor());

            tvCity = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_city"));
            tvCity.setTextColor(AppColorConfig.getInstance().getContentColor());

            tvCountry = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_country"));
            tvCountry.setTextColor(AppColorConfig.getInstance().getContentColor());

            tvPhone = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_phone"));
            tvPhone.setTextColor(AppColorConfig.getInstance().getContentColor());

            tvEmail = (TextView) itemView.findViewById(Rconfig.getInstance().id("tv_email"));
            tvEmail.setTextColor(AppColorConfig.getInstance().getContentColor());

            ivExtend = (ImageView) itemView.findViewById(Rconfig.getInstance().id("image_expand"));
            vAddress = (View) itemView.findViewById(Rconfig.getInstance().id("v_address"));

        }
    }

}
