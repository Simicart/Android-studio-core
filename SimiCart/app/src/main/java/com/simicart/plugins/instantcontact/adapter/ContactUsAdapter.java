package com.simicart.plugins.instantcontact.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.instantcontact.entity.ContactUsEntity;

import java.util.ArrayList;

/**
 * Created by Martial on 8/27/2016.
 */
public class ContactUsAdapter extends RecyclerView.Adapter<ContactUsAdapter.ContactHolder> {

    protected ArrayList<ContactUsEntity> listContacts;
    protected Context mContext;
    protected String style = "";

    public ContactUsAdapter(ArrayList<ContactUsEntity> listContacts, String style) {
        this.listContacts = listContacts;
        this.style = style;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = null;
        ContactHolder holder = null;
        if(style.equals("2")) {
            itemView = inflater.inflate(Rconfig.getInstance().layout("plugins_contactus_adapter_item_grid"), null, false);
        } else {
            itemView = inflater.inflate(Rconfig.getInstance().layout("plugins_contactus_adapter_item_list"), null, false);
        }
        holder = new ContactHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        ContactUsEntity contactUsEntity = listContacts.get(position);
        createItemContact(holder, contactUsEntity);
    }

    @Override
    public int getItemCount() {
        return listContacts.size();
    }

    protected void createItemContact(ContactHolder holder, final ContactUsEntity contactUsEntity) {

        final String name = contactUsEntity.getNameContactUs();
        if(Utils.validateString(name)) {
            holder.tvName.setText(SimiTranslator.getInstance().translate(name));
        }

        Drawable icon = AppColorConfig.getInstance().getIcon(contactUsEntity.getImageContactUs(), contactUsEntity.getActiveColor());
        holder.ivContact.setImageDrawable(icon);

        holder.rlItemContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (name) {
                    case "Email":
                        onClickEmail(contactUsEntity);
                        break;
                    case "Message":
                        onClickMessage(contactUsEntity);
                        break;
                    case "Call":
                        onClickCall(contactUsEntity);
                        break;
                    case "Website":
                        onClickWebsite(contactUsEntity);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    protected void onClickEmail(ContactUsEntity contact) {
		int size = contact.getEmail().size();
		String[] TO = new String[size];
		for (int i = 0; i < size; i++) {
			TO[i] = contact.getEmail().get(i);
		}
		Intent intentEmail = new Intent(Intent.ACTION_SEND);
		intentEmail.setData(Uri.parse("mailto:"));
		intentEmail.setType("message/rfc822");
		intentEmail.putExtra(Intent.EXTRA_EMAIL, TO);
		intentEmail.putExtra(Intent.EXTRA_SUBJECT, SimiTranslator.getInstance().translate("Your subject"));
		intentEmail.putExtra(Intent.EXTRA_TEXT,
                SimiTranslator.getInstance().translate("Enter your FeedBack"));
		try {
			mContext.startActivity(Intent.createChooser(intentEmail, SimiTranslator.getInstance().translate("Send FeedBack" + "...")));
		} catch (ActivityNotFoundException e) {
            SimiNotify.getInstance().showToast(SimiTranslator.getInstance().translate("There is no email client installed" + "."));
			String urlEmail = "https://mail.google.com";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(urlEmail));
			mContext.startActivity(i);
		}
    }

    protected void onClickMessage(ContactUsEntity contact) {
        if (!checkTelephonyFeature()) {
			return;
		}

		if (contact.getMessage().size() > 1) {
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(
					mContext);
			builderSingle.setTitle(SimiTranslator.getInstance().translate(
					"Select a phone number"));
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					mContext, android.R.layout.select_dialog_singlechoice);
			for (int i = 0; i < contact.getMessage().size(); i++) {
				arrayAdapter.add(contact.getMessage().get(i));
			}

			builderSingle.setAdapter(arrayAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String phone_number = arrayAdapter.getItem(which);
							sendSMS(phone_number);
						}
					});
			builderSingle.show();

		} else {
			String phone_number = contact.getMessage().get(0);
			sendSMS(phone_number);
		}
    }

    protected void sendSMS(String phone_number) {
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setData(Uri.parse("sms:" + phone_number));
		mContext.startActivity(smsIntent);
	}

    protected void onClickCall(ContactUsEntity contact) {
        if (!checkTelephonyFeature()) {
			return;
		}
		if (contact.getPhone().size() > 1) {
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(
					mContext);
			builderSingle.setTitle(SimiTranslator.getInstance().translate(
					"Select a phone number"));
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					mContext, android.R.layout.select_dialog_singlechoice);
			for (int i = 0; i < contact.getPhone().size(); i++) {
				arrayAdapter.add(contact.getPhone().get(i));
			}

			builderSingle.setAdapter(arrayAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String phone_number = arrayAdapter.getItem(which);
							call(phone_number);
						}
					});
			builderSingle.show();

		} else {
			String phone_number = contact.getPhone().get(0);
			call(phone_number);
		}
    }

    protected boolean checkTelephonyFeature() {
		if (!mContext.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_TELEPHONY)) {
            SimiNotify.getInstance().showToast("Your device does not support message and phone calling feature.");
			return false;
		}
		return true;
	}

    protected void call(String phone_number) {
		try {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ phone_number));
			mContext.startActivity(intent);
		} catch (Exception e) {

		}

	}

    protected void onClickWebsite(ContactUsEntity contact) {
        String url = contact.getWebsite();
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://" + url;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		mContext.startActivity(i);
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivContact;
        private RelativeLayout rlItemContact;

        public ContactHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(Rconfig
                    .getInstance().id("tv_contact_us"));
            tvName.setTextColor(AppColorConfig.getInstance().getContentColor());
            ivContact = (ImageView) v.findViewById(Rconfig
                    .getInstance().id("iv_contact_us"));
            rlItemContact = (RelativeLayout) v.findViewById(Rconfig.getInstance().id("rl_item_contact"));
        }
    }

}
