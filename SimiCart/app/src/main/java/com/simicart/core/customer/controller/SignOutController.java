package com.simicart.core.customer.controller;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.model.SignOutModel;

public class SignOutController extends SimiController {

    protected SimiDelegate mDelegate;

    public void setDelegate(SimiDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void onStart() {
        if (mDelegate != null) {
            mDelegate.showDialogLoading();
        }
        mModel = new SignOutModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                if (mDelegate != null) {
                    mDelegate.dismissDialogLoading();
                }
                SimiManager.getIntance().getRequestQueue().clearCacheL1();
                showToastSignOut();
                DataLocal.isNewSignIn = false;
//					EventController event = new EventController();
//					event.dispatchEvent(
//							"com.simicart.core.customer.controller.SignInController",
//							"");
                DataPreferences.saveSignInState(false);
                DataPreferences.clearEmailPassowrd();
                SimiManager.getIntance().onUpdateCartQty("");
                if (DataLocal.isTablet) {
                    SimiManager.getIntance().clearAllChidFragment();
                    SimiManager.getIntance().removeDialog();
                } else {
                    SimiManager.getIntance().backPreviousFragment();
                }


//                HomeFragment fragment = HomeFragment.newInstance();
//                SimiManager.getIntance().replaceFragment(fragment);
            }
        });

        mModel.request();

    }

    private void showToastSignOut() {
        LayoutInflater inflater = SimiManager.getIntance().getCurrentActivity()
                .getLayoutInflater();
        View layout_toast = inflater
                .inflate(
                        Rconfig.getInstance().layout(
                                "core_custom_toast_productlist"),
                        (ViewGroup) SimiManager
                                .getIntance()
                                .getCurrentActivity()
                                .findViewById(
                                        Rconfig.getInstance().id(
                                                "custom_toast_layout")));
        TextView txt_toast = (TextView) layout_toast.findViewById(Rconfig
                .getInstance().id("txt_custom_toast"));
        Toast toast = new Toast(SimiManager.getIntance().getCurrentActivity());
        txt_toast.setText(SimiTranslator.getInstance().translate("Logout Success"));
        toast.setView(layout_toast);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 400);
        toast.show();
    }

    @Override
    public void onResume() {

    }

}
