package cn.king.kingshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;

import cn.king.kingshop.KingApplication;
import cn.king.kingshop.activity.LoginActivity;
import cn.king.kingshop.bean.User;

/**
 * Created by king on 2016/11/28.
 */

public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = createView(inflater, container, savedInstanceState);
        ViewUtils.inject(this, view);//初始化xutils
        init();

        return view;
    }

    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();

    public void initToolBar(){};

    public void startActivity(Intent intent, boolean isNeedLogin) {

        if(isNeedLogin) {
            User user = KingApplication.getInstance().getUser();
            if(user != null) {
                KingApplication.getInstance().putIntent(intent);//将intent存起来
                super.startActivity(intent);
            } else {
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else {
            //不需要跳转登录页面 , 如商品详情页 , 直接跳转就是
            super.startActivity(intent);
        }
    }
}
