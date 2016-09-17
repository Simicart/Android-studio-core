package com.simicart.core.catalog.categorydetail.component;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.simicart.core.adapter.SortAdapter;
import com.simicart.core.base.component.SimiPopup;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.category.entity.Sort;
import com.simicart.core.catalog.categorydetail.delegate.SortCallBack;

import java.util.ArrayList;

/**
 * Created by frank on 23/08/2016.
 */
public class SortPopup extends SimiPopup {

    protected TextView tvTitle;
    protected ListView lvSort;
    protected int mCurentValue;
    protected SortCallBack mCallBack;

    public SortPopup() {
        super();
    }

    @Override
    public View createView() {
        View rootView = findLayout("core_popup_sort");
        // title
        tvTitle = (TextView) findView(rootView, "tv_title");
        String translateSort = SimiTranslator.getInstance().translate("Sort");
        tvTitle.setText(translateSort);

        lvSort = (ListView) findView(rootView, "lv_sort");
        initListSort();

        return rootView;
    }

    protected void initListSort() {
        String key[] = {"None", "Price: Low to High", "Price: High to Low",
                "Name: A to Z", "Name: Z to A"};
        ArrayList<Sort> listSort = new ArrayList<>();
        for (int i = 0; i < key.length; i++) {
            Sort item = new Sort();
            item.setId(i);
            item.setTitle(key[i]);
            listSort.add(item);
        }

        SortAdapter adapter = new SortAdapter(listSort);
        adapter.setCurrentValue(mCurentValue);
        lvSort.setAdapter(adapter);
        lvSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurentValue = position;
                if (null != mCallBack) {
                    mCallBack.onSort(mCurentValue);
                }
                dismiss();
            }
        });
    }

    public void setCallBack(SortCallBack callBack) {
        mCallBack = callBack;
    }

    public int getCurrentValue() {
        return mCurentValue;
    }

    public void setCurrentValue(int currentValue) {
        mCurentValue = currentValue;
    }

}
