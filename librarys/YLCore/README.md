
#应用类

     UpdateConfiguration configuration = new UpdateConfiguration()
            //输出错误日志
            .setEnableLog(true)
            //设置自定义的下载
            //.setHttpManager()
            //下载完成自动跳动安装页面
            .setJumpInstallPage(true)
            //支持断点下载
            .setBreakpointDownload(true)
            //设置是否显示通知栏进度
            .setShowNotification(true)
            //设置强制更新
            .setForcedUpgrade(false)
            //设置对话框按钮的点击监听
            .setButtonClickListener(this)
            //设置下载过程的监听
            .setOnDownloadListener(this);

     DownloadManager manager = DownloadManager.getInstance(this);
     manager.setApkName("appupdate.apk")
            .setApkUrl("https://raw.githubusercontent.com/azhon/AppUpdate/master/apk/appupdate.apk")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setShowNewerToast(true)
            .setConfiguration(configuration)
            .setDownloadPath(Environment.getExternalStorageDirectory() + "/AppUpdate")
            .setApkVersionCode(2)
            .setApkVersionName("2.1.8")
            .setApkSize("20.4")
            .setAuthorities(getPackageName())
            .setApkDescription("1.支持断点下载\n2.支持Android N\n3.支持Android O\n4.支持自定义下载过程")
            .download();