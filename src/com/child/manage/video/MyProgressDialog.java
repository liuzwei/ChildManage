package com.child.manage.video;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.child.manage.R;

/**
 * @author denger/qinglei.yin@192.168.88.9<br>
 *         2014-1-8 上午11:38:13<br>
 * @declaration 进度提示框<br>
 */
public class MyProgressDialog {
    private Context act;
    private String msg;
    private LayoutInflater inflater;
    private AlertDialog dialog;
    private View view;

    private boolean cancelable;
    private TextView loading_text;
    private int m_timeout;
    private String m_timeoutToast;
    private boolean m_isTimeout;
    private TimeoutThread m_thread;
    private ITimeoutCallback m_timeout_cb;

    private String gStr(Context act, Object msgText) {
        String str = "";
        if (act != null && msgText != null) {
            if (msgText instanceof Integer) {
                str = act.getResources().getString((Integer) msgText);
            } else if (msgText instanceof String) {
                str = (String) msgText;
            }
        }
        return str;
    }

    public MyProgressDialog(Context act) {
        this.act = act;
        buildView();
    }

    public MyProgressDialog(Context act, Object msg) {
        this.act = act;
        this.msg = gStr(act, msg);
        buildView();
    }

    public MyProgressDialog setCancelable(boolean flag) {
        cancelable = flag;
        return this;
    }

    public void setTitle(Object text) {
        changeText(text);
    }

    public void setTimeoutToast(String text) {
        m_timeoutToast = text;
    }

    public void setTimeoutCallback(ITimeoutCallback cb) {
        m_timeout_cb = cb;
    }

    public void changeText(Object text) {
        msg = gStr(act, text);
        if (null == loading_text) buildView();
        loading_text.setText(msg);
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    private void buildView() {
        inflater = LayoutInflater.from(act);
        view = inflater.inflate(R.layout.loading, null);
        if (msg != null) {
            loading_text = (TextView) view.findViewById(R.id.loading_text);
            loading_text.setText(msg);
        }
    }

    public MyProgressDialog show() {
        return show(0);
    }

    public MyProgressDialog show(int timeout) {
        if (timeout > 0) {
            if (null != m_thread) m_thread.interrupt();
            m_thread = new TimeoutThread();
            m_isTimeout = false;
            m_timeout = timeout;
            m_thread.start();
        }

        buildView();        //"Error:The specified child already has a parent." modify by yinql
        dialog = new AlertDialog.Builder(act).setCancelable(cancelable).create();
        dialog.show();
        dialog.setContentView(view);
        return this;
    }

    public void cancel() {
        if (null != m_thread) m_thread.interrupt();
        m_isTimeout = false;
        dialog.cancel();
    }

    public void dismiss() {
        if (null != m_thread) m_thread.interrupt();
        m_isTimeout = false;
        dialog.dismiss();
    }

    private void dismissByTimeout() {
        m_isTimeout = true;
        dialog.dismiss();
        if (null == m_timeout_cb) return;
        m_timeout_cb.onTimeout();
    }

    public boolean isTimeout() {
        return m_isTimeout;
    }

    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dismissByTimeout();
            if (null == m_timeoutToast) return;
            BaseDataUI.sendToast(m_timeoutToast);
        }
    };

    private class TimeoutThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(m_timeout, 0);
                Message message = new Message();
                message.what = 1;
                m_handler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public interface ITimeoutCallback {
        public void onTimeout();
    }
}
