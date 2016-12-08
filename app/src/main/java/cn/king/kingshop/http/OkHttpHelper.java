package cn.king.kingshop.http;

/**
 * Created by king on 2016/12/8.
 */

public class OkHttpHelper {
    private static OkHttpHelper mInstance;

    static {
        mInstance = new OkHttpHelper();
    }

    private OkHttpHelper() {

    }

    public static OkHttpHelper getInstance() {
        return mInstance;
    }
}
