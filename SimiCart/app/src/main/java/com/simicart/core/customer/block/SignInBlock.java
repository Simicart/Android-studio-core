package com.simicart.core.customer.block;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.SignInDelegate;
import com.simicart.core.material.ButtonRectangle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SignInBlock extends SimiBlock implements SignInDelegate {

    private ButtonRectangle btn_SignIn;
    private EditText edt_Email;
    private EditText edt_Password;
    private ImageView iv_ForgotPassword;
    private LinearLayout ll_signInLayout;
    private CheckBox cb_remember_password;
    private TextView txt_label_create_account;

    private String mEmail = "";
    private String mPassword = "";

    private ImageView img_email;
    private ImageView img_password;
    private View vSignInVisible;

    public SignInBlock(View view, Context context) {
        super(view, context);
        // TODO Auto-generated constructor stub
    }

    public void setEmailWatcher(TextWatcher watcher) {
        edt_Email.addTextChangedListener(watcher);
    }

    public void setPasswordWatcher(TextWatcher watcher) {
        edt_Password.addTextChangedListener(watcher);
    }

    public void setForgotPassClicker(OnClickListener clicker) {
        iv_ForgotPassword.setOnClickListener(clicker);
    }

    public void setCreateAccClicker(OnTouchListener clicker) {
        txt_label_create_account.setOnTouchListener(clicker);
    }

    public void setSingInClicker(OnClickListener clicker) {
        btn_SignIn.setOnClickListener(clicker);
    }

    public void setOutSideClicker(OnClickListener clicker) {
        ll_signInLayout.setOnClickListener(clicker);
    }

    public void setOnCheckBox(OnCheckedChangeListener click) {
        cb_remember_password.setOnCheckedChangeListener(click);
    }

    @Override
    public void initView() {

        vSignInVisible = (View) mView.findViewById(Rconfig
                .getInstance().id("vSignInVisible"));


        ll_signInLayout = (LinearLayout) mView.findViewById(Rconfig
                .getInstance().id("coreSignInLayout"));

        cb_remember_password = (CheckBox) mView.findViewById(Rconfig
                .getInstance().id("cb_re_password"));
        cb_remember_password.setText(SimiTranslator.getInstance().translate("Remember password"));
        cb_remember_password.setTextColor(AppColorConfig.getInstance().getContentColor());

        txt_label_create_account = (TextView) mView.findViewById(Rconfig
                .getInstance().id("lable_createAccount"));
        txt_label_create_account.setText(SimiTranslator.getInstance().translate("Don't have an account ") + "?");

        btn_SignIn = (ButtonRectangle) mView.findViewById(Rconfig.getInstance()
                .id("bt_signIn"));
        btn_SignIn.setText(SimiTranslator.getInstance().translate("Sign In"));
        btn_SignIn.setTextColor(AppColorConfig.getInstance().getButtonTextColor());
        btn_SignIn.setBackgroundColor(AppColorConfig.getInstance().getColorButtonBackground());
        btn_SignIn.setTextSize(Constants.SIZE_TEXT_BUTTON);
        vSignInVisible.setVisibility(View.GONE);

        // initial Email Field
        edt_Email = (EditText) mView.findViewById(Rconfig.getInstance().id(
                "et_email"));
        edt_Email.setHint(SimiTranslator.getInstance().translate("Email"));

        edt_Email.setTextColor(AppColorConfig.getInstance().getContentColor());
        edt_Email.setHintTextColor(AppColorConfig.getInstance().getContentColor());

        // initial Password Field
        edt_Password = (EditText) mView.findViewById(Rconfig.getInstance().id(
                "et_pass"));
        edt_Password.setHint(SimiTranslator.getInstance().translate("Password"));
        edt_Password.setTextColor(AppColorConfig.getInstance().getContentColor());
        edt_Password.setHintTextColor(AppColorConfig.getInstance().getContentColor());
        // initial Forgot Password
        iv_ForgotPassword = (ImageView) mView.findViewById(Rconfig
                .getInstance().id("iv_forgot_pass"));

        if (DataPreferences.getCheckRemember()) {
            cb_remember_password.setChecked(true);
            edt_Email.setText(DataPreferences.getEmailRemember());
            edt_Password.setText(DataPreferences.getPasswordRemember());
            btn_SignIn.setTextColor(Color.WHITE);
            btn_SignIn.setBackgroundColor(AppColorConfig.getInstance().getKeyColor());
        } else {
            if (mEmail != null && !mEmail.equals("")) {
                edt_Email.setText(mEmail);
            } else {
                edt_Email.setText("");
            }
            if (mPassword != null && !mPassword.equals("")) {
                edt_Password.setText(mPassword);
            } else {
                edt_Password.setText("");
            }
        }
        img_email = (ImageView) mView.findViewById(Rconfig.getInstance().id(
                "img_email"));
        img_password = (ImageView) mView.findViewById(Rconfig.getInstance().id(
                "iv_pass"));
        changeColorImageView(img_email, "ic_your_acc");
        changeColorImageView(img_password, "ic_your_pas");
        changeColorImageView(iv_ForgotPassword, "ic_forgot_pass");
    }

    private void changeColorImageView(ImageView imageView, String src) {
        Drawable icon = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable(src));
        icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        imageView.setImageDrawable(icon);
    }

    public void setVisibleSignIn(boolean visible) {
        if (visible == false) {
            vSignInVisible.setVisibility(View.GONE);
        } else {
            vSignInVisible.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public String getEmail() {
        return edt_Email.getText().toString();
    }

    @Override
    public String getPassword() {
        return edt_Password.getText().toString();
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    @Override
    public void showNotify(String message) {
        // SimiManager.getIntance().showNotify(null, message, "OK");
    }

    @Override
    public ButtonRectangle getSignIn() {
        return btn_SignIn;
    }

    @Override
    public View getViewFull() {
        return vSignInVisible;
    }
}
