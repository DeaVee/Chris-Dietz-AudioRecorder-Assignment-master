package com.xappmedia.audiorecorder;

import android.media.MediaRecorder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;

import static com.xappmedia.audiorecorder.Utils.generateDefaultFileName;
import static com.xappmedia.audiorecorder.Utils.getDefaultExternalFilePath;
import static com.xappmedia.audiorecorder.Utils.getExtension;
import static com.xappmedia.audiorecorder.Utils.hasProperExtension;


/**
 * A private {@link AudioRecorderOptions} class that will set default values for items that are not
 * set by the user.
 */
class DefaultOptions extends AudioRecorderOptions {

    /**
     * This is the full file location for the file being written to.
     */
    String fileLocation;

    public DefaultOptions(@Nullable AudioRecorderOptions o) {
        // Right now MediaRecorder.OutputFormat.THREE_GPP is the only format that is supported, so just use that.
        this.fileName = (o == null || TextUtils.isEmpty(o.fileName)) ?
                generateDefaultFileName(MediaRecorder.OutputFormat.THREE_GPP) :
                o.fileName;

        if (!hasProperExtension(this.fileName, MediaRecorder.OutputFormat.THREE_GPP)) {
            this.fileName += getExtension(MediaRecorder.OutputFormat.THREE_GPP);
        }

        this.fileLocation = getDefaultExternalFilePath() + "/" + this.fileName;
    }
}