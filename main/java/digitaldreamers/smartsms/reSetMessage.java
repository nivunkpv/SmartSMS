package digitaldreamers.smartsms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import digitaldreamers.smartsms.Managers.ContactManager;

public class reSetMessage extends AppCompatActivity {

    EditText messageBox;
    TextView template1,template2,template3,done;
    Intent mIntent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_set_message);
        Toolbar actionbar = (Toolbar) findViewById(R.id.reSetMessage_actionbar);
        setSupportActionBar(actionbar);

        getSupportActionBar().setTitle("Edit Message");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIntent =  new Intent();

        messageBox = (EditText) findViewById(R.id.resetMessage_textbox);
        done = (TextView) findViewById(R.id.resetMessage_doneButton);
        template1 = (TextView) findViewById(R.id.resetM_Template1);
        template2 = (TextView) findViewById(R.id.resetM_Template2);
        template3 = (TextView) findViewById(R.id.resetM_Template3);

        messageBox.setText(ContactManager.getMessage(this));

        template1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageBox.setText(((TextView)v).getText());
            }
        });
        template2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageBox.setText(((TextView)v).getText());
            }
        });
        template3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageBox.setText(((TextView)v).getText());
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK,mIntent);
                if(save())
                    finish();

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_OK,mIntent);
        if(save())
            finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                setResult(RESULT_OK,mIntent);
                if(save())
                    finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    boolean save()
    {
        if(messageBox.getText().toString().contentEquals("")||messageBox.getText().toString().contentEquals(" "))
        {
            Toast.makeText(this,"Type or Select a message",Toast.LENGTH_LONG).show();
            return false;
        }
        mIntent.putExtra("changed",true);
        ContactManager.setMessage(this,messageBox.getText().toString());
        return true;
    }


}
