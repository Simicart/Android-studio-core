package com.simicart.core.home.component;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.home.delegate.CateHomeCallBack;

import java.util.ArrayList;

/**
 * Created by frank on 17/08/2016.
 */
public class CateHomeComponent extends SimiComponent {

    protected TextView tvTitle;
    protected RecyclerView rcvCate;
    protected CateHomeCallBack mCallBack;
    protected ArrayList<Category> mListCate;

    public CateHomeComponent(ArrayList<Category> categories){
        super();
        mListCate = categories;
    }

    @Override
    public View createView() {
        rootView = findLayout("core_component_cate_home");
        tvTitle = (TextView) findView("tv_title");
        rcvCate = (RecyclerView) findView("rcv_cate");
        
        showTitle();
        showCate();
        return rootView;
    }

    protected  void showTitle(){
        String title = SimiTranslator.getInstance().translate("Category").toUpperCase();
        tvTitle.setText(title);
    }

    protected void showCate(){

    }


}
