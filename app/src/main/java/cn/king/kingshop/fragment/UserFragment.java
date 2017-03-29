package cn.king.kingshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.king.kingshop.R;
import cn.king.kingshop.activity.LoginActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/11/24.
 */

public class UserFragment extends BaseFragment {

    @ViewInject(R.id.circle_header)
    private CircleImageView mUserHeader;

    @ViewInject(R.id.tv_username)
    private TextView mUsername;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void init() {

    }

    @OnClick(value = {R.id.circle_header, R.id.tv_username})
    public void toLogin(View view) {
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
    }

}
