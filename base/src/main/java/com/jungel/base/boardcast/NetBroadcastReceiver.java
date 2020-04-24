package com.jungel.base.boardcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jungel.base.utils.ExEventBus;

public class NetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            // 当网络发生变化，判断当前网络状态，并通过NetEvent回调当前网络状态
            ExEventBus.getDefault().sendEvent(ExEventBus.MessageEvent.EVENT_TYPE_NET_CHANGE);
        }
    }
}
