package com.library.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.Iterator;
import java.util.Map;
import java.util.zip.CRC32;

public class ANRChecker {

    static ANRChecker s_INS = null;

    private Thread mMainThread = null;
    private HandlerThread mThreader = new HandlerThread("anr_checker");
    private Handler mChekerHandler = null;
    private Handler mMainHandler = null;
    private ANRListener mAnrObsever = null;

    ANRChecker() {
    }

    public static interface ANRListener {
        void onAppAnr(String info);
    }

    public static synchronized ANRChecker getIns() {
        if (s_INS == null) {
            s_INS = new ANRChecker();
        }
        return s_INS;
    }

    public synchronized void start(ANRListener observer) {
        if (mChekerHandler == null) {

            mMainThread = Thread.currentThread();
            if (mMainThread.getName().compareToIgnoreCase("main") != 0) {
                throw new RuntimeException("Anr checker Start Must run on Main thread");
            }

            mThreader.start();

            mMainHandler = new Handler(Looper.getMainLooper());
            mChekerHandler = new Handler(mThreader.getLooper());


            mMainHandler.postDelayed(mRunMainChecker, s_CheckTimeGap);
            mChekerHandler.removeCallbacks(mRunChecker);
            mChekerHandler.postDelayed(mRunChecker, s_DelayGap);

            mAnrObsever = observer;
        }
    }

    private static long s_CheckTimeGap = 10 * 1000;
    private static long s_DelayGap = s_CheckTimeGap + 5 * 1000;


    private Runnable mRunMainChecker = new Runnable() {

        @Override
        public void run() {
            mMainHandler.postDelayed(this, s_CheckTimeGap);
            mChekerHandler.removeCallbacks(mRunChecker);
            mChekerHandler.postDelayed(mRunChecker, s_DelayGap);
        }
    };

    private void printStack(Thread t, StackTraceElement[] stacks, StringBuilder sb) {
        try {
            sb.append("\t");
            sb.append(t.toString());
            sb.append("\n");
            if (stacks != null && stacks.length > 0) {
                for (StackTraceElement stack : stacks) {
                    sb.append("\t\t");
                    sb.append(stack.toString());
                    sb.append("\n");
                }
            }
        } catch (Throwable e) {

        }
    }

    private long getANRkey(StackTraceElement[] stacks) {
        long nResult = 0;

        if (stacks != null && stacks.length > 1) {
            StackTraceElement stack = stacks[0];

            CRC32 crc1 = new CRC32();
            crc1.update(stack.toString().getBytes());

            nResult = crc1.getValue();
        }
        return nResult;
    }

    private Runnable mRunChecker = new Runnable() {

        @Override
        public void run() {
            try {
                Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
                Thread curruntThread = Thread.currentThread();
                long nANRKey = 0;
                StringBuilder sb = new StringBuilder();
                sb.append("-----thread info-----\n");

                if (traces != null && traces.size() > 0) {
                    StackTraceElement[] mainStacks = traces.get(mMainThread);
                    if (mainStacks != null && mainStacks.length > 0) {
                        printStack(mMainThread, mainStacks, sb);

                        nANRKey = getANRkey(mainStacks);
                    }

                    Iterator iter = traces.keySet().iterator();
                    while (iter.hasNext()) {
                        Thread t = (Thread) iter.next();
                        StackTraceElement[] stacks = traces.get(t);

                        if (t != mMainThread && t != curruntThread)
                            printStack(t, stacks, sb);
                    }
                }
                sb.append("\n\n");
                sb.append("-----anrkey-----\n");
                sb.append("anrkey=");
                sb.append(nANRKey);
                sb.append("\n\n");

                //Logutils.e("ANRChecker", sb.toString());

                if (mAnrObsever != null) {
                    mAnrObsever.onAppAnr(sb.toString());
                }
            } catch (Throwable e) {

            }
        }
    };

}
