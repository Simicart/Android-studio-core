package com.simicart.plugins.wishlist.block;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.wishlist.adapter.MyWishListAdapter;
import com.simicart.plugins.wishlist.delegate.MyWishListDelegate;
import com.simicart.plugins.wishlist.entity.ItemWishList;

import java.util.ArrayList;

public class MyWistListBlock extends SimiBlock implements MyWishListDelegate {

	public static final String TITLE = "My Wishlist";
	public static final String ITEMS = "Items";
	public static final String ITEM = "Item";
	public static final String SHARE_WISHLIST = "Share Wishlist";

	private ArrayList<ItemWishList> mWishLists;

	protected LinearLayout ll_share_wishlist;
	protected ImageView im_shareAll;
	protected TextView tv_shareAll;
	protected MyWishListAdapter mAdapter;
	protected RelativeLayout rlt_layout_top;
	protected RecyclerView rv_wishlist;

	public void setShareListener(OnTouchListener touchListener) {
		ll_share_wishlist.setOnTouchListener(touchListener);
	}

	public MyWistListBlock(View view, Context context) {
		super(view, context);
	}

	@Override
	public void initView() {
		tv_shareAll = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_shareall"));
		tv_shareAll.setText(SimiTranslator.getInstance().translate(SHARE_WISHLIST));

		ll_share_wishlist = (LinearLayout) mView.findViewById(Rconfig
				.getInstance().id("ll_share_wishlist"));
		im_shareAll = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"im_shareall"));
		im_shareAll.setBackgroundDrawable(AppColorConfig.getInstance().getIcon("wishlist_share_icon"));
		rlt_layout_top = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("rl_mywishlist_top"));
		rv_wishlist = (RecyclerView) mView.findViewById(Rconfig.getInstance().id("rv_mywistlist"));
		if(DataLocal.isTablet) {
			rv_wishlist.setLayoutManager(new GridLayoutManager(mContext, 2));
		} else {
			rv_wishlist.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
		}
	}

	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> entity = collection.getCollection();
		if (null != entity && entity.size() > 0) {
			mWishLists = new ArrayList<ItemWishList>();
			for (SimiEntity simiEntity : entity) {
				ItemWishList itemWishList = (ItemWishList) simiEntity;
				mWishLists.add(itemWishList);
			}
			setWishLists();
		}
	}

	public void setWishLists() {
		if (null == mWishLists || mWishLists.size() == 0) {
			rlt_layout_top.setVisibility(View.GONE);
			LinearLayout ll_body = (LinearLayout) mView.findViewById(Rconfig
					.getInstance().id("ll_body"));
			ll_body.removeAllViewsInLayout();
			TextView tv_notify = new TextView(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			params.gravity = Gravity.CENTER;
			tv_notify.setText(SimiTranslator.getInstance().translate(
					"Your Wishlist is empty"));
			tv_notify.setGravity(Gravity.CENTER);
			tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
			ll_body.addView(tv_notify, params);
			return;
		}
		mAdapter = new MyWishListAdapter(mWishLists, this);
		rv_wishlist.setAdapter(mAdapter);
	}

	// MyWishListDelegate
	@Override
	public void setWishlist_qty(int wishlist_qty) {
		TextView tv_qtyItem = (TextView) mView.findViewById(Rconfig
				.getInstance().id("tv_qtyItem"));
		if (wishlist_qty < 2) {
			tv_qtyItem.setText(wishlist_qty + " "
					+ SimiTranslator.getInstance().translate(ITEM));
		} else {
			tv_qtyItem.setText(wishlist_qty + " "
					+ SimiTranslator.getInstance().translate(ITEMS));
		}
	}

	@Override
	public void updateData(ArrayList<ItemWishList> items) {
		mWishLists = items;
		setWishLists();
	}

	@Override
	public boolean isShown() {
		return mView.isShown();
	}

	@Override
	public void showDetail(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestShowNext() {
		// TODO Auto-generated method stub

	}
}
