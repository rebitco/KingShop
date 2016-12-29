package cn.king.kingshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.util.List;

import cn.king.kingshop.R;
import cn.king.kingshop.bean.Banner;
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

    private List<Banner> mBanner;
    private Gson mGson = new Gson();//json解析
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void init() {
        requestBannerImg();//请求banner网络图片
        initSlide();
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
//        ToastUtil.show(getActivity(), slider.getUrl()); //获取跳转的url
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mSliderLayout.stopAutoCycle();
    }
}
