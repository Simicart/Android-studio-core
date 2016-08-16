package com.simicart.core.catalog.listproducts.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.simicart.R;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.entity.PriceV2;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

import java.util.ArrayList;

/**
 * Created by Simi on 12-Apr-16.
 */
public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private int footerCount = 0;

    public String PRODUCT_PRICE_TYPE_1 = "simple_virtual";
    public String PRODUCT_PRICE_TYPE_2 = "bundle";
    public String PRODUCT_PRICE_TYPE_3 = "grouped";
    public String PRODUCT_PRICE_TYPE_4 = "configurable";

    private Context mContext;
    private ArrayList<Product> listProducts;
    private ArrayList<String> listProductIds;

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {

        public ViewHolderHeader(View itemView) {
            super(itemView);
        }
    }

    public static class ViewHolderFooter extends RecyclerView.ViewHolder {

        public ViewHolderFooter(View itemView) {
            super(itemView);
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolderItem extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView imageView;
        TextView txtName;
        LinearLayout layoutStock;
        TextView txt_outstock;
        //        LinearLayout ll_price;
        TextView tv_regular_price;
        TextView tv_special_price;
        TextView tv_minimal_price;
        RelativeLayout rl_product_list;

        public ViewHolderItem(View v) {
            super(v);
            txtName = (TextView) v.findViewById(Rconfig
                    .getInstance().id("tv_productItemName"));
            txtName
                    .setTextColor(AppColorConfig.getInstance().getContentColor());
            imageView = (ImageView) v.findViewById(Rconfig
                    .getInstance().id("iv_productItemImage"));
            layoutStock = (LinearLayout) v
                    .findViewById(Rconfig.getInstance().id("layout_stock"));
            layoutStock.setBackgroundColor(AppColorConfig.getInstance()
                    .getOutStockBackgroundColor());
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

            rl_product_list = (RelativeLayout) v
                    .findViewById(Rconfig.getInstance().id("rel_product_list"));

            if (DataLocal.isLanguageRTL) {
                txtName.setGravity(Gravity.RIGHT);
            }
        }

    }

    public void setListProducts(ArrayList<Product> listProducts) {
        this.listProducts = listProducts;
    }

    public void setListProductIds(ArrayList<String> listProductIds) {
        this.listProductIds = listProductIds;
    }

    public ProductListAdapter(Context context, ArrayList<Product> list_products, ArrayList<String> list_product_IDs) {
        listProducts = list_products;
        mContext = context;
        listProductIds = list_product_IDs;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        if (viewType == TYPE_HEADER) {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.core_base_loading, parent, false);
            View v = new View(parent.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.getValueDp(30));
            v.setLayoutParams(lp);
            return new ViewHolderHeader(v);
        } else if (viewType == TYPE_ITEM) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(Rconfig.getInstance().layout(
                    "core_item_list_search"), parent, false);
            // set the view's size, margins, paddings and layout parameters
            ViewHolderItem vh = new ViewHolderItem(v);
            return vh;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.core_base_loading, parent, false);
            return new ViewHolderFooter(v);
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderHeader) {

        } else if (holder instanceof ViewHolderFooter) {

        } else {
            final Product product = getItem(position - 1);

            ViewHolderItem vhItem = (ViewHolderItem) holder;

            vhItem.tv_special_price.setVisibility(View.VISIBLE);
            vhItem.tv_minimal_price.setVisibility(View.VISIBLE);
            vhItem.tv_regular_price.setPaintFlags(vhItem.tv_regular_price
                    .getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            vhItem.tv_regular_price.setVisibility(View.VISIBLE);

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
                getViewPriceType1(vhItem, product);
            } else if (mTypeProduct.equals("bundle")
                    || mTypeProduct.equals(PRODUCT_PRICE_TYPE_2)) {
                getViewPriceType2(vhItem, product);

            } else if (mTypeProduct.equals("grouped")
                    || mTypeProduct.equals(PRODUCT_PRICE_TYPE_3)) {
                getViewPriceType3(vhItem, product);
            } else if (mTypeProduct.equals("configurable")
                    || mTypeProduct.equals(PRODUCT_PRICE_TYPE_4)) {
                getViewPriceType1(vhItem, product);
            }

            vhItem.txtName.setText(product.getName());

            if (vhItem.imageView != null && product.getImage() != null) {
                DrawableManager.fetchDrawableOnThread(product.getImage(),
                        vhItem.imageView);
            }

            if (product.getStock() == true) {
                vhItem.layoutStock.setVisibility(View.GONE);
            } else {
                vhItem.layoutStock.setVisibility(View.VISIBLE);
                vhItem.txt_outstock.setText(SimiTranslator.getInstance().translate(
                        "Out Stock"));
            }

            vhItem.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String productId = product.getData("product_id");
                    if (productId != null) {
                        ProductDetailParentFragment fragment = ProductDetailParentFragment
                                .newInstance(productId, listProductIds);
                        SimiManager.getIntance().replaceFragment(fragment);
                    }
                    SimiManager.getIntance().hideKeyboard();
                }

            });

//            EventBlock eventBlock = new EventBlock();
//            eventBlock.dispatchEvent("com.simicart.image.product.list",
//                    vhItem.rl_product_list, product);
        }

    }

    public void addFooterView() {
        footerCount = 1;
        notifyItemInserted(getItemCount() - footerCount);
    }

    public void removeFooterView() {
        footerCount = 0;
        notifyItemRemoved(getItemCount() - footerCount);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (listProducts != null) {
            size = 1 + listProducts.size() + footerCount;
        }
        return size;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isPositionFooter(int position) {
        return position >= getItemCount() - footerCount;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public Product getItem(int position) {
        Product product = new Product();
        if (listProducts != null) {
            product = listProducts.get(position);
        }
        return product;
    }

    public void getViewPriceType1(ViewHolderItem holder, Product mProduct) {
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

    public void getViewPriceType2(ViewHolderItem holder, Product mProduct) {
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

    public void getViewPriceType3(ViewHolderItem holder, Product mProduct) {
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


}
