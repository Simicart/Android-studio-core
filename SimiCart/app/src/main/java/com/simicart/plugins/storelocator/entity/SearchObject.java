package com.simicart.plugins.storelocator.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

public class SearchObject extends SimiEntity implements Parcelable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String city;
    private String state;
    private String zipcode;
    private int tag;
    private String name_country;
    private int position_country;

    private String city_key = "city";
    private String state_key = "state";
    private String zipcode_key = "zipcode";
    private String tag_key = "tag";
    private String name_country_key = "name_country";
    private String position_country_key = "position_country";

    public SearchObject() {
        city = "";
        state = "";
        zipcode = "";
        tag = -1;
        name_country = "";
        position_country = 0;
    }

    @Override
    public void parse() {

        city = getData(city_key);

        state = getData(state_key);

        zipcode = getData(zipcode_key);

        String sTag = getData(tag_key);
        if (Utils.validateString(sTag)) {
            tag = Integer.parseInt(sTag);
        }

        name_country = getData(name_country_key);

        String positionCountry = getData(position_country_key);
        if (Utils.validateString(positionCountry)) {
            position_country = Integer.parseInt(positionCountry);
        }

    }

    public int getPosition_country() {
        return position_country;
    }

    public void setPosition_country(int position_country) {
        this.position_country = position_country;
    }

    public String getName_country() {
        return name_country;
    }

    public void setName_country(String name_country) {
        this.name_country = name_country;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "SearchObject [city=" + city + ", state=" + state + ", zipcode="
                + zipcode + ", tag=" + tag + ", name_country=" + name_country
                + ", position_country=" + position_country + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
