package digitaldreamers.smartsms.Managers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created by Nived on 05-02-2016.
 */
public abstract class VersionManager
{

    static ArrayList<String> Names;
    static ArrayList<String> Numbers;
    static ArrayList<Activity> FTCs;

    public static abstract class Config
    {
        static Typeface HeadingFont;
        static boolean DEBUG = true;



        public static void init(Context context)
        {
            HeadingFont = Typeface.createFromAsset(context.getAssets(), "segoeuib.ttf");
            FTCs = new ArrayList<>();
            //initContact(context);
        }


        public static Typeface getHeadingFont()
        {
            return HeadingFont;
        }
    }

    public static void print(String data)
    {
        if(Config.DEBUG)
        {
            System.out.println(data);
        }
    }

    public static void print(String[] data)
    {
        if(Config.DEBUG)
        {
            for(String d:data)
            {
                print(d);
            }
        }
    }

    public static boolean isServiceEnabled(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean("servicestate",false);
    }

    public static void enableService(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        sharedpreferences.edit().putBoolean("servicestate",true).apply();
    }

    public static void disableService(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        sharedpreferences.edit().putBoolean("servicestate",false).apply();
    }


    public static boolean isFirstRun(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean("firstrun",true);
    }

    public static void FTCDone(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        sharedpreferences.edit().putBoolean("firstrun",false).apply();
        startonBootOn(context);
        for(Activity activity:FTCs)
        {
            activity.finish();
        }
    }

    public static void FTCstack(Activity activity)
    {
        FTCs.add(activity);
    }


    public static void initContact(Context context)
    {
        Names = new ArrayList<>();
        Numbers = new ArrayList<>();

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");

        if (cur.getCount() > 0)
        {
            while (cur.moveToNext())
            {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    //System.out.println("name : " + name + ", ID : " + id);

                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext())
                    {
                        String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Names.add(name);
                        Numbers.add(phone);
                    }
                    pCur.close();
                }
            }
        }
    }

    public static ArrayList<String> getNames()
    {
        return Names;
    }

    public static ArrayList<String> getNumbers()
    {
        return Numbers;
    }

    public static boolean startonBoot(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean("onboot",false);
    }

    public static void startonBootOn(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        sharedpreferences.edit().putBoolean("onboot",true).apply();
    }

    public static void startonBootOff(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        sharedpreferences.edit().putBoolean("onboot",false).apply();
    }

    public static int getBatteryThreshold(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        return sharedpreferences.getInt("threshold",3);
    }

    public static void setBatteryThreshold(Context context,int threshold)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        sharedpreferences.edit().putInt("threshold",threshold).apply();
    }

}
