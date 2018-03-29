package com.example.routertest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.router.annotation.RouteLink;
import com.router.api.RouteMappingManager;
import com.router.api.Router;
import com.router.api.model.RouteInfo;

/**
 * User: chw
 * Date: 2018/1/30
 */
@RouteLink("/module2/routeractivity2")
public class TestRouterActivity2 extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test_2);
    }


    public void goModuleAct6(View view) {
        Router.openLink("/module3/routeractivity6").execute();
    }
}
