package cn.edu.whu.addetecting.util;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;
import java.util.Random;

import cn.edu.whu.addetecting.service.UploadService;

public class AlarmUtil {
    private static final long TIME_INTERVAL = 24 * 60 * 60 * 1000; // 闹钟执行任务的时间间隔
    private Context context;
    public static AlarmManager am;
    public static PendingIntent pendingIntent;
    private Calendar calendar;
    //singleton
    private static AlarmUtil instance = null;
    private AlarmUtil(Context aContext) {
        this.context = aContext;
    }



    public static AlarmUtil getInstance(Context aContext) {
        if (instance == null) {
            synchronized (AlarmUtil.class) {
                if (instance == null) {
                    instance = new AlarmUtil(aContext);
                }
            }
        }
        return instance;
    }

    public void createGetUpAlarmManager() {
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, UploadService.class);
        pendingIntent = PendingIntent.getService(context, 0, intent, 0);//每隔10秒启动一次服务
    }

    @SuppressLint("NewApi")
    public void getUpAlarmManagerStartWork() {
        Random random = new Random(114514);
        calendar = Calendar.getInstance();
        // 18:00 到 23:59
        calendar.set(Calendar.HOUR_OF_DAY, 18 + random.nextInt(6));
        calendar.set(Calendar.MINUTE, random.nextInt(60));
        calendar.set(Calendar.SECOND, 0);//这里代表 21.14.00

        //版本适配 System.currentTimeMillis()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0及以上
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4及以上
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    pendingIntent);
        } else {
            am.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), TIME_INTERVAL, pendingIntent);
        }
    }

    @SuppressLint("NewApi")
    public void getUpAlarmManagerWorkOnOthers() {
        //高版本重复设置闹钟达到低版本中setRepeating相同效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0及以上
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + TIME_INTERVAL, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4及以上
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                    + TIME_INTERVAL, pendingIntent);
        }
    }
}
