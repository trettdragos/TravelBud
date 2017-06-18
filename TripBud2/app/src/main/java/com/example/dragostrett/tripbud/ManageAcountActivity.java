package com.example.dragostrett.tripbud;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ManageAcountActivity extends AppCompatActivity {

    EditText username, email, pass1, pass2;
    CheckBox vis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_acount);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Manage Account");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        username=(EditText)findViewById(R.id.editText_name);
        email=(EditText)findViewById(R.id.editText_email);
        pass1=(EditText)findViewById(R.id.editText_pass1);
        pass2=(EditText)findViewById(R.id.editText_pass2);
        username.setText(UserInfo.getUsername());
        email.setText(UserInfo.getEmail());
    }
    public void saveChanges(View view){
        vis=(CheckBox)findViewById(R.id.checkBoxVisible);
        String aux="0";
        if(vis.isChecked())
            aux="1";
        if(pass1.getText().toString().equals(pass2.getText().toString()) && !username.getText().toString().equals("") && !email.getText().toString().equals("")){
            if(!pass1.getText().toString().equals(""))
            new UpdateUserInfo(this).execute(username.getText().toString(), pass1.getText().toString(), email.getText().toString(),aux);
            else new UpdateUserInfo(this).execute(username.getText().toString(), UserInfo.getPassword(), email.getText().toString(), aux);

        }
        else{
            Toast.makeText(this, "Passwords don't match",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            this.finishAndRemoveTask (); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
