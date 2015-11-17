package com.xappmedia.audiorecorder;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xappmedia.audiorecorder.exceptions.AudioRecorderError;

import java.io.File;

/**
 * An {@link AudioRecorderActivity} that contains a user interface for people to interact with the
 * audio recorder.
 */
public final class AudioRecorderDialogActivity extends AudioRecorderActivity {

    private Dialog dialog;
    private DialogViewController viewController;

    @Override
    public void onStart() {
        super.onStart();
        dialog = createDialog(this);
        viewController = new DialogViewController(dialog);
        addRecorderListener(viewController);
    }

    private static Dialog createDialog(Context ctx) {
        final View dialogView = LayoutInflater.from(ctx).inflate(R.layout.recorder_dialog_interface, null);
        return new AlertDialog.Builder(ctx, R.style.AudioRecorder_Dialog)
                .setCancelable(true)
                .setView(dialogView)
                .show();
    }

    private class DialogViewController implements View.OnClickListener, RecorderListener, DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
        final View dialogView;
        final Button playButton;
        final TextView timerText;

        public DialogViewController(Dialog dialog) {
            dialog.setOnCancelListener(this);
            dialog.setOnDismissListener(this);
            dialogView = dialog.findViewById(R.id.dialogRoot);
            if (dialogView == null) {
                throw new IllegalArgumentException("The dialog view must contain the appropriate root view.");
            }
            playButton = (Button) dialogView.findViewById(R.id.btnPlayPause);
            playButton.setOnClickListener(this);
            timerText = (TextView) dialogView.findViewById(R.id.txtTimer);
            onRecordTime(0);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnPlayPause) {
                playPressed();
            }
        }

        private void playPressed() {
            if (isRecording()) {
                stopRecording();
            } else {
                startRecording();
            }
        }

        @Override
        public void onRecorderStart() {
            playButton.setText(R.string.stop);
        }

        @Override
        public void onRecorderPause() {
            playButton.setText(R.string.play);
        }

        @Override
        public void onRecorderStop(File file) {
            playButton.setText(R.string.play);
        }

        @Override
        public void onRecorderError(AudioRecorderError e) {
            playButton.setText(R.string.play);
        }

        @Override
        public void onRecordTime(int seconds) {
            int hours = seconds / 3600;
            seconds -= hours * 3600;
            int minutes = seconds / 60;
            seconds -= minutes * 60;
            String text = getString(R.string.time, hours, minutes, seconds);
            timerText.setText(text);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            finish();
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            setResult(RESULT_CANCELED);
            // dismiss listener will handle the dismissal
        }
    }
}
