package digitaldreamers.smartsms.FTC_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import digitaldreamers.smartsms.Managers.VersionManager;
import digitaldreamers.smartsms.R;

public class Welcome extends AppCompatActivity
{

    TextView WelcomeHeading , LetStart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        VersionManager.Config.init(this);

        WelcomeHeading = (TextView) findViewById(R.id.welcome_mainHeading);
        LetStart = (TextView) findViewById(R.id.welcome_letstartbutton);

        WelcomeHeading.setTypeface(VersionManager.Config.getHeadingFont());
        LetStart.setTypeface(VersionManager.Config.getHeadingFont());

        LetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VersionManager.FTCstack(Welcome.this);
                startActivity(new Intent(Welcome.this, addContacts.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
