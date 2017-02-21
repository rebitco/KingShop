package cn.king.kingshop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.king.kingshop.R;

/**
 * Created by king on 2017/2/8.
 */

public class NumAddSubView extends LinearLayout implements View.OnClickListener {

    private LayoutInflater mInflater;

    private Button btAdd, btSub;
    private TextView tvNumber;

    private int value, maxVal, minVal;

    public OnButtonClickListener mOnButtonClickListener;

    public NumAddSubView(Context context) {
        this(context, null);
    }

    public NumAddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumAddSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);
        initView();

        btAdd.setOnClickListener(this);
        btSub.setOnClickListener(this);

        //自定义attr
        if(attrs != null) {
            TintTypedArray tta = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.NumAddSubView, defStyleAttr, 0);
            int value = tta.getInt(R.styleable.NumAddSubView_value, 0);
            setValue(value);
            int minVal = tta.getInt(R.styleable.NumAddSubView_minVal, 0);
            setMinVal(minVal);
            int maxVal = tta.getInt(R.styleable.NumAddSubView_maxVal, 0);
            if (maxVal != 0) {
                setMaxVal(maxVal);
            }

            Drawable drawableBtnAdd = tta.getDrawable(R.styleable.NumAddSubView_addBtnBackground);
            Drawable drawableBtnSub = tta.getDrawable(R.styleable.NumAddSubView_subBtnBackground);
            Drawable drawableTextView = tta.getDrawable(R.styleable.NumAddSubView_textViewBackground);

            setBtnAddbtAddBackground(drawableBtnAdd);
            setBtnSubBackground(drawableBtnSub);
            setTextViewBackground(drawableTextView);

            tta.recycle();
        }

    }

    private void setBtnAddbtAddBackground(Drawable drawable) {
        btAdd.setBackground(drawable);
    }
    private void setBtnSubBackground(Drawable drawable) {
        btSub.setBackground(drawable);
    }
    private void setTextViewBackground(Drawable drawable) {
        tvNumber.setBackground(drawable);
    }

    private void initView() {
        View view = mInflater.inflate(R.layout.widget_num_sub_add, this, true);
        btAdd = (Button) view.findViewById(R.id.bt_add);
        btSub = (Button) view.findViewById(R.id.bt_sub);
        tvNumber = (TextView) view.findViewById(R.id.tv_number);

    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mOnButtonClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_add) {
           //加
            addNum();
            if(mOnButtonClickListener != null) {
                mOnButtonClickListener.onButtonAddClick(v, value);
            }
        } else if(v.getId() == R.id.bt_sub) {
            //减
            subNum();
            if(mOnButtonClickListener != null) {
                mOnButtonClickListener.onButtonSubClick(v, value);
            }
        }
    }

    private void subNum() {
        getValue();

        if(value > minVal) {
            value = value - 1;
            tvNumber.setText(value + "");
        }
    }

    private void addNum() {
        getValue();

        if(value < maxVal) {
            value = value + 1;
            tvNumber.setText(value + "");
        }

    }

    public interface OnButtonClickListener {
        void onButtonAddClick(View v, int value);
        void onButtonSubClick(View v, int value);
    }

    public int getValue() {
        String value = tvNumber.getText().toString();
        if(value != null && !"".equals(value)) {
            this.value = Integer.parseInt(value);
        }

        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
        tvNumber.setText(value + "");
    }

    public int getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }

    public int getMinVal() {
        return minVal;
    }

    public void setMinVal(int minVal) {
        this.minVal = minVal;
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {

        return false;
    }
}
