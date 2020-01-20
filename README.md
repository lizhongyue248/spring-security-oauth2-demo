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
5. [spring security oauth2 自定义授权模式（手机、邮箱等）](https://echocow.cn/articles/2019/07/30/1564498598952.html)
6. [spring security oauth2 踩坑记录](https://echocow.cn/articles/2020/01/20/1579503807596.html)
