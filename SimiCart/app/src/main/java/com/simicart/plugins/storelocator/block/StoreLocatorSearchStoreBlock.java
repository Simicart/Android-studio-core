package com.simicart.plugins.storelocator.block;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.storelocator.adapter.CountrySearchAdapter;
import com.simicart.plugins.storelocator.adapter.SearchTagAdapter;
import com.simicart.plugins.storelocator.delegate.StoreLocatorSearchStoreDelegate;
import com.simicart.plugins.storelocator.entity.CountryObject;
import com.simicart.plugins.storelocator.entity.SearchObject;

import java.util.ArrayList;

public class StoreLocatorSearchStoreBlock extends SimiBlock implements StoreLocatorSearchStoreDelegate {

    protected SearchObject search_object;
    protected ArrayList<String> listConfigs;
    protected ArrayList<CountryObject> listCountries;
    protected ArrayList<String> listTags;
    protected int countryPosition = 0;
    protected int tagPosition = 0;
    protected String country_code;
    protected SearchTagAdapter tagSearchAdapter;
    private EditText et_city, et_state, et_code;
    private LinearLayout btn_search, one, two, three, for_, search, clear_all;
    private RecyclerView list_store_tag;
    private TextView txt_search_Area, txt_search_tag, txt_search, txt_country, txt_city, txt_code, txt_state;
    private Spinner spCountry;

    public StoreLocatorSearchStoreBlock(View view, Context context) {
        super(view, context);
        // TODO Auto-generated constructor stub
    }

    public void onSearchClick(OnClickListener listener) {
        btn_search.setOnClickListener(listener);
    }

    public void onClearAllClick(OnClickListener listener) {
        clear_all.setOnClickListener(listener);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        listConfigs = new ArrayList<>();
        listCountries = new ArrayList<>();
        listTags = new ArrayList<>();
        listTags.add(SimiTranslator.getInstance().translate("All"));

        et_city = (EditText) mView.findViewById(Rconfig.getInstance().getIdLayout("et_city"));
        et_state = (EditText) mView.findViewById(Rconfig.getInstance().getIdLayout("et_state"));
        et_code = (EditText) mView.findViewById(Rconfig.getInstance().getIdLayout("et_code"));
        // country = (LinearLayout) mView.findViewById(Rconfig.newInstance()
        // .getIdLayout("layout_country"));
        // txt_countryName = (TextView) mView.findViewById(Rconfig.newInstance()
        // .getIdLayout("txt_name_country"));
        spCountry = (Spinner) mView.findViewById(Rconfig.getInstance().id("sp_country"));
        list_store_tag = (RecyclerView) mView.findViewById(Rconfig.getInstance().getIdLayout("list_store_tag"));
        list_store_tag.setLayoutManager(new GridLayoutManager(mContext, 3));
        list_store_tag.setNestedScrollingEnabled(false);
        txt_search_Area = (TextView) mView.findViewById(Rconfig.getInstance().getIdLayout("txt_Search_by_Area"));
        txt_search_tag = (TextView) mView.findViewById(Rconfig.getInstance().getIdLayout("txt_Search_by_Task"));
        txt_country = (TextView) mView.findViewById(Rconfig.getInstance().getIdLayout("txt_country"));
        txt_code = (TextView) mView.findViewById(Rconfig.getInstance().getIdLayout("txt_code"));
        txt_city = (TextView) mView.findViewById(Rconfig.getInstance().getIdLayout("txt_city"));
        txt_search = (TextView) mView.findViewById(Rconfig.getInstance().getIdLayout("txt_search"));
        txt_state = (TextView) mView.findViewById(Rconfig.getInstance().getIdLayout("txt_state"));
        one = (LinearLayout) mView.findViewById(Rconfig.getInstance().getIdLayout("one"));
        two = (LinearLayout) mView.findViewById(Rconfig.getInstance().getIdLayout("two"));
        three = (LinearLayout) mView.findViewById(Rconfig.getInstance().getIdLayout("three"));
        for_ = (LinearLayout) mView.findViewById(Rconfig.getInstance().getIdLayout("for_"));
        search = (LinearLayout) mView.findViewById(Rconfig.getInstance().getIdLayout("llSearch"));
        btn_search = (LinearLayout) mView.findViewById(Rconfig.getInstance().getIdLayout("btn_search"));
        clear_all = (LinearLayout) mView.findViewById(Rconfig.getInstance().getIdLayout("clear_all"));
        if (search_object != null) {
            if (Utils.validateString(search_object.getCity())) {
                et_city.setText(search_object.getCity());
            }
            if (Utils.validateString(search_object.getState())) {
                et_state.setText(search_object.getState());
            }
            if (Utils.validateString(search_object.getZipcode())) {
                et_code.setText(search_object.getZipcode());
            }
        }
        txt_search_Area.setText(SimiTranslator.getInstance().translate("Search By Area"));
        txt_search_tag.setText(SimiTranslator.getInstance().translate("Filter by Tag"));
        txt_country.setText(SimiTranslator.getInstance().translate("Country:"));
        txt_city.setText(SimiTranslator.getInstance().translate("City") + ":");
        txt_code.setText(SimiTranslator.getInstance().translate("Zip Code") + ":");
        txt_search.setText(SimiTranslator.getInstance().translate("Search"));
        txt_state.setText(SimiTranslator.getInstance().translate("State") + ":");
        TextView txt_clear = (TextView) mView.findViewById(Rconfig.getInstance().getIdLayout("clear_search"));
        txt_clear.setText(SimiTranslator.getInstance().translate("Clear search"));

    }

