package com.example.routertest2.provider;

import android.content.Context;

import com.business.providers.Module2Provider;
import com.router.annotation.RouteProvider;

/**
 * User: chw
 * Date: 2018/2/2
 */
@RouteProvider("/module2/")
public class Module2ProviderImpl implements Module2Provider {
    @Override
    public void init(Context context) {

    }

    @Override
    public void doModule2Action() {

    }
}
