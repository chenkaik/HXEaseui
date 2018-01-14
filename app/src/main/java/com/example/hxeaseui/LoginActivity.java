package com.example.hxeaseui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class LoginActivity extends BaseActivity implements View.OnClickListener, EMCallBack {

    private EditText editTextUser;
    private EditText editTextPass;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 先判断是否登录过 登录成功过没调logout方法，这个方法的返回值一直是true
        if (EMClient.getInstance().isLoggedInBefore()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        editTextUser = findViewById(R.id.et_username);
        editTextUser.setText("chen");
        editTextPass = findViewById(R.id.et_password);
        editTextPass.setText("111111");
        findViewById(R.id.btn_login).setOnClickListener(this);
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
