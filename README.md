mPush 自动集成指南
===========

##集成步骤##


####1.使用git命令将mPush AndroidDemo下载到本地

	git clone https://github.com/mpush/AndroidDemo.git
	
####2.将AndroidManifest.xml 中的 `MPUSH_APPID` 替换为在Portal上注册该应用的的APP ID，例如:

	<meta-data android:name="MPUSH_APPID" android:value="5386a17859ba0740ce00001b" />
	
####3.如果需要配置渠道号，则将AndroidManifest.xml 中的 `MPUSH_CHANNEL` 替换为您自定义的渠道号，例如:

	<meta-data android:name="MPUSH_CHANNEL" android:value="mpush" />
	
####4.将AndroidManifest.xml 中的 `package` 改成您在Portal上配置的包名，例如：
	package="com.mrocker.push.demo"


更多信息请访问[mPush官方开发文档](http://doc.mpush.cn/)
