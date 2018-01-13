package com.example.hxeaseui;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseUI;

/**
 * @date: 2018/1/12.
 * @author: CHEN
 * @describe:
 */

public class EaseuiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化环信
        EaseUI.getInstance().init(this, null);
        // 在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

}
