package com.base.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.widget.Toast;

import com.base.BuildConfig;
import com.base.ui.view.ITipBaseUI;
import com.library.util.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jzy on 2017/5/25.
 */

public class DefaultTipUI implements ITipBaseUI {

    private Context mContext = null;
    private List<Dialog> dialogList = new ArrayList<>();

    private Dialog progressDialog;

    public DefaultTipUI(Context context) {
        this.mContext = context;
    }

    @Override
    public void showToast(String content, boolean showInRelease) {
        boolean isCanShow = false;
        if (showInRelease) {
            isCanShow = true;
        } else if (BuildConfig.DEBUG) {
            isCanShow = true;
            content = "debug:" + content;
        }
        if (isContextNotEmpty() && isCanShow) {
//            CustomToast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showTipDialog(String title, String content, final TipCallback callback) {
        if (isContextNotEmpty()) {
            Dialog dialog = new AlertDialog.Builder(mContext).setTitle(title).setMessage(content).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialogList.remove(dialog);
                }
            }).create();
            dialogList.add(dialog);
            dialog.show();
        }
    }

    @Override
    public void showLoadingDialog() {
        if (isContextNotEmpty()) {
            if (progressDialog == null || !progressDialog.isShowing()) {
                progressDialog = new FullLoadingDialog(mContext);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialogList.remove(dialog);
                    }
                });
                dialogList.add(progressDialog);
                if (!isDestroy(mContext)) {
                    progressDialog.show();
                }
            }
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onTipDestroy() {
        for (int i = dialogList.size() - 1; i > 0; i--) {
            Dialog dialog = dialogList.get(i);
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            dialogList.remove(i);
        }
    }

    private boolean isContextNotEmpty() {
        return mContext != null;
    }

    public static boolean isDestroy(Context context) {
        return DeviceUtils.isActivityDestroy(context);
    }
}
