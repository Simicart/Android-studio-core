package com.simicart.core.base.notify;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.Toast;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.translate.SimiTranslator;

/**
 * Created by Martial on 8/16/2016.
 */
public class SimiNotify {

    protected Boolean isShowedNotify = false;
    public static SimiNotify instance;

    public static SimiNotify getInstance() {
        if(instance == null) {
            instance = new SimiNotify();
        }
        return instance;
    }

    public void showError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SimiManager.getIntance().getCurrentActivity());
        builder.setTitle(SimiTranslator.getInstance().translate("Warning..."));
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(SimiTranslator.getInstance().translate("OK"),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showNotify(String message) {

        synchronized (isShowedNotify) {
            if (!isShowedNotify) {
                isShowedNotify = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        SimiManager.getIntance().getCurrentActivity());
                builder.setTitle(SimiTranslator.getInstance().translate("Warning..."));
                builder.setMessage(message)
                        .setCancelable(true)
                        .setPositiveButton(SimiTranslator.getInstance().translate("OK"),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.dismiss();
                                        isShowedNotify = false;
                                    }
                                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

    }

    public void showNotify(String title, String message, String sPositiveButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SimiManager.getIntance().getCurrentActivity());
        builder.setTitle(SimiTranslator.getInstance().translate(title));
        builder.setMessage(SimiTranslator.getInstance().translate(message))
                .setCancelable(true)
                .setPositiveButton(
                        SimiTranslator.getInstance().translate(sPositiveButton),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void showToast(String mes) {
        Toast toast = Toast.makeText(SimiManager.getIntance().getCurrentActivity(), SimiTranslator.getInstance().translate(mes), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
