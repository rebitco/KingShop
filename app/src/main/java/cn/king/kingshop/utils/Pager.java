package cn.king.kingshop.utils;


import android.content.Context;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.king.kingshop.R;
import cn.king.kingshop.bean.Page;
import cn.king.kingshop.http.OkHttpHelper;
import cn.king.kingshop.http.SpotsCallback;

public class Pager {

    private OkHttpHelper httpHelper;

    private static final int STATE_NOMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;
    private int state = STATE_NOMAL;


    private static Builder builder;

    private Pager() {
        httpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    public static Builder newBuild() {
        builder = new Builder();

        return builder;
    }

    public void request() {
        requestData();
    }

    public void putParam(String key, Object value) {
        builder.params.put(key, value);
    }

    private void initRefreshLayout() {
        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);

        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
                refresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(builder.pageIndex < builder.totalPage) {
                    loadMore();
                } else {
                    Toast.makeText(builder.mContext, "无更多数据", Toast.LENGTH_SHORT).show();
                    materialRefreshLayout.finishRefreshLoadMore();;
                    materialRefreshLayout.setLoadMore(false);
                }
            }
        });
    }

    /**
     * 刷新数据
     */
    private void refresh() {
        state = STATE_REFRESH;
        builder.pageIndex = 1;
        requestData();
    }

    /**
     * 隐藏数据
     */
    private void loadMore() {
        state = STATE_MORE;
        builder.pageIndex = ++builder.pageIndex;
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        String url = buildUrl();
        httpHelper.get(url, new RequestCallBack(builder.mContext));
    }

    /**
     * 显示数据
     */
    private <T> void showData(List<T> datas, int totalPage, int totalCount) {
        if(datas == null || datas.size() <= 0) {
            Toast.makeText(builder.mContext, "加载不到数据", Toast.LENGTH_SHORT).show();
            return;
        }

        if(STATE_NOMAL == state) {
            if(builder.onPageListener != null) {
                builder.onPageListener.load(datas, totalPage, totalCount);
            }
        }

        else if(STATE_REFRESH == state) {
            builder.mRefreshLayout.finishRefresh();
            if(builder.onPageListener != null) {
                builder.onPageListener.refresh(datas, totalPage, totalCount);
            }
        }

        else if(STATE_MORE == state) {
            builder.mRefreshLayout.finishRefreshLoadMore();
            if(builder.onPageListener != null) {
                builder.onPageListener.loadMore(datas, totalPage, totalCount);
            }
        }
    }

    /**
     * 构建URL
     */
    private String buildUrl() {
        return builder.mUrl + "?" + buildUrlParams();
    }
    private String buildUrlParams() {
        HashMap<String, Object> map = builder.params;
        map.put("curPage", builder.pageIndex);
        map.put("pageSize", builder.pageSize);

        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s= sb.toString();
        if(s.endsWith("&")) {
            s = s.substring(0,s.length() - 1);//如果url以&结尾,截取掉
        }

        return s;
    }

    /**
     * 建造者
     */
    public static class Builder {

        private String mUrl;

        private Context mContext;
        private Type mType;

        private int totalPage = 1;
        private int pageIndex = 1;
        private int pageSize = 10;

        private MaterialRefreshLayout mRefreshLayout;

        private HashMap<String, Object> params = new HashMap<>(5);

        private boolean canLoadMore;

        private OnPageListener onPageListener;

        public Builder setUrl(String url) {
            builder.mUrl = url;

            return builder;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;

            return builder;
        }

        public Builder putParam(String key, Object value) {
            params.put(key, value);

            return builder;
        }

        public Builder setLoadMore(boolean loadMore) {
            this.canLoadMore = loadMore;

            return builder;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout) {
            this.mRefreshLayout = refreshLayout;

            return builder;
        }

        public Builder setOnPageListener(OnPageListener listener) {
            this.onPageListener = listener;

            return builder;
        }

        public Pager build(Context context, Type type) {
            this.mType = type;
            this.mContext = context;

            valid();

            return new Pager();
        }

        private void valid() {
            if(this.mContext == null) {
                throw  new RuntimeException("content can`t be null");
            }
            if(this.mUrl == null || "".equals(this.mUrl)) {
                throw new RuntimeException("url can`t be null");
            }
            if(this.mRefreshLayout == null) {
                throw new RuntimeException("MaterialRefreshLayout can`t be null");
            }
        }

    }

    public interface OnPageListener<T> {
        void load(List<T> datas, int totalPage, int totalCount);
        void refresh(List<T> datas, int totalPage, int totalCount);
        void loadMore(List<T> datas, int totalPage, int totalCount);
    }

    class RequestCallBack<T> extends SpotsCallback<Page<T>> {

        public RequestCallBack(Context context) {
            super(context);
            super.mType = builder.mType;
        }

        @Override
        public void onFailure(Request request, Exception e) {
            dismissDialog();//隐藏dialog
            Toast.makeText(builder.mContext, UIUtils.getString(R.string.requestError)+e.getMessage(), Toast.LENGTH_LONG).show();

            if(state == STATE_REFRESH) {
                builder.mRefreshLayout.finishRefresh();
            } else if(state == STATE_MORE) {
                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }

        @Override
        public void onSuccess(Response response, Page<T> tPage) {
            builder.pageIndex = tPage.getCurrentPage();
            builder.pageSize = tPage.getPageSize();
            builder.totalPage = tPage.getTotalPage();

            //显示数据
            showData(tPage.getList(), tPage.getTotalPage(), tPage.getTotalCount());
        }

        @Override
        public void onError(Response response, int code, Exception e) {
            Toast.makeText(builder.mContext, UIUtils.getString(R.string.loadFail), Toast.LENGTH_LONG).show();

            if(state == STATE_REFRESH) {
                builder.mRefreshLayout.finishRefresh();
            } else if(state == STATE_MORE) {
                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }
    }
}
