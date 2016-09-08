package com.simicart.core.catalog.categorydetail.component;

import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.component.SimiExpandRowComponent;
import com.simicart.core.base.component.SimiIconDeleteRowComponent;
import com.simicart.core.base.component.SimiPopup;
import com.simicart.core.base.component.callback.ExpandRowCallBack;
import com.simicart.core.base.component.callback.IconDeleteRowCallBack;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.categorydetail.delegate.FilterCallBack;
import com.simicart.core.catalog.categorydetail.entity.FilterEntity;
import com.simicart.core.catalog.categorydetail.entity.FilterState;
import com.simicart.core.catalog.categorydetail.entity.ValueFilterEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by frank on 23/08/2016.
 */
public class FilterPopup extends SimiPopup {


    protected ArrayList<FilterState> mStates;
    protected ArrayList<FilterEntity> mListFilter;
    protected LinearLayout llStateSelected;
    protected LinearLayout llFilterValue;
    protected JSONObject mJSONFilter;
    protected View rootView;
    protected String mNameCate;
    protected FilterCallBack mCallBack;

    @Override
    public View createView() {
        rootView = findLayout("core_popup_filter");

        // name category
        TextView tvNameCate = (TextView) findView(rootView, "tv_name_cate");
        if (Utils.validateString(mNameCate)) {
            tvNameCate.setText(mNameCate);
        }


        initSelected();

        initValue();


        return rootView;
    }

    protected void initSelected() {
        LinearLayout ll_selected = (LinearLayout) findView(rootView, "ll_selected");
        if (null != mStates && mStates.size() > 0) {
            TextView tvTitle = (TextView) findView(rootView, "tv_title_selected");
            String translateTitle = SimiTranslator.getInstance().translate("Selected Filter");
            tvTitle.setText(translateTitle);

            llStateSelected = (LinearLayout) findView(rootView, "ll_body_selected");
            createListStateSelected();

            AppCompatButton btnClear = (AppCompatButton) findView(rootView, "btn_clear_filter");
            String translateClear = SimiTranslator.getInstance().translate("Clear All");
            btnClear.setText(translateClear);
            btnClear.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());
            btnClear.setTextColor(AppColorConfig.getInstance().getContentColor());
            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mJSONFilter = null;
                    if (null != mCallBack) {
                        mCallBack.requestFilter(mJSONFilter);
                    }
                }
            });

        } else {
            ll_selected.setVisibility(View.GONE);
        }
    }

    protected void initValue() {

        TextView tvTitle = (TextView) findView(rootView, "tv_tilte_value");
        String translateTitle = SimiTranslator.getInstance().translate("Select a filter");
        tvTitle.setText(translateTitle);


        llFilterValue = (LinearLayout) findView(rootView, "ll_filter_value");
        createListFilter();
    }

    protected void createListStateSelected() {
        if (null != mStates && mStates.size() > 0) {
            for (FilterState state : mStates) {
                createAStateSelected(state);
            }
        }
    }

    protected void createAStateSelected(final FilterState state) {
        SimiIconDeleteRowComponent deleteComponent = new SimiIconDeleteRowComponent();
        String title = state.getTitle();
        String label = state.getLabel();
        String value = String.format("%s : %s", title, label);
        deleteComponent.setValue(value);
        deleteComponent.setCallBack(new IconDeleteRowCallBack() {
            @Override
            public void onDelete() {
                updateJSONFilter(state, false);
            }
        });
        View stateView = deleteComponent.createView();
        llStateSelected.addView(stateView);
    }


    protected void createListFilter() {
        if (null != mListFilter && mListFilter.size() > 0) {
            for (int i = 0; i < mListFilter.size(); i++) {
                FilterEntity filterEntity = mListFilter.get(i);
                createFilter(filterEntity);
            }
        } else {

        }
    }

    protected void createFilter(final FilterEntity filterEntity) {
        SimiExpandRowComponent expandRowComponent = new SimiExpandRowComponent();
        String title = filterEntity.getmTitle();
        expandRowComponent.setValue(title);
        Log.e("FilterPopup", "TITLE " + title);
        final ArrayList<ValueFilterEntity> values = filterEntity.getmValueFilters();
        if (null != values && values.size() > 0) {
            ArrayList<String> listChild = new ArrayList<>();
            for (int i = 0; i < values.size(); i++) {
                listChild.add(values.get(i).getLabel());
            }
            expandRowComponent.setListValue(listChild);
        }
        expandRowComponent.setCallBack(new ExpandRowCallBack() {
            @Override
            public boolean onSelect(int positionValue) {
                if (null == mJSONFilter) {
                    mJSONFilter = new JSONObject();
                }

                String key = filterEntity.getmAttribute();
                String value = values.get(positionValue).getmValue();
                try {
                    mJSONFilter.put(key, value);
                    dismiss();
                    if (null != mCallBack) {
                        mCallBack.requestFilter(mJSONFilter);
                    }
                } catch (JSONException e) {
                    return false;
                }

                return true;
            }
        });
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.topMargin = topMargin;
        llFilterValue.addView(expandRowComponent.createView());

    }

    protected void updateJSONFilter(FilterState state, boolean isAdd) {
        if (isAdd) {

        } else {
            String attribute = state.getAttribute();
            if (null != mJSONFilter && mJSONFilter.has(attribute)) {
                mJSONFilter.remove(attribute);
            }
            if (null != mCallBack) {
                mCallBack.requestFilter(mJSONFilter);
            }
            dismiss();
        }
    }

    public ArrayList<FilterEntity> getListFilter() {
        return mListFilter;
    }

    public void setListFilter(ArrayList<FilterEntity> mListFilter) {
        this.mListFilter = mListFilter;
    }

    public String getNameCate() {
        return mNameCate;
    }

    public void setNameCate(String mNameCate) {
        this.mNameCate = mNameCate;
    }

    public ArrayList<FilterState> getStates() {
        return mStates;
    }

    public void setStates(ArrayList<FilterState> mStates) {
        this.mStates = mStates;
    }

    public void setCallBack(FilterCallBack callBack) {
        mCallBack = callBack;
    }

    public void setJSONFilter(JSONObject jsFilter) {
        mJSONFilter = jsFilter;
    }
}
