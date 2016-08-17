package com.simicart.theme.ztheme.home.block;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.theme.ztheme.home.adapter.HomeZThemeAdapter;
import com.simicart.theme.ztheme.home.delegate.HomeZThemeDelegate;
import com.simicart.theme.ztheme.home.entity.ZThemeCatalogEntity;

public class HomeZThemeBlock extends SimiBlock implements HomeZThemeDelegate {

	private static int lastExpandedPosition = -1;
	ExpandableListView lv_category;

	public HomeZThemeBlock(View view, Context context) {
		super(view, context);
	}

	public void setCategoryListener(OnGroupClickListener listener) {
		lv_category.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				// for (int i = 0; i < lv_category.getAdapter().getCount(); i++)
				// {
				// if (i != groupPosition) {
				// lv_category.collapseGroup(i);
				// }
				// }
				if (lastExpandedPosition != -1
						&& groupPosition != lastExpandedPosition) {
					lv_category.collapseGroup(lastExpandedPosition);
				}
				lastExpandedPosition = groupPosition;

				lv_category.setSelectedGroup(groupPosition);
			}
		});

		lv_category.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				// TODO Auto-generated method stub
			}
		});

		lv_category.setOnGroupClickListener(listener);
	}

	public void setChildCategoryListener(OnChildClickListener listener) {
		lv_category.setOnChildClickListener(listener);
	}

	@Override
	public void initView() {
		lv_category = (ExpandableListView) mView.findViewById(Rconfig
				.getInstance().id("lv_category"));
		if (!DataLocal.isTablet) {
			TextView view = new TextView(mContext);
			ListView.LayoutParams tv_lp = new ListView.LayoutParams(
					ListView.LayoutParams.MATCH_PARENT, Utils.getValueDp(40));
			view.setLayoutParams(tv_lp);
			lv_category.addHeaderView(view);
		}
	}

	@Override
	public void drawView(SimiCollection collection) {

		ArrayList<SimiEntity> entity = collection.getCollection();
		ArrayList<ZThemeCatalogEntity> categories = new ArrayList<ZThemeCatalogEntity>();
		if (null != entity && entity.size() > 0) {
			for (SimiEntity simiEntity : entity) {
				ZThemeCatalogEntity ZThemeCatalogEntity = new ZThemeCatalogEntity();
				ZThemeCatalogEntity.setJSONObject(simiEntity.getJSONObject());
				ZThemeCatalogEntity.parse();
				categories.add(ZThemeCatalogEntity);
			}
		} else {
//			for (int i = 1; i < 5; i++) {
//				ZThemeCatalogEntity ZThemeCatalogEntity = new ZThemeCatalogEntity();
//				ZThemeCatalogEntity.setCategoryId("fake");
//				ZThemeCatalogEntity.setType(ZThemeCatalogEntity.TYPE_CAT);
//				ZThemeCatalogEntity.setCategoryName("Category " + i);
//				ZThemeCatalogEntity.setTitle("Category " + i);
//				categories.add(ZThemeCatalogEntity);
//			}
		}

		if (categories.size() > 0) {
			showCategoriesView(categories);
		}
	}

	protected void showCategoriesView(ArrayList<ZThemeCatalogEntity> categories) {
		HomeZThemeAdapter adapter = new HomeZThemeAdapter(mContext, categories);
		lv_category.setAdapter(adapter);
	}

	@Override
	public void showCatSub(ZThemeCatalogEntity category) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelection(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCategoryTree(ArrayList<ZThemeCatalogEntity> categoriesTree) {
		// TODO Auto-generated method stub
	}
}
