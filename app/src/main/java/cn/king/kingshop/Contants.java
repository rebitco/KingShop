package cn.king.kingshop;

/**
 * Created by king on 2016/12/26.
 */

public class Contants {

    public static final String CAMPAIGN_ID = "campaigin_id";

    public static final String COMPAINGAIN_ID="compaigin_id";
    public static final String WARE="ware";

    public static final String USER_JSON = "user_json";
    public static final String TOKEN = "token";
//    public static final String TOKEN = "1491798324.72##3d07e6945f01e6a3c2ef213199b1b6eabbdfc691";

    public  static final String DES_KEY="Cniao5_123456";

    public  static final int REQUEST_CODE=0;
    public  static final int REQUEST_CODE_PAYMENT=1;

    public static class API {

        public static final String BASE_URL = "http://112.124.22.238:8081/course_api/";

        public static final String CAMPAIGN_HOME = BASE_URL + "campaign/recommend";

        public static final String BANNER_QUERY = BASE_URL + "banner/query";//首页banner

        public static final String WARES_HOT = BASE_URL + "wares/hot";//热门产品
        public static final String WARES_LIST = BASE_URL + "wares/list";//分类产品
        public static final String WARE_CAMPAIGN_LIST = BASE_URL + "wares/campaign/list";
        public static final String WARE_DETAIL = BASE_URL + "wares/detail.html";

        public static final String CATEGORY_LIST = BASE_URL + "category/list";

        public static final String LOGIN = BASE_URL + "auth/login";//登录
        public static final String REG = BASE_URL + "auth/reg";//注册

        public static final String USER_DETAIL = BASE_URL + "user/get?id=1";

        //订单
        public static final String ORDER_CREATE = BASE_URL + "order/create";
        public static final String ORDER_COMPLETE = BASE_URL + "order/complete";
        public static final String ORDER_LIST = BASE_URL + "order/list";

        //地址
        public static final String ADDRESS_LIST = BASE_URL + "addr/list";
        public static final String ADDRESS_CREATE = BASE_URL + "addr/create";
        public static final String ADDRESS_UPDATE = BASE_URL + "addr/update";

        //收藏
        public static final String FAVORITE_LIST= BASE_URL + "favorite/list";
        public static final String FAVORITE_CREATE= BASE_URL + "favorite/create";

    }

    //SMS velification
    public static final String SMS_APPKEY = "1caa2c2a0a277";
    public static final String SMS_APPSECRECT = "d637aabaf8fa6faa3087eafb7cbde942";

}
