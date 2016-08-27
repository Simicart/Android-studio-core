package com.simicart.plugins.instantcontact.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;
import com.simicart.plugins.instantcontact.ContactUs;
import com.simicart.plugins.instantcontact.common.ContactUsConstant;

public class ContactUsEntity extends SimiEntity {

	private ArrayList<String> emails;
	private ArrayList<String> phones;
	private ArrayList<String> messages;
	private String website;
	private String style;
	private String activecolor;
	private String imageContactUs;
	private String nameContactUs;

	private String email_key = "email";
	private String phone_key = "phone";
	private String message_key = "message";
	private String website_key = "website";
	private String style_key = "style";
	private String active_color_key = "activecolor";

	public ContactUsEntity() {

	}

	public ContactUsEntity(ContactUsEntity baseContact, String icon, String name) {
		this.emails = baseContact.getEmail();
		this.phones = baseContact.getPhone();
		this.messages = baseContact.getMessage();
		this.website = baseContact.getWebsite();
		this.activecolor = baseContact.getActiveColor();
		this.style = baseContact.getStyle();
		this.imageContactUs = icon;
		this.nameContactUs = name;
	}

	@Override
	public void parse() {

		if(hasKey(email_key)) {
			try {
				JSONArray emailArr = mJSON.getJSONArray(email_key);
				if(emailArr != null) {
					emails = new ArrayList<>();
					for(int i=0;i<emailArr.length();i++) {
						String email = emailArr.getString(i);
						if(Utils.validateString(email)) {
							emails.add(email);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if(hasKey(phone_key)) {
			try {
				JSONArray phoneArr = mJSON.getJSONArray(phone_key);
				if(phoneArr != null) {
					phones = new ArrayList<>();
					for(int i=0;i<phoneArr.length();i++) {
						String phone = phoneArr.getString(i);
						if(Utils.validateString(phone)) {
							phones.add(phone);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if(hasKey(message_key)) {
			try {
				JSONArray messageArr = mJSON.getJSONArray(message_key);
				if(messageArr != null) {
					messages = new ArrayList<>();
					for(int i=0;i<messageArr.length();i++) {
						String message = messageArr.getString(i);
						if(Utils.validateString(message)) {
							messages.add(message);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		website = getData(website_key);

		style = getData(style_key);

		activecolor = getData(active_color_key);

	}

	public void setActiveColor(String color) {
		activecolor = color;
	}

	public String getActiveColor() {
		if (!activecolor.contains("#")) {
			activecolor = "#" + activecolor;
		}
		return activecolor;
	}

	public ArrayList<String> getMessage() {
		return messages;
	}

	public void setMessage(ArrayList<String> message) {
		this.messages = message;
	}

	public ArrayList<String> getEmail() {
		return emails;
	}

	public void setEmail(ArrayList<String> email) {
		this.emails = email;
	}

	public ArrayList<String> getPhone() {
		return phones;
	}

	public void setPhone(ArrayList<String> phone) {
		this.phones = phone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getImageContactUs() {
		return imageContactUs;
	}

	public void setImageContactUs(String imageContactUs) {
		this.imageContactUs = imageContactUs;
	}

	public String getNameContactUs() {
		return nameContactUs;
	}

	public void setNameContactUs(String nameContactUs) {
		this.nameContactUs = nameContactUs;
	}
	
}
