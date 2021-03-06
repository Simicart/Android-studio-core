package com.simicart.plugins.storelocator.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.simicart.core.base.model.entity.SimiEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoreObject extends SimiEntity implements Parcelable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String storelocator_id;
    private String name;
    private String address;
    private String city;
    private String country;
    private String zipcode;
    private String state;
    private String state_id;
    private String email;
    private String phone;
    private String fax;
    private String description;
    private String status;
    private String sort;
    private String link;
    private String latitude;
    private String longtitude;
    private String monday_status;
    private String monday_open;
    private String monday_close;
    private String tuesday_status;
    private String tuesday_open;
    private String tuesday_close;
    private String wednesday_status;
    private String wednesday_open;
    private String wednesday_close;
    private String thursday_status;
    private String thursday_open;
    private String thursday_close;
    private String friday_status;
    private String friday_open;
    private String friday_close;
    private String saturday_status;
    private String saturday_open;
    private String saturday_close;
    private String sunday_status;
    private String sunday_open;
    private String sunday_close;
    private String zoom_level;
    private String image_icon;
    private String distance;
    private String image;
    // haita
    private String country_name;
    // end
    private ArrayList<SpecialObject> list_special;
    private ArrayList<SpecialObject> list_holiday;

    private String storelocator_id_key = "storelocator_id";
    private String name_key = "name";
    private String address_key = "address";
    private String city_key = "city";
    private String country_key = "country";
    private String zipcode_key = "zipcode";
    private String state_key = "state";
    private String state_id_key = "state_id";
    private String email_key = "email";
    private String phone_key = "phone";
    private String fax_key = "fax";
    private String description_key = "description";
    private String status_key = "status";
    private String sort_key = "sort";
    private String link_key = "link";
    private String latitude_key = "latitude";
    private String longtitude_key = "longtitude";
    private String monday_status_key = "monday_status";
    private String monday_open_key = "monday_open";
    private String monday_close_key = "monday_close";
    private String tuesday_status_key = "tuesday_status";
    private String tuesday_open_key = "tuesday_open";
    private String tuesday_close_key = "tuesday_close";
    private String wednesday_status_key = "wednesday_status";
    private String wednesday_open_key = "wednesday_open";
    private String wednesday_close_key = "wednesday_close";
    private String thursday_status_key = "thursday_status";
    private String thursday_open_key = "thursday_open";
    private String thursday_close_key = "thursday_close";
    private String friday_status_key = "friday_status";
    private String friday_open_key = "friday_open";
    private String friday_close_key = "friday_close";
    private String saturday_status_key = "saturday_status";
    private String saturday_open_key = "saturday_open";
    private String saturday_close_key = "saturday_close";
    private String sunday_status_key = "sunday_status";
    private String sunday_open_key = "sunday_open";
    private String sunday_close_key = "sunday_close";
    private String zoom_level_key = "zoom_level";
    private String image_icon_key = "image_icon";
    private String distance_key = "distance";
    private String image_key = "image";
    private String country_name_key = "country_name";
    private String list_special_key = "special_days";
    private String list_holiday_key = "holiday_days";

    @Override
    public void parse() {

        storelocator_id = getData(storelocator_id_key);

        image = getData(image_key);

        country_name = getData(country_name_key);

        country = getData(country_key);

        name = getData(name_key);

        address = getData(address_key);

        city = getData(city_key);

        zipcode = getData(zipcode_key);

        state = getData(state_key);

        state_id = getData(state_id_key);

        email = getData(email_key);

        phone = getData(phone_key);

        fax = getData(fax_key);

        description = getData(description_key);

        status = getData(status_key);

        sort = getData(sort_key);

        link = getData(link_key);

        latitude = getData(latitude_key);

        longtitude = getData(longtitude_key);

        monday_status = getData(monday_status_key);

        monday_open = getData(monday_open_key);

        monday_close = getData(monday_close_key);

        tuesday_status = getData(tuesday_status_key);

        tuesday_open = getData(tuesday_open_key);

        tuesday_close = getData(tuesday_close_key);

        wednesday_status = getData(wednesday_status_key);

        wednesday_open = getData(wednesday_open_key);

        wednesday_close = getData(wednesday_close_key);

        thursday_status = getData(thursday_status_key);

        thursday_open = getData(thursday_open_key);

        thursday_close = getData(thursday_close_key);

        friday_status = getData(friday_status_key);

        friday_open = getData(friday_open_key);

        friday_close = getData(friday_close_key);

        saturday_status = getData(saturday_status_key);

        saturday_open = getData(saturday_open_key);

        saturday_close = getData(saturday_close_key);

        sunday_status = getData(sunday_status_key);

        sunday_open = getData(sunday_open_key);

        sunday_close = getData(sunday_close_key);

        zoom_level = getData(zoom_level_key);

        image_icon = getData(image_icon_key);

        distance = getData(distance_key);

        if (hasKey(list_holiday_key)) {
            try {
                JSONArray holidayArr = mJSON.getJSONArray(list_holiday_key);
                if (holidayArr != null) {
                    list_holiday = new ArrayList<>();
                    for (int i = 0; i < holidayArr.length(); i++) {
                        JSONObject holidayObj = holidayArr.getJSONObject(i);
                        SpecialObject specialObject = new SpecialObject();
                        specialObject.parse(holidayObj);
                        list_holiday.add(specialObject);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (hasKey(list_special_key)) {
            try {
                JSONArray specialArr = mJSON.getJSONArray(list_special_key);
                if (specialArr != null) {
                    list_special = new ArrayList<>();
                    for (int i = 0; i < specialArr.length(); i++) {
                        JSONObject specialObj = specialArr.getJSONObject(i);
                        SpecialObject specialObject = new SpecialObject();
                        specialObject.parse(specialObj);
                        list_special.add(specialObject);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public String getStorelocator_id() {
        return storelocator_id;
    }

    public void setStorelocator_id(String storelocator_id) {
        this.storelocator_id = storelocator_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStoreID() {
        return storelocator_id;
    }

    public void setStoreID(String storeID) {
        this.storelocator_id = storeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getMonday_status() {
        return monday_status;
    }

    public void setMonday_status(String monday_status) {
        this.monday_status = monday_status;
    }

    public String getMonday_open() {
        return monday_open;
    }

    public void setMonday_open(String monday_open) {
        this.monday_open = monday_open;
    }

    public String getMonday_close() {
        return monday_close;
    }

    public void setMonday_close(String monday_close) {
        this.monday_close = monday_close;
    }

    public String getTuesday_status() {
        return tuesday_status;
    }

    public void setTuesday_status(String tuesday_status) {
        this.tuesday_status = tuesday_status;
    }

    public String getTuesday_open() {
        return tuesday_open;
    }

    public void setTuesday_open(String tuesday_open) {
        this.tuesday_open = tuesday_open;
    }

    public String getTuesday_close() {
        return tuesday_close;
    }

    public void setTuesday_close(String tuesday_close) {
        this.tuesday_close = tuesday_close;
    }

    public String getWednesday_status() {
        return wednesday_status;
    }

    public void setWednesday_status(String wednesday_status) {
        this.wednesday_status = wednesday_status;
    }

    public String getWednesday_open() {
        return wednesday_open;
    }

    public void setWednesday_open(String wednesday_open) {
        this.wednesday_open = wednesday_open;
    }

    public String getWednesday_close() {
        return wednesday_close;
    }

    public void setWednesday_close(String wednesday_close) {
        this.wednesday_close = wednesday_close;
    }

    public String getThursday_status() {
        return thursday_status;
    }

    public void setThursday_status(String thursday_status) {
        this.thursday_status = thursday_status;
    }

    public String getThursday_open() {
        return thursday_open;
    }

    public void setThursday_open(String thursday_open) {
        this.thursday_open = thursday_open;
    }

    public String getThursday_close() {
        return thursday_close;
    }

    public void setThursday_close(String thursday_close) {
        this.thursday_close = thursday_close;
    }

    public String getFriday_status() {
        return friday_status;
    }

    public void setFriday_status(String friday_status) {
        this.friday_status = friday_status;
    }

    public String getFriday_open() {
        return friday_open;
    }

    public void setFriday_open(String friday_open) {
        this.friday_open = friday_open;
    }

    public String getFriday_close() {
        return friday_close;
    }

    public void setFriday_close(String friday_close) {
        this.friday_close = friday_close;
    }

    public String getSaturday_status() {
        return saturday_status;
    }

    public void setSaturday_status(String saturday_status) {
        this.saturday_status = saturday_status;
    }

    public String getSaturday_open() {
        return saturday_open;
    }

    public void setSaturday_open(String saturday_open) {
        this.saturday_open = saturday_open;
    }

    public String getSaturday_close() {
        return saturday_close;
    }

    public void setSaturday_close(String saturday_close) {
        this.saturday_close = saturday_close;
    }

    public String getSunday_status() {
        return sunday_status;
    }

    public void setSunday_status(String sunday_status) {
        this.sunday_status = sunday_status;
    }

    public String getSunday_open() {
        return sunday_open;
    }

    public void setSunday_open(String sunday_open) {
        this.sunday_open = sunday_open;
    }

    public String getSunday_close() {
        return sunday_close;
    }

    public void setSunday_close(String sunday_close) {
        this.sunday_close = sunday_close;
    }

    public String getZoom_level() {
        return zoom_level;
    }

    public void setZoom_level(String zoom_level) {
        this.zoom_level = zoom_level;
    }

    public String getImage_icon() {
        return image_icon;
    }

    public void setImage_icon(String image_icon) {
        this.image_icon = image_icon;
    }

    public ArrayList<SpecialObject> getList_special() {
        return list_special;
    }

    public void setList_special(ArrayList<SpecialObject> list_special) {
        this.list_special = list_special;
    }

    public ArrayList<SpecialObject> getList_holiday() {
        return list_holiday;
    }

    public void setList_holiday(ArrayList<SpecialObject> list_holiday) {
        this.list_holiday = list_holiday;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
