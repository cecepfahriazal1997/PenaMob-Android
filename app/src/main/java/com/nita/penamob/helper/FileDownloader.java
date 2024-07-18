package com.nita.penamob.helper;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class FileDownloader {
    private Context context;
    private long downloadID;
    private ProgressDialog progressDialog;
    private DownloadManager downloadManager;
    private String fileName;

    public FileDownloader(Context context) {
        this.context = context;
        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void startDownload(String fileUrl) {
        fileName = getFileNameFromUrl(fileUrl);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl))
                .setTitle("Downloading File")
                .setDescription("Downloading " + fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadID = downloadManager.enqueue(request);

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Downloading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean downloading = true;
                    while (downloading) {
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(downloadID);
                        Cursor cursor = downloadManager.query(query);
                        if (cursor.moveToFirst()) {
                            int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                            int bytesDownloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                            int bytesTotalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);

                            if (statusIndex != -1 && bytesDownloadedIndex != -1 && bytesTotalIndex != -1) {
                                int status = cursor.getInt(statusIndex);
                                int bytesDownloaded = cursor.getInt(bytesDownloadedIndex);
                                int bytesTotal = cursor.getInt(bytesTotalIndex);

                                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                    downloading = false;
                                }

                                final int dlProgress = (int) ((bytesDownloaded * 100L) / bytesTotal);

                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.setProgress(dlProgress);
                                    }
                                });
                            }
                        }
                        cursor.close();
                    }
                }
            }).start();
        }
    }

    private String getFileNameFromUrl(String fileUrl) {
        return Uri.parse(fileUrl).getLastPathSegment();
    }

    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                progressDialog.dismiss();
                Toast.makeText(context, "Download Completed: " + fileName, Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void unregisterReceiver() {
        context.unregisterReceiver(onDownloadComplete);
    }
}