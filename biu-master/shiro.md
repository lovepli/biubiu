
-shiro常用注解
@RequiresAuthentication

验证用户是否登录，等同于方法subject.isAuthenticated() 结果为true时。

@RequiresUser

验证用户是否被记忆，user有两种含义：

一种是成功登录的（subject.isAuthenticated() 结果为true）；

另外一种是被记忆的（subject.isRemembered()结果为true）。

@RequiresGuest

验证是否是一个guest的请求，与@RequiresUser完全相反。

 换言之，RequiresUser  == !RequiresGuest。

此时subject.getPrincipal() 结果为null.

@RequiresRoles

例如：@RequiresRoles("aRoleName");

  void someMethod();

如果subject中有aRoleName角色才可以访问方法someMethod。如果没有这个权限则会抛出异常AuthorizationException。

@RequiresPermissions
例如： @RequiresPermissions({"file:read", "write:aFile.txt"} )
  void someMethod();
要求subject中必须同时含有file:read和write:aFile.txt的权限才能执行方法someMethod()。否则抛出异常AuthorizationException。
