package demo.router.com.moduledemo3;

import android.app.Activity;
import android.os.Bundle;

import com.business.providers.Module1Provider;
import com.router.annotation.RouteLink;
import com.router.api.Router;

/**
 * User: chw
 * Date: 2018/1/30
 */
@RouteLink("/module3/routeractivity5")
public class TestRouterActivity5 extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_test_7);
        Router.openProvider(Module1Provider.class).doModule1Action();
    }


    private void initTest() {

    }
}
