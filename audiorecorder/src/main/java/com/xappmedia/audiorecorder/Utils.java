package com.xappmedia.audiorecorder;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility methods that can be used by AudioRecorder.
 */
class Utils {

    /**
     * Retrieves the main application name of the app
     * @return
     *      String value of the application.
     */
    static String getPackageLabel(Context ctx) {
        return ctx.getPackageName();
    }

    /**
     * Returns the default external directory location for the file to be saved.
     */
    static String getDefaultExternalFilePath(Context ctx) {
        // TODO: Need to handle situations where the environmental path is not accessible.
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + getPackageLabel(ctx);
    }

    /**
     * Returns a default name for the given media format including the extension.
     * @param mediaFormat
     *      One of the media formats as found in {@link android.media.MediaRecorder.OutputFormat}
     * @return
     *      File name and extension of the file.
     */
    static String generateDefaultFileName(int mediaFormat) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_kk_mm_ss_S", Locale.getDefault());
        return format.format(new Date()) + getExtension(mediaFormat);
    }

    /**
     * Returns the appropriate extension for the given file type.
     * @param mediaFormat
     *      One of the media formats as found in {@link android.media.MediaRecorder.OutputFormat}
     * @return
     *      A string representing a file type of the given output format.
     */
    static String getExtension(int mediaFormat) {
        switch (mediaFormat) {
            case MediaRecorder.OutputFormat.THREE_GPP:
                return ".3gp";
            default:
                throw new IllegalArgumentException("Media format selected is not supported by this audio recorder.");
        }
    }

    /**
     * Returns true if the file name provided contains the appropriate extension for the media format.
     * @param fileName
     *      File name to check
     * @param mediaFormat
     *      Media format of the actual file
     * @return
     *      True if the file name matches or false otherwise.
     */
    static boolean hasProperExtension(String fileName, int mediaFormat) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        String extension = getExtension(fileName);
        String extensionThatIsShouldBe = getExtension(mediaFormat);
        return extension.equals(extensionThatIsShouldBe);
    }

    /**
     * Returns the extension appended to the end of the file name.
     * @param fileName
     *      File to retrieve extension for
     * @return
     *      Extension of file or blank if there is no extension.
     */
    public static String getExtension(String fileName) {
        int i = fileName != null ? fileName.lastIndexOf('.') : 0;
        return (i > 0) ? fileName.substring(i) : "";
    }

    /**
     * Given the specified path, this will ensure that the file provided is created.
     *
     * If the path is a file, then the file will attempted to be created as-is.
     *
     * @param path
     * @return
     * @throws IOException
     */
    static File createFileIfNecessary(String path) throws IOException {
        File file = new File(path);
        File parent = file.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            throw new IOException("Unable to create folders at location " + path);
        }
        if (!file.exists() && !file.createNewFile() && !file.canWrite()) {
            throw new IOException("Unable to create file at location " + path);
        }
        return file;
    }
}
