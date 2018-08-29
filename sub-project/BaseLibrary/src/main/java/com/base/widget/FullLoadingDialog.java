package com.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.base.R;
import com.library.util.DeviceUtils;


/**
 * Created by jzy on 2017/6/12.
 */

public class FullLoadingDialog extends Dialog {

    public FullLoadingDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme_NoTitle);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.view_loading, null);
        view.setMinimumWidth(100000);
        view.setMinimumHeight(100000);
        setContentView(view);

        // 设置全屏
        Window window = getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        Rect rect = new Rect();
        View view1 = window.getDecorView();
        view1.getWindowVisibleDisplayFrame(rect);
        windowparams.height = DeviceUtils.getScreenPx(getContext()).height();
        windowparams.width = DeviceUtils.getScreenPx(getContext()).width();
        window.setWindowAnimations(R.style.pic_gallery_dialog_animstyle);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(windowparams);

        setCanceledOnTouchOutside(false);
    }
}
