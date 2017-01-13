package cn.king.kingshop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;

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
}
