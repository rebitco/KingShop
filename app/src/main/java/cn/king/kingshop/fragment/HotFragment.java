package cn.king.kingshop.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.king.kingshop.R;

/**
 * Created by Administrator on 2016/11/24.
 */

public class HotFragment extends BaseFragment {

    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshView;
    @ViewInject(R.id.recyclerview_hot)
    private RecyclerView mRecyclerView;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_hot, container, false);
    }

    @Override
    public void init() {

    }
}
