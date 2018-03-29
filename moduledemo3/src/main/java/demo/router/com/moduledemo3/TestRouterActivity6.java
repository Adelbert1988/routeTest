package demo.router.com.moduledemo3;

import android.app.Activity;
import android.os.Bundle;

import com.router.annotation.RouteLink;

/**
 * User: chw
 * Date: 2018/1/30
 */
@RouteLink("/module3/routeractivity6")
public class TestRouterActivity6 extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test_6);
    }

}
