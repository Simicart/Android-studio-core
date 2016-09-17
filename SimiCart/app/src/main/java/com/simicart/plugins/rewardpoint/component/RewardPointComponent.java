package com.simicart.plugins.rewardpoint.component;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.simicart.core.base.component.SimiComponent;
import com.simicart.core.base.delegate.ModelFailCallBack;
import com.simicart.core.base.delegate.ModelSuccessCallBack;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.error.SimiError;
import com.simicart.core.base.notify.SimiNotify;
import com.simicart.core.base.translate.SimiTranslator;
import com.simicart.core.checkout.component.PaymentMethodComponent;
import com.simicart.core.checkout.component.TotalPriceComponent;
import com.simicart.core.checkout.entity.ReviewOrderEntity;
import com.simicart.core.config.AppColorConfig;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.model.ModelRewardSeerbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Martial on 8/31/2016.
 */
public class RewardPointComponent extends SimiComponent {

    protected TextView tvTitle, tvLabel, tvMin, tvSpending, tvMax;
    protected AppCompatSeekBar sbSpending;
    protected String pointStepLabel, pointStepDiscount;
    protected int maxPoints, minPoint, loyalty_spend, point_step, loy_spend;
    protected ArrayList<SimiComponent> listComponents;
    protected ProgressDialog pd_loading;

    @Override
    public View createView() {
        rootView = findLayout("plugins_rewardpoint_item_review_order");

        pd_loading = ProgressDialog.show(SimiManager.getIntance().getCurrentActivity(), null, null, true, false);
        pd_loading.setContentView(Rconfig.getInstance().layout(
                "core_base_loading"));
        pd_loading.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pd_loading.setCanceledOnTouchOutside(false);
        pd_loading.setCancelable(false);
        pd_loading.dismiss();

        tvTitle = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_title"));
        tvTitle.setTextColor(AppColorConfig.getInstance().getSectionTextColor());
        tvTitle.setBackgroundColor(AppColorConfig.getInstance().getSectionColor());
        tvTitle.setText(SimiTranslator.getInstance().translate("SPEND MY POINTS"));

        tvLabel = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_label"));
        tvLabel.setTextColor(AppColorConfig.getInstance().getSectionTextColor());
        tvLabel.setText(SimiTranslator.getInstance().translate("Each of") + " "
                + pointStepLabel + " " + SimiTranslator.getInstance().translate("gets")
                + " " + pointStepDiscount + " "
                + SimiTranslator.getInstance().translate("discount"));

        tvMin = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_min"));
        tvMin.setTextColor(AppColorConfig.getInstance().getSectionTextColor());
        tvMin.setText(minPoint + "");

        tvSpending = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_spending"));
        tvSpending.setTextColor(AppColorConfig.getInstance().getSectionTextColor());
        tvSpending.setText(SimiTranslator.getInstance().translate("Spending") + ": "
                + loyalty_spend);

        tvMax = (TextView) rootView.findViewById(Rconfig.getInstance().id("tv_max"));
        tvMax.setTextColor(AppColorConfig.getInstance().getSectionTextColor());
        tvMax.setText(maxPoints + "");

        sbSpending = (AppCompatSeekBar) rootView.findViewById(Rconfig.getInstance().id("sb_spend"));
        sbSpending.setMax(maxPoints);
        sbSpending.setProgress(loyalty_spend);
        sbSpending.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int process = 0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int end_process = handleProcess(process, point_step);
                tvSpending.setText("");
                tvSpending.setText(SimiTranslator.getInstance().translate("Spending")
                        + ":" + end_process);
                seekBar.setProgress(end_process);
                if (end_process != loy_spend) {
                    // send request to server
                    // url : /loyalty/point/spend
                    // param : {"ruleid":"rate","usepoint":26}
                    loy_spend = end_process;
                    requestUpdateReviewOrder(loy_spend);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                process = progress;
            }
        });

        return rootView;
    }

    protected void requestUpdateReviewOrder(int point) {
        final ModelRewardSeerbar model = new ModelRewardSeerbar();
        showDialogLoading();
        model.setSuccessListener(new ModelSuccessCallBack() {
            @Override
            public void onSuccess(SimiCollection collection) {
                dismissDialogLoading();

                ReviewOrderEntity reviewOrderEntity = null;
                ArrayList<SimiEntity> entities = collection.getCollection();
                if (null != entities && entities.size() > 0) {
                    reviewOrderEntity = (ReviewOrderEntity) entities.get(0);
                }
                if (null != reviewOrderEntity) {
                    for (SimiComponent component : listComponents) {
                        if (component instanceof PaymentMethodComponent) {
                            ((PaymentMethodComponent) component).setListPaymentMethod(reviewOrderEntity.getListPaymentMethod());
                            ((PaymentMethodComponent) component).refreshPaymentMethods();
                        } else if (component instanceof TotalPriceComponent) {
                            ((TotalPriceComponent) component).setTotalPrice(reviewOrderEntity.getTotalPrice());
                            ((TotalPriceComponent) component).refreshTotalPrice();
                        }
                    }
                }
            }
        });
        model.setFailListener(new ModelFailCallBack() {
            @Override
            public void onFail(SimiError error) {
                dismissDialogLoading();
                SimiNotify.getInstance().showNotify(error.getMessage());
            }
        });
        model.addBody("ruleid", "rate");
        model.addBody("usepoint", point + "");
        model.request();
    }

    private int handleProcess(int process, int inputNumber) {
        int endProcess = 0;
        float progress = process;
        float number = inputNumber;
        if (inputNumber == 0) {
            number = 1;
            inputNumber = 1;
        }

        int result = process / inputNumber;
        float excess = progress - (result * number);

        float divide_number = number / 2;

        if (excess >= divide_number) {
            // cong them
            endProcess = (int) (result * number + inputNumber);
        } else {
            // tru di
            endProcess = result * inputNumber;
        }
        return endProcess;
    }

    public void setJSONData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("loyalty_spend")) {
                loyalty_spend = jsonObject.getInt("loyalty_spend");
            }
            if (jsonObject.has("loyalty_rules")) {
                JSONArray array_loyalty_rules = jsonObject
                        .getJSONArray("loyalty_rules");
                JSONObject object_rules = array_loyalty_rules.getJSONObject(0);
                if (object_rules.has("minPoints")) {
                    minPoint = object_rules.getInt("minPoints");
                }
                if (object_rules.has("maxPoints")) {
                    maxPoints = object_rules.getInt("maxPoints");
                }
                if (object_rules.has("pointStepLabel")) {
                    pointStepLabel = object_rules.getString("pointStepLabel");
                }
                if (object_rules.has("pointStepDiscount")) {
                    pointStepDiscount = object_rules
                            .getString("pointStepDiscount");
                }
                if (object_rules.has("pointStep")) {
                    point_step = object_rules.getInt("pointStep");
                }
            }

        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        }
    }

    public void setListComponents(ArrayList<SimiComponent> listComponents) {
        this.listComponents = listComponents;
    }

    public void showDialogLoading() {
        pd_loading.show();
    }

    public void dismissDialogLoading() {
        pd_loading.dismiss();
    }

}
