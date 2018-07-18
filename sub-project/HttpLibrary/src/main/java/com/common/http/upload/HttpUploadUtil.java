package com.common.http.upload;


import com.common.http.mgr.OkHttpClientMgr;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * (Description)
 *
 * @author jzy
 */
public final class HttpUploadUtil {

    private URL url;
    private HttpURLConnection conn;
    private String boundary = "--------httppost123";
    private Map<String, String> textParams = new HashMap<String, String>();
    private Map<String, File> fileparams = new HashMap<String, File>();
    private DataOutputStream ds;
    private boolean flag = true;
    private OnUploadProgressLinstener onUploadProgressLinstener;
    private String fileName;

    public HttpUploadUtil(String url) throws Exception {
        this.url = new URL(url);
    }

    /**
     * 重新设置要请求的服务器地址，即上传文件的地址。
     *
     * @param url
     * @throws Exception
     */
    public void setUrl(String url) throws Exception {
        this.url = new URL(url);
    }

    /**
     * 增加一个普通字符串数据到form表单数据中
     *
     * @param name
     * @param value
     */
    public void addTextParameter(String name, String value) {
        textParams.put(name, value);
    }

    /**
     * 增加一个文件到form表单数据中
     *
     * @param name
     * @param value
     */
    public void addFileParameter(String name, File value) {
        fileparams.put(name, value);
    }

    /**
     * 清空所有已添加的form表单数据
     */
    public void clearAllParameters() {
        textParams.clear();
        fileparams.clear();
    }

    /**
     * 发送数据到服务器，返回一个字节包含服务器的返回结果的数组
     *
     * @return
     * @throws Exception
     */
    public byte[] send() throws Exception {
        initConnection();
        try {
            conn.connect();
        } catch (SocketTimeoutException e) {
            // something

            throw new RuntimeException();
        }
        ds = new DataOutputStream(conn.getOutputStream());
        writeFileParams();
        writeStringParams();
        paramsEnd();
        InputStream in = conn.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        conn.disconnect();
        return out.toByteArray();
    }

    /**
     * 文件上传的connection的一些必须设置
     *
     * @throws Exception
     */
    private void initConnection() throws Exception {
        conn = (HttpURLConnection) this.url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(10000); // 连接超时为10秒

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    /**
     * 普通字符串数据
     *
     * @throws Exception
     */
    private void writeStringParams() throws Exception {
        Set<String> keySet = textParams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            String value = textParams.get(name);
            ds.writeBytes("--" + boundary + "\r\n");
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
            ds.writeBytes("\r\n");
            ds.writeBytes(encode(value) + "\r\n");
        }
    }

    /**
     * 文件数据
     *
     * @throws Exception
     */
    private void writeFileParams() throws Exception {
        Set<String> keySet = fileparams.keySet();
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            String name = it.next();
            File value = fileparams.get(name);
            ds.writeBytes("--" + boundary + "\r\n");
            ds.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\""
                    + encode(value.getName()) + "\"\r\n");
            ds.writeBytes("Content-Type: " + getContentType(value) + "\r\n");
            ds.writeBytes("\r\n");
            ds.write(getBytes(value));
            ds.writeBytes("\r\n");
        }
    }

    // 获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream

    private String getContentType(File f) throws Exception {
        return "application/octet-stream";
    }

    /**
     * 把文件转换成字节数组
     *
     * @param f
     * @return
     * @throws Exception
     */
    private byte[] getBytes(File f) throws Exception {
        long fileLength = f.length();
        long curLength = 0;
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = in.read(b)) != -1 && flag) {
            out.write(b, 0, n);
            curLength += n;
            if (onUploadProgressLinstener != null)
                onUploadProgressLinstener.onUploadProgress(fileLength, curLength, f.getName(), f.getPath());
        }
        in.close();
        return out.toByteArray();
    }

    /**
     * 添加结尾数据
     *
     * @throws Exception
     */
    private void paramsEnd() throws Exception {
        ds.writeBytes("--" + boundary + "--" + "\r\n");
        ds.writeBytes("\r\n");
    }

    /**
     * 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
     *
     * @param value
     * @return
     * @throws Exception
     */
    private String encode(String value) throws Exception {
        if (null == value)
            return null;
        return URLEncoder.encode(value, "UTF-8");
    }

    /**
     * 停止上传
     */
    public void stopUpload() {
        flag = false;
    }

    /**
     * 设置上传进度监听
     *
     * @param onUploadProgressLinstener
     */
    public void setOnUploadProgressLinstener(OnUploadProgressLinstener onUploadProgressLinstener) {
        this.onUploadProgressLinstener = onUploadProgressLinstener;
    }

    /**
     * 上传进度监听 (Description)
     *
     * @author admin
     */
    public interface OnUploadProgressLinstener {
        public void onUploadProgress(long fileLength, long curLength, String fileName, String filePath);
    }

    /**
     * 示例
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        HttpUploadUtil u = new HttpUploadUtil("http://10.8.7.40:7178/ClienterAPI/GetReceipt?Version=1.0");
        u.addFileParameter("img", new File("D:\\q.jpg"));
        u.addTextParameter("OrderId", "435");
        byte[] b = u.send();
        String result = new String(b);
        System.out.println(result);

    }


    public void uploadFileByOkHttp(final OnUploadFileCallback callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM);
        // 添加字符串参数
        Iterator<String> it = textParams.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = textParams.get(key);
            builder.addFormDataPart(key, value);
        }

        // 添加文件参数
        it = fileparams.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            File value = fileparams.get(key);
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), value);
            builder.addFormDataPart("file", key, fileBody);
        }

        // 创建body
        MultipartBody mBody = builder.build();

        Request request = new Request.Builder().url(url).post(mBody).build();
        OkHttpClientMgr.getIns().getClient().newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(okhttp3.Call call, Response response) {
                try {
                    final String bodyStr = response.body().string();
                    final boolean ok = response.isSuccessful();
                    if (callback != null) {
                        callback.onOkHttpUploadFileCallback(ok, bodyStr);
                    }
                } catch (Exception e) {
                    if (callback != null) {
                        callback.onOkHttpUploadFileCallback(false, "");
                    }
                }

            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (callback != null) {
                    callback.onOkHttpUploadFileCallback(false, e.toString());
                }
            }
        });
    }

    public interface OnUploadFileCallback {
        void onOkHttpUploadFileCallback(boolean isSuccess, String result);
    }
}

