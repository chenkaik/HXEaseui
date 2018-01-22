package com.example.hxeaseui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hxeaseui.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class LoginActivity extends BaseActivity implements View.OnClickListener, EMCallBack {

    private EditText editTextUser;
    private EditText editTextPass;
    private ProgressDialog progressDialog;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUser = findViewById(R.id.et_username);
        editTextUser.setText("chen");
        editTextPass = findViewById(R.id.et_password);
        editTextPass.setText("111111");
        findViewById(R.id.btn_login).setOnClickListener(this);
        editTextUser.setSelection(editTextUser.getText().length());
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIsLogin();
    }

    public void checkIsLogin(){
        // 先判断是否登录过 登录成功过没调logout方法，这个方法的返回值一直是true
        if (EMClient.getInstance().isLoggedInBefore()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
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
                        //finish();
                        Toast.makeText(LoginActivity.this, "缺少必要权限，程序无法正常使用", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        builder = null;
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

    @Override
    public void onClick(View v) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登录...");
        progressDialog.show();
        String username = editTextUser.getText().toString().trim();
        String password = editTextPass.getText().toString().trim();
        EMClient.getInstance().login(username, password, this);
    }

    @Override
    public void onSuccess() { // 登录成功
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                progressDialog = null;
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onError(final int i, final String s) { // 登录失败错误代码与文本类型错误描述
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                progressDialog = null;
                Toast.makeText(LoginActivity.this, "登录失败 " + s + " " + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onProgress(int i, String s) { // 	进度信息
        // 刷新进度的回调函数
    }

}
