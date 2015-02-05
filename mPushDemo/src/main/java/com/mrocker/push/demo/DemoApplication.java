package com.mrocker.push.demo;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import com.mrocker.push.PushManager;
import com.mrocker.push.service.PushBasicNotificationBuilder;
import com.mrocker.push.service.PushReceiverListener;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

         PushManager.startPushService(getApplicationContext(), new PushReceiverListener() {
             @Override
             public boolean onMessage(Context context, String title,
                                      String content, Map extention) {

                 String str = "title: " + title + "\ncontent: " + content
                         + "\nextent ion: " + extention.toString();
                 Log.d("app==>>", "title: " + title + " content: " + content
                         + " extention: " + extention.toString());

                 Message msg = new Message();
                 String fname = Environment.getExternalStorageDirectory()
                         .getAbsolutePath() + "/mpush.log";
                 String tmp = "Listener->Application:\n" + str + "\n\n";
                 msg.obj = tmp;
                 MainActivity.handler.sendMessage(msg);
                 try {
                     FileOutputStream fos = new FileOutputStream(fname, true);
                     fos.write(tmp.getBytes());
                     fos.close();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

                 return false;
             }
         });

        //启动debug模式
        PushManager.setDebugMode(true);

        //设置别名
        PushManager.setAlias("alias");


        // 下面的代码为可选功能
        // [可选] 添加点击通知后启动界面
        Map<String, Class> map = new HashMap<String, Class>();
        map.put("xxx", MainActivity.class);
        map.put("DEMO_测试打开", CustomViewActivity.class);
        PushManager.setCustomViews(map);

//		使用layout自定义 Notification
        Resources resource = this.getResources();
        String pkgName = this.getPackageName();

//        PushNotificationBuilder cBuilder = new PushNotificationBuilder(resource.getIdentifier("push_layout", "layout", pkgName), resource.getIdentifier("push_image", "id", pkgName),
//                resource.getIdentifier("push_title", "id", pkgName), resource.getIdentifier("push_text", "id", pkgName), resource.getIdentifier("push_title_time", "id", pkgName));
//        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
//        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
//        cBuilder.setStatusbarIcon(resource.getIdentifier("logo", "drawable", pkgName));
//        cBuilder.setLayoutDrawable(resource.getIdentifier("ic_launcher", "drawable", pkgName));
//        PushManager.setNotificationBuilder(cBuilder);


        PushBasicNotificationBuilder builder = new PushBasicNotificationBuilder();
        if (Build.VERSION.SDK_INT > 20){
            builder.setStatusbarIcon(resource.getIdentifier("logo_transparent", "drawable", pkgName));
        }else{
            builder.setStatusbarIcon(resource.getIdentifier("logo", "drawable", pkgName));
        }
        PushManager.setNotificationBuilder(builder);



    }
}
