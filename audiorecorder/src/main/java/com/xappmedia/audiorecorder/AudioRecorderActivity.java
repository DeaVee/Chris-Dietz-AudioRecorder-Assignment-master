package com.xappmedia.audiorecorder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

/**
 * An AudioRecorderActivity is a basic Activity that can be used to record audio.
 * To use this activity, just start it with {@link android.app.Activity#startActivityForResult(Intent, int)}
 * The audio will be sent back as a URI in with the returning intent if it is saved.
 *
 * This can be extended with a custom UI to handle features enabled by the audiorecorder.
 */
public class AudioRecorderActivity extends AppCompatActivity {

    /**
     * Activities that call this from {@link android.app.Activity#startActivityForResult(Intent, int)}
     * can look for the result in this argument on a RESULT_OK.
     */
    public static final String RESULT_ARG_FILE_URI = "fileUri";

    private AudioRecorder recorder;

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        recorder = new AudioRecorder(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRecording(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recorder.shutdown();
    }

    /**
     * Canceling an ongoing recording and returns the activity.
     */
    protected void cancelRecording() {
        stopRecording(true);
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Child activities can call this with a recorded file to set the result of the Activity and return the
     * listed file.
     * @param file
     *      File to save.
     */
    protected void returnRecording(@NonNull File file) {
        Uri fileUri = Uri.fromFile(file);
        final Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_ARG_FILE_URI, fileUri);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * Adds a recorder listener to the recorder.
     * @param listener
     *      Listener to add
     */
    protected void addRecorderListener(@NonNull RecorderListener listener) {
        recorder.addRecorderListener(listener);
    }

    /**
     * Removes a recorder listener from the recorder.
     * @param listener
     *      Listener to remove
     */
    protected void removeListener(@NonNull RecorderListener listener) {
        recorder.removeRecorderListener(listener);
    }

    /**
     * Returns true if the user is currently recording a message.
     */
    protected boolean isRecording() {
        return recorder.isRecording();
    }

    /**
     * Start a recording.
     * @param options
     *      Options to give the recorder for the specific recording.
     */
    protected void startRecording(AudioRecorderOptions options) {
        recorder.startRecording(options);
    }


    /**
     * Stop the current recording.
     * @param deleteFile
     *      Pass true to delete the file upon stopping.
     */
    protected void stopRecording(boolean deleteFile) {
        recorder.stopRecording(deleteFile);
    }

    /**
     * Pause the recording at the current position. Starting will restart the recording at it's current spot.
     */
    protected void pauseRecording() {
        recorder.pauseRecording();
    }
}
