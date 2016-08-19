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

	protected ExpandableListView lv_category;
	protected ArrayList<ZThemeCatalogEntity> listCatalogs;

	public HomeZThemeBlock(View view, Context context) {
		super(view, context);
	}

	public void setListViewListener(ExpandableListView.OnGroupClickListener groupClickListener,
									ExpandableListView.OnChildClickListener childClickListener) {

		lv_category.setOnGroupClickListener(groupClickListener);

		lv_category.setOnChildClickListener(childClickListener);

	}

	@Override
	public void initView() {
		lv_category = (ExpandableListView) mView.findViewById(Rconfig
				.getInstance().id("lv_category"));
	}

	@Override
	public void drawView(SimiCollection collection) {

		ArrayList<SimiEntity> entity = collection.getCollection();
		listCatalogs = new ArrayList<ZThemeCatalogEntity>();
		if (null != entity && entity.size() > 0) {
			for (SimiEntity simiEntity : entity) {
				ZThemeCatalogEntity ZThemeCatalogEntity = new ZThemeCatalogEntity();
				ZThemeCatalogEntity.setJSONObject(simiEntity.getJSONObject());
				ZThemeCatalogEntity.parse();
				listCatalogs.add(ZThemeCatalogEntity);
			}
		}

		if (listCatalogs.size() > 0) {
			showCategoriesView(listCatalogs);
		}
	}

	protected void showCategoriesView(ArrayList<ZThemeCatalogEntity> categories) {
		HomeZThemeAdapter adapter = new HomeZThemeAdapter(mContext, categories);
		lv_category.setAdapter(adapter);
	}

	@Override
	public ArrayList<ZThemeCatalogEntity> getListCatalog() {
		return listCatalogs;
	}
}
