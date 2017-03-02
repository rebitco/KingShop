package cn.king.kingshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

import cn.king.kingshop.Contants;
import cn.king.kingshop.R;
import cn.king.kingshop.ShopListActivity;
import cn.king.kingshop.adapter.HomeCategoryAdapter;
import cn.king.kingshop.adapter.decoration.CardViewDecoration;
import cn.king.kingshop.bean.Banner;
import cn.king.kingshop.bean.Campaign;
import cn.king.kingshop.bean.HomeCampaign;
import cn.king.kingshop.http.BaseCallback;
import cn.king.kingshop.http.HttpUrl;
import cn.king.kingshop.http.OkHttpHelper;
import cn.king.kingshop.http.SpotsCallback;

/**
 * Created by king on 2016/11/24.
 */

public class HomeFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener, View.OnClickListener {

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;
    @ViewInject(R.id.custom_indicator)
    private PagerIndicator mIndicator;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    private List<Banner> mBanner;
    private Gson mGson = new Gson();//json解析
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();
    private HomeCategoryAdapter mAdapter;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void init() {
        requestBannerImg();//请求banner网络图片
        initSlide();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mHttpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {
            @Override
            public void onBeforeRequest(Request request) {

            }
            @Override
            public void onFailure(Request request, Exception e) {

            }
            @Override
            public void onResponse(Response response) {

            }
            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }
            @Override
            public void onError(Response response, int code, Exception e) {

            }
            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    private void initData(List<HomeCampaign> homeCampaigns) {
        mAdapter = new HomeCategoryAdapter(homeCampaigns, getActivity());
        mAdapter.setOnCampaignClickListener(new HomeCategoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View v, Campaign campaign) {
                //点击跳转
                Intent intent = new Intent(getContext(), ShopListActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new CardViewDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    private void requestBannerImg() {
        mHttpHelper.get(HttpUrl.BANNER_URL, new SpotsCallback<List<Banner>>(getActivity()) {
            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner = banners;
                initSlide();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void initSlide() {
        if(mBanner != null) {
            for(Banner banner : mBanner) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl())
                                .description(banner.getName())
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(this);
                mSliderLayout.addSlider(textSliderView);
            }
        }

        mSliderLayout.setCustomIndicator(mIndicator);//设置指示器
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //每个slider的点击事件
//        ToastUtil.show(getActivity(), slider.getUrl());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mSliderLayout.stopAutoCycle();
    }
}
