package com.router.api;


import com.router.api.provider.IProvider;
import com.router.api.model.RouteInfo;
import com.router.api.provider.ILinkRedirectProvider;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * User: chw
 * Date: 2018/1/26
 * 路由映射地址注册管理
 */
public class RouteMappingManager {

    private ILinkRedirectProvider mRouteLinkProvider;

    private HashMap<String, RouteInfo> mRouteLinkMap;
    private HashMap<String, IProvider> mProviderInstanceMap;
    private HashMap<String, Class<? extends IProvider>> mProviderClassMap;

    private static class Holder {
        static RouteMappingManager INSTANCE = new RouteMappingManager();
    }

    public static RouteMappingManager getInstance() {
        return Holder.INSTANCE;
    }

    public RouteMappingManager() {
        mRouteLinkMap = new HashMap<>();
        mProviderClassMap = new HashMap<>();
        mProviderInstanceMap = new HashMap<>();
    }

    /**
     * 初始化跳转，provider映射
     */
    public void initMapping() {
        initLinkMapping();
        initProviderMapping();
    }

    /**
     * 注册link动态跳转服务
     * @param iRouteLinkProvider
     */
    public void registerLinkRedirectProvider(ILinkRedirectProvider iRouteLinkProvider) {
        mRouteLinkProvider = iRouteLinkProvider;
    }

/**
 * 编译的时候aspectj使用代码的方法 ======================================================================
 */
    /**
     * 织入被注解标记的activity的link
     */
    private void initLinkMapping() {}

    /**
     * 织入被注解标记的provider服务类
     */
    private void initProviderMapping() {}

    /**
     * 编译的时候动态生成调用该方法注入link跳转的代码
     * @param path
     * @param routeInfo
     */
    public void addLinkMapping(String path, RouteInfo routeInfo) {
        mRouteLinkMap.put(path, routeInfo);
    }

    /**
     * 编译的时候动态生成调用该方法注入provider的代码
     * @param path
     * @param providerCls
     */
    public void addProviderMapping(String path, Class<? extends IProvider> providerCls) {
        mProviderClassMap.put(path, providerCls);
    }

/**
 * 编译的时候aspectj会使用的方法 =======================================================================
 */


    public RouteInfo getRouteInfo(String path) {
        return mRouteLinkMap.get(path);
    }

    public IProvider getModuleProvider(String providerName) {
        if (mProviderInstanceMap.containsKey(providerName)) {
            return mProviderInstanceMap.get(mProviderInstanceMap);
        }

        if (!mProviderClassMap.containsKey(providerName)) {
            return null;
        }

        Class<? extends IProvider> provider = mProviderClassMap.get(providerName);
        try {
            IProvider providerInstance = provider.getConstructor().newInstance();
            providerInstance.init(RouteCenter.getInstance().getContext());
            mProviderInstanceMap.put(providerName, providerInstance);
            return providerInstance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取link动态重定向服务
     * @return
     */
    public ILinkRedirectProvider getLinkRedirectProvider() {
        return mRouteLinkProvider;
    }
}

