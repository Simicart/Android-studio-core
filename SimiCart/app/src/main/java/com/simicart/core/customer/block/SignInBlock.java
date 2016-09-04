package com.simicart.core.customer.block;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.SignInDelegate;
import com.simicart.core.customer.entity.ProfileEntity;

public class SignInBlock extends SimiBlock implements SignInDelegate {

    private AppCompatButton btn_SignIn;
    private EditText edt_Email;
    private EditText edt_Password;
    private ImageView iv_ForgotPassword;
    private CheckBox cb_remember_password;
    private TextView txt_label_create_account;

    private String mEmail;
    private String mPassword;

    private ImageView img_email;
    private ImageView img_password;

    public SignInBlock(View view, Context context) {
        super(view, context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void initView() {

        initRememberPass();
        initButtonSignIn();
        initEmail();
        initPassword();

        iv_ForgotPassword = (ImageView) mView.findViewById(Rconfig
                .getInstance().id("iv_forgot_pass"));

        txt_label_create_account = (TextView) mView.findViewById(Rconfig
                .getInstance().id("lable_createAccount"));
        txt_label_create_account.setText(SimiTranslator.getInstance().translate("Don't have an account ") + "?");

        img_email = (ImageView) mView.findViewById(Rconfig.getInstance().id(
                "img_email"));
        img_password = (ImageView) mView.findViewById(Rconfig.getInstance().id(
                "iv_pass"));
        changeColorImageView(img_email, "ic_your_acc");
        changeColorImageView(img_password, "ic_your_pas");
        changeColorImageView(iv_ForgotPassword, "ic_forgot_pass");
    }

    private void initEmail() {
        edt_Email = (EditText) mView.findViewById(Rconfig.getInstance().id(
                "et_email"));
        edt_Email.setHint(SimiTranslator.getInstance().translate("Email"));

        edt_Email.setTextColor(AppColorConfig.getInstance().getContentColor());
        edt_Email.setHintTextColor(AppColorConfig.getInstance().getContentColor());

        if(mEmail != null) {
            edt_Email.setText(mEmail);
        } else if(Utils.validateString(DataPreferences.getEmailRemember())) {
            edt_Email.setText(DataPreferences.getEmailRemember());
        }

        edt_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = edt_Email.getText().toString();
                String password = edt_Password.getText().toString();
                if (email.length() != 0 && password.length() >= 6) {
                    changeColorSignIn(AppColorConfig.getInstance().getButtonBackground());
                } else {
                    changeColorSignIn(AppColorConfig.getInstance().getButtonBackground(Color.GRAY));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initPassword() {
        edt_Password = (EditText) mView.findViewById(Rconfig.getInstance().id(
                "et_pass"));
        edt_Password.setHint(SimiTranslator.getInstance().translate("Password"));
        edt_Password.setTextColor(AppColorConfig.getInstance().getContentColor());
        edt_Password.setHintTextColor(AppColorConfig.getInstance().getContentColor());

        if(mPassword != null) {
            edt_Password.setText(mPassword);
        } else if(Utils.validateString(DataPreferences.getPasswordRemember())) {
            edt_Password.setText(DataPreferences.getPasswordRemember());
        }

        edt_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = edt_Email.getText().toString();
                String password = edt_Password.getText().toString();
                if (email.length() != 0 && password.length() >= 6) {
                    changeColorSignIn(AppColorConfig.getInstance().getButtonBackground());
                } else {
                    changeColorSignIn(AppColorConfig.getInstance().getButtonBackground(Color.GRAY));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initButtonSignIn() {
        btn_SignIn = (AppCompatButton) mView.findViewById(Rconfig.getInstance()
                .id("bt_signIn"));
        btn_SignIn.setText(SimiTranslator.getInstance().translate("Sign In"));
        btn_SignIn.setTextColor(AppColorConfig.getInstance().getButtonTextColor());

        if(Utils.validateString(DataPreferences.getEmailRemember()) && Utils.validateString(DataPreferences.getPasswordRemember())
                || Utils.validateString(mEmail) && Utils.validateString(mPassword)) {
            btn_SignIn.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
        } else {
            btn_SignIn.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground(Color.GRAY));
        }
    }

    private void initRememberPass() {
        cb_remember_password = (CheckBox) mView.findViewById(Rconfig
                .getInstance().id("cb_re_password"));
        cb_remember_password.setText(SimiTranslator.getInstance().translate("Remember password"));
        cb_remember_password.setTextColor(AppColorConfig.getInstance().getContentColor());

        if(DataPreferences.getCheckRemember() == true) {
            cb_remember_password.setChecked(true);
        }
    }

    private void changeColorImageView(ImageView imageView, String src) {
        Drawable icon = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable(src));
        icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                PorterDuff.Mode.SRC_ATOP);
        imageView.setImageDrawable(icon);
    }

    @Override
    public void changeColorSignIn(ColorStateList color) {
        btn_SignIn.setSupportBackgroundTintList(color);
    }

    @Override
    public ProfileEntity getProfileSignIn() {
        ProfileEntity entity = null;
        String email = edt_Email.getText().toString();
        String pass = edt_Password.getText().toString();
        boolean isValidate = true;

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()) {
            SimiNotify.getInstance().showNotify(SimiTranslator.getInstance().translate(
                    "Invalid email address"));
            isValidate = false;
        }
        if (!Utils.validateString(email)) {
            SimiNotify.getInstance().showNotify(SimiTranslator.getInstance().translate(
                    "Email is empty.Please input an email."));
            isValidate = false;
        }
        if (!Utils.validateString(pass)) {
            SimiNotify.getInstance().showNotify(SimiTranslator.getInstance().translate(
                    "Password is empty.Please input a password."));
            isValidate = false;
        }

        if(!isValidate) {
            return null;
        }

        entity = new ProfileEntity();
        entity.setEmail(email);
        entity.setCurrentPass(pass);

        return entity;
    }

    public void setForgotPassClicker(OnClickListener clicker) {
        iv_ForgotPassword.setOnClickListener(clicker);
    }

    public void setCreateAccClicker(OnTouchListener clicker) {
        txt_label_create_account.setOnTouchListener(clicker);
    }

    public void setSignInClicker(OnClickListener clicker) {
        btn_SignIn.setOnClickListener(clicker);
    }

    public void setRememberPassClicker(OnCheckedChangeListener click) {
        cb_remember_password.setOnCheckedChangeListener(click);
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

}
