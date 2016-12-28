package cn.king.kingshop.utils;

import android.content.Context;
import android.text.TextUtils;

import cn.king.kingshop.Contants;
import cn.king.kingshop.bean.User;

/**
 * Created by king on 2016/12/26.
 */

public class UserLocalData {

    public static void putUser(Context context, User user) {
        String user_json = JSONUtil.toJson(user);
        PreferencesUtil.putString(context, Contants.USER_JSON, user_json);
    }

    public static void putToken(Context context, String token) {
        PreferencesUtil.putString(context, Contants.TOKEN, token);
    }

    public static User getUser(Context context) {
        String user_json = PreferencesUtil.getString(context, Contants.USER_JSON);
        if (!TextUtils.isEmpty(user_json)) {
            return JSONUtil.fromJson(user_json, User.class);
        }

        return null;
    }

    public static String getToken(Context context) {
        return PreferencesUtil.getString(context, Contants.TOKEN);
    }

    public static void clearUser(Context context) {
        PreferencesUtil.putString(context, Contants.USER_JSON, "");
    }

    public static void clearToken(Context context) {
        PreferencesUtil.putString(context, Contants.TOKEN, "");
    }

}
