package com.simicart.core.checkout.component;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.simicart.core.base.component.SimiRowComponent;
import com.simicart.core.base.component.SimiSpinnerRowComponent;
import com.simicart.core.base.component.SimiTextRowComponent;
import com.simicart.core.checkout.entity.CreditCard;
import com.simicart.core.checkout.entity.PaymentMethodEntity;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by frank on 26/08/2016.
 */
public class CreditCardPopup {

    protected ArrayList<SimiRowComponent> mListRow;
    protected PaymentMethodEntity mPaymentMethodEntity;
    protected CreditCard mCreditCard;
    protected LayoutInflater mInflater;

    public CreditCardPopup(PaymentMethodEntity entity) {
        mPaymentMethodEntity = entity;
    }

    public View createView() {
        int idView = Rconfig.getInstance().layout("core_popup_credit_card");
        View rootView = mInflater.inflate(idView, null);
        LinearLayout llBody = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id(""));
        mListRow = new ArrayList<>();
        createRow();

        for (int i = 0; i < mListRow.size(); i++) {
            View view = mListRow.get(i).createView();
            llBody.addView(view);
        }

        return rootView;
    }

    protected void createRow() {
        // name on card
        SimiTextRowComponent nameComponent = new SimiTextRowComponent();
        nameComponent.setInputType(InputType.TYPE_CLASS_TEXT);
        nameComponent.setTitle("Name On Card");
        nameComponent.setKey("");
        if (null != mPaymentMethodEntity) {
        }
        mListRow.add(nameComponent);


        // credit card type
        SimiSpinnerRowComponent typeComponent = new SimiSpinnerRowComponent();
        typeComponent.setTitle("Credit Card Type");
        typeComponent.setKey("card_type");

        mListRow.add(typeComponent);


        // credit card number
        SimiTextRowComponent numberComponent = new SimiTextRowComponent();
        numberComponent.setInputType(InputType.TYPE_CLASS_NUMBER);
        numberComponent.setTitle("Credit Card Number");
        numberComponent.setKey("card_number");
        mListRow.add(numberComponent);


        // expiration date

        // card verification number
        SimiTextRowComponent CCIDComponent = new SimiTextRowComponent();
        CCIDComponent.setInputType(InputType.TYPE_CLASS_NUMBER);
        CCIDComponent.setTitle("Card Vefification Number");
        CCIDComponent.setKey("cc_id");
        mListRow.add(CCIDComponent);
    }

}
