package cn.king.kingshop.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import cn.king.kingshop.R;

/**
 * Created by king on 2017/4/8.
 */

public class CountTimerView extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */

    private static final int TIME_COUNT = 61000;//为保证打开即从60秒开始计时
    private TextView btn; //触发重发按钮
    private int endStrRid; //按钮上所对应的文本

    public CountTimerView(long millisInFuture, long countDownInterval, TextView btn, int endStrRid) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    public CountTimerView(TextView btn, int endStrRid) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    public CountTimerView(TextView btn) {
        super(TIME_COUNT, 1000);
        this.btn = btn;
        this.endStrRid = R.string.smssdk_resend_identify_code;
    }

    /**
     * 倒计时过程
     * @param millisUntilFinished
     */
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setEnabled(false);
        btn.setText((int)(millisUntilFinished / 1000) + "秒后可重新发送");
    }

    /**
     * 倒计时完成
     */
    @Override
    public void onFinish() {
        btn.setEnabled(true);//可点击
        btn.setText(endStrRid);
    }
}
