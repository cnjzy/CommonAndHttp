/*
 * Copyright (C) 2017 zhouyou(478319399@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.common.http.util;

import android.text.TextUtils;
import android.view.TextureView;

import com.library.log.LogUtils;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * <p>描述：http工具类</p>
 * 作者： zhouyou<br>
 * 日期： 2017/5/15 17:13 <br>
 * 版本： v1.0<br>
 */
public class HttpUtil {
    public static final Charset UTF8 = Charset.forName("UTF-8");

    public static String createUrlFromParams(String url, Map<String, String> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if (url.indexOf('&') > 0 || url.indexOf('?') > 0) sb.append("&");
            else sb.append("?");
            for (Map.Entry<String, String> urlParams : params.entrySet()) {
                String urlValues = urlParams.getValue();
                //对参数进行 utf-8 编码,防止头信息传中文
                //String urlValue = URLEncoder.encode(urlValues, UTF8.name());
                sb.append(urlParams.getKey()).append("=").append(urlValues).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (Exception e) {
            HttpLog.e(e.getMessage());
        }
        return url;
    }

    public static RequestBody encryptRequestBody(RequestBody requestBody) {
        if (requestBody == null) {
            return null;
        }
        try {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            String bodyStr = URLDecoder.decode(buffer.readString(charset), Charset.forName("UTF-8").name());
            if (TextUtils.isEmpty(bodyStr) || "{}".equals(bodyStr) || "[]".equals(bodyStr) || (!bodyStr.startsWith("{") && !bodyStr.startsWith("["))) {
                return null;
            }
            LogUtils.e("oldBody:" + bodyStr + " newBody:" + RSAUtils.encryptByPublic(bodyStr));
            RequestBody newBody = RequestBody.create(requestBody.contentType(), RSAUtils.encryptByPublic(bodyStr));
            return newBody;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
