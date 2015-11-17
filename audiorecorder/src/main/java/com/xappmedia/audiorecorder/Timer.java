package com.xappmedia.audiorecorder;

import android.os.Handler;
import android.os.Looper;

/**
 *  Simple timer object that will count in seconds to the provided {@link com.xappmedia.audiorecorder.Timer.TimerListener}
 *  callback.
 */
class Timer {
    private static Handler sUIHandler = new Handler(Looper.getMainLooper());

    public interface TimerListener {
        void onSecond(int second);
    }

    private static final int STATE_STOPPED = 0;
    private static final int STATE_PAUSED = 1;
    private static final int STATE_STARTED = 2;

    private int state = STATE_STOPPED;
    private TimerRunnable currentRunnable = null;

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        stop();
    }

    public void start(TimerListener listener) {
        if (state == STATE_STARTED) {
            // Restarting in this case.
            stop();
        }

        if (currentRunnable == null) {
            currentRunnable = new TimerRunnable(listener);
        }

        state = STATE_STARTED;
        sUIHandler.postDelayed(currentRunnable, 1000);
    }

    public void pause() {
        if (currentRunnable != null) {
            sUIHandler.removeCallbacks(currentRunnable);
        }

        state = STATE_PAUSED;
    }

    public void stop() {
        if (currentRunnable != null) {
            sUIHandler.removeCallbacks(currentRunnable);
        }
        currentRunnable = null;
        state = STATE_STOPPED;
    }

    private static class TimerRunnable implements Runnable {
        private final TimerListener timerListener;

        private int count;

        public TimerRunnable(TimerListener listener) {
            timerListener = listener;
            count = -1;
        }

        @Override
        public void run() {
            timerListener.onSecond(++count);
            sUIHandler.postDelayed(this, 1000);
        }
    }
}
