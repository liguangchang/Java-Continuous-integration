# Nginx

```
反向代理服务器 http server
```

```
负载均衡
静态代理
限流
缓存
黑白名单
```

### 负载均衡

```
通过反向代理，将请求按照自定义策略分发到服务器上，达到负载的效果
我接触的都是tomcat

```

##### 实现负载均衡方式

###### 	轮询

```
按请求顺序分配服务器，比较均衡，不关服务器实际连接书和当前的系统负载
```

###### 	加权轮询

```
不用的服务器负载不同，通过权重让性能高负载低的服务器处理更多的请求。
```

###### 	ip_hash 

```
获取客户端ip 通过哈希函数计算到一个数值，进行取模运算得到要访问的服务器
ip_hash能解决session共享问题 但是可能服务器宕机就session丢失不够高可用
#TODO 一致性哈希算法/crc16
```

###### 	随机 

```
通过系统的随机算法，根据后端服务器的列表大小值来随机选取其中的一台服务器进行访问。
```

###### 	最小连接数

```
根据后端服务器当前的连接情况，动态地选取其中当前积压连接数最少的一台服务器来处理当前的请求，尽可能地提高后端服务的利用效率，将负责合理地分流到每一台服务器。
```

###### 	fair

```
(第三方）按后端服务器的响应时间来分配请求，响应时间短的优先分配。与weight分配策略类似
```



### 限流  

```
基于漏桶算法实现的，在高并发场景非常实用
limit_req_zone定义在http块中，$binary_remote_addr 表示保存客户端IP地址的二进制形式。
Zone定义ip状态和url访问频率的共享区域
zone=keyword标识区域的名字，以及冒号后面跟区域的大小 16000个ip地址的状态信息约为1mb
Rate定义最大请求速率
burst 排队大小 
nodelay 不限制单个请求间的时间
```

```
limit_req_zone $binary_remote_addr zone=mylimit:10m rate=100r/s
limit_req zone =mylimit burst=20 nodelay;
```

### 缓存

```
浏览器缓存，静态资源缓存 expire
loction ~ .* \.(|){
		expires 7d;
}
```

```
代理层缓存
```

### 黑白名单

```
不限流白名单
```

```
黑名单
```



### Nginx.conf

```
# 定义Nginx运行的用户 和 用户组 如果对应服务器暴露在外面的话建议使用权限较小的用户 防止被入侵
# user www www;
#Nginx进程数, 建议设置为等于CPU总核心数
worker_processes 8;
#开启全局错误日志类型
error_log /var/log/nginx/error.log info;
#进程文件
pid /var/run/nginx.pid;
#一个Nginx进程打开的最多文件描述数目 建议与ulimit -n一致
#如果面对高并发时 注意修改该值 ulimit -n 还有部分系统参数 而并非这个单独确定
worker_rlimit_nofile 65535;
events{
 #使用epoll模型提高性能
 use epoll;
 #单个进程最大连接数
 worker_connections 65535;
}
http{
 #扩展名与文件类型映射表
 include mime.types;
 #默认类型
 default_type application/octet-stream;
 sendfile on;
 tcp_nopush on;
 tcp_nodelay on;
 keepalive_timeout 65;
 types_hash_max_size 2048;
 #日志
 access_log /var/log/nginx/access.log;
 error_log /var/log/nginx/error.log;
 #gzip 压缩传输
 gzip on;
 gzip_min_length 1k; #最小1K
 gzip_buffers 16 64K;
 gzip_http_version 1.1;
 gzip_comp_level 6;
 gzip_types text/plain application/x-javascript text/css application/xml application/javascript;
 gzip_vary on;
 #负载均衡组
 #静态服务器组
 upstream static.zh-jieli.com {
 server 127.0.0.1:808 weight=1;
 }
 #动态服务器组
 upstream zh-jieli.com {
 server 127.0.0.1:8080;
 #server 192.168.8.203:8080;
 }
 #配置代理参数
 proxy_redirect off;
 proxy_set_header Host $host;
 proxy_set_header X-Real-IP $remote_addr;
 proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
 client_max_body_size 10m;
 client_body_buffer_size 128k;
 proxy_connect_timeout 65;
 proxy_send_timeout 65;
 proxy_read_timeout 65;
 proxy_buffer_size 4k;
 proxy_buffers 4 32k;
 proxy_busy_buffers_size 64k;
 #缓存配置
 proxy_cache_key '$host:$server_port$request_uri';
 proxy_temp_file_write_size 64k;
 proxy_temp_path /dev/shm/JieLiERP/proxy_temp_path;
 proxy_cache_path /dev/shm/JieLiERP/proxy_cache_path levels=1:2 keys_zone=cache_one:200m inactive=5d max_size=1g;
 proxy_ignore_headers X-Accel-Expires Expires Cache-Control Set-Cookie;
server{
 listen 80;
 server_name erp.zh-jieli.com;
 location / {
 index index; #默认主页为 /index
 #proxy_pass http://jieli;
 }
 location ~ .*\.(js|css|ico|png|jpg|eot|svg|ttf|woff) {
 proxy_cache cache_one;
 proxy_cache_valid 200 304 302 5d;
 proxy_cache_valid any 5d;
 proxy_cache_key '$host:$server_port$request_uri';
 add_header X-Cache '$upstream_cache_status from $host';
 proxy_pass http://static.zh-jieli.com;
 #所有静态文件直接读取硬盘
 # root /var/lib/tomcat7/webapps/JieLiERP/WEB-INF ;
 expires 30d; #缓存30天
 }
 #其他页面反向代理到tomcat容器
 location ~ .*$ {
 index index;
 proxy_pass http://zh-jieli.com;
 }
 }
 server{
 listen 808;
 server_name static;
 location / {
}
 location ~ .*\.(js|css|ico|png|jpg|eot|svg|ttf|woff) {
 #所有静态文件直接读取硬盘
 root /var/lib/tomcat7/webapps/JieLiERP/WEB-INF ;
 expires 30d; #缓存30天
 }
 }
}
```

### 跨域