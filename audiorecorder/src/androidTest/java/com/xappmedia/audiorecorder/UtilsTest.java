package com.xappmedia.audiorecorder;

import android.media.MediaRecorder;
import android.os.Environment;
import android.test.AndroidTestCase;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class UtilsTest extends AndroidTestCase {

    public void test_getDefaultExternalFilePath() {
        String path = Utils.getDefaultExternalFilePath(getContext());
        assertEquals(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + getContext().getPackageName(), path);
    }

    public void test_getExtensionString() {
        String fileName = "fileName.txt";
        String extension = Utils.getExtension(fileName);
        assertEquals(".txt", extension);

        fileName = "notExtension";
        extension = Utils.getExtension(fileName);
        assertEquals("", extension);

        fileName = null;
        //noinspection ConstantConditions  Useless LINT expression.
        extension = Utils.getExtension(fileName);
        assertEquals("", extension);
    }

    public void test_getExtension() {
        String extension = Utils.getExtension(MediaRecorder.OutputFormat.THREE_GPP);
        assertEquals(".3gp", extension);

        try {
            extension = Utils.generateDefaultFileName(Integer.MIN_VALUE);
            assertFalse("Exception was not thrown for invalid output format and got " + extension, true);
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
            assertFalse("Exception was not thrown for invalid output format and got " + fileName, true);
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
                //noinspection ResultOfMethodCallIgnored  It's a shame if it doesn't get deleted, but not a huge concern for this test.
                createdFile.delete();
            }
        }
    }
}
