package cn.king.kingshop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.king.kingshop.R;

/**
 * Created by king on 2017/3/29.
 */

public class PasswordEditText extends AppCompatEditText implements View.OnTouchListener {

    private boolean isTouch = false;
    private OnTouchListener mOnTouchListener;

    public PasswordEditText(Context context) {
        super(context);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setOnTouchListener(this);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                Drawable drawableRight = getCompoundDrawables()[2];//长度为4的数组，分别表示左上右下四张图片

                if(drawableRight == null && event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > getWidth()
                        - getPaddingRight()
                        - drawableRight.getIntrinsicWidth()){
                    Drawable drawableLeft = getResources().getDrawable(R.drawable.icon_lock);
                    drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
                    if (isTouch) {
                        setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        Drawable drawableOpenEye = getResources().getDrawable(R.drawable.icon_openeye_32);
                        drawableOpenEye.setBounds(0, 0, drawableOpenEye.getMinimumWidth(), drawableOpenEye.getMinimumHeight());
                        setCompoundDrawables(drawableLeft, null, drawableOpenEye, null);
                    } else {
                        setTransformationMethod(PasswordTransformationMethod.getInstance());
                        Drawable drawableCloseEye = getResources().getDrawable(R.drawable.icon_closeeye_32);
                        drawableCloseEye.setBounds(0, 0, drawableCloseEye.getMinimumWidth(), drawableCloseEye.getMinimumHeight());
                        setCompoundDrawables(drawableLeft, null, drawableCloseEye, null);
                    }
                    isTouch = !isTouch;
                    setSelection(getText().toString().length());
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
