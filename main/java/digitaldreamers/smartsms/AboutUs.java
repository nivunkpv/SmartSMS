package digitaldreamers.smartsms;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import digitaldreamers.smartsms.Managers.VersionManager;

public class AboutUs extends AppCompatActivity {

    ImageView logo;
    ImageView text;
    TextView invoid_Text;
    Toolbar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        logo = (ImageView) findViewById(R.id.dd_logo);
        text = (ImageView) findViewById(R.id.dd_text);
        invoid_Text = (TextView) findViewById(R.id.invoid_text);
        actionbar = (Toolbar) findViewById(R.id.about_actionbar);

        setSupportActionBar(actionbar);
        getSupportActionBar().setTitle("About US");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        invoid_Text.setTypeface(VersionManager.Config.getHeadingFont());
        logo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AboutUs.this);
                builder.setTitle("Credits");
                builder.setItems(new String[]{"Nived Kumar", "Azfar Abdul Jabbar"}, null);
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });

        invoid_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/invoidsecnar"));
                startActivity(browserIntent);
            }
        });
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
}
