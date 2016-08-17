package com.simicart.core.home.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;

/**
 * Created by frank on 7/14/16.
 */
public class SearchComponent extends SimiComponent {

    protected EditText edtQuery;
    protected ImageView imgDelete;
    protected ImageView imgIconSearch;
    protected SearchCallBack mCallBack;


    @Override
    public View createView() {
        rootView = findLayout("core_component_search");

        rootView.setBackgroundColor(AppColorConfig.getInstance().getSearchBoxBackground());


        edtQuery = (EditText) findView("edt_query");
        edtQuery.setTextColor(AppColorConfig.getInstance().getSearchTextColor());
        edtQuery.setBackgroundColor(AppColorConfig.getInstance().getSearchBoxBackground());
        String searchText = SimiTranslator.getInstance().translate("Search Products");
        edtQuery.setHint(searchText);


        imgDelete = (ImageView) findView("img_delete");
        Drawable icDelete = AppColorConfig.getInstance().getIcon("ic_delete");
        imgDelete.setImageDrawable(icDelete);

        imgIconSearch = (ImageView) findView("img_search");
        Drawable icSearch = AppColorConfig.getInstance().getIcon("ic_search");
        imgIconSearch.setImageDrawable(icSearch);

        RelativeLayout rltSearch = (RelativeLayout) findView("rlt_search");
        rltSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableSearchAction(true);
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableSearchAction(false);
            }
        });

        edtQuery.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    enableSearchAction(true);
                }
            }
        });

        edtQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableSearchAction(true);
            }
        });


        edtQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = edtQuery.getText().toString();
                    hiddenKeyboard(v);
                    if (null != mCallBack) {
                        mCallBack.performSearch(query);
                    } else {
                        performSearch(query);
                    }
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    protected void enableSearchAction(boolean isEnable) {
        if (isEnable) {

            RelativeLayout.LayoutParams paramsIcon = new RelativeLayout.LayoutParams(Utils.toDp(15), Utils.toDp(15));
            paramsIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramsIcon.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsIcon.rightMargin = Utils.toDp(5);
            paramsIcon.leftMargin = Utils.toDp(16);
            imgIconSearch.setLayoutParams(paramsIcon);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.toDp(30));
            params.addRule(RelativeLayout.RIGHT_OF, imgIconSearch.getId());
            edtQuery.setLayoutParams(params);
            imgDelete.setVisibility(View.VISIBLE);


        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.toDp(30));
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            edtQuery.setLayoutParams(params);
            imgDelete.setVisibility(View.GONE);

            RelativeLayout.LayoutParams paramsIcon = new RelativeLayout.LayoutParams(Utils.toDp(15), Utils.toDp(15));
            paramsIcon.addRule(RelativeLayout.LEFT_OF, edtQuery.getId());
            paramsIcon.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsIcon.rightMargin = Utils.toDp(5);
            paramsIcon.leftMargin = Utils.toDp(5);
            imgIconSearch.setLayoutParams(paramsIcon);

            edtQuery.clearFocus();
            hiddenKeyboard(edtQuery);
        }

    }


    public void setCallBack(SearchCallBack callBack) {
        mCallBack = callBack;

    }

    protected void performSearch(String query) {
//        SimiData data = new SimiData();
//        HashMap<String, Object> hs = new HashMap<>();
//        hs.put("q", query);
//        data.setHsData(hs);
//        CategoryDetailFragment fragment = CategoryDetailFragment.newInstance(data);
//        SimiManager.getIntance().replaceFragment(fragment);
    }

    protected void hiddenKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getRootView().getWindowToken(), 0);
    }

}
