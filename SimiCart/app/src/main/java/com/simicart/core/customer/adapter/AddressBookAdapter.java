package com.simicart.core.customer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.AddressEntity;

import java.util.ArrayList;

/**
 * Created by Martial on 8/23/2016.
 */
public class AddressBookAdapter extends RecyclerView.Adapter<AddressBookAdapter.AddressBookHolder> {

    protected ArrayList<AddressEntity> listAddress;
    protected int addressBookFor = -1;

    public AddressBookAdapter(ArrayList<AddressEntity> listAddress, int addressBookFor) {
        this.listAddress = listAddress;
        this.addressBookFor = addressBookFor;
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

        if (addressBookFor == ValueData.ADDRESS_BOOK.CHECKOUT_ADDRESS) {
            holder.ivExtend.setVisibility(View.GONE);
        }

        holder.vAddress.setBackgroundColor(AppColorConfig.getInstance().getLineColor());

        holder.rlItemAddressBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressBookFor == ValueData.ADDRESS_BOOK.CUSTOMER_ADDRESS) {
                    onChooseAddressEdit(addressEntity);
                } else if (addressBookFor == ValueData.ADDRESS_BOOK.CHECKOUT_ADDRESS) {
                    onChooseAddressCheckout(addressEntity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAddress.size();
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

    protected void onChooseAddressEdit(AddressEntity addressEntity) {
//        SimiData data = new SimiData();
//        data.addData("address_for", Constants.KeyAddress.EDIT_ADDRESS);
//        data.addData("address_entity", addressEntity);
//        AddressFragment fragment = AddressFragment.newInstance(data);
//        SimiManager.getIntance().replaceFragment(fragment);
    }

    protected void onChooseAddressCheckout(AddressEntity addressEntity) {
//        ReviewOrderFragment fragment = new ReviewOrderFragment();
//        fragment.setTypeCheckout(ReviewOrderFragment.CHECKOUTTYPE.AS_LOGGED);
//        fragment.setShippingAddress(addressEntity);
//        fragment.setBillingAddress(addressEntity);
//        SimiManager.getIntance().replaceFragment(fragment);
    }

}
