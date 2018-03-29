package com.example.routertest1;

import android.app.Activity;
import android.os.Bundle;

import com.router.annotation.RouteLink;

import demo.router.com.moduledemo1.R;

/**
 * User: chw
 * Date: 2018/1/30
 */
@RouteLink("/module1/routeractivity4")
public class TestRouterActivity4 extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_test_4);
    }


    private void initTest() {

    }
}
