package com.simicart.core.home.block;

import android.content.Context;
import android.view.View;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.config.Config;
import com.simicart.core.home.controller.CategoryHomeListener;
import com.simicart.core.home.delegate.CategoryHomeDelegate;

import java.util.ArrayList;

public class CategoryHomeBlock extends SimiBlock implements
		CategoryHomeDelegate {

	private View mView;
	private CategoryHomeListener categoryHomeListener;

	public CategoryHomeBlock(View view, Context context) {
		super(view, context);
		this.mView = view;
	}

	public void showCategorys(ArrayList<Category> listCategory) {

	}

	private void showFakeCategorys() {
		ArrayList<Category> categories = new ArrayList<Category>();
		for (int i = 0; i < 4; i++) {
			Category category1 = new Category();
			category1.setCategoryId("fake");
			category1.setCategoryName("Category " + i);
			category1.setChild(false);
			category1.setCategoryImage("fake_category");
			categories.add(category1);
		}
		showCategorys(categories);
	}

	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> entity = collection.getCollection();
		if (null != entity && entity.size() > 0) {
			ArrayList<Category> categories = new ArrayList<Category>();
			for (SimiEntity simiEntity : entity) {
				Category category = (Category) simiEntity;
				categories.add(category);
			}
			if (categories.size() > 0) {
				showCategorys(categories);
			}
		} else {
			if (Config.getInstance().isDemoVersion()) {
				showFakeCategorys();
			} else {
				mView.setVisibility(View.GONE);
			}
		}
	}
}
