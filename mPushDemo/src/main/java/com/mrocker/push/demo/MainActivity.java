package com.mrocker.push.demo;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.mrocker.push.AuthSmsCallBack;
import com.mrocker.push.PushManager;
import com.mrocker.push.entity.LocalMode;
import com.mrocker.push.entity.LocalPushAction;
import com.mrocker.push.entity.PushEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

public class MainActivity extends Activity {

    static TextView txt;
    static String buf = "";
    static ScrollView scroll;
    static EditText mobileEdit;

    public static Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (txt != null) {
                String tmp = String.valueOf(msg.obj);
                buf += tmp;
                txt.setText(buf);
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources resource = this.getResources();
        String pkgName = this.getPackageName();

        setContentView(resource.getIdentifier("activity_main", "layout", pkgName));
//        PushManager.update(this, true);
        // todo 推送下发消息启动程序时，透传的数据
        String title = this.getIntent().getStringExtra(
                PushEntity.EXTRA_PUSH_TITLE);
        String content = this.getIntent().getStringExtra(
                PushEntity.EXTRA_PUSH_CONTENT);
        HashMap<String, String> extention = (HashMap<String, String>) this
                .getIntent().getSerializableExtra(
                        PushEntity.EXTRA_PUSH_EXTENTION);
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)
                && extention != null) {
            String str = "title: " + title + "\ncontent: " + content
                    + "\nextention: " + extention.toString();
            Log.d("app=intent==>>", "title: " + title + " content: " + content
                    + " extention: " + extention.toString());
            String fname = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/mpush.log";
            String tmp = "打开程序Main:\n" + str + "\n\n";
            try {
                FileOutputStream fos = new FileOutputStream(fname, true);
                fos.write(tmp.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        txt = (TextView) findViewById(resource.getIdentifier("txt", "id", pkgName));
//        txt.setText(1);
        scroll = (ScrollView) findViewById(resource.getIdentifier("scroll", "id", pkgName));

        mobileEdit = (EditText) findViewById(resource.getIdentifier("mobile", "id", pkgName));

        String fname = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/mpush.log";

        if (!new File(fname).exists()){
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(fname);
            buf = IoUtil.toString(fis);
            fis.close();
            txt.setText(buf);
            scroll.fullScroll(View.FOCUS_DOWN);
        } catch (Exception e1) {
            e1.printStackTrace();
            Log.e(MainActivity.class.getName(), e1.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PushManager.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PushManager.onPause(this);

    }

    /**
     * 清除log监听
     */
    public void clearLog(View v) {
        String fname = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/mpush.log";
        File file = new File(fname);
        file.delete();
        buf = "";
        txt.setText(buf);
    }

    /**
     * 创建本地推送 监听
     */
    public void local(View v) {
        PushManager.sendLocalPushMsg("title0", "content0", new LocalPushAction("", new LocalMode(true, false, false, false), 0), null, System.currentTimeMillis() + (1000 * 10));
        PushManager.sendLocalPushMsg("title1", "content1", new LocalPushAction("DEMO_测试打开", new LocalMode(true, true, true, false), 1), null, System.currentTimeMillis() + (1000 * 30));
        PushManager.sendLocalPushMsg("title2", "content2", new LocalPushAction("", new LocalMode(false, true, false, false), 2), null, System.currentTimeMillis() + (1000 * 20));
        PushManager.sendLocalPushMsg("title3", "content3", new LocalPushAction("http://www.baidu.com", new LocalMode(false, true, false, true), 3), null, System.currentTimeMillis() + (1000 * 40));
    }

    /**
     * 移除全部本地推送 监听
     */
    public void del_local(View v) {
        PushManager.cancelAllLocalPush();
    }

    public void authsms(View v) {
        final String mobile = mobileEdit.getText().toString();
        if (mobile.length() > 0) {
            PushManager.authSms(mobile, 1, new AuthSmsCallBack() {
                @Override
                public void callBack(Exception e, String code) {
                    String result;
                    if (e != null) {
                        result = "验证过程出错：" + e.getMessage();
                    } else {
                        result = "验证码：" + code + "\n注意：同一手机号一天只能验证最多10次";
                    }
                    clearLog(null);
                    txt.setText(result);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
        }
    }

    public void copyuuid(View v) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, PushManager.getPushId(getApplication())));
        if (clipboardManager.hasPrimaryClip()) {
            clipboardManager.getPrimaryClip().getItemAt(0).getText();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
