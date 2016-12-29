package cn.king.kingshop.http;

import android.content.Context;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import dmax.dialog.SpotsDialog;

/**
 * Created by king on 2016/12/29.
 */

public abstract class SpotsCallback<T> extends SimpleCallback<T> {
    private SpotsDialog mDialog;

    public SpotsCallback(Context context) {
        super(context);
        initSpotsDialog();
    }

    private void initSpotsDialog() {
        mDialog = new SpotsDialog(mContext, "搏命加载中...");
    }

    public void showDialog() {
        mDialog.show();
    }

    public void dismissDialog() {
        mDialog.dismiss();
    }

    public void setLoadingMsg(int resId) {
        mDialog.setMessage(mContext.getString(resId));
    }

    //在请求网络前显示
    @Override
    public void onBeforeRequest(Request request) {
        showDialog();
    }

    //在响应后结束显示
    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }
}
