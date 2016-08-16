package com.simicart.core.customer.block;

import java.util.ArrayList;
import java.util.Calendar;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.adapter.GenderAdapter;
import com.simicart.core.customer.controller.ProfileController;
import com.simicart.core.customer.delegate.ProfileDelegate;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.GenderConfig;
import com.simicart.core.customer.entity.ProfileEntity;
import com.simicart.core.material.ButtonRectangle;
import com.simicart.core.material.LayoutRipple;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class ProfileBlock extends SimiBlock implements ProfileDelegate {
	protected TextView tv_profile;
	private TextView tv_gender;
	protected EditText edt_prefix;
	protected EditText edt_fullname;
	protected EditText edt_suffix;
	protected EditText edt_email;
	protected TextView tv_dateBirth;
	protected RelativeLayout rl_gender;
	protected Spinner sp_gender;
	protected EditText edt_taxVAT;
	protected EditText edt_currentPass;
	protected EditText edt_newPass;
	protected EditText edt_confirmPass;
	protected ButtonRectangle btn_save;
	protected ConfigCustomerAddress mProfile;
	protected ImageView im_show_current_pass;
	protected ImageView im_show_new_pass;
	protected ImageView im_show_confirm_pass;
	private ImageView img_show_gender;
	private RelativeLayout rlt_show_gender;
	private String mDay = "";
	private String mMonth = "";
	private String mYear = "";

	private LayoutRipple layout_date_ofbirt;

	public ProfileBlock(View view, Context context) {
		super(view, context);

		mProfile = DataLocal.ConfigCustomerProfile;
	}

	public void setSaveClicker(OnClickListener clicker) {
		btn_save.setOnClickListener(clicker);
	}

	public void setShowCurrentPass(OnTouchListener onTouch) {
		im_show_current_pass.setOnTouchListener(onTouch);
	}

	public void setClickImageGender(OnClickListener listener) {
		rlt_show_gender.setOnClickListener(listener);
	}

	public void setShowNewPass(OnTouchListener onTouch) {
		im_show_new_pass.setOnTouchListener(onTouch);
	}

	public void setShowConfirmPass(OnTouchListener onTouch) {
		im_show_confirm_pass.setOnTouchListener(onTouch);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initView() {
		createPrefix();
		createSuffix();
		createDateBirth();
		createTaxVat();
		createGender();

		btn_save = (ButtonRectangle) mView.findViewById(Rconfig.getInstance()
				.id("bt_save"));
		btn_save.setTextSize(Constants.SIZE_TEXT_BUTTON);
		btn_save.setText(SimiTranslator.newInstance().translate("Save"));
		btn_save.setTextColor(Color.parseColor("#ffffff"));
		btn_save.setBackgroundColor(AppColorConfig.getInstance().getKeyColor());

		// full name
		edt_fullname = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_fullname"));
		edt_fullname.setHint(SimiTranslator.newInstance().translate("Name") + "(*)");

		// email
		edt_email = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_email"));
		edt_email.setHint(SimiTranslator.newInstance().translate("Email") + "(*)");


		// current password
		edt_currentPass = (EditText) mView.findViewById(Rconfig.getInstance()
				.id("et_current_pass"));
		edt_currentPass.setHint(SimiTranslator.newInstance().translate("Current Password"));

		// new password
		edt_newPass = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_new_pass"));
		edt_newPass.setHint(SimiTranslator.newInstance().translate("New Password"));

		// confirm password
		edt_confirmPass = (EditText) mView.findViewById(Rconfig.getInstance()
				.id("et_confirm_pass"));
		edt_confirmPass.setHint(SimiTranslator.newInstance().translate("Confirm Password"));


		im_show_current_pass = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("im_show_current_pass"));
		im_show_new_pass = (ImageView) mView.findViewById(Rconfig.getInstance()
				.id("im_show_new_pass"));
		im_show_confirm_pass = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("im_show_confirm_pass"));
		img_show_gender = (ImageView) mView.findViewById(Rconfig.getInstance()
				.id("im_extend"));
		rlt_show_gender = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("rlt_im_extend"));
		setColorComponent();
	}

	private void setColorComponent() {
		setColorTextview(tv_profile);
		setColorTextview(tv_dateBirth);
		setColorTextview(tv_gender);

		setColorEdittext(edt_prefix);
		setHintColorEdittext(edt_prefix);
		setColorEdittext(edt_fullname);
		setHintColorEdittext(edt_fullname);

		setColorEdittext(edt_suffix);
		setHintColorEdittext(edt_suffix);

		setColorEdittext(edt_email);
		setHintColorEdittext(edt_email);

		setColorEdittext(edt_taxVAT);
		setHintColorEdittext(edt_taxVAT);

		setColorEdittext(edt_currentPass);
		setHintColorEdittext(edt_currentPass);

		setColorEdittext(edt_newPass);
		setHintColorEdittext(edt_newPass);

		setColorEdittext(edt_confirmPass);
		setHintColorEdittext(edt_confirmPass);

		changeColorImageView(im_show_current_pass, "ic_show_password");
		changeColorImageView(im_show_new_pass, "ic_show_password");
		changeColorImageView(im_show_confirm_pass, "ic_show_password");
		changeColorImageView(img_show_gender, "ic_extend");

	}

	private void setColorTextview(TextView view) {
		if (view != null) {
			view.setTextColor(AppColorConfig.getInstance().getContentColor());
		}
	}

	private void setColorEdittext(EditText editText) {
		if (editText != null) {
			editText.setTextColor(AppColorConfig.getInstance().getContentColor());
		}
	}

	private void setHintColorEdittext(EditText editText) {
		if (editText != null) {
			editText.setHintTextColor(AppColorConfig.getInstance().getContentColor());
		}
	}

	private void changeColorImageView(ImageView img, String src) {
		if (img != null) {
			Drawable icon = mContext.getResources().getDrawable(
					Rconfig.getInstance().drawable(src));
			icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
					PorterDuff.Mode.SRC_ATOP);
			img.setImageDrawable(icon);
		}
	}


	protected void createPrefix() {
		edt_prefix = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_prefix_show"));
		edt_prefix.setHint(SimiTranslator.newInstance().translate("Prefix") + " (*)");
		switch (mProfile.getPrefix().toLowerCase()) {
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

	protected void createSuffix() {

		edt_suffix = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_suffix_show"));
		switch (mProfile.getSuffix().toLowerCase()) {
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

	protected void createTaxVat() {
		edt_taxVAT = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_taxvat_show"));
		// if (DataLocal.isLanguageRTL) {
		// setPositionRTL(edt_taxVAT);
		// }
		switch (mProfile.getTaxvat().toLowerCase()) {
		case "":
			edt_taxVAT.setVisibility(View.GONE);
			break;
		case "req":
			edt_taxVAT.setHint(SimiTranslator.newInstance().translate("Tax/VAT number")
					+ "(*)");
			break;
		case "opt":
			edt_taxVAT.setHint(SimiTranslator.newInstance().translate("Tax/VAT number"));
			break;
		default:
			break;
		}
	}

	protected void createDateBirth() {
		layout_date_ofbirt = (LayoutRipple) mView.findViewById(Rconfig
				.getInstance().id("layout_date_of_birt"));
		tv_dateBirth = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_date_birth"));

		Calendar cDate = Calendar.getInstance();
		final int cDay = cDate.get(Calendar.DAY_OF_MONTH);
		final int cMonth = cDate.get(Calendar.MONTH) + 1;
		final int cYear = cDate.get(Calendar.YEAR);

		switch (mProfile.getDob().toLowerCase()) {
		case "":
			layout_date_ofbirt.setVisibility(View.GONE);
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

		layout_date_ofbirt.setOnClickListener(new OnClickListener() {

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
			switch (mProfile.getDob().toLowerCase()) {
			case "":
				layout_date_ofbirt.setVisibility(View.GONE);
				break;
			case "req":
				if (DataLocal.isLanguageRTL) {
					tv_dateBirth.setText(selectedDate + " :(*)"
							+ SimiTranslator.newInstance().translate("Date of Birth"));
				} else {
					tv_dateBirth.setText(SimiTranslator.newInstance().translate("Date of Birth")
							+ "(*): " + selectedDate);
				}
				break;
			case "opt":
				if (DataLocal.isLanguageRTL) {
					tv_dateBirth.setText(selectedDate + " :"
							+ SimiTranslator.newInstance().translate("Date of Birth"));
				} else {
					tv_dateBirth.setText(SimiTranslator.newInstance().translate("Date of Birth")
							+ ": " + selectedDate);
				}
				break;
			default:
				break;
			}
		}
	};

	protected void createGender() {
		rl_gender = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
				.id("rl_gender"));
		sp_gender = (Spinner) rl_gender.findViewById(Rconfig.getInstance().id(
				"sp_gender"));
		tv_gender = (TextView) rl_gender.findViewById(Rconfig.getInstance().id(
				"tv_gender"));

		GenderAdapter adapter = new GenderAdapter(mContext);
		sp_gender.setAdapter(adapter);

		switch (mProfile.getGender().toLowerCase()) {
		case "":
			rl_gender.setVisibility(View.GONE);
			break;
		case "req":
			if (DataLocal.isLanguageRTL) {
				tv_gender.setText(":(*)"
						+ SimiTranslator.newInstance().translate("Gender"));
			} else {
				tv_gender.setText(SimiTranslator.newInstance().translate("Gender")
						+ "(*):");
			}
			break;
		case "opt":
			if (DataLocal.isLanguageRTL) {
				tv_gender.setText(":" + SimiTranslator.newInstance().translate("Gender"));
			} else {
				tv_gender.setText(SimiTranslator.newInstance().translate("Gender") + ":");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> entity = collection.getCollection();
		if (null != entity && entity.size() > 0) {
			ProfileEntity profile = (ProfileEntity) entity.get(0);
			if (View.VISIBLE == edt_prefix.getVisibility()) {
				edt_prefix.setText(profile.getPrefix());
			}

			if (View.VISIBLE == edt_suffix.getVisibility()) {
				edt_suffix.setText(profile.getSuffix());
			}

			if (View.VISIBLE == edt_fullname.getVisibility()) {
				edt_fullname.setText(profile.getName());
			}

			if (View.VISIBLE == edt_email.getVisibility()) {
				edt_email.setText(profile.getEmail());
			}

			if (View.VISIBLE == edt_taxVAT.getVisibility()) {
				edt_taxVAT.setText(profile.getTaxVat());
			}

			if (View.VISIBLE == edt_taxVAT.getVisibility()) {
				edt_taxVAT.setText(profile.getTaxVat());
			}

			String day = profile.getDay();
			if (null != day && !day.equals("") && !day.equals("null")) {
				String selectedDate = profile.getDay() + "/"
						+ profile.getMonth() + "/" + profile.getYear();
				switch (mProfile.getDob().toLowerCase()) {
				case "":
					tv_dateBirth.setVisibility(View.GONE);
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

			if (profile.getGender().equals("")) {
				sp_gender.setSelection(0);
			} else {
				int i = 0;
				for (GenderConfig genderConfig : DataLocal.ConfigCustomerAddress
						.getGenderConfigs()) {
					i++;
					if (genderConfig.getLabel().equals(profile.getGender())) {
						sp_gender.setSelection(i);
					}
				}
			}
		}
	}

	@Override
	public ProfileEntity getProfileEntity() {
		ProfileEntity profile = new ProfileEntity();
		profile.setEmail(edt_email.getText().toString());
		profile.setName(edt_fullname.getText().toString());
		profile.setPrefix(edt_prefix.getText().toString());
		profile.setSuffix(edt_suffix.getText().toString());

		profile.setGender(sp_gender.getSelectedItem().toString());

		profile.setTaxVat(edt_taxVAT.getText().toString());

		profile.setDay(mDay);
		profile.setMonth(mMonth);
		profile.setYear(mYear);

		profile.setCurrentPass(edt_currentPass.getText().toString());
		profile.setNewPass(edt_newPass.getText().toString());
		profile.setConfirmPass(edt_confirmPass.getText().toString());
		// and so on
		return profile;
	}

	@Override
	public void onTouchDown(int type) {
		if (type == ProfileController.TOUCH_CURRENT_PASS) {
			edt_currentPass
					.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		} else if (type == ProfileController.TOUCH_NEW_PASS) {
			edt_newPass
					.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		} else if (type == ProfileController.TOUCH_CONFIRM_PASS) {
			edt_confirmPass
					.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}
	}

	@Override
	public void onTouchCancel(int type) {
		if (type == ProfileController.TOUCH_CURRENT_PASS) {
			edt_currentPass.setInputType(129);
			int position = edt_currentPass.length();
			Selection.setSelection(edt_currentPass.getText(), position);
		} else if (type == ProfileController.TOUCH_NEW_PASS) {
			edt_newPass.setInputType(129);
			int position = edt_newPass.length();
			Selection.setSelection(edt_newPass.getText(), position);
		} else if (type == ProfileController.TOUCH_CONFIRM_PASS) {
			edt_confirmPass.setInputType(129);
			int position = edt_confirmPass.length();
			Selection.setSelection(edt_confirmPass.getText(), position);
		}
	}

	@Override
	public Spinner getGenderSpinner() {
		return sp_gender;
	}

	@Override
	public ImageView getImageViewGender() {
		return img_show_gender;
	}

	@Override
	public RelativeLayout getRelativeGender() {
		return rlt_show_gender;
	}
}
