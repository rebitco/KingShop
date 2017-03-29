package cn.king.kingshop.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.king.kingshop.R;
import cn.king.kingshop.widget.ClearEditext;
import cn.king.kingshop.widget.MyToolBar;

/**
 * Created by king on 2017/3/28.
 */

public class LoginActivity extends AppCompatActivity implements View.OnTouchListener {

    @ViewInject(R.id.toolbar)
    private MyToolBar mToolbar;

    @ViewInject(R.id.et_telnum)
    private ClearEditext cetTelnum;
    @ViewInject(R.id.et_pwd)
    private EditText etPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

        etPwd.setOnTouchListener(this);

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * 密码明文/密文处理
     */
    private boolean isTouch = false;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                Drawable drawableRight = etPwd.getCompoundDrawables()[2];//长度为4的数组，分别表示左上右下四张图片

                if(drawableRight == null && event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > etPwd.getWidth()
                        - etPwd.getPaddingRight()
                        - drawableRight.getIntrinsicWidth()){
                    Drawable drawableLeft = getResources().getDrawable(R.drawable.icon_lock);
                    drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
                    if (isTouch) {
                        etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        Drawable drawableOpenEye = getResources().getDrawable(R.drawable.icon_openeye_32);
                        drawableOpenEye.setBounds(0, 0, drawableOpenEye.getMinimumWidth(), drawableOpenEye.getMinimumHeight());
                        etPwd.setCompoundDrawables(drawableLeft, null, drawableOpenEye, null);
                    } else {
                        etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        Drawable drawableCloseEye = getResources().getDrawable(R.drawable.icon_closeeye_32);
                        drawableCloseEye.setBounds(0, 0, drawableCloseEye.getMinimumWidth(), drawableCloseEye.getMinimumHeight());
                        etPwd.setCompoundDrawables(drawableLeft, null, drawableCloseEye, null);
                    }
                    isTouch = !isTouch;
                    etPwd.setSelection(etPwd.getText().toString().length());
                }

                break;
            case MotionEvent.ACTION_MOVE :

                break;
            case MotionEvent.ACTION_UP :

                break;
        }

        return false;
    }
}
