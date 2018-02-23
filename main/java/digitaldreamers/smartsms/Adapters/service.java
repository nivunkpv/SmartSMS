package digitaldreamers.smartsms.Adapters;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import digitaldreamers.smartsms.Home;
import digitaldreamers.smartsms.R;

public class service extends Service
{
    BatteryChangeReceiver Receiver ;
    public service()
    {

    }

    public void onCreate()
    {
        //Toast.makeText(this,"OnCreate",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Receiver = new BatteryChangeReceiver();
        //Toast.makeText(this,"OnStartCommand",Toast.LENGTH_SHORT).show();
        registerReceiver(Receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Intent pintent = new Intent(this, Home.class);
        PendingIntent pp = PendingIntent.getActivity(this, 0, pintent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Smartsms")
                .setContentText("Service is Enabled")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pp)
                .setOngoing(true).getNotification();
        startForeground(101, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        stopForeground(true);
        unregisterReceiver(Receiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
