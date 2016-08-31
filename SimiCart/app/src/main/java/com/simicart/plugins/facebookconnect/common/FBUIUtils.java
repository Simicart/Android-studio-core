package com.simicart.plugins.facebookconnect.common;

import java.text.DecimalFormat;

import android.content.Context;

import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.Config;


public class FBUIUtils {

	public static String numberToShortenedString(Context context, Long num) {

		if (num < 1000)
			return num.toString();

		String format = "###,###.#";

		if (num > 9999) {
			format = "###,###";
		}

		DecimalFormat oneDForm = new DecimalFormat(format);

		return oneDForm.format((double) num / 1000) + SimiTranslator.getInstance().translate("k");

	}

}
