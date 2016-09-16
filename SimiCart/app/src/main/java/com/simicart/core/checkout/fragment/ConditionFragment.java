package com.simicart.core.checkout.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;

public class ConditionFragment extends SimiFragment {

	public static ConditionFragment newInstance(SimiData data) {
		ConditionFragment fragment = new ConditionFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable("data", data);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(
				Rconfig.getInstance().layout(
						"core_information_description_layout"), container,
				false);
		String content  = null;
		if(mHashMapData.containsKey(KeyData.CONDITION_PAGE.CONTENT)){
			content = (String) mHashMapData.get(KeyData.CONDITION_PAGE.CONTENT);
		}

		WebView webView = (WebView) rootView.findViewById(Rconfig.getInstance()
				.id("webview"));

		WebSettings setting = webView.getSettings();
		webView.setBackgroundColor(AppColorConfig.getInstance().getAppBackground());
		setting.setJavaScriptEnabled(true);
		setting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		setting.setLoadWithOverviewMode(true);
		setting.setUseWideViewPort(true);
		setting.setDisplayZoomControls(true);
		setting.setLoadsImagesAutomatically(true);
		setting.setTextSize(TextSize.LARGEST);
		webView.setVerticalScrollBarEnabled(true);
		webView.setHorizontalScrollBarEnabled(false);

		webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);

		return rootView;
	}
}
