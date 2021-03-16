package cn.edu.whu.addetecting.service;

import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Random;

import cn.edu.whu.addetecting.util.AlarmUtil;
import cn.edu.whu.addetecting.util.UploadUtil;

public class UploadService extends Service {

    private static final String tag = "Upload Service";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(tag, "开始定时上传任务...");
        UploadUtil.execUploadData("acceleration");
        AlarmUtil.getInstance(getApplicationContext()).getUpAlarmManagerWorkOnOthers();

        return super.onStartCommand(intent, flags, startId);
    }
}
