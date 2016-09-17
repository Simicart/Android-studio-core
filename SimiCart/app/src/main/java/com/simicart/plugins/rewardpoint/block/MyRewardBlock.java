package com.simicart.plugins.rewardpoint.block;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.common.DataPreferences;
import com.simicart.core.common.Utils;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.delegate.MyRewardDelegate;
import com.simicart.plugins.rewardpoint.entity.ItemPointData;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Martial on 8/30/2016.
 */
public class MyRewardBlock extends SimiBlock implements MyRewardDelegate {

    int spending_mint = 1;
    String passBookLogo = "";
    String loyalty_redeem = "";
    String passbook_text = "";
    String background_passbook = "";
    String passbook_foreground = "";
    String passbook_barcode = "";
    int is_notification;
    int expire_notification;
    private RelativeLayout rlt_layout_card;
    private RelativeLayout rlt_layout_history;
    private RelativeLayout rlt_layout_setting;
    private Button btn_signin;
    private TextView txt_card;
    private TextView txt_history;
    private TextView txt_setting;
    private TextView txt_ours_policies;
    private LinearLayout layout_point;
    private ProgressBar progress_point;
    private TextView txt_maxpoint;
    private TextView txt_availble_point;
    private TextView txt_number_point;
    private TextView txt_number_redeem;
    private TextView txt_changemoney;
    private TextView txt_earnpoint;
    private TextView txt_earnpoint_content;
    private TextView txt_spendpoint;
    private TextView txt_spendpoint_content;
    private TextView txt_coint;
    private LinearLayout layout_policy_content;
    private RelativeLayout layout_policy;
    private RelativeLayout rlt_earpoint_text;
    private RelativeLayout rlt_spendpoint_text;
    private RelativeLayout rlt_ours_policies;
    private LinearLayout linearLayout_top;
    private LinearLayout linearLayout_center;
    private LinearLayout linearLayout_bottom;

    public MyRewardBlock(View view, Context context) {
        super(view, context);
    }

