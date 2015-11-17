package com.xappmedia.audiorecorder.exceptions;

/**
 * Exception class for audio recording errors.
 */
public class AudioRecorderError extends RuntimeException {

    public AudioRecorderError() {
        super();
    }

    public AudioRecorderError(String message) {
        super(message);
    }

    public AudioRecorderError(Throwable throwable) {
        super(throwable);
    }
}
