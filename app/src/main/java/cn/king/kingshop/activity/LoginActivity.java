package cn.king.kingshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import java.util.HashMap;

import cn.king.kingshop.Contants;
import cn.king.kingshop.KingApplication;
import cn.king.kingshop.R;
import cn.king.kingshop.bean.User;
import cn.king.kingshop.http.OkHttpHelper;
import cn.king.kingshop.http.SpotsCallback;
import cn.king.kingshop.msg.LoginRespMsg;
import cn.king.kingshop.utils.DESUtil;
import cn.king.kingshop.widget.ClearEditext;
import cn.king.kingshop.widget.MyToolBar;
import cn.king.kingshop.widget.PasswordEditText;

/**
 * Created by king on 2017/3/28.
 */

public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private MyToolBar mToolbar;

    @ViewInject(R.id.et_telnum)
    private ClearEditext cetTelnum;

    @ViewInject(R.id.et_pwd)
    private PasswordEditText etPwd;

    private OkHttpHelper mHttpHelp = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        initToolbar();

    }

    private void initToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void toLogin(View view) {
        String userTelnum = cetTelnum.getText().toString().trim();
        if(TextUtils.isEmpty(userTelnum)) {
            Toast.makeText(this, R.string.telnum_notnull, Toast.LENGTH_SHORT).show();
            return;
        }
        if(userTelnum.length()<10) {
            Toast.makeText(this, R.string.telnum_error, Toast.LENGTH_SHORT).show();
            return;
        }

        String userPwd = etPwd.getText().toString().trim();
        if(TextUtils.isEmpty(userPwd)) {
            Toast.makeText(this, R.string.pwd_notnull, Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Contants.API.LOGIN;
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("phone", userTelnum);
        params.put("password", DESUtil.encode(Contants.DES_KEY, userPwd));
        mHttpHelp.post(url, params, new SpotsCallback<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                KingApplication application = KingApplication.getInstance();
                application.putUser(userLoginRespMsg.getUser(), userLoginRespMsg.getToken());//保存起来

                if(application.getIntent() == null) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    application.gotoTargetActivity(LoginActivity.this);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);
            }
        });
    }

    @OnClick(R.id.tv_reg)
    private void toReg(View v) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
