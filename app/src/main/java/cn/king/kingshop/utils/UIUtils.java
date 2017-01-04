package cn.king.kingshop.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import cn.king.kingshop.KingApplication;

/**
 * Created by king on 2017/1/4.
 */

public class UIUtils {

    /////////////上下文//////////////
    public static Context getContext() {
        return KingApplication.getContext();
    }

    /////////////资源文件//////////////
    public static String getString(int resId) {
        return getContext().getResources().getString(resId);
    }
    public static String[] getStringArray(int resId) {
        return getContext().getResources().getStringArray(resId);
    }
    public static Drawable getDrawable(int resId) {
        return getContext().getResources().getDrawable(resId);
    }
    public static int getColor(int resId) {
        return getContext().getResources().getColor(resId);
    }

    /////////////获取尺寸 单位px//////////////
    public static int getDimens(int id) {
        return getContext().getResources().getDimensionPixelSize(id);
    }

    /////////////单位转换//////////////
    public static int dip2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;

        return (int) (dip * density + 0.5f);
    }
    public static float px2dip(int px){
        float density = getContext().getResources().getDisplayMetrics().density;

        return px / density;
    }

}
