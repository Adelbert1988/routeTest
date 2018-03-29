package demo.router.com.routertest;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.business.providers.TestProvider;
import com.router.api.Router;
import com.router.api.provider.ILinkRedirectProvider;
import com.router.api.router.RouteReq;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uri uri = Uri.parse("geinihua://geinihua.app/test/main/activity?appid=12&type=123");
        Log.d("myuri path: ", ""+ uri.getPath() + "   " + uri.getQuery());

        Router.init(this);

        /** module之间通信，调用**/
        Router.openProvider(TestProvider.class).doTest();

        /**
         * 注册动态拦截跳转
         */
        Router.registerLinkRedirect(new ILinkRedirectProvider() {

            @Override
            public String redirectPath(String originalPath) {
                return super.redirectPath(originalPath);
            }

            @Override
            public Uri redirectUri(Uri originalUri) {
                return super.redirectUri(originalUri);
            }
        });
    }

    public void goModuleAct2(View view) {
        Router.openLink("/module2/routeractivity2").execute();

        /*Router.openLink("/module2/routeractivity2").execute(this, new GNHRouteCallBack() {
            @Override
            public void onSuccess(RouteReq routeReq) {

            }

            @Override
            public void onFail(RouteReq routeReq) {

            }
        });*/
    }
}
