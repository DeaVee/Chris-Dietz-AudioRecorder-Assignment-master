package com.xappmedia.audiorecorder;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * An AudioRecorderActivity is a basic Activity that can be used to record audio.
 * To use this activity, just start it with {@link Context#startActivityForResult(Intent, int)}
 * The audio will be sent back as a URI in with the returning intent.
 *
 * This can be extended with a custom UI to handle features enabled by the audiorecorder.
 */
public class AudioRecorderActivity extends AppCompatActivity {

    private AudioRecorder recorder;

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        recorder = new AudioRecorder();
    }

    @Override
    public void onPause() {
        super.onPause();
        recorder.stopRecording();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recorder.shutdown();
    }

    protected void addRecorderListener(RecorderListener listener) {
        recorder.addRecorderListener(listener);
    }

    protected void removeListener(RecorderListener listener) {
        recorder.removeRecorderListener(listener);
    }

    protected boolean isRecording() {
        return recorder.isRecording();
    }

    protected void startRecording() {
        recorder.startRecording(null);
    }

    protected void stopRecording() {
        recorder.stopRecording();
    }

    protected void pauseRecording() {
        recorder.pauseRecording();
    }
}
