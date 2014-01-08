package com.kpbird.hellosmartwatch;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;

import com.sonyericsson.extras.liveware.aef.notification.Notification;
import com.sonyericsson.extras.liveware.aef.registration.Registration;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;

public class HelloRegistrationInformation extends RegistrationInformation{
	private Context mContext;
	public HelloRegistrationInformation(Context ctx){
		 if (ctx == null) {
	            throw new IllegalArgumentException("context == null");
	        }
	        mContext = ctx;
	}
	@Override
	public int getRequiredNotificationApiVersion() {
		return 1;
	}
	@Override
	public int getRequiredWidgetApiVersion() {
		return 0;
	}

	@Override
	public int getRequiredControlApiVersion() {
		return 2;
	}

	@Override
	public int getRequiredSensorApiVersion() {
		return 0;
	}

	@Override
	public ContentValues getExtensionRegistrationConfiguration() {
		  String extensionIcon = ExtensionUtils.getUriString(mContext,R.drawable.ic_launcher);
	       String iconHostapp = ExtensionUtils.getUriString(mContext,R.drawable.ic_launcher);
	        String extensionIcon48 = ExtensionUtils.getUriString(mContext,R.drawable.ic_launcher_48);

	        String configurationText = "Hello SmartWatch";
	        String extensionName = "Hello SmartWatch";

	        ContentValues values = new ContentValues();
	        values.put(Registration.ExtensionColumns.CONFIGURATION_ACTIVITY,MainActivity.class.getName());
	        values.put(Registration.ExtensionColumns.CONFIGURATION_TEXT, configurationText);
	        values.put(Registration.ExtensionColumns.EXTENSION_ICON_URI, extensionIcon);
	        values.put(Registration.ExtensionColumns.EXTENSION_48PX_ICON_URI, extensionIcon48);

	        values.put(Registration.ExtensionColumns.EXTENSION_KEY,HelloExtensionService.EXTENSION_KEY);
	        values.put(Registration.ExtensionColumns.HOST_APP_ICON_URI, iconHostapp);
	        values.put(Registration.ExtensionColumns.NAME, extensionName);
	        values.put(Registration.ExtensionColumns.NOTIFICATION_API_VERSION,getRequiredNotificationApiVersion());
	        values.put(Registration.ExtensionColumns.PACKAGE_NAME, mContext.getPackageName());
	        return values;
	}

	
	
	@Override
	public ContentValues[] getSourceRegistrationConfigurations() {
		
		
		 ContentValues sourceValues = null;

	        String iconSource1 = ExtensionUtils.getUriString(mContext,R.drawable.ic_launcher_30);
	        String iconSource2 = ExtensionUtils.getUriString(mContext,R.drawable.ic_launcher_18);
	        String iconBw = ExtensionUtils.getUriString(mContext,R.drawable.ic_launcher_18_bw);
	        String textToSpeech = "Notification from Hello SmartWatch Application";
	        sourceValues = new ContentValues();
	        sourceValues.put(Notification.SourceColumns.ENABLED, true);
	        sourceValues.put(Notification.SourceColumns.ICON_URI_1, iconSource1);
	        sourceValues.put(Notification.SourceColumns.ICON_URI_2, iconSource2);
	        sourceValues.put(Notification.SourceColumns.ICON_URI_BLACK_WHITE, iconBw);
	        sourceValues.put(Notification.SourceColumns.UPDATE_TIME, System.currentTimeMillis());
	        sourceValues.put(Notification.SourceColumns.NAME, mContext.getString(R.string.app_name));
	        sourceValues.put(Notification.SourceColumns.EXTENSION_SPECIFIC_ID, HelloExtensionService.EXTENSION_SPECIFIC_ID);
	        sourceValues.put(Notification.SourceColumns.PACKAGE_NAME, mContext.getPackageName());
	        sourceValues.put(Notification.SourceColumns.TEXT_TO_SPEECH, textToSpeech);
	        sourceValues.put(Notification.SourceColumns.ACTION_1,"Hello");
	        sourceValues.put(Notification.SourceColumns.ACTION_ICON_1,ExtensionUtils.getUriString(mContext, R.drawable.ic_launcher));
	        
		
		
		List<ContentValues> bulkValues = new ArrayList<ContentValues>();
        bulkValues.add(sourceValues);
        return bulkValues.toArray(new ContentValues[bulkValues.size()]);
	}
	
	@Override
    public boolean isDisplaySizeSupported(int width, int height) {
        return ((width == HelloControlSmartWatch2.getSupportedControlWidth(mContext)
                && height == HelloControlSmartWatch2
                        .getSupportedControlHeight(mContext) || width == HelloControlSmartWatch2
                .getSupportedControlWidth(mContext) && height == HelloControlSmartWatch2
                .getSupportedControlHeight(mContext)) );
    }


}
