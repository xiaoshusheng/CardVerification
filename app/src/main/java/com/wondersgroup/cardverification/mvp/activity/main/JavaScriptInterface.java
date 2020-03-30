package com.wondersgroup.cardverification.mvp.activity.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.wondersgroup.cardverification.widget.camera.CameraConfig;
import com.wondersgroup.cardverification.widget.camera.CropActivity;

public class JavaScriptInterface {
    private Context context;
    public JavaScriptInterface(Context c) {
        context = c;
    }

    /**
     * 拨打电话
     *
     * @param phoneNum 电话号码
     */
    @SuppressLint("MissingPermission")
    @JavascriptInterface
    public void phoneCall(String phoneNum) {
        Log.e("phoneCall: ", phoneNum);
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }


    @JavascriptInterface
    public void callCamera() {
        Intent intent = new Intent(context, CropActivity.class);

        intent.putExtra(CameraConfig.MASK_COLOR, 0x99000000);
        intent.putExtra(CameraConfig.TOP_OFFSET, 0);
        intent.putExtra(CameraConfig.RECT_CORNER_COLOR, 0xff00ff00);
        intent.putExtra(CameraConfig.TEXT_COLOR, 0xffffffff);
        intent.putExtra(CameraConfig.HINT_TEXT, "");
        intent.putExtra(CameraConfig.IMAGE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/CameraCardCrop/" + System.currentTimeMillis() + ".jpg");
        context.startActivity(intent);
    }

    /**
     * 获取设备的唯一标识设备号
     */
    @JavascriptInterface
    public String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint({"MissingPermission", "HardwareIds"}) String id = tm.getDeviceId();
        return id;
    }


}
