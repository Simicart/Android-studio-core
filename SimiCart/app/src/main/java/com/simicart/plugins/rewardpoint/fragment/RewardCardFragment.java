package com.simicart.plugins.rewardpoint.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.utils.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RewardCardFragment extends SimiFragment {

    protected ProgressDialog pd_loading;
    private String passBookLogo = "";
    private String loyalty_redeem = "";
    private String passbook_text = "";
    private String background_passbook = "";
    private String passbook_foreground = "";
    private String passbook_barcode = "";
    private ImageView img_logo;
    private TextView txt_balance;
    private TextView txt_balance_content;
    private TextView txt_passbook;
    private ImageView img_add_passbook;
    private ImageView img_barcode;
    private TextView txt_barcode;
    private Context mContext;
    private String file_pkpass = "/reward.pkpass";
    private LinearLayout layout_passbook;

//	public RewardCardFragment(String passBookLogo, String loyalty_redeem,
//			String nameapp, String background, String passbook_foreground,
//			String passbook_barcode) {
//		this.passBookLogo = passBookLogo;
//		this.loyalty_redeem = loyalty_redeem;
//		this.passbook_text = nameapp;
//		this.background_passbook = background;
//		this.passbook_foreground = passbook_foreground.trim();
//		this.passbook_barcode = passbook_barcode.trim();
//	}

    public static RewardCardFragment newInstance(SimiData data) {
        RewardCardFragment fragment = new RewardCardFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_DATA, data);
        fragment.setArguments(bundle);
        return fragment;

    }

    @SuppressWarnings("unused")
    private static boolean launchPassWallet(Context applicationContext,
                                            Uri uri, boolean launchGooglePlay) {
        if (null != applicationContext) {
            PackageManager packageManager = applicationContext
                    .getPackageManager();
            if (null != packageManager) {
                final String strPackageName = "com.attidomobile.passwallet";
                Intent startIntent = new Intent();
                startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startIntent.setAction(Intent.ACTION_VIEW);
                Intent passWalletLaunchIntent = packageManager
                        .getLaunchIntentForPackage(strPackageName);
                if (null == passWalletLaunchIntent) {
                    // PassWallet isn't installed, open Google Play:
                    if (launchGooglePlay) {
                        String strReferrer = "";
                        try {
                            final String strEncodedURL = URLEncoder.encode(
                                    uri.toString(), "UTF-8");
                            strReferrer = "&referrer=" + strEncodedURL;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            strReferrer = "";
                        }
                        try {
                            startIntent.setData(Uri
                                    .parse("market://details?id="
                                            + strPackageName + strReferrer));
                            applicationContext.startActivity(startIntent);
                        } catch (android.content.ActivityNotFoundException anfe) {
                            // Google Play not installed, open via website
                            startIntent
                                    .setData(Uri
                                            .parse("http://play.google.com/store/apps/details?id="
                                                    + strPackageName
                                                    + strReferrer));
                            applicationContext.startActivity(startIntent);
                        }
                    }
                } else {
                    final String strClassName = "com.attidomobile.passwallet.activity.TicketDetailActivity";
                    startIntent.setClassName(strPackageName, strClassName);
                    startIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                    startIntent.setDataAndType(uri,
                            "application/vnd.apple.pkpass");
                    applicationContext.startActivity(startIntent);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                Rconfig.getInstance()
                        .layout("plugins_rewardpoint_cardfragment"), container,
                false);
        mContext = getActivity();

//		getdata
        if (getArguments() != null) {
            passBookLogo = (String) getValueWithKey(KeyData.REWARD_POINT.PASSBOOK_LOGO);
            loyalty_redeem = (String) getValueWithKey(KeyData.REWARD_POINT.LOYALTY_REDEEM);
            passbook_text = (String) getValueWithKey(KeyData.REWARD_POINT.PASSBOOK_TEXT);
            background_passbook = (String) getValueWithKey(KeyData.REWARD_POINT.PASSBOOK_BACKGROUND);
            passbook_foreground = (String) getValueWithKey(KeyData.REWARD_POINT.PASSBOOK_FOREGROUND);
            passbook_barcode = (String) getValueWithKey(KeyData.REWARD_POINT.PASSBOOK_BARCODE);
        }

        img_logo = (ImageView) view.findViewById(Rconfig.getInstance().id(
                "img_logo_passbook"));
        img_barcode = (ImageView) view.findViewById(Rconfig.getInstance().id(
                "img_code"));
        txt_barcode = (TextView) view.findViewById(Rconfig.getInstance().id(
                "txt_barcode"));
        txt_barcode.setText(passbook_barcode);
        txt_balance = (TextView) view.findViewById(Rconfig.getInstance().id(
                "txt_balance"));
        if (!passbook_foreground.equals("")) {
            txt_balance.setTextColor(Color
                    .parseColor("#" + passbook_foreground));
        }
        txt_balance.setText(SimiTranslator.getInstance().translate("Balance"));
        txt_balance_content = (TextView) view.findViewById(Rconfig
                .getInstance().id("txt_balance_content"));
        txt_balance_content.setText(loyalty_redeem);
        if (!passbook_foreground.equals("")) {
            txt_balance_content.setTextColor(Color.parseColor("#"
                    + passbook_foreground));
        }

        txt_passbook = (TextView) view.findViewById(Rconfig.getInstance().id(
                "txt_passbook"));
        txt_passbook.setText(passbook_text);
        if (!passbook_foreground.equals("")) {
            txt_passbook.setTextColor(Color.parseColor("#"
                    + passbook_foreground));
        }

        layout_passbook = (LinearLayout) view.findViewById(Rconfig
                .getInstance().id("layout_passbook"));
        if (!background_passbook.equals("")) {
            layout_passbook.setBackgroundColor(Color.parseColor("#"
                    + background_passbook.trim()));
        }
        GradientDrawable bg_passbook = new GradientDrawable();
        if (!background_passbook.equals("")) {
            bg_passbook.setColor(Color.parseColor("#"
                    + background_passbook.trim()));
        } else {
            bg_passbook.setColor(Color.parseColor("#FF6600"));
        }
        bg_passbook.setCornerRadius(Utils.toDp(15));
        bg_passbook.setStroke(1, Color.parseColor("#FF6600"));

        layout_passbook.setBackgroundDrawable(bg_passbook);

        if (!passBookLogo.equals("")) {
            DrawableManager.fetchDrawableOnThread(passBookLogo, img_logo);
        }
        img_add_passbook = (ImageView) view.findViewById(Rconfig.getInstance()
                .id("img_addto_passbook"));
        img_add_passbook.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = Config.getInstance().getBaseUrl()
                        + Constant.GET_CART;
                new getFilePkpassFromServer().execute(url);
            }
        });
        getImageBarcode();
        return view;
    }

    private void getImageBarcode() {
        String url_request = Config.getInstance().getBaseUrl()
                + Constant.GET_PASSBOOK_BARCODE + passbook_barcode;
        try {
            SimiDrawImage drawImage = new SimiDrawImage();
            drawImage.drawImage(img_barcode, url_request);
        } catch (Exception e) {
            Log.d("Error Get Barcode:", e.getMessage());
        }

    }

    // public static DefaultHttpClient getNewHttpClient() {
    // DefaultHttpClient httpClient = null;
    // try {
    // if (httpClient == null) {
    // HttpParams params = new BasicHttpParams();
    // HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    // HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
    // SchemeRegistry registry = new SchemeRegistry();
    // registry.register(new Scheme("http", PlainSocketFactory
    // .getSocketFactory(), 80));
    //
    // KeyStore trustStore = KeyStore.newInstance(KeyStore
    // .getDefaultType());
    // trustStore.load(null, null);
    // SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
    // sf.setHostnameVerifier((X509HostnameVerifier)
    // SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    // registry.register(new Scheme("https", sf, 443));
    //
    // ClientConnectionManager ccm = new ThreadSafeClientConnManager(
    // params, registry);
    // httpClient = new DefaultHttpClient(ccm, params);
    // }
    // return httpClient;
    // } catch (Exception e) {
    // if (httpClient == null) {
    // httpClient = new DefaultHttpClient();
    // }
    // return httpClient;
    // }
    // }

    private class getFilePkpassFromServer extends
            AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd_loading = ProgressDialog.show(mContext, null, null, true, false);
            pd_loading.setContentView(Rconfig.getInstance().layout(
                    "core_base_loading"));
            pd_loading.setCanceledOnTouchOutside(false);
            pd_loading.setCancelable(false);
            pd_loading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            byte[] data;
            // HttpClient httpclient = getNewHttpClient();
//			HttpClient httpclient = new DefaultHttpClient();
//			HttpPost httppost = new HttpPost(url);
//			String userAgent = System.getProperty("http.agent");
//			String cookie = Config.newInstance().getCookie();
//			httppost.setHeader("Cookie", cookie);
//			// read response
//			try {
//				HttpResponse response = httpclient.execute(httppost);
//				InputStream input = response.getEntity().getContent();
//				data = new byte[input.available()];
//				input.read(data);
//				File path = new File(Environment.getExternalStorageDirectory()
//						+ file_pkpass);
//				if (!path.isFile()) {
//					path.createNewFile();
//				}
//				OutputStream outputStream = new FileOutputStream(path);
//				int read = 0;
//				byte[] bytes = new byte[1024];
//				while ((read = input.read(bytes)) != -1) {
//					outputStream.write(bytes, 0, read);
//				}
//			} catch (Exception e) {
//				Log.d("Error:", e.getMessage());
//			}

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd_loading.dismiss();
            String url_file = "file:///sdcard" + file_pkpass;
            launchPassWallet(mContext, Uri.parse(url_file), true);
        }
    }
}
