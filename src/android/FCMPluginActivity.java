package com.gae.scaffolder.plugin;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import java.util.Map;
import java.util.HashMap;

import java.util.*;
import java.util.Arrays;
import java.util.List;

public class FCMPluginActivity extends Activity {
    private static String TAG = "FCMPlugin";

    /*
     * this activity will be started if the user touches a notification that we own. 
     * We send it's data off to the push plugin for processing.
     * If needed, we boot up the main activity to kickstart the application. 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG, "==> FCMPluginActivity onCreate");
		
		Map<String, Object> data = new HashMap<String, Object>();
        if (getIntent().getExtras() != null) {
			Log.d(TAG, "==> USER TAPPED NOTFICATION");
			data.put("wasTapped", true);
			for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "\tKey: " + key + " Value: " + value);
				data.put(key, value);
            }
        }
		
		FCMPlugin.sendPushPayload(data);

        finish();
	
	if (!isMainActivityRunning(getApplicationContext().getPackageName())) {
	    forceMainActivityReload();
	}
    }
	
	public boolean isMainActivityRunning(String packageName) {
	    ActivityManager activityManager = (ActivityManager) getSystemService (Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(Integer.MAX_VALUE); 
		
	    for (int i = 0; i < tasksInfo.size(); i++) {
		if (tasksInfo.get(i).baseActivity.getPackageName().toString().equals(packageName))
		{
			return true;
		}
		    
	    }
	    return false;
	} 

    private void forceMainActivityReload() {
        PackageManager pm = getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(getApplicationContext().getPackageName());
        startActivity(launchIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
		Log.d(TAG, "==> FCMPluginActivity onResume");
        final NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "==> FCMPluginActivity onStart");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "==> FCMPluginActivity onStop");
	}

}
