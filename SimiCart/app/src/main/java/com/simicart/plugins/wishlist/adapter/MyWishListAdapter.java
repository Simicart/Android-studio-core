package com.simicart.plugins.wishlist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.drawImage.SimiDrawImage;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.checkout.entity.Option;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.AppStoreConfig;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.wishlist.common.WishListConstants;
import com.simicart.plugins.wishlist.delegate.MyWishListDelegate;
import com.simicart.plugins.wishlist.entity.ItemWishList;
import com.simicart.plugins.wishlist.model.AddToCartFromWishListModel;
import com.simicart.plugins.wishlist.model.RemoveWishListModel;

import java.util.ArrayList;

/**
 * Created by Martial on 8/27/2016.
 */
public class MyWishListAdapter extends RecyclerView.Adapter<MyWishListAdapter.MyWishListHolder> {

    protected ArrayList<ItemWishList> mWishLists;
    protected Context mContext;
    protected MyWishListDelegate mDelegate;
    protected SimiModel mModel;

    public MyWishListAdapter(ArrayList<ItemWishList> mWishLists,
                             MyWishListDelegate mDelegate) {
        this.mWishLists = mWishLists;
        this.mDelegate = mDelegate;
    }

    @Override
    public MyWishListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(Rconfig.getInstance().layout("plugins_wishlist_layout_item_mywishlist"), null, false);
        MyWishListHolder holder = new MyWishListHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyWishListHolder holder, final int position) {
        final ItemWishList itemWishList = mWishLists.get(position);

        String urlImage = itemWishList.getProduct_image();
        if (null != urlImage) {
            SimiDrawImage drawImage = new SimiDrawImage();
            drawImage.drawImage(holder.img_avartar, urlImage);
        }

        String name = itemWishList.getProduct_name();
        if (null != name) {
            holder.tv_name.setText(name);
        }

        // set options
        showOption(holder, itemWishList);

        // set price
        holder.tv_price1.setText(AppStoreConfig.getInstance().getPrice(
                Float.toString(itemWishList.getProduct_price())));
        holder.tv_price1.setTextColor(AppColorConfig.getInstance().getPriceColor());
        holder.tv_price2.setVisibility(View.GONE);

        String stock = itemWishList.getStock_status();
        if (null != stock) {
            holder.tv_stock.setText(stock);
        }
        //holder.tv_stock.setVisibility(View.GONE);

        // click add cart
        if (!itemWishList.getStock_status().equals(
                SimiTranslator.getInstance().translate("In Stock"))) {
            holder.bt_add_cart.setText(itemWishList.getStock_status());
            holder.bt_add_cart.setTextColor(Color.WHITE);
            holder.bt_add_cart.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground(Color.GRAY));
        } else {
            holder.bt_add_cart.setText(SimiTranslator.getInstance().translate("Add To Cart"));
            holder.bt_add_cart.setTextColor(AppColorConfig.getInstance().getButtonTextColor());
            holder.bt_add_cart.setSupportBackgroundTintList(AppColorConfig.getInstance().getButtonBackground());

            holder.bt_add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemWishList.isSelected_all_required_options()) {
                        String addID = itemWishList.getWishlist_item_id();
                        if (DataLocal.isTablet) {
                            String showID = null;
                            if (position != (mWishLists.size() - 1)) {
                                ItemWishList nextItem = mWishLists
                                        .get(position + 1);
                                if (null != nextItem) {
                                    showID = nextItem.getProduct_id();
                                }
                            } else {
                                ItemWishList nextItem = mWishLists.get(0);
                                if (null != nextItem) {
                                    showID = nextItem.getProduct_id();
                                }
                            }
                            controllerAddAndShowNext(addID,
                                    showID, itemWishList.getProduct_id());
                        } else {
                            controllerAddToCart(addID,
                                    itemWishList.getProduct_id(), position);
                        }

                    } else {
                        SimiNotify.getInstance().showNotify(null,
                                "The product is not selected all options requirement.", "Yes");
                    }
                }
            });
        }

        holder.tv_moreOption.setText(SimiTranslator.getInstance().translate("More options"));
        holder.ll_moreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.ll_more_show.getVisibility() == View.VISIBLE) {
                    holder.ll_more_show.setVisibility(View.GONE);
                    holder.im_arrow.setVisibility(View.GONE);
                    holder.im_more.setRotation(0);
                    notifyDataSetChanged();
                } else {
                    holder.ll_more_show.setVisibility(View.VISIBLE);
                    holder.im_arrow.setVisibility(View.VISIBLE);
                    holder.im_more.setRotation(180);
                }
            }
        });

        holder.tv_shareProduct.setText(SimiTranslator.getInstance().translate("Share"));

        holder.ll_shareProduct.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        holder.ll_shareProduct.setBackgroundColor(Color.GRAY);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        shareProduct(itemWishList.getShare_mes());
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        holder.ll_shareProduct.setBackgroundResource(Rconfig.getInstance()
                                .drawable("core_background_cart_popup"));
                        break;
                    }
                    default:
                        break;
                }
                return true;
            }
        });

        Drawable icon = mContext.getResources().getDrawable(
                Rconfig.getInstance().drawable("plugins_wishlist_ic_delete"));
        icon.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        holder.im_delete.setBackgroundDrawable(icon);

        holder.tv_deleteProduct.setText(SimiTranslator.getInstance().translate("Remove"));
        holder.ll_deleteProduct.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        holder.ll_deleteProduct.setBackgroundColor(Color.GRAY);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        AlertDialog.Builder alertboxDowload = new AlertDialog.Builder(
                                mContext);
                        alertboxDowload.setMessage(SimiTranslator.getInstance().translate(
                                "Are you sure you want to remove?"));
                        alertboxDowload.setCancelable(false);
                        alertboxDowload.setPositiveButton(SimiTranslator.getInstance()
                                        .translate("Yes"),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        onRemoveProduct(itemWishList, position);
                                    }
                                });
                        alertboxDowload.setNegativeButton(SimiTranslator.getInstance()
                                        .translate("No"),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                    }
                                });
                        alertboxDowload.show();
                    }

                    case MotionEvent.ACTION_CANCEL: {
                        holder.ll_deleteProduct.setBackgroundResource(Rconfig
                                .getInstance().drawable(
                                        "core_background_cart_popup"));
                        break;
                    }
                    default:
                        break;
                }
                return true;
            }
        });


        // click item
        holder.img_avartar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> listID = new ArrayList<String>();
                for (int i = 0; i < mWishLists.size(); i++) {
                    listID.add(mWishLists.get(i).getProduct_id());
                }
                ProductDetailParentFragment fragment = ProductDetailParentFragment
                        .newInstance(itemWishList.getProduct_id(), listID);
