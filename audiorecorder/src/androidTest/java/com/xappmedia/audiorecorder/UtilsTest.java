package com.xappmedia.audiorecorder;

import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.os.Environment;
import android.test.AndroidTestCase;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class UtilsTest extends AndroidTestCase {

    public void test_getDefaultExternalFilePath() {
        String path = Utils.getDefaultExternalFilePath(getContext());
        assertEquals(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + BuildConfig.APPLICATION_ID, path);
    }

    public void test_getExtensionString() {
        String fileName = "fileName.txt";
        String extension = Utils.getExtension(fileName);
        assertEquals(".txt", extension);

        fileName = "notExtension";
        extension = Utils.getExtension(fileName);
        assertEquals("", extension);

        fileName = null;
        extension = Utils.getExtension((String) null);
        assertEquals("", extension);
    }

    public void test_getExtension() {
        String extension = Utils.getExtension(MediaRecorder.OutputFormat.THREE_GPP);
        assertEquals(".3gp", extension);

        try {
            extension = Utils.generateDefaultFileName(Integer.MIN_VALUE);
            assertFalse("Exception was not thrown for invalid output format.", true);
        } catch (Exception e) {
            // worked.
        }
    }

    public void test_getDefaultFileName() {
        String fileName = Utils.generateDefaultFileName(MediaRecorder.OutputFormat.THREE_GPP);
        assertNotNull(fileName);
        String extension = Utils.getExtension(fileName);
        assertEquals(".3gp", extension);

        try {
            fileName = Utils.generateDefaultFileName(Integer.MIN_VALUE);
            assertFalse("Exception was not thrown for invalid output format.", true);
        } catch (Exception e) {
            // worked
        }
    }

    public void test_createFileIfNecessary() {
        String fullName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/TestFolder2/fakeFile.txt";
        File createdFile = null;
        try {
            createdFile = Utils.createFileIfNecessary(fullName);
            assertNotNull(createdFile);
            assertTrue(createdFile.getAbsoluteFile() + " is not a file.", createdFile.isFile());
            assertTrue(createdFile.canWrite());

            File path = createdFile.getParentFile();
            assertEquals(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/TestFolder2", path.getAbsolutePath());
        } catch (IOException e) {
            assertFalse("IOException while trying to create file: " + e.getMessage(), true);
        } finally {
            if (createdFile != null) {
                createdFile.delete();
            }
        }
    }
}
