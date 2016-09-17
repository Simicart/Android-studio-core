package com.simicart.core.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.SimiData;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.fragment.BasicInforFragment;
import com.simicart.core.catalog.product.fragment.CustomerReviewFragment;
import com.simicart.core.catalog.product.fragment.DescriptionFragment;
import com.simicart.core.catalog.product.fragment.RelatedProductFragment;
import com.simicart.core.catalog.product.fragment.TechSpecsFragment;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class TabAdapterFragment extends FragmentStatePagerAdapter {
    protected Product mProduct;
    protected ArrayList<SimiFragment> mListFragment;
    protected ArrayList<String> mListTitle;

    public TabAdapterFragment(FragmentManager fm, Product product) {
        super(fm);
        this.mProduct = product;
        mListFragment = new ArrayList<SimiFragment>();
        mListTitle = new ArrayList<String>();
        addFragment();
        addTitle();
        EventTabFragment();
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mListTitle.get(position);
    }

    private void addFragment() {
        HashMap<String, Object> hmData = new HashMap<>();
        hmData.put(Constants.KeyData.PRODUCT, mProduct);
        BasicInforFragment fragment_basic = BasicInforFragment.newInstance(new SimiData(hmData));
//		fragment_basic.setProduct(mProduct);

        mListFragment.add(fragment_basic);

        if (Utils.validateString(mProduct.getDecripition())) {
            DescriptionFragment fragment_description = DescriptionFragment
                    .newInstance(new SimiData(hmData));
//			fragment_description.setDescription(mProduct.getDecripition());
            mListFragment.add(fragment_description);
        }

        if (!mProduct.getAttributes().isEmpty()) {

            TechSpecsFragment fragment_tech = TechSpecsFragment.newInstance(new SimiData(hmData));
//			fragment_tech.setAttributes(mProduct.getAttributes());
            mListFragment.add(fragment_tech);
        }

        if (mProduct.getRate() > 0 && mProduct.getReviewNumber() > 0) {
            CustomerReviewFragment fragment_review = CustomerReviewFragment
                    .newInstance(new SimiData(hmData));
            mListFragment.add(fragment_review);
        }

        RelatedProductFragment fragment_related = RelatedProductFragment
                .newInstance(new SimiData(hmData));
//		fragment_related.setID(mProduct.getId());
        mListFragment.add(fragment_related);

    }

    private void addTitle() {
        mListTitle.add(SimiTranslator.getInstance().translate("Basic Info"));
        if (Utils.validateString(mProduct.getDecripition())) {
            mListTitle.add(SimiTranslator.getInstance().translate("Description"));
        }
        if (!mProduct.getAttributes().isEmpty()) {
            mListTitle.add(SimiTranslator.getInstance().translate("Tech Specs"));
        }
        if (mProduct.getRate() > 0 && mProduct.getReviewNumber() > 0) {
            mListTitle.add(SimiTranslator.getInstance().translate("Review"));
        }
        mListTitle.add(SimiTranslator.getInstance().translate("Related Products"));
    }

    public void EventTabFragment() {
//		EventBlock event = new EventBlock();
//		CacheBlock cacheBlock = new CacheBlock();
//		cacheBlock.setListFragment(mListFragment);
//		cacheBlock.setListName(mListTitle);
//		cacheBlock.setSimiEntity(mProduct);
//		event.dispatchEvent("com.simicart.core.adapter.TabAdapterFragment",
//				cacheBlock);
    }
}
