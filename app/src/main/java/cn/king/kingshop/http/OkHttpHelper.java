package cn.king.kingshop.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.king.kingshop.Contants;
import cn.king.kingshop.KingApplication;

/**
 * Created by king on 2016/12/8.
 */

public class OkHttpHelper {
    public static final int TOKEN_MISSING = 401;//token丢失
    public static final int TOKEN_ERROR = 402;//token错误
    public static final int TOKEN_EXPIRE = 403;//token过期


    private static OkHttpHelper mInstance;
    private OkHttpClient mOkHttpClient;
    private Gson mGson;
    private Handler mHandler;

    static {
        mInstance = new OkHttpHelper();
    }

    private OkHttpHelper() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(30, TimeUnit.SECONDS);//30秒

        mGson = new Gson();

        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance() {
        return mInstance;
    }

    public void get(String url, Map<String, Object> param, BaseCallback callback) {

    }

    public void request(final Request request, final BaseCallback callback) {
        callback.onBeforeRequest(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {//异步网络请求
            @Override
            public void onFailure(Request request, IOException e) {
                callbackFailure(callback, request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callbackResponse(callback, response);
                if(response.isSuccessful()) {
                    String resultStr = response.body().string();
                    Logger.d("result=" + resultStr);

                    if(callback.mType == String.class) {
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {
                            Object obj = mGson.fromJson(resultStr, callback.mType);
                            callbackSuccess(callback, response, obj);
                        } catch (JsonParseException e) {
                            //json解析错误
                            callback.onError(response, response.code(), e);
                        }
                    }
                } else if(response.code()==TOKEN_ERROR || response.code()==TOKEN_EXPIRE ||
                        response.code()==TOKEN_MISSING) {
                    callbackTokenError(callback, response);
                } else {
                    callbackError(callback, response, null);
                }
            }
        });
    }

    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }

    private void callbackResponse(final BaseCallback callback, final Response response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }

    public void callbackSuccess(final BaseCallback callback, final Response response, final Object obj) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });
    }

    private void callbackTokenError(final BaseCallback callback, final Response response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response, response.code());
            }
        });
    }

    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }

    private Request buildPostRequest(String url, Map<String, Object> params) {
        return null;
    }
    private Request buildGetRequest(String url, Map<String, Object> params) {
        return null;
    }
    private Request buildRequest(String url, HttpMethodType methodType, Map<String, Object> params) {
        Request.Builder builder = new Request.Builder().url(url);
        if(methodType == HttpMethodType.POST) {
            RequestBody body = builderFormData(params);
            builder.post(body);
        } else if(methodType == HttpMethodType.GET) {
            url = buildUrlParams(url, params);
            builder.url(url);
            builder.get();
        }

        return builder.build();
    }

    private String buildUrlParams(String url, Map<String, Object> params) {
        if(params == null) {
            params = new HashMap<>(1);
        }
        String token = KingApplication.getInstance().getToken();
        if(!TextUtils.isEmpty(token)) {
            params.put("token", token);
        }

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + (entry.getValue()==null?"":entry.getValue().toString()));
            sb.append("&");
        }
        String s = sb.toString();
        if(s.endsWith("&")) {
            s.substring(0, s.length()-1);
        }
        if(url.indexOf("?")>0) {
            url = url + "&" + s;
        } else {
            url = url + "?" + s;
        }

        return url;
    }

    enum HttpMethodType {
        GET,
        POST
    }

    private RequestBody builderFormData(Map<String, Object> params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if(params != null) {
            for(Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue()==null ? "" : entry.getValue().toString());
            }
        }

        String token = KingApplication.getInstance().getToken();
        if(!TextUtils.isEmpty(token)) {
            builder.add(Contants.TOKEN, token);
        }

        return builder.build();
    }

}
