package cn.king.kingshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.king.kingshop.Contants;
import cn.king.kingshop.R;
import cn.king.kingshop.widget.ClearEditext;
import cn.king.kingshop.widget.MyToolBar;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * Created by king on 2017/4/6.
 */

public class RegisterActivity extends AppCompatActivity {

    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";

    @ViewInject(R.id.toolbar)
    private MyToolBar mToolBar;
    @ViewInject(R.id.tv_user_protocal)
    private TextView tvProtocal;

    @ViewInject(R.id.tv_country)
    private TextView tvCountry;
    @ViewInject(R.id.tv_countrycode)
    private TextView tvCountrycode;
    @ViewInject(R.id.input_telnum)
    private ClearEditext telNum;
    @ViewInject(R.id.input_pwd)
    private ClearEditext password;

    private SMSEventHandler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ViewUtils.inject(this);

        initToolbar();

        initRichText();

        SMSSDK.initSDK(this, Contants.SMS_APPKEY,
                        Contants.SMS_APPSECRECT,
                        false);
        mHandler = new SMSEventHandler();
        SMSSDK.registerEventHandler(mHandler);

        //设置国家 + 国家代码
        String[] countryParams = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        if(countryParams != null) {
            tvCountry.setText(countryParams[0]);
            tvCountrycode.setText("+" + countryParams[1]);
        }
    }

    private void initRichText() {
        String richText = getResources().getString(R.string.user_protocal);
        tvProtocal.setText(Html.fromHtml(richText));
        tvProtocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, UserProtocalActivity.class));
            }
        });
    }

    private void initToolbar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.1
                getCode();
            }
        });
    }

    private void getCode() {
        String telephoneNum = telNum.getText().toString().trim().replaceAll("\\s*", "");
        String code = tvCountrycode.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        //1.2
        checkTelNum(telephoneNum, code, pwd);
        //1.3
        SMSSDK.getVerificationCode(code, telephoneNum);
    }

    private void checkTelNum(String telephoneNum, String code, String password) {
        if(code.startsWith("+")) {
          code = code.substring(1);
        }

        if(TextUtils.isEmpty(telephoneNum)) {
            Toast.makeText(this, R.string.telnum_notnull, Toast.LENGTH_SHORT).show();

            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.pwd_notnull, Toast.LENGTH_SHORT).show();

            return;
        }


        if(code == "86") {
            if(telephoneNum.length() != 11) {
                Toast.makeText(this, R.string.telnum_error, Toast.LENGTH_SHORT).show();

                return;
            }
        }

        //手机号码&密码正则表达式
        String rule = "^1(3|5|7|8|4)\\d{9}";
        String rule_pwd = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$";
        Pattern p = Pattern.compile(rule);
        Pattern p2 = Pattern.compile(rule_pwd);
        Matcher matcher = p.matcher(telephoneNum);
        Matcher matcher2 = p2.matcher(password);
        if(!matcher.matches()) {
            Toast.makeText(this, R.string.telnum_error, Toast.LENGTH_SHORT).show();

            return;
        }
        if(!matcher2.matches()) {
            Toast.makeText(this, R.string.pwd_tips, Toast.LENGTH_LONG).show();

            return;
        }
    }

    //1.4
    class SMSEventHandler extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(result == SMSSDK.RESULT_COMPLETE) {
                        if(event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            onCountryListGot((ArrayList<HashMap<String, Object>>) data);
                        } else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //请求验证码后 , 跳转到验证码页面
                            afterVerificationCodeRequested((Boolean) data);
                        }
                    } else {
                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }
                    }
                }
            });
        }
    }

    private void afterVerificationCodeRequested(boolean verify) {
        String telephoneNum = telNum.getText().toString().trim().replaceAll("\\s*", "");
        String code = tvCountrycode.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        if(code.startsWith("+")) {
            code.substring(1);
        }

        Intent intent = new Intent(RegisterActivity.this, RegSecondActivity.class);
        intent.putExtra("telephone", telephoneNum);
        intent.putExtra("countrycode", code);
        intent.putExtra("password", pwd);
        startActivity(intent);
    }

    private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
        // 解析国家列表
        for (HashMap<String, Object> country : countries) {
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue;
            }

            Logger.e("code="+code + "rule="+rule);


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SMSSDK.unregisterEventHandler(mHandler);
    }
}
