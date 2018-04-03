package com.router.api.provider;

import android.content.Context;

import com.router.api.router.RouteReq;

/**
 * User: chw
 * Date: 2018/3/23
 * 拦截器
 */

public interface InterceptProvider {

    /**
     * 拦截
     * @param routeReq
     * @return
     */
     boolean onIntercept(Context context, RouteReq routeReq);

}