    @Override
    public void initView() {
        rlt_layout_card = (RelativeLayout) mView.findViewById(Rconfig
                .getInstance().id("layout_card"));
        rlt_layout_history = (RelativeLayout) mView.findViewById(Rconfig
                .getInstance().id("layout_history"));
        rlt_layout_setting = (RelativeLayout) mView.findViewById(Rconfig
                .getInstance().id("layout_setting"));
        txt_card = (TextView) mView.findViewById(Rconfig.getInstance().id(
                "txt_card"));
        txt_history = (TextView) mView.findViewById(Rconfig.getInstance().id(
                "txt_history"));
        txt_setting = (TextView) mView.findViewById(Rconfig.getInstance().id(
                "txt_setting"));
        txt_ours_policies = (TextView) mView.findViewById(Rconfig.getInstance()
                .id("txt_ours_policies"));

        txt_card.setText(SimiTranslator.getInstance().translate("Rewards Card"));
        txt_history.setText(SimiTranslator.getInstance().translate("Rewards History"));
        txt_setting.setText(SimiTranslator.getInstance().translate("Settings"));
        txt_ours_policies.setText(SimiTranslator.getInstance().translate("Our Policies"));

        layout_point = (LinearLayout) mView.findViewById(Rconfig.getInstance()
                .id("layout_point"));
        progress_point = (ProgressBar) mView.findViewById(Rconfig.getInstance()
                .id("progressBarPoint"));
        txt_maxpoint = (TextView) mView.findViewById(Rconfig.getInstance().id(
                "txt_maxpoint"));

        txt_number_point = (TextView) mView.findViewById(Rconfig.getInstance()
                .id("txt_number_point"));
        txt_number_redeem = (TextView) mView.findViewById(Rconfig.getInstance()
                .id("txt_point_redeem"));
        txt_changemoney = (TextView) mView.findViewById(Rconfig.getInstance()
                .id("txt_changemoney"));
        txt_earnpoint = (TextView) mView.findViewById(Rconfig.getInstance().id(
                "txt_earnpoint"));
        txt_earnpoint_content = (TextView) mView.findViewById(Rconfig
                .getInstance().id("txt_earnpoint_content"));
        txt_spendpoint = (TextView) mView.findViewById(Rconfig.getInstance().id(
                "txt_spendpoint"));
        txt_spendpoint_content = (TextView) mView.findViewById(Rconfig
                .getInstance().id("txt_spendpoint_content"));
        txt_coint = (TextView) mView.findViewById(Rconfig.getInstance().id(
                "txt_coint"));
        layout_policy_content = (LinearLayout) mView.findViewById(Rconfig
                .getInstance().id("layout_ours_policies_content"));
        rlt_ours_policies = (RelativeLayout) mView.findViewById(Rconfig
                .getInstance().id("layout_ours_policies"));
        layout_policy = (RelativeLayout) mView.findViewById(Rconfig
                .getInstance().id("layout_ours_policies"));
        linearLayout_bottom = (LinearLayout) mView.findViewById(Rconfig
                .getInstance().id("layout_bottom"));

        linearLayout_top = (LinearLayout) mView.findViewById(Rconfig
                .getInstance().id("layout_top1"));
        linearLayout_center = (LinearLayout) mView.findViewById(Rconfig
                .getInstance().id("layout_center"));
        rlt_earpoint_text = (RelativeLayout) mView.findViewById(Rconfig
                .getInstance().id("layout_earnpoint"));

        rlt_spendpoint_text = (RelativeLayout) mView.findViewById(Rconfig
                .getInstance().id("layout_spendpoint"));
        txt_availble_point = (TextView) mView.findViewById(Rconfig.getInstance()
                .id("txt_avaible_point"));
        txt_availble_point.setText(SimiTranslator.getInstance().translate(
                "AVAILABLE POINTS"));

        progress_point.setMax(10);
        progress_point.setProgress(7);
        btn_signin = (Button) mView.findViewById(Rconfig.getInstance().id(
                "btn_reward_signin"));
        GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(AppColorConfig.getInstance().getKeyColor());
        gdDefault.setCornerRadius(15);
        if (!DataPreferences.isSignInComplete()) {
            // btn_signin.setVisibility(View.VISIBLE);
            btn_signin.setBackgroundDrawable(gdDefault);
            btn_signin.setText(SimiTranslator.getInstance().translate("Login or Signup"));
            LinearLayout layout_top = (LinearLayout) mView.findViewById(Rconfig
                    .getInstance().id("layout_top"));
            layout_top.setVisibility(View.GONE);
            LinearLayout layout_center = (LinearLayout) mView
                    .findViewById(Rconfig.getInstance().id("layout_center"));
            layout_center.setVisibility(View.GONE);
        } else {
            btn_signin.setVisibility(View.GONE);
        }
    }

    @Override
    public void drawView(SimiCollection collection) {
        super.drawView(collection);
    }

