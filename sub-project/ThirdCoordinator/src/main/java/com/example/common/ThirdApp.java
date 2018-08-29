package com.example.common;

import android.content.Context;

import com.igexin.sdk.PushManager;
import com.library.util.DeviceUtils;

public class ThirdApp {

    public static void init(Context context) {
        initGetui(context);

    }

    private static void initGetui(Context context) {
        PushManager.getInstance().initialize(context, DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(context, DemoIntentService.class);
        PushManager.getInstance().bindAlias(context, DeviceUtils.getAndroidID(context));
    }
}
