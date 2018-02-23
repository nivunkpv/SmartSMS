package digitaldreamers.smartsms;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import digitaldreamers.smartsms.Managers.ContactManager;
import digitaldreamers.smartsms.Managers.VersionManager;

public class reSelectContacts extends AppCompatActivity
{
    TextView addContact;
    LinearLayout SelectedContacts;
    Intent mIntent ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reselectcontacts);
        Toolbar actionbar = (Toolbar) findViewById(R.id.reSelectContacts_actionbar);
        setSupportActionBar(actionbar);

        getSupportActionBar().setTitle("Edit Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIntent =  new Intent();

        addContact = (TextView) findViewById(R.id.reselectContact_addcontactButton);
        SelectedContacts = (LinearLayout) findViewById(R.id.reSelectContacts_SelectedContacts);

        addContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ContactManager.getCount() == 5)
                {
                    Toast.makeText(reSelectContacts.this, "Maximum 5 Contacts", Toast.LENGTH_SHORT).show();
                    return;
                }


                final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
                Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
                startActivityForResult(intentPickContact, 1);
            }
        });

        getSavedContacts();
    }

    void getSavedContacts()
    {
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

    public void newContact(String name,String number)
    {
        mIntent.putExtra("changed",true);
        setResult(RESULT_OK, mIntent);
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
        mIntent.putExtra("changed", true);

        ContactManager.removeContact(id);
        SelectedContacts.removeViewAt(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                setResult(RESULT_OK,mIntent);
                if (ContactManager.getCount() == 0)
                {
                    Toast.makeText(reSelectContacts.this, "Atleast 1 contact is needed", Toast.LENGTH_SHORT).show();
                    return false;
                }
                ContactManager.saveContacts(this);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_OK,mIntent);
        if (ContactManager.getCount() == 0)
        {
            Toast.makeText(reSelectContacts.this, "Atleast 1 contact is needed", Toast.LENGTH_SHORT).show();
            return;
        }
        ContactManager.saveContacts(this);
        finish();
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
                            Toast.makeText(this, "Number not found", Toast.LENGTH_SHORT).show();
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

}
