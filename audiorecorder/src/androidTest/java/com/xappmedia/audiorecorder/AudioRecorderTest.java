package com.xappmedia.audiorecorder;

import android.os.Environment;
import android.test.AndroidTestCase;

import com.xappmedia.audiorecorder.exceptions.AudioRecorderError;

import java.io.File;
import java.util.ArrayList;

public class AudioRecorderTest extends AndroidTestCase {

    public void test_helloWorld_returnsTrue() {
        assertTrue(AudioRecorder.helloWorld());
    }

    public void test_startAndStopState() {
        final TestRecorderListener listener = new TestRecorderListener();

        final AudioRecorder recorder = new AudioRecorder(getContext());
        recorder.addRecorderListener(listener);
        recorder.startRecording(null);

        assertEquals(1, listener.startCalledHitRate);
        assertEquals(0, listener.pauseCalledHitRate);
        assertEquals(0, listener.stopCalledHitRate);
        assertTrue(listener.errorsCaptured.isEmpty());
        assertTrue(recorder.isRecording());
        assertFalse(recorder.isStopped());
        assertFalse(recorder.isPaused());

        sleep(5000);
        recorder.stopRecording();

        assertEquals(1, listener.startCalledHitRate);
        assertEquals(0, listener.pauseCalledHitRate);
        assertEquals(1, listener.stopCalledHitRate);
        assertTrue(listener.seconds == 4 || listener.seconds == 5); // Race condition could actually make this either 4 or 5 depending on the timings.
        assertTrue(listener.errorsCaptured.isEmpty());
        assertTrue(recorder.isStopped());
        assertFalse(recorder.isRecording());
        assertFalse(recorder.isPaused());

        assertNotNull(listener.savedFile);
        assertTrue(listener.savedFile.exists());
        long filelength = listener.savedFile.length();
        assertTrue(listener.savedFile.delete());
        // Checking length after deletion to ensure that the file is always deleted.
        // Checking if something was actually written to the file. Need a valid way to determine if what is written was actually what needed to be though.
        assertTrue(filelength > 0);

        recorder.shutdown();
    }

    public void test_PauseStates() {
        final TestRecorderListener listener = new TestRecorderListener();

        final AudioRecorderOptions options = new AudioRecorderOptions();
        options.fileName = "pauseFile";

        final AudioRecorder recorder = new AudioRecorder(getContext());
        recorder.addRecorderListener(listener);
        recorder.startRecording(options);

        assertEquals(1, listener.startCalledHitRate);
        assertEquals(0, listener.pauseCalledHitRate);
        assertEquals(0, listener.stopCalledHitRate);
        assertTrue(listener.errorsCaptured.isEmpty());
        assertTrue(recorder.isRecording());

        sleep(5000);
        recorder.pauseRecording();

        assertEquals(1, listener.startCalledHitRate);
        assertEquals(1, listener.pauseCalledHitRate);
        assertEquals(0, listener.stopCalledHitRate);
        assertTrue(listener.errorsCaptured.isEmpty());
        assertFalse(recorder.isRecording());
        assertFalse(recorder.isStopped());
        assertTrue(recorder.isPaused());

        sleep(5000);

        recorder.stopRecording();

        assertEquals(1, listener.startCalledHitRate);
        assertEquals(1, listener.pauseCalledHitRate);
        assertEquals(1, listener.stopCalledHitRate);
        assertTrue(listener.seconds == 4 || listener.seconds == 5); // Race condition could actually make this either 4 or 5 depending on the timings.
        assertTrue(listener.errorsCaptured.isEmpty());
        assertTrue(recorder.isStopped());
        assertFalse(recorder.isRecording());
        assertFalse(recorder.isPaused());

        assertNotNull(listener.savedFile);
        assertTrue(listener.savedFile.exists());
        long filelength = listener.savedFile.length();
        assertTrue(listener.savedFile.delete());
        // Checking length after deletion to ensure that the file is always deleted.
        // Checking if something was actually written to the file. Need a valid way to determine if what is written was actually what needed to be though.
        assertTrue(filelength > 0);

        recorder.shutdown();
    }

    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (Exception e) {
            // don't care
        }
    }

    static class TestRecorderListener implements RecorderListener {

        int startCalledHitRate = 0;
        int pauseCalledHitRate = 0;
        int stopCalledHitRate = 0;
        int seconds = 0;
        File savedFile = null;
        ArrayList<AudioRecorderError> errorsCaptured = new ArrayList<>();

        @Override
        public void onRecorderStart() {
            startCalledHitRate++;
        }

        @Override
        public void onRecorderPause() {
            pauseCalledHitRate++;
        }

        @Override
        public void onRecorderStop(File file) {
            stopCalledHitRate++;
            savedFile = file;
        }

        @Override
        public void onRecorderError(AudioRecorderError e) {
            errorsCaptured.add(e);
        }

        @Override
        public void onRecordTime(int seconds) {
            this.seconds = seconds;
        }
    }
}
