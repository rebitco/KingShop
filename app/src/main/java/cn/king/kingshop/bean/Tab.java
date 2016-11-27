package cn.king.kingshop.bean;


/**
 * 对底部tab的一个封装
 * Created by king on 2016/11/25.
 */

public class Tab {
    private int tabText;
    private int tabIcon;
    private Class fragment;

    public Tab(int tabText, int tabIcon, Class fragment) {
        this.tabText = tabText;
        this.tabIcon = tabIcon;
        this.fragment = fragment;
    }

    public int getTabText() {
        return tabText;
    }

    public void setTabText(int tabText) {
        this.tabText = tabText;
    }

    public int getTabIcon() {
        return tabIcon;
    }

    public void setTabIcon(int tabIcon) {
        this.tabIcon = tabIcon;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
