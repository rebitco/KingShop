package cn.king.kingshop.http;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import cn.king.kingshop.KingApplication;
import cn.king.kingshop.activity.LoginActivity;

/**
 * Created by king on 2016/12/29.
 */

public abstract class SimpleCallback<T> extends BaseCallback<T> {
    protected Context mContext;

    public SimpleCallback(Context context) {
        mContext = context;
    }

    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onTokenError(Response response, int code) {
        Toast.makeText(mContext, "登录失败,请重试", Toast.LENGTH_SHORT).show();
        //跳回登录页
        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        KingApplication.getInstance().clearUser();
    }
}