    @Override
    public void drawView(SimiCollection collection) {
        // TODO Auto-generated method stub
        initConfig();
        initCountry();
        initTagSearch();
    }

    protected void initConfig() {
        if (listConfigs.size() > 0) {
            search.setVisibility(View.VISIBLE);
            clear_all.setVisibility(View.VISIBLE);
            for (int i = 0; i < listConfigs.size(); i++) {
                String tag = listConfigs.get(i);
                if (Utils.validateString(tag)) {
                    if (tag.equals("1")) {
                        one.setVisibility(View.VISIBLE);
                    } else if (tag.equals("2")) {
                        two.setVisibility(View.VISIBLE);
                    } else if (tag.equals("3")) {
                        three.setVisibility(View.VISIBLE);
                    } else if (tag.equals("4")) {
                        for_.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            search.setVisibility(View.GONE);
            clear_all.setVisibility(View.GONE);
        }
    }

    public void initCountry() {
        if (listCountries.size() > 0) {
            // txt_countryName.setText(listCountries.get(countryPosition).getCountry_name());
            country_code = listCountries.get(countryPosition).getCountry_code();
            CountrySearchAdapter countrySearchAdapter = new CountrySearchAdapter(mContext, listCountries);
            spCountry.setAdapter(countrySearchAdapter);
            if (search_object != null) {
                spCountry.setSelection(search_object.getPosition_country());
            }
        }
    }

    public void initTagSearch() {
        tagSearchAdapter = new SearchTagAdapter(listTags, tagPosition);
        list_store_tag.setAdapter(tagSearchAdapter);
    }

    @Override
    public void setListConfig(ArrayList<String> listConfigs) {
        // TODO Auto-generated method stub
        this.listConfigs = listConfigs;
    }

    @Override
    public void setListCountry(ArrayList<CountryObject> listCountries) {
        // TODO Auto-generated method stub
        this.listCountries = listCountries;
    }

    @Override
    public void setListTag(ArrayList<String> listTags) {
        // TODO Auto-generated method stub
        this.listTags.addAll(listTags);
    }

    @Override
    public SearchObject getSearchObject() {
        // TODO Auto-generated method stub
        search_object = new SearchObject();

        int country = spCountry.getSelectedItemPosition();
        search_object.setPosition_country(country);

        String countryName = listCountries.get(country).getCountry_code();
        if (Utils.validateString(countryName)) {
            search_object.setName_country(countryName);
        }

        String city = et_city.getText().toString();
        if (Utils.validateString(city)) {
            search_object.setCity(city);
        }

        String state = et_state.getText().toString();
        if (Utils.validateString(state)) {
            search_object.setState(state);
        }

        String zip = et_code.getText().toString();
        if (Utils.validateString(zip)) {
            search_object.setZipcode(zip);
        }

        return search_object;
    }

    public void setSearchObject(SearchObject object) {
        search_object = object;
    }

}
