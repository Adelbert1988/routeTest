package demo.router.com.moduledemo3.provider;

import android.content.Context;

import com.business.providers.Module3Provider;
import com.router.annotation.RouteProvider;

/**
 * User: chw
 * Date: 2018/2/2
 */
@RouteProvider("/module3/")
public class Module3ProviderImpl implements Module3Provider {
    @Override
    public void init(Context context) {

    }

    @Override
    public void doModule3Action() {

    }
}
