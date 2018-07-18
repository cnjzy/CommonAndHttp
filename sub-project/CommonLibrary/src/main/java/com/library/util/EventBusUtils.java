package com.library.util;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtils {

    public static void register(Object o) {
        EventBus.getDefault().register(o);
    }

    public static void unregister(Object o) {
        EventBus.getDefault().unregister(o);
    }

    public static void post(Object o) {
        EventBus.getDefault().post(o);
    }

    public static void postSticky(Events o) {
        EventBus.getDefault().postSticky(o);
    }

    public class Events<T> {
        public T data;


        public int cmd;

        public Events(T data, int cmd) {
            this.data = data;
            this.cmd = cmd;
        }

        public Events(int cmd) {
            this.cmd = cmd;
        }

        public Events() {

        }

        public Events(T data) {
            this.data = data;
        }
    }
}
