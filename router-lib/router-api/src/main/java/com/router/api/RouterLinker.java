package com.router.api;

import android.content.Context;
import android.net.Uri;

import com.router.api.provider.ILinkRedirectProvider;
import com.router.api.router.RouteReq;

/**
 * User: chw
 * Date: 2018/2/7
 */

public class RouterLinker {

    /**
     * 初始化路由
     * @param context
     */
    public static void init (Context context) {
        RouteCenter.getInstance().init(context);
        RouteMappingManager.getInstance().initMapping();
    }

    /**
     * path跳转
     * @param linkPath
     * @return
     */
    public static RouteReq openLink(String linkPath) {
        return new RouteReq(linkPath);
    }

    /**
     * Uri打开
     * @param linkUri
     * @return
     */
    public static RouteReq openLink(Uri linkUri) {
        return new RouteReq(linkUri);
    }

    /**
     * 获取module provider服务类
     * @param providerCls
     * @return
     */
    public static <T> T openProvider(Class<? extends T> providerCls) {
        return RouteCenter.getInstance().routeProvider(providerCls);
    }

    /**
     * 注册link动态跳转服务
     * @param iRouteLinkProvider
     */
    public static void registerLinkRedirect(ILinkRedirectProvider iRouteLinkProvider) {
        RouteMappingManager.getInstance().registerLinkRedirectProvider(iRouteLinkProvider);
    }


}
