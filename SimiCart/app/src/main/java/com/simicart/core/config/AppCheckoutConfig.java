package com.simicart.core.config;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;

/**
 * Created by Glenn on 8/15/2016.
 */
public class AppCheckoutConfig extends SimiEntity {

    public static AppCheckoutConfig instance;
    private boolean mGuestCheckout;
    private boolean mEnableAgreements;
    private boolean mTaxVatShow;
    private String enable_guest_checkout = "enable_guest_checkout";
    private String enable_agreements = "enable_agreements";
    private String taxvat_show = "taxvat_show";

    public static AppCheckoutConfig getInstance() {
        if (instance == null) {
            instance = new AppCheckoutConfig();
        }
        return instance;
    }

    @Override
    public void parse() {

        if (hasKey(enable_guest_checkout)) {
            String guestCheckout = getData(enable_guest_checkout);
            if (Utils.TRUE(guestCheckout)) {
                setGuestCheckout(true);
            }
        }

        if (hasKey(enable_agreements)) {
            String agreement = getData(enable_agreements);
            if (Utils.TRUE(agreement)) {
                setenableAgreements(true);
            }
        }

        if (hasKey(taxvat_show)) {
            String taxvatShow = getData(taxvat_show);
            if (Utils.TRUE(taxvatShow)) {
                settaxVatShow(true);
            }
        }

    }

    public boolean isGuestCheckout() {
        return mGuestCheckout;
    }

    public void setGuestCheckout(boolean guestCheckout) {
        this.mGuestCheckout = guestCheckout;
    }

    public boolean isenableAgreements() {
        return mEnableAgreements;
    }

    public void setenableAgreements(boolean enableAgreements) {
        this.mEnableAgreements = enableAgreements;
    }

    public boolean istaxVatShow() {
        return mTaxVatShow;
    }

    public void settaxVatShow(boolean taxVatShow) {
        this.mTaxVatShow = taxVatShow;
    }
}
