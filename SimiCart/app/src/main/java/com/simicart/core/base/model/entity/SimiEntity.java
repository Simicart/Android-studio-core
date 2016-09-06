package com.simicart.core.base.model.entity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SimiEntity {
	protected JSONObject mJSON;
	protected Bundle bundle = new Bundle();

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	public Bundle getBundle() {
		return bundle;
	}

	public void setJSONObject(JSONObject json) {
		this.mJSON = json;
	}

	public JSONObject getJSONObject() {
		return mJSON;
	}

	public String getData(String key) {
		if (mJSON != null && mJSON.has(key)) {
			try {
				return this.mJSON.getString(key);
			} catch (JSONException e) {
				Log.e("SimiEntity ","GET DATA Exception " + e.getMessage());
			}
		}
		return null;
	}

	public JSONObject getJSONObjectWithKey(JSONObject jsParent, String key) {
		if (null != jsParent) {
			if (jsParent.has(key)) {
				try {
					JSONObject jsChild = jsParent.getJSONObject(key);
					return jsChild;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		return null;

	}

	public JSONArray getArray(String key) {
		JSONArray array = null;

		if (mJSON != null && mJSON.has(key)) {
			try {
				array = mJSON.getJSONArray(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		return array;

	}

	public JSONArray getJSONArrayWithKey(JSONObject jsParent, String key) {
		if (null != jsParent) {
			if (jsParent.has(key)) {
				try {
					JSONArray array = jsParent.getJSONArray(key);
					return array;
				} catch (JSONException e) {
				}
			}

		}
		return null;
	}

	public boolean hasKey(String key) {
		return mJSON.has(key);
	}

	public void parse(JSONObject json) {
		mJSON = json;
		this.parse();
	}

	public void parse() {
	}

	protected float parseFloat(String price) {
		try {
			return Float.parseFloat(price);
		} catch (Exception e) {

		}
		return 0;
	}

	protected int parseInt(String price) {
		try {
			return Integer.parseInt(price);
		} catch (Exception e) {

		}
		return 0;
	}

}
