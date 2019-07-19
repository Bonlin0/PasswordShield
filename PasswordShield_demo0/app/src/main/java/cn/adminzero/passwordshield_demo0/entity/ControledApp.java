package cn.adminzero.passwordshield_demo0.entity;

import android.graphics.drawable.Drawable;

/**
 * app控制列表
 * 哪些app可以
 * 被软件控制
 */

public class ControledApp {
    private String lable;
    private String uri;
    private Drawable icon;

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getPackageName() {
        return uri;
    }

    public void setPackageName(String packageName) {
        this.uri = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
