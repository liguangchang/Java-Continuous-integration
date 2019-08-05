# servlet

### 生命周期

```
serlet类加载->实例化->服务->销毁

servlet接收到请求

创建httpRequest对象将请求信息封装到对象中

servlet容器创建一个httpResponse对象

servlet容器调用httpservlet对象的service方法，将httpRequest和httpResponse传给httpServlet

httpservlet调用httpRequest对象的方法获取请求信息

httpServlet调用httpResponse对象的方法生成响应数据

servlet容器把httpservlet的响应结果传给client
```

### get&&post

```
get把参数放在url中，post在request body中比较安全
get请求参数会被把保存在浏览器记录中，post不会保存
get请求在url有长度限制，post没有
get请求可被缓存
```

```
OPTIONS 返回服务器所支持的请求方法
GET 向服务器获取指定资源HEAD 与GET一致，只不过响应体不返回，只返回响应头
POST 向服务器提交数据，数据放在请求体里PUT 与POST相似，只是具有幂等特性，一般用于更新DELETE 删除服务器指定资源TRACE 回显服务器端收到的请求，测试的时候会用到这个
CONNECT 预留，暂无使用
```

### post&&get过程

```
post请求的过程：
（1）浏览器请求tcp连接（第一次握手）
（2）服务器答应进行tcp连接（第二次握手）
（3）浏览器确认，并发送post请求头（第三次握手，这个报文比较小，所以http会在此时进行第一次数据发送）
（4）服务器返回100 Continue响应
（5）浏览器发送数据
（6）服务器返回200 OK响应
get请求的过程：
（1）浏览器请求tcp连接（第一次握手）
（2）服务器答应进行tcp连接（第二次握手）
（3）浏览器确认，并发送get请求头和数据（第三次握手，这个报文比较小，所以http会在此时进行第一次数据发送）
（4）服务器返回200 OK响应
```

### 取参

```
get  
@RequestParam(require=true(name="",defaultValaue=“”)) 静态
@PathVariable（name=""） 动态
post
@RequestBody
```

### jsp

#### 内置对象

```
request 一次请求，封装http请求信息 头信息，系统信息，请求方式，请求参数
response 
session 服务器为每一个用户自动创建的 内部使用map类存储，value可以是复杂对象
application 系统全局变量，保存子啊服务器知道服务器关闭
out 用于在浏览器内输出信息，并在管理应用服务器上的输出缓冲区
pageContext 可以取任何范围的参数，可以获取 out,request,response,session,application
config 获取服务器信息，通过pageContext.getServletConfig（）获取，servlet初始化将信息通过config对象传递给servlet,可以在web.xml文件为应用程序初始化参数
page 当前页面有效，代表jsp本身
exception 显示异常信息，只有在包含 isErrorPahr="true"的页面中才可以被使用

```

```
保存数据:
Context.setAttribute("内容");//默认保存到page域  
pageContext.setAttribute("内容",域范围常量);//保存到指定域中  
//四个域常量  
PageContext.PAGE_SCOPE  
PageContext.REQUEST_SCOPE  
PageContext..SESSION_SCOPE  
PageContext.APPLICATION_SCOPE  
获取数据:
pageContext.getAttribute("内容");  
pageContext.getAttribute("name",域范围常量);

//自动在四个域中搜索数据 pageContext.findAttribute("内容");//在四个域中自动搜索数据,顺序:page域->request域->session域->application域(context域)

域作用范围:
page域:    只能在当前jsp页面使用                (当前页面)  
request域: 只能在同一个请求中使用               (转发)  
session域: 只能在同一个会话(session对象)中使用  (私有的)  
context域: 只能在同一个web应用中使用            (全局的)
```

