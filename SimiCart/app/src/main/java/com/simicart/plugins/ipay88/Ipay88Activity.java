package com.simicart.plugins.ipay88;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ipay.Ipay;
import com.ipay.IpayPayment;
import com.ipay.IpayResultDelegate;
import com.simicart.MainActivity;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.entity.OrderInforEntity;
import com.simicart.core.checkout.entity.PaymentMethodEntity;
import com.simicart.core.checkout.entity.ReviewOrderEntity;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

import java.util.HashMap;

public class Ipay88Activity extends AppCompatActivity {


    protected HashMap<String, Object> mData;
    protected String mTotalPrice;
    protected String mInvoiceNumber = "";
    protected OrderInforEntity mOrderInforEntity;
    protected PaymentMethodEntity mPaymentMethodEntity;
    int REQUEST_CODE_PAYMENT = 1;
    protected ProgressBar prb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Rconfig.getInstance().layout("plugins_ipay88_activity"));
        prb_loading = (ProgressBar) findViewById(Rconfig.getInstance().id("prb_loading"));
        prb_loading.setVisibility(View.GONE);
        parseExtras();
        parseData();

        IpayPayment ipayPayment = createIpayPayment();
        IpayResultDelegate delegate = new IpayResultDelegate() {
            @Override
            public void onPaymentSucceeded(String TransId, String RefNo, String Amount,
                                           String Remark, String AuthCode) {
                requestUpdateIpay(TransId, AuthCode, RefNo, "1", mInvoiceNumber);
            }

            @Override
            public void onPaymentFailed(String TransId, String RefNo, String Amount,
                                        String Remark, String AuthCode) {
                showError(AuthCode);
                requestUpdateIpay(TransId, AuthCode, RefNo, "2", mInvoiceNumber);
            }

            @Override
            public void onPaymentCanceled(String TransId, String RefNo, String Amount,
                                          String Remark, String AuthCode) {
                showError(AuthCode);
                requestUpdateIpay(TransId, AuthCode, RefNo, "2", mInvoiceNumber);
            }

            @Override
            public void onRequeryResult(String s, String s1, String s2, String s3) {

            }
        };
        Intent checkoutIntent = Ipay.getInstance().checkout(ipayPayment,
                Ipay88Activity.this, delegate);
        startActivityForResult(checkoutIntent, REQUEST_CODE_PAYMENT);

    }

    protected IpayPayment createIpayPayment() {
        IpayPayment payment = new IpayPayment();
        String currency_code = "";
        String country_id = "";

        String is_sandbox = mPaymentMethodEntity.getData("is_sandbox");
        if (Utils.validateString(is_sandbox) && is_sandbox.equals("1")) {
            currency_code = mOrderInforEntity.getData("currency_code");
            country_id = mOrderInforEntity.getData("country_id");
        } else {
            currency_code = "MYR";
            country_id = "MY";
        }
        String amount = mOrderInforEntity.getData("amount");

        payment.setCountry(country_id);
        payment.setCurrency(currency_code);
        payment.setAmount(amount);

        String merchantKey = mPaymentMethodEntity.getData("merchant_key");
        payment.setMerchantKey(merchantKey);

        String merchantCode = mPaymentMethodEntity.getData("merchant_code");
        payment.setMerchantCode(merchantCode);

        String productDes = mOrderInforEntity.getData("product_des");
        payment.setProdDesc(productDes);

        String name = mOrderInforEntity.getData("name");
        payment.setUserName(name);

        String email = mOrderInforEntity.getData("email");
        payment.setUserEmail(email);

        String contact = mOrderInforEntity.getData("contact");
        payment.setUserContact(contact);

        payment.setRefNo(mInvoiceNumber);
        payment.setPaymentId("");
        payment.setRemark("Success");
        payment.setLang("ISO-8859-1");
        payment.setBackendPostURL("https://www.mobile88.com/epayment/report/testsignature_response.asp");

        return payment;
    }

    protected void parseExtras() {
        Bundle extras = getIntent().getExtras();
        Bundle data = extras.getBundle("bundle");
        SimiData simiData = data.getParcelable("data");
        mData = simiData.getData();

    }


    protected void parseData() {
        if (mData.containsKey("total_price")) {
            TotalPrice totalPrice = (TotalPrice) mData.get("total_price");
            parseTotalPrice(totalPrice);
        }

        if (mData.containsKey("payment_method")) {
            PaymentMethodEntity paymentMethodEntity = (PaymentMethodEntity) mData.get("payment_method");
            parsePaymentMethod(paymentMethodEntity);
        }

        if (mData.containsKey("review_order_entity")) {
            ReviewOrderEntity reviewOrderEntity = (ReviewOrderEntity) mData.get("review_order_entity");
        }

        if (mData.containsKey("order_infor_entity")) {
            OrderInforEntity orderInforEntity = (OrderInforEntity) mData.get("order_infor_entity");
            parseOrderInfor(orderInforEntity);
        }
    }

    protected void parseTotalPrice(TotalPrice totalPrice) {
        String grandTotal = totalPrice.getGrandTotal();
        if (Utils.validateString(grandTotal)) {
            mTotalPrice = grandTotal;
            return;
        }

        String grandTotalIncl = totalPrice.getGrandTotalInclTax();
        if (Utils.validateString(grandTotalIncl)) {
            mTotalPrice = grandTotalIncl;
            return;
        }

        String grandTotalExcl = totalPrice.getGrandTotalExclTax();
        if (Utils.validateString(grandTotalExcl)) {
            mTotalPrice = grandTotalExcl;
            return;
        }
    }


    protected void parsePaymentMethod(PaymentMethodEntity paymentMethodEntity) {
        mPaymentMethodEntity = paymentMethodEntity;
    }

    protected void parseOrderInfor(OrderInforEntity orderInforEntity) {
        mOrderInforEntity = orderInforEntity;
        mInvoiceNumber = orderInforEntity.getData("invoice_number");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                SimiNotify.getInstance().showToast("Your order has been canceled");
                Intent i = new Intent(Ipay88Activity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        }
    }

    public void requestUpdateIpay(String transcation_id, String auth_code,
                                  String ref_no, String status, String order_id) {
        prb_loading.setVisibility(View.VISIBLE);
        IPay88Model iPay88Model = new IPay88Model();
        iPay88Model.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                prb_loading.setVisibility(View.GONE);
                backMain();
            }
        });

        iPay88Model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                prb_loading.setVisibility(View.GONE);
                backMain();
            }
        });

        iPay88Model.addBody("transaction_id", transcation_id);
        iPay88Model.addBody("auth_code", auth_code);
        iPay88Model.addBody("ref_no", ref_no);
        iPay88Model.addBody("status", status);
        iPay88Model.addBody("order_id", order_id);
        iPay88Model.request();

    }

    protected void showError(String message) {
        Toast toast = Toast.makeText(this, SimiTranslator.getInstance().translate
                (message), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    protected void backMain() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

}
