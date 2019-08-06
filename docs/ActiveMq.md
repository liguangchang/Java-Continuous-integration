# ActiveMq

### 作用

```
业务逻辑解耦，单一职能原则
服务之间解耦 服务启动顺序
```

### 消息类型

```
点对点 一个生产者一个消费者 类似发短信
发布订阅模式 一个生产者产生消息并发送，可以别多个订阅者消费
```

### 消息形式

```
StreamMessage java原始数据流
MapMessage 名称-值
TextMessage 字符串
ObjectMessage 序列化的对象
BytesMessage 字节的数据流
```

