package com.kpbird.hellosmartwatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HelloExtensionReceiver  extends BroadcastReceiver{
	
	private String TAG = this.getClass().getSimpleName();
	@Override
	public void onReceive(Context context, Intent intent) {
		  Log.i(TAG, "HelloExtensionReceiver onReceiver: " + intent.getAction());
	      intent.setClass(context, HelloExtensionService.class);
	      context.startService(intent);
	}

}
