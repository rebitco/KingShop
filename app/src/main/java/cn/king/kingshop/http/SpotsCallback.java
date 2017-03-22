package cn.king.kingshop.http;

import android.app.Activity;
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
        //TODO 这里的dialog有问题
        initSpotsDialog();
    }

    private void initSpotsDialog() {
        mDialog = new SpotsDialog(mContext, "搏命加载中...");
    }

    public void showDialog() {
        if (mContext instanceof Activity) {
            mDialog.show();
        }
    }

    public void dismissDialog() {
        if(mContext instanceof Activity) {
            mDialog.dismiss();
        }
    }

    public void setLoadingMsg(int resId) {
        mDialog.setMessage(mContext.getString(resId));
    }

    //在请求网络前显示
    @Override
    public void onBeforeRequest(Request request) {
        if(mContext instanceof Activity) {
            showDialog();
        }
    }

    //在响应后结束显示
    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }
}
