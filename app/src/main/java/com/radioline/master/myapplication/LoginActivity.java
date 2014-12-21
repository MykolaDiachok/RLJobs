package com.radioline.master.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseConfig;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.radioline.master.basic.BaseValues;
import com.radioline.master.basic.Basket;
import com.radioline.master.basic.ParseSetting;
import com.splunk.mint.Mint;

public class LoginActivity extends Activity {


    private Button btExit, btLogin;
    private EditText etUserId, etPasswordId;


    @Override
    protected void onStop() {
        super.onStop();
        Mint.closeSession(this);
        Mint.flush();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Mint.startSession(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this, getString(R.string.mint));
        //Mint.enableDebug();
        setContentView(R.layout.activity_login);

        ParseCrashReporting.enable(this);
        ParseObject.registerSubclass(ParseSetting.class);
        ParseObject.registerSubclass(Basket.class);
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "5pOXIrqgAidVKFx2mWnlMHj98NPYqbR37fOEkuuY", "oZII0CmkEklLvOvUQ64CQ6i4QjOzBIEGZfbXvYMG");
        ParseAnalytics.trackAppOpened(getIntent());

        etUserId = (EditText) findViewById(R.id.etUserId);

        String userID = BaseValues.GetValue("UserId");
        if (userID != null)
            etUserId.setText(userID);

        etPasswordId = (EditText) findViewById(R.id.etPasswordId);
        String passwordId = BaseValues.GetValue("PasswordId");
        if (passwordId != null)
            etPasswordId.setText(passwordId);


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
                login();
            }
        });


        if ((!userID.isEmpty()) && (!passwordId.isEmpty())) {
            login();
        }

    }

    private void login() {
        Integer tlog = null;
        try {
            tlog = Integer.parseInt(etUserId.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, getString(R.string.NoLogin), Toast.LENGTH_LONG).show();
            return;
        }
        if (tlog == null) {
            Toast.makeText(LoginActivity.this, getString(R.string.NoLogin), Toast.LENGTH_LONG).show();
            return;
        }
        ParseUser.logInInBackground(tlog.toString(), etPasswordId.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    ParseUser.getCurrentUser().increment("RunCount");
                    ParseUser.getCurrentUser().saveInBackground();
                    ParseInstallation.getCurrentInstallation().saveInBackground();
                    ParseConfig.getInBackground();

                    ParsePush.subscribeInBackground("", new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                            } else {
                                Log.e("com.parse.push", "failed to subscribe for push", e);
                            }
                        }
                    });

                    Intent intent = new Intent(LoginActivity.this, FirstGroupActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.NoLogin), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
