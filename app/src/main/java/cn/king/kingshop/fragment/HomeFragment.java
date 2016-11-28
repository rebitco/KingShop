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

import java.util.HashMap;

import cn.king.kingshop.R;

/**
 * Created by king on 2016/11/24.
 */

public class HomeFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener, View.OnClickListener {

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;
    @ViewInject(R.id.custom_indicator)
    private PagerIndicator mIndicator;

    private Gson mGson = new Gson();//json解析

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

    }

    private void initSlide() {
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("one", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("two", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("three", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("four", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        for (String name : url_maps.keySet()
             ) {
            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name));

            mSliderLayout.addSlider(textSliderView);
        }

        mSliderLayout.setCustomIndicator(mIndicator);//设置指示器
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onStop() {
        mSliderLayout.stopAutoCycle();
        super.onStop();
    }
}
