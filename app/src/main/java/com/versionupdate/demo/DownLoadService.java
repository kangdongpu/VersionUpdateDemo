package com.versionupdate.demo;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by kangdongpu on 2017/2/16.
 */
public class DownLoadService extends Service {

    private DownloadManager downloadManager;
    private long myId;

    private DownLoadBroadCastReceiver downLoadBroadCastReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //实例化DownloadManager下载对象
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        String url = intent.getStringExtra("url");

        //取出上次保存下载id，如果id不为-1,再根据id获取下载的状态，如果下载完成则去安装新版本(在此之前先比较版本信息),否则重新下载apk
        long downloadApkId = SPUtils.getInstance().getApkId();

        if (downloadApkId != -1L) {
            //
            int status = getDownloadStatus(downloadApkId);

            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                Uri uri = getDownloadUri(downloadApkId);
                if (uri != null) {
                    //对比下载的apk版本和本地应用版本
                    if (compare(getApkInfo(uri.getPath()))) {
                        installApk(uri);
                    } else {
                        downloadManager.remove(downloadApkId);
                        startDownload(url);
                    }

                }

            } else if (status == DownloadManager.STATUS_FAILED) {
                startDownload(url);
            } else {
                Log.d("DownLoadService", "apk is already downloading");
            }
        } else {
            startDownload(url);
        }

        return Service.START_STICKY;
    }


    private void startDownload(final String url) {


        //注册下载完成广播
        downLoadBroadCastReceiver = new DownLoadBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downLoadBroadCastReceiver, intentFilter);


        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("V+Event");
        request.setDescription("V+Event");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //下载路径
        //第一种
        //storage/emulated/0/Android/data/your-package/files/Download/update.apk
        request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.DIRECTORY_DOWNLOADS, "updata.apk");
        //第二种
        ///storage/emulated/0/Download/update.apk
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "updata.apk");
        myId = downloadManager.enqueue(request);
        //保存下载的id,第一次下载apk完成后如果用户按了返回键没有进行安装，下次进行版本更新的时候就不需要再重新下载了
        SPUtils.getInstance().setApkId(myId);
    }


    private class DownLoadBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (myId == id) {
                //下载完成
                unregisterReceiver(downLoadBroadCastReceiver);
                //跳到安装界面
                installApk(getDownloadUri(id));
            }
        }
    }

    /**
     * 安装apk
     *
     * @param uri
     */
    private void installApk(Uri uri) {

        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(installIntent);

        //停止service
        stopSelf();
    }


    /**
     * 获取下载状态
     *
     * @param downloadApkId
     * @return
     */
    private int getDownloadStatus(long downloadApkId) {

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadApkId);

        Cursor c = downloadManager.query(query);

        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                c.close();
            }
        } else {
            Log.d("c", "c is null");
        }
        return -1;

    }

    /**
     * 获取保存的apk文件的地址
     *
     * @param downloadApkId
     * @return
     */
    private Uri getDownloadUri(long downloadApkId) {
        return downloadManager.getUriForDownloadedFile(downloadApkId);
    }


    /**
     * 获取下载的apk版本信息
     *
     * @param path
     * @return
     */
    private PackageInfo getApkInfo(String path) {
        PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {

            return info;
        }
        return null;
    }

    /**
     * 如果当前版本号小于apk的版本号则返回true
     *
     * @param apkInfo
     * @return
     */
    private boolean compare(PackageInfo apkInfo) {
        if (apkInfo == null) {
            return false;
        }
        int versionCode = AppUtils.getCurrentVersionCode(this);

        if (apkInfo.versionCode > versionCode) {
            return true;
        }

        return false;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (downLoadBroadCastReceiver != null) {
            unregisterReceiver(downLoadBroadCastReceiver);
        }
        Log.d("DownLoadService", "-----------------DownLoadService  onDestory");
    }
}
