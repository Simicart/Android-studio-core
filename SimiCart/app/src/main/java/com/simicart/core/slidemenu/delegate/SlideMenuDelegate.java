package com.simicart.core.slidemenu.delegate;

import com.simicart.core.slidemenu.entity.ItemNavigation;

import java.util.ArrayList;

public interface SlideMenuDelegate {
	public void onSelectedItem(int position);

	public void onRefresh();

	public void setAdapter(ArrayList<ItemNavigation> items);

	public void setUpdateSignIn(String name);
}