//				fragment.setProductID(itemWishList.getProduct_id());
//				fragment.setListIDProduct(listID);
                SimiManager.getIntance().removeDialog();
                SimiManager.getIntance().replaceFragment(fragment);
            }
        });
        // truong 07-09-2015
        holder.img_share_wishlist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shareProduct(itemWishList.getShare_mes());
            }
        });
        holder.img_remove_wishlist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertboxDowload = new AlertDialog.Builder(
                        mContext);
                alertboxDowload.setMessage(SimiTranslator.getInstance().translate(
                        "Are you sure you want to remove?"));
                alertboxDowload.setCancelable(false);
                alertboxDowload.setPositiveButton(
                        SimiTranslator.getInstance().translate("Yes"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                onRemoveProduct(itemWishList, position);
                            }
                        });
                alertboxDowload.setNegativeButton(
                        SimiTranslator.getInstance().translate("No"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                alertboxDowload.show();
            }
        });
    }

    protected void showOption(MyWishListHolder holder, ItemWishList itemWishList) {
        ArrayList<Option> options = itemWishList.getmOptions();
        if (null != options && options.size() > 0) {
            if (options.size() == 1) {
                Option option = options.get(0);
                holder.lbl_option1.setVisibility(View.VISIBLE);
                holder.tv_option1.setVisibility(View.VISIBLE);
                holder.lbl_option1.setText(option.getOption_title());
                holder.tv_option1.setText(option.getOption_value());
            } else if (options.size() == 2) {
                Option option = options.get(0);
                holder.lbl_option1.setVisibility(View.VISIBLE);
                holder.tv_option1.setVisibility(View.VISIBLE);
                holder.lbl_option1.setText(option.getOption_title());
                holder.tv_option1.setText(option.getOption_value());
                option = options.get(1);
                holder.lbl_option2.setVisibility(View.VISIBLE);
                holder.tv_option2.setVisibility(View.VISIBLE);
                holder.lbl_option2.setText(option.getOption_title());
                holder.tv_option2.setText(option.getOption_value());
            } else {
                Option option = options.get(0);
                holder.lbl_option1.setVisibility(View.VISIBLE);
                holder.tv_option1.setVisibility(View.VISIBLE);
                holder.lbl_option1.setText(option.getOption_title());
                holder.tv_option1.setText(option.getOption_value());
                option = options.get(1);
                holder.lbl_option2.setVisibility(View.VISIBLE);
                holder.tv_option2.setVisibility(View.VISIBLE);
                holder.lbl_option2.setText(option.getOption_title());
                holder.tv_option2.setText(option.getOption_value());
                holder.tv_option3.setVisibility(View.VISIBLE);
                holder.tv_option3.setText("........");
            }
        }
    }

    public void shareProduct(String sharing_mes) {
        if (sharing_mes != null && !sharing_mes.equals("")) {
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("text/plain");
            intent2.putExtra(Intent.EXTRA_TEXT, sharing_mes);
            mContext.startActivity(Intent.createChooser(intent2,
                    "Share via"));
        }
    }

    protected void onRemoveProduct(ItemWishList itemWishList, int position) {
        String deletedID = itemWishList.getWishlist_item_id();

        Log.e("AdapterMyWishList ", "onRemoveProduct : " + deletedID);

        if (DataLocal.isTablet) {
            String nextID = null;
            if (position != (mWishLists.size() - 1)) {
                ItemWishList nextItem = mWishLists.get(position + 1);
                if (null != nextItem) {
                    nextID = nextItem.getProduct_id();
                }
            } else {
                ItemWishList nextItem = mWishLists.get(0);
                if (null != nextItem) {
                    nextID = nextItem.getProduct_id();
                }
            }
            controllerRemoveAndShowNext(deletedID, nextID);
        } else {
            controllerRemoveItem(deletedID);
        }
        mWishLists.remove(position);
        notifyDataSetChanged();
    }

    public void controllerRemoveItem(String deletedID) {
        mDelegate.showDialogLoading();
        mModel = new RemoveWishListModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissDialogLoading();
                ArrayList<SimiEntity> entity = mModel.getCollection()
                        .getCollection();
                ArrayList<ItemWishList> items = new ArrayList<ItemWishList>();
                if (null != entity && entity.size() > 0) {

                    for (SimiEntity simiEntity : entity) {
                        ItemWishList itemWishList = (ItemWishList) simiEntity;
                        items.add(itemWishList);
                    }

                }
                mDelegate.updateData(items);
                mDelegate.setWishlist_qty(((RemoveWishListModel) mModel)
                        .getWishlist_qty());
                SimiNotify.getInstance().showToast(
                        "It's removed from Wishlist");
            }
        });
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissDialogLoading();
                String mes = "Error! Please try again.";
                String message = error.getMessage();
                if (message != null) {
                    mes = message;
                }
                SimiNotify.getInstance().showToast(mes);
            }
        });

        mModel.addBody(WishListConstants.WISHLIST_ITEM_ID, "" + deletedID);
        mModel.request();
    }

    public void controllerRemoveAndShowNext(String deletedID, String nextID) {
        controllerRemoveItem(deletedID);
        if (null != nextID) {
            mDelegate.showDetail(nextID);
        }
    }

    public void controllerAddToCart(String wishlist_item_id,
                                    final String product_id, final int position) {
        mDelegate.showDialogLoading();
        mModel = new AddToCartFromWishListModel();
        mModel.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                mDelegate.dismissDialogLoading();
                refreshWishlist(position);
                ArrayList<SimiEntity> entity = mModel.getCollection()
                        .getCollection();
                ArrayList<ItemWishList> items = new ArrayList<ItemWishList>();
                if (null != entity && entity.size() > 0) {

                    for (SimiEntity simiEntity : entity) {
                        ItemWishList itemWishList = (ItemWishList) simiEntity;
                        items.add(itemWishList);
                    }
                }
                int cart_qty = ((AddToCartFromWishListModel) mModel)
                        .getCartQty();

                SimiManager.getIntance().onUpdateCartQty(
                        String.valueOf(cart_qty));
                mDelegate.updateData(items);
                mDelegate
                        .setWishlist_qty(((AddToCartFromWishListModel) mModel)
                                .getWishlist_qty());
                SimiNotify.getInstance().showToast(
                        "Added to Cart and Removed from Wishlist");
            }
        });
        mModel.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                mDelegate.dismissDialogLoading();
                if (DataLocal.isTablet) {
                    mDelegate.showDetail(product_id);
                }
                String mes = "Error! Please try again.";
                String message = error.getMessage();
                if (message != null) {
                    mes = message;
                }
                SimiNotify.getInstance().showToast(mes);
            }
        });
        mModel.addBody(WishListConstants.WISHLIST_ITEM_ID, ""
                + wishlist_item_id);
        mModel.request();
    }

    public void refreshWishlist(int position) {
        mWishLists.remove(position);
        notifyDataSetChanged();
    }

    public void controllerAddAndShowNext(String addID, String showID,
                                         String product_id) {
        controllerAddToCart(addID, product_id, 0);
        if (null != showID) {
            mDelegate.showDetail(showID);
        }
    }

    @Override
    public int getItemCount() {
        return mWishLists.size();
    }

    public static class MyWishListHolder extends RecyclerView.ViewHolder {

        private TextView tv_name, tv_price1, tv_price2, lbl_option1, lbl_option2,
                tv_option1, tv_option2, tv_option3, tv_stock, tv_moreOption,
                tv_shareProduct, tv_deleteProduct;
        private ImageView img_avartar, im_arrow, im_more, img_remove_wishlist, img_share_wishlist, im_delete;
        private LinearLayout ll_more_show, ll_moreOption, ll_shareProduct, ll_deleteProduct;
        private AppCompatButton bt_add_cart;

        public MyWishListHolder(View itemView) {
            super(itemView);
            img_avartar = (ImageView) itemView.findViewById(Rconfig
                    .getInstance().id("img_item_wishlist"));
            tv_name = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("tv_name"));
            tv_price1 = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("tv_price1"));
            tv_price2 = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("tv_price2"));
            tv_price2.setVisibility(View.GONE);

            lbl_option1 = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("lbl_option1"));
            lbl_option2 = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("lbl_option2"));
            tv_option1 = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("tv_option1"));
            tv_option2 = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("tv_option2"));
            tv_option3 = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("tv_option3"));

            tv_stock = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("tv_stock"));

            bt_add_cart = (AppCompatButton) itemView.findViewById(Rconfig
                    .getInstance().id("bt_add_cart"));

            ll_more_show = (LinearLayout) itemView
                    .findViewById(Rconfig.getInstance().id("ll_more_show"));
            im_arrow = (ImageView) itemView.findViewById(Rconfig
                    .getInstance().id("im_arrow"));
            im_more = (ImageView) itemView.findViewById(Rconfig
                    .getInstance().id("im_more"));
            tv_moreOption = (TextView) itemView
                    .findViewById(Rconfig.getInstance().id("tv_moreOption"));
            ll_moreOption = (LinearLayout) itemView
                    .findViewById(Rconfig.getInstance().id("ll_moreOption"));
            tv_shareProduct = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("tv_shareProduct"));
            ll_shareProduct = (LinearLayout) itemView
                    .findViewById(Rconfig.getInstance().id("ll_shareProduct"));
            im_delete = (ImageView) itemView.findViewById(Rconfig
                    .getInstance().id("im_delete"));
            img_share_wishlist = (ImageView) itemView
                    .findViewById(Rconfig.getInstance().id("img_share_wishlist"));
            img_remove_wishlist = (ImageView) itemView
                    .findViewById(Rconfig.getInstance().id("img_remove_wishlist"));
            ll_deleteProduct = (LinearLayout) itemView
                    .findViewById(Rconfig.getInstance().id("ll_deleteProduct"));
            tv_deleteProduct = (TextView) itemView.findViewById(Rconfig
                    .getInstance().id("tv_deleteProduct"));

        }
    }
}
