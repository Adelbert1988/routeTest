package demo.router.com.routertest;

import com.router.api.router.RouteLinkCallBack;
import com.router.api.router.RouteReq;

/**
 * User: chw
 * Date: 2018/3/28
 * 自定义实现route回调处理
 */

public abstract class GNHRouteCallBack implements RouteLinkCallBack{

    @Override
    public void onNeedLogin(RouteReq routeReq) {
        //实现项目的登陆逻辑
    }
}
