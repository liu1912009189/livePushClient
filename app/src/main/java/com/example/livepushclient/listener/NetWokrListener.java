package com.example.livepushclient.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.livepushclient.util.NetUtil;


/**
 * 锁屏，解锁，开屏监听
 */
public class NetWokrListener {
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private NetWorkBroadcastReceiver mNetWorkReceiver;
    private NetWorkStateListener mNetWorkStateListener;
    private NetUtil.NetState mCurrentState = NetUtil.NetState.NET_NO;

    public NetWokrListener(Context context) {
        mContext = context;
        mNetWorkReceiver = new NetWorkBroadcastReceiver();
    }

    /**
     * screen状态广播接收者
     */
    private class NetWorkBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 这wifi的打开与关闭，与wifi的连接无关
            if (mCurrentState != NetUtil.isConnected(context) &&  mNetWorkStateListener != null) {
                mCurrentState = NetUtil.isConnected(context);
                Log.e(TAG, "当前上网状态 " + mCurrentState);
                if(mCurrentState == NetUtil.NetState.NET_NO ){
                    mNetWorkStateListener.onNetWorkDisable();
                }else {
                    mNetWorkStateListener.onNetworkChanged();
                }
            }
        }
    }


    /**
     * 开始监听screen状态
     *
     * @param listener
     */
    public void begin(NetWorkStateListener listener) {
        mNetWorkStateListener = listener;
        registerListener();
    }


    /**
     * 停止screen状态监听
     */
    public void unregisterListener() {
        mContext.unregisterReceiver(mNetWorkReceiver);
    }

    /**
     * 启动screen状态广播接收器
     */
    private void registerListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        mContext.registerReceiver(mNetWorkReceiver, filter);
    }

    public interface NetWorkStateListener {// 返回给调用者网络状态信息

        void onNetworkChanged();

        void onNetWorkDisable();

    }
}