package com.simicart.theme.ztheme.home.controller;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.theme.ztheme.home.entity.ZThemeCatalogEntity;

public class HomeZThemeControllerTablet extends HomeZThemeController {
	OnItemClickListener onClickItemCat;

	@Override
	protected void onAction() {
		// requestCatTree();
		onClickItemCat = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				switch (mCategories.get(position).getType()) {
//				case ZThemeCatalogEntity.TYPE_CAT:
//					mDelegate.showCatSub(mCategories.get(position));
//					break;
//				case ZThemeCatalogEntity.TYPE_SPOT:
//					selecteSpot(mCategories.get(position)
//							.getZThemeSpotEntity());
//					break;
//				default:
//					break;
//				}
			}
		};

	}

	public OnItemClickListener getOnClickItemCat() {
		return onClickItemCat;
	}

}
