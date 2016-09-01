package com.simicart.core.base.component;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by frank on 01/09/2016.
 */
public class SimiDOBRowComponent extends SimiRowComponent {

    protected RelativeLayout rltHeader;
    protected TextView tvDOB;
    protected ImageView imgExtend;
//    protected DatePicker dpDOB;
    protected int mDay;
    protected int mMonth;
    protected int mYear;
    protected ArrayList<Integer> mDOBValue;
    protected DatePickerDialog dobDialog;

    public SimiDOBRowComponent(int day, int month, int year) {
        super();
        mDay = day;
        mMonth = month;
        mYear = year;
    }

    @Override
    protected void initView() {
        rootView = findLayout("core_component_dob_row");

    }

    @Override
    protected void initBody() {
        rltHeader = (RelativeLayout) findView("rlt_header");
        tvDOB = (TextView) findView("tv_dob");
        imgExtend = (ImageView) findView("img_extend");
//        dpDOB = (DatePicker) findView("dp_dob");
//        dpDOB.getParent().requestDisallowInterceptTouchEvent(true);

        rltHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(dobDialog.isShowing()){
                   dobDialog.dismiss();
               }else{
                   dobDialog.show();
               }
            }
        });

        initDOB();
    }

    protected void initDOB() {
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        if (mDay == 0) {
            mDay = day;
        }
        if (mMonth == 0) {
            mMonth = month;
        }
        if (mYear == 0) {
            mYear = year;
        }
        updateDOB();

        dobDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear + 1;
                mDay = dayOfMonth;
                updateDOB();
            }
        },mYear, mMonth, mDay);


    }

    protected void updateDOB() {
        StringBuilder builder = new StringBuilder();
        if (mDay < 10) {
            String day = "0" + mDay;
            builder.append(day);
        } else {
            builder.append(String.valueOf(mDay));
        }
        builder.append("-");
        if (mMonth < 10) {
            String month = "0" + mMonth;
            builder.append(month);
        } else {
            builder.append(String.valueOf(mMonth));
        }
        builder.append("-");

        builder.append(mYear);

        tvDOB.setText(builder.toString());

    }


    @Override
    public Object getValue() {
        mDOBValue = new ArrayList<>();
        mDOBValue.add(mDay);
        mDOBValue.add(mMonth);
        mDOBValue.add(mYear);
        return mDOBValue;
    }
}
