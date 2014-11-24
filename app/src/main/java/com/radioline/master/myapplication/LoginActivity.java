package com.radioline.master.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.radioline.master.basic.ParseSetting;
import com.splunk.mint.Mint;

public class LoginActivity extends Activity {

    Button btExit, btLogin;
    EditText etUserId, etPasswordId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, getString(R.string.mint));
        //Mint.enableDebug();

        //ParseObject.registerSubclass(ParseGroups.class);
        try {
            ParseObject.registerSubclass(ParseSetting.class);
            Parse.enableLocalDatastore(getApplicationContext());
            Parse.initialize(this, "5pOXIrqgAidVKFx2mWnlMHj98NPYqbR37fOEkuuY", "oZII0CmkEklLvOvUQ64CQ6i4QjOzBIEGZfbXvYMG");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }


        setContentView(R.layout.activity_login);


        etUserId = (EditText) findViewById(R.id.etUserId);

        ParseQuery<ParseSetting> query = ParseSetting.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo("key", "UserId");
        try {
            ParseSetting getSet = query.getFirst();
            if (getSet.isDataAvailable()) {
                etUserId.setText(getSet.getValue().toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        etPasswordId = (EditText) findViewById(R.id.etPasswordId);

        ParseQuery<ParseSetting> queryPass = ParseSetting.getQuery();
        queryPass.fromLocalDatastore();
        queryPass.whereEqualTo("key", "PasswordId");
        try {
            ParseSetting getSet = queryPass.getFirst();
            if (getSet.isDataAvailable()) {
                etPasswordId.setText(getSet.getValue().toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btExit = (Button) findViewById(R.id.btExit);
        btLogin = (Button) findViewById(R.id.btLogin);
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((etUserId.getText().toString().length() > 0) && (etPasswordId.getText().toString().length() > 0)) {
                    ParseQuery<ParseSetting> queryUID = ParseSetting.getQuery();
                    queryUID.fromLocalDatastore();
                    queryUID.whereEqualTo("key", "UserId");
                    try {
                        if ((queryUID.count() > 0)) {
                            ParseSetting getSet = queryUID.getFirst();
                            getSet.setValue(etUserId.getText().toString());
                        } else {
                            ParseSetting setUID = new ParseSetting();
                            setUID.setKey("UserId");
                            setUID.setValue(etUserId.getText().toString());
                            setUID.pinInBackground();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    ParseQuery<ParseSetting> queryPID = ParseSetting.getQuery();
                    queryPID.fromLocalDatastore();
                    queryPID.whereEqualTo("key", "PasswordId");
                    try {
                        if ((queryPID.count() > 0)) {
                            ParseSetting getSet = queryPID.getFirst();
                            getSet.setValue(etPasswordId.getText().toString());
                        } else {
                            ParseSetting setUID = new ParseSetting();
                            setUID.setKey("PasswordId");
                            setUID.setValue(etPasswordId.getText().toString());
                            setUID.pinInBackground();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    Intent intent = new Intent(LoginActivity.this, FirstGroupActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.NonLoginAndPassword), Toast.LENGTH_LONG).show();
                    if (etUserId.getText().toString().length() == 0) {
                        etUserId.requestFocus();
                    }
                    if (etPasswordId.getText().toString().length() == 0) {
                        etPasswordId.requestFocus();
                    }
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
