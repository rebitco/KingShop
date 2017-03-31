package cn.king.kingshop.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import cn.king.kingshop.Contants;
import cn.king.kingshop.KingApplication;
import cn.king.kingshop.R;
import cn.king.kingshop.activity.LoginActivity;
import cn.king.kingshop.bean.User;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/11/24.
 */

public class UserFragment extends BaseFragment {

    @ViewInject(R.id.circle_header)
    private CircleImageView mUserHeader;

    @ViewInject(R.id.tv_username)
    private TextView mUsername;

    @ViewInject(R.id.btn_logout)
    private Button btLogout;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void init() {
        showUser();
    }

    @OnClick(value = {R.id.circle_header, R.id.tv_username})
    public void toLogin(View view) {
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivityForResult(intent, Contants.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        showUser();
    }

    private void showUser() {
        User user = KingApplication.getInstance().getUser();
        if(user == null) {
            btLogout.setVisibility(View.GONE);
            mUsername.setText(R.string.to_login);
        } else {
            btLogout.setVisibility(View.VISIBLE);
            mUsername.setText(user.getUsername());
            if(!TextUtils.isEmpty(user.getLogo_url())) {
                Picasso.with(getActivity()).load(Uri.parse(user.getLogo_url())).into(mUserHeader);//加载用户头像
            }
        }
    }
}
