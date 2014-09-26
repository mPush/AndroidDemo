package com.mrocker.push.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mrocker.push.entity.PushEntity;

/**
 * Created by liuyu on 14-5-29.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(PushEntity.ACTION_PUSH_MESSAGE)) {
			// 接收透传信息
			onMessage(context, intent);
		} else if (action.equals(PushEntity.ACTION_PUSH_TOKEN)) {
			// 接收pushToken
			onToken(context, intent);
		} else if (action.equals(PushEntity.ACTION_PUSH_CLICK)) {
			// 点击通知 返回内容
			onClick(context, intent);
		}
	}

	public void onMessage(Context context, Intent intent) {
		String msg = intent
				.getStringExtra(PushEntity.EXTRA_PUSH_MESSAGE_STRING);

		Log.d("Receiver=onMessage==>>", "Msg: " + msg);
	}

	public void onToken(Context context, Intent intent) {
		String token = intent
				.getStringExtra(PushEntity.EXTRA_PUSH_MESSAGE_STRING);
		System.out.println("Receive token: " + token);
	}

	public void onClick(Context context, Intent intent) {
		String msg = intent
				.getStringExtra(PushEntity.EXTRA_PUSH_MESSAGE_STRING);

		Log.d("Receiver=onClick==>>", "Msg: " + msg);
	}

}
