package com.child.manage.video;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.android.audio.AudioBuffer;
import com.android.audio.AudioPlayer;
import com.android.audio.AudioPlayer.MyAudioParameter;
import com.android.audio.AudioPlayer.MyRecordCallback;
import com.android.opengles.FrameBuffer;
import com.android.opengles.OpenglesRender;
import com.android.opengles.OpenglesView;
import com.child.manage.R;
import ipc.android.sdk.com.*;
import ipc.android.sdk.impl.DeviceInfo;
import ipc.android.sdk.impl.FunclibAgent;
import ipc.android.sdk.impl.FunclibAgent.IFunclibAgentCB;
import ipc.android.sdk.impl.PlayCtrlAgent;
import ipc.android.sdk.impl.PlayCtrlAgent.IPlayCtrlAgentCB;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author qinglei.yin@192.168.88.9<br>
 *         2014-3-5 下午2:28:33<br>
 * @declaration 测试demo<br>
 */
@SuppressLint("SimpleDateFormat")
public class MainYuvShowUI extends BaseDataUI implements OnClickListener {

    Context m_ctx = null;
    private static final String TAG = "MainYuvShowUI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_ctx = this.getBaseContext();
        mTipDlg = new MyProgressDialog(this, "");
        mTipDlg.setCancelable(false);

        setContentView(R.layout.main_yuv_view_ui);

