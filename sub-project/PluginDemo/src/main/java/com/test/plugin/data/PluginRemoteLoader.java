package com.test.plugin.data;

import com.common.http.bean.BaseReq;
import com.common.http.callback.HttpCallback;
import com.common.http.common.BaseCommonRemoteLoader;
import com.common.http.common.ICommonSource;

/**
 * @author jzy
 * created at 2018/6/21
 */
public class PluginRemoteLoader extends BaseCommonRemoteLoader {

    private PluginRemoteLoader() {
    }

    private static class HOLDER {
        static final PluginRemoteLoader INS = new PluginRemoteLoader();
    }

    public static ICommonSource getInstance() {
        return HOLDER.INS;
    }

    @Override
    public void postWithForm(BaseReq req, HttpCallback callback) {
        super.post(req, callback);
    }
}
