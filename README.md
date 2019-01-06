## Android项目结构
> AndroidLib库，用来存放与业务无关的逻辑
* component 用来存放公用组件，如net、log、toast、mvp等一系列组件
* util 用来存放公用方法
* widget 用来存放自定义控件
> Android主项目
* api  用来存放项目中所有的网络请求
* base 用来存放基类。比如BaseActivity、BaseAdapter等
* component 用来存跟业务关联的组件，（一般为Lib库component的派生）
* helper 用来存放帮助类，比如用户管理器、基础数据管理器等
* model 用来存放项目中的业务数据。constants(常量、枚举等)，local(数据库)，vo(实体类)
* module 用来存放项目中的业务模块。比如登陆模块、注册模块等
* widget 用来存放跟业务相关联的自定义控件

> 初始化更新基础库
* git submodule init
* git submodule update