    @Override
    public void showView(JSONObject data) {
        if (data != null) {
            linearLayout_top.setVisibility(View.VISIBLE);
            if (!DataPreferences.isSignInComplete()) {
                linearLayout_center.setVisibility(View.GONE);
            } else {
                linearLayout_center.setVisibility(View.VISIBLE);
            }
            linearLayout_bottom.setVisibility(View.VISIBLE);
            ItemPointData itempointData = new ItemPointData()
                    .getItemPointFromJson(data);
            is_notification = itempointData.getIs_notification();
            expire_notification = itempointData.getExpire_notification();
            loyalty_redeem = itempointData.getLoyalty_redeem();
            try {
                passBookLogo = itempointData.getLoyalty_image();
                if (!Utils.validateString(itempointData.getSpending_policy())
                        || !Utils.validateString(itempointData
                        .getSpending_label())) {
                    rlt_spendpoint_text.setVisibility(View.GONE);
                    txt_spendpoint_content.setVisibility(View.GONE);
                } else {
                    rlt_spendpoint_text.setVisibility(View.VISIBLE);
                    txt_spendpoint_content.setVisibility(View.VISIBLE);
                    txt_spendpoint_content.setText(itempointData
                            .getSpending_policy());
                    txt_spendpoint.setText(itempointData.getSpending_label());
                }
                if (Utils
                        .validateString("" + itempointData.getSpending_point())
                        && Utils.validateString(""
                        + itempointData.getSpending_discount())) {
                    txt_changemoney.setVisibility(View.VISIBLE);
                    txt_changemoney.setText(itempointData.getSpending_point()
                            + " = " + itempointData.getSpending_discount());
                } else {
                    txt_changemoney.setVisibility(View.GONE);
                }

                // set content to view
                txt_number_point.setText(itempointData.getLoyalty_point() + "");
                txt_availble_point.setVisibility(View.VISIBLE);
                txt_number_redeem.setText(SimiTranslator.getInstance().translate("Equal")
                        + " " + loyalty_redeem + " "
                        + SimiTranslator.getInstance().translate("to redeem"));
                txt_coint.setText(itempointData.getSpending_discount());

                System.out.println("xx");
                String label = itempointData.getEarning_label();
                String policy = itempointData.getEarning_policy();
                if (!Utils.validateString(itempointData.getEarning_label())
                        || !Utils.validateString(itempointData
                        .getEarning_policy())) {
                    rlt_earpoint_text.setVisibility(View.GONE);
                    txt_earnpoint_content.setVisibility(View.GONE);
                } else {
                    rlt_earpoint_text.setVisibility(View.VISIBLE);
                    txt_earnpoint_content.setVisibility(View.VISIBLE);
                    txt_earnpoint.setText(itempointData.getEarning_label());
                    txt_earnpoint_content.setText(itempointData
                            .getEarning_policy());
                }

                passbook_text = itempointData.getPassbook_text();
                background_passbook = itempointData.getBackground_passbook();
                passbook_foreground = itempointData.getPassbook_foreground();
                if (passbook_foreground.equals("")) {
                    rlt_layout_card.setVisibility(View.GONE);
                }
                passbook_barcode = itempointData.getPassbook_barcode();
                JSONArray array_policies = itempointData.getArray_policies();
                if (array_policies.length() > 0) {
                    rlt_ours_policies.setVisibility(View.VISIBLE);
                    layout_policy_content.removeAllViews();
                    for (int i = 0; i < array_policies.length(); i++) {
                        LinearLayout.LayoutParams paramsTextview = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        TextView textView = new TextView(mContext);
                        textView.setPadding(Utils.toDp(10),
                                Utils.toDp(10), Utils.toDp(10),
                                Utils.toDp(10));
                        textView.setText(array_policies.getString(i));
                        textView.setTextColor(Color.parseColor("#000000"));
                        textView.setLayoutParams(paramsTextview);
                        layout_policy_content.addView(textView);
                    }

                    if (!DataPreferences.isSignInComplete()) {
                        btn_signin.setVisibility(View.VISIBLE);
                    } else {
                        btn_signin.setVisibility(View.GONE);
                    }

                } else {
                    layout_policy.setVisibility(View.GONE);
                    layout_policy_content.setVisibility(View.GONE);
                }
                if (itempointData.getLoyalty_point() > spending_mint - 1
                        || (itempointData.getLoyalty_point() == 0)) {
                    layout_point.setVisibility(View.GONE);
                    txt_number_redeem.setVisibility(View.GONE);
                } else {
                    layout_point.setVisibility(View.VISIBLE);
                    progress_point.setMax(spending_mint);
                    progress_point
                            .setProgress(itempointData.getLoyalty_point());
                    int until = spending_mint
                            - itempointData.getLoyalty_point();
                    txt_number_redeem.setText(SimiTranslator.getInstance().translate(
                            "Only")
                            + " "
                            + until
                            + " "
                            + SimiTranslator.getInstance().translate("Points until")
                            + " " + itempointData.getStart_discount());
                    txt_maxpoint.setText(spending_mint + "");
                }
            } catch (Exception e) {
                // System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public int isNotification() {
        return 0;
    }

    @Override
    public int expireNotification() {
        return expire_notification;
    }

    @Override
    public String getPassbookLogo() {
        return passBookLogo;
    }

    @Override
    public String getLoyaltyRedeem() {
        return loyalty_redeem;
    }

    @Override
    public String getPassbookText() {
        return passbook_text;
    }

    @Override
    public String getPassbookBackground() {
        return background_passbook;
    }

    @Override
    public String getPassbookForeground() {
        return passbook_foreground;
    }

    @Override
    public String getPassbookBarcode() {
        return passbook_barcode;
    }

    public void onClickHistory(View.OnClickListener listener) {
        rlt_layout_history.setOnClickListener(listener);
    }

    public void onClickSetting(View.OnClickListener listener) {
        rlt_layout_setting.setOnClickListener(listener);
    }

    public void onClickCard(View.OnClickListener listener) {
        rlt_layout_card.setOnClickListener(listener);
    }

}
