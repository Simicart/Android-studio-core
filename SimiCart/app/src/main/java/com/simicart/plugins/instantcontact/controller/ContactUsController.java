package com.simicart.plugins.instantcontact.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.plugins.instantcontact.model.ContactUsModel;

public class ContactUsController extends SimiController {

    protected SimiDelegate mDelegate;

    public void setDelegate(SimiDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void onStart() {
        mDelegate.showLoading();
        mModel = new ContactUsModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissLoading();
                mDelegate.updateView(mModel.getCollection());
            }
        });
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissLoading();
                SimiNotify.getInstance().showNotify(error.getMessage());
            }
        });
        mModel.request();

    }

    @Override
    public void onResume() {
        mDelegate.updateView(mModel.getCollection());
    }

//	protected void makeACall() {
//		if (!checkTelephonyFeature()) {
//			return;
//		}
//		ContactUsEntity contact = getContactFromCollection();
//		if (contact.getPhone().size() > 1) {
//			AlertDialog.Builder builderSingle = new AlertDialog.Builder(
//					mContext);
//			builderSingle.setTitle(Config.newInstance().getText(
//					"Select a phone number"));
//			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//					mContext, android.R.layout.select_dialog_singlechoice);
//			for (int i = 0; i < ((ContactUsEntity) mModel.getCollection()
//					.getCollection().get(0)).getPhone().size(); i++) {
//				arrayAdapter.add(((ContactUsEntity) mModel.getCollection()
//						.getCollection().get(0)).getPhone().get(i));
//			}
//
//			builderSingle.setAdapter(arrayAdapter,
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							String phone_number = arrayAdapter.getItem(which);
//							call(phone_number);
//						}
//					});
//			builderSingle.show();
//
//		} else {
//			String phone_number = ((ContactUsEntity) mModel.getCollection()
//					.getCollection().get(0)).getPhone().get(0);
//			call(phone_number);
//		}
//
//	}
//
//	protected void call(String phone_number) {
//		try {
//			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
//					+ phone_number));
//			mContext.startActivity(intent);
//		} catch (Exception e) {
//
//		}
//		// Intent callIntent = new Intent(Intent.ACTION_CALL);
//		// callIntent.setData(Uri.parse("tel:" + phone_number));
//		// mContext.startActivity(callIntent);
//
//	}
//
//	protected void sendEmail() {
//		ContactUsEntity contact = getContactFromCollection();
//		int size = contact.getEmail().size();
//		String[] TO = new String[size];
//		for (int i = 0; i < size; i++) {
//			TO[i] = contact.getEmail().get(i);
//		}
//		Intent intentEmail = new Intent(Intent.ACTION_SEND);
//		intentEmail.setData(Uri.parse("mailto:"));
//		intentEmail.setType("message/rfc822");
//		intentEmail.putExtra(Intent.EXTRA_EMAIL, TO);
//		intentEmail.putExtra(Intent.EXTRA_SUBJECT, Config.newInstance()
//				.getText("Your subject"));
//		intentEmail.putExtra(Intent.EXTRA_TEXT,
//				Config.newInstance().getText("Enter your FeedBack"));
//		try {
//			mContext.startActivity(Intent.createChooser(intentEmail, Config
//					.newInstance().getText("Send FeedBack...")));
//		} catch (ActivityNotFoundException e) {
//			Toast.makeText(
//					mContext,
//					Config.newInstance().getText(
//							"There is no email client installed."),
//					Toast.LENGTH_SHORT).show();
//			String urlEmail = "https://mail.google.com";
//			Intent i = new Intent(Intent.ACTION_VIEW);
//			i.setData(Uri.parse(urlEmail));
//			mContext.startActivity(i);
//		}
//	}
//
//	protected void visitWebsite() {
//		ContactUsEntity contact = getContactFromCollection();
//		String url = contact.getWebsite();
//		if (!url.startsWith("http://") && !url.startsWith("https://")) {
//			url = "http://" + url;
//		}
//		Intent i = new Intent(Intent.ACTION_VIEW);
//		i.setData(Uri.parse(url));
//		mContext.startActivity(i);
//	}
//
//	protected void sendMessage() {
//		if (!checkTelephonyFeature()) {
//			return;
//		}
//
//		ContactUsEntity contact = getContactFromCollection();
//
//		if (contact.getMessage().size() > 1) {
//			AlertDialog.Builder builderSingle = new AlertDialog.Builder(
//					mContext);
//			builderSingle.setTitle(Config.newInstance().getText(
//					"Select a phone number"));
//			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//					mContext, android.R.layout.select_dialog_singlechoice);
//			for (int i = 0; i < contact.getMessage().size(); i++) {
//				arrayAdapter.add(contact.getMessage().get(i));
//			}
//
//			builderSingle.setAdapter(arrayAdapter,
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							String phone_number = arrayAdapter.getItem(which);
//							sendSMS(phone_number);
//						}
//					});
//			builderSingle.show();
//
//		} else {
//			String phone_number = contact.getMessage().get(0);
//			sendSMS(phone_number);
//		}
//	}
//
//	protected void sendSMS(String phone_number) {
//		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//		smsIntent.setData(Uri.parse("sms:" + phone_number));
//		mContext.startActivity(smsIntent);
//	}
//
//	protected boolean checkTelephonyFeature() {
//		if (!mContext.getPackageManager().hasSystemFeature(
//				PackageManager.FEATURE_TELEPHONY)) {
//			Toast.makeText(
//					mContext,
//					Config.newInstance()
//							.getText(
//									"Your device does not support message and phone calling feature."),
//					Toast.LENGTH_LONG).show();
//			return false;
//		}
//		return true;
//	}
//
//	protected ContactUsEntity getContactFromCollection() {
//
//		return (ContactUsEntity) mModel.getCollection().getCollection().get(0);
//	}

}
