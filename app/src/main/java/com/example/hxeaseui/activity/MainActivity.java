package com.example.hxeaseui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hxeaseui.R;
import com.example.hxeaseui.chat.ChatActivity;
import com.example.hxeaseui.chat.PermissionListener;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, EMCallBack, PermissionListener {

    private static final String TAG = "MainActivity";
    private EditText chatNickName;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatNickName = findViewById(R.id.ed_nick);
//        chatNickName.setText("kai");
        findViewById(R.id.to_chat).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
    }

    private static final int REQUEST_CODE = 1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_chat:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //startActivityForResult(intent, REQUEST_CODE);
                        startActivity(intent);
                        //finish();
                    } else {
                        //有了权限 你要做什么 具体的动作
                        checkPermission();
                    }
                }else {
                    checkPermission();
                }
                //toChatPage();
                break;
            case R.id.logout:
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("正在退出");
                progressDialog.show();
                // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
                EMClient.getInstance().logout(false, this);
                break;
            default:
                break;
        }
    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
            requestRuntimePermission(new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE}, this);
        } else {
            toChatPage();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == REQUEST_CODE) {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (Settings.System.canWrite(this)) {
//                    checkPermission();
//                }else {
//                    Toast.makeText(this, "请允许设置系统权限", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

    @Override
    public void onGranted() {
        toChatPage();
    }

    @Override
    public void onDenied(List<String> deniedPermissions) {
        showMissingPermissionDialog();
    }

    /**
     * 去聊天页面
     */
    private void toChatPage() {
        String nickName = chatNickName.getText().toString().trim();
        if (!TextUtils.isEmpty(nickName)) {
            String currentName = EMClient.getInstance().getCurrentUser();
            if (nickName.equals(currentName)) {
                Toast.makeText(this, "不能和自己聊天", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, nickName);
                startActivity(intent);
                //startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, nickName));
            }
        } else {
            Toast.makeText(this, "聊天的对象昵称不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess() { // 退出登录成功
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                progressDialog = null;
                Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onError(int i, String s) { // 退出登录失败
        Log.e(TAG, "logout error " + i + " - " + s);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "退出失败", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                progressDialog = null;
            }
        });
    }

    @Override
    public void onProgress(int i, String s) {

    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        Toast.makeText(MainActivity.this, "缺少必要权限，程序无法正常使用", Toast.LENGTH_SHORT).show();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

}
