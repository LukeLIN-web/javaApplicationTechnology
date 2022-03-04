

2022年2月22日, 离毕业还有四个月, 上完java课已经一年多, 开始学习spring.  意识到课堂的东西实在是太浅薄, 真要学东西还得做项目. 不做项目, 就会一事无成. 不要小看b站网课, 有人一步步教学还是很有用的. 

https://www.bilibili.com/video/BV1rv411k7RD?p=9&spm_id_from=pageDriver

static , 放css等静态资源.

template, 前端引擎, 百叶香, 

原来pom 继承了parent的类, 就有这些依赖. 

springboot内嵌了tomcat

一个springboot只能有一个核心配置文件. 可以用yml, yaml, properties等. 

P9 yml 根据空格和制表符排版,   还有yaml的. 如果同时存在. yml 优先. 

系统学习很花时间,但是以后还是要学的.

P13多环境的切换. 设置多个配置文件,改主核心配置文件. 

`spring.profiles.active=dev`

P23 mybatis集成

建议在一开始建立项目的时候＋依赖, 不然每次都要重启

![](https://pica.zhimg.com/80/v2-24a305cb13dc3f8dde283d60d3953691_1440w.jpg)

**开放接口层**:可直接封装 Service 方法暴露成 RPC 接口;通过 Web 封装成 http 接口;进行 网关安全控制、流量控制等。

**终端显示层**:各个端的模板渲染并执行显示的层。当前主要是 velocity 渲染，JS 渲染， JSP 渲染，[移动端展示](https://www.zhihu.com/search?q=移动端展示&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A"1769210914"})等。

**Web 层**:主要是对访问控制进行转发，各类基本参数校验，或者不复用的业务简单处理等。
Service 层:相对具体的业务逻辑服务层。

**Manager 层**:通用业务处理层，它有如下特征:1. 对第三方平台封装的层，预处理返回结果及转化异常信息;2. 对Service层通用能力的下沉，如缓存方案、[中间件](https://www.zhihu.com/search?q=中间件&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A"1769210914"})通用处理;3. 与DAO层交互，对多个DAO的组合复用。

**DAO 层**:数据访问层，与底层 MySQL、Oracle、Hbase 进行数据交互。

阿里巴巴规约中的分层比较清晰简单明了，但是描述得还是过于简单了，以及service层和manager层有很多同学还是有点分不清楚之间的关系，就导致了很多项目中根本没有Manager层的存在。

2月25日, 继续学习spring,还是得看视频, 自己做没人教太难了, 才意识到之前老师教了我多少东西. 

阿里巴巴连接池, druid. 

自动访问mysql太难了, 太烦了.

unicode不给提示而是闪退(弱智软件), Access denied for user ‘root’@’localhost’ (using password yes) 

其实就是密码不对

`ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';` 密码是这个password. 所以每一步都要理解, 代码很科学, 很有逻辑, 对就是对, 错就是错. 

Unknown initial character set index '255' received from server. Initial client character set can be forced via the 'characterEncoding' property.

```java
connectionURL="jdbc:mysql://127.0.0.1:3306/miaosha?characterEncoding=utf8"
```

成功了!

mybatis, 我会了!!! 自动生成sql语句和java类大大节省了CRUD重复语句的量 ,自动生成DO的方法

exmple, 就是复杂的语句实现复杂的查询, 不过据说用到的不多, 

springboot扫描mybatis一些文件. 

`@SpringBootApplication` 注解, app托管给spring. 指定为主启动类 

根目录下的包依次扫描, 注解的方式自动发现 service component 这些spring特定的注解.

能力还是有很多可迁移的, 所以多做项目很重要, 不学无术. 学的越多越容易理解.

2月26日

dataobject 完全一一映射. 没有逻辑部分, 

service不能直接透出dataobject, 要有一个model 概念, 

密码不能直接到前端, 利用模型组合, `BeanUtils.copyProperties(userModel, userVo);` 

问题: 没有复制过来, 原因是  没有setter方法, 就不会自动接受, BeanUtils.copyProperties 可以自动调用setter和getter

service层组装模型, 

#### 3-2 定义通用的返回对象

前端拿到json的string, 怎么归一化处理异常?

现在只有默认的错误码500.

怎么返回一个有意义的错误信息?

利用CommonReturnType 类

handlerException 可以放base controller, 

以前用过一些, 但是没有系统地整理过, 上课的作用就是系统整理学习. 自己瞎搞就是半吊子. 

返回json对象,  定义enum 统一管理错误码, 处理未知异常

2月27日

redis有过期时间, 自动覆盖

otp 和手机号关联, 用http session.

拿到 HttpServletRequest, 通过bean方式注入, 是一个单例模式, 怎么支持多个用户并发? 本质是一个proxy, 内部拥有threadlocal方式的map,去让用户在每个线程中，处理自己对应的request，并且有threadlocal清除的机制.

3月2日

用ajax请求, 前后端分离, 而不是post,

`jQuery(document).ready(*function*()` 渲染好了再做. 

3月3日

跨域问题,

点击了之后没有, 就是浏览器不能执行其他网站的jsp,对象无法获取 Ajax请求发送不出去. 请求的服务器是localhost, ajax 的域是本地文件,认定回调的域不同.

加一个注解即可 `@crossorigin`.

今天学习了css, href, rel 使用css样式控制, 有模板很方便. 

html可以引用css样式, 
