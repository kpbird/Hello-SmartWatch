package com.kpbird.hellosmartwatch;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlObjectClickEvent;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;
import com.sonyericsson.extras.liveware.extension.util.control.ControlView;
import com.sonyericsson.extras.liveware.extension.util.control.ControlView.OnClickListener;
import com.sonyericsson.extras.liveware.extension.util.control.ControlViewGroup;

public class HelloControlSmartWatch2 extends ControlExtension {

    private static final int ANIMATION_DELTA_MS = 500;
    private static final int MENU_ITEM_0 = 0;
    private static final int MENU_ITEM_1 = 1;
    private static final int MENU_ITEM_2 = 2;

    
    private boolean mIsShowingAnimation = false;   
    private Animation mAnimation = null;    
	private Handler mHandler;
    private ControlViewGroup mLayout = null;
    private String TAG = this.getClass().getSimpleName();
    private Bundle[] mMenuItemsText = new Bundle[3];


	public HelloControlSmartWatch2(Context context, String hostAppPackageName,Handler handler) {
		super(context, hostAppPackageName);
		 if (handler == null) {
	            throw new IllegalArgumentException("handler == null");
	     }
	     mHandler = handler;
	     LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	     View layout = inflater.inflate(R.layout.hello_control_2, null);
	     mLayout = parseLayout(layout);
	     if(mLayout !=null){
	    	 ControlView androidAnim = mLayout.findViewById(R.id.imgHelloControl);
	    	 androidAnim.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick() {
						Toast.makeText(mContext, "Button Clicked", Toast.LENGTH_LONG).show();
						Log.i(TAG, "Android Button Clicked");
						toggleAnimation();
				}
			});  
	    	
	     }
	     
	     
	        mMenuItemsText[0] = new Bundle();
	        mMenuItemsText[0].putInt(Control.Intents.EXTRA_MENU_ITEM_ID, MENU_ITEM_0);
	        mMenuItemsText[0].putString(Control.Intents.EXTRA_MENU_ITEM_TEXT, "Item 1");
	        
	        mMenuItemsText[1] = new Bundle();
	        mMenuItemsText[1].putInt(Control.Intents.EXTRA_MENU_ITEM_ID, MENU_ITEM_1);
	        mMenuItemsText[1].putString(Control.Intents.EXTRA_MENU_ITEM_TEXT, "Item 2");
	        
	        mMenuItemsText[2] = new Bundle();
	        mMenuItemsText[2].putInt(Control.Intents.EXTRA_MENU_ITEM_ID, MENU_ITEM_2);
	        mMenuItemsText[2].putString(Control.Intents.EXTRA_MENU_ITEM_TEXT, "Item 3");
	        
	}
	
	@Override
	public void onResume() {
		super.onResume();
		 showLayout(R.layout.hello_control_2, null);
		 startAnimation();
	}
	
	@Override
	public void onPause() {
		super.onPause();
        stopAnimation();
	}
	
    @Override
    public void onDestroy() {   
    	stopAnimation();
        mHandler = null;
    };
	

    public static int getSupportedControlWidth(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.smart_watch_2_control_width);
    }

    public static int getSupportedControlHeight(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.smart_watch_2_control_height);
    }

    
    @Override
    public void onTouch(ControlTouchEvent event) {
    	Log.i(TAG, "onTouch() " + event.getAction());
    }
    @Override
    public void onObjectClick(ControlObjectClickEvent event) {
    	 Log.i(TAG, "onObjectClick() " + event.getClickType());
    	 if (event.getLayoutReference() != -1) {
             mLayout.onClick(event.getLayoutReference());
         }
    }
    @Override
    public void onKey(int action, int keyCode, long timeStamp) {
        Log.i(TAG, "onKey() " + action + "\t" + keyCode + "\t" + timeStamp);
        if (action == Control.Intents.KEY_ACTION_RELEASE
                && keyCode == Control.KeyCodes.KEYCODE_OPTIONS) {
           showMenu(mMenuItemsText);
        }
    }
    
    @Override
    public void onMenuItemSelected(int menuItem) {
        Log.d(TAG, "onMenuItemSelected() - menu item " + menuItem);
        if (menuItem == MENU_ITEM_0) {
            clearDisplay();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onResume();
                }
            }, 1000);
        }

    }
    
    private void toggleAnimation() {
        if (mIsShowingAnimation) {
            stopAnimation();
        }
        else {
            startAnimation();
        }
    }
    
    private void startAnimation() {
        if (!mIsShowingAnimation) {
            mIsShowingAnimation = true;
            mAnimation = new Animation();
            mAnimation.run();
        }
    }
    private void stopAnimation() {
        if (mIsShowingAnimation) {
            // Stop animation on accessory
            if (mAnimation != null) {
                mAnimation.stop();
                mHandler.removeCallbacks(mAnimation);
                mAnimation = null;
            }
            mIsShowingAnimation = false;
        }
    }

    private class Animation implements Runnable {

        private int mIndex = 1;
        private boolean mIsStopped = false;
        
        Animation() {
            mIndex = 1;
        }

        public void stop() {
            mIsStopped = true;
        }

        @Override
        public void run() {
            int resourceId;
            switch (mIndex) {
                case 1:
                    resourceId = R.drawable.dancing_android_0;
                    break;
                case 2:
                    resourceId = R.drawable.dancing_android_1;
                    break;

                default:
                    Log.e(TAG, "mIndex out of bounds: " + mIndex);
                    resourceId = R.drawable.dancing_android_0;
                    break;
            }
            mIndex++;
            if (mIndex > 2) {
                mIndex = 1;
            }

            if (!mIsStopped) {
                updateAnimation(resourceId);
            }
            if (mHandler != null && !mIsStopped) {
                mHandler.postDelayed(this, ANIMATION_DELTA_MS);
            }
        }
        private void updateAnimation(int resourceId) {
            sendImage(R.id.imgHelloControl, resourceId);
        }
    };
}
