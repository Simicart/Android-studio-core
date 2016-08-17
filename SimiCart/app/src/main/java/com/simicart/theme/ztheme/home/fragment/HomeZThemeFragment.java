package com.simicart.theme.ztheme.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.listproducts.entity.TagSearch;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.home.block.SearchHomeBlock;
import com.simicart.core.home.component.SearchComponent;
import com.simicart.theme.ztheme.home.block.HomeZThemeBlock;
import com.simicart.theme.ztheme.home.controller.HomeZThemeController;

public class HomeZThemeFragment extends SimiFragment {
	protected HomeZThemeController mController;
	protected HomeZThemeBlock mBlock;
	protected SearchHomeBlock mSearchHomeBlock;

	public static HomeZThemeFragment newInstance() {
		HomeZThemeFragment fragment = new HomeZThemeFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScreenName("Home Screen");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				Rconfig.getInstance().layout("theme_z_fragment_home"), null);

		Context context = getActivity();

		mBlock = new HomeZThemeBlock(rootView, context);
		mBlock.initView();
		if (null == mController) {
			mController = new HomeZThemeController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		mBlock.setCategoryListener(mController.getmGroupExpand());
		mBlock.setChildCategoryListener(mController.getmChildClick());

		// initial search
		SearchComponent searchComponent = new SearchComponent();
		View searchView = searchComponent.createView();
		LinearLayout llSearch = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id("ll_search"));
		llSearch.addView(searchView);

		return rootView;
	}
}
