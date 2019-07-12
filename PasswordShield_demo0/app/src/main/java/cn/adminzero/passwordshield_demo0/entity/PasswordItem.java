package cn.adminzero.passwordshield_demo0.entity;


/**
 * APP的范围内的账户密码
 */
public class PasswordItem {

    /**
     * 主属性id
     */
    private int id;
    /**
     * app登录账户名
     */
    private String account;
    /**
     * app登录密码
     */
    private String password;
    /**
     * 应用程序包名(非浏览器)
     * 或者  浏览器网页域名
     */
    private String uri;
    /**
     * 备注信息
     */
    private String note;
}

