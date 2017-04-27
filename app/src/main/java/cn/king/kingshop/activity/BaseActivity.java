package cn.king.kingshop.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import cn.king.kingshop.KingApplication;
import cn.king.kingshop.bean.User;

/**
 * Created by king on 2017/4/2.
 */

public class BaseActivity extends AppCompatActivity {

    public void startActivity(Intent intent, boolean isNeedLogin) {
        if(isNeedLogin) {
            User user = KingApplication.getInstance().getUser();
            if(user != null) {
                super.startActivity(intent);
            } else {
                KingApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else {
            super.startActivity(intent);
        }

    }
}
