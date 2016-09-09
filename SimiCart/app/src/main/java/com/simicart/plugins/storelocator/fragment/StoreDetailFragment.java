package com.simicart.plugins.storelocator.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.GPSTracker;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.storelocator.adapter.SpecialDayAdapter;
import com.simicart.plugins.storelocator.common.ShowMapError;
import com.simicart.plugins.storelocator.common.StoreLocatorConfig;
import com.simicart.plugins.storelocator.entity.StoreObject;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreDetailFragment extends SimiFragment {
	private View view;
	private TextView txt_name, txt_phone, txt_email, txt_web, txt_address, txt_des, txt_direc, txt_openning,
			txt_special, txt_holiday;
	private CircleImageView img_map;
	private TextView txt_mon, txt_tue, txt_wed, txt_thur, txt_fri, txt_sat, txt_sun, read_more;
	private TextView lb_monday, lb_sunday, lb_tuesday, lb_wednesday, lb_thursday, lb_friday, lb_saturday;
	private LinearLayout map, special_layout, holiday_layout;
	private RecyclerView list_special, list_holiday;
	private boolean readMore = true;
	private LinearLayout locator, phone, email, home, decrip;
	private StoreObject storeObject;

	public static StoreDetailFragment newInstance(SimiData data) {
		StoreDetailFragment detail = new StoreDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_DATA, data);
		detail.setArguments(bundle);
		return detail;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(Rconfig.getInstance().getId("plugins_store_locator_detail", "layout"), null);

		// getdata
		if (mData != null) {
			storeObject = (StoreObject) getValueWithKey(Constants.KeyData.STORE_OBJECT);
		}

		map = (LinearLayout) view.findViewById(Rconfig.getInstance().getIdLayout("map"));

		initStoreName();
		initAddress();
		initPhone();
		initEmail();
		initHomePage();
		initDescription();
		initSmallMap();
		initOpeningHour();

		onClick();
		
		return view;
	}

	protected void initStoreName() {
		txt_name = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_name_store"));
		String storeName = storeObject.getName();
		if (Utils.validateString(storeName)) {
			txt_name.setText(storeName);
		}
	}

	protected void initAddress() {
		locator = (LinearLayout) view.findViewById(Rconfig.getInstance().getIdLayout("location"));
		txt_address = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_address"));
		txt_address.setText(StoreLocatorConfig.convertAddress(storeObject));
	}

	protected void initPhone() {
		phone = (LinearLayout) view.findViewById(Rconfig.getInstance().getIdLayout("phone_layout"));
		txt_phone = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_phone"));
		String phone = storeObject.getPhone();
		if (Utils.validateString(phone)) {
			txt_phone.setText(phone);
		} else {
			txt_phone.setVisibility(View.GONE);
		}
	}

	protected void initEmail() {
		email = (LinearLayout) view.findViewById(Rconfig.getInstance().getIdLayout("email_layout"));
		txt_email = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_email"));
		String email = storeObject.getEmail();
		if (Utils.validateString(email)) {
			txt_email.setText(email);
		} else {
			txt_email.setVisibility(View.GONE);
		}
	}

	protected void initHomePage() {
		home = (LinearLayout) view.findViewById(Rconfig.getInstance().getIdLayout("home"));
		txt_web = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_web"));
		String link = storeObject.getLink();
		if (Utils.validateString(link)) {
			txt_web.setText(link);
		} else {
			txt_web.setVisibility(View.GONE);
		}
	}

	protected void initDescription() {
		decrip = (LinearLayout) view.findViewById(Rconfig.getInstance().getIdLayout("decrep_layout"));
		txt_des = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txtDescrip"));
		read_more = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("read_more"));

		final String fulldess = storeObject.getDescription();
		String shortdess = "";

		if (!Utils.validateString(fulldess)) {
			decrip.setVisibility(View.GONE);
		}

		if (fulldess.length() > 250) {
			readMore = true;
			read_more.setVisibility(View.VISIBLE);
			read_more.setText(">> " + SimiTranslator.getInstance().translate("Read more"));
			shortdess = storeObject.getDescription().substring(0, 250);
			txt_des.setText(Html.fromHtml(shortdess) + "...");
		} else {
			readMore = false;
			read_more.setVisibility(View.GONE);
			txt_des.setText(Html.fromHtml(fulldess));
		}
		read_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (readMore == true) {
					readMore = false;
					txt_des.setText(Html.fromHtml(fulldess));
					read_more.setText("<< " + SimiTranslator.getInstance().translate("Show less"));
				} else if (readMore == false) {
					readMore = true;
					txt_des.setText(Html.fromHtml(storeObject.getDescription().substring(0, 250)) + "...");
					read_more.setText(">> " + SimiTranslator.getInstance().translate("Read more"));
				}
			}
		});
	}

	protected void initSmallMap() {
		String url = "http://maps.google.com/maps/api/staticmap?center=" + storeObject.getLatitude() + ","
				+ storeObject.getLongtitude() + "&markers=icon:"
				+ getResources().getDrawable(Rconfig.getInstance().getIdDraw("plugins_locator_maker_default")) + "|"
				+ storeObject.getLatitude() + "," + storeObject.getLongtitude() + "&zoom=20&size=400x400&sensor=false";
		img_map = (CircleImageView) view.findViewById(Rconfig.getInstance().getIdLayout("image_map"));
//		Picasso.with(getActivity()).load(url).transform(new CircleTransform()).into(img_map);
		SimiDrawImage drawImage = new SimiDrawImage();
		drawImage.drawImage(img_map, url);
	}

	protected void initOpeningHour() {

		txt_mon = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_mon"));
		txt_tue = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_tus"));
		txt_wed = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_wen"));
		txt_thur = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_thur"));
		txt_fri = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_fri"));
		txt_sat = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_sat"));
		txt_sun = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_sun"));
		txt_direc = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("direction"));
		txt_special = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_special"));
		txt_openning = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_open_hour"));
		txt_holiday = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("txt_holiday"));
		lb_monday = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("lb_monday"));
		lb_saturday = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("lb_saturday"));
		lb_sunday = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("lb_sunday"));
		lb_tuesday = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("lb_tuesday"));
		lb_wednesday = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("lb_wednesday"));
		lb_thursday = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("lb_thursday"));
		lb_friday = (TextView) view.findViewById(Rconfig.getInstance().getIdLayout("lb_friday"));
		special_layout = (LinearLayout) view.findViewById(Rconfig.getInstance().getIdLayout("special_Layout"));
		holiday_layout = (LinearLayout) view.findViewById(Rconfig.getInstance().getIdLayout("holiday_layout"));
		list_special = (RecyclerView) view.findViewById(Rconfig.getInstance().getIdLayout("list_special"));
		list_special.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		list_special.setNestedScrollingEnabled(false);
		list_holiday = (RecyclerView) view.findViewById(Rconfig.getInstance().getIdLayout("list_holyday"));
		list_holiday.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		list_holiday.setNestedScrollingEnabled(false);

		lb_monday.setText(SimiTranslator.getInstance().translate("Monday") + ":");
		lb_saturday.setText(SimiTranslator.getInstance().translate("Saturday") + ":");
		lb_sunday.setText(SimiTranslator.getInstance().translate("Sunday") + ":");
		lb_tuesday.setText(SimiTranslator.getInstance().translate("Tuesday") + ":");
		lb_wednesday.setText(SimiTranslator.getInstance().translate("Wednesday") + ":");
		lb_thursday.setText(SimiTranslator.getInstance().translate("Thursday") + ":");
		lb_friday.setText(SimiTranslator.getInstance().translate("Friday") + ":");

		if (storeObject.getMonday_status().equals("1")) {
			if (!storeObject.getMonday_open().equals("") && !storeObject.getMonday_open().equals("00:00")
					&& !storeObject.getMonday_close().equals("") && !storeObject.getMonday_close().equals("00:00")) {
				txt_mon.setText(storeObject.getMonday_open() + " - " + storeObject.getMonday_close());
			} else {
				txt_mon.setText(SimiTranslator.getInstance().translate("Open"));
			}
		} else if (storeObject.getMonday_status().equals("2")) {
			txt_mon.setText(SimiTranslator.getInstance().translate("Close"));
		}

		if (storeObject.getTuesday_status().equals("1")) {
			if (!storeObject.getTuesday_open().equals("") && !storeObject.getTuesday_open().equals("00:00")
					&& !storeObject.getTuesday_close().equals("") && !storeObject.getTuesday_close().equals("00:00")) {
				txt_tue.setText(storeObject.getTuesday_open() + " - " + storeObject.getTuesday_close());
			} else {
				txt_tue.setText(SimiTranslator.getInstance().translate("Open"));
			}
		} else if (storeObject.getTuesday_status().equals("2")) {
			txt_tue.setText(SimiTranslator.getInstance().translate("Close"));
		}

		if (storeObject.getWednesday_status().equals("1")) {
			if (!storeObject.getWednesday_open().equals("") && !storeObject.getWednesday_open().equals("00:00")
					&& !storeObject.getWednesday_close().equals("")
					&& !storeObject.getWednesday_close().equals("00:00")) {
				txt_wed.setText(storeObject.getWednesday_open() + " - " + storeObject.getWednesday_close());
			} else {
				txt_wed.setText(SimiTranslator.getInstance().translate("Open"));
			}
		} else if (storeObject.getWednesday_status().equals("2")) {
			txt_wed.setText(SimiTranslator.getInstance().translate("Close"));
		}

		if (storeObject.getThursday_status().equals("1")) {
			if (!storeObject.getThursday_open().equals("") && !storeObject.getThursday_open().equals("00:00")
					&& !storeObject.getThursday_close().equals("")
					&& !storeObject.getThursday_close().equals("00:00")) {
				txt_thur.setText(storeObject.getThursday_open() + " - " + storeObject.getThursday_close());
			} else {
				txt_thur.setText(SimiTranslator.getInstance().translate("Open"));
			}
		} else if (storeObject.getThursday_status().equals("2")) {
			txt_thur.setText(SimiTranslator.getInstance().translate("Close"));
		}

		if (storeObject.getFriday_status().equals("1")) {
			if (!storeObject.getFriday_open().equals("") && !storeObject.getFriday_open().equals("00:00")
					&& !storeObject.getFriday_close().equals("") && !storeObject.getFriday_close().equals("00:00")) {
				txt_fri.setText(storeObject.getFriday_open() + " - " + storeObject.getFriday_close());
			} else {
				txt_fri.setText(SimiTranslator.getInstance().translate("Open"));
			}
		} else if (storeObject.getFriday_status().equals("2")) {
			txt_fri.setText(SimiTranslator.getInstance().translate("Close"));
		}

		if (storeObject.getSaturday_status().equals("1")) {
			if (!storeObject.getSaturday_open().equals("") && !storeObject.getSaturday_open().equals("00:00")
					&& !storeObject.getSaturday_close().equals("")
					&& !storeObject.getSaturday_close().equals("00:00")) {
				txt_sat.setText(storeObject.getSaturday_open() + " - " + storeObject.getSaturday_close());
			} else {
				txt_sat.setText(SimiTranslator.getInstance().translate("Open"));
			}
		} else if (storeObject.getSaturday_status().equals("2")) {
			txt_sat.setText(SimiTranslator.getInstance().translate("Close"));
		}

		if (storeObject.getSunday_status().equals("1")) {
			if (!storeObject.getSunday_open().equals("") && !storeObject.getSunday_open().equals("00:00")
					&& !storeObject.getSunday_close().equals("") && !storeObject.getSunday_close().equals("00:00")) {
				txt_sun.setText(storeObject.getSunday_open() + " - " + storeObject.getSunday_close());
			} else {
				txt_sun.setText(SimiTranslator.getInstance().translate("Close"));
			}
		} else if (storeObject.getSunday_status().equals("2")) {
			txt_sun.setText(SimiTranslator.getInstance().translate("Close"));
		}
		if (storeObject.getList_special() != null && storeObject.getList_special().size() > 0) {
			SpecialDayAdapter specialDayAdapter = new SpecialDayAdapter(storeObject.getList_special(), false);
			list_special.setAdapter(specialDayAdapter);
		} else {
			special_layout.setVisibility(View.GONE);
		}
		if (storeObject.getList_holiday() != null && storeObject.getList_holiday().size() > 0) {
			SpecialDayAdapter hoAdapter = new SpecialDayAdapter(storeObject.getList_holiday(), true);
			list_holiday.setAdapter(hoAdapter);
		} else {
			holiday_layout.setVisibility(View.GONE);
		}
	}

	private void onClick() {
		map.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					map.setBackgroundDrawable(getResources()
							.getDrawable(Rconfig.getInstance().getIdDraw("plugins_locator_show_map_hover")));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					map.setBackgroundDrawable(
							getResources().getDrawable(Rconfig.getInstance().getIdDraw("plugins_locator_show_map")));
					// Giang 28/10
					LocationManager service = (LocationManager) SimiManager.getIntance().getCurrentActivity()
							.getSystemService(Context.LOCATION_SERVICE);
					boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
					if (!enabled) {
						if (StoreLocatorConfig.choose != 1) {
							new ShowMapError(getActivity()).showGpsError();
							StoreLocatorConfig.choose = 1;
						}
					}

					Location currrentLocation = new Location("");
					GPSTracker gpsTracker = new GPSTracker(getActivity());
					Location current = gpsTracker.getLocation();
					if (current != null) {
						currrentLocation.setLongitude(current.getLongitude());
						currrentLocation.setLatitude(current.getLatitude());
					}

					if (currrentLocation == null) {
						Criteria criteria = new Criteria();
						String provider = service.getBestProvider(criteria, false);
						if (service != null && provider != null) {
							Location location = service.getLastKnownLocation(provider);
							if (location == null) {

								Toast.makeText(getActivity(), SimiTranslator.getInstance().translate("Location not available"),
										Toast.LENGTH_LONG).show();

								return true;
							} else {
								storeObject.setLatitude(location.getLatitude() + "");
								storeObject.setLongtitude(location.getLongitude() + "");
							}
						}
					} else {
						LatLng myLocator = new LatLng(Double.parseDouble(storeObject.getLatitude()),
								Double.parseDouble(storeObject.getLongtitude()));
						String uri_ = String.format(Locale.ENGLISH, "geo:<lat>,<long>?q=<%f>,<%f>(%s)",
								myLocator.latitude, myLocator.longitude, storeObject.getName());
						Intent intent_ = new Intent(Intent.ACTION_VIEW, Uri.parse(uri_));
						intent_.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
						startActivity(intent_);
					}
				}
				return true;
			}
		});
		phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + storeObject.getPhone()));
					startActivity(intent);
				} catch (Exception e) {

				}
				// String phone_number = "tel:" + storeObject.getPhone();
				// Intent callIntent = new Intent(Intent.ACTION_CALL);
				// callIntent.setData(Uri.parse(phone_number));
				// startActivity(callIntent);
			}
		});
		email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String mail = storeObject.getEmail();
					Intent gmail = new Intent(Intent.ACTION_SEND);
					// gmail.setClassName("com.google.android.gm",
					// "com.google.android.gm.ConversationListActivity");
					gmail.putExtra(Intent.EXTRA_EMAIL, new String[] { mail });
					gmail.setData(Uri.parse(mail));
					gmail.putExtra(Intent.EXTRA_SUBJECT, "");
					gmail.setType("plain/text");
					gmail.putExtra(Intent.EXTRA_TEXT, "");
					startActivity(gmail);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		// img_map.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// MapViewFragment fragment = MapViewFragment
		// .newInstance(storeObject);
		// SimiManager.getIntance().replacePopupFragment(fragment);
		// }
		// });
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(storeObject.getLink()));
					startActivity(intent);
				} catch (Exception e) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("http:///" + storeObject.getLink()));
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
}
