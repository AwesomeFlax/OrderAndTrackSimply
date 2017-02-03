package com.example.denis.orderandtracksimply;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

public class Login extends Activity
{
    // access variables
    DigitsAuthButton digitsButton;
    Intent intent;
    SharedPreferences sPref;
    String user_data = "";

    // screen data
    EditText Email;
    Button login_email;
    Button login_phone;
    CheckBox remember;

    // twitter keys
    private static final String TWITTER_KEY = "ERP6uVK9qi0liApgFEhPakDTf";
    private static final String TWITTER_SECRET = "NKMxW5EAMDN4YslwDWvtWvsLxd21dtONOePcucm4aW31qP8eBK";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
        setContentView(R.layout.activity_login);

        // fonts init
        //Typeface bold = Typeface.createFromAsset(getAssets(), getString(R.string.bold_font));
        Typeface regular = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));
        //Typeface medium = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));

        intent = new Intent(this, MainZone.class);

        // making notification bar colored
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.deep_blue));
        }

        // screen elements
        Email = (EditText) findViewById(R.id.email);
        login_email = (Button) findViewById(R.id.using_email);
        login_phone = (Button) findViewById(R.id.using_phone);
        TextView enter_email = (TextView) findViewById(R.id.enter_email);
        Email = (EditText) findViewById(R.id.email);
        remember = (CheckBox) findViewById(R.id.remember);

        loadText();

        // using our fonts everywhere
        enter_email.setTypeface(regular);
        Email.setTypeface(regular);
        remember.setTypeface(regular);

        login_email.setTypeface(regular);
        login_phone.setTypeface(regular);

        // setting on clicker funcs
        login_email.setOnClickListener(new AuthorizationMethods());
        login_phone.setOnClickListener(new AuthorizationMethods());

        digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);

        assert digitsButton != null;

        digitsButton.setCallback(new AuthCallback()
        {
            @Override
            public void success(DigitsSession session, String phoneNumber)
            {
                //TODO: associate the session userID with your user model
                Toast.makeText(getApplicationContext(), "Authentication successful for "
                        + phoneNumber, Toast.LENGTH_LONG).show();

                if (phoneNumber != null)
                {
                    intent.putExtra("phone", phoneNumber);
                    intent.putExtra("email", "empty");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void failure(DigitsException exception)
            {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });
    }

    // user data saving
    void saveText()
    {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        if (remember.isChecked())
        {
            ed.putString("user_data", Email.getText().toString());
            ed.putBoolean("to_load", true);
        }
        else
        {
            ed.putBoolean("to_load", false);
            user_data = sPref.getString("user_data", "");
            ed.putString("user_data", user_data);
        }

        ed.apply();
    }

    // user data loading
    void loadText()
    {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        user_data = sPref.getString("user_data", "");

        Boolean to_load = sPref.getBoolean("to_load", false);

        if (to_load)
        {
            remember.setChecked(true);
            Email.setText(user_data);
        }
    }

    class AuthorizationMethods implements View.OnClickListener
    {
        @Override
        public void onClick(final View v)
        {
            switch (v.getId())
            {
                case R.id.using_phone:
                {
                    // later to replace with better solution

                    digitsButton.performClick();
                }
                break;

                case R.id.using_email:
                {
                    // sending data to another one activity
                    intent.putExtra("phone", "empty");
                    intent.putExtra("email", Email.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    saveText();
                    startActivity(intent);
                }
                break;
            }
        }
    }
}
