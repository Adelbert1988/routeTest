# 路由方案
1. 通过自定义注解标记页面路由地址和模块provider服务  

	注解页面路由

	 ```java  
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
	```
	注解模块provider

	 ```java  
		@RouteProvider("/module1/test")
		public class Module1ProviderImpl implements Module1Provider {

		    @Override
		    public void init(Context context) {

		    }

		    @Override
		    public void doModule1Action() {

		    }
		}
	 ```
2. 自动注册  
  * 编译时Apt扫描出被注解的activity和provider, 使用Javapoet生成映射类文件  
  ```java  
	@Aspect
	public final class Router$$moduledemo4$$ActivityMapping {
	    @After("execution(* com.router.api.RouteMappingManager.initLinkMapping())")
	    public void addRouterLink() {
		RouteMappingManager.getInstance().addLinkMapping("/module4/routeractivity5", RouteInfo.build("/module4/routeractivity5", TestRouterActivity7.class, "", false, -2147483648));
		RouteMappingManager.getInstance().addLinkMapping("/module4/routeractivity6", RouteInfo.build("/module4/routeractivity6", TestRouterActivity8.class, "", false, -2147483648));
	    }
	}
  ```  
    
    
  ```java  
	@Aspect
	public final class Router$$moduledemo4$$ProviderMapping {
	  @After("execution(* com.router.api.RouteMappingManager.initProviderMapping())")
	  public void addRouterProvider() {
	    RouteMappingManager.getInstance().addProviderMapping("com.business.providers.Module4Provider", Module4ProviderImpl.class);
	  }
	}
  ```  
        
  * Aspectj 在编译class过程中织入所有module的路由映射类的初始化到 RouteMappingManager 的init方法，实现自动注册

  织入provider注册代码到路由地址管理中心  
  ```
  @After("execution(* com.router.api.RouteMappingManager.initLinkMapping())")
  ```  
    
  织入provider注册代码到路由地址管理中心  
  ```
  @After("execution(* com.router.api.RouteMappingManager.initProviderMapping())")
  ```
   
***  
  
3. 路由跳转  
builder模式封装路由请求RouteReq, 可携带参数bundle，requestCode, intentFlag, 路由返回回调
  
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
4. 动态拦截跳转  
根据服务端下发的逻辑动态替换路由跳转页面
  

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

5. 拦截器  
  * 自定义拦截器需实现InterceptProvider接口
  
	```  
	public interface InterceptProvider {
	
	    /**
	     * 拦截
	     * @param routeReq
	     * @return
	     */
	     boolean onIntercept(Context context, RouteReq routeReq);
	
	}
	```  
	例:登陆拦截器  
	  
	```
	public class LoginInterceptor implements InterceptProvider {

    @Override
    public boolean onIntercept(Context context, RouteReq routeReq) {
        //实现项目的登陆逻辑
        if (routeReq.routeInfo.isNeedLogin() && !UserManager.getInstance().isLogin()) {
            //go login activty
            return true;
        }
        return false;
    }
}
	```  
  * 添加拦截器  

	```
	RouterLinker.addInterceptor(new LoginInterceptor()); 
	```
	 
6. module之间调用  
对外暴露module服务接口，不暴露具体实现类，使用在library工程中定义Module暴露的服务接口找到module服务实现  
	
	```
	/** module之间通信，调用**/
	Router.openProvider(TestProvider.class).doTest();
	```  

  
  
    
      
      
