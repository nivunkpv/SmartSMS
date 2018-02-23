package digitaldreamers.smartsms.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import digitaldreamers.smartsms.R;

/**
 * Created by Nived on 07-02-2016.
 */
public abstract class ContactManager
{
    static ArrayList<Contact> Contacts;


    public static void init(Context context)
    {
        Contacts = new ArrayList<>();
        Contacts = getContacts(context);
    }

    public static class Contact
    {
        int id;
        public String Name;
        public String Number;
    }


    public static int getCount()
    {
        return Contacts.size();
    }


    public static ArrayList<Contact> getContacts(Context context)
    {
        Gson gson = new Gson();
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        String data = sharedpreferences.getString("contacts",null);
        if(data==null)
            return new ArrayList<Contact>();
        return gson.fromJson(data, new TypeToken<ArrayList<Contact>>() {
        }.getType());
    }

    public static ArrayList<Contact> getContacts()
    {
        return Contacts;
    }

    public static void saveContacts(Context context)
    {
        Gson gson = new Gson();
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        VersionManager.print(gson.toJson(Contacts));
        sharedpreferences.edit().putString("contacts",gson.toJson(Contacts)).apply();

    }

    public static void addContact(Contact contact)
    {
        Contacts.add(contact);
    }

    public static void removeContact(int id)
    {
        Contacts.remove(id);
    }


    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * (metrics.densityDpi / 160f));
        return px;
    }

    public static View infalateContact(final Context context, final Contact contact)
    {
        LayoutInflater inflater = (LayoutInflater)context.getApplicationContext().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_contact_model, null);
        TextView Name = (TextView) view.findViewById(R.id.contact_model_Name);
        TextView Number = (TextView) view.findViewById(R.id.contact_model_Number);

        Name.setText(contact.Name);
        Number.setText(contact.Number);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,convertDpToPixel(60,context));
        view.setLayoutParams(params);
        return view;
    }

    public static void setMessage(Context context ,String msg)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        sharedpreferences.edit().putString("message",msg).apply();
    }

    public static String getMessage(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences("digitaldreamers.smartsms", Context.MODE_PRIVATE);
        return sharedpreferences.getString("message",null);
    }

}
