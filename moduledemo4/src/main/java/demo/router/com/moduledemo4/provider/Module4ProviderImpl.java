package demo.router.com.moduledemo4.provider;

import android.content.Context;

import com.business.providers.Module4Provider;
import com.router.annotation.RouteProvider;

/**
 * User: chw
 * Date: 2018/2/2
 */
@RouteProvider("/module4/")
public class Module4ProviderImpl implements Module4Provider {
    @Override
    public void init(Context context) {

    }

    @Override
    public void doModule4Action() {

    }
}
