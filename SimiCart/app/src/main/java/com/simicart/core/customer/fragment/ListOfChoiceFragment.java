package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.common.KeyData;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.ListOfChoiceBlock;
import com.simicart.core.customer.controller.ListOfChoiceController;
import com.simicart.core.customer.delegate.ListOfChoiceDelegate;

import java.util.ArrayList;

public class ListOfChoiceFragment extends SimiFragment {

    public static ListOfChoiceFragment newInstance(SimiData data) {
        ListOfChoiceFragment fragment = new ListOfChoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }


    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(
                Rconfig.getInstance().layout("core_fragment_list_choice"), null,
                false);
        Context context = getActivity();

        ListOfChoiceDelegate delegate = (ListOfChoiceDelegate) getValueWithKey(KeyData.LIST_OF_CHOICE.DELEGATE);
        ArrayList<String> data = (ArrayList<String>) getValueWithKey(KeyData.LIST_OF_CHOICE.LIST_DATA);

        ListOfChoiceBlock block = new ListOfChoiceBlock(rootView, context);
        block.initView();

        ListOfChoiceController controller = new ListOfChoiceController();
        controller.setData(data);
        controller.setDelegate(delegate);
        controller.onStart();

        block.setOnItemClicker(controller.getSelectItemListener());
        return rootView;
    }
}
