package com.child.manage.video;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.audio.AudioBuffer;
import com.android.audio.AudioPlayer;
import com.android.opengles.FrameBuffer;
import com.android.opengles.OpenglesRender;
import com.android.opengles.OpenglesView;
import com.child.manage.R;
import ipc.android.sdk.com.*;
import ipc.android.sdk.impl.FunclibAgent;
import ipc.android.sdk.impl.PlayCtrlAgent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014-06-23.
 */
public class DirectConnectUI extends BaseDataUI implements View.OnClickListener {
    public static final String TAG = "DirectConnectUI";
    private DirectConnectCB m_cb = new DirectConnectCB();
    private FunclibAgent m_fa = FunclibAgent.getInstance();
    private PlayCtrlAgent m_pca = PlayCtrlAgent.getInstance();
    private Map<Integer, NetSDK_IPC_ENTRY> m_ipc_entry = new HashMap<>();
    private Map<Integer, Integer> m_port_id_map = new HashMap<>();

    class PlayerEntry {
        int m_login_id;
        int m_play_id;
        int m_prot_id;
        AudioPlayer m_audio;
        OpenglesRender m_video;
    }

    Map<Integer, PlayerEntry> m_play_entry_map = new HashMap<>();
    Map<Integer, PlayerEntry> m_play_id_entry_map = new HashMap<>();

    final int MaxBranch = 4;
    int[] mGlViewRes = {R.id.glVideo1, R.id.glVideo2, R.id.glVideo3, R.id.glVideo4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direct_connect_ui);

        findViewById(R.id.btnSearch).setOnClickListener(this);
        findViewById(R.id.btnStopSearch).setOnClickListener(this);
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

        findViewById(R.id.btnPtzUp).setOnClickListener(this);
        findViewById(R.id.btnPtzDown).setOnClickListener(this);
        findViewById(R.id.btnPtzLeft).setOnClickListener(this);
        findViewById(R.id.btnPtzRight).setOnClickListener(this);
        findViewById(R.id.btnPtzAuto).setOnClickListener(this);

        m_fa.setIDirectConnectCB(m_cb);
        m_fa.setDirectConnectCallbackFunc();
        m_pca.setIPlayCtrlAgentCB(m_cb);

