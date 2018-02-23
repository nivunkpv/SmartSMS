package digitaldreamers.smartsms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import digitaldreamers.smartsms.Adapters.service;
import digitaldreamers.smartsms.FTC_Activities.Welcome;
import digitaldreamers.smartsms.Managers.ContactManager;
import digitaldreamers.smartsms.Managers.VersionManager;


public class Home extends AppCompatActivity {


    private Toolbar mToolbar;
    private ArrayList<ContactManager.Contact> Contacts;
    private LinearLayout SelectedContacts;
    private SwitchCompat switchCompat;
    private ImageButton contact_edit;
    private ImageButton message_edit;
    private TextView ServiceStatus,Messageview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(VersionManager.isFirstRun(this))
        {
            startActivity(new Intent(this, Welcome.class));
            finish();
        }

        ContactManager.init(this);
        VersionManager.Config.init(this);

        if(VersionManager.startonBoot(this))
            if(VersionManager.isServiceEnabled(this))
                startService(new Intent(this,service.class));

        setContentView(R.layout.activity_home);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        ServiceStatus = (TextView) findViewById(R.id.serviceStatus_tv);
        SelectedContacts = (LinearLayout) findViewById(R.id.home_SelectedContactsLL);
        Contacts = ContactManager.getContacts(this);
        switchCompat = (SwitchCompat) findViewById(R.id.home_switch);
        contact_edit = (ImageButton) findViewById(R.id.contact_edit);
        message_edit = (ImageButton) findViewById(R.id.message_edit);
        Messageview = (TextView) findViewById(R.id.messageView);

        ((TextView)findViewById(R.id.textView5)).setTypeface(VersionManager.Config.getHeadingFont());
        ((TextView)findViewById(R.id.textView7)).setTypeface(VersionManager.Config.getHeadingFont());
        ((TextView)findViewById(R.id.textView8)).setTypeface(VersionManager.Config.getHeadingFont());


        View tmp;
        for(ContactManager.Contact contact:Contacts)
        {
            tmp = ContactManager.infalateContact(this, contact);
            tmp.findViewById(R.id.contact_model_remove).setVisibility(View.GONE);
            SelectedContacts.addView(tmp);
        }

        if(VersionManager.isServiceEnabled(this))
        {
            switchCompat.setChecked(true);
            ServiceStatus.setText("Service is Enabled");
        }
        Messageview.setText(ContactManager.getMessage(this));

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    ServiceStatus.setText("Service is Enabled");
                    VersionManager.enableService(Home.this);
                    startService(new Intent(Home.this, service.class));
                }
                else
                {
                    ServiceStatus.setText("Service is Disabled");
                    VersionManager.disableService(Home.this);
                    stopService(new Intent(Home.this, service.class));
                }

            }
        });

        contact_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Home.this, reSelectContacts.class), 2);
            }
        });

        message_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Home.this, reSetMessage.class), 2);
            }
        });


    }


    public void reDo()
    {
        VersionManager.print("HOME:REDO");
        Messageview.setText(ContactManager.getMessage(this));
        SelectedContacts.removeAllViews();
        View tmp;
        for(ContactManager.Contact contact:ContactManager.getContacts(this))
        {
            tmp = ContactManager.infalateContact(this, contact);
            tmp.findViewById(R.id.contact_model_remove).setVisibility(View.GONE);
            SelectedContacts.addView(tmp);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            startActivityForResult(new Intent(this, Settings.class), 2);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        VersionManager.print("IN HOME::RESULTS");
        if (requestCode == 2)
        {
            if(data.getBooleanExtra("changed",false))
                Snackbar.make(findViewById(R.id.home_scroll), "Changes Saved", Snackbar.LENGTH_SHORT).show();
            reDo();
        }
    }
}
