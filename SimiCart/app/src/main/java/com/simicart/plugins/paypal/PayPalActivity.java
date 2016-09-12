package com.simicart.plugins.paypal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.simicart.MainActivity;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Rconfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class PayPalActivity extends AppCompatActivity {

    protected String CONFIG_CLIENT_ID = "AbwLSxDR0lE1ksdFL7YxfJlQ8VVmFCIbvoiO6adhbjb5vw2bWcJNnWXn";
    protected String CONFIG_ENVIROMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    protected String mBNCode = "Magestore_SI_MagentoCE";
    protected String mTotalPrice = "0.0";
    protected String mInvoiceNumber = "";
    protected int REQUEST_CODE_PAYMENT = 1;
    protected ProgressBar prb_loading;
    protected PaypalEntity mPaypalEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseExtras();
        setContentView(Rconfig.getInstance().layout("plugins_paypal_activty"));
        prb_loading = (ProgressBar) findViewById(Rconfig.getInstance().id("prb_loading"));
        prb_loading.setVisibility(View.GONE);

        PayPalConfiguration config = new PayPalConfiguration().environment(
                CONFIG_ENVIROMENT).clientId(CONFIG_CLIENT_ID);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        onBuyPressed();
    }

    protected void onBuyPressed() {
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        thingToBuy.bnCode(mBNCode);

        Intent intent = new Intent(PayPalActivity.this, PaymentActivity.class);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String environment) {
        return new PayPalPayment(new BigDecimal(mTotalPrice), AppStoreConfig.getInstance().getCurrencyCode()
                , "Total fee", environment);
    }


    protected void parseExtras() {
        Bundle extras = getIntent().getExtras();
        mPaypalEntity = (PaypalEntity) extras.getSerializable("data");

        mTotalPrice = mPaypalEntity.getTotalPrice();

        String client_id = mPaypalEntity.getClientID();
        if (Utils.validateString(client_id)) {
            CONFIG_CLIENT_ID = client_id;
        }

        if (mPaypalEntity.isSandbox()) {
            CONFIG_ENVIROMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
        } else {
            CONFIG_ENVIROMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
        }

        String bnCode = mPaypalEntity.getBNCode();
        if (Utils.validateString(bnCode)) {
            mBNCode = bnCode;
        }

        mInvoiceNumber = mPaypalEntity.getInvoiceNumber();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data
                    .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                requestUpdatePaypal(confirm.toJSONObject(), "1");
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            requestUpdatePaypal(null, "2");
            backMain();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            setErrorConnection("Error", "CurrencyCode is invalid. Please see the docs.");
        }
    }

    protected void requestUpdatePaypal(JSONObject jsonObject, String payment_status) {

        try {
            prb_loading.setVisibility(View.VISIBLE);
            PaypalModel paypalModel = new PaypalModel();
            paypalModel.setSuccessListener(new ModelSuccessCallBack() {
                @Override
                public void onSuccess(SimiCollection collection) {
                    prb_loading.setVisibility(View.VISIBLE);
                    backMain();
                }
            });

            paypalModel.setFailListener(new ModelFailCallBack() {
                @Override
                public void onFail(SimiError error) {
                    prb_loading.setVisibility(View.VISIBLE);
                }
            });

            if (null != jsonObject) {
                JSONObject data = new JSONObject();
                String response_type = jsonObject.getString("response_type");
                data.put("response_type", response_type);
                JSONObject dataClient = jsonObject.getJSONObject("client");
                data.put("client", dataClient);
                JSONObject dataResponse = jsonObject.getJSONObject("response");
                data.put("response", dataResponse);
                paypalModel.addBody("proof", data);
            }

            paypalModel.addBody("invoice_number", mInvoiceNumber);
            paypalModel.addBody("payment_status", payment_status);
            paypalModel.request();
        } catch (JSONException e) {

        }
    }

    protected void backMain() {
        Intent i = new Intent(PayPalActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    public void setErrorConnection(String title, String message) {
        ProgressDialog.Builder alertbox = new ProgressDialog.Builder(this);
        alertbox.setTitle(SimiTranslator.getInstance().translate(title));
        alertbox.setMessage(SimiTranslator.getInstance().translate(message));
        alertbox.setPositiveButton(SimiTranslator.getInstance().translate("OK")
                .toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog alertDialog = alertbox.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

}