        initGLView();
    }

    private void initGLView() {
        for (int i = 0; i < MaxBranch; i++) {
            OpenglesView glView = (OpenglesView) findViewById(mGlViewRes[i]);
            OpenglesRender glRender = new OpenglesRender(glView, i);
            glRender.setVideoMode(OpenglesRender.VIDEO_MODE_CUSTOM);// VIDEO_MODE_FIT
            glView.setRenderer(glRender);
            glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
            PlayerEntry entry = new PlayerEntry();
            entry.m_audio = new AudioPlayer(i);
            entry.m_video = glRender;
            m_play_entry_map.put(i, entry);
        }
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                int ret = m_fa.StartSearchDev();
                break;
            case R.id.btnStopSearch:
                m_fa.StopSearchDev();
                break;
            case R.id.btnLogin:
                if (m_ipc_entry.isEmpty()) {
                    sendMyToast("请先搜索设备！");
                    return;
                }

                NetSDK_IPC_ENTRY entry = m_ipc_entry.get(0);
                String user = "admin";
                String pwd = "123456";
                if (entry.getUserCfg().getCount() > 0) {
                    NetSDK_UserAccount account = entry.getUserCfg().getAccounts()[0];
                    user = account.getUserName();
                    pwd = account.getPassword();
                }

                String ip = entry.getLanCfg().getIPAddress();
                //ip = "192.168.66.20";
                int port = entry.getStreamCfg().getPtzPort();
                ret = m_fa.LoginDev(ip, port, user, pwd);
                if (ret == 0) {
                    sendMyToast("登录失败！");
                    return;
                }

                m_play_entry_map.get(0).m_login_id = ret;
                break;
            case R.id.btnLogoff:
                m_fa.LogOutDev(m_play_entry_map.get(0).m_login_id);
                break;
            case R.id.btnStartVideo:
                onStartVideo();
                break;
            case R.id.btnStopVideo:
                onStopVideo();
                break;
        }
    }

    private void onStopVideo() {
        int id = m_play_entry_map.get(0).m_play_id;
        m_fa.StopRealPlay(id);
    }

    private void onStartVideo() {
        int id = m_play_entry_map.get(0).m_login_id;
        NetSDK_USER_VIDEOINFO info = new NetSDK_USER_VIDEOINFO();
        info.setbIsTcp(1);
        NetSDK_IPC_ENTRY entry = m_ipc_entry.get(0);
        info.setnVideoPort(entry.getStreamCfg().getVideoPort());
        info.setnVideoChannle(1);
        int ret = m_fa.RealPlay(id, info);
        if (ret == 0) {
            sendMyToast("启动视频失败。");
            return;
        }

        m_play_entry_map.get(0).m_play_id = ret;
        m_play_id_entry_map.put(ret, m_play_entry_map.get(0));
    }

    class DirectConnectCB implements FunclibAgent.IDirectConnectCB, PlayCtrlAgent.IPlayCtrlAgentCB {

        @Override
        public int SearchIPC(int nEventCode, int index, NetSDK_IPC_ENTRY entry) {
            m_ipc_entry.put(index, entry);
            sendMyToast("发现设备，IP=" + entry.getLanCfg().getIPAddress());
            return 0;
        }

        @Override
        public int StatusEvent(int lUser, int nStateCode, String pResponse) {
            sendMyToast("StatusEvent，lUser=" + lUser + ",nStateCode=" + nStateCode + "," + pResponse);
            return 0;
        }

        @Override
        public int AUXResponse(int lUser, int nType, String pResponse) {
            sendMyToast("AUXResponse，lUser=" + lUser + ",nType=" + nType + "," + pResponse);
            return 0;
        }

        @Override
        public int VoiceData(int lVoiceComHandle, String pRecvDataBuffer, int dwBufSize, byte byAudioFlag, FRAME_EXTDATA pUser) {
            return 0;
        }

        @Override
        public int RealData(int lRealHandle, int dwDataType, byte[] pBuffer, int dwBufSize, FRAME_EXTDATA pExtData) {
            if (!btnIsOpen(R.id.btnDecode)) return 0;
            if (dwDataType == 0) {// 接收视频数据，H264格式
                if (dwBufSize <= 0 || null == pExtData) return -1;
                int isKey = pExtData.getbIsKey();
                //接收数据交给底层库解码，解码回调函数为：decDataCB（如需要自己处理，可不调用此函数）
                m_pca.InputVideoDataAgent(m_play_id_entry_map.get(lRealHandle).m_prot_id, pBuffer, dwBufSize, isKey, (int) pExtData.getTimestamp());
            } else if (dwDataType == 1) {// 接收音频数据，G711格式
                if (dwBufSize <= 0 || null == pExtData) return -1;
                //接收数据交给底层库解码，解码回调函数为：decDataCB（如需要自己处理，可不调用此函数）
                m_pca.InputAudioDataAgent(m_play_id_entry_map.get(lRealHandle).m_prot_id, pBuffer, dwBufSize, (int) pExtData.getTimestamp());
            } else if (dwDataType == 2) {
                PlayerEntry entry = m_play_id_entry_map.get(lRealHandle);
                if (m_port_id_map.get(entry.m_prot_id) != null) return 0;
                //解码参数
                ByteBuffer byteBuffer = ByteBuffer.allocate(dwBufSize);
                byteBuffer.order(ByteOrder.nativeOrder());
                byteBuffer.put(pBuffer, 0, dwBufSize);
                byteBuffer.rewind();
                NetSDK_STREAM_AV_PARAM param = (NetSDK_STREAM_AV_PARAM) NetSDK_STREAM_AV_PARAM.createObjectByByteBuffer(byteBuffer);
                int prot = m_pca.GetProtAgent();
                Log.i(TAG, "VideoParam=" + param.getVideoParam().toString());
                TPS_VIDEO_PARAM vp = new TPS_VIDEO_PARAM();
                vp.setStream_index(0);
                vp.setVideo_encoder(param.getVideoParam().getCodec().getBytes());
                vp.setWidth(param.getVideoParam().getWidth());
                vp.setHeight(param.getVideoParam().getHeight());
                vp.setFramerate(param.getVideoParam().getFramerate());
                vp.setIntraframerate(param.getVideoParam().getFramerate() * 4);
                vp.setBitrate(param.getVideoParam().getBitrate());
                vp.setConfig(param.getVideoParam().getVol_data().getBytes());
                vp.setConfig_len(param.getVideoParam().getVol_length());
                byte[] videoParm = vp.objectToByteBuffer(ByteOrder.nativeOrder()).array();
                int ret = m_pca.OpenStreamAgent(prot, videoParm, videoParm.length, 0/*0:视频 1:音频*/, 25 * 1/*最大缓冲帧数*/);
                if (0 != ret) {
                    Log.i(TAG, "Video OpenStreamAgent failed.");
                    return -1;
                }

                //判断是否有音频，如果有音频才进行打开音频流及配置语音参数
                if (param.getbHaveAudio() != 0) {
                    Log.i(TAG, "AudioParam=" + param.getAudioParam().toString());
                    //设置音频流参数（摄像机端要配置音频编码方式为G711，其它格式暂不支持）
                    TPS_AUDIO_PARAM ap = new TPS_AUDIO_PARAM();
                    ap.setStream_index(0);
                    ap.setAudio_encoder(param.getAudioParam().getCodec().getBytes());
                    ap.setSamplerate(param.getAudioParam().getSamplerate());
                    ap.setSamplebitswitdh(param.getAudioParam().getBitspersample());
                    ap.setChannels(param.getAudioParam().getChannels());
                    ap.setBitrate(param.getAudioParam().getBitrate());
                    ap.setFramerate(param.getAudioParam().getFramerate());
                    byte[] audioParm = ap.objectToByteBuffer(ByteOrder.nativeOrder()).array();
                    ret = m_pca.OpenStreamAgent(prot, audioParm, audioParm.length, 1/*0:视频 1:音频*/, 20/*最大缓冲帧数*/);
                    if (0 != ret) {
                        Log.i(TAG, "Audio OpenStreamAgent failed.");
                        return -1;
                    }

                    // 根据摄像机音频参数，配置AudioPlayer的参数
                    AudioPlayer audioPlayer = m_play_entry_map.get(0).m_audio;
                    if (audioPlayer == null) return -1;
                    AudioPlayer.MyAudioParameter audioParameter = new AudioPlayer.MyAudioParameter(param.getAudioParam().getSamplerate(), param.getAudioParam().getChannels(), param.getAudioParam().getBitspersample());
                    audioPlayer.initAudioParameter(audioParameter);
                }

                //0表示decDataCB解码出来的为yuv数据, 非0表示对应位数的rgb数据（支持的位数有：16、24、32）
                m_pca.PlayAgent(prot, 0);
                //记录端口句柄和设备ID间的映射关系
                m_play_id_entry_map.get(lRealHandle).m_prot_id = prot;
                m_port_id_map.put(prot, lRealHandle);
            }
            return 0;
        }

        @Override
        public int PlayActionEvent(int lUser, int nType, int nFlag, String pData) {
            return 0;
        }

        @Override
        public int Exception(int dwType, int lUserID, int lHandle) {
            return 0;
        }

        @Override
        public int EncodeAudio(int lType, int lPara1, int lPara2) {
            return 0;
        }

        @Override
        public int SerialData(int lUser, byte[] pRecvDataBuffer, int dwBufSize) {
            return 0;
        }

        @Override
        public int RecFileName(int lRealHandle, byte[] pRecFileNameBuf, int dwBufSize) {
            return 0;
        }

        @Override
        public int decDataCB(int nPort, byte[] pDecData, int nSize, byte[] pFrameInfo, byte[] pUser) {
            if (!btnIsOpen(R.id.btnShow)) return 0;
            if (nSize <= FRAME_INFO.SIZE) return -1;
            ByteBuffer bBuf = (ByteBuffer) ByteBuffer.wrap(pFrameInfo).order(ByteOrder.nativeOrder()).rewind();
            FRAME_INFO fInfo = (FRAME_INFO) FRAME_INFO.createObjectByByteBuffer(bBuf);
            Log.i(TAG, "decDataCB-->data is success:nSize=" + nSize + "," + fInfo.toString());
            // 处理解码后的媒体数据
            if (fInfo.getnIsVideo() == 1 && fInfo.getnWidth() > 0 && fInfo.getnHeight() > 0) {//处理视频数据(yuv或rgb数据)
                FrameBuffer buf = new FrameBuffer(fInfo.getnWidth(), fInfo.getnHeight());
                OpenglesRender _glRender = m_play_id_entry_map.get(m_port_id_map.get(nPort)).m_video;
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
                AudioBuffer audioBuf = new AudioBuffer(pDecData, nSize);
                m_play_id_entry_map.get(m_port_id_map.get(nPort)).m_audio.addToBuf(audioBuf);
            }

            return 0;
        }
    }
}
