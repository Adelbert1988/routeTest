package com.router.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.router.api.provider.ILinkRedirectProvider;
import com.router.api.router.RouteLinkCallBack;
import com.router.api.router.RouteReq;

/**
 * User: chw
 * Date: 2018/3/23
 */

public class RouteCenter {

    private Context mContext;
    private Handler mRouteHandler = new Handler(Looper.getMainLooper());

    private static class Holder {
        static RouteCenter INSTANCE = new RouteCenter();
    }

    public static RouteCenter getInstance() {
        return Holder.INSTANCE;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 执行路由逻辑
     * @param context
     * @param routeReq
     * @param callBack
     */
    public void execute(Context context, final RouteReq routeReq, final RouteLinkCallBack callBack) {
        redirectLinkReq(routeReq);

        routeReq.routeInfo = RouteMappingManager.getInstance().getRouteInfo(routeReq.linkPath);
        if (null != callBack && routeReq.routeInfo == null) {
            callBack.onFail(routeReq);
            return;
        }

        if (null != callBack && routeReq.routeInfo.isNeedLogin()) {
            callBack.onNeedLogin(routeReq);
            return;
        }

        final Context currentContext = (context == null) ? mContext : context;
        final Intent intent = new Intent(currentContext, routeReq.routeInfo.getDestClass());
        if (routeReq.bundle != null) {
            intent.putExtras(routeReq.bundle);
        }

        if (routeReq.flags != -1) {
            intent.setFlags(routeReq.flags);
        } else if (!(currentContext instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        mRouteHandler.post(new Runnable() {
            @Override
            public void run() {
                if (routeReq.requestCode > 0) {
                    ((Activity)mContext).startActivityForResult(intent, routeReq.requestCode);
                } else {
                    mContext.startActivity(intent);
                }

                if (null != callBack) {
                    callBack.onSuccess(routeReq);
                }
            }
        });
    }

    /**
     * 处理重定向link跳转
     * @param routeReq
     */
    private void redirectLinkReq(RouteReq routeReq) {
        ILinkRedirectProvider routeLinkProvider = RouteMappingManager.getInstance().getLinkRedirectProvider();
        if (routeLinkProvider != null) {
            if (routeReq.uri != null) {
                routeReq.uri = routeLinkProvider.redirectUri(routeReq.uri);
            } else if (!TextUtils.isEmpty(routeReq.linkPath)) {
                routeReq.linkPath = routeLinkProvider.redirectPath(routeReq.linkPath);
            }
        }

        if (routeReq.uri != null) {
            routeReq.linkPath = routeReq.uri.getPath();
        }
    }

    public <T> T routeProvider(Class<T> providerCls) {
        return (T) RouteMappingManager.getInstance().getModuleProvider(providerCls.getName());
    }
}
