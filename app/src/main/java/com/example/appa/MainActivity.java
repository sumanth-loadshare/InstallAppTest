package com.example.appa;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        findViewById(R.id.install_appb).setOnClickListener(view -> {
            downloadAndOpenFile();
        });

        findViewById(R.id.start_appb).setOnClickListener(view -> {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.example.appb", "com.example.appb.DemoActivity"));
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "AppB is not installed or updated", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
        });
    }

    private void downloadAndOpenFile() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        Intent intent = new Intent(this, DownloadService.class);
//        intent.putExtra("url", "https://fibonalabs-my.sharepoint.com/:u:/p/ranjith_s/ETBlAl7IlTdIlIEVeSOgijMBszz0tnEi92frXUBzjnTsVg?e=JQmkLv");
        intent.putExtra("url", "https://drive.google.com/uc?export=download&id=1g7ibe0jRGNT7gIbGPOoDEbNCtpl0J4xU");
        intent.putExtra("receiver", new DownloadReceiver(new Handler(), new Callback() {
            @Override
            public void onProgress(int progress) {
                progressDialog.setProgress(progress);
            }

            @Override
            public void onCompleted() {
                progressDialog.dismiss();
                OpenNewVersion("/sdcard/Download/appb.apk");
            }
        }));
        startService(intent);


        //*******AssetManager reference

//        AssetManager assetManager = getAssets();
//
//        InputStream in = null;
//        OutputStream out = null;
//
//        try {
//            in = assetManager.open("appb.apk");
//            out = new FileOutputStream("/sdcard/Download/appb.apk");
//
//            byte[] buffer = new byte[1024];
//
//            int read;
//            while((read = in.read(buffer)) != -1) {
//
//                out.write(buffer, 0, read);
//
//            }
//
//            in.close();
//            in = null;
//
//            out.flush();
//            out.close();
//            out = null;
//
//            OpenNewVersion("/sdcard/Download/appb.apk");

//        } catch(Exception e) {
//            e.printStackTrace();
//        }

    }

    void OpenNewVersion(String location) {
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", new File(location));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
}