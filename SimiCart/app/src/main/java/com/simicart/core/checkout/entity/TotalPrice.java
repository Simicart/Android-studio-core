package com.simicart.core.checkout.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Constants;

public class TotalPrice extends SimiEntity {
	private String mSub_Total;
	private String mSubtotalExclTax;
	private String mSubtotalInclTax;
	private String mDiscount;
	private String mShippingHandling;
	private String mShippingHandlingInclTax;
	private String mShippingHandlingExclTax;
	private String mTax;
	private String mGrandTotal;
	private String mGrandTotalInclTax;
	private String mGrandTotalExclTax;
	private String mCurrencySymbol;
	private String mSubTotalOrderHistory;
	private JSONObject jsonV2;

	public JSONObject getJSONV2() {
		if (null == jsonV2) {
			if (null != mJSON && mJSON.has(Constants.V2)) {
				try {
					String value = mJSON.getString(Constants.V2);
					if (null != value && !value.equals("")
							&& !value.equals("null")) {
						jsonV2 = new JSONObject(value);
					}
				} catch (JSONException ex) {
					return null;
				}
			}
		}

		return jsonV2;
	}

	public String getValueWithJSONV2(String key) {
		if (jsonV2.has(key)) {
			try {
				String value = jsonV2.getString(key);
				return value;
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}

	public void setCurrencySymbol(String symbol) {
		mCurrencySymbol = symbol;
	}

	public String getCurrencySymbol() {
		if (null == mCurrencySymbol) {
			mCurrencySymbol = getData(Constants.CURRENCY_SYMBOL);
		}
		return mCurrencySymbol;
	}

	public String getSubTotal() {
		if (null == mSub_Total) {
			if (null != getJSONV2()) {
				mSub_Total = getValueWithJSONV2(Constants.SUB_TOTAL);
			}
			if (null == mSub_Total) {
				mSub_Total = getData(Constants.SUB_TOTAL);
			}
			if (null == mSub_Total) {
				mSub_Total = getData(Constants.SUBTOTAL);
			}
		}
		return mSub_Total;
	}

	public String getSubTotalOrderHis() {
		if (null == mSubTotalOrderHistory) {
			if (null != getJSONV2()) {
				mSubTotalOrderHistory = getValueWithJSONV2(Constants.SUB_TOTAL);
				if (null == mSubTotalOrderHistory) {
					mSubTotalOrderHistory = getValueWithJSONV2(Constants.SUBTOTAL);
				}
			}
			if (null == mSubTotalOrderHistory) {
				mSubTotalOrderHistory = getData(Constants.SUB_TOTAL);
			}
			if (null == mSubTotalOrderHistory) {
				mSubTotalOrderHistory = getData(Constants.SUBTOTAL);
			}
		}
		return mSubTotalOrderHistory;
	}

	public void setSubTotalOrderHis(String subtotal) {
		mSubTotalOrderHistory = subtotal;
	}

	public String getSubtotalExclTax() {
		if (null == mSubtotalExclTax) {
			if (null != getJSONV2()) {
				mSubtotalExclTax = getValueWithJSONV2(Constants.SUBTOTAL_EXCL_TAX);
			}
			if (null == mSubtotalExclTax) {
				mSubtotalExclTax = getData(Constants.SUBTOTAL_EXCL_TAX);
			}
		}
		return mSubtotalExclTax;
	}

	public void setSubtotalExclTax(String subtotal_excl_tax) {
		this.mSubtotalExclTax = subtotal_excl_tax;
	}

	public String getSubtotalInclTax() {
		if (null == mSubtotalInclTax) {

			if (null != getJSONV2()) {
				mSubtotalInclTax = getValueWithJSONV2(Constants.SUBTOTAL_INCL_TAX);
			}

			if (null == mSubtotalInclTax) {
				mSubtotalInclTax = getData(Constants.SUBTOTAL_INCL_TAX);
			}
		}

		return mSubtotalInclTax;
	}

	public void setSubtotalInclTax(String subtotal_incl_tax) {
		this.mSubtotalInclTax = subtotal_incl_tax;
	}

	public String getDiscount() {
		if (null == mDiscount) {
			if (null != getJSONV2()) {
				mDiscount = getValueWithJSONV2(Constants.DISCOUNT);
			}
			if (null == mDiscount) {
				mDiscount = getData(Constants.DISCOUNT);
			}
		}
		return mDiscount;
	}

	public void setDiscount(String discount) {
		this.mDiscount = discount;
	}

	public String getShippingHandling() {
		if (null == mShippingHandling) {

			if (null != getJSONV2()) {
				mShippingHandling = getValueWithJSONV2(Constants.SHIPPING_HAND);
			}
			if (null == mShippingHandling) {
				mShippingHandling = getData(Constants.SHIPPING_HAND);
			}
		}
		return mShippingHandling;
	}

	public void setShippingHandling(String shipping_handling) {
		this.mShippingHandling = shipping_handling;
	}

	public String getShippingHandlingInclTax() {
		if (null == mShippingHandlingInclTax) {

			if (null != getJSONV2()) {
				mShippingHandlingInclTax = getValueWithJSONV2(Constants.SHIPPING_HAND_INCL_TAX);
			}
			if (null == mShippingHandlingInclTax) {
				mShippingHandlingInclTax = getData(Constants.SHIPPING_HAND_INCL_TAX);
			}
		}
		return mShippingHandlingInclTax;
	}

	public void setShippingHandlingInclTax(String shipping_handling_incl_tax) {
		this.mShippingHandlingInclTax = shipping_handling_incl_tax;
	}

	public String getShippingHandlingExclTax() {
		if (null == mShippingHandlingExclTax) {
			if (null != getJSONV2()) {
				mShippingHandlingExclTax = getValueWithJSONV2(Constants.SHIPPING_HAND_EXCL_TAX);
			}
			if (null == mShippingHandlingExclTax) {
				mShippingHandlingExclTax = getData(Constants.SHIPPING_HAND_EXCL_TAX);
			}
		}

		return mShippingHandlingExclTax;
	}

	public void setShippingHandlingExclTax(String shipping_handling_excl_tax) {
		this.mShippingHandlingExclTax = shipping_handling_excl_tax;
	}

	public String getTax() {
		if (null == mTax) {
			if (null != getJSONV2()) {
				mTax = getValueWithJSONV2(Constants.TAX);
			}
			if (null == mTax) {
				mTax = getData(Constants.TAX);
			}
		}
		return mTax;
	}

	public void setTax(String tax) {
		this.mTax = tax;
	}

	public String getGrandTotal() {
		if (null == mGrandTotal) {
			if (null != getJSONV2()) {
				mGrandTotal = getValueWithJSONV2(Constants.GRAND_TOTAL);
			}

			if (null == mGrandTotal) {
				mGrandTotal = getData(Constants.GRAND_TOTAL);
			}
		}
		return mGrandTotal;
	}

	public void setGrandTotal(String grand_total) {
		this.mGrandTotal = grand_total;
	}

	public String getGrandTotalInclTax() {
		if (null == mGrandTotalInclTax) {
			if (null != getJSONV2()) {
				mGrandTotalInclTax = getValueWithJSONV2(Constants.GRAND_TOTAL_INCL_TAX);
			}
			if (null == mGrandTotalInclTax) {
				mGrandTotalInclTax = getData(Constants.GRAND_TOTAL_INCL_TAX);
			}
		}

		return mGrandTotalInclTax;
	}

	public void setGrandTotalInclTax(String grand_total_incl_tax) {
		this.mGrandTotalInclTax = grand_total_incl_tax;
	}

	public String getGrandTotalExclTax() {
		if (null == mGrandTotalExclTax) {

			if (null != getJSONV2()) {
				mGrandTotalExclTax = getValueWithJSONV2(Constants.GRAND_TOTAL_EXCL_TAX);
			}

			if (null == mGrandTotalExclTax) {
				mGrandTotalExclTax = getData(Constants.GRAND_TOTAL_EXCL_TAX);
			}
		}
		return mGrandTotalExclTax;
	}

	public void setGrandTotalExclTax(String grand_total_excl_tax) {
		this.mGrandTotalExclTax = grand_total_excl_tax;
	}

}
