package com.versionupdate.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    Intent intentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public void checkUpdate(View view) {
        showUpdataDialog();
    }

    private void showUpdataDialog() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);

        build.setTitle("版本更新");
        build.setMessage("检测到有新版本,是否要更新?");
        build.setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        build.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                intentService = new Intent(MainActivity.this, DownLoadService.class);
                intentService.putExtra("url", "http://211.136.65.152/cache/gdown.baidu.com/data/wisegame/fc328fa3a33efe57/QQ_482.apk?ich_args2=110-16165507022051_387cf7850833aac3cd93cea162a5c2cb_10068001_9c886c2bd5c0f4d2923c518939a83798_dfb6a903901fca6273ffe2ba3d94635f");
                startService(intentService);
            }
        });
        build.create().show();
    }

}
