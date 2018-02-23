package digitaldreamers.smartsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import digitaldreamers.smartsms.Adapters.service;
import digitaldreamers.smartsms.Managers.VersionManager;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent servs = new Intent(context,service.class);
        if(VersionManager.startonBoot(context) && VersionManager.isServiceEnabled(context))
        {
            context.startService(servs);
        }

    }
}
