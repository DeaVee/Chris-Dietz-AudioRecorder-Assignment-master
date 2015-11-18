package com.xappmedia.audiorecorder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xappmedia.audiorecorder.exceptions.AudioRecorderError;

import java.io.File;
import java.util.ArrayList;

/**
 * Internal recorder listener that handles other listeners.
 */
class CompoundRecorderListener implements RecorderListener {

    private final ArrayList<RecorderListener> listeners;

    public CompoundRecorderListener() {
        listeners = new ArrayList<>(5);
    }

    void registerListener(@Nullable RecorderListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    void unregisterListener(@Nullable RecorderListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    @Override
    public void onRecorderStart() {
        for (RecorderListener listener : listeners) {
            listener.onRecorderStart();
        }
    }

    @Override
    public void onRecorderPause() {
        for (RecorderListener listener : listeners) {
            listener.onRecorderPause();
        }
    }

    @Override
    public void onRecorderStop(@Nullable File file) {
        for (RecorderListener listener : listeners) {
            listener.onRecorderStop(file);
        }
    }

    @Override
    public void onRecorderError(@NonNull AudioRecorderError e) {
        for (RecorderListener listener : listeners) {
            listener.onRecorderError(e);
        }
    }

    @Override
    public void onRecordTime(int seconds) {
        for (RecorderListener listener : listeners) {
            listener.onRecordTime(seconds);
        }
    }
}
