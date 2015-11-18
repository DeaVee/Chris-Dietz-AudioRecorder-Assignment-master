package com.xappmedia.audiorecorderharness;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xappmedia.audiorecorder.AudioRecorderDialogActivity;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements FileAdapter.FileAdapterListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private static final int RESULT_CODE_RECORDER = 1;

    private RecyclerView listView;
    private FileAdapter listAdapter;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(view.getContext(), AudioRecorderDialogActivity.class);
                startActivityForResult(intent, RESULT_CODE_RECORDER);
            }
        });

        listAdapter = new FileAdapter();
        listAdapter.setListener(this);

        listView = (RecyclerView) findViewById(R.id.fileList);
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(listAdapter);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);


        File mainFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), getPackageName());
        if (!mainFolder.exists() && mainFolder.mkdirs()) {
            Snackbar.make(listView, "There was an error creating the recordings directory.", Snackbar.LENGTH_SHORT).show();
        } else {
            File[] filesList = mainFolder.listFiles();
            listAdapter.addFiles(filesList);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopPlayer();
        mediaPlayer.release();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE_RECORDER) {
            if (resultCode == RESULT_OK) {
                final Uri fileUri = data.getParcelableExtra(AudioRecorderDialogActivity.RESULT_ARG_FILE_URI);
                File file = new File(fileUri.getPath());
                listAdapter.addFile(file);
            }
        }
    }

    @Override
    public void onFileSelected(File file) {
        play(file);
    }

    @Override
    public void onFileDelete(File file) {
        stopPlayer();
        if (file.delete()) {
            listAdapter.removeFile(file);
        }
    }

    private void stopPlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        mediaPlayer.reset();
    }

    private void play(File file) {
        stopPlayer();
        try {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Snackbar.make(listView, "There was an error playing the file " + file.getName(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.reset();
    }
}
