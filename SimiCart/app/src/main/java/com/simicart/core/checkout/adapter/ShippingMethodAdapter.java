package com.simicart.core.checkout.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

@SuppressLint("ViewHolder")
public class ShippingMethodAdapter extends BaseAdapter {
	protected ArrayList<ShippingMethod> mListShipping;
	protected LayoutInflater mInflater;

	public ShippingMethodAdapter(Context context,
			ArrayList<ShippingMethod> ShippingList) {
		mListShipping = ShippingList;
		mInflater = LayoutInflater.from(context);
	}

	public void setListShipping(ArrayList<ShippingMethod> listShipping) {
		mListShipping = listShipping;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListShipping.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListShipping.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		convertView = mInflater
				.inflate(
						Rconfig.getInstance().layout(
								"core_item_shipping_layout"), null);
		ShippingMethod shippintMethod = (ShippingMethod) getItem(position);

		Log.e("ShippingMethodAdapter getView ", shippintMethod.getJSONObject()
				.toString());

		// name
		TextView tv_name = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("shipping_name"));
		String name = shippintMethod.getS_method_name();

		if (name == null || name.equals("null")) {
			tv_name.setVisibility(View.GONE);
		} else {
			tv_name.setText(name);
		}

		// title
		TextView tv_title = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("shipping_title"));
		tv_title.setText(shippintMethod.getS_method_title());

		// price
		TextView tv_price = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("shipping_price"));
		String incl_tax = shippintMethod.getS_method_fee_incl_tax();
		String price = shippintMethod.getS_method_fee();

		if (incl_tax == null || incl_tax.equals("") || incl_tax.equals("null")) {
			tv_price.setText(AppStoreConfig.getInstance().getPrice(price));
			tv_price.setTextColor(AppColorConfig.getInstance().getPriceColor());
		} else {
			String price_method = "<font  color='"
					+ AppColorConfig.getInstance().getPriceColor() + "'>"
					+ AppStoreConfig.getInstance().getPrice(price)
					+ "</font> (<font color='grey'>+"
					+ SimiTranslator.getInstance().translate("Incl. Tax")
					+ "</font> <font  color='"
					+ AppColorConfig.getInstance().getPriceColor() + "'> "
					+ AppStoreConfig.getInstance().getPrice(incl_tax) + "</font>)";
			tv_price.setText(Html.fromHtml(price_method));
		}

		CheckBox checkbox = (CheckBox) convertView.findViewById(Rconfig
				.getInstance().id("shipping_checkbox"));
		if (shippintMethod.isS_method_selected()) {
			checkbox.setBackgroundResource(Rconfig.getInstance().drawable(
					"ic_action_accept"));
		} else {
			checkbox.setBackgroundColor(0);
		}
		return convertView;
	}
}
