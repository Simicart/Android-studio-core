package com.simicart.core.base.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by frank on 8/16/16.
 */
public class SimiData implements Parcelable {

    protected HashMap<String, Object> mData;

    public SimiData(HashMap<String, Object> data) {
        mData = data;
    }

    protected SimiData(Parcel in) {
        mData = in.readHashMap(HashMap.class.getClassLoader());
    }

    public static final Creator<SimiData> CREATOR = new Creator<SimiData>() {
        @Override
        public SimiData createFromParcel(Parcel in) {
            return new SimiData(in);
        }

        @Override
        public SimiData[] newArray(int size) {
            return new SimiData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mData);
    }

    public HashMap<String, Object> getData() {
        return mData;
    }

}
