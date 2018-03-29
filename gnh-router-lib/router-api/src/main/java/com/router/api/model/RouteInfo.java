package com.router.api.model;

/**
 * User: chw
 * Date: 2018/2/2
 */

public class RouteInfo {

    private String name;

    private String path;

    private int extra;

    private boolean isNeedLogin;

    private Class<?> destClass;

    public String getPath() {
        return path;
    }

    public Class<?> getDestClass() {
        return destClass;
    }

    public boolean isNeedLogin() {
        return isNeedLogin;
    }

    public void setDestClass(Class<?> destClass) {
        this.destClass = destClass;
    }

    public static RouteInfo build(String path, Class<?> destClass, String name, boolean isNeedLogin, int extra) {
        return new RouteInfo(path, destClass, name, isNeedLogin, extra);
    }

    public RouteInfo(String path, Class<?> destClass, String name, boolean isNeedLogin, int extra) {
        this.path = path;
        this.destClass = destClass;
        this.name = name;
        this.isNeedLogin = isNeedLogin;
        this.extra = extra;
    }
}
