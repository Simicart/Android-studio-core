package com.trueplus.simicart.braintreelibrary.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.trueplus.simicart.braintreelibrary.BuildConfig;
import com.trueplus.simicart.braintreelibrary.exceptions.InvalidArgumentException;

public class TokenizationKey extends Authorization implements Parcelable {

    protected static String MATCHER = "^[a-zA-Z0-9_]+_[a-zA-Z0-9]+_[a-zA-Z0-9_]+$";

    private final String mEnvironment;
    private final String mMerchantId;
    private final String mUrl;

    TokenizationKey(String tokenizationKey) throws InvalidArgumentException {
        super(tokenizationKey);

        String[] tokenizationKeyParts = tokenizationKey.split("_", 3);
        mEnvironment = tokenizationKeyParts[0];
        mMerchantId = tokenizationKeyParts[2];
        mUrl = BraintreeEnvironment.getUrl(mEnvironment) + "merchants/" +
                mMerchantId + "/client_api/";
    }

    public String getEnvironment() {
        return mEnvironment;
    }

    public String getMerchantId() {
        return mMerchantId;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    public String getConfigUrl() {
        return mUrl + "v1/configuration";
    }

    private enum BraintreeEnvironment {

        DEVELOPMENT("development", "https://dev-api.jajahub.com/"),
        SANDBOX("sandbox", "https://api.sandbox.braintreegateway.com/"),
        PRODUCTION("production", "https://api.braintreegateway.com/");

        private String mEnvironment;
        private String mUrl;

        BraintreeEnvironment(String environment, String url) {
            mEnvironment = environment;
            mUrl = url;
        }

        static String getUrl(String environment) throws InvalidArgumentException {
            for (BraintreeEnvironment braintreeEnvironment : BraintreeEnvironment.values()) {
                if (braintreeEnvironment.mEnvironment.equals(environment)) {
                    return braintreeEnvironment.mUrl;
                }
            }
            throw new InvalidArgumentException("Tokenization Key contained invalid environment");
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mEnvironment);
        dest.writeString(mMerchantId);
        dest.writeString(mUrl);
    }

    protected TokenizationKey(Parcel in) {
        super(in);
        mEnvironment = in.readString();
        mMerchantId = in.readString();
        mUrl = in.readString();
    }

    public static final Creator<TokenizationKey> CREATOR = new Creator<TokenizationKey>() {
        public TokenizationKey createFromParcel(Parcel source) {
            return new TokenizationKey(source);
        }

        public TokenizationKey[] newArray(int size) {
            return new TokenizationKey[size];
        }
    };
}
