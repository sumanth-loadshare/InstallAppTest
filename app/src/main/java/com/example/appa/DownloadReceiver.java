package com.example.appa;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class DownloadReceiver extends ResultReceiver {

    Callback callback;

    public DownloadReceiver(Handler handler, Callback callback) {
        super(handler);
        this.callback = callback;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        super.onReceiveResult(resultCode, resultData);

        if (resultCode == DownloadService.PROGRESS_UPDATE) {

            int progress = resultData.getInt("progress"); //get the progress
            callback.onProgress(progress);
//            dialog.setProgress(progress);

            if (progress == 100) {
//                dialog.dismiss();
                callback.onCompleted();
            }
        }
    }
}
