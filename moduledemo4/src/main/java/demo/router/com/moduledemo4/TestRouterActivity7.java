package demo.router.com.moduledemo4;

import android.app.Activity;
import android.os.Bundle;

import com.router.annotation.RouteLink;

/**
 * User: chw
 * Date: 2018/1/30
 */
@RouteLink("/module4/routeractivity5")
public class TestRouterActivity7 extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_test_1);
    }


    private void initTest() {

    }
}
