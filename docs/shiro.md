# shiro

### 主体

```
访问系统资源的对象 subject
```

### 安全管理器 

```
对主体进行认证授权 security
```

### 认证

``` 
Authenticator  认证器 
```

### 授权

```
Authorizer 授权器
```

### Realm

```
数据源 security通过realm获取用户权限信息
```

### 认证流程

```

创建 securityManager 
subject提交认证 security调用Authenticator查询subject信息
自定义realm继承AuthorizingRealm
1.执行subject.isPermitted("user:create")
2.securityManager调用Authorizer
3.Authorizer调用realm获取权限信息，解析权限字符串，校验是否匹配
// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		// 获取身份信息
		String username = (String) principals.getPrimaryPrincipal();
		// 根据身份信息从数据库中查询权限数据
		//....这里使用静态数据模拟
		List<String> permissions = new ArrayList<String>();
		permissions.add("user:create");
		permissions.add("user:delete");
		
		//将权限信息封闭为AuthorizationInfo
		SimpleAuthorizationInfo simpleAuthorizationInfo = new
                                                SimpleAuthorizationInfo();
		for(String permission:permissions){
			simpleAuthorizationInfo.addStringPermission(permission);
		}
		
		return simpleAuthorizationInfo;
	}

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //从token中 获取用户身份信息
        String username = (String) token.getPrincipal();
        //拿username从数据库中查询
        //....
        //如果查询不到则返回null
        if(!username.equals("zhang")){//这里模拟查询不到
            return null;
        }
          //获取从数据库查询出来的用户密码
        String password = "123";//这里使用静态数据模拟。。

        //返回认证信息
        SimpleAuthenticationInfo simpleAuthenticationInfo = new
                SimpleAuthenticationInfo(username, password, getName());

        return simpleAuthenticationInfo;
```

### 授权方式

```
编程式 if/else 判断是否有权限
注解 @requiresPermissions("权限标识")
jsp标签
<shiro: hasPermission name="权限标识">
	<菜单>
</shiro: hasPermission >
```

### 表单过滤器

```
FormAuthenticationFilter
subject没有认证请求loginurl认证，拦截取request中的username和password
过滤器调用realm传入token，realm认证时根据username查询用户信息（在user中存储，包括 userid、username、password、menus）。
如果查询不到，realm返回null，FormAuthenticationFilter向request域中填充一个参数（记录了异常信息） 
```

| 过滤器简称 | 对应的java类                                                 |
| ---------- | ------------------------------------------------------------ |
| anon       | org.apache.shiro.web.filter.authc.AnonymousFilter            |
| authc      | org.apache.shiro.web.filter.authc.FormAuthenticationFilter   |
| authcBasic | org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter |
| perms      | org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter |
| port       | org.apache.shiro.web.filter.authz.PortFilter                 |
| rest       | org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter |
| roles      | org.apache.shiro.web.filter.authz.RolesAuthorizationFilter   |
| ssl        | org.apache.shiro.web.filter.authz.SslFilter                  |
| user       | org.apache.shiro.web.filter.authc.UserFilter                 |
| logout     | org.apache.shiro.web.filter.authc.LogoutFilter               |

```
anon:例子/admins/**=anon 没有参数，表示可以匿名使用。

authc:例如/admins/user/**=authc表示需要认证(登录)才能使用，没有参数

roles：例子/admins/user/**=roles[admin],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，当有多个参数时，例如admins/user/**=roles["admin,guest"],每个参数通过才算通过，相当于hasAllRoles()方法。

perms：例子/admins/user/**=perms[user:add:*],参数可以写多个，多个时必须加上引号，并且参数之间用逗号分割，例如/admins/user/**=perms["user:add:*,user:modify:*"]，当有多个参数时必须每个参数都通过才通过，想当于isPermitedAll()方法。

rest：例子/admins/user/**=rest[user],根据请求的方法，相当于/admins/user/**=perms[user:method] ,其中method为post，get，delete等。

port：例子/admins/user/**=port[8081],当请求的url的端口不是8081是跳转到schemal://serverName:8081?queryString,其中schmal是协议http或https等，serverName是你访问的host,8081是url配置里port的端口，queryString

是你访问的url里的？后面的参数。

authcBasic：例如/admins/user/**=authcBasic没有参数表示httpBasic认证

 

ssl:例子/admins/user/**=ssl没有参数，表示安全的url请求，协议为https

user:例如/admins/user/**=user没有参数表示必须存在用户，当登入操作时不做检查

注：

anon，authcBasic，auchc，user是认证过滤器，

perms，roles，ssl，rest，port是授权过滤器
```

```
Jsp页面添加：
<%@ tagliburi="http://shiro.apache.org/tags" prefix="shiro" %>

标签名称	标签条件（均是显示标签内容）
<shiro:authenticated>	登录之后
<shiro:notAuthenticated>	不在登录状态时
<shiro:guest>	用户在没有RememberMe时
<shiro:user>	用户在RememberMe时
<shiro:hasAnyRoles name="abc,123" >	在有abc或者123角色时
<shiro:hasRole name="abc">	拥有角色abc
<shiro:lacksRole name="abc">	没有角色abc
<shiro:hasPermission name="abc">	拥有权限资源abc
<shiro:lacksPermission name="abc">	没有abc权限资源
<shiro:principal>	显示用户身份名称
 <shiro:principal property="username"/>     显示用户身份中的属性值
```

