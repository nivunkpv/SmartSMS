package digitaldreamers.smartsms.Adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;

import digitaldreamers.smartsms.Managers.ContactManager;
import digitaldreamers.smartsms.Managers.VersionManager;

public class BatteryChangeReceiver extends BroadcastReceiver {



    Context context;
    static boolean sent = false;
    double threshold ;
    public BatteryChangeReceiver()
    {

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.threshold = (double)VersionManager.getBatteryThreshold(context);
        this.context = context;
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int maxLevel = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
        VersionManager.print(Integer.toString(batteryLevel));
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||status == BatteryManager.BATTERY_STATUS_FULL;
        float batteryPercentage = ((float) batteryLevel / (float) maxLevel) * 100;
        if(batteryPercentage>threshold)
            sent=true;
        if(batteryPercentage==threshold && sent && VersionManager.isServiceEnabled(context) && !isCharging)
        {
            _("SmartSMS sending alert");
            send();
            sent=false;
        }

    }

    void send()
    {
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<ContactManager.Contact> contacts = ContactManager.getContacts(context);
        String msg = ContactManager.getMessage(context);
        for(ContactManager.Contact contact:contacts)
        {
            smsManager.sendTextMessage(contact.Number, null, msg, null, null);
        }

    }

    void _(String text)
    {
        Toast.makeText(this.context, text, Toast.LENGTH_SHORT).show();
    }
}

