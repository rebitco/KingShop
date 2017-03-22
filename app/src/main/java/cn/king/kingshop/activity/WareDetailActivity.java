package cn.king.kingshop.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;

import cn.king.kingshop.Contants;
import cn.king.kingshop.R;
import cn.king.kingshop.bean.Wares;
import cn.king.kingshop.utils.CartProvide;
import cn.king.kingshop.widget.MyToolBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;

/**
 * Created by king on 2017/3/15.
 */

public class WareDetailActivity extends AppCompatActivity {
    @ViewInject(R.id.webview)
    private WebView mWebView;
    @ViewInject(R.id.toolbar)
    private MyToolBar mToolbar;

    private Wares mWare;

    private WebAppInterface mInterface;

    private CartProvide cartProvide;

    private SpotsDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waredetail);
        ViewUtils.inject(this);

        Serializable serializable = getIntent().getSerializableExtra(Contants.WARE);
        if(serializable == null) {
            this.finish();
        } else {
            mWare = (Wares) serializable;
        }

        initWebview();
        initToolbar();

        cartProvide = CartProvide.getCartProvide();
        mDialog = new SpotsDialog(this);
    }

    private void initToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareDetailActivity.this.finish();
            }
        });

        //右边分享按钮
        mToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    private void initWebview() {
        WebSettings setting = mWebView.getSettings();
        setting.setBlockNetworkImage(false);
        setting.setJavaScriptEnabled(true);
        setting.setAppCacheEnabled(true);
        mWebView.loadUrl(Contants.API.WARE_DETAIL);

        mInterface = new WebAppInterface(this);
        mWebView.addJavascriptInterface(mInterface, "appInterface");

        mWebView.setWebViewClient(new CustomWebClient());
    }

    /**
     * 加载中
     */
    class CustomWebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if(mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            mInterface.showDetail();
        }
    }


    class WebAppInterface {
        private Context mContext;

        public WebAppInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void showDetail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showDetail(" + mWare.getId() + ")");
                }
            });
        }

        @JavascriptInterface
        public void buy(long id) {

        }

        @JavascriptInterface
        public void addToCart(long id) {
            cartProvide.put(mWare);
            Toast.makeText(mContext, R.string.addCart, Toast.LENGTH_SHORT).show();
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(mWare.getName());
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://th-guestshow.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mWare.getDescription());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(mWare.getImgUrl());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://th-guestshow.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("Guestshow");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://th-guestshow.com");

// 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ShareSDK.stopSDK(this);
    }
}
