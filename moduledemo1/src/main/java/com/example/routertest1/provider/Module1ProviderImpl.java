package com.example.routertest1.provider;

import android.content.Context;

import com.business.providers.Module1Provider;
import com.router.annotation.RouteProvider;

/**
 * User: chw
 * Date: 2018/2/2
 */
@RouteProvider("/module1/test")
public class Module1ProviderImpl implements Module1Provider {

    @Override
    public void init(Context context) {

    }

    @Override
    public void doModule1Action() {

    }
}
