package com.example.routertest1;

import android.app.Activity;
import android.os.Bundle;

import com.business.providers.TestProvider;
import com.router.annotation.RouteLink;
import com.router.api.RouterLinker;

import demo.router.com.moduledemo1.R;

/**
 * User: chw
 * Date: 2018/1/30
 */
@RouteLink(value = "/module1/routeractivity3", isNeedLogin = true)
public class TestRouterActivity3 extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_test_3);
        RouterLinker.openProvider(TestProvider.class).doTest();
    }


    private void initTest() {
        RouterLinker.openLink("adsf").execute();
    }
}
