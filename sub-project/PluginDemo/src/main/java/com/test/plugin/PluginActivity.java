package com.test.plugin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.common.http.callback.HttpCallback;
import com.library.log.LogUtils;
import com.test.plugin.data.PluginRemoteLoader;
import com.test.plugin.data.bean.NewsBean;
import com.test.plugin.data.req.ReqNewsList15;

import java.util.List;

public class PluginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        loadImage();
    }

    private void loadImage() {
        image = findViewById(R.id.image);
        Glide.with(this).load("https://imgsa.baidu.com/exp/whcrop=92,69/sign=fd32b608a818972ba36f568889bd46b0/ac4bd11373f08202778b1b2348fbfbedab641b94.jpg").into(image);
    }

    private void loadNews() {
        PluginRemoteLoader.getInstance().post(new ReqNewsList15(), new HttpCallback<List<NewsBean>>() {
            @Override
            public void onError(int code, Throwable e) {
                LogUtils.e("=========error:" + e.getMessage());
            }

            @Override
            public void onSuccess(List<NewsBean> newsList) {
                LogUtils.e("=========== list size:" + newsList.size());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load_btn:
                loadNews();
                break;
        }
    }
}
