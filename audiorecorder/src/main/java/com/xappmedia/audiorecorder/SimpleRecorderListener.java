package com.xappmedia.audiorecorder;

import com.xappmedia.audiorecorder.exceptions.AudioRecorderError;

import java.io.File;

/**
 * Abstract version of a {@link RecorderListener} which has empty implementations of each method
 * so children only have to implement what they need.
 */
public class SimpleRecorderListener implements RecorderListener {
    @Override
    public void onRecorderStart() {

    }

    @Override
    public void onRecorderPause() {

    }

    @Override
    public void onRecorderStop(File file) {

    }

    @Override
    public void onRecorderError(AudioRecorderError e) {

    }

    @Override
    public void onRecordTime(int seconds) {

    }
}
