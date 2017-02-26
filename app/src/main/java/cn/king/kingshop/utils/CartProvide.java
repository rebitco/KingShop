package cn.king.kingshop.utils;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.king.kingshop.bean.ShoppingCart;
import cn.king.kingshop.bean.Wares;

/**
 * Created by king on 2017/2/10.
 */

public class CartProvide {

    private SparseArray<ShoppingCart> datas = null;

    private Context mContext;
    private final String CART_JSON = "cart_json";

    public CartProvide(Context context) {
        datas = new SparseArray<>(10);
        mContext = context;
        listToSparseArray();
    }

    public void put(ShoppingCart cart) {
        ShoppingCart temp = datas.get(cart.getId().intValue());//生成一个临时对象
        if (temp != null) {
            temp.setCount(temp.getCount() + 1);
        } else {
            temp = cart;
            temp.setCount(1);
        }

        datas.put(cart.getId().intValue(), temp);
        commit();//存储到本地
    }

    public void delete(ShoppingCart cart) {
        datas.delete(cart.getId().intValue());
        commit();
    }

    public void update(ShoppingCart cart) {
        datas.put(cart.getId().intValue(), cart);
        commit();//存储到本地
    }

    public List<ShoppingCart> getAll() {

        return getDataFromLocal();
    }

    /**
     * 从本地读取购物车数据
     * @return List
     */
    public List<ShoppingCart> getDataFromLocal() {
        String json = PreferencesUtil.getString(mContext, CART_JSON);
        List<ShoppingCart> carts = null;
        if(json != null) {
            carts  = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }

        return carts;
    }

    /**
     * 将购物车数据存储到本地
     */
    public void commit() {
        List<ShoppingCart> list = sparseArrayToList();
        PreferencesUtil.putString(mContext, CART_JSON, JSONUtil.toJson(list));
    }

    /**
     * 将SparseArray转换成List
     */
    private List<ShoppingCart> sparseArrayToList() {
        int size = datas.size();
        List<ShoppingCart> list = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            list.add(datas.valueAt(i));
        }

        return list;
    }


    /**
     * 为了同步本地和内存中的数据 , 在初始化时将本地的数据同步过来
     */
    public void listToSparseArray() {
        List<ShoppingCart> carts = getDataFromLocal();
        if(carts != null && carts.size() > 0) {
            for (ShoppingCart cart : carts) {
                datas.put(cart.getId().intValue(), cart);
            }
        }
    }


    /**
     * ware 与 shoppingcart互转
     * @param ware
     */
    public void put(Wares ware) {
        ShoppingCart cart = convertData(ware);
        put(cart);
    }
    private ShoppingCart convertData(Wares ware) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(ware.getId());
        cart.setName(ware.getName());
        cart.setPrice(ware.getPrice());
        cart.setDescription(ware.getDescription());
        cart.setImgUrl(ware.getImgUrl());

        return cart;
    }

}
