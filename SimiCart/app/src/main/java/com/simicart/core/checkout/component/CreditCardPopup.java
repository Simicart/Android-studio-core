package com.simicart.core.checkout.component;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.simicart.core.base.component.SimiParentRowComponent;
import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.component.SimiSpinnerRowComponent;
import com.simicart.core.base.component.SimiTextRowComponent;
import com.simicart.core.base.component.callback.SpinnerRowCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.adapter.CardAdapter;
import com.simicart.core.checkout.adapter.DateAdapter;
import com.simicart.core.checkout.delegate.CreditCardCallBack;
import com.simicart.core.checkout.entity.CreditCard;
import com.simicart.core.checkout.entity.PaymentMethodEntity;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by frank on 26/08/2016.
 */
public class CreditCardPopup {

    protected ArrayList<SimiRowComponent> mListRow;
    protected PaymentMethodEntity mPaymentMethodEntity;
    protected CreditCard mCreditCard;
    protected LayoutInflater mInflater;
    protected AppCompatButton btnSave;
    protected Context mContext;
    protected ProgressDialog ppFilter;
    protected String mCurrentMonth;
    protected String mYear;
    protected int topMargin = Utils.toDp(10);
    protected String mTypeCard;
    protected SimiTextRowComponent nameComponent;
    protected SimiTextRowComponent numberComponent;
    protected SimiTextRowComponent CCIDComponent;
    protected CreditCardCallBack mCallBack;


    public CreditCardPopup(PaymentMethodEntity entity) {
        mPaymentMethodEntity = entity;
        if (null != mPaymentMethodEntity) {
            mCreditCard = mPaymentMethodEntity.getCurrentCardEntity();
        }
        mContext = SimiManager.getIntance().getCurrentActivity();
        mInflater = LayoutInflater.from(mContext);
        createPopup();
    }

    protected void createPopup() {
        ppFilter = ProgressDialog.show(mContext, null, null, true, false);
        View contentView = createView();
        ppFilter.setContentView(contentView);
        ppFilter.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        ppFilter.setCanceledOnTouchOutside(true);
    }

