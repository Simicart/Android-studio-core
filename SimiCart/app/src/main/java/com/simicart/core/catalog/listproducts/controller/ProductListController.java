package com.simicart.core.catalog.listproducts.controller;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.delegate.FilterRequestDelegate;
import com.simicart.core.catalog.category.fragment.SortFragment;
import com.simicart.core.catalog.filter.FilterEvent;
import com.simicart.core.catalog.filter.entity.FilterEntity;
import com.simicart.core.catalog.filter.entity.FilterState;
import com.simicart.core.catalog.filter.entity.ValueFilterEntity;
import com.simicart.core.catalog.listproducts.delegate.ProductListDelegate;
import com.simicart.core.catalog.listproducts.entity.TagSearch;
import com.simicart.core.catalog.listproducts.model.AllProductModel;
import com.simicart.core.catalog.listproducts.model.ConstantsSearch;
import com.simicart.core.catalog.listproducts.model.ProductListCategoryModel;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simi on 12-Apr-16.
 */
public class ProductListController extends SimiController implements
        FilterRequestDelegate {

    protected ProductListDelegate mDelegate;

    //data
    protected String mCatID = "-1";
    protected String mCatName = "";
    protected String mSortID = "None";
    protected String tagView = "";
    protected JSONObject jsonFilter;
    protected FilterEvent filterEvent = null;
    protected Map<String, String> list_param = new HashMap<>();
    protected ArrayList<String> listProductIds = new ArrayList<>();
    protected ArrayList<Product> listProduct = new ArrayList<>();

    //more
    protected int limit = 8;
    protected String resultNumber;
    protected int mCurrentOffset = 0;
    protected boolean isOnscroll = true;
    protected boolean is_back_filter = false;

    //action
    protected RecyclerView.OnScrollListener mScrollListProductListener;
    protected View.OnTouchListener mOnTouchChangeViewData, mOnTouchToFilter,
            mOnTouchToSort;
    protected RecyclerView.OnTouchListener mOnTouchGridview;

    public void setCatID(String mCatID) {
        this.mCatID = mCatID;
    }

    public void setDelegate(ProductListDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    public void setCatName(String mCatName) {
        this.mCatName = mCatName;
    }

    public void setSortID(String mSortID) {
        this.mSortID = mSortID;
    }

    public void setTagView(String tagView) {
        this.tagView = tagView;
    }

    public void setJsonFilter(JSONObject jsonFilter) {
        this.jsonFilter = jsonFilter;
    }

    public void setList_param(Map<String, String> list_param) {
        this.list_param = list_param;
    }

    public View.OnTouchListener getmOnTouchChangeViewData() {
        return mOnTouchChangeViewData;
    }

    public View.OnTouchListener getmOnTouchToFilter() {
        return mOnTouchToFilter;
    }

    public View.OnTouchListener getmOnTouchToSort() {
        return mOnTouchToSort;
    }

    public RecyclerView.OnScrollListener getOnScrolListProduct() {
        return mScrollListProductListener;
    }

    public RecyclerView.OnTouchListener getmOnTouchGridview() {
        return mOnTouchGridview;
    }

    @Override
    public void onStart() {
        createActionListProduct();

        createActionBottonMenu();

        requestProducts();
    }

    private void createActionListProduct() {
        mScrollListProductListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int threshold = 1;
                int count = recyclerView.getChildCount();
                if (recyclerView.getChildAt(count - 1) != null && (recyclerView.getChildAt(count - 1).getVisibility() == View.VISIBLE)
                        && Integer.parseInt(resultNumber) > (count - threshold)) {
                    if (isOnscroll) {
                        mCurrentOffset += limit;
                        isOnscroll = false;
                        mDelegate.addFooterView();
                        requestProducts();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    mDelegate.setVisibilityMenuBotton(false);
                } else {
                    // Scrolling down
                    mDelegate.setVisibilityMenuBotton(true);
                }
            }
        };

        mOnTouchGridview = new RecyclerView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Handle touch events here...
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        Log.d(TAG, "mode=DRAG");
                        mode = DRAG;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        Log.d(TAG, "oldDist=" + oldDist);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                            Log.d(TAG, "mode=ZOOM");
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        Log.d(TAG, "mode=NONE");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            // ...
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - start.x, event.getY()
                                    - start.y);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            Log.d(TAG, "newDist=" + newDist);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = newDist / oldDist;
                                matrix.postScale(scale, scale, mid.x, mid.y);
                                // setting the scaling of the
                                // matrix...if scale > 1 means
                                // zoom in...if scale < 1 means
                                // zoom out
                                if (scale > 1) {
                                    mDelegate.onZoomIn();
                                    return true;
                                } else {
                                    mDelegate.onZoomOut();
                                    return true;
                                }
                            }

                        }
                        break;
                }

                return false;
            }
        };
    }

    private static final String TAG = "Touch";
    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    /**
     * Determine the space between the first two fingers
     */

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private void selectemItemGrid(int position) {
        if (position >= 0) {
            String productId = listProduct.get(position).getData("product_id");
            if (productId != null) {
                ProductDetailParentFragment fragment = ProductDetailParentFragment
                        .newInstance(productId, listProductIds);
                SimiManager.getIntance().replaceFragment(fragment);
            }
        }
        SimiManager.getIntance().hideKeyboard();
    }

    private void createActionBottonMenu() {
        mOnTouchChangeViewData = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (tagView == TagSearch.TAG_GRIDVIEW) {
                            tagView = TagSearch.TAG_LISTVIEW;
                        } else {
                            tagView = TagSearch.TAG_GRIDVIEW;
                        }
                        mDelegate.setTagView(tagView);
                        mDelegate.changeDataView();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        };

        mOnTouchToFilter = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        };

        mOnTouchToSort = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        toSortLayout("");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }
        };
    }

    private void toSortLayout(String query) {
        String param_url = "";
        if (!getValueListParam(ConstantsSearch.PARAM_URL).equals("")) {
            param_url = getValueListParam(ConstantsSearch.PARAM_URL);
        }

        String param_key = getValueListParam(ConstantsSearch.PARAM_KEY);
        if (getValueListParam(ConstantsSearch.PARAM_KEY) != null
                && !getValueListParam(ConstantsSearch.PARAM_KEY).equals("")) {
            param_key = getValueListParam(ConstantsSearch.PARAM_KEY);
        }

        SortFragment fragment = SortFragment.newInstance(param_url, mCatID, mCatName,
                tagView, jsonFilter, param_key, query, mSortID);
        SimiManager.getIntance().replacePopupFragment(fragment);
    }

    protected void requestProducts() {
        //limit offset
        if (mCurrentOffset == 0) {
            mDelegate.showLoading();
        }
        if (DataLocal.isTablet) {
            limit = 16;
        } else {
            limit = 8;
        }

        if (mModel == null) {
            if (mCatID.equals("-1")) {
                mModel = new AllProductModel();
            } else {
                mModel = new ProductListCategoryModel();
            }
        }

        String param_categoryid = getValueListParam(ConstantsSearch.PARAM_CATEGORY_ID);
        if (param_categoryid != null && !param_categoryid.equals("")
                && !param_categoryid.equals("-1")) {
            mModel.addParam(ConstantsSearch.PARAM_CATEGORY_ID, param_categoryid);
        }
        String param_offset = getValueListParam(ConstantsSearch.PARAM_OFFSET);
        if (param_offset != null && !param_offset.equals("")) {
            mModel.addParam(ConstantsSearch.PARAM_OFFSET,
                    String.valueOf(param_offset));
        } else {
            mModel.addParam(ConstantsSearch.PARAM_OFFSET,
                    String.valueOf(mCurrentOffset));
        }
        String param_limit = getValueListParam(ConstantsSearch.PARAM_LIMIT);
        if (param_limit != null && !param_limit.equals("")) {
            mModel.addParam(ConstantsSearch.PARAM_LIMIT,
                    String.valueOf(param_limit));
        } else {
            mModel.addParam(ConstantsSearch.PARAM_LIMIT, String.valueOf(limit));
        }
        String param_sort_option = getValueListParam(ConstantsSearch.PARAM_SORT_OPTION);
        if (param_sort_option != null && !param_sort_option.equals("")) {
            mModel.addParam(ConstantsSearch.PARAM_SORT_OPTION,
                    param_sort_option);
        } else {
            mModel.addParam(ConstantsSearch.PARAM_SORT_OPTION, mSortID);
        }
        mModel.addParam(ConstantsSearch.PARAM_WIDTH, "300");
        mModel.addParam(ConstantsSearch.PARAM_HEIGHT, "300");
        if (null != jsonFilter) {
            mModel.addParam("filter", jsonFilter);
        } else {
            mModel.addParam("filter", "");
        }
        mModel.setDelegate(new ModelDelegate() {

            @Override
            public void callBack(String message, boolean isSuccess) {
                mDelegate.dismissLoading();
                mDelegate.removeFooterView();
                if (isSuccess) {
                    resultNumber = message;
                    mDelegate.setQty(resultNumber);
                    listProduct = ((ProductListCategoryModel) mModel).getListProduct();
                    listProductIds = ((ProductListCategoryModel) mModel).getListProductIds();
                    mDelegate.setListProductIds(listProductIds);
                    mDelegate.updateView(mModel.getCollection());
                    if ((mCurrentOffset + limit) <= Integer.parseInt(resultNumber))
                        isOnscroll = true;
                }
            }
        });
        mModel.request();
    }

    protected String getValueListParam(String key) {
        if (list_param.containsKey(key)) {
            return list_param.get(key);
        }
        return "";
    }

    @Override
    public void onResume() {
        if (is_back_filter) {
            is_back_filter = false;
            mDelegate.setTagView(tagView);
            mModel.getCollection().getCollection().clear();
            requestProducts();
        } else {
            listProduct = ((ProductListCategoryModel) mModel).getListProduct();
            listProductIds = ((ProductListCategoryModel) mModel).getListProductIds();
            mDelegate.setListProductIds(listProductIds);
            mDelegate.setTagView(tagView);
            mDelegate.updateView(mModel.getCollection());
            if (mModel.getCollection().getCollection().size() > 0) {
                mDelegate.setQty(resultNumber.trim());
            }
            mDelegate.setVisibilityMenuBotton(true);
        }
    }

    @Override
    public void requestFilter(FilterEntity filterEntity) {
        if (null != filterEntity) {
            if (null == jsonFilter) {
                jsonFilter = new JSONObject();
            }
            String attribute = filterEntity.getmAttribute();
            ArrayList<ValueFilterEntity> valueEntity = filterEntity
                    .getmValueFilters();
            if (null != valueEntity && valueEntity.size() > 0) {
                for (int i = 0; i < valueEntity.size(); i++) {
                    ValueFilterEntity entity = valueEntity.get(i);
                    if (entity.isSelected()) {
                        String value = entity.getmValue();
                        if (Utils.validateString(attribute)
                                && Utils.validateString(value)) {
                            try {
                                jsonFilter.put(attribute, value);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
                // request
                is_back_filter = true;

            }
        }
    }

    @Override
    public void clearFilter(FilterState filter) {
        if (null != filter && null != jsonFilter) {
            String attribute = filter.getAttribute();
            if (jsonFilter.has(attribute)) {
                jsonFilter.remove(attribute);
            }
            is_back_filter = true;
        }
    }

    @Override
    public void clearAllFilter() {
        is_back_filter = true;
        jsonFilter = null;
    }
}
