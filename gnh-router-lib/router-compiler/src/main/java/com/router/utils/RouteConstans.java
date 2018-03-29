package com.router.utils;

import com.router.processor.RouterLinkProcessor;
import com.squareup.javapoet.ClassName;

/**
 * User: chw
 * Date: 2018/2/9
 */

public class RouteConstans {

    public static final String PACKAGE_NAME = RouterLinkProcessor.class.getPackage().getName();

    public static final String MODULE_NAME = "moduleName";
    public static final String ROUTE_PROVIDER = "com.router.api.provider.IProvider";
    public static final ClassName ROUTE_INFO = ClassName.get("com.router.api.model", "RouteInfo");
    public static final ClassName ROUTE_MAPPING_MANAGER = ClassName.get("com.router.api", "RouteMappingManager");

}
