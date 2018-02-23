package digitaldreamers.smartsms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import digitaldreamers.smartsms.Managers.VersionManager;

public class Settings extends AppCompatActivity {

    CheckBox onBoot;
    TextView batterThreshold , EditContacts , EditMessage, Aboutus;

    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_actionbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        mIntent = new Intent();
        setResult(RESULT_OK,mIntent);

        ((TextView) findViewById(R.id.textView6)).setTypeface(VersionManager.Config.getHeadingFont());
        ((TextView)findViewById(R.id.textView12)).setTypeface(VersionManager.Config.getHeadingFont());
        ((TextView)findViewById(R.id.textView15)).setTypeface(VersionManager.Config.getHeadingFont());

        onBoot = (CheckBox) findViewById(R.id.settings_onboot);
        batterThreshold = (TextView) findViewById(R.id.settings_threshold);
        EditContacts = (TextView) findViewById(R.id.settings_editcontacts);
        EditMessage = (TextView) findViewById(R.id.settings_editmessage);
        Aboutus = (TextView) findViewById(R.id.settings_about);

        onBoot.setChecked(VersionManager.startonBoot(this));
        batterThreshold.setText("Battery Threshold Level :" + Integer.toString(VersionManager.getBatteryThreshold(this)) + "%");

        EditContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Settings.this, reSelectContacts.class), 3);
            }
        });

        EditMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Settings.this, reSetMessage.class), 3);
            }
        });

        Aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, AboutUs.class));
            }
        });

        onBoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    VersionManager.startonBootOn(Settings.this);
                else
                    VersionManager.startonBootOff(Settings.this);
            }
        });

        batterThreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final EditText text = new EditText(Settings.this);
                text.setInputType(InputType.TYPE_CLASS_NUMBER);

                new AlertDialog.Builder(Settings.this)
                        .setTitle("Set Battery Threshold")
                        .setMessage("Type a battery percentage for alerting")
                        .setView(text)
                        .setPositiveButton("Set Threshold", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                VersionManager.setBatteryThreshold(Settings.this, Integer.parseInt(text.getText().toString()));
                                batterThreshold.setText("Battery Threshold Level :" + text.getText().toString() + "%");
                                mIntent.putExtra("changed",true);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        VersionManager.print("IN HOME::RESULTS");
        if (requestCode == 3)
        {
            setResult(RESULT_OK,data);
        }
    }
}
