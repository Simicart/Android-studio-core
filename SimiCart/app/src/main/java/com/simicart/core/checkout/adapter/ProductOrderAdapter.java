package com.simicart.core.checkout.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

public class ProductOrderAdapter extends BaseAdapter {
	ArrayList<Cart> mListCart;
	View rootView;
	Context context;
	LayoutInflater inflater;
	protected String mCurrecySymbol;

	public void setCurrencySymbol(String symbol) {
		mCurrecySymbol = symbol;
	}

	public ProductOrderAdapter(Context context, ArrayList<Cart> listProductOrder) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.mListCart = listProductOrder;
	}

	@Override
	public int getCount() {
		if(mListCart != null){
			int size = mListCart.size();
		return size;
	}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (DataLocal.isLanguageRTL) {
			convertView = this.inflater.inflate(
					Rconfig.getInstance().layout(
							"rtl_listitem_product_orderhis"), null);
		} else {
			convertView = this.inflater.inflate(
					Rconfig.getInstance().layout(
							"core_listitem_product_orderhis"), null);
		}
		convertView.setBackgroundColor(AppColorConfig.getInstance().getAppBackground());
		if(mListCart != null){
		Cart cart = mListCart.get(position);
		
		TextView name = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("name_product"));
		name.setTextColor(AppColorConfig.getInstance().getContentColor());
		if (DataLocal.isLanguageRTL) {
			name.setGravity(Gravity.RIGHT);
		}
		name.setText(cart.getProduct_name());
		TextView tv_price = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("price_product"));
		if (DataLocal.isLanguageRTL) {
			tv_price.setGravity(Gravity.RIGHT);
		}
		tv_price.setTextColor(AppColorConfig.getInstance()
				.getPriceColor());
		String price = AppStoreConfig.getInstance().getPrice(
				Float.toString(cart.getProduct_price()));
		if (null != mCurrecySymbol) {
			price = AppStoreConfig.getInstance().getPrice(
					Float.toString(cart.getProduct_price()), mCurrecySymbol);
		}

		tv_price.setText(price);
		TextView qty = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("qty_product"));
		qty.setTextColor(AppColorConfig.getInstance().getContentColor());
		qty.setText("" + cart.getQty());

		ImageView image = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("image_product"));
		String img = cart.getProduct_image();
		DrawableManager.fetchDrawableOnThread(img, image);
		}
		return convertView;
	}

}
