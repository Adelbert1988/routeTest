package com.example.routertest2;

import android.app.Activity;
import android.os.Bundle;

import com.router.annotation.RouteLink;

/**
 * User: chw
 * Date: 2018/1/30
 */
@RouteLink("/module2/routeractivity1")
public class TestRouterActivity1 extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_test_1);
    }


    private void initTest() {

    }
}
