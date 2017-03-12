package cn.king.kingshop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import cn.king.kingshop.R;
import cn.king.kingshop.widget.SplashView;

/**
 * Created by king on 2017/3/11.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initSplash();
    }

    private void initSplash() {
        SplashView.showSplashView(this, 4, R.mipmap.splash_default, new SplashView.OnSplashViewActionListener() {

            @Override
            public void onSplashViewDismiss(boolean initiativeDismiss) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }

            @Override
            public void onSplashImageClick(String actionUrl) {
                Toast.makeText(getApplicationContext(), "点击后跳转广告页面", Toast.LENGTH_SHORT).show();
            }
        });

        //更新广告图
        String imgUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489262182762&di=647b678dbf002399bf63147a1a71031e&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01b2715631e47d32f8755701607ffe.jpg";
        SplashView.updateSplashData(SplashActivity.this, imgUrl, null);
    }
}
