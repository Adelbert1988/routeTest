package demo.router.com.moduledemo4;

import android.app.Activity;
import android.os.Bundle;

import com.router.annotation.RouteLink;

/**
 * User: chw
 * Date: 2018/1/30
 */
@RouteLink("/module4/routeractivity6")
public class TestRouterActivity8 extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test_2);
    }


    private void initTest() {

    }
}
