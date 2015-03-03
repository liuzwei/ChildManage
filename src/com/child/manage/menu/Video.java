package com.child.manage.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.adapter.TreeViewAdapter;
import com.child.manage.base.FlipperLayout;
import com.child.manage.entity.MyNodeinfo;
import com.child.manage.ui.PlayActivity;

import szy.utility.*;

import java.util.*;

/**
 * 菜单首页类
 *
 * @author rendongwei
 */
public class Video extends Activity{
    private Button video_menu;
//    private Context mContext;
//    private Activity mActivity;
//    private ChildApplication mKXApplication;
//    private View mHome;
//    private FlipperLayout.OnOpenListener mOnOpenListener;

    //视频接口
    private final String USERNAME = "15123036810";
    private final String PASSWORD = "654321";
    private final String[] SERVER_IPADDRESS = { "login137.53t02.com",
            "login137.4006043110.cn"};
    private final int SERVER_PORT = 8006;

    private final int ADD_FIRSTLEVEL_NODE = 11;
    private final int ADD_CHILD_NODE = 12;

    private LinkedList<MyNodeinfo> mListShowingNodes = new LinkedList<MyNodeinfo>();
    private ListView mListView;
    private TreeViewAdapter mTreeViewAdapter;
    private LinearLayout mLayoutTree;
    private ProgressBar mProgressBar;
    private TextView mTextView;

    private HashMap<String, Integer> mMapFrush = new HashMap<String, Integer>();
    private LinkedList<MyNodeinfo> mListJiedianInfos = new LinkedList<MyNodeinfo>();
    private Map<String, LinkedList<MyNodeinfo>> mMapNode = new HashMap<String, LinkedList<MyNodeinfo>>();

    private String mStrUserid = "";
    private String mStrClubName = "";
    private SzyUtility mSzyUtility;


    //play
    private PlayView mPlayView;

    private boolean mBoolStop = false;

    private LinearLayout mLayoutProgress;

    private MyNodeinfo mNodeInfo;
    private LinearLayout mLayoutPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);
        findViewById();
