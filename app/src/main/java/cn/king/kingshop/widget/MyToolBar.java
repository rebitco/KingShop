package cn.king.kingshop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.king.kingshop.R;

/**
 * 自定义toolbar
 * Created by king on 2016/11/26.
 */

public class MyToolBar extends Toolbar {

    private LayoutInflater mInflater;
    public EditText searchView;
    public TextView tvTitle;
    public Button rightButton;
    public View mView;

    public MyToolBar(Context context) {
        this(context, null);
    }

    public MyToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        setContentInsetsRelative(10,10);//设置左右两边的间距

        if(attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.MyToolBar, defStyleAttr, 0);


            final Drawable rightIcon = a.getDrawable(R.styleable.MyToolBar_rightButtonIcon);
            if (rightIcon != null) {
                //setNavigationIcon(navIcon);
                setRightButton(rightIcon);
            }


            boolean isShowSearchView = a.getBoolean(R.styleable.MyToolBar_isShowSearchView,false);

            if(isShowSearchView){//这里根据页面需求来展示, 如果布局设置app:isShowSearchView="true",则显示searchview
                showSearchView();//如果设置为false , 则设置app:title="文本内容" ,就显示title文本内容
                hideTitle();
            }

            CharSequence rightButtonText = a.getText(R.styleable.MyToolBar_rightButtonText);
            if(rightButtonText !=null){
                setRightButtonText(rightButtonText);
            }

            a.recycle();
        }
    }

    private void initView() {

        if(mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);
            searchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            tvTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            rightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_HORIZONTAL);
            addView(mView, params);
        }
    }

    /**
     * toolbar的右边按钮 & 点击事件
     */
    public void setRightButtonText(CharSequence text) {
        rightButton.setText(text);
        showRightText();
    }
    public void setRightButtonText(int id) {
        setRightButtonText(getResources().getText(id));
        showRightText();
    }
    public void setRightButton(Drawable icon) {
        if(rightButton != null) {
            rightButton.setBackground(icon);
            rightButton.setVisibility(View.VISIBLE);
        }
    }
    public void setRightButton(int resId) {
        setRightButton(getResources().getDrawable(resId));
    }
    public void setRightButtonOnClickListener(OnClickListener listener) {
        rightButton.setOnClickListener(listener);
    }

    public Button getRightButton() {
        return this.rightButton;
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        if(tvTitle != null) {
            tvTitle.setText(title);
            showTitle();
        }
    }

    public void showTitle() {
        if(tvTitle != null) {
            tvTitle.setVisibility(View.VISIBLE);
        }
    }

    public void hideTitle() {
        if(tvTitle != null) {
            tvTitle.setVisibility(View.GONE);
        }
    }

    public void showSearchView() {
        if(searchView != null) {
            searchView.setVisibility(View.VISIBLE);
        }
    }

    public void hideSearchView() {
        if(searchView != null) {
            searchView.setVisibility(View.GONE);
        }
    }

    public void showRightText() {
        if (rightButton != null) {
            rightButton.setVisibility(View.VISIBLE);
        }
    }

}
