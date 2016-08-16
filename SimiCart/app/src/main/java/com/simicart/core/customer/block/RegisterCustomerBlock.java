package com.simicart.core.customer.block;

import java.util.Calendar;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.adapter.GenderAdapter;
import com.simicart.core.customer.delegate.RegisterCustomerDelegate;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.RegisterCustomer;
import com.simicart.core.material.ButtonRectangle;
import com.simicart.core.material.LayoutRipple;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class RegisterCustomerBlock extends SimiBlock implements
		RegisterCustomerDelegate {
	protected EditText edt_prefix;
	protected EditText edt_name;
	protected EditText edt_suffix;
	protected EditText edt_email;
	protected EditText edt_taxvat;
	protected EditText edt_pass;
	protected EditText edt_confirmPass;
//	 protected TextView tv_labelRegister;
//	 protected TextView tv_labelRequired;
	protected TextView tv_dateBirth;
	protected RelativeLayout rl_gender;
	protected Spinner sp_gender;
	protected ButtonRectangle btn_register;
	protected TextView tv_gender;
	protected ConfigCustomerAddress mCustomer;
	private ImageView img_gender;

	protected String mDay;
	protected String mMonth;
	protected String mYear;

	private RelativeLayout rlt_image_extend;
	private LayoutRipple layout_date_of_birt;

	public RegisterCustomerBlock(View view, Context context) {
		super(view, context);
		mCustomer = DataLocal.ConfigCustomerProfile;
	}

	public void setOnClickTextViewGender(OnClickListener listener) {
		tv_gender.setOnClickListener(listener);
	}

	public void setOnClickRelativeLayout(OnClickListener listener) {
		rlt_image_extend.setOnClickListener(listener);
	}

	public void setRegisterClick(OnClickListener clicker) {
		btn_register.setOnClickListener(clicker);
	}

	@SuppressLint("DefaultLocale")
	@SuppressWarnings("deprecation")
	@Override
	public void initView() {
		edt_prefix = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_prefix_show"));
		edt_suffix = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_suffix_show"));
		edt_name = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_fullname"));
		edt_name.setHint(SimiTranslator.newInstance().translate("Full Name") + "(*)");
		edt_email = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_email"));
		edt_email.setHint(SimiTranslator.newInstance().translate("Email") + "(*)");
		edt_pass = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_pass"));
		edt_pass.setHint(SimiTranslator.newInstance().translate("Password") + "(*)");
		edt_confirmPass = (EditText) mView.findViewById(Rconfig.getInstance()
				.id("et_confirm_pass"));
		edt_confirmPass.setHint(SimiTranslator.newInstance().translate("Confirm Password") + "(*)");
		edt_taxvat = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_taxvat_show"));
		// set text color
		edt_prefix.setTextColor(AppColorConfig.getInstance().getContentColor());
		edt_suffix.setTextColor(AppColorConfig.getInstance().getContentColor());
		edt_name.setTextColor(AppColorConfig.getInstance().getContentColor());
		edt_email.setTextColor(AppColorConfig.getInstance().getContentColor());
		edt_pass.setTextColor(AppColorConfig.getInstance().getContentColor());
		edt_confirmPass.setTextColor(AppColorConfig.getInstance().getContentColor());
		edt_taxvat.setTextColor(AppColorConfig.getInstance().getContentColor());

		edt_prefix
				.setHintTextColor(AppColorConfig.getInstance().getContentColor());
		edt_suffix
				.setHintTextColor(AppColorConfig.getInstance().getContentColor());
		edt_name.setHintTextColor(AppColorConfig.getInstance().getContentColor());
		edt_email.setHintTextColor(AppColorConfig.getInstance().getContentColor());
		edt_pass.setHintTextColor(AppColorConfig.getInstance().getContentColor());
		edt_confirmPass.setHintTextColor(AppColorConfig.getInstance().getContentColor());
		edt_taxvat
				.setHintTextColor(AppColorConfig.getInstance().getContentColor());
		// end set text color
		rl_gender = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
				.id("rl_gender"));
		sp_gender = (Spinner) rl_gender.findViewById(Rconfig.getInstance().id(
				"sp_gender"));

		tv_gender = (TextView) rl_gender.findViewById(Rconfig.getInstance().id(
				"tv_gender"));
		tv_gender.setTextColor(AppColorConfig.getInstance().getContentColor());
		tv_dateBirth = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_date_birth"));
		tv_dateBirth.setTextColor(AppColorConfig.getInstance().getContentColor());
		btn_register = (ButtonRectangle) mView.findViewById(Rconfig
				.getInstance().id("bt_register"));
		btn_register.setText(SimiTranslator.newInstance().translate("Register"));
		btn_register.setTextColor(Color.WHITE);
		btn_register.setBackgroundColor(AppColorConfig.getInstance().getKeyColor());
		btn_register.setTextSize(Constants.SIZE_TEXT_BUTTON);

		rlt_image_extend = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("rlt_image_extend"));
		layout_date_of_birt = (LayoutRipple) mView.findViewById(Rconfig
				.getInstance().id("layout_date_of_birt"));

		img_gender = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"im_extend"));
		Drawable icon = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_extend"));
		icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
				PorterDuff.Mode.SRC_ATOP);
		img_gender.setImageDrawable(icon);
		createPrefix();
		createSuffix();
		createTaxVat();
		createGender();
		createDateBirth();
	}

	private void createPrefix() {
		edt_prefix.setHint(SimiTranslator.newInstance().translate("Prefix") + " (*)");
		switch (mCustomer.getPrefix().toLowerCase()) {
		case "":
			edt_prefix.setVisibility(View.GONE);
			break;
		case "req":
			edt_prefix.setHint(SimiTranslator.newInstance().translate("Prefix") + "(*)");
			break;
		case "opt":
			edt_prefix.setHint(SimiTranslator.newInstance().translate("Prefix"));
			break;
		default:
			break;
		}
	}

	private void createSuffix() {
		edt_suffix.setHint(SimiTranslator.newInstance().translate("Suffix") + " (*)");
		switch (mCustomer.getSuffix().toLowerCase()) {
		case "":
			edt_suffix.setVisibility(View.GONE);
			break;
		case "req":
			edt_suffix.setHint(SimiTranslator.newInstance().translate("Suffix") + "(*)");
			break;
		case "opt":
			edt_suffix.setHint(SimiTranslator.newInstance().translate("Suffix"));
			break;
		default:
			break;
		}
	}

	private void createTaxVat() {
		edt_taxvat.setHint(SimiTranslator.newInstance().translate("Tax/VAT number")
				+ " (*)");
		switch (mCustomer.getTaxvat().toLowerCase()) {
		case "":
			edt_taxvat.setVisibility(View.GONE);
			break;
		case "req":
			edt_taxvat.setHint(SimiTranslator.newInstance().translate("Tax/VAT number")
					+ "(*)");
			break;
		case "opt":
			edt_taxvat.setHint(SimiTranslator.newInstance().translate("Tax/VAT number"));
			break;
		default:
			break;
		}
	}

	private void createGender() {
		tv_gender.setText(SimiTranslator.newInstance().translate("Gender") + "(*):");
		GenderAdapter adapter = new GenderAdapter(mContext);
		sp_gender.setAdapter(adapter);
		switch (mCustomer.getGender().toLowerCase()) {
		case "":
			rl_gender.setVisibility(View.GONE);
			break;
		case "req":
			tv_gender.setText(SimiTranslator.newInstance().translate("Gender") + "(*):");
			break;
		case "opt":
			tv_gender.setText(SimiTranslator.newInstance().translate("Gender") + ":");
			break;
		default:
			break;
		}

	}

	private void createDateBirth() {
		Calendar cDate = Calendar.getInstance();
		final int cDay = cDate.get(Calendar.DAY_OF_MONTH);
		final int cMonth = cDate.get(Calendar.MONTH);
		final int cYear = cDate.get(Calendar.YEAR);
		tv_dateBirth.setTextColor(AppColorConfig.getInstance().getContentColor());
		switch (mCustomer.getDob().toLowerCase()) {
		case "":
			layout_date_of_birt.setVisibility(View.GONE);
			break;
		case "req":
			tv_dateBirth.setHint(SimiTranslator.newInstance().translate("Date of Birth")
					+ "(*)");
			break;
		case "opt":
			tv_dateBirth.setHint(SimiTranslator.newInstance().translate("Date of Birth"));
			break;

		default:
			break;
		}

		layout_date_of_birt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(
						mContext, onDateSet, cYear, cMonth, cDay);
				datePickerDialog.show();
			}
		});
	}

	private OnDateSetListener onDateSet = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			int sYear = year;
			int sMonth = monthOfYear + 1;
			int sDay = dayOfMonth;
			mDay = String.valueOf(sDay);
			mMonth = String.valueOf(sMonth);
			mYear = String.valueOf(sYear);
			String selectedDate = new StringBuilder().append(sDay).append("/")
					.append(sMonth).append("/").append(sYear).append(" ")
					.toString();
			tv_dateBirth.setTextColor(AppColorConfig.getInstance().getContentColor());
			switch (mCustomer.getDob().toLowerCase()) {
			case "":
				layout_date_of_birt.setVisibility(View.GONE);
				break;
			case "req":
				tv_dateBirth.setText(SimiTranslator.newInstance().translate("Date of Birth")
						+ "(*): " + selectedDate);
				break;
			case "opt":
				tv_dateBirth.setText(SimiTranslator.newInstance().translate("Date of Birth")
						+ ": " + selectedDate);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public RegisterCustomer getRegisterCustomer() {
		RegisterCustomer register = new RegisterCustomer();
		register.setPrefix(edt_prefix.getText().toString());
		register.setSuffix(edt_suffix.getText().toString());
		register.setName(edt_name.getText().toString());
		register.setEmail(edt_email.getText().toString());
		register.setTaxVat(edt_taxvat.getText().toString());
		register.setPass(edt_pass.getText().toString());
		register.setConfirmPass(edt_confirmPass.getText().toString());
		register.setGender(sp_gender.getSelectedItem().toString());
		register.setDay(mDay);
		register.setMonth(mMonth);
		register.setYear(mYear);
		return register;
	}

	@Override
	public RelativeLayout getRelativeImage() {
		return rlt_image_extend;
	}

	@Override
	public Spinner getSpinnerSex() {
		return sp_gender;
	}
}
