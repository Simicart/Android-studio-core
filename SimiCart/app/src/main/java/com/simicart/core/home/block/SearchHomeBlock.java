package com.simicart.core.home.block;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.listproducts.fragment.SearchListFragment;
import com.simicart.core.catalog.listproducts.adapter.ListPopupAdapter;
import com.simicart.core.catalog.listproducts.entity.ItemListPopup;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

public class SearchHomeBlock extends SimiBlock {

    protected String mCatID = "-1";
    protected String mCatName = "";
    protected String mQuery = "";
    protected String sortID = "None";
    private RelativeLayout rlt_layout;
    private String tag;
    private EditText et_search;
    private RelativeLayout relativeLayout;

    private PopupWindow popUp;

    public SearchHomeBlock(View view, Context context) {
        super(view, context);
        view.setBackgroundColor(0);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setCateID(String catID) {
        this.mCatID = catID;
    }

    public void setCatName(String catName) {
        this.mCatName = catName;
    }

    @Override
    public void initView() {
        LinearLayout ll_search = (LinearLayout) mView.findViewById(Rconfig
                .getInstance().id("ll_search"));
        ll_search.setBackgroundColor(AppColorConfig.getInstance().getSearchBoxBackground());

        rlt_layout = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
                .id("rlt_layout"));
        relativeLayout = (RelativeLayout) mView.findViewById(Rconfig
                .getInstance().id("rlt_layout"));
        ImageView img_ic_search = (ImageView) mView.findViewById(Rconfig
                .getInstance().id("img_ic_search"));
        Drawable drawable = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("ic_search"));
        drawable.setColorFilter(AppColorConfig.getInstance().getSearchIConColor(),
                PorterDuff.Mode.SRC_ATOP);
        img_ic_search.setImageDrawable(drawable);

        et_search = (EditText) mView.findViewById(Rconfig.getInstance().id(
                "et_search"));
        et_search.setHint(SimiTranslator.getInstance().translate("Search"));
        if (!mCatName.equals("")
                && !mCatName.equals(SimiTranslator.getInstance()
                .translate("all products"))) {
            et_search.setHint("" + mCatName);
            et_search.setTypeface(null, Typeface.BOLD);
        }
        if (!mQuery.equals("")) {
            et_search.setText(mQuery);
        }
        et_search.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        et_search.setTextColor(AppColorConfig.getInstance().getSearchTextColor());
        et_search.setHintTextColor(AppColorConfig.getInstance().getSearchTextColor());
        et_search.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    rlt_layout.setVisibility(View.GONE);
                } else {
                    rlt_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        et_search.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)
                        && (event.getAction() == KeyEvent.ACTION_DOWN)) {
                    showSearchScreen(et_search.getText().toString(), null, null, tag);
                    Utils.hideKeyboard(v);
                    hidePopupListView();
                    return true;
                }
                return false;
            }
        });
        popUp = popupWindowsort();
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mCatName != null && !mCatName.equals("")
                        && !mCatName.equals("all categories")) {
                    if (s.length() > 0 && et_search.hasFocus()) {
                        popupWindowsort();
                        if (popUp.isShowing()) {
                        } else {
                            popUp.showAsDropDown(et_search, 0, 0);
                        }
                    } else {
                        hidePopupListView();
                    }
                }
            }
        });
        relativeLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                et_search.requestFocus();
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_search, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        updateView(new SimiCollection());
    }

    private PopupWindow popupWindowsort() {
        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(mContext);
        popupWindow.setFocusable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        ArrayList<ItemListPopup> listItem = new ArrayList<ItemListPopup>();
        ItemListPopup item1 = new ItemListPopup();
        item1.setName(mCatName);
        item1.setCheckSearch(false);
        listItem.add(item1);
        ItemListPopup item2 = new ItemListPopup();
        item2.setName(SimiTranslator.getInstance().translate("all categories"));
        item2.setCheckSearch(true);
        listItem.add(item2);
        ListPopupAdapter adapter = new ListPopupAdapter(mContext, listItem);
        // the drop down list is a list view
        ListView listViewSort = new ListView(mContext);
        // listViewSort.setBackgroundColor(Color.parseColor("#E6ffffff"));
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemListPopup item = (ItemListPopup) parent.getItemAtPosition(position);
                if (item.getName().equals("all categories")) {
                    showSearchScreen(et_search.getText().toString(), null, null, tag);
                } else {
                    showSearchScreen(et_search.getText().toString(), mCatID, mCatName, tag);
                }
                hidePopupListView();
                SimiManager.getIntance().hideKeyboard();
            }
        });
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setContentView(listViewSort);
        return popupWindow;
    }

    private void hidePopupListView() {
        if (popUp != null) {
            if (popUp.isShowing()) {
                popUp.dismiss();
            }
        }
        SimiManager.getIntance().hideKeyboard();
    }

    public void showSearchScreen(String key, String mCatID, String mCatName, String tag) {
        //Neu ko có cateID thi la search all; ko thi search cate
        if (key != null && !key.equals("")) {
            SearchListFragment fragment = SearchListFragment.newInstance(key, mCatID, mCatName, sortID, null, tag);
            SimiManager.getIntance().addFragment(fragment);
        }
    }

    @Override
    public void drawView(SimiCollection collection) {
    }

    public void setQuery(String mQuery) {
        this.mQuery = mQuery;
    }

    public void onDestroy() {
        hidePopupListView();
    }
}