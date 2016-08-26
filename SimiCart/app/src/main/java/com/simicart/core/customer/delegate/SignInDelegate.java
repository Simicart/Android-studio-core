package com.simicart.core.customer.delegate;

import android.content.res.ColorStateList;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.customer.entity.ProfileEntity;

public interface SignInDelegate extends SimiDelegate {

	public void changeColorSignIn(ColorStateList color);

	public ProfileEntity getProfileSignIn();
}
