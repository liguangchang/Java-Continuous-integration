## jdk1.8

### Functional Programming

### 特性

```
函数作为基本运算单位
函数可以作为变量
函数可以接受函数，可以返回函数
```

### lambda @FunctionalInterface

```
简化代码+类型推断 --> 参数+方法体
```

```
FunctionalInterface可以传入
接口实现类
lambda
符合方法签名的静态方法
符合方法签名的实例方法 实例类型被看作第一个参数类型
符合方法签名的构造方法 实例类型被看作返回类型
```

### 内置接口

Consumer<T>

Supplier<T>

Function<T,R>

Predicate<T>

UnaryOperation

BiFunction

### Method Reference

```
对象::实例方法名

类::静态方法名

类::实例方法名
```



### Stream

##### 特性

```
没有内部存储
惰性计算
支持并行
可以存储有限或无限个元素
可以转换为另一个stream
计算通常发生在最后结果的获取
支持过滤，查找，转换，汇总，聚合
```



##### 创建stream

```
现有序列转换为stream
Arrays.of（） 传入范型数组 
	#Stream<Integer> s=Steam.of(1,2,3); 指定元素
Collection.stream Set、List、Map、SortedSet
  #Stream<Integer> s =Arrays.stream(theArray) 数组
  #Stream<Integer> s =alist.stream(); collection
Stream.iterate(T,UnaryOperator<T>)
Stream.generate(Supplier<T> s)
```

```
Stream<T> s = Stream.generate(Supplier<T> s);无限序列
```

```
基本类型的Stream有IntStream、LongStream、DoubleStream
```

##### Stream.map()

```
将Stream的每个元素映射成另一个元素并生成一个新的Stream
将一种元素类型转换为另一种元素类型
```

##### Stream.filter()

```
转化Stream 过滤生成一个新的stream
peek 对象操作
```

##### Stream.reduce()

```
聚合方法 将每个元素依次作用于BiFunction 将结果合并
count()
max(Comparator<T> cp)
min(Comparator<T> cp)

#对于IntStream/LongStream/DoubleStream
sum()
average()
```

##### 排序

```
Stream<T> sorted()  按元素默认大小排序 必须实现comparable接口

Stream<T> sorted(Comparator<? super T> cp) 按指定的comparator比较的结果排序
```

#####  去重

```
Stream<T> distinct() 返回去除重复元素的新Stream

```

##### 截取

```
Stream<T> limit(long) 截取当前Stream个元素
Stream<T> skip(long） 跳过当前streamd的前N个元素
```

##### 合并

```
Stream<T> concat(Stream,Stream) 将两个Streama合并为一个Stream
```

##### 如果Stream元素是集合

```
s.flatMap() 把元素映射成stream在合并
a
```

##### 并行

```
将一个Stream转换为可以并行处理的Stream
s.parallel()
```

##### 判断

```
测试Stream元素是否满足
boolean allMarch(Predicate<? super T>)  所有元素满足测试条件
boolean anyMatch(Predicate<? super T>) 	至少一个元素满足测试条件
boolean noneMatch(Predicate<? super T>)
```

##### 循环处理

```
foreach(Consumer<? super T> action)
```

##### 类型转换

``` 
Object[] toArray() 转换为Object数组
A[] toArray(IntFunction<A[]>) 转换为A[]数组
<R,A> R collect(Collector<? super T,A,R> collector) 转换为集合
```

### optinal

```
Optional<T>类（java.util.Optional）是一个容器类，代表一个值存在或者不存在，本质上是一个包含可选值的包装类，所以optional类既可以含有对象也可以为空


```



```
常用方法：
Optional of(T t):创建一个Optional实例
Optional empty():创建一个空的Optional实例
Optional ofNullable(T t):若t不为null，创建Optional实例，否则创建空实例
isPresent():判断是否包括值
orElse(T t):如果调用对象包括值，返回該值，否则返回t
orElseGet(Supplier s):如果调用对象包含值，返回该值，否则返回s获取的值
map(Function f):如果有值对其处理，并返回处理后的Optional，否则返回Optonal.empty()
flatMap(Function mapper):与map类似，要求返回值必须是Optional

```

