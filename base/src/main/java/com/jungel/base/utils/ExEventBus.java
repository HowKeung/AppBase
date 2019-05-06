package com.jungel.base.utils;

import org.greenrobot.eventbus.EventBus;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by lion on 2017/3/14.
 */

public class ExEventBus {


    private static ExEventBus instance;

    public static ExEventBus getDefault() {
        if (instance == null) {
            instance = new ExEventBus();
        }
        return instance;
    }


    public EventBus getDefaultEventBus() {
        return EventBus.getDefault();
    }

    public void sendEvent(int eventType) {
        MessageEvent event = new MessageEvent(eventType);
        getDefaultEventBus().post(event);
    }

    public void sendEvent(MessageEvent event) {
        getDefaultEventBus().post(event);
    }

    public void startFragment(SupportFragment fragment) {
        getDefaultEventBus().post(new MessageFragment(fragment));
    }

    public void startFragmentForResult(SupportFragment fragment, int requestCode) {
        getDefaultEventBus().post(new MessageFragment(fragment, requestCode));
    }

    /**
     * event bus event message
     */
    public static class MessageEvent {

        public static final int EVENT_TYPE_JUMP_TRADE = 1;
        public static final int EVENT_TYPE_ON_LOGIN = 2;

        public static final int EVENT_TYPE_REQUEST_UPDATE_WALLET = 3;

        public static final int EVENT_TYPE_UPDATE_USER_INFO = 12;
        public static final int EVENT_TYPE_RELOGIN = 13;
        public static final int EVENT_TYPE_FINISH_MAIN_ACTIVITY = 14;

        public static final int EVENT_TYPE_ADD_WALLET = 9;

        public static final int EVENT_TYPE_FORCE_UPDATE = 10;

        public static final int EVENT_TYPE_CHANGE_PAGE = 11;


        /**
         * the event type,
         * separate the event with the {this#type}
         */
        private int type;

        /**
         * the event data,
         * the event with the {this#data}
         */
        private Object data;


        public MessageEvent() {
        }

        public MessageEvent(int type) {
            this(type, null);
        }

        public MessageEvent(int type, Object data) {
            this.type = type;
            this.data = data;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    public static class MessageFragment {

        public static final int REQUEST_CODE_NORMAL = -1;
        public static final int REQUEST_CODE_SELECT_COUNTRY = 2;
        public static final int REQUEST_CODE_TRADE_PWD = 3;
        public static final int REQUEST_CODE_LOGIN = 4;
        public static final int REQUEST_CODE_SELECT_COIN = 5;
        public static final int REQUEST_CODE_QR_SCANNER = 6;
        public static final int REQUEST_CODE_SELECT_ADDRESS = 7;
        public static final int REQUEST_CODE_IDENTITY2 = 8;
        public static final int REQUEST_CODE_ADD_WALLET = 9;
        public static final int REQUEST_CODE_TRANSFER_SUCCESS = 10;
        public static final int REQUEST_PERMISSION_CODE_CAMERA = 11;
        public static final int REQUEST_PERMISSION_CODE_IMEI = 12;
        public static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 13;
        public static final int REQUEST_CODE_LOGIN_PWD = 14;
        public static final int REQUEST_CODE_REGISTER_SUCCESS = 15;
        public static final int REQUEST_CODE_FIND_PWD_SUCCESS = 16;

        private SupportFragment fragment;

        private int requestCode;

        public MessageFragment(SupportFragment fragment) {
            this(fragment, REQUEST_CODE_NORMAL);
        }

        public MessageFragment(SupportFragment fragment, int requestCode) {
            this.fragment = fragment;
            this.requestCode = requestCode;
        }

        public SupportFragment getFragment() {
            return fragment;
        }

        public void setFragment(SupportFragment fragment) {
            this.fragment = fragment;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }
    }
}
