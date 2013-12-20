package com.kpbird.hellosmartwatch;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;

import com.sonyericsson.extras.liveware.aef.notification.Notification;
import com.sonyericsson.extras.liveware.aef.registration.Registration;
import com.sonyericsson.extras.liveware.extension.util.ExtensionService;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.notification.NotificationUtil;
import com.sonyericsson.extras.liveware.extension.util.registration.DeviceInfoHelper;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;


public class HelloExtensionService extends ExtensionService   {

	public static final String EXTENSION_SPECIFIC_ID = "EXTENSION_SPECIFIC_ID_HELLO_NOTIFICATION";
	public static final String EXTENSION_KEY =  "com.kpbird.hellosmartwatch.key";
	public static final String INTENT_ACTION_ADD = "com.kpbird.hellosmartwatch.action.add";
	public static final String INTENT_ACTION_CLEAR = "com.kpbird.hellosmartwatch.action.clear";

	private String TAG = this.getClass().getSimpleName();
	public HelloExtensionService() {
		super(EXTENSION_KEY);
		Log.i(TAG, "Constructor");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy()");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int retVal = super.onStartCommand(intent, flags, startId);
		if (intent != null) {
			if (INTENT_ACTION_CLEAR.equals(intent.getAction())) {
				Log.d(TAG, "onStart action: INTENT_ACTION_CLEAR");
				clearData(intent);
				stopSelfCheck();
			} else if (INTENT_ACTION_ADD.equals(intent.getAction())) {
				Log.d(TAG, "onStart action: INTENT_ACTION_ADD");
				addData(intent);
				stopSelfCheck();
			}
		}

		return retVal;
	}

	@Override
	protected RegistrationInformation getRegistrationInformation() {
		return new HelloRegistrationInformation(this);
	}

	@Override
	protected boolean keepRunningWhenConnected() {
		return false;
	}

	private void clearData(Intent intent) {
		 NotificationUtil.deleteAllEvents(this);
	}

	private void addData(Intent intent) {
		String name = "Name";
		String message = "Message";
		if (intent.getExtras().containsKey("name"))
			name = intent.getExtras().getString("name");
		if (intent.getExtras().containsKey("message"))
			message = intent.getExtras().getString("message");

		long time = System.currentTimeMillis();
		long sourceId = NotificationUtil.getSourceId(this,
				EXTENSION_SPECIFIC_ID);
		Log.i(TAG, "Source ID :" + sourceId);
		if (sourceId == NotificationUtil.INVALID_ID) {
			Log.e(TAG, "Failed to insert data");
			return;
		}
		String profileImage = ExtensionUtils.getUriString(this,R.drawable.ic_launcher);

		ContentValues eventValues = new ContentValues();
		eventValues.put(Notification.EventColumns.EVENT_READ_STATUS, false);
		eventValues.put(Notification.EventColumns.DISPLAY_NAME, name);
		eventValues.put(Notification.EventColumns.MESSAGE, message);
		eventValues.put(Notification.EventColumns.PERSONAL, 1);
		eventValues.put(Notification.EventColumns.PROFILE_IMAGE_URI,profileImage);
		eventValues.put(Notification.EventColumns.PUBLISHED_TIME, time);
		eventValues.put(Notification.EventColumns.SOURCE_ID, sourceId);

		try {
			getContentResolver().insert(Notification.Event.URI, eventValues);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "Failed to insert event", e);
		} catch (SecurityException e) {
			Log.e(TAG,
					"Failed to insert event, is Live Ware Manager installed?",
					e);
		} catch (SQLException e) {
			Log.e(TAG, "Failed to insert event", e);
		}
	}

	@Override
	protected void onViewEvent(Intent intent) {
		String action = intent.getStringExtra(Notification.Intents.EXTRA_ACTION);
		Log.i(TAG, "Action : " + action);
		String hostAppPackageName = intent.getStringExtra(Registration.Intents.EXTRA_AHA_PACKAGE_NAME);
		Log.i(TAG, "HostAppPackageName: " + hostAppPackageName);
		boolean advancedFeaturesSupported = DeviceInfoHelper.isSmartWatch2ApiAndScreenDetected(this, hostAppPackageName);
		Log.i(TAG, "Advanced Features Supported: " + advancedFeaturesSupported);
		int eventId = intent.getIntExtra(Notification.Intents.EXTRA_EVENT_ID,-1);
		try {
			Cursor cursor = getContentResolver().query(Notification.Event.URI,null, Notification.EventColumns._ID + " = " + eventId,null, null);
			if (cursor != null && cursor.moveToFirst()) {
				String name = cursor.getString(cursor.getColumnIndex(Notification.EventColumns.DISPLAY_NAME));
				String message = cursor.getString(cursor.getColumnIndex(Notification.EventColumns.MESSAGE));
				Toast.makeText(this,"Notification: Name: " + name + " Message: " + message,Toast.LENGTH_LONG).show();
				Log.i(TAG, "Name: " + name);
				Log.i(TAG, "Message: "+ message);
			}
			cursor.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void onRegisterResult(boolean success) {
		super.onRegisterResult(success);
		 Log.d(TAG, "onRegisterResult :" + success);
	}


}
