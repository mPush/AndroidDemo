package com.mrocker.push.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.mrocker.push.entity.PushEntity;

import java.io.FileOutputStream;
import java.util.HashMap;

public class CustomViewActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //todo 推送下发消息启动程序时，透传的数据
        String title = this.getIntent().getStringExtra(PushEntity.EXTRA_PUSH_TITLE);
        String content = this.getIntent().getStringExtra(PushEntity.EXTRA_PUSH_CONTENT);
        HashMap<String, String> extention = (HashMap<String, String>) this.getIntent().getSerializableExtra(PushEntity.EXTRA_PUSH_EXTENTION);
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content) && extention != null) {
            String str = "title: " + title + "\ncontent: " + content + "\nextention: " + extention.toString();
            Log.d("app=intent==>>", "title: " + title + " content: " + content + " extention: " + extention.toString());

            Message msg = new Message();
            String fname = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mpush.log";
            String tmp = "打开程序Custom:\n" + str + "\n\n";
            msg.obj = tmp;
            MainActivity.handler.sendMessage(msg);
            try {
                FileOutputStream fos = new FileOutputStream(fname, true);
                fos.write(tmp.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
