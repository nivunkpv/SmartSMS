package digitaldreamers.smartsms.FTC_Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import digitaldreamers.smartsms.Managers.ContactManager;
import digitaldreamers.smartsms.Managers.VersionManager;
import digitaldreamers.smartsms.R;

public class addContacts extends AppCompatActivity {

    TextView mainHeading,addContact,Next;
    LinearLayout SelectedContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        ContactManager.init(this);

        mainHeading = (TextView) findViewById(R.id.addContact_mainHeading);
        SelectedContacts = (LinearLayout) findViewById(R.id.addContact_selectedContactsLL);
        addContact = (TextView) findViewById(R.id.addContact_addcontactButton);
        Next = (TextView) findViewById(R.id.addContact_next);

        mainHeading.setTypeface(VersionManager.Config.getHeadingFont());
        addContact.setTypeface(VersionManager.Config.getHeadingFont());
        Next.setTypeface(VersionManager.Config.getHeadingFont());

        //HIDE THE NEXT BUTTON FIRST WHEN NO CONTACTS ARE THERE.
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) addContact.getLayoutParams();
        params.setMargins(5,0,5,0);
        addContact.setLayoutParams(params);
        Next.setVisibility(View.GONE);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContactManager.getCount() == 5) {
                    Toast.makeText(addContacts.this, "Maximum 5 Contacts", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                Intent intentPickContact = new Intent(Intent.ACTION_PICK,
                        uriContact);
                startActivityForResult(intentPickContact, 1);
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactManager.saveContacts(addContacts.this);
                VersionManager.FTCstack(addContacts.this);
                startActivity(new Intent(addContacts.this,setMessage.class));
            }
        });

        getSavedContacts();
    }


    void getSavedContacts()
    {
        if(ContactManager.getCount()>=1)
        {
            VersionManager.print("COUNT :1");
            Next.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) addContact.getLayoutParams();
            params.setMargins(5,0,2,0);
            addContact.setLayoutParams(params);
        }

        if(ContactManager.getCount()==5)
        {
            VersionManager.print("COUNT :5");
            addContact.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) addContact.getLayoutParams();
            params.setMargins(5,0,5,0);
            Next.setLayoutParams(params);
        }
        VersionManager.print(Integer.toString(ContactManager.getCount()));
        SelectedContacts.removeAllViews();
        for(final ContactManager.Contact contact:ContactManager.getContacts())
        {
            View tmp = ContactManager.infalateContact(this, contact);
            tmp.findViewById(R.id.contact_model_remove).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    removeContact(ContactManager.getContacts().indexOf(contact));
                }
            });
            SelectedContacts.addView(tmp);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                final String Name;
                final ArrayList<String> Numbers = new ArrayList<>();
                Uri returnUri = data.getData();
                Cursor cursor = getContentResolver().query(returnUri, null, null, null, null);
                if (cursor.moveToFirst())
                {
                    Name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Cursor cursorID = getContentResolver().query(returnUri,
                            new String[]{ContactsContract.Contacts._ID},
                            null, null, null);

                    if (cursorID.moveToFirst())
                    {

                        String contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
                        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",

                                new String[]{contactID},
                                null);
                        if(cursorPhone.getCount()==0)
                        {
                            Toast.makeText(this,"Number not found",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        while(cursorPhone.moveToNext())
                        {
                            Numbers.add(cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\s+", ""));
                        }
                        cursorPhone.close();
                        if(Numbers.size()>1)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle(Name);
                            builder.setItems(Numbers.toArray(new String[Numbers.size()]), new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                    newContact(Name, Numbers.get(which));
                                }
                            });
                            builder.create().show();
                        }
                        else
                            newContact(Name, Numbers.get(0));
                    }
                    cursorID.close();
                }

                //Show Next button.

            }
        }
    }

    public void newContact(String name,String number)
    {

        if(checkExists(number))
        {
            Toast.makeText(this,"Number already added",Toast.LENGTH_SHORT).show();
            return;
        }
        VersionManager.print(name + " : " + number);
        ContactManager.Contact contact = new ContactManager.Contact();
        contact.Name = name;
        contact.Number =number;

        ContactManager.addContact(contact);
        getSavedContacts();
    }

    void removeContact(int id)
    {
        ContactManager.removeContact(id);
        SelectedContacts.removeViewAt(id);
        if(ContactManager.getCount()==0)
        {
            Next.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) addContact.getLayoutParams();
            params.setMargins(5,0,5,0);
            addContact.setLayoutParams(params);
        }
    }

    private boolean checkExists(String number)
    {
        for(int c=0;c<SelectedContacts.getChildCount();c++)
        {
            if(((TextView)SelectedContacts.getChildAt(c).findViewById(R.id.contact_model_Number)).getText().toString().contentEquals(number)) {
                return true;
            }
        }
        return false;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
