# routeTest 路由demo
* 自动注册  
  编译时apt扫描出被注解的activity和module provider汇总生成映射类文件, 通过aspectj注入 RouteMappingManager的
  init方法实现自动注册  
  ```
  @Aspect
  public final class Router$$moduledemo1$$ActivityMapping {
    @After("execution(* com.router.api.RouteMappingManager.initLinkMapping())")
    public void addRouterLink() {
      RouteMappingManager.getInstance().addLinkMapping("/module1/routeractivity3", RouteInfo.build("/module1/routeractivity3",
           TestRouterActivity3.class, "", true, -2147483648));
      RouteMappingManager.getInstance().addLinkMapping("/module1/routeractivity4", RouteInfo.build("/module1/routeractivity4",            TestRouterActivity4.class, "", false, -2147483648));
    }
  }  
  ```
***  
* 路由link跳转  
```
  //没有回调   
  Router.openLink("/module2/routeractivity2").execute(); 
  //带跳转回调          
  Router.openLink("/module2/routeractivity2").execute(this, new GNHRouteCallBack() {
            @Override
            public void onSuccess(RouteReq routeReq) {

            }

            @Override
            public void onFail(RouteReq routeReq) {

            }
        });
```
* 动态跳转  
```
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
```
***
* module之间调用  
```
     /** module之间通信，调用**/
     Router.openProvider(TestProvider.class).doTest();
```
***
