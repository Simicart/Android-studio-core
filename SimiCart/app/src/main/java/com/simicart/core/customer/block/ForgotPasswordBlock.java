package com.simicart.core.customer.block;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.ForgotPasswordDelegate;
import com.simicart.core.material.ButtonRectangle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class ForgotPasswordBlock extends SimiBlock implements
		ForgotPasswordDelegate {

	protected AppCompatButton btn_Send;
	protected EditText edt_Email;
	private TextView lable_email;

	public ForgotPasswordBlock(View view, Context context) {
		super(view, context);
		// TODO Auto-generated constructor stub
	}

	public void setOnClicker(OnClickListener clicker) {
		btn_Send.setOnClickListener(clicker);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initView() {
		lable_email = (TextView) mView.findViewById(Rconfig
				.getInstance().id("lable_email"));
		lable_email.setTextColor(Color.GRAY);
		lable_email.setText(SimiTranslator.getInstance().translate("Enter Your Email")
				.toUpperCase()
				+ ":");

		// button sent
		btn_Send = (AppCompatButton) mView.findViewById(Rconfig.getInstance().id(
				"bt_send"));
		btn_Send.setText(SimiTranslator.getInstance().translate("Reset my password"));
		btn_Send.setTextColor(Color.WHITE);
		btn_Send.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground(Color.GRAY));
		btn_Send.setTextSize(Constants.SIZE_TEXT_BUTTON);
		btn_Send.setClickable(false);

		// Email Field
		initEmail();
		
		lable_email.setTextColor(AppColorConfig.getInstance().getContentColor());
		edt_Email.setTextColor(AppColorConfig.getInstance().getContentColor());
	}

	private void initEmail() {
		edt_Email = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_email"));
		edt_Email.setHint(SimiTranslator.getInstance().translate("Email"));
		edt_Email.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				validateEmail();
			}
		});
	}

	private boolean isValidEmail(String email) {
		return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	private boolean validateEmail() {
		String email = edt_Email.getText().toString().trim();

		if (email.isEmpty() || !isValidEmail(email)) {
			btn_Send.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground(Color.GRAY));
			btn_Send.setClickable(false);
			return false;
		} else {
			btn_Send.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
			btn_Send.setClickable(true);
		}

		return true;
	}

	@Override
	public String getEmail() {
		return edt_Email.getText().toString();
	}

}
