package com.groupahead.screen;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Application;
import android.content.Context;
import android.os.Debug;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 * Keeps the screen on
 */
public class Screen extends CordovaPlugin {

    private PowerManager mPm;
    private PowerManager.WakeLock mWakeLock;
    private CallbackContext mCallBackContext;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        mPm = (PowerManager) cordova.getActivity().getApplicationContext().getSystemService(Context.POWER_SERVICE);
        @SuppressWarnings("deprecation")
        PowerManager.WakeLock wakeLock = mPm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "keepOn");
        mWakeLock = wakeLock;
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        mCallBackContext = callbackContext;
        if (action.equals("keepOn")) {
            boolean bool = args.getBoolean(0);
            this.keepOn(bool, callbackContext);
            return true;
        }
        return false;
    }

    @Override
    public void onPause(boolean multitasking) {
        keepOn(false, mCallBackContext);
        super.onPause(multitasking);
    }

    @Override
    public void onResume(boolean multitasking) {
        keepOn(true, mCallBackContext);
        super.onResume(multitasking);
    }

    private void keepOn(boolean bool, CallbackContext callbackContext) {
        if (bool) {
            try {
                mWakeLock.acquire();
                callbackContext.success("Screen keepalive on");
            } catch (Exception e) {
                callbackContext.error("Screen keepalive on request failed: " + e.getMessage());
            }
        } else {
            try {
                if (mWakeLock.isHeld()) {
                    mWakeLock.release();
                    callbackContext.success("Screen keepalive off");
                }
            } catch (Exception e) {
                callbackContext.error("Screen keepalive off request failed: " + e.getMessage());
            }
        }
    }
}