# Spring Security Oauth2 从零到一完整实践

![](https://img.hacpai.com/bing/20180817.jpg?imageView2/1/w/960/h/540/interlace/1/q/100) 

很久没有写一篇长文章了，自己说起来其实年初换成 solo 到现在，写的让自己满意的技术性文章也就只有 [spring boot restful API 从零到一完整实践](https://echocow.cn/articles/2019/01/05/1546684795983.html) 这篇了，其他的其实都是只属于自己比较容易理解的笔记和记录而已。想想年中了，还是需要写上这么一篇实践性文章的。这段时间比较折磨自己的，莫过于就是 [spirng security oauth2](https://spring.io/projects/spring-security-oauth) 了，自己折腾了很久，也算是学会了一些吧，按照原来的方式，写了一篇文章。前面也写过 [spring boot security oauth2  构建简单安全的 restful api](https://echocow.cn/articles/2019/01/22/1548148450889.html)，但是太过于基础并且那时候自己也有很多不懂，现在实践了很多，有了更加深入的了解，记录一下顺便分享给大家。

> github 地址：[spring-security-oauth2-demo](https://github.com/lizhongyue248/spring-security-oauth2-demo)
>
> 博客地址：[echocow.cn](https://echocow.cn)


[TOC]

# 系列文章

1. [较为详细的学习 oauth2 的四种模式其中的两种授权模式](https://echocow.cn/articles/2019/07/14/1563082088646.html)
2. [spring boot oauth2 自动配置实现](https://echocow.cn/articles/2019/07/14/1563082247386.html)
3. [spring security oauth2 授权服务器配置](https://echocow.cn/articles/2019/07/14/1563096109754.html)
4. [spring security oauth2 资源服务器配置](https://echocow.cn/articles/2019/07/20/1563611848587.html)
5. spring security oauth2 自定义授权模式（手机、邮箱等）
6. spring security oauth2 踩坑记录

原本打算全部写完一起发的，但是才写到第三点，就已经上万字了，所以还是觉得分系列发吧～

# 预备知识

具备以下基础知识能够方便你更好的阅读本篇文章

- spring 基础
- spring boot web 的使用与配置
- spring security 的使用与配置
- postman 的使用
- idea、maven、lombok 的使用与配置

# Oauth2

学习一项新的东西之前，我们要先了解一下他为我们解决了哪些事，能够带来什么样的便利，而在 IT 行业，了解一个东西最简单的方式就是去他的官网了解，所以我们先去官网了解一下这个协议：[Oauth2](https://oauth.net/)

> An **open protocol** to allow **secure authorization**  in a **simple** and **standard** method from web, mobile and desktop applications.

一个允许从Web、移动和桌面应用程序简单和标准方法进行安全授权的开放协议。

> The OAuth 2.0 authorization framework enables third-party
> applications to obtain **limited** access to a web service.

OAuth 2.0 授权框架使第三方应用程序能够获得对 Web 服务的有限访问权限。

从官网的解释就可以知道它可以完成如下两件事：

1. 对你的应用站点进行安全授权
2. 使第三方应用程序能够获得对 Web 服务的有限访问权限

我们这篇教程就是通过 spring security oauth2 来完成这么两件事。我们来详细了解一下这个协议，首先了解什么要使用 oauth2。我们以 web 为例来进行了解。

## 传统应用

在我们传统的 web 应用中，我们的前端页面和后端的逻辑都是一起部署的，大概流程如下：



![传统模式](https://resources.echocow.cn/file/2019/07/08/%E6%B7%B1%E5%BA%A6%E6%88%AA%E5%9B%BE_%E9%80%89%E6%8B%A9%E5%8C%BA%E5%9F%9F_20190708190354.png)

当我们发送一个请求的时候，直接先发给后端处理，后端处理完成后将数据发送给前端，然后前端渲染，再交给用户，所以有了模板引擎这个东西，例如 jsp、thymeleaf、freemarker 这些，都是这样的流程。而这些个东西最为重要的就是 session，你可以通过存储在 session 里面的东西对他进行授权/认证等操作，大概如下：

![授权](https://resources.echocow.cn/file/2019/07/08/%E6%B7%B1%E5%BA%A6%E6%88%AA%E5%9B%BE_%E9%80%89%E6%8B%A9%E5%8C%BA%E5%9F%9F_20190708194101.png)

## 现在的应用

那么现在我们的应用是什么样的呢？现在的前端已经不再是只有 html、css、javascript 了，也不再是 bootstrap 的天下，也没有 jquery 一出，万人空巷了。前端项目组建工程化，已经能够完整的独立成为一个工程化的项目了。所以我们现在前后端是完全分离的，**前后端各司其职，前端完成前端的事，只做页面，后端完成后端的事，只做逻辑和数据库操作，完全两个独立的引用，通过接口进行交互**，那么我们的大概流程如下：

![前后端分离](https://resources.echocow.cn/file/2019/07/08/%E6%B7%B1%E5%BA%A6%E6%88%AA%E5%9B%BE_%E9%80%89%E6%8B%A9%E5%8C%BA%E5%9F%9F_20190708191537.png)

用户通过浏览器请求前端应用的页面，然后页面里面加载请求到数据，再渲染页面。那么现在的授权没有 session 了，前后端是完全独立的两个项目了，我们要怎么进行认证授权呢？对于一个受保护的应用来说，他的请求流程如下：

![现在](https://resources.echocow.cn/file/2019/07/08/%E6%B7%B1%E5%BA%A6%E6%88%AA%E5%9B%BE_%E9%80%89%E6%8B%A9%E5%8C%BA%E5%9F%9F_20190708203251.png)

在这个流程中，我们后端应用其实变成了两个，一个是授权服务器一个是资源服务器，**当然你完全可以简爱嗯他们两个同时写在一个之中**。单独提出来的好处是什么呢？最主要的一点就是上面提到的 **使第三方应用程序能够获得对 Web 服务的有限访问权限**，简单的说就是能够更加方便的另外一个应用接入。当你写好一个授权服务器以后，其他应用就可以共用这个授权服务器，他们就作为资源服务器亦或是客户端即可。

## 角色

在这个协议中，我们需要明确一个 **角色** 的概念，在前面的和传统应用的对比中，我们提到了资源服务器和授权服务器，这就是其中两个角色，在 Oauth2 中，总共有四种角色：



| 名称       | 英文名               | 描述                                                         | web例子      |
| ---------- | -------------------- | ------------------------------------------------------------ | ------------ |
| 资源所有者 | resource owner       | 能够授予对受保护资源的访问权的实体。当资源所有者是一个人时，它就是用户。 | 用户         |
| 资源服务器 | resource server      | 承载受保护资源的服务器，能够使用访问令牌接受和响应受保护资源请求。 | 后端资源数据 |
| 客户端     | client               | 代表资源所有者及其授权发出受保护资源请求的应用程序。“客户端” 并不意味着任何特定的实现特征(例如，应用程序是否在服务器、桌面或其他设备上执行)。 | 前端应用     |
| 授权服务器 | authorization server | 在成功认证资源所有者并获得授权后，服务器向客户端发出访问令牌。 | 后端授权     |

而授权服务器可以是与资源服务器**相同的服务器**或**单独的服务器**。 单个授权服务器可以发出由**多个资源服务器接受的访问令牌**。

流程图大概如下：



```text
     +--------+                               +-----------------+
     |        |--（A）------- 授权请求 -------->|                 |
     |        |                               | 资源所有者（用户） |
     |        |<-（B）------- 授权许可 ---------|                 |
     |        |                               +-----------------+
     |        |
     |        |                               +-----------------+
     |        |--（C）------- 授权许可 -------->|                 |
     | 客户端  |                               |  授权服务器（1    |
     |        |<-（D）----- Access Token ----）|                 |
     |        |                               +-----------------+
     |        |
     |        |                               +-----------------+
     |        |（-（E）---- Access Token ----->|                 |
     |        |                               |   资源服务器（2   |
     |        |<-（F）---- 获取受保护的资源 -----|                 |
     +--------+                               +-----------------+

```

图中所示的 抽象 OAuth 2.0 流程描述了四个角色之间的交互，包括以下步骤：

（A）客户机请求资源所有者（用户）的授权。授权请求可以直接发送给资源所有者(如图所示)，最好通过作为中介的授权服务器间接发送。**简单地说，用户点击登录，会转到登录页面显示给用户。**

（B）客户端接收授权许可，这是表示资源所有者授权的凭据，使用 Oauth2 规范中定义的四种授权类型之一或使用扩展授权类型表示。授权授予类型取决于客户机用于请求授权的方法和授权服务器支持的类型。**简单地说，选择 oauth2 中四种授权模式进行授权。**

（C）客户端通过向授权服务器进行认证并呈现授权授权来请求访问令牌。**简单地说，客户端会向授权服务器使用前面选择的四种方式之一请求认证。**

（D）授权服务器对客户端进行身份验证并验证授权授予，如果有效，则发出访问令牌。**简单地说，授权成功发放令牌。**

（E）客户端从资源服务器请求受保护的资源，并通过呈现访问令牌进行身份验证。**简单地说，携带 令牌 请求资源服务器。**

（F）资源服务器验证访问令牌，如果有效，则为请求服务。**简单地说，如果令牌有效，就允许访问资源。**



（1）授权服务器可以只有一台，一台授权可以发放多个资源服务器。

（2）资源服务器需要关联一台授权服务器作为资源的保护和认证。



最为重要的部分为 B 中的 授权许可，它是代表资源所有者的授权（访问其受保护的资源）的**凭据**，客户端使用该授权来获得访问令牌。该规范定义了四种授权类型——**授权代码、隐式、资源所有者密码凭证和客户端凭证**——以及用于定义其他类型的可扩展性机制（自定义授权）。



## 四种授权模式

客户端必须得到用户的授权（authorization grant），才能获得令牌（access token）。OAuth 2.0定义了四种授权方式如下：

- 授权码模式（authorization code）
- 密码模式（resource owner password credentials）
- 简化模式（implicit）
- 客户端模式（client credentials）
- 扩展模式（extension）

最为常用的为第一、二种，我们这篇文章也只会完成第一二种，四种具体请参考 [阮一峰 理解OAuth 2.0](http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html) ，请注意详细看文章的 **名词定义** 模块。阮一峰老师的文章已经写的很清楚了，但是我依旧还是需要指明一下我们即将开始的第一二种的 api 设计。

> **注意：以下 api 设计为 spring security 提供实现，并不是 oauth2 的标准 api 实现**

不过在那之前，我们先来了解一下 **客户端的加密**

## 客户端加密

在 spring security oauth 中，推荐加密我们的客户端信息，客户端和授权服务器建立适合授权服务器安全要求的客户端认证方法。授权服务器可以接受满足其安全要求的任何形式的客户端身份验证。一般来说我们使用的是 **密码验证** 的方式加密我们的客户端信息。

推荐的方式是使用 HTTP Basic ，我们需要设置以下参数，当设置成功以后将客户端凭证加密存放在请求头中去请求授权信息，参数如下：

| 参数名称      | 是否必填 | 描述                                                         |
| ------------- | -------- | ------------------------------------------------------------ |
| client_id     | REQUIRED | 客户端 id                                                    |
| client_secret | REQUIRED | 客户端密码，如果客户机secret是空字符串，则客户机可以省略该参数 |

当我们请求的时候，需要设置相应的客户端认证信息，并存放在请求头中，设置方法如下：

```
 Authorization: Basic client_id：client_secret base64编码
 eg:
 client_id:web
 client_secret:secret
 加密“web:secret” 得到 “QmFzaWMgd2ViOnNlY3JldA==”
 授权请求头中需要携带如下键值对：
 Authorization: Basic QmFzaWMgd2ViOnNlY3JldA==
```

这是保证客户端安全十分重要的一环，强烈推荐对客户端进行加密！

### 授权码模式

他是一种流程最为严密，安全性最高的授权模式，主要为以下几个步骤：

> 注意：以下所有请求都必须在请求头中携带上一点中的客户端加密信息！

1. 用户进入应用，携带一个 *重定向应用地址* 到 **授权服务器** 进行登录，在登录成功并且用户同意授权以后，授权服务携带一个生成的 **授权码** 重定向到指定的 *重定向应用地址* 。
2. 应用携带接收到的 **授权码** 再次去请求 **授权服务器**，在验证成功后，验证服务器下发 token。

所以需要两个请求，在 spring security oauth2 中，api 如下，我们将这些 api 称为 *端点*：

### 授权端点

- /oauth/authorize：授权端点，通过此端点跳转到  **授权服务器** 进行认证，完成第一个请求。携带如下参数：

| 参数名称      | 是否必填    | 描述                                                         |
| ------------- | ----------- | ------------------------------------------------------------ |
| response_type | REQUIRED    | 必须为 code                                                  |
| client_id     | REQUIRED    | 客户端的 id                                                  |
| redirect_uri  | OPTIONAL    | 获取授权码后重定向地址                                       |
| scope         | OPTIONAL    | 申请的权限范围                                               |
| state         | RECOMMENDED | 客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值，推荐。 |

授权成功的情况，会携带以下两个参数重定向到到 **redirect_uri** 中：

| 参数名称 | 是否必有 | 描述                                                         |
| -------- | -------- | ------------------------------------------------------------ |
| code     | REQUIRED | 授权服务器生成的授权代码。授权代码必须在发布后不久过期，以降低泄漏的风险。最大授权代码生命周期为10分钟 |
| state    | REQUIRED | 如果上一步中提供 `state` 参数，会原封不动地返回这个值。      |

>  注意：官网中给出的解释 code 有 RECOMMENDED 推荐的情况，但是我没找到如何使用，所以没写。

授权失败的情况分为两种

1. 如果请求由于重定向URI丢失、无效或不匹配而失败，或者如果客户端标识符丢失或无效，授权服务器应通知资源所有者错误，并且**不得自动将用户代理重定向到无效的重定向URI**。
2. 如果资源所有者拒绝访问请求，或者如果请求由于除了丢失或无效重定向URI以外的原因而失败，则授权服务器通过使用 `application/x-www-form-urlencoded` 格式**向重定向 URI 的查询组件添加以下参数来通知客户端**，参数如下：(对于 spring ，目前没有遇到 error_uri 属性)

| 参数名称          | 是否必有 | 值                        | 描述                                                         |
| ----------------- | -------- | ------------------------- | ------------------------------------------------------------ |
| error             | REQUIRED | invalid_request           | 请求缺少必需的参数，包括无效的参数值，不止一次地包含参数，或者存在其他形式的异常。 |
|                   |          | unauthorized_client       | 未授权客户端使用此方法请求授权代码。                         |
|                   |          | access_denied             | 资源所有者或授权服务器拒绝了该请求。                         |
|                   |          | unsupported_response_type | 授权服务器不支持使用此方法获取授权代码。                     |
|                   |          | invalid_scope             | 请求的作用域无效、未知或格式不正确。                         |
|                   |          | server_error              | 授权服务器遇到意外情况，无法满足请求。(此错误代码是必需的，因为500内部服务器错误HTTP状态代码不能通过HTTP重定向返回给客户端。) |
|                   |          | temporarily_unavailable   | 由于服务器暂时过载或维护，授权服务器当前无法处理该请求。(此错误代码是必需的，因为503服务不可用的HTTP状态代码不能通过HTTP重定向返回给客户端。) |
| error_description | OPTIONAL | -                         | 提供附加信息的人类可读ASCII [USASCII]文本，用于帮助客户端开发人员理解所发生的错误。 |
| error_uri         | OPTIONAL |                           | 一种带有错误信息的可读网页的URI标识，用于向客户端开发人员提供有关错误的附加信息。 |

### 令牌端点

- /oauth/token：令牌端点，通过上一步获取到的 **授权码** 验证与生成令牌，完成第二个请求，携带如下参数：

| 参数名称     | 是否必填    | 描述                                         |
| ------------ | ----------- | -------------------------------------------- |
| grant_type   | REQUIRED    | 使用的授权模式，值固定为"authorization_code" |
| code         | REQUIRED    | 上一步获得的授权码                           |
| redirect_uri | REQUIRED    | 重定向URI，必须与上一步中的该参数值保持一致  |
| client_id    | REQUIRED    | 客户端的 id                                  |
| scope        | RECOMMENDED | 授权范围，必须与第一步相同                   |

如果访问令牌请求有效且经过授权，授权服务器将发出访问令牌和可选的刷新令牌，可以得到如下响应参数：

| 参数名称      | 是否必有    | 描述                                                  | 是否有实现   |
| ------------- | ----------- | ----------------------------------------------------- | ------------ |
| access_token  | REQUIRED    | 授权服务器颁发的访问令牌                              | 是           |
| token_type    | REQUIRED    | 令牌类型，该值大小写不敏感，可以是bearer类型或mac类型 | 是           |
| expires_in    | RECOMMENDED | 过期时间，单位为秒                                    | 是           |
| refresh_token | OPTIONAL    | 表示更新令牌，用来获取下一次的访问令牌                | 是，需要设置 |
| scope         | OPTIONAL    | 权限范围，如果有，则与客户端申请的范围一致            | 是           |

如果请求客户端身份验证失败或无效，授权服务器将返回错误响应，授权服务器使用HTTP 400（错误请求）状态代码进行响应（除非另有说明），并在响应中包含以下参数：

| 参数名称          | 是否必有 | 值                     | 描述                                                         |
| ----------------- | -------- | ---------------------- | ------------------------------------------------------------ |
| error             | REQUIRED | invalid_request        | 请求缺少必需的参数，包含不受支持的参数值(授权类型除外)，重复参数，包含多个凭据，使用多个机制来验证客户端，或者格式不正确。 |
|                   |          | invalid_client         | 客户端身份验证失败(例如，未知客户端、不包含客户端身份验证或不支持的身份验证方法)。授权服务器可以返回一个超文本传输协议401(未授权)状态码，以指示支持哪些超文本传输协议认证方案。如果客户端试图通过“授权”请求头字段进行身份验证，授权服务器必须用一个HTTP<br/>401(未授权)状态代码进行响应，并包括与客户端使用的身份验证方案相匹配的“WWW-Authenticate”响应头字段。 |
|                   |          | invalid_grant          | 所提供的授权授予(例如，授权代码、资源所有者凭证)或刷新令牌无效、过期、已撤销、不匹配授权请求中使用的重定向URI，或已向其他客户机发出。 |
|                   |          | unauthorized_client    | 经过身份验证的客户端无权使用此授权授权类型。                 |
|                   |          | unsupported_grant_type | 授权服务器不支持授权授权类型。                               |
|                   |          | invalid_scope          | 请求的范围无效、未知、格式错误或超出了资源所有者授予的范围。 |
| error_description | OPTIONAL | -                      | 提供附加信息的人类可读ASCII [USASCII]文本，用于帮助客户端开发人员理解所发生的错误。 |
| error_uri         | OPTIONAL | -                      | 一种带有错误信息的可读网页的URI标识，用于向客户端开发人员提供有关错误的附加信息。 |

## 密码模式

这种模式可以理解成我们普通应用的用户名密码登录，在第三方接入的时候不建议使用这种模式，但是如果是自己的应用，那么这种模式是最为简单方便快捷的了。步骤只有一个：

> 注意：以下所有请求都必须在请求头中携带上面所说的客户端加密信息！

- 用户携带用户名密码请求授权服务器，验证通过后下发令牌

他只需要一个请求，所以她只有一个令牌端点：

### 令牌端点

- /oauth/token：令牌端点，携带如下参数请求即可：

| 请求参数   | 是否必填 | 描述                               |
| ---------- | -------- | ---------------------------------- |
| grant_type | REQUIRED | 使用的密码模式，值固定为"password" |
| username   | REQUIRED | 用户名                             |
| password   | REQUIRED | 密码                               |
| scope      | OPTIONAL | 请求权限范围                       |

请求成功和失败的响应同授权码模式。

## 安全考虑

>  注意：以下所有请求都必须在请求头中携带上面所说的客户端加密信息！

作为一个灵活且可扩展的框架，OAuth 的安全考虑取决于许多因素。spring security oauth 为我们提供了一些默认的端点如下：

- /oauth/authorize：授权端点
- /oauth/token：令牌端点
- /oauth/token：令牌端点也同时拥有刷新用户的功能，请求参数如下：

| 参数名称      | 是否必填 | 描述                                  |
| ------------- | -------- | ------------------------------------- |
| grant_type    | REQUIRED | 固定值为“refresh_token”               |
| refresh_token | REQUIRED | 请求到 token 时传过来的 refresh_token |

- /oauth/confirm_access：用户确认授权提交端点
- /oauth/error：授权服务错误信息端点
- /oauth/check_token：用于资源服务访问的令牌解析端点，请求参数如下：

| 参数名称 | 是否必填 | 描述             |
| -------- | -------- | ---------------- |
| token    | REQUIRED | 得到的有效的令牌 |

- /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话

# 技术选型

- 核心框架：spring boot
- 构建工具：maven
- 应用框架：spring boot data jpa
- 数据库：mysql
- web： spring boot web
- 测试框架：junit5、spring boot test
- 开发工具：idea
- 安全框架：spring security oauth2

# 在这之前

我们需要对项目的基本初始化，也就是使用 idea 创建我们 spring boot 项目

![1](https://resources.echocow.cn/file/2019/07/08/%E6%B7%B1%E5%BA%A6%E6%88%AA%E5%9B%BE_%E9%80%89%E6%8B%A9%E5%8C%BA%E5%9F%9F_20190709093428.png)

![2](https://resources.echocow.cn/file/2019/07/08/%E6%B7%B1%E5%BA%A6%E6%88%AA%E5%9B%BE_plasmashell_20190709093631.png)

![3](https://resources.echocow.cn/file/2019/07/08/%E6%B7%B1%E5%BA%A6%E6%88%AA%E5%9B%BE_plasmashell_20190709094513.png)

![4](https://resources.echocow.cn/file/2019/07/08/%E6%B7%B1%E5%BA%A6%E6%88%AA%E5%9B%BE_plasmashell_20190709095136.png)

父项目忘记添加 web 依赖了，如下：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

![5](https://resources.echocow.cn/file/2019/07/08/%E6%B7%B1%E5%BA%A6%E6%88%AA%E5%9B%BE_plasmashell_20190709100047.png)

![](https://resources.echocow.cn/file/2019/07/08/%E6%B7%B1%E5%BA%A6%E6%88%AA%E5%9B%BE_%E9%80%89%E6%8B%A9%E5%8C%BA%E5%9F%9F_20190709101728.png)

可选，配置阿里云国内源仓库

```xml
<repositories>
    <!--阿里云主仓库，代理了maven central和jcenter仓库-->
    <repository>
        <id>aliyun</id>
        <name>aliyun</name>
        <url>https://maven.aliyun.com/repository/public</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    <!--阿里云代理Spring 官方仓库-->
    <repository>
        <id>spring-milestones</id>
        <name>Spring Milestones</name>
        <url>https://maven.aliyun.com/repository/spring</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
<!--远程插件库-->
<pluginRepositories>
    <!--阿里云代理Spring 插件仓库-->
    <pluginRepository>
        <id>spring-plugin</id>
        <name>spring-plugin</name>
        <url>https://maven.aliyun.com/repository/spring-plugin</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </pluginRepository>
</pluginRepositories>
```

> 注意：请自行配置 lombok 支持！！！

这样，我们的父项目基本就构建完成了

我们下一篇回来完成第二件事，spring security oauth2 自动配置实现。
