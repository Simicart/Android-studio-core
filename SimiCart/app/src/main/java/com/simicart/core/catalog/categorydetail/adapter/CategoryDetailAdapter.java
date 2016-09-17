package com.simicart.core.catalog.categorydetail.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.base.event.base.SimiEvent;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.entity.PriceV2;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.KeyData;
import com.simicart.core.common.KeyEvent;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ValueData;
import com.simicart.core.common.price.ProductPriceViewProductGridV03;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Martial on 8/19/2016.
 */
public class CategoryDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public String PRODUCT_PRICE_TYPE_1 = "simple_virtual";
    public String PRODUCT_PRICE_TYPE_2 = "bundle";
    public String PRODUCT_PRICE_TYPE_3 = "grouped";
    public String PRODUCT_PRICE_TYPE_4 = "configurable";
    protected ArrayList<Product> listProducts;
    protected Context mContext;
    protected String tagView = "";
    protected int numCollums = 0;

    public CategoryDetailAdapter(ArrayList<Product> listProducts) {
        this.listProducts = listProducts;
    }

    public void setNumCollums(int numCollums) {
        this.numCollums = numCollums;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = null;
        RecyclerView.ViewHolder holder = null;
        if (tagView.equals(Constants.TAG_GRIDVIEW)) {
            itemView = inflater.inflate(Rconfig.getInstance().layout("core_adapter_grid_products"), null, false);
            holder = new GridProductHolder(itemView);
        } else {
            itemView = inflater.inflate(Rconfig.getInstance().layout("core_adapter_list_products"), null, false);
            holder = new ListProductHolder(itemView);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Product product = listProducts.get(position);
        if (tagView.equals(Constants.TAG_GRIDVIEW)) {
            createItemGridView((GridProductHolder) holder, product);
            dispatchEventForProductLabel(((GridProductHolder) holder).rl_product_list, product, ValueData.PRODUCT_LABEL.GRID);
        } else {
            createItemListView((ListProductHolder) holder, product);
            dispatchEventForProductLabel(((ListProductHolder) holder).rl_product_list, product, ValueData.PRODUCT_LABEL.LIST);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public void setTagView(String tagView) {
        this.tagView = tagView;
    }

    public void setListProducts(ArrayList<Product> listProducts) {
        this.listProducts = listProducts;
    }

    protected void createItemListView(ListProductHolder holder, final Product product) {

        holder.tv_special_price.setVisibility(View.VISIBLE);
        holder.tv_minimal_price.setVisibility(View.VISIBLE);
        holder.tv_regular_price.setPaintFlags(holder.tv_regular_price
                .getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        holder.tv_regular_price.setVisibility(View.VISIBLE);

        String mTypeProduct = PRODUCT_PRICE_TYPE_1;
        if (product != null) {
            mTypeProduct = product.getType();
        }
        if (null != mTypeProduct) {
            mTypeProduct = mTypeProduct.toLowerCase();
        }

        if (mTypeProduct == null) {
            mTypeProduct = "simple";
        }
        if (mTypeProduct.equals("simple") || mTypeProduct.equals("virtual")
                || mTypeProduct.equals("downloadable")
                || mTypeProduct.equals(PRODUCT_PRICE_TYPE_1)) {
            getViewPriceType1(holder, product);
        } else if (mTypeProduct.equals("bundle")
                || mTypeProduct.equals(PRODUCT_PRICE_TYPE_2)) {
            getViewPriceType2(holder, product);

        } else if (mTypeProduct.equals("grouped")
                || mTypeProduct.equals(PRODUCT_PRICE_TYPE_3)) {
            getViewPriceType3(holder, product);
        } else if (mTypeProduct.equals("configurable")
                || mTypeProduct.equals(PRODUCT_PRICE_TYPE_4)) {
            getViewPriceType1(holder, product);
        }

        holder.txtName.setText(product.getName());

        Log.e("CategoryDetailAdapter ", "+++++ LIST NAME " + product.getName());

        if (holder.imageView != null && product.getImage() != null) {
            SimiDrawImage drawImage = new SimiDrawImage();
            drawImage.drawImage(holder.imageView, product.getImage());
        }

        if (product.getStock() == true) {
            holder.txt_outstock.setVisibility(View.GONE);
        } else {
            holder.txt_outstock.setVisibility(View.VISIBLE);
            holder.txt_outstock.setText(SimiTranslator.getInstance().translate(
                    "Out Stock"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = product.getId();
                ArrayList<String> listID = new ArrayList<String>();
                for (int i = 0; i < listProducts.size(); i++) {
                    listID.add(listProducts.get(i).getId());
                }
                HashMap<String, Object> hmData = new HashMap<>();
                hmData.put(KeyData.PRODUCT_DETAIL.PRODUCT_ID, id);
                hmData.put(KeyData.PRODUCT_DETAIL.LIST_PRODUCT_ID, listID);
                SimiManager.getIntance().openProductDetail(hmData);
            }
        });
    }

    protected void createItemGridView(GridProductHolder holder, final Product product) {
        if (product.getStock() == true) {
            holder.txt_outstock.setVisibility(View.GONE);
        } else {
            holder.txt_outstock.setVisibility(View.VISIBLE);
            holder.txt_outstock.setText(SimiTranslator.getInstance().translate(
                    "Out Stock"));
        }

        ProductPriceViewProductGridV03 price_view = new ProductPriceViewProductGridV03(
                product);
        price_view.setShowOnePrice(false);
        View view = price_view.getViewPrice();
        if (null != view && null != holder.ll_price) {
            holder.ll_price.removeAllViewsInLayout();
            holder.ll_price.addView(view);
        }

        String name = product.getName();
        if (null != name) {
            name.trim();
            holder.tv_name.setText(name);
        } else {
            holder.tv_name.setText("SimiCart");
        }


        if (product.getImage() != null) {
            SimiDrawImage drawImage = new SimiDrawImage();
            drawImage.drawImage(holder.img_avartar, product.getImage());
        }

        int screenWidthPx = Utils.getScreenWidth();
        int itemSize;
        LinearLayout.LayoutParams params = null;
        if (numCollums == 2) {
            itemSize = (int) ((screenWidthPx - 22) / 2);
            params = new LinearLayout.LayoutParams(itemSize, itemSize);
        } else if (numCollums == 4) {
            itemSize = (int) ((screenWidthPx - 20) / 4);
            params = new LinearLayout.LayoutParams(itemSize, itemSize);
        }
        holder.rl_product_list.setLayoutParams(params);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = product.getId();
                ArrayList<String> listID = new ArrayList<String>();
                for (int i = 0; i < listProducts.size(); i++) {
                    listID.add(listProducts.get(i).getId());
                }
                HashMap<String, Object> hmData = new HashMap<>();
                hmData.put(KeyData.PRODUCT_DETAIL.PRODUCT_ID, id);
                hmData.put(KeyData.PRODUCT_DETAIL.LIST_PRODUCT_ID, listID);
                SimiManager.getIntance().openProductDetail(hmData);
            }
        });

    }

    protected void dispatchEventForProductLabel(View view, Product product, String method) {
        if (product.getJSONObject().has("product_label")) {
            try {
                JSONArray array = product.getJSONObject().getJSONArray("product_label");
                HashMap<String, Object> hmData = new HashMap<>();
                hmData.put(KeyData.PRODUCT_LABEL.PRODUCT_LABEL_VIEW, view);
                hmData.put(KeyData.PRODUCT_LABEL.PRODUCT_LABEL_JSON, array);
                hmData.put(KeyData.PRODUCT_LABEL.PRODUCT_LABEL_METHOD, method);
                SimiEvent.dispatchEvent(KeyEvent.PRODUCT_LABEL.PRODUCT_LABEL_ADD_ITEM, hmData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getViewPriceType1(ListProductHolder holder, Product mProduct) {
        String regular_price = "";
        String special_price = "";
        String minimal_price = "";

        PriceV2 priceV2 = mProduct.getPriceV2();

        // step 1 : display regular price
        float excl_tax = priceV2.getExclTax();
        float incl_tax = priceV2.getInclTax();
        float product_regualar_price = priceV2.getRegularPrice();
        if (excl_tax > -1 && incl_tax > -1) {
            regular_price = getHtmlForPrice(incl_tax);
        } else {
            if (product_regualar_price > -1) {
                regular_price = getHtmlForPrice(product_regualar_price);
            }
        }

        // step 2 : display special price
        float excl_tax_special = priceV2.getExclTaxSpecial();
        float incl_tax_special = priceV2.getInclTaxSpecial();
        float price = priceV2.getPrice();
        if (excl_tax_special > -1 && incl_tax_special > -1) {
            special_price = getHtmlForSpecialPrice(incl_tax_special);
        } else {
            if (price > -1) {
                special_price = getHtmlForSpecialPrice(price);
            }
        }

        // step 3 : display minimal price
        String mimimal_price_label = priceV2.getMinimalPriceLabel();
        float minimalPrice = priceV2.getMinimalPrice();
        if (minimalPrice > -1 && Utils.validateString(mimimal_price_label)) {
            minimal_price = getHtmlForPrice(minimalPrice, mimimal_price_label);
            holder.tv_minimal_price.setText(Html.fromHtml(minimal_price));
        } else {
            holder.tv_minimal_price.setVisibility(View.GONE);
        }

        if (Utils.validateString(special_price)) {
            holder.tv_regular_price.setPaintFlags(holder.tv_regular_price
                    .getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_special_price.setText(Html.fromHtml(special_price));
        } else {
            holder.tv_special_price.setVisibility(View.GONE);
        }

        if (Utils.validateString(regular_price)) {
            holder.tv_regular_price.setText(Html.fromHtml(regular_price));
        }
    }

    public void getViewPriceType2(ListProductHolder holder, Product mProduct) {
        PriceV2 priceV2 = mProduct.getPriceV2();

        String minimal_label = priceV2.getMinimalPriceLabel();
        if (Utils.validateString(minimal_label)) {
            // JSON of product has 'minimal_price_label' tag
            holder.tv_special_price.setVisibility(View.GONE);
            holder.tv_minimal_price.setVisibility(View.GONE);
            float excl_tax_minimal = priceV2.getExclTaxMinimal();
            float incl_tax_minimal = priceV2.getInclTaxMinimal();
            if (incl_tax_minimal > -1) {
                holder.tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
                        incl_tax_minimal, minimal_label)));
            } else {
                holder.tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
                        excl_tax_minimal, minimal_label)));
            }

        } else {

            holder.tv_special_price.setVisibility(View.GONE);
            // JSON of product doesn't has 'minimal_price_label' tag
            float incl_tax_from = priceV2.getInclTaxFrom();
            float incl_tax_to = priceV2.getInclTaxTo();
            float excl_tax_from = priceV2.getExclTaxFrom();
            float excl_tax_to = priceV2.getExclTaxTo();
            String from_text = SimiTranslator.getInstance().translate("From");
            String to_text = SimiTranslator.getInstance().translate("To");
            if (incl_tax_from > -1) {
                holder.tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
                        incl_tax_from, from_text)));
                holder.tv_minimal_price.setText(Html.fromHtml(getHtmlForPrice(
                        incl_tax_to, to_text)));
            } else {
                if (excl_tax_from > -1) {
                    holder.tv_regular_price
                            .setText(Html.fromHtml(getHtmlForPrice(
                                    excl_tax_from, from_text)));
                    holder.tv_minimal_price.setText(Html
                            .fromHtml(getHtmlForPrice(excl_tax_to, to_text)));
                }
            }
        }
    }

    public void getViewPriceType3(ListProductHolder holder, Product mProduct) {
        holder.tv_special_price.setVisibility(View.GONE);
        holder.tv_minimal_price.setVisibility(View.GONE);

        PriceV2 priceV2 = mProduct.getPriceV2();
        if (priceV2 != null) {
            float incl_tax_minimal = priceV2.getInclTaxMinimal();
            float minimal_price = priceV2.getMinimalPrice();
            String minimal_price_label = priceV2.getMinimalPriceLabel();

            if (incl_tax_minimal > -1) {
                holder.tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
                        incl_tax_minimal, minimal_price_label)));
            } else {
                if (minimal_price > -1) {
                    holder.tv_regular_price.setText(Html
                            .fromHtml(getHtmlForPrice(minimal_price,
                                    minimal_price_label)));
                }
            }
        }
    }

    protected String getHtmlForPrice(float price) {
        return "<font color='" + AppColorConfig.getInstance().getPriceColor() + "'>"
                + AppStoreConfig.getInstance().getPrice("" + price) + "</font>";
    }

    protected String getHtmlForPrice(float price, String label) {
        return "<font color='" + AppColorConfig.getInstance().getPriceColor() + "'>"
                + label + ": </font><font color='"
                + AppColorConfig.getInstance().getPriceColor() + "'>"
                + AppStoreConfig.getInstance().getPrice("" + price) + "</font>";
    }

    protected String getHtmlForSpecialPrice(float price) {
        return "<font color='" + AppColorConfig.getInstance().getSpecialPriceColor()
                + "'>" + AppStoreConfig.getInstance().getPrice("" + price) + "</font>";
    }

    public class ListProductHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtName;
        TextView txt_outstock;
        TextView tv_regular_price;
        TextView tv_special_price;
        TextView tv_minimal_price;
        RelativeLayout rl_product_list;

        public ListProductHolder(View v) {
            super(v);
            txtName = (TextView) v.findViewById(Rconfig
                    .getInstance().id("tv_productItemName"));
            txtName
                    .setTextColor(AppColorConfig.getInstance().getContentColor());
            imageView = (ImageView) v.findViewById(Rconfig
                    .getInstance().id("iv_productItemImage"));

            ImageView ic_expand = (ImageView) v.findViewById(Rconfig
                    .getInstance().id("ic_expand"));
            Drawable icon = mContext.getResources().getDrawable(
                    Rconfig.getInstance().drawable("ic_extend"));
            icon.setColorFilter(AppColorConfig.getInstance().getContentColor(),
                    PorterDuff.Mode.SRC_ATOP);
            ic_expand.setImageDrawable(icon);

            tv_regular_price = (TextView) v
                    .findViewById(Rconfig.getInstance().id("tv_regular"));
            tv_special_price = (TextView) v
                    .findViewById(Rconfig.getInstance().id("tv_special"));
            tv_minimal_price = (TextView) v
                    .findViewById(Rconfig.getInstance().id("tv_minimal"));
            txt_outstock = (TextView) v.findViewById(Rconfig
                    .getInstance().id("txt_out_stock"));
            txt_outstock.setTextColor(AppColorConfig.getInstance()
                    .getOutStockTextColor());
            txt_outstock.setBackgroundColor(AppColorConfig.getInstance()
                    .getOutStockBackgroundColor());

            rl_product_list = (RelativeLayout) v
                    .findViewById(Rconfig.getInstance().id("rel_product_list"));

            if (AppStoreConfig.getInstance().isRTL()) {
                txtName.setGravity(Gravity.RIGHT);
            }
        }
    }

    public class GridProductHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView tv_name;
        private ImageView img_avartar;
        private LinearLayout ll_price;
        private TextView txt_outstock;
        private RelativeLayout rl_product_list;

        public GridProductHolder(View v) {
            super(v);
            tv_name = (TextView) v.findViewById(Rconfig
                    .getInstance().id("tv_name"));
            tv_name
                    .setTextColor(AppColorConfig.getInstance().getContentColor());
            ll_price = (LinearLayout) v.findViewById(Rconfig
                    .getInstance().id("ll_price"));
            img_avartar = (ImageView) v.findViewById(Rconfig
                    .getInstance().id("img_avartar"));

            txt_outstock = (TextView) v.findViewById(Rconfig
                    .getInstance().id("txt_out_stock"));
            txt_outstock.setTextColor(AppColorConfig.getInstance()
                    .getOutStockTextColor());
            txt_outstock.setBackgroundColor(AppColorConfig.getInstance()
                    .getOutStockBackgroundColor());

            rl_product_list = (RelativeLayout) v
                    .findViewById(Rconfig.getInstance().id("rel_product_list"));
            int withScreen = Utils.getScreenWidth();
            LinearLayout.LayoutParams params2Collums = new LinearLayout.LayoutParams(
                    ((withScreen - 22) / 2),
                    (withScreen - 22) / 2);
            LinearLayout.LayoutParams params4Collums = new LinearLayout.LayoutParams(
                    Utils.toDp((int) ((withScreen - 20) / 4)),
                    Utils.toDp((int) ((withScreen - 20) / 4)));
            if (DataLocal.isTablet) {
                rl_product_list.setLayoutParams(params4Collums);
            } else {
                rl_product_list.setLayoutParams(params2Collums);
            }
        }
    }

}
