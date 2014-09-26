package com.mrocker.push.demo;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.mrocker.push.PushManager;
import com.mrocker.push.service.PushReceiverListener;

public class DemoApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		PushManager.setDebugMode(true);

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

        //设置别名
        PushManager.setAlias("alias");


        // 下面的代码为可选功能
		// [可选] 添加点击通知后启动界面
		Map<String, Class> map = new HashMap<String, Class>();
		map.put("DEMO_启动页面", MainActivity.class);
		map.put("DEMO_测试打开", CustomViewActivity.class);
		PushManager.setCustomViews(map);

		// 获取raw文件
		// Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"
		// + R.raw.chongzi);
		// 获取sd卡文件
		// Uri uri =
		// Uri.parse("file://"+Environment.getExternalStorageDirectory().getAbsolutePath()+"/Chongzi.mp3");

		// 自定义 Notification
		// PushNotificationBuilder cBuilder = new
		// PushNotificationBuilder(R.layout.push_layout, R.id.push_image,
		// R.id.push_title, R.id.push_text, R.id.push_title_time);
		// cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		// cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
		// cBuilder.setStatusbarIcon(R.drawable.ic_launcher);
		// cBuilder.setLayoutDrawable(R.drawable.ic_launcher);
		// cBuilder.setNotificationSound(uri);
		// PushManager.setNotificationBuilder(cBuilder);

	}
}
