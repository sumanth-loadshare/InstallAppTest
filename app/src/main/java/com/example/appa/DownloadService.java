package com.example.appa;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadService extends IntentService {

    public static final int PROGRESS_UPDATE = 1;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String urlToDownload = intent.getStringExtra("url");
        ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");

        try {
            //create url and connect
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();
            connection.connect();

            // show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream());

            String path = "/sdcard/Download/appb.apk";
            OutputStream output = new FileOutputStream(path);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;

                // publishing the progress....
                Bundle resultData = new Bundle();
                resultData.putInt("progress", (int) (total * 100 / fileLength));
                receiver.send(PROGRESS_UPDATE, resultData);
                output.write(data, 0, count);
            }

            // close streams
            output.flush();
            output.close();
            input.close();

        }catch (Exception e) {
            e.printStackTrace();
        }

        //*******Different way reference

//        try {
//            URL u = new URL(urlToDownload);
//            InputStream is = u.openStream();
//
//            DataInputStream dis = new DataInputStream(is);
//
//            byte[] buffer = new byte[1024];
//            int length;
//
//            FileOutputStream fos = new FileOutputStream(new File(/*Environment.getExternalStorageDirectory() + "/" + "data/test.kml"*/"/sdcard/test.apk"));
//            while ((length = dis.read(buffer))>0) {
//                fos.write(buffer, 0, length);
//            }
//
//        } catch (MalformedURLException mue) {
//            Log.e("SYNC getUpdate", "malformed url error", mue);
//        } catch (IOException ioe) {
//            Log.e("SYNC getUpdate", "io error", ioe);
//        } catch (SecurityException se) {
//            Log.e("SYNC getUpdate", "security error", se);
//        }


//        int count;
//        try {
//            URL url = new URL(urlToDownload);
//            URLConnection connection = url.openConnection();
//            connection.connect();
//
//            // this will be useful so that you can show a tipical 0-100%
//            // progress bar
//            int lenghtOfFile = connection.getContentLength();
//
//            // download the file
//            InputStream input = new BufferedInputStream(url.openStream(),
//                    8192);
//
//            // Output stream
//            OutputStream output = new FileOutputStream(/*Environment
//                    .getExternalStorageDirectory().toString()
//                    + "/2011.kml"*/"/sdcard/Download/appb.apk");
//
//            byte data[] = new byte[1024];
//
//            long total = 0;
//
//            while ((count = input.read(data)) != -1) {
//                total += count;
//                // publishing the progress....
//                // After this onProgressUpdate will be called
////                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
//
//                Bundle resultData = new Bundle();
//                Log.d("qweqwe",total+"");
//                resultData.putInt("progress", (int) (total * 100 / lenghtOfFile));
//                receiver.send(PROGRESS_UPDATE, resultData);
//                output.write(data, 0, count);
//
//
//                // writing data to file
//                output.write(data, 0, count);
//            }
//
//            // flushing output
//            output.flush();
//
//            // closing streams
//            output.close();
//            input.close();

//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }

        Bundle resultData = new Bundle();
        resultData.putInt("progress", 100);

        receiver.send(PROGRESS_UPDATE, resultData);
    }
}