        findViewById(R.id.btnInit).setOnClickListener(this);
        findViewById(R.id.btnUninit).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnLogoff).setOnClickListener(this);
        findViewById(R.id.btnRegister).setOnClickListener(this);
        findViewById(R.id.btnAddDevice).setOnClickListener(this);
        findViewById(R.id.btnShot).setOnClickListener(this);

        findViewById(R.id.btnStartVideo).setOnClickListener(this);
        findViewById(R.id.btnStopVideo).setOnClickListener(this);
        findViewById(R.id.btnStartRecord).setOnClickListener(this);
        findViewById(R.id.btnStopRecord).setOnClickListener(this);
        findViewById(R.id.btnModifyDevUserPwd).setOnClickListener(this);

        findViewById(R.id.btnOpenVoice).setOnClickListener(this);
        findViewById(R.id.btnCloseVoice).setOnClickListener(this);
        findViewById(R.id.btnOpenTalk).setOnClickListener(this);
        findViewById(R.id.btnCloseTalk).setOnClickListener(this);
        findViewById(R.id.btnMainStream).setOnClickListener(this);
        findViewById(R.id.btnSubStream).setOnClickListener(this);

        findViewById(R.id.btnPtzUp).setOnClickListener(this);
        findViewById(R.id.btnPtzDown).setOnClickListener(this);
        findViewById(R.id.btnPtzLeft).setOnClickListener(this);
        findViewById(R.id.btnPtzRight).setOnClickListener(this);
        findViewById(R.id.btnPtzAuto).setOnClickListener(this);

        findViewById(R.id.btnGetMediaCapability).setOnClickListener(this);
        findViewById(R.id.btnGetMediaParam).setOnClickListener(this);
        findViewById(R.id.btnGetAlarmConfig).setOnClickListener(this);
        findViewById(R.id.btnGetInputAlarm).setOnClickListener(this);
        findViewById(R.id.btnGetMotionDetectAlarm).setOnClickListener(this);

        findViewById(R.id.btnGetFrontEndRecord).setOnClickListener(this);
        findViewById(R.id.btnReplayFrontEndRecord).setOnClickListener(this);

        findViewById(R.id.btnStartAlertorBind).setOnClickListener(this);
        findViewById(R.id.btnStopAlertorBind).setOnClickListener(this);
        findViewById(R.id.btnGetAlertorList).setOnClickListener(this);
        findViewById(R.id.btnDelAlertorBind).setOnClickListener(this);
        findViewById(R.id.btnSetSecurity1).setOnClickListener(this);
        findViewById(R.id.btnSetSecurity0).setOnClickListener(this);
        findViewById(R.id.btnGetSecurity).setOnClickListener(this);
        findViewById(R.id.btnSetAlertorAlias).setOnClickListener(this);
        findViewById(R.id.btnSetAlertorPreset).setOnClickListener(this);
        findViewById(R.id.btnGetAlmPicture).setOnClickListener(this);
        findViewById(R.id.btnConfirmAlertorAlm).setOnClickListener(this);

        File f = new File("/sdcard/netsdk");
        if (!f.exists()) f.mkdir();

        initGLView(); //初始化显示视图
        testSDK(); //测试SdK
    }

    /**
     * 登录状态标识
     */
    int[] mLoginFlags = {-1, -1, -1, -1, -1, -1};
    final int MaxBranch = 4;
    int MAX_PLAY_NUMS = 1;
    int[] mGlViewRes = {R.id.glVideo1, R.id.glVideo2, R.id.glVideo3, R.id.glVideo4};
    OpenglesRender[] mGlRenderAry = new OpenglesRender[MaxBranch];
    AudioPlayer[] mAudioPlayerAry = new AudioPlayer[MaxBranch];

    /**
     * 渲染对象映射类，键为设备ID，值为OpenglesRender(GL渲染对象)
     */
    Map<String, OpenglesRender> mRenderMap = new HashMap<String, OpenglesRender>();
    /**
     * 音频播放映射类，键为设备ID，值为AudioPlayer音频播放类
     */
    Map<String, AudioPlayer> mAudioPlayerMap = new HashMap<String, AudioPlayer>();

    /**
     * 设备ID和端口句柄映射类，键为设备ID，值为端口句柄
     */
    Map<String, Integer> mIDOrPortMap = new HashMap<String, Integer>();
    /**
     * 设备ID和端口句柄映射类，键为口句柄，值为端设备ID
     */
    Map<String, String> mPortOrIDMap = new HashMap<String, String>();

    Map<String, Integer> m_streamMap = new HashMap<>();

    DeviceInfo m_modifyInfo = null;
    Device m_modifyUserPwdDev = null;

    NetSDK_Media_Capability m_media_caps;
    NetSDK_Media_Video_Config m_video_config;
    NetSDK_Media_Video_Config m_new_video_config;

    /**
     * 初始化GL视图类及绑定OpenglesRender渲染类
     */
    public void initGLView() {
        for (int i = 0; i < MaxBranch; i++) {
            OpenglesView glView = (OpenglesView) findViewById(mGlViewRes[i]);

            OpenglesRender glRender = new OpenglesRender(glView, i);
            glRender.setVideoMode(OpenglesRender.VIDEO_MODE_CUSTOM);// VIDEO_MODE_FIT
            glView.setRenderer(glRender);
            glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
            mGlRenderAry[i] = glRender;

            mAudioPlayerAry[i] = new AudioPlayer(i);
        }
        setEtStr(R.id.etDeviceID, devID[0]);
    }

    public void audioCallback(final String devID, final byte[] data, final int len) {
        AudioBuffer audioBuf = new AudioBuffer(data, len);
        AudioPlayer audioPlayer = mAudioPlayerMap.get(devID);
        if (audioPlayer != null) {
            audioPlayer.addToBuf(audioBuf);
        }
    }

    public void stopOneVoice(int index) {
        if (index >= 0 && mAudioPlayerAry != null) {
            mAudioPlayerAry[index].stopOutAudio();
        }
    }

    public void stopAllVoice() {
        for (int i = 0; i < MaxBranch; i++) {
            stopOneVoice(i);
        }
    }

    public void sendMessageToUI(int what, int arg1, int arg2, Object recvObj) {
        if (mhHandler == null) return;
        Message msg = Message.obtain();
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.what = what;
        msg.obj = recvObj;
        mhHandler.sendMessage(msg);
    }

    /**
     * 播放控制实例类
     */
    PlayCtrlAgent pc;
    /**
     * 通讯协议实例类
     */
    FunclibAgent fb;

    public void testSDK() {
        if (null != pc) return;
        Log.i("MSG", "testDemo is begin...");
        pc = PlayCtrlAgent.getInstance();
        fb = FunclibAgent.getInstance();// 初始化
        fb.initAgent();
        fb.setIFunclibAgentCB(new IFunclibAgentCB() {// 实现消息及媒体回调函数
            @SuppressWarnings("unchecked")
            @Override
            public int msgRspCB(int nMsgType, byte[] pData, int nDataLen) {// 消息回调
                switch (nMsgType) {
                    case SDK_CONSTANT.TPS_MSG_NOTIFY_LOGIN_OK: {// 登录成功消息，返回UserRight结构
                        if (pData != null && nDataLen == UserRight.SIZE) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(nDataLen);
                            byteBuffer.order(ByteOrder.nativeOrder());
                            byteBuffer.put(pData, 0, nDataLen);
                            byteBuffer.rewind();
                            UserRight ts = (UserRight) UserRight
                                    .createObjectByByteBuffer(byteBuffer);
                            Log.i("MSG", "msgRspCB-->login is success,UserRight" + ts.toString());
                            sendMyToast("msgRspCB-->login is suc");
                        }
                    }
                    break;

                    case SDK_CONSTANT.TPS_MSG_NOTIFY_LOGIN_FAILED:// 登录失败消息
                        Log.w("MSG", "msgRspCB-->login is failed");
                        sendMyToast("msgRspCB-->login is failed");
                        break;

                    case SDK_CONSTANT.TPS_MSG_NOTIFY_DEV_DATA:// 登录成功后，接收设备列表
                        List<Device> lst = (List<Device>) new Device().fromXML(pData, "DeviceList");
                        if (lst != null && lst.size() > 0) {
                            Log.i("MSG", "msgRspCB-->recv device list is success:" + lst.toString());
                            sendMyToast("msgRspCB-->recv device list is success");

                            m_modifyUserPwdDev = lst.get(0);
                            // is NVR
                            if (lst.get(0).getDevType() == 200) {
                                setEtStr(R.id.etDeviceID, lst.get(0).getDevId());
                            }
                        } else {
                            Log.w("MSG", "msgRspCB-->recv device list is failed.");
                            sendMyToast("msgRspCB-->recv device list is failed");
                        }
                        break;

                    case SDK_CONSTANT.TPS_MSG_RSP_ADDWATCH: // AddWatchAgent函数请求视频，返回TPS_AddWachtRsp结构
                        if (pData != null && nDataLen == TPS_AddWachtRsp.SIZE) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(nDataLen);
                            byteBuffer.order(ByteOrder.nativeOrder());
                            byteBuffer.put(pData, 0, nDataLen);
                            byteBuffer.rewind();
                            TPS_AddWachtRsp ts = (TPS_AddWachtRsp) TPS_AddWachtRsp.createObjectByByteBuffer(byteBuffer);

                            if (ts != null && ts.getnResult() == 0) {// 视频请求成功
                                String _devID = new String(ts.getSzDevId()).trim();
                                sendMyToast("msgRspCB-->addWatch is success,devid=" + _devID);
                                Log.w("MSG", "msgRspCB-->addWatch is success," + ts.toString());
                                // mediaRecvCB回调就会接收到数据
                                pc = PlayCtrlAgent.getInstance();
                                pc.setIPlayCtrlAgentCB(new IPlayCtrlAgentCB() {
                                    @Override
                                    public int decDataCB(int nPort, byte[] pDecData, int nSize, byte[] pFrameInfo, byte[] pUser) {//解码回调函数
                                        if (!btnIsOpen(R.id.btnShow)) {
                                            Log.i("MSG", "[" + (nPort) + "]decDataCB-->data isn't show:nSize=" + nSize);
                                            return 0;
                                        }
                                        if (nSize > FRAME_INFO.SIZE) {
                                            ByteBuffer bBuf = (ByteBuffer) ByteBuffer.wrap(pFrameInfo).order(ByteOrder.nativeOrder()).rewind();
                                            FRAME_INFO fInfo = (FRAME_INFO) FRAME_INFO.createObjectByByteBuffer(bBuf);
                                            // 处理解码后的媒体数据
                                            if (fInfo != null) {
                                                if (fInfo.getnIsVideo() == 1 && fInfo.getnWidth() > 0 && fInfo.getnHeight() > 0) {//处理视频数据(yuv或rgb数据)
                                                    FrameBuffer buf = new FrameBuffer(fInfo.getnWidth(), fInfo.getnHeight());
                                                    OpenglesRender _glRender = mRenderMap.get(mPortOrIDMap.get(nPort + ""));
                                                    if (_glRender != null) {
                                                        Log.v("MSG", "_glRender is not null...suc");

                                                        // pDecData:TPS_VIDEO_FRAME_HEADER(16)+y+v+u
                                                        int frameSize = nSize;
                                                        byte[] frameData = ByteBuffer.wrap(pDecData, 0, frameSize).array();
                                                        buf.fData = frameData;
                                                        _glRender.updateView(buf);
                                                    } else {
                                                        Log.e("MSG", "_glRender is null...fail");
                                                    }
                                                } else if (fInfo.getnIsVideo() == 0) {//处理音频数据(pcm数据)
                                                /*if(mAudioPlayerAry[0] != null){
                                                    int audioLen = nSize - TPS_AUDIO_PARAM.SIZE;
													byte[] audioData = ByteBuffer.wrap(pDecData,TPS_AUDIO_PARAM.SIZE,audioLen).array();
													AudioBuffer buf = new AudioBuffer(audioData, audioLen);
													mAudioPlayerAry[0].addToBuf(buf);
												}*/
                                                    audioCallback(mPortOrIDMap.get(nPort + ""), pDecData, nSize);
                                                }
                                            }
                                            Log.i("MSG", "decDataCB-->data is success:nSize=" + nSize + "," + fInfo.toString());
                                        } else {
                                            Log.i("MSG", "decDataCB-->data is fail");
                                        }
                                        return 0;
                                    }
                                });

                                //获取端口句柄
                                int port = pc.GetProtAgent();
                                //设置视频流参数
                                byte[] videoParm = ts.getVideoParam().objectToByteBuffer(ByteOrder.nativeOrder()).array();
                                pc.OpenStreamAgent(port, videoParm, videoParm.length, 0/*0:视频 1:音频*/, 25 * 1/*最大缓冲帧数*/);

                                //判断是否有音频，如果有音频才进行打开音频流及配置语音参数
                                if (ts.hasAudio()) {
                                    //设置音频流参数（摄像机端要配置音频编码方式为G711，其它格式暂不支持）
                                    byte[] audioParm = ts.getAudioParam().objectToByteBuffer(ByteOrder.nativeOrder()).array();
                                    pc.OpenStreamAgent(port, audioParm, audioParm.length, 1/*0:视频 1:音频*/, 20/*最大缓冲帧数*/);

                                    // 根据摄像机音频参数，配置AudioPlayer的参数
                                    AudioPlayer audioPlayer = mAudioPlayerMap.get(_devID);
                                    if (audioPlayer != null) {
                                        MyAudioParameter audioParameter = new MyAudioParameter(ts.getAudioParam().getSamplerate(), ts.getAudioParam().getChannels(), ts.getAudioParam().getSamplebitswitdh());
                                        audioPlayer.initAudioParameter(audioParameter);
                                        Log.i("Audio", "Audio is init....");
                                    } else {
                                        Log.w("Audio", "Audio isn't init....");
                                    }
                                }

                                //0表示decDataCB解码出来的为yuv数据, 非0表示对应位数的rgb数据（支持的位数有：16、24、32）
                                pc.PlayAgent(port, 0);

                                //记录端口句柄和设备ID间的映射关系
                                mIDOrPortMap.put(_devID, port);
                                mPortOrIDMap.put(port + "", _devID);
                            } else {// 视频请求失败
                                Log.w("MSG", "msgRspCB-->addWatch is failed.");
                            }
                        }
                        break;
                    case SDK_CONSTANT.TPS_MSG_P2P_SELF_ID:
                        Log.w("MSG", "TPS_MSG_P2P_SELF_ID.");
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_TALK: {//打开对讲时的消息
                        if (pData != null && nDataLen == TPS_TALKRsp.SIZE) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(nDataLen);
                            byteBuffer.order(ByteOrder.nativeOrder());
                            byteBuffer.put(pData, 0, nDataLen);
                            byteBuffer.rewind();
                            TPS_TALKRsp ts = (TPS_TALKRsp) TPS_TALKRsp.createObjectByByteBuffer(byteBuffer);
                            Log.i("MSG", "msgRspCallBack-->TPS_TALKRsp@" + ts.toString());
                            final String _devID = new String(ts.getSzDevId()).trim();

                            // 实现手机音频录制回调
                            mAudioPlayerAry[0].addRecordCallback(new MyRecordCallback() {
                                @Override
                                public void recvRecordData(byte[] data, int length, int reserver) {
                                    // 发送手机录制的音频数据给摄像机
                                    TPS_AudioData audioData = new TPS_AudioData(length, data, reserver);
                                    if (fb != null) {
                                        fb.InputAudioDataAgent(_devID, audioData.objectToByteBuffer(ByteOrder.nativeOrder()).array());
                                    }
                                }
                            });

                            TPS_AUDIO_PARAM audioParm = ts.getAudioParam();

                            if (audioParm != null) {
                                if (ts.getnResult() == 0) {
                                    if (SDK_CONSTANT.AUDIO_TYPE_G711.compareToIgnoreCase(new String(audioParm.getAudio_encoder()).trim()) == 0) {
                                        stopAllVoice();
                                        mAudioPlayerMap.get(_devID).startTalk();
                                    } else {
                                        sendMyToast("打开对讲失败-不支持的音频编码格式(非G711格式)");
                                    }
                                } else {
                                    sendMyToast("打开对讲失败");
                                }
                            }
                        }
                    }
                    break;

                    case SDK_CONSTANT.TPS_MSG_RSP_TALK_CLOSE: {//关闭对讲时的消息
                        if (pData != null && nDataLen == TPS_NotifyInfo.SIZE) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(nDataLen);
                            byteBuffer.order(ByteOrder.nativeOrder());
                            byteBuffer.put(pData, 0, nDataLen);
                            byteBuffer.rewind();
                            TPS_NotifyInfo ts = (TPS_NotifyInfo) TPS_NotifyInfo.createObjectByByteBuffer(byteBuffer);
                            Log.i("MSG", "msgRspCallBack-->TPS_MSG_RSP_TALK_CLOSE@" + ts.toString());
                            if (ts.getnResult() == 0) {
                                mAudioPlayerAry[0].removeRecordCallback();
                                mAudioPlayerAry[0].startOutAudio();
                                sendMyToast("关闭对讲成功");
                            } else {
                                sendMyToast("关闭对讲失败");
                            }
                        }
                        Log.i("talk", "TPS_MSG_RSP_TALK_CLOSE-->" + nDataLen);
                    }

                    case SDK_CONSTANT.TPS_MSG_REC_STOP: {//录像已停止
                        if (pData != null && nDataLen == TPS_NotifyInfo.SIZE) {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(nDataLen);
                            byteBuffer.order(ByteOrder.nativeOrder());
                            byteBuffer.put(pData, 0, nDataLen);
                            byteBuffer.rewind();
                            TPS_NotifyInfo ni = (TPS_NotifyInfo) TPS_NotifyInfo.createObjectByByteBuffer(byteBuffer);
                            Log.i("MSG", "msgRspCallBack-->TPS_MSG_REC_STOP#TPS_NotifyInfo@" + ni.toString());
                            String _devID = new String(ni.getSzDevId()).trim();
                            if (_devID != null && ni.getnResult() == 0) {
                                Log.i("MSG", _devID + "已停止录像");
                            }
                        }
                    }
                    break;
                    case SDK_CONSTANT.TPS_MSG_ALARM:
                        this.onMsgAlarm(pData, nDataLen);
                        break;

                    case SDK_CONSTANT.TPS_MSG_RSP_START_ALERTOR_BIND:
                        sendMyToast("TPS_MSG_RSP_START_ALERTOR_BIND");
                        TPS_NotifyInfo ni = getNotifyInfo(pData, nDataLen);
                        String info = new String(ni.getSzInfo()).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_STOP_ALERTOR_BIND:
                        sendMyToast("TPS_MSG_RSP_STOP_ALERTOR_BIND");
                        ni = getNotifyInfo(pData, nDataLen);
                        info = new String(ni.getSzInfo()).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_GET_ALERTOR_LIST_FAILED:
                        sendMyToast("TPS_MSG_RSP_GET_ALERTOR_LIST_FAILED");
                        ni = getNotifyInfo(pData, nDataLen);
                        info = new String(ni.getSzInfo()).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_GET_ALERTOR_LIST_OK:
                        sendMyToast("TPS_MSG_RSP_GET_ALERTOR_LIST_OK");
                        info = new String(pData, 0, nDataLen).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_DEL_ALERTOR_BIND:
                        sendMyToast("TPS_MSG_RSP_DEL_ALERTOR_BIND");
                        ni = getNotifyInfo(pData, nDataLen);
                        info = new String(ni.getSzInfo()).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_SECURITY_SET:
                        sendMyToast("TPS_MSG_RSP_SECURITY_SET");
                        ni = getNotifyInfo(pData, nDataLen);
                        info = new String(ni.getSzInfo()).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_ALERTOR_ALIAS_SET:
                        sendMyToast("TPS_MSG_RSP_ALERTOR_ALIAS_SET");
                        ni = getNotifyInfo(pData, nDataLen);
                        info = new String(ni.getSzInfo()).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_ALERTOR_PTZ_SET:
                        sendMyToast("TPS_MSG_RSP_ALERTOR_PTZ_SET");
                        ni = getNotifyInfo(pData, nDataLen);
                        info = new String(ni.getSzInfo()).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_NOTIFY_ALERTOR_ALM:
                        sendMyToast("TPS_MSG_NOTIFY_ALERTOR_ALM");
                        info = new String(pData, 0, nDataLen).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_GET_ALM_PIC:
                        sendMyToast("TPS_MSG_RSP_GET_ALM_PIC");
                        ni = getNotifyInfo(pData, nDataLen);
                        info = new String(ni.getSzInfo()).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_SECURITY_GET_FAILED:
                        sendMyToast("TPS_MSG_RSP_SECURITY_GET_FAILED");
                        ni = getNotifyInfo(pData, nDataLen);
                        info = new String(ni.getSzInfo()).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_SECURITY_GET_OK:
                        sendMyToast("TPS_MSG_RSP_SECURITY_GET_OK");
                        info = new String(pData, 0, nDataLen).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_ALARM_CONFIRM:
                        sendMyToast("TPS_MSG_RSP_ALARM_CONFIRM");
                        ni = getNotifyInfo(pData, nDataLen);
                        info = new String(ni.getSzInfo()).trim();
                        sendMyToast(info);
                        break;
                    case SDK_CONSTANT.TPS_MSG_RSP_SEARCH_ALARM:
                        sendMyToast("TPS_MSG_RSP_SEARCH_ALARM");
                        info = new String(pData, 0, nDataLen).trim();
                        sendMyToast(info);
                        break;
                }

                int flag = nMsgType & 0xff000000;
                int cmd_ex = nMsgType & 0x00ffffff;
                switch (cmd_ex) {
                    case NetSDK_CMD_TYPE.CMD_GET_SYSTEM_USER_CONFIG:
                        if (null == pData) return 0;
                        String xml = new String(pData).trim();
                        List<NetSDK_UserAccount> lst = (List<NetSDK_UserAccount>) new NetSDK_UserAccount().fromXML(pData);
                        sendMessageToUI(cmd_ex, flag, 0, lst);
                        return 0;
                    case NetSDK_CMD_TYPE.CMD_SET_SYSTEM_USER_CONFIG:
                        sendMessageToUI(cmd_ex, flag, 0, null);
                        return 0;
                    case 501: // 读取视频参数配置
                        if (null == pData) return 0;
                        xml = new String(pData).trim();
                        NetSDK_Media_Video_Config cfg = (NetSDK_Media_Video_Config) new NetSDK_Media_Video_Config().fromXML(xml);
                        sendMessageToUI(cmd_ex, flag, 0, cfg);
                        return 0;
                    case 523: // 设置视频参数配置
                        sendMessageToUI(cmd_ex, flag, 0, null);
                        break;
                    case 1031: // 读取媒体编码能力
                        if (null == pData) return 0;
                        xml = new String(pData).trim();
                        if ("".equals(xml)) return 0;
                        NetSDK_Media_Capability cap = (NetSDK_Media_Capability) new NetSDK_Media_Capability().fromXML(xml);
                        sendMessageToUI(cmd_ex, flag, 0, cap);
                        return 0;
                    case 800:
                        xml = new String(pData).trim();
                        if ("".equals(xml)) return 0;
                        NetSDK_Alarm_Config config = (NetSDK_Alarm_Config) new NetSDK_Alarm_Config().fromXML(xml);
                        sendMessageToUI(cmd_ex, flag, 0, config);
                        break;
                    case 801:
                        xml = new String(pData).trim();
                        if ("".equals(xml)) return 0;
                        config = new NetSDK_Alarm_Config();
                        NetSDK_Alarm_Config.InputAlarm inputAlarm = (NetSDK_Alarm_Config.InputAlarm) config.fromInputAlarmXML(xml);
                        sendMessageToUI(cmd_ex, flag, 0, inputAlarm);
                        break;
                    case 802:
                        xml = new String(pData).trim();
                        if ("".equals(xml)) return 0;
                        config = new NetSDK_Alarm_Config();
                        NetSDK_Alarm_Config.MotionDetectAlarm motionDetectAlarm = (NetSDK_Alarm_Config.MotionDetectAlarm) config.fromMotionDetectAlarmXML(xml);
                        sendMessageToUI(cmd_ex, flag, 0, motionDetectAlarm);
                        break;
                    case 820:
                        sendMessageToUI(cmd_ex, flag, 0, null);
                        break;
                    case 821:
                        sendMessageToUI(cmd_ex, flag, 0, null);
                        break;
                    case 822:
                        sendMessageToUI(cmd_ex, flag, 0, null);
                        break;
                    case 1021:
                        xml = new String(pData).trim();
                        if ("".equals(xml)) return 0;
                        List<TPS_RecordFile> records = (List<TPS_RecordFile>) new TPS_RecordFile().fromXML(xml);
                        sendMessageToUI(cmd_ex, flag, 0, records);
                        break;
                }

                return 0;
            }

            private void onMsgAlarm(byte[] pData, int nDataLen) {
                if (null == pData) return;
                ByteBuffer byteBuffer = ByteBuffer.allocate(nDataLen);
                byteBuffer.order(ByteOrder.nativeOrder());
                byteBuffer.put(pData, 0, nDataLen);
                byteBuffer.rewind();
                TPS_AlarmInfo ai = (TPS_AlarmInfo) TPS_AlarmInfo.createObjectByByteBuffer(byteBuffer);
                String devID = new String(ai.getSzDevId()).trim();
                String desc = new String(ai.getSzDesc()).trim();
                sendMyToast("接收到报警消息，设备ID＝" + devID + "，描述＝" + desc);
            }

            @Override
            public int mediaRecvCB(byte[] pDevId, int nMediaType, byte[] pFrameData, int nDataLen, byte[] pExtData) {// 媒体回调
                if (!btnIsOpen(R.id.btnDecode)) {
                    Log.v("MSG", "[" + (new String(pDevId).trim()) + "]mediaRecvCB-->recvdata video isn't decode,nDataLen=" + nDataLen);
                    return 0;
                }
                if (nMediaType == 0) {// 接收视频数据，H264格式
                    if (pc != null && nDataLen > 0 && pExtData != null) {
                        ByteBuffer _buf = (ByteBuffer) ByteBuffer.wrap(pExtData).order(ByteOrder.nativeOrder()).rewind();
                        TPS_EXT_DATA extDATA = (TPS_EXT_DATA) TPS_EXT_DATA.createObjectByByteBuffer(_buf);
                        Log.w("MSG", "mediaRecvCB-->recvdata video is success,nDataLen=" + nDataLen + ",frameHead=" + extDATA);
                        if (extDATA != null) {
                            int isKey = extDATA.getbIsKey();
                            final String _devID = new String(pDevId).trim();

                            //接收数据交给底层库解码，解码回调函数为：decDataCB（如需要自己处理，可不调用此函数）
                            pc.InputVideoDataAgent(mIDOrPortMap.get(_devID), pFrameData, nDataLen, isKey, (int) extDATA.getTimestamp());
                        }
                    } else {
                        Log.w("MSG", "mediaRecvCB-->recvdata video is failed.");
                    }
                } else if (nMediaType == 1) {// 接收音频数据，G711格式
                    if (pc != null && nDataLen > 0 && pExtData != null) {
                        ByteBuffer _buf = (ByteBuffer) ByteBuffer.wrap(pExtData).order(ByteOrder.nativeOrder()).rewind();
                        TPS_EXT_DATA extDATA = (TPS_EXT_DATA) TPS_EXT_DATA.createObjectByByteBuffer(_buf);
                        Log.w("MSG", "mediaRecvCB-->recvdata audio is success,nDataLen=" + nDataLen);
                        if (extDATA != null) {
                            final String _devID = new String(pDevId).trim();

                            //接收数据交给底层库解码，解码回调函数为：decDataCB（如需要自己处理，可不调用此函数）
                            pc.InputAudioDataAgent(mIDOrPortMap.get(_devID), pFrameData, nDataLen, (int) extDATA.getTimestamp());
                        }
                    } else {
                        Log.w("MSG", "mediaRecvCB-->recvdata audio is failed.");
                    }
                }
                return 0;
            }
        });

        Log.i("MSG", "testDemo is end...");
    }

    @SuppressLint("SdCardPath")
    @Override
    public void onClick(View v) {
        devID[0] = getEtStr(R.id.etDeviceID);
        name = getEtStr(R.id.etUserName);
        pwd = getEtStr(R.id.etUserPwd);

        if (v.getId() != R.id.btnInit) {
            if (fb == null) {
                sendMyToast("请先初始化！");
                return;
            }
        }

        switch (v.getId()) {
            case R.id.btnInit:
                testSDK();
                sendMyToast("初始化成功！");
                break;
            case R.id.btnUninit:
                destroy();
                sendMyToast("销毁成功！");
                break;
            case R.id.btnLogin:// 登录
                if (fb == null) {
                    sendMyToast("请先初始化！");
                    break;
                }
                for (int i = 0; i < MAX_PLAY_NUMS; i++) {
                    // 记录设备ID与渲染类间映射关系
                    mGlRenderAry[i].start();
                    mRenderMap.put(devID[i], mGlRenderAry[i]);
                    mAudioPlayerMap.put(devID[i], mAudioPlayerAry[i]);

                    // admin：设备用户名（设备出厂时的默认值）
                    // 123456：设备密码（设备出厂时的默认值）
                    // 100134：为设备的云ID，设备注册云服务时自动生成的唯一ID
                    // 80：云服务端口（未自己搭建服务器的，使用此默认值）
                    mLoginFlags[i] = fb.LoginAgent(name, pwd, devID[i], (short) 80);
                    m_streamMap.put(devID[i], 1);
                }
                break;
            case R.id.btnLogoff:// 注销
                if (fb != null) {
                    for (int i = 0; i < mIDOrPortMap.size(); i++) {
                        closeOneVide(mIDOrPortMap.get(devID[i]), devID[i]);
                    }

                    //TODO:只需要在退出时调用一次就可以了。
                    //fb.LogoutAgent();
                }
                sendMyToast("关闭视频并注销");
                break;

            case R.id.btnShot://启动抓拍
                if (mGlRenderAry[0] != null) {
                    String strDate = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Date());
                    String fileName = "/sdcard/netsdk/" + devID[0] + "_" + strDate + ".jpg";
                    boolean isShotOK = mGlRenderAry[0].startShot(fileName);//fileName为文件绝对路径+文件名
                    if (isShotOK) {
                        sendMyToast("启动抓拍,文件存储于:" + fileName);
                    } else {
                        sendMyToast("未获取到视频数据。");
                    }
                }
                break;

            case R.id.btnStartVideo://启动视频
                for (int i = 0; i < MAX_PLAY_NUMS; i++) {
                    if (mLoginFlags[i] == 0) {//登录成功后启动视频
                        sendMyToast("登录成功开始请求视频");
                        // 100134：为设备的云ID，设备注册云服务时自动生成的唯一ID
                        // nStreamNo：0主码流，1子码流
                        // nFrameType：0表示请求播放所有帧，1表示只请求播放关键帧
                        int ret = fb.AddWatchAgent(devID[i], m_streamMap.get(devID[i]), /* nFrameType */0);
                        if (ret != 0) {
                            sendMyToast(devID[i] + ":请求视频流失败，ret=" + ret);
                        }
                    } else {
                        sendMyToast("请先登录" + devID[i]);
                    }
                }
                break;
            case R.id.btnStopVideo: // 停止视频
                for (int i = 0; i < mIDOrPortMap.size(); i++) {
                    closeOneVide(mIDOrPortMap.get(devID[i]), devID[i]);
                }
                sendMyToast("停止视频");
                break;

            case R.id.btnStartRecord://录像
                if (fb != null) {
                    String path = "/sdcard/netsdk/";
                    File f = new File(path);
                    if (!f.exists()) {
                        boolean isOK = f.mkdirs();
                        Log.e("MSG", path + " mkdirs is ..." + isOK);
                    }
                    //第二个参数传一个目录路径即可
                    fb.StartRecordAgent(devID[0], path, 60/**每60秒拆分一个录像文件 */);
                    sendMyToast("启动录像，录像目录在" + path);
                }
                break;
            case R.id.btnStopRecord:
                if (fb != null) {
                    fb.StopRecordAgent(devID[0]);
                    sendMyToast("停止录像");
                }
                break;
            case R.id.btnModifyDevUserPwd:
                if (null == m_modifyUserPwdDev) return;
                if (m_modifyUserPwdDev.getDevType() == 200) {
                    sendMyToast("不能修改NVR设备的密码。");
                    return;
                }

                modifyUserPwd(m_modifyUserPwdDev);
                break;
            case R.id.btnOpenVoice: {//监听
                if (mAudioPlayerAry[0] != null) {
                    mAudioPlayerAry[0].startOutAudio();
                    sendMyToast("打开监听");
                }
            }
            break;
            case R.id.btnCloseVoice: {
                if (mAudioPlayerAry[0] != null) {
                    mAudioPlayerAry[0].stopOutAudio();
                    sendMyToast("关闭监听");
                }
            }
            break;
            case R.id.btnOpenTalk: {//对讲
                if (fb != null) {
                    fb.StartTalkAgent(devID[0]);
                    sendMyToast("打开对讲");
                }
            }
            break;
            case R.id.btnCloseTalk: {
                if (mAudioPlayerAry[0] != null) {
                    mAudioPlayerAry[0].stopTalk();
                }
                if (fb != null) {
                    fb.StopTalkAgent(devID[0]);
                    sendMyToast("关闭对讲");
                }
            }
            break;
            case R.id.btnMainStream:
                onClick(findViewById(R.id.btnStopVideo));
                m_streamMap.put(devID[0], 0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                onClick(findViewById(R.id.btnStartVideo));
                break;
            case R.id.btnSubStream:
                onClick(findViewById(R.id.btnStopVideo));
                m_streamMap.put(devID[0], 1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                onClick(findViewById(R.id.btnStartVideo));
                break;
            case R.id.btnPtzUp://云台控制
                if (fb != null) {
                    //SDK_CONSTANT.PTZ_UP/SDK_CONSTANT.PTZ_RIGHT_DOWN/SDK_CONSTANT.PTZ_LEFT/SDK_CONSTANT.PTZ_RIGHT更多参考SDK_CONSTANT类
                    String _ptzXml = new TPS_PtzInfo(SDK_CONSTANT.PTZ_UP, 5/*云台转动速度1-10*/, 5).toXMLString();
                    fb.PTZActionAgent(devID[0], _ptzXml);
                    sendMyToast("云台向上");
                }
                break;
            case R.id.btnPtzDown://云台控制
                if (fb != null) {
                    //SDK_CONSTANT.PTZ_UP/SDK_CONSTANT.PTZ_RIGHT_DOWN/SDK_CONSTANT.PTZ_LEFT/SDK_CONSTANT.PTZ_RIGHT更多参考SDK_CONSTANT类
                    String _ptzXml = new TPS_PtzInfo(SDK_CONSTANT.PTZ_DOWN, 5/*云台转动速度1-10*/, 5).toXMLString();
                    fb.PTZActionAgent(devID[0], _ptzXml);
                    sendMyToast("云台向下");
                }
                break;
            case R.id.btnPtzLeft://云台控制
                if (fb != null) {
                    //SDK_CONSTANT.PTZ_UP/SDK_CONSTANT.PTZ_RIGHT_DOWN/SDK_CONSTANT.PTZ_LEFT/SDK_CONSTANT.PTZ_RIGHT更多参考SDK_CONSTANT类
                    String _ptzXml = new TPS_PtzInfo(SDK_CONSTANT.PTZ_LEFT, 5/*云台转动速度1-10*/, 5).toXMLString();
                    fb.PTZActionAgent(devID[0], _ptzXml);
                    sendMyToast("云台向左");
                }
                break;
            case R.id.btnPtzRight://云台控制
                if (fb != null) {
                    //SDK_CONSTANT.PTZ_UP/SDK_CONSTANT.PTZ_RIGHT_DOWN/SDK_CONSTANT.PTZ_LEFT/SDK_CONSTANT.PTZ_RIGHT更多参考SDK_CONSTANT类
                    String _ptzXml = new TPS_PtzInfo(SDK_CONSTANT.PTZ_RIGHT, 5/*云台转动速度1-10*/, 5).toXMLString();
                    fb.PTZActionAgent(devID[0], _ptzXml);
                    sendMyToast("云台向右");
                }
                break;

            case R.id.btnPtzAuto://自动巡航
                if (fb != null) {
                    //启动巡航：SDK_CONSTANT.PTZ_LEFT、停止巡航：SDK_CONSTANT.PTZ_STOP
                    String _ptzXml = new TPS_PtzInfo(SDK_CONSTANT.PTZ_AUTO, 5/*云台转动速度1-10*/, 5,
                            false/*是否延时，为true时延时3秒*/).toXMLString();
                    fb.PTZActionAgent(devID[0], _ptzXml);
                    sendMyToast("启动自动巡航");
                }
                break;
            case R.id.btnGetMediaCapability:
                showTipDlg("获取设备媒体能力...", 20000, "获取设备媒体能力超时。");
                int ret = FunclibAgent.getInstance().P2PDevSystemControl(devID[0], 1031, "");
                break;
            case R.id.btnGetMediaParam:
                showTipDlg("获取设备媒体参数...", 20000, "获取设备媒体参数超时。");
                ret = FunclibAgent.getInstance().GetP2PDevConfig(devID[0], 501);
                break;
            case R.id.btnGetAlarmConfig:
                showTipDlg("获取告警参数...", 20000, "获取告警参数超时。");
                ret = FunclibAgent.getInstance().GetP2PDevConfig(devID[0], 800);
                break;
            case R.id.btnGetInputAlarm:
                showTipDlg("获取IO输入告警参数...", 20000, "获取IO输入告警参数超时。");
                ret = FunclibAgent.getInstance().GetP2PDevConfig(devID[0], 801);
                break;
            case R.id.btnGetMotionDetectAlarm:
                //testAlarmProject();
                showTipDlg("获取移动侦测告警参数...", 20000, "获取移动侦测告警参数超时。");
                ret = FunclibAgent.getInstance().GetP2PDevConfig(devID[0], 802);
                break;
            case R.id.btnGetFrontEndRecord:
                onGetFrontEndRecord();
                break;
            case R.id.btnReplayFrontEndRecord:
                break;
            case R.id.btnStartAlertorBind:
                onStartAlertorBind();
                break;
            case R.id.btnStopAlertorBind:
                onStopAlertorBind();
                break;
            case R.id.btnGetAlertorList:
                onGetAlertorList();
                break;
            case R.id.btnDelAlertorBind:
                onDelAlertorBind();
                break;
            case R.id.btnSetSecurity1:
                onSetSecurity1();
                break;
            case R.id.btnSetSecurity0:
                onSetSecurity0();
                break;
            case R.id.btnGetSecurity:
                onGetSecurity();
                break;
            case R.id.btnSetAlertorAlias:
                onSetAlertorAlias();
                break;
            case R.id.btnSetAlertorPreset:
                onSetAlertorPreset();
                break;
            case R.id.btnGetAlmPicture:
                onGetAlmPicture();
                break;
            case R.id.btnConfirmAlertorAlm:
                onConfirmAlertorAlm();
                break;
            default:
                break;
        }
    }

    private void onGetFrontEndRecord() {
        Calendar c = Calendar.getInstance();
        Date dt = c.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String date = df.format(dt);
        String startTime = date + " 00:00:00";
        String endTime = date + " 23:59:59";
        getFrontEndRecord(startTime, endTime);
    }

    /**
     * 关闭一路视频
     *
     * @param port  端口句柄
     * @param devid 设备ID
     */
    public void closeOneVide(int port, String devid) {
        if (fb != null && pc != null) {
            pc.StopAgent(port);
            pc.FreeProtAgent(port);
            fb.StopTalkAgent(devid);
            fb.StopWatchAgent(devid);
        }
    }

    public void stopAllVideo() {
        for (int i = 0; i < mIDOrPortMap.size(); i++) {
            closeOneVide(mIDOrPortMap.get(devID[i]), devID[i]);
        }
    }

    public void destroy() {
        stopAllVideo();
        for (int i = 0; i < MaxBranch; i++) {
            if (null != mAudioPlayerAry[i]) {
                mAudioPlayerAry[i].stopInAudio();
                mAudioPlayerAry[i].stopOutAudio();
            }
        }

        fb.setIFunclibAgentCB(null);
        pc.setIPlayCtrlAgentCB(null);

        mRenderMap.clear();
        mAudioPlayerMap.clear();
        m_streamMap.clear();
        mIDOrPortMap.clear();
        mPortOrIDMap.clear();
        for (int i = 0; i < MAX_PLAY_NUMS; i++) {
            mLoginFlags[i] = -1;
        }

        //TODO:在整个程序退出时调用一次就可以了，这里只是测试。
        PlayCtrlAgent.getInstance().free();
        FunclibAgent.getInstance().free();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroy();
        for (int i = 0; i < MaxBranch; i++) {
            if (null == mGlRenderAry[i]) continue;
            mGlRenderAry[i].destory();
        }
        SystemClock.sleep(10);
//		android.os.Process.killProcess(android.os.Process.myPid()); // Dalvik VM的本地方法
//		System.exit(0);
    }

    /**
     * 处理手机重力感应及屏幕旋转
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("MSG", "MainYuvShowUI@onConfigurationChanged is called...");
        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 启动全屏
            findViewById(R.id.llCtrlInfo).setVisibility(View.GONE);
            findViewById(R.id.llPlayCtrl).setVisibility(View.GONE);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 退出全屏
            findViewById(R.id.llCtrlInfo).setVisibility(View.VISIBLE);
            findViewById(R.id.llPlayCtrl).setVisibility(View.VISIBLE);
        }
    }

    /**
     * ###########################Test...begin##############################
     */
    public void btnTagClick(View v) {
        setBtnTag(v.getId(), 0);
        switch (v.getId()) {
            case R.id.btnDecode:
            case R.id.btnShow: {
                if (btnIsOpen(R.id.btnDecode) && btnIsOpen(R.id.btnShow)) {
                    setBtnTag(R.id.btnDecShow, 1);
                } else {
                    setBtnTag(R.id.btnDecShow, -1);
                }
            }
            break;

            case R.id.btnDecShow: {
                if (btnIsOpen(R.id.btnDecShow)) {
                    setBtnTag(R.id.btnDecode, 1);
                    setBtnTag(R.id.btnShow, 1);
                } else {
                    setBtnTag(R.id.btnDecode, -1);
                    setBtnTag(R.id.btnShow, -1);
                }
            }
            break;

            case R.id.btnOneVideo: {
                MAX_PLAY_NUMS = 1;
                if (btnIsOpen(R.id.btnOneVideo)) {
                    setBtnTag(R.id.btnFourVideo, -1);
                    for (int i = 1; i < MaxBranch; i++) findViewById(mGlViewRes[i]).setVisibility(View.GONE);
                    findViewById(R.id.llVideo2).setVisibility(View.GONE);
                } else {
                    setBtnTag(R.id.btnFourVideo, 1);
                    for (int i = 0; i < MaxBranch; i++) findViewById(mGlViewRes[i]).setVisibility(View.VISIBLE);
                    findViewById(R.id.llVideo2).setVisibility(View.VISIBLE);
                }

            }
            break;
            case R.id.btnFourVideo: {
                MAX_PLAY_NUMS = 4;
                if (btnIsOpen(R.id.btnFourVideo)) {
                    setBtnTag(R.id.btnOneVideo, -1);
                    for (int i = 0; i < MaxBranch; i++) findViewById(mGlViewRes[i]).setVisibility(View.VISIBLE);
                    findViewById(R.id.llVideo2).setVisibility(View.VISIBLE);
                } else {
                    setBtnTag(R.id.btnOneVideo, 1);
                    for (int i = 1; i < MaxBranch; i++) findViewById(mGlViewRes[i]).setVisibility(View.GONE);
                    findViewById(R.id.llVideo2).setVisibility(View.GONE);
                }
            }
            break;
        }
    }

    public void setBtnTag(int resID, int isForce) {
        Button btn = (Button) findViewById(resID);
        if (isForce == 1) {
            btn.setTag("open");
            btn.setBackgroundColor(0xff00cc00);
        } else if (isForce == -1) {
            btn.setTag("");
            btn.setBackgroundColor(0xffcc0000);
        } else {
            if (TextUtils.isEmpty((String) btn.getTag())) {
                btn.setTag("open");
                btn.setBackgroundColor(0xff00cc00);
            } else {
                btn.setTag("");
                btn.setBackgroundColor(0xffcc0000);
            }
        }
    }

    public boolean btnIsOpen(int resID) {
        Button btn = (Button) findViewById(resID);
        return !TextUtils.isEmpty((String) btn.getTag());
    }

    /**
     * ###########################Test...end################################
     */


    public Handler mhHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NetSDK_CMD_TYPE.CMD_GET_SYSTEM_USER_CONFIG:
                    onGetUserConfig(msg.arg1, (List<NetSDK_UserAccount>) msg.obj);
                    break;
                case NetSDK_CMD_TYPE.CMD_SET_SYSTEM_USER_CONFIG:
                    onSetUserConfig(msg.arg1);
                    break;
                case SDK_CONSTANT.TPS_MSG_NOTIFY_LOGIN_FAILED:
                    onLoginFailed((Device) msg.obj);
                    break;
                case 501: // 读取视频参数配置
                    onGetVideoParam(msg.arg1, (NetSDK_Media_Video_Config) msg.obj);
                    break;
                case 523: // 设置视频参数配置
                    onSetVideoParam(msg.arg1);
                    break;
                case 1031: // 读取媒体编码能力
                    onGetMediaCapability(msg.arg1, (NetSDK_Media_Capability) msg.obj);
                    break;
                case 800:
                    onGetAlarmConfig(msg.arg1, (NetSDK_Alarm_Config) msg.obj);
                    break;
                case 801:
                    onGetIOInputAlarm(msg.arg1, (NetSDK_Alarm_Config.InputAlarm) msg.obj);
                    break;
                case 802:
                    onGetMotionDetectAlarm(msg.arg1, (NetSDK_Alarm_Config.MotionDetectAlarm) msg.obj);
                    break;
                case 820:
                    onSetAlarmConfig(msg.arg1);
                    break;
                case 821:
                    onSetIOInputAlarm(msg.arg1);
                    break;
                case 822:
                    onSetMotionDetect(msg.arg1);
                    break;
                case 1021:
                    onGetFrontEndRecordResp(msg.arg1, (List<TPS_RecordFile>) msg.obj);
            }
        }
    };

    private MyProgressDialog mTipDlg;

    public void showTipDlg(String resId, int timeout, String timeoutMsg) {
        mTipDlg.setTitle(resId);
        mTipDlg.setTimeoutToast(timeoutMsg);
        mTipDlg.setTimeoutCallback(new MyProgressDialog.ITimeoutCallback() {
            @Override
            public void onTimeout() {

            }
        });
        mTipDlg.show(timeout);
    }

    public void modifyUserPwd(final Device dev) {
        int ret = FunclibAgent.getInstance().GetP2PDevConfig(dev.getDevId(), NetSDK_CMD_TYPE.CMD_GET_SYSTEM_USER_CONFIG);
        if (0 == ret) return;
        sendMyToast("获取设备用户列表失败。");
    }

    /**
     * 设置用户列表响应
     *
     * @param flag 设置是否成功
     */
    private void onSetUserConfig(int flag) {
        if (0 != flag) {
            m_modifyInfo = null;
            sendMyToast("设置设备信息失败。");
            return;
        }

        final Device dev = m_modifyUserPwdDev;
        if (null == dev) return;
        // 已经设置新的用户信息，再次发送获取请求并验证
        int ret = FunclibAgent.getInstance().GetP2PDevConfig(dev.getDevId(), NetSDK_CMD_TYPE.CMD_GET_SYSTEM_USER_CONFIG);
        if (0 != ret) {
            sendMyToast("设置设备信息失败。");
        }
    }

    private AlertDialog m_modify_user_pwd_dlg = null;

    /**
     * 获取设备用户列表并修改用户名和密码
     *
     * @param flag 获取是否成功
     * @param obj  设备的用户列表
     */
    private void onGetUserConfig(int flag, List<NetSDK_UserAccount> obj) {
        final List<NetSDK_UserAccount> lstUser = obj;
        final Device dev = m_modifyUserPwdDev;
        if (null == dev) return;
        if (null != m_modify_user_pwd_dlg && m_modify_user_pwd_dlg.isShowing()) return;
        if (0 != flag || lstUser.isEmpty()) {
            sendMyToast("获取设备用户列表失败。");
            return;
        }

        // 已经设置新的用户信息，再次获取并验证
        if (null != m_modifyInfo) {
            boolean found = false;
            for (NetSDK_UserAccount u : lstUser) {
                if (!u.getUserName().equals(m_modifyInfo.getUserName())
                        || !u.getPassword().equals(m_modifyInfo.getUserPassword())) continue;
                found = true;
                break;
            }

            DeviceInfo info = m_modifyInfo;
            m_modifyInfo = null;
            if (!found) {
                sendMyToast("设置设备信息失败。");
                return;
            }

            // 验证设置成功，调用更新函数通知云平台同步修改
            int ret = FunclibAgent.getInstance().ModifyDevPassword(dev.getDevId(), info.getUserName(), info.getUserPassword());
            if (0 != ret) {
                sendMyToast("设置设备信息失败。");
                return;
            }

            sendMyToast("设置设备信息成功。");
            return;
        }

        //LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.modify_user_pwd, null);

        final EditText etUser = new EditText(this);
        etUser.setHint("输入用户名");
        etUser.setPadding(10, 10, 10, 10);
        etUser.setSingleLine(true);
        etUser.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_FILTER | EditorInfo.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        etUser.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});

        final EditText etPwd = new EditText(this);
        etPwd.setHint("输入密码");
        etPwd.setPadding(10, 10, 10, 10);
        etPwd.setSingleLine(true);
        etPwd.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        etPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});

        final EditText etNewUser = new EditText(this);
        etNewUser.setHint("输入新用户名");
        etNewUser.setPadding(10, 10, 10, 10);
        etNewUser.setSingleLine(true);
        etNewUser.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        etNewUser.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});

        final EditText etNewPwd = new EditText(this);
        etNewPwd.setHint("输入新密码");
        etNewPwd.setPadding(10, 10, 10, 10);
        etNewPwd.setSingleLine(true);
        etNewPwd.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        etNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});

        final EditText etNewPwd2 = new EditText(this);
        etNewPwd2.setHint("确认新密码");
        etNewPwd2.setPadding(10, 10, 10, 10);
        etNewPwd2.setSingleLine(true);
        etNewPwd2.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        etNewPwd2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(5, 0, 5, 0);
        layout.setBackgroundColor(Color.rgb(207, 232, 179));
        layout.addView(etUser);
        layout.addView(etPwd);
        layout.addView(etNewUser);
        layout.addView(etNewPwd);
        layout.addView(etNewPwd2);

        if (null != m_modify_user_pwd_dlg) {
            m_modify_user_pwd_dlg.show();
            return;
        }

        m_modify_user_pwd_dlg = new AlertDialog.Builder(this).setTitle("修改设备用户名密码")
                .setView(layout)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, true);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userName = etUser.getText().toString();
                        String password = etPwd.getText().toString();
                        String newUser = etNewUser.getText().toString();
                        String newPwd = etNewPwd.getText().toString();
                        String newPwd2 = etNewPwd2.getText().toString();
                        if ("".equals(userName) || "".equals(password) || "".equals(newUser)) {
                            try {
                                Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                field.setAccessible(true);
                                field.set(dialog, false);
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                e.printStackTrace();
                            }

                            return;
                        }

                        boolean found = false;
                        // 找到要修改的用户
                        NetSDK_UserAccount foundUser = null;
                        for (NetSDK_UserAccount u : lstUser) {
                            if (!u.getUserName().equals(userName) || !u.getPassword().equals(password)) continue;
                            foundUser = u;
                            found = true;
                            break;
                        }

                        if (!found) {
                            // 未找到，提示重新输入
                            try {
                                Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                field.setAccessible(true);
                                field.set(dialog, false);
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                e.printStackTrace();
                            }

                            sendMyToast("设备原用户名或密码不正确。");
                            return;
                        }

                        // 是否需要修改密码
                        if ("".equals(newPwd) && "".equals(newPwd2)) {
                            newPwd = password;
                        } else if (!newPwd.equals(newPwd2)) {
                            try {
                                Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                field.setAccessible(true);
                                field.set(dialog, false);
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                e.printStackTrace();
                            }

                            sendMyToast("两次输入的密码不同，请重新输入。");
                            return;
                        }

                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, true);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                        m_modifyInfo = new DeviceInfo();
                        m_modifyInfo.setUserName(newUser);
                        m_modifyInfo.setUserPassword(newPwd);
                        foundUser.setUserName(newUser);
                        foundUser.setPassword(newPwd);
                        List<AbstractDataSerialBase> lst = new ArrayList<AbstractDataSerialBase>();
                        lst.add(foundUser);
                        foundUser.addHead(false);
                        String xml = foundUser.toXMLString(lst, "UserConfig");
                        // 设置设备用户信息
                        int ret = FunclibAgent.getInstance().SetP2PDevConfig(dev.getDevId(), NetSDK_CMD_TYPE.CMD_SET_SYSTEM_USER_CONFIG, xml);
                        if (0 != ret) {
                            sendMyToast("设置设备信息失败。");
                            return;
                        }
                    }
                }).create();
        m_modify_user_pwd_dlg.show();
    }

    private AlertDialog m_input_user_pwd_dlg = null;

    // 登录设备失败，提示用户输入正确的用户名密码
    private void onLoginFailed(final Device dev) {
        if (isFinishing()) return;
        if (null != m_input_user_pwd_dlg && m_input_user_pwd_dlg.isShowing()) return;
        final EditText etUser = new EditText(this);
        etUser.setHint("输入用户名");
        etUser.setPadding(10, 10, 10, 10);
        etUser.setSingleLine(true);
        etUser.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_FILTER | EditorInfo.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        etUser.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});

        final EditText etPwd = new EditText(this);
        etPwd.setHint("输入密码");
        etPwd.setPadding(10, 10, 10, 10);
        etPwd.setSingleLine(true);
        etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etPwd.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        etPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(5, 0, 5, 0);
        layout.setBackgroundColor(Color.rgb(207, 232, 179));
        layout.addView(etUser);
        layout.addView(etPwd);

        if (null != m_input_user_pwd_dlg) {
            m_input_user_pwd_dlg.show();
            return;
        }

        m_input_user_pwd_dlg = new AlertDialog.Builder(this).setTitle("输入设备用户名和密码")
                .setView(layout)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, true);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userName = etUser.getText().toString();
                        String password = etPwd.getText().toString();
                        if ("".equals(userName) || "".equals(password)) {
                            try {
                                Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                field.setAccessible(true);
                                field.set(dialog, false);
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                e.printStackTrace();
                            }

                            return;
                        }

                        try {
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            field.set(dialog, true);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                        int ret = FunclibAgent.getInstance().ModifyDevPassword(dev.getDevId(), userName, password);
                        if (0 != ret) {
                            sendMyToast("设置设备信息失败。");
                            return;
                        }

                        sendMyToast("设置设备信息成功。");
                        // 再次进行修改用户名和密码
                        modifyUserPwd(m_modifyUserPwdDev);
                    }
                }).create();
        m_input_user_pwd_dlg.show();
    }

    private void onGetMediaCapability(int flag, NetSDK_Media_Capability cap) {
        mTipDlg.dismiss();
        if (flag != 0 || null == cap) return;
        m_media_caps = cap;
        sendMyToast("获取媒体能力成功。");
    }

    private void onGetVideoParam(int flag, NetSDK_Media_Video_Config cfg) {
        mTipDlg.dismiss();
        if (flag != 0 || null == cfg) return;
        m_video_config = cfg;
        if (cfg.encode.EncodeList.size() < 2) {
            Log.e(TAG, "onGetVideoParam, EncodeList is empty");
            return;
        }

        m_new_video_config = (NetSDK_Media_Video_Config) m_video_config.clone();
        m_video_config.addHead(false);
        String xml = m_video_config.getEncodeXMLString();
        int ret = FunclibAgent.getInstance().SetP2PDevConfig(devID[0], 523, xml);
        if (0 != ret) {
            sendMyToast("设置媒体参数失败。");
        }

        xml = m_video_config.getCaptureXMLString();
        ret = FunclibAgent.getInstance().SetP2PDevConfig(devID[0], 524, xml);
        if (0 != ret) {
            sendMyToast("设置采集参数失败。");
        }
    }

    private void onSetVideoParam(int flag) {
        mTipDlg.dismiss();
        if (0 != flag) {
            sendMyToast("设置媒体参数失败。");
        } else {
            sendMyToast("设置媒体参数成功。");
        }
    }

    private void onGetMotionDetectAlarm(int flag, NetSDK_Alarm_Config.MotionDetectAlarm obj) {
        mTipDlg.dismiss();
        if (0 != flag) {
            sendMyToast("获取移动侦测告警参数失败。");
        } else {
            sendMyToast("获取移动侦测告警参数成功。");
            NetSDK_Alarm_Config config = new NetSDK_Alarm_Config();
            config.addHead(false);
            config.motionDetectAlarm = obj;
            NetSDK_Alarm_Config config1 = (NetSDK_Alarm_Config) config.clone();
            config1.motionDetectAlarm.Enable = Integer.parseInt(config.motionDetectAlarm.Enable) == 0 ? "1" : "0";
            String xml = config1.getMotionDetectAlarmXMLString();
            int ret = FunclibAgent.getInstance().SetP2PDevConfig(devID[0], 822, xml);
            if (0 != ret) {
                sendMyToast("设置移动侦测告警参数失败。");
                return;
            }

            showTipDlg("设置移动侦测告警参数...", 20000, "设置移动侦测告警参数超时。");
        }
    }

    private void onGetIOInputAlarm(int flag, NetSDK_Alarm_Config.InputAlarm obj) {
        mTipDlg.dismiss();
        if (0 != flag) {
            sendMyToast("获取IO输入告警参数失败。");
        } else {
            sendMyToast("获取IO输入告警参数成功。");
            NetSDK_Alarm_Config config = new NetSDK_Alarm_Config();
            config.addHead(false);
            config.inputAlarm = obj;
            String xml = config.getInputAlarmXMLString();
            int ret = FunclibAgent.getInstance().SetP2PDevConfig(devID[0], 821, xml);
            if (0 != ret) {
                sendMyToast("设置IO输入告警参数失败。");
                return;
            }

            showTipDlg("设置IO输入告警参数...", 20000, "设置IO输入告警参数超时。");
        }
    }

    private void onGetAlarmConfig(int flag, NetSDK_Alarm_Config obj) {
        mTipDlg.dismiss();
        if (0 != flag) {
            sendMyToast("获取告警参数失败。");
        } else {
            sendMyToast("获取告警参数成功。");
            obj.addHead(false);
            String xml = obj.toXMLString();
            int ret = FunclibAgent.getInstance().SetP2PDevConfig(devID[0], 820, xml);
            if (0 != ret) {
                sendMyToast("设置告警参数失败。");
                return;
            }

            showTipDlg("设置告警参数...", 20000, "设置告警参数超时。");
        }
    }

    private void onSetMotionDetect(int flag) {
        mTipDlg.dismiss();
        if (0 != flag) {
            sendMyToast("设置移动侦测告警参数失败。");
        } else {
            sendMyToast("设置移动侦测告警参数成功。");
        }
    }

    private void onSetIOInputAlarm(int flag) {
        mTipDlg.dismiss();
        if (0 != flag) {
            sendMyToast("设置IO输入告警参数失败。");
        } else {
            sendMyToast("设置IO输入告警参数成功。");
        }
    }

    private void onSetAlarmConfig(int flag) {
        mTipDlg.dismiss();
        if (0 != flag) {
            sendMyToast("设置告警参数失败。");
        } else {
            sendMyToast("设置告警参数成功。");
        }
    }

    private void getFrontEndRecord(String startTime, String endTime) {
        String req = "<REQUEST_PARAM\n" +
                "RecordMode=\"ALL\"\n" +
                "StartTime=\"" + startTime + "\"\n" +
                "EndTime=\"" + endTime + "\"\n" +
                "MediaType=\"AUDIOVIDEO\"\n" +
                "StreamIndex=\"1\"\n" +
                "MinSize=\"-1\"\n" +
                "MaxSize=\"-1\"\n" +
                "Page=\"0\"\n" + "/>";
        int ret = FunclibAgent.getInstance().P2PDevSystemControl(devID[0], 1021, req);
    }

    private void onGetFrontEndRecordResp(int flags, List<TPS_RecordFile> records) {
        int i = 10;
    }

    private TPS_NotifyInfo getNotifyInfo(byte[] pData, int nDataLen) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(nDataLen);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.put(pData, 0, nDataLen);
        byteBuffer.rewind();
        TPS_NotifyInfo ni = (TPS_NotifyInfo) TPS_NotifyInfo.createObjectByByteBuffer(byteBuffer);
        return ni;
    }

    private void onStartAlertorBind() {
        int ret = FunclibAgent.getInstance().StartAlertorBind(devID[0]);
        if (ret != 0) {
            sendMyToast("函数调用失败");
        }
    }

    private void onStopAlertorBind() {
        int ret = FunclibAgent.getInstance().StopAlertorBind(devID[0]);
        if (ret != 0) {
            sendMyToast("函数调用失败");
        }
    }

    private void onGetAlertorList() {
        int ret = FunclibAgent.getInstance().GetAlertorList(devID[0]);
        if (ret != 0) {
            sendMyToast("函数调用失败");
        }
    }

    private void onDelAlertorBind() {
        int ret = FunclibAgent.getInstance().DelAlertorBind(devID[0], "");
        if (ret != 0) {
            sendMyToast("函数调用失败");
        }
    }

    private void onSetSecurity1() {
        int ret = FunclibAgent.getInstance().SetSecurity(devID[0], "", 1, 1);
        if (ret != 0) {
            sendMyToast("函数调用失败");
        }
    }

    private void onSetSecurity0() {
        int ret = FunclibAgent.getInstance().SetSecurity(devID[0], "", 1, 0);
        if (ret != 0) {
            sendMyToast("函数调用失败");
        }
    }

    private void onGetSecurity() {
        int ret = FunclibAgent.getInstance().GetSecurity(devID[0]);
        if (ret != 0) {
            sendMyToast("函数调用失败");
        }
    }

    private void onSetAlertorAlias() {
        int ret = FunclibAgent.getInstance().SetAlertorAlias(devID[0], "", "");
        if (ret != 0) {
            sendMyToast("函数调用失败");
        }
    }

    private void onSetAlertorPreset() {
        int ret = FunclibAgent.getInstance().SetAlertorPreset(devID[0], "", 0);
        if (ret != 0) {
            sendMyToast("函数调用失败");
        }
    }

    private void onGetAlmPicture() {
        int ret = FunclibAgent.getInstance().GetAlmPicture(devID[0], "", 0, "");
        if (ret != 0) {
            sendMyToast("函数调用失败");
        }
    }

    private void onConfirmAlertorAlm() {
        int ret = FunclibAgent.getInstance().ConfirmAlertorAlm(devID[0], "");
        if (ret != 0) {
            sendMyToast("函数调用失败");
        }
    }
}
