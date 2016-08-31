package com.simicart.plugins.facebookconnect.view;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.plugins.facebookconnect.common.FBUIUtils;


public class FacebookLikesButton {

	private Context mContext;
	private ProgressBar mLikeProgess;
	private TextView mLikeCount;

	public FacebookLikesButton(Context con, ProgressBar pro, TextView txt) {
		this.mLikeProgess = pro;
		this.mLikeCount = txt;
		this.mContext = con;
		initView();
	}

	protected void initView() {
		mLikeProgess.setVisibility(View.VISIBLE);
		mLikeCount.setVisibility(View.GONE);
	}

	private class SharesCountFetcherTask extends AsyncTask<String, Void, Long> {

		@Override
		protected Long doInBackground(String... uri) {

//			HttpClient httpclient = new DefaultHttpClient();
//			HttpResponse response;
			Long shares = null;
//			try {
//
//				HttpGet getRequest = new HttpGet(
//						"http://graph.facebook.com/fql?q="
//								+ URLEncoder.encode(
//										"SELECT like_count FROM link_stat WHERE url='"
//												+ uri[0] + "'", "UTF-8"));
//				response = httpclient.execute(getRequest);
//				StatusLine statusLine = response.getStatusLine();
//				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
//					ByteArrayOutputStream out = new ByteArrayOutputStream();
//					response.getEntity().writeTo(out);
//					out.close();
//					JSONObject result = new JSONObject(out.toString());
//					JSONArray data = result.getJSONArray("data");
//					shares = ((JSONObject) data.get(0)).getLong("like_count");
//				} else {
//					// Closes the connection.
//					response.getEntity().getContent().close();
//					throw new IOException(statusLine.getReasonPhrase());
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			return shares;

		}

		@Override
		protected void onPostExecute(Long result) {

			onLikeDownloaded(result);
		}

	}

	public void onLikeDownloaded(Long result) {
		mLikeProgess.setVisibility(View.GONE);
		mLikeCount.setVisibility(View.VISIBLE);
		if (result != null) {
			mLikeCount.setText(FBUIUtils.numberToShortenedString(mContext,
					result));
		} else {
			mLikeCount.setText(SimiTranslator.getInstance().translate("N/A"));
		}
	}

	public void downloadLikes(String likesUrl) {

		new SharesCountFetcherTask().execute(likesUrl);

	}
}
