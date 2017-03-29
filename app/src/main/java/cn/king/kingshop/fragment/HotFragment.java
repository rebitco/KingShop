package cn.king.kingshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.king.kingshop.Contants;
import cn.king.kingshop.R;
import cn.king.kingshop.activity.WareDetailActivity;
import cn.king.kingshop.adapter.BaseAdapter;
import cn.king.kingshop.adapter.HWAdapter;
import cn.king.kingshop.bean.Page;
import cn.king.kingshop.bean.Wares;
import cn.king.kingshop.utils.Pager;

import static cn.king.kingshop.R.id.refresh_view;

/**
 * Created by Administrator on 2016/11/24.
 */

public class HotFragment extends BaseFragment implements Pager.OnPageListener {

    @ViewInject(refresh_view)
    private MaterialRefreshLayout mRefreshView;
    @ViewInject(R.id.recyclerview_hot)
    private RecyclerView mRecyclerView;

    private HWAdapter mAdapter;

//    private OkHttpHelper mHttpHelp = OkHttpHelper.getInstance();

//    private int currentPage = 1, pageSize = 10;
//    private int totalPage = 3;
//    private List<Wares> mDatas;

//    private static final int STATE_NORMAL = 0;
//    private static final int STATE_REFRESH = 1;
//    private static final int STATE_MORE = 2;
//    private int currentState = STATE_NORMAL;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_hot, container, false);
    }

    @Override
    public void init() {

//        initRecyclerView();
//        initRefresh();
        if(!isRemoving()) {
            Pager pager = Pager.newBuild()
                    .setUrl(Contants.API.WARES_HOT)
                    .setLoadMore(true)
                    .setOnPageListener(this)
                    .setPageSize(20)
                    .setRefreshLayout(mRefreshView)
                    .build(getContext(), new TypeToken<Page<Wares>>(){}.getType());

            pager.request();
        }
    }

    @Override
    public void load(final List datas, int totalPage, int totalCount) {
        mAdapter = new HWAdapter(getContext(), datas);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                intent.putExtra(Contants.WARE, mAdapter.getItem(position));
                startActivity(intent);

            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) {
            mAdapter.refreshData(datas);
            mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) {
        mAdapter.loadMoreData(datas);
        mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
    }

//    private void initRefresh() {
//        mRefreshView.setLoadMore(true);
//        mRefreshView.setMaterialRefreshListener(new MaterialRefreshListener() {
//            @Override
//            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
//                refreshData();
//            }
//
//            @Override
//            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
//                if (currentPage <= totalPage) {
//                    loadMore();
//                } else {
//                    mRefreshView.finishRefreshLoadMore();
//                    Toast.makeText(getActivity(), R.string.no_more_data, Toast.LENGTH_SHORT).show();
//                    Logger.d("当前的页码是 -------------->"+currentPage);
//                    Logger.d("总的页码是 -------------->"+totalPage);
//                }
//            }
//        });
//    }
//
//    private void loadMore() {
//        currentPage = ++currentPage;
//        currentState = STATE_MORE;
//        initRecyclerView();
//    }
//
//    private void refreshData() {
//        currentPage = 1;
//        currentState = STATE_REFRESH;
//        initRecyclerView();
//    }
//
//    private void initRecyclerView() {
//        String url = Contants.API.WARES_HOT +
//                "?curPage=" + currentPage + "&pageSize=" + pageSize;
//        mHttpHelp.get(url, new SpotsCallback<Page<Wares>>(getContext()) {
//
//            @Override
//            public void onSuccess(Response response, Page<Wares> waresPage) {
//                mDatas = waresPage.getList();
//                currentPage = waresPage.getCurrentPage();
//                pageSize = waresPage.getPageSize();
////                totalPage = waresPage.getTotalPage();
//
//                showData();
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//
//            }
//        });
//    }
//
//    private void showData() {
//        switch (currentState) {
//            case STATE_NORMAL:
//                mAdapter = new HWAdapter(getContext(), mDatas);
//                mRecyclerView.setAdapter(mAdapter);
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
////        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
//                break;
//            case STATE_REFRESH:
//                mAdapter.clearData(mDatas);
//                mAdapter.addData(mDatas);
//
//                mRecyclerView.scrollToPosition(0);
//                mRefreshView.finishRefresh();
//                break;
//            case STATE_MORE:
//                mAdapter.addData(mAdapter.getDatas().size(), mDatas);
//
//                mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
//                mRefreshView.finishRefreshLoadMore();
//                break;
//        }
//    }

}
