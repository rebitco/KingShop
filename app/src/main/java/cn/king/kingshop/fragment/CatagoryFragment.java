package cn.king.kingshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.util.List;

import cn.king.kingshop.Contants;
import cn.king.kingshop.R;
import cn.king.kingshop.adapter.BaseAdapter;
import cn.king.kingshop.adapter.CategoryAdapter;
import cn.king.kingshop.adapter.WaresAdapter;
import cn.king.kingshop.adapter.decoration.DividerItemDecoration;
import cn.king.kingshop.bean.Banner;
import cn.king.kingshop.bean.Category;
import cn.king.kingshop.bean.Page;
import cn.king.kingshop.bean.Wares;
import cn.king.kingshop.http.HttpUrl;
import cn.king.kingshop.http.OkHttpHelper;
import cn.king.kingshop.http.SimpleCallback;
import cn.king.kingshop.http.SpotsCallback;

/**
 * Created by Administrator on 2016/11/24.
 */

public class CatagoryFragment extends Fragment {
    private OkHttpHelper mHttpHelper;
    private CategoryAdapter mAdapter;
    private WaresAdapter mWaresAdapter;
    private List<Banner> mBanner;

    @ViewInject(R.id.slider)
    private SliderLayout slider;
    @ViewInject(R.id.recyclerview_category)
    private RecyclerView categoryGuide;
    @ViewInject(R.id.custom_indicator)
    private PagerIndicator mIndicator;
    @ViewInject(R.id.recyclerview_product)
    private RecyclerView ryvWares;
    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshView;

    private long categoryId = 0;//初始化左侧类目ID
    private int curPage = 1, pageSize = 10, totalPage = 1;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NORMAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null);
        ViewUtils.inject(this,view);

        mHttpHelper = OkHttpHelper.getInstance();

        initCategoryGuide();
        initSlider();
        initRefreshandLoadMore();

        return view;
    }

    private void initRefreshandLoadMore() {
        mRefreshView.setLoadMore(true);
        mRefreshView.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(curPage <= totalPage) {
                    loadMoreData();
                } else {
                    mRefreshView.finishRefreshLoadMore();
                    Toast.makeText(getContext(), "没有更多的数据了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMoreData() {
        curPage = ++curPage;
        state = STATE_MORE;

        initWaresData(categoryId);
    }

    private void refreshData() {
        curPage = 1;
        state = STATE_REFRESH;

        initWaresData(categoryId);
    }


    private void initSlider() {
        mHttpHelper.get(HttpUrl.BANNER_URL, new SpotsCallback<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner = banners;
                if (mBanner != null) {
                    for (Banner banner : mBanner) {
                        TextSliderView sliderView = new TextSliderView(getActivity());
                        sliderView.image(banner.getImgUrl())
                                  .description(banner.getName())
                                  .setScaleType(BaseSliderView.ScaleType.Fit);

                        slider.addSlider(sliderView);
                    }
                }

                slider.setCustomIndicator(mIndicator);
                slider.setPresetTransformer(SliderLayout.Transformer.CubeIn);//滚动动画
                slider.setDuration(3000);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void initCategoryGuide() {
        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallback<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);
                if(categories != null && categories.size() > 0) {
                    categoryId = categories.get(0).getId();//当进入商品分类页,第一时间加载第一个类目的商品
                    initWaresData(categoryId);

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showCategoryData(final List<Category> categories) {

        mAdapter = new CategoryAdapter(getContext(), categories);

        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                categoryId = mAdapter.getItem(position).getId();
                initWaresData(categoryId);
            }
        });



        categoryGuide.setAdapter(mAdapter);
        categoryGuide.setItemAnimator(new DefaultItemAnimator());
        categoryGuide.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryGuide.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    private void initWaresData(long categoryId) {
        //http://112.124.22.238:8081/course_api/wares/list?categoryId=3&curPage=1&pageSize=10
        String waresUrl = Contants.API.WARES_LIST + "?categoryId=" + categoryId + "&curPage=" + curPage + "&pageSize=" + pageSize;
        mHttpHelper.get(waresUrl, new SimpleCallback<Page<Wares>>(getContext()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                curPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();
                showWares(waresPage.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showWares(List<Wares> wares) {

        switch (state) {
            case STATE_NORMAL:
                if(mWaresAdapter == null) {
                    mWaresAdapter = new WaresAdapter(getContext(), wares);
                    ryvWares.setAdapter(mWaresAdapter);
                    ryvWares.setLayoutManager(new GridLayoutManager(getContext(), 2));//@param spanCount The number of columns in the grid, 一行的个数
                    ryvWares.setItemAnimator(new DefaultItemAnimator());
                } else {
                    mWaresAdapter.clear();
                    mWaresAdapter.addData(wares);
                }
            break;

            case STATE_REFRESH:
                mWaresAdapter.clear();
                mWaresAdapter.addData(wares);

                ryvWares.scrollToPosition(0);
                mRefreshView.finishRefresh();

            break;

            case STATE_MORE:
                mWaresAdapter.addData(wares.size(), wares);

                ryvWares.scrollToPosition(wares.size());
                mRefreshView.finishRefreshLoadMore();
            break;
        }
    }

}