    public View createView() {
        int idView = Rconfig.getInstance().layout("core_popup_credit_card");
        View rootView = mInflater.inflate(idView, null);
        LinearLayout llBody = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_body"));

        btnSave = (AppCompatButton) rootView.findViewById(Rconfig.getInstance().id("btn_save"));
        String translateSave = SimiTranslator.getInstance().translate("Save");
        btnSave.setText(translateSave);
        btnSave.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
        btnSave.setTextColor(AppColorConfig.getInstance().getContentColor());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCard();
            }
        });


        mListRow = new ArrayList<>();
        createRow();
        llBody.removeAllViewsInLayout();

        for (int i = 0; i < mListRow.size(); i++) {
            View view = mListRow.get(i).createView();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = topMargin;
            llBody.addView(view, params);
        }

        return rootView;
    }

    protected void createRow() {
        // name on card
        nameComponent = new SimiTextRowComponent();
        nameComponent.setInputType(InputType.TYPE_CLASS_TEXT);
        nameComponent.setTitle("Name On Card");
        nameComponent.setKey("");
        nameComponent.setRequired(true);
        mListRow.add(nameComponent);


        // credit card type
        initListCardAdapter();


        // credit card number
        numberComponent = new SimiTextRowComponent();
        numberComponent.setInputType(InputType.TYPE_CLASS_NUMBER);
        numberComponent.setTitle("Credit Card Number");
        numberComponent.setKey("card_number");
        numberComponent.setRequired(true);
        mListRow.add(numberComponent);


        // expiration date
        SimiRowComponent yearComponent = initYearComponent();
        SimiRowComponent monthComponent = initMonthComponent();
        ArrayList<SimiRowComponent> monthYear = new ArrayList<>();
        monthYear.add(monthComponent);
        monthYear.add(yearComponent);
        SimiParentRowComponent expirationComponent = new SimiParentRowComponent(monthYear);
        mListRow.add(expirationComponent);

        // card verification number
        CCIDComponent = new SimiTextRowComponent();
        CCIDComponent.setInputType(InputType.TYPE_CLASS_NUMBER);
        CCIDComponent.setTitle("Card Vefification Number");
        CCIDComponent.setKey("cc_id");
        CCIDComponent.setRequired(true);
        mListRow.add(CCIDComponent);
    }

    public void show() {
        ppFilter.show();
    }

    public void dismiss() {
        if (ppFilter.isShowing()) {
            ppFilter.dismiss();
        }
    }

    protected void saveCard() {
        CreditCard creditCard = new CreditCard();

        // name
        String name = String.valueOf(nameComponent.getValue());
        if (name.equals("")) {
            return;
        }
        creditCard.setName(name);

        // type
        if (!Utils.validateString(mTypeCard)) {
            return;
        }
        creditCard.setType(mTypeCard);

        // number
        String number = String.valueOf(numberComponent.getValue());
        if (name.equals("")) {
            return;
        }
        creditCard.setNumber(number);

        // month
        if (!Utils.validateString(mCurrentMonth)) {
            return;
        }
        creditCard.setExpMonth(mCurrentMonth);

        // year
        if (!Utils.validateString(mYear)) {
            return;
        }
        creditCard.setExpYear(mYear);

        // card verification number
        String ccid = String.valueOf(CCIDComponent.getValue());
        if (ccid.equals("")) {
            return;
        }
        creditCard.setCId(ccid);

        JSONObject jsonSave = creditCard.toData();
        if (null != jsonSave) {
            saveCreditCardToLocal(jsonSave);
        }

        mPaymentMethodEntity.setCurrentCardEntity(creditCard);
        if (null != mCallBack) {
            mCallBack.onSaveCreditCard(mPaymentMethodEntity);
        }
        ppFilter.dismiss();
    }

    protected void saveCreditCardToLocal(JSONObject jsonSave) {
        String email = DataPreferences.getEmail();
        if (Utils.validateString(email)) {

        }
    }

    protected void initListCardAdapter() {
        final ArrayList<CreditCard> cards = mPaymentMethodEntity.getListCCType();
        if (null != cards && cards.size() > 0) {
            SimiSpinnerRowComponent typeComponent = new SimiSpinnerRowComponent();
            typeComponent.setTitle("Credit Card Type");
            typeComponent.setKey("card_type");
            CardAdapter cardAdapter = new CardAdapter(cards);
            typeComponent.setAdapter(cardAdapter);
            typeComponent.setRequired(true);
            typeComponent.setCallBack(new SpinnerRowCallBack() {
                @Override
                public void onSelect(int position) {
                    CreditCard creditCard = cards.get(position);
                    mTypeCard = creditCard.getCode();
                }
            });
            mListRow.add(typeComponent);
        }
    }

    protected SimiRowComponent initMonthComponent() {

        final ArrayList<String> months = new ArrayList<>(Arrays.asList("January - 01", "February - 02",
                "March - 03", "April - 04", "May - 05", "June - 06",
                "July - 07", "August -  08", "September - 09", "October - 10",
                "November - 11", "December - 12"));

        SimiSpinnerRowComponent monthComponent = new SimiSpinnerRowComponent();
        monthComponent.setTitle("Month");
        monthComponent.setCallBack(new SpinnerRowCallBack() {
            @Override
            public void onSelect(int position) {
                String month = months.get(position);
                String[] splits = month.split("-");
                mCurrentMonth = splits[1].trim();
            }
        });


        DateAdapter monthAdapter = new DateAdapter(months);
        monthComponent.setAdapter(monthAdapter);

        return monthComponent;
    }

    protected SimiRowComponent initYearComponent() {
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        final ArrayList<String> years = new ArrayList<>();
        years.add(String.valueOf(curYear));
        for (int i = 0; i < 20; i++) {
            int year = curYear + i;
            years.add(String.valueOf(year));
        }

        DateAdapter yearAdapter = new DateAdapter(years);
        SimiSpinnerRowComponent yearComponent = new SimiSpinnerRowComponent();
        yearComponent.setAdapter(yearAdapter);
        yearComponent.setTitle("Year");
        yearComponent.setCallBack(new SpinnerRowCallBack() {
            @Override
            public void onSelect(int position) {
                mYear = years.get(position);
            }
        });

        return yearComponent;
    }

    public void setCallBack(CreditCardCallBack callBack) {
        mCallBack = callBack;
    }


}
