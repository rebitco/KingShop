package cn.king.kingshop.http;

import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by king on 2016/12/20.
 */

public abstract class BaseCallback<T> {

    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if(superclass instanceof  Class) {
            throw  new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;

        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallback() {
        mType = getSuperclassTypeParameter(getClass());
    }

    public abstract void onBeforeRequest(Request request);

    public abstract void onFailure(Request request, Exception e);

    /**
     * 请求成功时调用此方法
     * @param response
     */
    public abstract void onResponse(Response response);

    /**
     * 状态码大于200, 小于300时调用此方法
     * @param response
     * @param t
     */
    public abstract void onSuccess(Response response, T t);

    /**
     * 状态码为400 403 404 500时调用
     * @param response
     * @param code
     * @param e
     */
    public abstract void onError(Response response, int code, Exception e);

    /**
     * Token验证失败, 状态码为401 402 403时调用此方法
     * @param response
     * @param code
     */
    public abstract void onTokenError(Response response, int code);
}
