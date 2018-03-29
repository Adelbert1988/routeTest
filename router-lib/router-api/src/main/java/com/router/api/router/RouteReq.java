package com.router.api.router;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.router.api.RouteCenter;
import com.router.api.model.RouteInfo;

/**
 * User: chw
 * Date: 2018/3/23
 */

public class RouteReq {

    public Uri uri;
    public String linkPath;

    public int flags = -1;
    public int requestCode = -1000;

    public Bundle bundle;

    public RouteInfo routeInfo;

    public RouteReq(String linkPath) {
        this.linkPath = linkPath;
    }

    public RouteReq(Uri uri) {
        this.uri = uri;
    }

    public RouteReq withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public RouteReq withBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public RouteReq withRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public void execute() {
        execute(null, null);
    }

    public void execute(Context context) {
        execute(context, null);
    }

    public void execute(RouteLinkCallBack callBack) {
        execute(null, callBack);
    }

    public void execute(Context context , RouteLinkCallBack callBack) {
        RouteCenter.getInstance().execute(context,this, callBack);
    }
}
