package cn.king.kingshop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.king.kingshop.R;
import cn.king.kingshop.widget.MyToolBar;

/**
 * Created by king on 2017/4/7.
 */

public class UserProtocalActivity extends AppCompatActivity {

    @ViewInject(R.id.toolbar)
    private MyToolBar mToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocal);
        ViewUtils.inject(this);

        initToolbar();
    }

    private void initToolbar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProtocalActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

}
