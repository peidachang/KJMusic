KJmusic
=======
# **KJ音乐 Android 客户端项目简析** #

*注：KJ音乐项目拟采用最新的Android应用开发框架KJFrameForAndroid从新开发*

启动Eclipse，点击菜单并导入Android客户端项目，请确保你当前的Android SDK是最新版。<br>
如果编译出错，请修改项目根目录下的 project.properties 文件。<br>
推荐使用Android 4.0 以上版本的SDK,请使用JDK1.6编译：

> target=android-17

********本项目采用 GPL v2授权协议:
GPLV2协议说明：GPL协议的主要内容是只要在一个软件中使用(“使用”指类库引用，修改后的代码或者衍生代码)GPL 协议的产品，则该软件产品必须也采用GPL协议，既必须也是开源和免费。这就是所谓的”传染性”。GPL协议的产品作为一个单独的产品使用没有任何问题，还可以享受免费的优势。<br>
你拥有的权利:<br>
    以任何目的运行此程序的自由;<br>
    再发行复制件的自由;<br>
    改进此程序，并公开发布改进的自由.<br>
你需要注意:<br>
                如果在发布源于GPL的软件的时候，同时添加强制的条款，以在一定程度上保障其它一些人的权益，那么将无权发布该软件。<br>
********欢迎大家在这个基础上进行改进，并与大家分享。<br>

下面将简单的解析下项目：<br>

## **一、项目的目录结构** ##
> 根目录<br>
> ├ src<br>
> ├ libs<br>
> ├ res<br>
> ├ AndroidManifest.xml<br>
> ├ LICENSE.txt<br>
> ├ proguard.cfg<br>
> └ project.properties<br>

下面是src目录的子目录（未来可能变更）：<br>
	> src<br>
	> ├ org.kymjs.music<br>
	> ├ org.kymjs.music.ui<br>
	> ├ org.kymjs.music.ui.fragment<br>
	> ├ org.kymjs.music.ui.widget<br>
	> ├ org.kymjs.music.adapter<br>
	> ├ org.kymjs.music.utils<br>
	> ├ org.kymjs.music.bean<br>
	> ├ org.kymjs.music.service<br>
	> ├ org.kymjs.music.db<br>
	> └ org.kymjs.music.resolve<br>
	> └ org.kymjs.music.receiver<br>
	
	org.kymjs.music	- APP启动及管理包
	org.kymjs.music.ui - APP界面包
	org.kymjs.music.ui.fragment - APP碎片界面
	org.kymjs.music.ui.widget - APP自定义控件
	org.kymjs.music.adapter - APP适配器包
	org.kymjs.music.util - APP工具包，帮助类
	org.kymjs.music.bean - APP实体类包
	org.kymjs.music.service - APP所需服务
	org.kymjs.music.db - APP数据库相关
	org.kymjs.music.resolve - APP网络数据解析器
	org.kymjs.music.inter - 所需接口包
	org.kymjs.music.receiver - 接收全局广播
	
## **二、项目的功能流程** ##
#### 1、APP启动流程 ####
	应用首次启动，将跳转至org.kymjs.music包下的AppStart，在载入动画和资源的同时判断是否为首次安装程序，之后跳转到相应的Activity（欢迎界面Welcome或主界面Main）。<br>

#### 2、APP访问API流程 ####
	**1) 初始化控件**<br>
		首页Activity(Main.java)在initWidget()方法里面加载布局文件(Main.xml)，初始化底部栏bottomBar并设置点击事件监听。<br>
		注：布局文件在/res/layout目录，点击事件监听器写在widgetClick()方法中。<br>
	**2) 异步线程访问**<br>
	**3) 解析数据显示**<br>
		数据解析类统一写在resolve包下<br>
