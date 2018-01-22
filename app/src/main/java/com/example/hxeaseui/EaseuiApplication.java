package com.example.hxeaseui;

import android.content.IntentFilter;

import com.example.hxeaseui.receiver.CallReceiver;
import com.example.hxeaseui.util.VMLog;
import com.example.hxeaseui.voice.CallManager;
import com.hyphenate.EMConferenceListener;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConferenceStream;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;

import java.util.List;

/**
 * @date: 2018/1/12.
 * @author: CHEN
 * @describe:
 * https://github.com/lzan13/VMChatDemoCall
 */

public class EaseuiApplication extends VMApplication {

    private CallReceiver callReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化环信
        EaseUI.getInstance().init(this, null);
        // 开启debug模式，在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        // 设置通话广播监听器
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }
        //注册通话广播接收者
        context.registerReceiver(callReceiver, callFilter);
        // 通话管理类的初始化
        CallManager.getInstance().init(context);

        setConnectionListener();

//        setConferenceListener();

//        setMessageListener();
    }

    /**
     * 设置连接监听
     */
    private void setConnectionListener() {
        EMConnectionListener connectionListener = new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int i) {
                String str = "" + i;
                switch (i) {
                    case EMError.USER_REMOVED:
                        str = "账户被移除";
                        break;
                    case EMError.USER_LOGIN_ANOTHER_DEVICE:
                        str = "其他设备登录";
                        break;
                    case EMError.USER_KICKED_BY_OTHER_DEVICE:
                        str = "其他设备强制下线";
                        break;
                    case EMError.USER_KICKED_BY_CHANGE_PASSWORD:
                        str = "密码修改";
                        break;
                    case EMError.SERVER_SERVICE_RESTRICTED:
                        str = "被后台限制";
                        break;
                }
                VMLog.i("onDisconnected %s", str);
            }
        };
        EMClient.getInstance().addConnectionListener(connectionListener);
    }

    /**
     * 设置多人会议监听
     */
    private void setConferenceListener() {
        EMClient.getInstance().conferenceManager().addConferenceListener(new EMConferenceListener() {
            @Override
            public void onMemberJoined(String username) {
                VMLog.i("Joined username: %s", username);
            }

            @Override
            public void onMemberExited(String username) {
                VMLog.i("Exited username: %s", username);
            }

            @Override
            public void onStreamAdded(EMConferenceStream stream) {
                VMLog.i("Stream added streamId: %s, streamName: %s, memberName: %s, username: %s, extension: %s, videoOff: %b, mute: %b",
                        stream.getStreamId(), stream.getStreamName(), stream.getMemberName(), stream.getUsername(),
                        stream.getExtension(), stream.isVideoOff(), stream.isAudioOff());
                VMLog.i("Conference stream subscribable: %d, subscribed: %d",
                        EMClient.getInstance().conferenceManager().getAvailableStreamMap().size(),
                        EMClient.getInstance().conferenceManager().getSubscribedStreamMap().size());
            }

            @Override
            public void onStreamRemoved(EMConferenceStream stream) {
                VMLog.i("Stream removed streamId: %s, streamName: %s, memberName: %s, username: %s, extension: %s, videoOff: %b, mute: %b",
                        stream.getStreamId(), stream.getStreamName(), stream.getMemberName(), stream.getUsername(),
                        stream.getExtension(), stream.isVideoOff(), stream.isAudioOff());
                VMLog.i("Conference stream subscribable: %d, subscribed: %d",
                        EMClient.getInstance().conferenceManager().getAvailableStreamMap().size(),
                        EMClient.getInstance().conferenceManager().getSubscribedStreamMap().size());
            }

            @Override
            public void onStreamUpdate(EMConferenceStream stream) {
                VMLog.i("Stream added streamId: %s, streamName: %s, memberName: %s, username: %s, extension: %s, videoOff: %b, mute: %b",
                        stream.getStreamId(), stream.getStreamName(), stream.getMemberName(), stream.getUsername(),
                        stream.getExtension(), stream.isVideoOff(), stream.isAudioOff());
                VMLog.i("Conference stream subscribable: %d, subscribed: %d",
                        EMClient.getInstance().conferenceManager().getAvailableStreamMap().size(),
                        EMClient.getInstance().conferenceManager().getSubscribedStreamMap().size());
            }

            @Override
            public void onPassiveLeave(int error, String message) {
                VMLog.i("passive leave code: %d, message: %s", error, message);
            }

            @Override
            public void onConferenceState(ConferenceState state) {
                VMLog.i("State " + state);
            }

            @Override
            public void onStreamSetup(String streamId) {
                VMLog.i("Stream id %s", streamId);
            }

            @Override
            public void onReceiveInvite(String confId, String password, String extension) {
                VMLog.i("Receive conference invite confId: %s, password: %s, extension: %s", confId, password, extension);

//                Intent conferenceIntent = new Intent(context, ConferenceActivity.class);
//                conferenceIntent.putExtra("isCreator", false);
//                conferenceIntent.putExtra("confId", confId);
//                conferenceIntent.putExtra("password", password);
//                conferenceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(conferenceIntent);
            }
        });
    }

    private void setMessageListener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                for (EMMessage message : list) {
                    VMLog.i("收到新消息" + message);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });
    }

}
