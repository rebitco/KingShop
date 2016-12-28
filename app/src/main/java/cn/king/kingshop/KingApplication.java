package cn.king.kingshop;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;

import cn.king.kingshop.bean.User;
import cn.king.kingshop.utils.UserLocalData;

/**
 * Created by king on 2016/12/20.
 */

public class KingApplication extends Application {
    private User user;

    private static KingApplication mInstance;

    public static KingApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        initUser();

        Logger.init("KINGTAG");
//        Logger.init("KINGTAG").logLevel(LogLevel.NONE);//打包上线时关闭所有打TAG
    }

    private void initUser() {
        this.user = UserLocalData.getUser(this);
    }
    public User getUser() {
        return user;
    }
    public String getToken() {
        return UserLocalData.getToken(this);
    }

    public void putUser(User user, String token) {
        this.user = user;
        UserLocalData.putUser(this, user);
        UserLocalData.putToken(this, token);
    }
    public void clearUser() {
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    private Intent intent;
    public void putIntent(Intent intent) {
        this.intent = intent;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public void gotoTargetActivity(Context context) {
        context.startActivity(intent);
        this.intent = null;
    }

}