//        setListener();

        //视频接口
        mLayoutTree = (LinearLayout) this.findViewById(R.id.tree_layout_rect);
        mProgressBar = (ProgressBar) this.findViewById(R.id.pbar_tree);
        mTextView = (TextView) this.findViewById(R.id.text_tips);
        mListView = (ListView) this.findViewById(R.id.list_node);

        mTreeViewAdapter = new TreeViewAdapter(this, R.layout.tree_node,
                mListShowingNodes);
        mListView.setAdapter(mTreeViewAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
        showProgress();
        mSzyUtility = new SzyUtility();

        mSzyUtility.init(SERVER_IPADDRESS, SERVER_PORT, mSdkHandle2);
        mSzyUtility.login(USERNAME, PASSWORD, getDeviceKey());




//        TextView textView = (TextView) findViewById(R.id.text_title);
//        textView.setText(mNodeInfo.getsNodeName());
        mLayoutPlay = (LinearLayout) findViewById(R.id.llayout_playview);

        mLayoutProgress = (LinearLayout) findViewById(R.id.llayout_progress);
        Display dis = getWindowManager().getDefaultDisplay();
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                dis.getWidth(),dis.getWidth()*3/4);
        mPlayView = new PlayView(this);
        mPlayView.setPlayWH(dis.getWidth(), dis.getWidth() * 3 / 4);
        mPlayView.setPlayListener(playListener);
        mLayoutPlay.addView(mPlayView, lParams);


    }

    @Override
    protected void onDestroy() {
        mSzyUtility.release();
        super.onDestroy();
    }

    private PlayListener playListener = new PlayListener() {

        @Override
        public void infoCallback(int type, String sInfo) {
            Message msg = new Message();
            msg.what = type;
            msg.obj = sInfo;
            mmHandler.sendMessage(msg);
        }
    };

    private Handler mmHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PlayView.PLAY_VIDEO_SUCCESS:
                    hideProgress();
                    break;
                case PlayView.PLAY_VIDEO_ERROR_WATCH_TOOMUCH:
                    //超过观看人数上限
                    showTipsDlg((String)msg.obj);
                    break;
                case PlayView.PLAY_VIDEO_ERROR_RECV_TIMEOUT:
                    //音视频数据接收失败
                    showTipsDlg2(R.string.shujujieshousb);
                    break;
                case PlayView.PLAY_VIDEO_ERROR_CONNECT_TIMEOUT:
                    //连接服务器失败
                    showTipsDlg2(R.string.fuwuqilianjiesb);
                    break;
                default:
                    break;
            }
        }
    };

    private void showTipsDlg2(int nMsg) {
        if (!mBoolStop) {
            hideProgress();
            AlertDialog.Builder builder = new AlertDialog.Builder(Video.this);
            builder.setMessage(nMsg);
            builder.setTitle(R.string.dlg_tishi);
            builder.setPositiveButton(R.string.btn_queding,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
    }

    private void showTipsDlg(String sMsg) {
        if (!mBoolStop) {
            hideProgress();
            AlertDialog.Builder builder = new AlertDialog.Builder(Video.this);
            builder.setMessage(sMsg);
            builder.setTitle(R.string.dlg_tishi);
            builder.setPositiveButton(R.string.btn_queding,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
    }
//    public Video(Context context, Activity activity, ChildApplication application) {
//        mContext = context;
//        mActivity = activity;
//        mKXApplication = application;
//        mHome = LayoutInflater.from(context).inflate(R.layout.video, null);
//        findViewById();
//        setListener();
//
//        //视频接口
//        mLayoutTree = (LinearLayout) mHome.findViewById(R.id.tree_layout_rect);
//        mProgressBar = (ProgressBar) mHome.findViewById(R.id.pbar_tree);
//        mTextView = (TextView) mHome.findViewById(R.id.text_tips);
//        mListView = (ListView) mHome.findViewById(R.id.list_node);
//
//        mTreeViewAdapter = new TreeViewAdapter(mContext, R.layout.tree_node,
//                mListShowingNodes);
//        mListView.setAdapter(mTreeViewAdapter);
//        mListView.setOnItemClickListener(mOnItemClickListener);
//        showProgress();
//        mSzyUtility = new SzyUtility();
//
//        mSzyUtility.init(SERVER_IPADDRESS, SERVER_PORT, mSdkHandle2);
//        mSzyUtility.login(USERNAME, PASSWORD, getDeviceKey());
//
//
//    }

//    @Override
//    protected void onDestroy() {
//        mSzyUtility.release();
//        super.onDestroy();
//    }

    private void findViewById() {
        video_menu = (Button) this.findViewById(R.id.video_menu);
        video_menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

//    private void setListener() {
//        mMenu.setOnClickListener(new OnClickListener() {
//
//            public void onClick(View v) {
//                if (mOnOpenListener != null) {
//                    mOnOpenListener.open();
//                }
//            }
//        });
//
//    }

//
//    public View getView() {
//        return mHome;
//    }
//
//    public void setOnOpenListener(FlipperLayout.OnOpenListener onOpenListener) {
//        mOnOpenListener = onOpenListener;
//    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            //没有子节点，处在树控件的最低级，即设备节点
            if (mListShowingNodes.get(position).isbSxtNode()) {
                if(mListShowingNodes.get(position).isbAtTerm()){
//                    Toast.makeText(this, R.string.like_fuwudaoqi, Toast.LENGTH_LONG).show();
                } else if (!mListShowingNodes.get(position).isbAlive()) {
//                    Toast.makeText(mContext, R.string.sxtlixian, Toast.LENGTH_LONG).show();
                }else {
                    gotoPlayActivity(mListShowingNodes.get(position));
                }
            } else {
                if (mListShowingNodes.get(position).isbExpanded()) {
                    //收起树节点
                    unExpandedTreeNode(position);
                }
                else {
                    NodeInfo tNode = mListShowingNodes.get(position);
                    if (isNeedGetDeviceNode(mListShowingNodes.get(position))) {
                        mSzyUtility.getNodeList("1", tNode.getsNodeId(), tNode.getnSxtCount(), tNode.getnJieDianCount());
                    }else {
                        if (mMapFrush.get(tNode.getsNodeId())==null
                                && (tNode.getnJieDianCount()!=0 || tNode.getnSxtCount()!=0)) {
                            mSzyUtility.getNodeList("1", tNode.getsNodeId(), tNode.getnSxtCount(), tNode.getnJieDianCount());
                        }
                        //展开节点
                        expandedTreeNode(position);
                    }
                }
            }
        }

    };

    private void gotoPlayActivity(MyNodeinfo nodeInfo) {
        Intent intent = new Intent();
        intent.setClass(this, PlayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.putExtra("ID", nodeInfo.getsNodeId());
//		intent.putExtra("IP", mStrServerIP);
//		intent.putExtra("PORT", mIntServerPort);
//		intent.putExtra("USERNAME", mStrUsername);
//		intent.putExtra("PASSWORD", mStrPassword);
        intent.putExtra("nodeinfo", nodeInfo);
//		intent.putExtra("ID", nodeInfo.getsNodeId());
//		intent.putExtra("NAME", nodeInfo.getsNodeName());
//		intent.putExtra("PTZ", nodeInfo.isbCloudDev());
//		intent.putExtra("IP", "220.181.120.243");
//		intent.putExtra("PORT", 9023);
//		intent.putExtra("USERNAME", mStrUsername);
//		intent.putExtra("PASSWORD", mStrPassword);
        startActivity(intent);
        this.finish();
    }

    // 判断是否需要发送获取设备节点的信息
    public Boolean isNeedGetDeviceNode(NodeInfo nodeInfo) {
        if (nodeInfo.getnJieDianCount() == 0
                && nodeInfo.getnSxtCount() == 0) {
            return false;
        }
        if (mMapNode.get(nodeInfo.getsNodeId()) != null) {
            return false;
        }
        return true;
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ADD_FIRSTLEVEL_NODE:

                    mListJiedianInfos.clear();
                    @SuppressWarnings("unchecked")
                    LinkedList<NodeInfo> lNodeInfos = (LinkedList<NodeInfo>) msg.obj;
                    if (lNodeInfos!=null) {
                        for (NodeInfo nodeInfo : lNodeInfos) {
                            MyNodeinfo myNodeinfo = new MyNodeinfo(nodeInfo);
                            myNodeinfo.setnLevel(1);
                            mListJiedianInfos.add(myNodeinfo);
                        }
                    }
                    initialData();
                    if (mTreeViewAdapter != null) {
                        mTreeViewAdapter.notifyDataSetChanged();
                    }
//                    gotoPlayActivity(mListShowingNodes.get(2));
                    break;
                case ADD_CHILD_NODE:
                    addTreeNode(msg);
                    break;
                case SzyUtility.LOGIN_SUCCESS:// 登陆成功
                    String strMsg = (String) msg.obj;
                    if (strMsg.length()>0) {
                        showTipsDlg(strMsg, false);
                    }
                    break;
                case SzyUtility.LOGIN_RET_MSG_ERROR:// 登录数据异常
                    showErrorDlg(R.string.login_liebiaohqsb);
                    break;
                case SzyUtility.LOGIN_PASSWORD_ERROR:// 用户密码出错
                    showErrorDlg(R.string.login_mimabudui);
                    break;
                case SzyUtility.LOGIN_USERNAME_ERROR:// 用户名不存在
                    showErrorDlg(R.string.login_yonghumingbd);
                    break;
                case SzyUtility.LOGIN_CHILD_USER_OVERDATE:// 子账号已过期
                    showErrorDlg(R.string.login_zhizhanghaogq);
                    break;
                case SzyUtility.LOGIN_ONLINE_MAX_NUM:// 在线人数已达上限
                    showErrorDlg(R.string.login_denglushangxianrs);
                    break;
                case SzyUtility.LOGIN_OEM_OVERDATE:// OEM已到期
                    showErrorDlg(R.string.login_zhanghaoguoqi);
                    break;
                case SzyUtility.LOGIN_SERVER_ERROR:// 服务器维护
                    String strMsg2 = (String) msg.obj;
                    if (strMsg2.length()>0) {
                        showTipsDlg(strMsg2, false);
                    }
                    break;
                case SzyUtility.LOGIN_CONNECT_TIMEOUT:// 连接服务器超时
                    showErrorDlg(R.string.login_lianjiefuwuqisb);
                    break;
                case SzyUtility.LOGIN_LIST_TIMEOUT:// 获取列表超时
                    showErrorDlg(R.string.login_liebiaohuoqusb);
                    break;
                default:
                    break;
            }
        }
    };

    private SdkHandle2 mSdkHandle2 = new SdkHandle2() {

        @SuppressWarnings("rawtypes")
        @Override
        public void nodelistCallback(String sId, int nType,
                                     LinkedList lNodeInfos) {
            Message msg = new Message();
            msg.what = ADD_CHILD_NODE;
            msg.arg1 = nType;
            msg.obj = lNodeInfos;
            mHandler.sendMessage(msg);
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void loginCallback(int nRet, String sMsg, String strUserid, String strClubname,
                                  LinkedList lNodeInfos) {
            switch (nRet) {
                case SzyUtility.LOGIN_SUCCESS:// 登陆成功
                    mStrUserid = strUserid;
                    mStrClubName = strClubname;
                    Message msg = new Message();
                    msg.what = ADD_FIRSTLEVEL_NODE;
                    msg.obj = lNodeInfos;
                    mHandler.sendMessage(msg);
                    if (sMsg.length()>0) {
                        Message msg2 = new Message();
                        msg2.what = SzyUtility.LOGIN_SUCCESS;
                        msg2.obj = sMsg;
                        mHandler.sendMessage(msg2);
                    }
                    break;
                case SzyUtility.LOGIN_RET_MSG_ERROR:// 登录数据异常
                case SzyUtility.LOGIN_PASSWORD_ERROR:// 用户密码出错
                case SzyUtility.LOGIN_USERNAME_ERROR:// 用户名不存在
                case SzyUtility.LOGIN_CHILD_USER_OVERDATE:// 子账号已过期
                case SzyUtility.LOGIN_ONLINE_MAX_NUM:// 在线人数已达上限
                case SzyUtility.LOGIN_OEM_OVERDATE:// OEM已到期
                case SzyUtility.LOGIN_CONNECT_TIMEOUT:// 连接服务器超时
                case SzyUtility.LOGIN_LIST_TIMEOUT:// 获取列表超时
                    mHandler.sendEmptyMessage(nRet);
                    break;
                case SzyUtility.LOGIN_SERVER_ERROR:// 服务器维护
                    if (sMsg.length() > 0) {
                        Message msg2 = new Message();
                        msg2.what = SzyUtility.LOGIN_SERVER_ERROR;
                        msg2.obj = sMsg;
                        mHandler.sendMessage(msg2);
                    }
                    break;
                default:

                    break;
            }
        }

//		@Override
//		public void serverInfoCallback(String strUserid, String strClubname,String strServerIP, int nServerPort) {
//			mStrUserid = strUserid;
//			mStrClubName = strClubname;
//			mStrServerIP = strServerIP;
//			mIntServerPort = nServerPort;
//		}
    };

    private void initialData() {
        MyNodeinfo treeNode = new MyNodeinfo(mStrUserid,mStrClubName, false, "0", 0,true,true);
        mListShowingNodes.add(treeNode);

        LinkedList<MyNodeinfo> linkedListTempNodes = new LinkedList<MyNodeinfo>();
        for (int i=0; i<mListJiedianInfos.size();i++) {
            MyNodeinfo treeNode1 = mListJiedianInfos.get(i);
            if (treeNode1.getnLevel()==1) {
                linkedListTempNodes.add(treeNode1);
                mListShowingNodes.add(treeNode1);
                if (treeNode1.isbExpanded() && !treeNode1.isbSxtNode()) {
                    AddNode2TreeList(mListShowingNodes.size()-1,treeNode1.getsNodeId(),1);
                }
            }
        }
        mMapNode.put(treeNode.getsNodeId(), linkedListTempNodes);


        int position = 1;
        //没有子节点，处在树控件的最低级，即设备节点
        if (mListShowingNodes.get(position).isbSxtNode()) {
            if(mListShowingNodes.get(position).isbAtTerm()){
//                Toast.makeText(mContext, R.string.like_fuwudaoqi, Toast.LENGTH_LONG).show();
            } else if (!mListShowingNodes.get(position).isbAlive()) {
//                Toast.makeText(mContext, R.string.sxtlixian, Toast.LENGTH_LONG).show();
            }else {
                gotoPlayActivity(mListShowingNodes.get(position));
            }
        } else {
            if (mListShowingNodes.get(position).isbExpanded()) {
                //收起树节点
                unExpandedTreeNode(position);
            }
            else {
                NodeInfo tNode = mListShowingNodes.get(position);
                if (isNeedGetDeviceNode(mListShowingNodes.get(position))) {
                    mSzyUtility.getNodeList("1", tNode.getsNodeId(), tNode.getnSxtCount(), tNode.getnJieDianCount());
                }else {
                    if (mMapFrush.get(tNode.getsNodeId())==null
                            && (tNode.getnJieDianCount()!=0 || tNode.getnSxtCount()!=0)) {
                        mSzyUtility.getNodeList("1", tNode.getsNodeId(), tNode.getnSxtCount(), tNode.getnJieDianCount());
                    }
                    //展开节点
                    expandedTreeNode(position);
                }
            }
        }


    }

    private void addTreeNode(Message msg) {
        if (msg.arg1 == 0) {
            @SuppressWarnings("unchecked")
            LinkedList<NodeInfo> lstNodes = (LinkedList<NodeInfo>) msg.obj;
            if (lstNodes.size() > 0) {
                int nPos = getNodeidPos(lstNodes.get(0).getsParentId());
                if (nPos >= 0) {
                    AddJiedianNode(mListShowingNodes.get(nPos), lstNodes);
                    expandedTreeNode(nPos);
                }
            }
        } else if (msg.arg1 == 1) {
            @SuppressWarnings("unchecked")
            LinkedList<NodeInfo> lstSxts = (LinkedList<NodeInfo>) msg.obj;
            if (lstSxts.size() > 0) {
                int nPos = getNodeidPos(lstSxts.get(0).getsParentId());
                if (nPos >= 0) {
                    AddSxtNode(mListShowingNodes.get(nPos), lstSxts);
                    expandedTreeNode(nPos);
                }
            }
        }
    }

    private void AddJiedianNode(MyNodeinfo treeNode, LinkedList<NodeInfo> lstNodes) {
        LinkedList<MyNodeinfo> linkedListTempNodes = new LinkedList<MyNodeinfo>();
        for (int i = 0; i < lstNodes.size(); i++) {
            NodeInfo nodeInfo = lstNodes.get(i);
            MyNodeinfo myNodeinfo = new MyNodeinfo(nodeInfo);
            myNodeinfo.setnLevel(treeNode.getnLevel() + 1);
            linkedListTempNodes.add(myNodeinfo);

        }
        mMapNode.put(treeNode.getsNodeId(), linkedListTempNodes);
        mMapFrush.put(treeNode.getsNodeId(), 1);
    }

    private void AddSxtNode(MyNodeinfo treeNode, LinkedList<NodeInfo> lstSxts) {
        LinkedList<MyNodeinfo> listTempNodes = new LinkedList<MyNodeinfo>();
        for (int intPos = 0; intPos < lstSxts.size(); intPos++) {
            NodeInfo sxtInfo = lstSxts.get(intPos);
            MyNodeinfo myNodeinfo = new MyNodeinfo(sxtInfo);
            myNodeinfo.setbExpanded(false);
            myNodeinfo.setnLevel(treeNode.getnLevel() + 1);
            sxtInfo.setnJieDianCount(0);
            sxtInfo.setsParentId(treeNode.getsNodeId());
            listTempNodes.add(myNodeinfo);
        }

        mMapNode.put(treeNode.getsNodeId(), listTempNodes);
        mMapFrush.put(treeNode.getsNodeId(), 1);
    }

    private int getNodeidPos(String nodeId) {
        int nPos = -1;
        for (int i = 0; i < mListShowingNodes.size(); i++) {
            if (nodeId.equals(mListShowingNodes.get(i).getsNodeId())) {
                nPos = i;
                break;
            }
        }
        return nPos;
    }

    //收起树节点
    private void unExpandedTreeNode(int nPos){

        mListShowingNodes.get(nPos).setbExpanded(false);
        MyNodeinfo nodeInfo = mListShowingNodes.get(nPos);
        ArrayList<MyNodeinfo> temp = new ArrayList<MyNodeinfo>();

        for (int i = nPos+1; i < mListShowingNodes.size(); i++) {
            if (nodeInfo.getnLevel()>=mListShowingNodes.get(i).getnLevel()) {
                break;
            }
            temp.add(mListShowingNodes.get(i));
        }
        mListShowingNodes.removeAll(temp);

        mTreeViewAdapter.notifyDataSetChanged();//通知界面更新
    }

    // 展开树节点
    private void expandedTreeNode(int nPos) {
        if (mListShowingNodes.get(nPos).isbExpanded()) {
            unExpandedTreeNode(nPos);
        }
        mListShowingNodes.get(nPos).setbExpanded(true);
        AddNode2TreeList(nPos, mListShowingNodes.get(nPos).getsNodeId(),
                1);
        if (mTreeViewAdapter != null) {
            mTreeViewAdapter.notifyDataSetChanged();
        }
    }

    private int AddNode2TreeList(int nPos, String nodeId, int j) {
        LinkedList<MyNodeinfo> listTempNodes =  mMapNode.get(nodeId);
        if (listTempNodes!=null) {
            for (int intPos=0; intPos< listTempNodes.size(); intPos++) {
                MyNodeinfo treeNode = listTempNodes.get(intPos);
                mListShowingNodes.add(nPos+j, treeNode);
                j++;
                //如果此节点还有子节点而且处于展开状态
                if (treeNode.isbExpanded() && !treeNode.isbSxtNode()) {
                    j = AddNode2TreeList(nPos, treeNode.getsNodeId(), j);
                }else{
//                    gotoPlayActivity(mListShowingNodes.get(2));
                    mNodeInfo = mListShowingNodes.get(2);
                    int nRet = mPlayView.startPlay(mNodeInfo.getsParentId(), mNodeInfo.getsNodeId());
                    if (nRet == PlayView.SXT_AT_TREM) {
                        showTipsDlg("摄像头到期");
                    }else if (nRet == PlayView.SXT_NOT_ONLINE) {
                        showTipsDlg("摄像头不在线");
                    }
//                    showProgress();
                    mBoolStop = false;
                }
            }
        }
        return j;
    }

    private void showProgress() {
        try {
            mLayoutTree.setGravity(Gravity.CENTER);
            mProgressBar.setVisibility(View.VISIBLE);
            mTextView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);

            mLayoutProgress.setVisibility(View.VISIBLE);
            mPlayView.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideProgress() {
        try {
            mLayoutTree.setGravity(Gravity.TOP);
            mProgressBar.setVisibility(View.GONE);
            mTextView.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
            mLayoutTree.setVisibility(View.GONE);

            mLayoutProgress.setVisibility(View.GONE);
            mPlayView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDeviceKey(){
        TelephonyManager tm = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        String strKey = tm.getDeviceId();
        if (strKey==null) {
            strKey = String.valueOf(System.currentTimeMillis());
        }
        return strKey;
    }

    private void showErrorDlg(int nMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(nMsg);
        builder.setTitle(R.string.dlg_tishi);
        builder.setPositiveButton(R.string.btn_queding,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        ListActivity.this.finish();
                    }
                });
        builder.create().show();
    }

    private void showTipsDlg(String sMsg,final boolean bFlag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(sMsg);
        builder.setTitle(R.string.dlg_tishi);
        builder.setPositiveButton(R.string.btn_queding,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (bFlag) {
//                            ListActivity.this.finish();
                        }
                    }
                });
        builder.create().show();
    }

}
