package com.simicart.core.customer.component;

import android.view.View;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.AddressEntity;

/**
 * Created by Martial on 9/1/2016.
 */
public class AddressOrderComponent extends SimiComponent {

    protected AddressEntity addressEntity;

    public AddressOrderComponent(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    @Override
    public View createView() {
        rootView = findLayout("core_component_address_order");

        TextView tvName = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_name"));
        tvName.setTextColor(AppColorConfig.getInstance().getContentColor());
        String name = addressEntity.getName();
        if(Utils.validateString(name)) {
            tvName.setText(name);
        }

        TextView tvStreet = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_street"));
        tvStreet.setTextColor(AppColorConfig.getInstance().getContentColor());
        String street = addressEntity.getStreet();
        if(Utils.validateString(street)) {
            tvStreet.setText(street);
        }

        TextView tvCity = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_city"));
        tvCity.setTextColor(AppColorConfig.getInstance().getContentColor());
        String city = addressEntity.getCity();
        if(Utils.validateString(city)) {
            tvCity.setText(city);
        }

        TextView tvCountry = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_country"));
        tvCountry.setTextColor(AppColorConfig.getInstance().getContentColor());
        String country = addressEntity.getCountryName();
        if(Utils.validateString(country)) {
            tvCountry.setText(country);
        }

        TextView tvPhone = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_phone"));
        tvPhone.setTextColor(AppColorConfig.getInstance().getContentColor());
        String phone = addressEntity.getPhone();
        if(Utils.validateString(phone)) {
            tvPhone.setText(phone);
        }

        TextView tvEmail = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_email"));
        tvEmail.setTextColor(AppColorConfig.getInstance().getContentColor());
        String email = addressEntity.getEmail();
        if(Utils.validateString(email)) {
            tvEmail.setText(email);
        }

        return rootView;
    }
}
