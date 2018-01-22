package com.example.hxeaseui.chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.hxeaseui.activity.LoginActivity;
import com.example.hxeaseui.activity.MainActivity;
import com.example.hxeaseui.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseUI;

public class WelcomeActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!Settings.System.canWrite(this)) {
                showMissingPermissionDialog();
                return;
            }else {
                testWelcome();
            }
        }else {
            testWelcome();
        }
    }

    public void testWelcome(){
        initEase();
        MyHandler myHandler = new MyHandler();
        myHandler.sendEmptyMessageDelayed(1, 2000L);
    }

    public void initEase(){ // 初始化环信
        EaseUI.getInstance().init(this, null);
    }

    class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper l) {
            super(l);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 先判断是否登录过 登录成功过没调logout方法，这个方法的返回值一直是true
                    if (EMClient.getInstance().isLoggedInBefore()) {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    }else {
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开允许访问系统设置权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(WelcomeActivity.this, "缺少必要权限，程序无法正常使用", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        builder = null;
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                        dialog.dismiss();
                        builder = null;
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
