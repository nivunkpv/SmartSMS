package digitaldreamers.smartsms.FTC_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import digitaldreamers.smartsms.Home;
import digitaldreamers.smartsms.Managers.ContactManager;
import digitaldreamers.smartsms.Managers.VersionManager;
import digitaldreamers.smartsms.R;

public class setMessage extends AppCompatActivity {

    TextView Heading,Done,Template1,Template2,Template3;
    EditText MessageText;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_message);

        Heading = (TextView) findViewById(R.id.setMessage_Heading);
        Done = (TextView) findViewById(R.id.setMessage_done);
        Template1 = (TextView) findViewById(R.id.setMessage_template1);
        Template2 = (TextView) findViewById(R.id.setMessage_template2);
        Template3 = (TextView) findViewById(R.id.setMessage_template3);

        MessageText = (EditText) findViewById(R.id.setMessage_messageText);

        Heading.setTypeface(VersionManager.Config.getHeadingFont());
        Done.setTypeface(VersionManager.Config.getHeadingFont());

        MessageText.clearFocus();

        Template1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageText.setText(Template1.getText());
            }
        });

        Template2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageText.setText(Template2.getText());
            }
        });

        Template3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageText.setText(Template3.getText());
            }
        });

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MessageText.getText().toString().contentEquals("")||MessageText.getText().toString().contentEquals(" "))
                {
                    Toast.makeText(setMessage.this,"Type or Select a Message",Toast.LENGTH_LONG).show();
                    return;
                }
                ContactManager.setMessage(setMessage.this, MessageText.getText().toString());
                VersionManager.FTCDone(setMessage.this);
                VersionManager.enableService(setMessage.this);
                startActivity(new Intent(setMessage.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }
}
