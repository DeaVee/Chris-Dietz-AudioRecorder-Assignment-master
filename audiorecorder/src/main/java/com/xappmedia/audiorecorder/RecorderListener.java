package com.xappmedia.audiorecorder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xappmedia.audiorecorder.exceptions.AudioRecorderError;

import java.io.File;

/**
 * A RecorderListener that can be registered to a {@link AudioRecorder} to listen for particular
 * recording events.
 */
public interface RecorderListener {
    /**
     * An audio recording has successfully started.
     */
    void onRecorderStart();
    /**
     * A audio recording has successfully been paused.
     */
    void onRecorderPause();
    /**
     * An audio recording has been completely stopped and the recorder reset.
     * @param file
     *      If stopped manually, this is the audio file that has been saved by the recorder.
     */
    void onRecorderStop(@Nullable File file);
    /**
     * An error was thrown when trying to record.
     * @param e
     *      Audio recorder error that was thrown.
     */
    void onRecorderError(@NonNull AudioRecorderError e);

    /**
     * When recording, this will display the number of seconds that the user has recorded for.
     * @param seconds
     *      Number of seconds the user has been recording for.
     */
    void onRecordTime(int seconds);
}
