package com.example.hxeaseui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.hyphenate.easeui.ui.EaseBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @date: 2018/1/12.
 * @author: CHEN
 * @describe:
 */

public class BaseActivity extends EaseBaseActivity {

    protected PermissionListener permissionListener;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    /**
     * 申请权限
     *
     * @param permissions 申请的权限名
     * @param listener    权限授权的回调
     */
    public void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        permissionListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            permissionListener.onGranted(); // 表示全都授权了
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i]; // 判断是否授权
                        String permission = permissions[i]; // 请求权限的名字
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        permissionListener.onGranted();
                    } else {
                        permissionListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * group:android.permission-group.CONTACTS
     permission:android.permission.WRITE_CONTACTS
     permission:android.permission.GET_ACCOUNTS
     permission:android.permission.READ_CONTACTS

     group:android.permission-group.PHONE
     permission:android.permission.READ_CALL_LOG
     permission:android.permission.READ_PHONE_STATE
     permission:android.permission.CALL_PHONE
     permission:android.permission.WRITE_CALL_LOG
     permission:android.permission.USE_SIP
     permission:android.permission.PROCESS_OUTGOING_CALLS
     permission:com.android.voicemail.permission.ADD_VOICEMAIL

     group:android.permission-group.CALENDAR
     permission:android.permission.READ_CALENDAR
     permission:android.permission.WRITE_CALENDAR

     group:android.permission-group.CAMERA
     permission:android.permission.CAMERA

     group:android.permission-group.SENSORS
     permission:android.permission.BODY_SENSORS

     group:android.permission-group.LOCATION
     permission:android.permission.ACCESS_FINE_LOCATION
     permission:android.permission.ACCESS_COARSE_LOCATION

     group:android.permission-group.STORAGE
     permission:android.permission.READ_EXTERNAL_STORAGE
     permission:android.permission.WRITE_EXTERNAL_STORAGE

     group:android.permission-group.MICROPHONE
     permission:android.permission.RECORD_AUDIO

     group:android.permission-group.SMS
     permission:android.permission.READ_SMS
     permission:android.permission.RECEIVE_WAP_PUSH
     permission:android.permission.RECEIVE_MMS
     permission:android.permission.RECEIVE_SMS
     permission:android.permission.SEND_SMS
     permission:android.permission.READ_CELL_BROADCASTS
     */

}
