package cn.king.kingshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.HashMap;

import cn.king.kingshop.Contants;
import cn.king.kingshop.KingApplication;
import cn.king.kingshop.R;
import cn.king.kingshop.bean.User;
import cn.king.kingshop.http.OkHttpHelper;
import cn.king.kingshop.http.SpotsCallback;
import cn.king.kingshop.msg.LoginRespMsg;
import cn.king.kingshop.utils.CountTimerView;
import cn.king.kingshop.utils.DESUtil;
import cn.king.kingshop.widget.MyToolBar;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * Created by king on 2017/4/6.
 */

public class RegSecondActivity extends AppCompatActivity {

    @ViewInject(R.id.toolbar)
    private MyToolBar mToolBar;
    @ViewInject(R.id.edittxt_code)
    private EditText etCode;
    @ViewInject(R.id.btn_reSend)
    private Button btResend;
    @ViewInject(R.id.txt_tips)
    private TextView tvTips;

    private String telephone, countrycode, password;
    private SMSEventHandler mHandler;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();
    private CountTimerView countTimerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsecond);

        ViewUtils.inject(this);

        Intent intent = getIntent();
        telephone = intent.getStringExtra("telephone");
        countrycode = intent.getStringExtra("countrycode");
        password = intent.getStringExtra("password");

        initToolbar();

        String formatedPhone = countrycode + " " + splitPhone(telephone);
        String text = getString(R.string.smssdk_send_mobile_detail) + formatedPhone;
        tvTips.setText(Html.fromHtml(text));

        //开启计时器
        CountTimerView countDown = new CountTimerView(btResend);
        countDown.start();

        SMSSDK.initSDK(this, Contants.SMS_APPKEY,
                Contants.SMS_APPSECRECT,
                false);
        mHandler = new SMSEventHandler();
        SMSSDK.registerEventHandler(mHandler);

    }

    private void initToolbar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegSecondActivity.this.finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交验证码
                commitCode();
            }
        });
    }

    @OnClick(R.id.btn_reSend)
    private void resend(View view) {
        SMSSDK.getVerificationCode(countrycode, telephone);
        countTimerView = new CountTimerView(btResend, R.string.smssdk_resend_identify_code);
        countTimerView.start();
    }

    class SMSEventHandler extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(result == SMSSDK.RESULT_COMPLETE) {
                        if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //完成注册 , 上传用户资料, 保存本地, 跳转主界面
                            finishReg();
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
                                Toast.makeText(RegSecondActivity.this, des, Toast.LENGTH_SHORT).show();
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

    private void finishReg() {
        String url = Contants.API.REG;
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("phone", telephone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, password));
        mHttpHelper.post(url, params, new SpotsCallback<LoginRespMsg<User>>(RegSecondActivity.this) {

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                if(userLoginRespMsg.getStatus() == LoginRespMsg.STATUS_ERROR) {
                    Toast.makeText(RegSecondActivity.this, getString(R.string.reg_error)+userLoginRespMsg.getMessage(), Toast.LENGTH_SHORT).show();
                }

                KingApplication application = KingApplication.getInstance();
                application.putUser(userLoginRespMsg.getUser(), userLoginRespMsg.getToken());

                startActivity(new Intent(RegSecondActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
                Toast.makeText(RegSecondActivity.this, "注册失败,请重试", Toast.LENGTH_SHORT).show();
                RegSecondActivity.this.finish();
                //跳回登录页
                Intent intent = new Intent();
                intent.setClass(mContext, LoginActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    private void commitCode() {
        String verifyCode = etCode.getText().toString().trim();
        if(TextUtils.isEmpty(verifyCode)) {
            Toast.makeText(this, R.string.verifycode_notnull, Toast.LENGTH_SHORT).show();

            return;
        }
        SMSSDK.submitVerificationCode(countrycode, telephone, verifyCode);
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

    /**
     * 分割号码
     */
    private String splitPhone(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();

        return builder.toString();
    }
}
