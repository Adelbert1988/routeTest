package com.example.routertest1.provider;

import android.content.Context;
import android.util.Log;

import com.business.providers.TestProvider;
import com.router.annotation.RouteProvider;

/**
 * User: chw
 * Date: 2018/3/28
 */
@RouteProvider("/model/asdf/")
public class TestProviderImpl implements TestProvider{
    @Override
    public void init(Context context) {

    }

    @Override
    public void doTest() {

        Log.d("myroter: ", "dotest");
    }
}
