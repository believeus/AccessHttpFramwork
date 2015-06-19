package com.ysten.start;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupBroadCast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent();
		i.setClass(context, BootStrap.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(i);
	}

}
