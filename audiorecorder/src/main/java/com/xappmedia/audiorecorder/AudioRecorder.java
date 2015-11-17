package com.xappmedia.audiorecorder;

import android.content.Context;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xappmedia.audiorecorder.exceptions.AudioRecorderError;

import java.io.File;
import java.io.IOException;

import static com.xappmedia.audiorecorder.Utils.*;

/**
 * Audio Recorder can be used to handle all states regarding recording from a device microphone.
 *
 * By default, all recordings will be places in the external storage under the public music folder with a
 * sub-directory that is the application package name.
 */
public class AudioRecorder {

    private static final String TAG = "AudioRecorder";

    /**
     * Sample JavaDoc for sample method
     *
     * @return true Always returns true
     */
    public static boolean helloWorld() {
        Log.d(TAG, "hello world!");
        return true;
    }

    private static final int STATE_STOPPED = 0;
    private static final int STATE_RECORDING = 1;
    private static final int STATE_PAUSED = 2;
    private static final int STATE_RELEASED = 3;

    private final CompoundRecorderListener recorderListener;
    private final MediaRecorder mediaRecorder;
    private final Timer timer;

    private Context appContext;
    private DefaultOptions currentRecordingOptions;
    private int state;

    public AudioRecorder(Context ctx) {
        mediaRecorder = new MediaRecorder();
        recorderListener = new CompoundRecorderListener();
        timer = new Timer();
        state = STATE_STOPPED;
        appContext = ctx.getApplicationContext();
    }

    /**
     * Adds a recorder listener to the audio recorder library.
     */
    public void addRecorderListener(RecorderListener listener) {
        recorderListener.registerListener(listener);
    }

    /**
     * Removes a specific listener from the audio recorder library.
     */
    public void removeRecorderListener(RecorderListener listener) {
        recorderListener.unregisterListener(listener);
    }

    /**
     * Returns true if the current recording session is completely stopped and can be started.
     * If started from this state, then starting again with the same file path will overwrite the file
     * at that given path.
     * @return
     *      True if the audio recorder is in the stopped state.
     */
    public final synchronized boolean isStopped() {
        return state == STATE_STOPPED;
    }

    /**
     * Returns true if the current recording session is paused in the middle of recording.
     * If started from this state, then the recording will start again where it left off previous.
     *
     * @return
     *      True if the audio recorder is in the paused state.
     */
    public final synchronized boolean isPaused() {
        return state == STATE_PAUSED;
    }

    /**
     * Returns true if the current recording session is recording audio to the file.
     * @return
     *      True if the audio recorder is in the recording state.
     */
    public final synchronized boolean isRecording() {
        return state == STATE_RECORDING;
    }

    /**
     * Stops all recordings and
     */
    public final synchronized void shutdown() {
        synchronized (this) {
            if (state == STATE_RELEASED) {
                return;
            }
            shutdownInternal();
        }
    }

    /**
     * Starts the recording in the current state if it is not already recording.  This does nothing
     * if the recorder is currently saving audio to the device.
     *
     * @param options
     *      Option parameters to the specific recording.  This can be null in which case default settings will
     *      be used.
     */
    public final void startRecording(@Nullable AudioRecorderOptions options) {
        synchronized (this) {
            if (state == STATE_RECORDING) {
                return;
            }
            startInternal(options);
        }
    }

    /**
     * Pauses the recording.  In the paused state, the recorder can
     */
    public final void pauseRecording() {
        synchronized (this) {
            if (state == STATE_PAUSED) {
                return;
            }
            pauseInternal();
        }
    }

    /**
     * Stops the recording and prepare it for another recording later.
     */
    public final void stopRecording() {
        synchronized (this) {
            if (state == STATE_STOPPED) {
                return;
            }
            stopInternal();
        }
    }

    private void startInternal(AudioRecorderOptions options) {
        try {
            if (state != STATE_PAUSED) {
                currentRecordingOptions = new DefaultOptions(appContext, options);
                prepRecorder(appContext, mediaRecorder, currentRecordingOptions);
                mediaRecorder.prepare();
            }
            state = STATE_RECORDING;
            mediaRecorder.start();
            recorderListener.onRecorderStart();
            timer.start(new TimerListener(recorderListener));
        } catch (IOException e) {
            recorderListener.onRecorderError(new AudioRecorderError(e));
        }
    }

    private void pauseInternal() {
        state = STATE_PAUSED;
        mediaRecorder.stop();
        recorderListener.onRecorderPause();
        timer.pause();
    }

    private void stopInternal() {
        if (state != STATE_PAUSED) {
            mediaRecorder.stop();
        }
        state = STATE_STOPPED;
        mediaRecorder.reset();
        recorderListener.onRecorderStop((currentRecordingOptions != null) ? new File(currentRecordingOptions.fileLocation) : null);
        timer.stop();
        currentRecordingOptions = null;
    }

    private void shutdownInternal() {
        if (state == STATE_RECORDING) {
            stopInternal();
        }
        mediaRecorder.release();
    }

    private static void prepRecorder(@NonNull Context appContext, @NonNull MediaRecorder recorder, @Nullable AudioRecorderOptions o) throws IOException {
        final DefaultOptions newOptions = new DefaultOptions(appContext, o);
        File file = createFileIfNecessary(newOptions.fileLocation);
        // TODO: Need validation for this file location in case the user chose something weird.
        recorder.setOutputFile(file.getAbsolutePath());
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    private static class TimerListener implements Timer.TimerListener {
        private final RecorderListener listener;

        public TimerListener(RecorderListener listener) {
            this.listener = listener;
        }

        @Override
        public void onSecond(int second) {
            listener.onRecordTime(second);
        }
    }
}
