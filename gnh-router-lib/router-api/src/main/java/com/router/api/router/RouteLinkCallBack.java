package com.router.api.router;

/**
 * User: chw
 * Date: 2018/3/22
 */

public interface RouteLinkCallBack {
    void onSuccess(RouteReq routeReq);
    void onFail(RouteReq routeReq);
    void onNeedLogin(RouteReq routeReq);
}
