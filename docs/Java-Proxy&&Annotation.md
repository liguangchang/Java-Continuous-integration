## Java 

[TOC]



### 1.注解

```java
所有的注解类型都是继承了java.lang.annotation.Annotation接口
```

##### 元注解

```
指定注解的生命周期以及作用目标信息
@Target 注解的作用目标 方法？类？属性？
@Retention 注解的生命周期
@Documented 注解是否应当背包含在JavaDoc文档中
@Inherited 是否允许子类继承该注解
```

##### @Target 指明注解作用目标

```
@Target(value={ElementType.?})
ElementType是一个枚举类型
ElementType.TYPE 允许作用在类，接口和枚举上
ElementType.FIELD 允许作用在属性字段上
ElementType.METHOD 允许作用在方法上
ElementType.PARAMETER 允许作用在参数上
ElementType.CONSTRUCTOR 允许作用在构造器上
ElementType.LOCAL_VARIABLE 允许作用在本地局部变量上
ElementType.PACKAGE 允许作用在包上
```

##### @Retention 指明注解的生命周期

```
@Retention(RetentionPolicy.RUNTIME)
RetentionPolicy 是一个枚举类型
RetentionPolicy.SOURCE：当前注解编译期可见，不会写入 class 文件
RetentionPolicy.CLASS：类加载阶段丢弃，会写入 class 文件
RetentionPolicy.RUNTIME：永久保存，可以反射获取
```

##### @Inherited

```
@Inherited 注解修饰的注解是具有可继承性的，也就说我们的注解修饰了一个类，而该类的子类将自动继承父类的该注解。
```

### 2.代理 

```
代理(Proxy)是一种设计模式,提供了对目标对象另外的访问方式，产生一个代理代替行使职能

```

#### 静态代理

```
1.需要定义接口或者父类
2.被代理对象与代理对象一起实现相同的接口或者是继承相同父类
代理对象需要与目标对象实现一样的接口,所以会有很多代理类,类太多.同时,一旦接口增加方法,目标对象与代理对象都要维护.
```

```java
接口
public interface UserDao {
     void save();
}
```

```java
实现类
public class UserDaoImpl implements UserDao {
    @Override
    public void save() {
        System.out.println("userDao out");
    }
}
```



```java
代理类
public class StaticProxy implements UserDao {

    UserDao target;

    public StaticProxy(UserDao userDao) {
        target = userDao;
    }

    @Override
    public void save() {
        System.out.println("静态代理前");
        target.save();
        System.out.println("静态代理后");

    }
}
```

```java
测试
public class TestStaticProxy {
    public static void main(String[] args) {
        testProxy();
        /**
         * out
         * 静态代理前
         * userDao out
         * 静态代理后
         */
    }

    public static void testProxy(){
        UserDao target = new UserDaoImpl();
        StaticProxy staticProxy = new StaticProxy(target);
        staticProxy.save();
    }
}
```

#### 动态代理

```
1.代理对象,不需要实现接口
2.代理对象的生成,是利用JDK的API,动态的在内存中构建代理对象(需要我们指定创建代理对象/目标对象实现的接口的类型)
```

##### jdk动态代理 （接口代理）

```
使用 newProxyInstance(ClassLoader loader, Class<>[] interfaces,InvocationHandler h )方法返回一个代理对象
classloader 指定使用哪个类加载器
class<>[] 通过接口指定生成哪个对象的代理
InvocationHandler 指定生成的代理要作什么
```

###### 代理类

```java
//实例工厂返回返回一个目标对象的代理对象
    public Object createProxyInstance(Object targetObject) {
        this.targetObject = targetObject;
        /**
         * 第一个参数设置代码使用的类装载器，一般采用跟目标相同的类装载器
         * 第二个参数设置代理类实现的接口，所以要求代理的目标对象必须实现一个类
         * 第三个参数设置回调对象，当代理对象的方法被调用时，会委派给该参数指定对象的invoke方法
         */
        return Proxy.newProxyInstance(this.targetObject.getClass().getClassLoader()
                , this.targetObject.getClass().getInterfaces(),
                (proxy, method, args) -> method.invoke(targetObject, args));
    }
```

###### 或者这样可以做一些拓展

```java
class JdkProxy implements InvocationHandler{
	Object target;


	public Object createProxyInstance(Object targetObj){
		target=targetObj;
		return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),
				this::invoke);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result;
		//拓展
		System.err.println("ss");
		result= method.invoke(target,args);
		//拓展
		System.err.println("ss");
		return result;

	}
}
```



###### 测试jdk代理

```java
测试
public static void testJdkProxy(){
        JDKProxyFactory jdkProxyFactory = new JDKProxyFactory(new UserDaoImpl());
        /**
         * 返回的是接口
         */
        UserDao proxyInstance = (UserDao) jdkProxyFactory.getProxyInstance();
        proxyInstance.save();
    }
```

##### cglib代理

```
子类代理，是基于asm框架（字节码操作和分析的框架，它可以用来修改一个已存在的类或者动态产生一个新的类），实现了无反射机制进行代理，利用空间来换取了时间，代理效率高于jdk ,它是在内存中构建一个子类对象从而实现对目标对象功能的扩展
```

```
Cglib包的底层是通过使用一个小而块的字节码处理框架ASM来转换字节码并生成新的类
代理的类不能为final,否则报错
目标对象的方法如果为final/static,那么就不会被拦截,即不会执行目标对象额外的业务方法.
```

###### 代理类没有实现接口

```java
public class UserDaoWithoutInterFace {
    public void save() {
        System.out.println("----UserDaoWithoutInterFace 输出!----");
    }
}
```

###### cglib代理工厂创建代理对象 调用方法

```java
public class CGlibProxyFactory implements MethodInterceptor {
    //维护目标对象
    private Object target;
    public CGlibProxyFactory(Object target) {
        this.target = target;
    }
    //给目标对象创建一个代理对象
    public Object getProxyInstance() {
        //1.工具类
        Enhancer en = new Enhancer();
        //2.设置父类
        en.setSuperclass(target.getClass());
        //3.设置回调函数
        en.setCallback(this);
        //4.创建子类(代理对象)
        return en.create();
    }
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("before CGlibProxyFactory...");
        //执行目标对象的方法
        Object returnValue = method.invoke(target, args);
        System.out.println("after CGlibProxyFactory...");
        return returnValue;
    }
}
```

###### 测试

```java
UserDaoWithoutInterFace target2 = new UserDaoWithoutInterFace();
CGlibProxyFactory cGlibProxyFactory = new CGlibProxyFactory(target2);
UserDaoWithoutInterFace dao = (UserDaoWithoutInterFace)cGlibProxyFactory.getProxyInstance();
        dao.save();
        /**
         * out
         * before CGlibProxyFactory...
         * ----UserDaoWithoutInterFace 输出!----
         * after CGlibProxyFactory...
         */
```

###### 总结

```java
jdk代理是要被代理的类实现接口的，主要就是生成一个代理对象代理行使职能，一个方法调用
通过Proxy.newProxyInstance（Classloader classloader,class<>[] interface,InvocationHandler handler）
代理实现方法是重写invoke()方法，代理对象调用方法，返回method.invoke(代理对象，参数)
```

```
cglib代理
#TODO Enhancer
```

