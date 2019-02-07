# mwj-cms

#### 介绍
mwj-cms后台管理系统(孟伟进后台管理系统)

mwj-cms后台管理系统 是个人为防止知识的遗忘，总要做点什么来留住一些技术性的东西，同时，也方便查漏补缺， 于是做了这样一个东西，尽管同类型开源作品数不胜数，但只有自己亲自写下来，才能记忆深刻。

温馨提示： 个人拙作，请文明用语，愿大家共同进步！如果您觉得还不错就给个小星星吧O(∩_∩)O

在线演示：[http://60.205.177.13/](http://60.205.177.13/)


#### 安装部署教程
```
1. 准备正常的mysql数据库
2. 修改application-dev.yml中数据源的url,username,password
3. 部署jar mwj-cms-admin-1.0.jar到服务器
4. 启动成功后，默认使用80端口，访问http://服务器ip/
```

#### 目录结构
```
mwj-cms
├─doc  项目文档及工具
│
├─mwj-cms-admin   后台管理模块，Spring Boot
│  │
│  ├─java   代码
│  │  │
│  │  └─MwjCmsAdminApplication.java    启动类
│  │ 
│  └─resources 资源包
│     │
│     ├─templates   页面模板
│     │
│     ├─application.yml   Spring Boot 配置文件
│     │
│     └─application-dev.yml   Spring Boot 配置文件
│
│
├─mwj-cms-framework   后台管理框架模块
│  │
│  ├─java   代码
│  │  │
│  │  │─common   常量、枚举、异常、工具类
│  │  │
│  │  │─framework   框架主要技术集成
│  │  │
│  │  └─system    后台管理系统controller、entity、mapper、service
│  │ 
│  └─resources 资源包
│     │
│     ├─db  flyway数据库脚本
│     │
│     ├─files   工具资源文件，7z
│     │
│     ├─i18n   国际化资源文件
│     │
│     ├─mapper   mybatis mapper.xml文件
│     │
│     ├─static   静态资源文件。图片、js、css等
│     │
│     ├─templates   公用页面模板
│     │
│     ├─banner.txt
│     │
│     └─ValidationMessages_zh_CN.properties     Spring MVC后台数据验证资源
│
├─.gitignore
│
├─LICENSE
│
├─README.md
│
└─pom.xml 

```
#### 技术选型

类别|名称|描述
------|------|------
部署容器			 |   Docker										|	使用dockerfile文件构建docker镜像后部署应用jar到docker容器
接口文档			 |   Swagger、Swagger-ui							|	Controller接口API说明文档及接口测试
应用基础框架		 |   Spring Boot								|	Spring Boot 2.0以上版本，使用JDK 1.8以上版本
安全框架			 |   Spring Security							|	主要包含用户认证、菜单授权、xss过滤、csrf过滤等
模板引擎			 |   Thymeleaf									|	前台HTML页面渲染模板引擎
缓存框架			 |   EHCache 3									|	应用缓存框架，目前只在用户认证处使用
ORM框架			 |	 Mybatis、Mybatis-plus						|	操作数据库，方便的增删改查操作
数据源			 |	 Druid										|	数据源配置、监控，可配置多个数据源
应用服务器		 |	 Apache Tomcat								|	应用服务器
数据库			 |	 MySQL 8.0									|	MySQL 8.0数据库，使用docker容器运行
数据库版本控制	 |	 Flyway										|	统一数据库脚本升级管理
Web Service框架	 |	 Apache CXF									|	使用CXF发布和调用Web Service服务
调度框架			 |   Quartz										|	定时调度任务框架
发送邮件			 |   Javax Mail									|	应用发送邮件服务
压缩工具			 |   7z											|	压缩/解压缩工具，全格式支持
工具类库			 |   apache commons、Google Guava、fastJSON、JSoup、hutool			|	常用工具类库，避免重复造轮子
验证码			 |	kaptcha										|	生成验证码图片
前端框架/组件		 |   jquery、layui、bootstrap					|	jquery前端js库；layui前端UI库；bootstrap前端css库。
树组件			 |	zTree										|	前端树组件
图表组件			 |   echarts									|	各种饼图、柱状图等图表组件
字体图标			 |    Font Awesome、Glyphicons、Layui icon		|	支持Font Awesome、Glyphicons、Layui字体图标

## 演示图

<table>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2019/0207/132019_ffd53c1a_1644072.png"/></td>   
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2019/0207/132044_62f7b906_1644072.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2019/0207/132058_1e6ec69c_1644072.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2019/0207/132112_c86bdd95_1644072.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2019/0207/132523_f2aa4b22_1644072.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2019/0207/132142_480819e8_1644072.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2019/0207/132155_e1206597_1644072.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2019/0207/132207_a2525626_1644072.png"/></td>
    </tr>
	<tr>
        <td><img src="https://images.gitee.com/uploads/images/2019/0207/132552_7ca9f651_1644072.png"/></td>
    </tr>
    <tr>
        <td><img src="https://images.gitee.com/uploads/images/2019/0207/132235_0f042054_1644072.png"/></td>
    </tr>
</table>

#### 参与贡献

1. Fork 本仓库
2. 提交代码
3. 新建 Pull Request

