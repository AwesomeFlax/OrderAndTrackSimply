package com.example.denis.orderandtracksimply;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
    //переменные для доступа по всему классу
    DigitsAuthButton digitsButton;
    Intent intent;

    //данные с экрана
    EditText Email;
    ImageButton login_email;
    ImageButton login_phone;
    CheckBox remember;

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

        //инициализация шрифтов
        Typeface bold = Typeface.createFromAsset(getAssets(), getString(R.string.bold_font));
        Typeface regular = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));
        Typeface medium = Typeface.createFromAsset(getAssets(), getString(R.string.regular_font));

        intent = new Intent(this, MainZone.class);

        //хватаем элементы с экрана
        Email = (EditText) findViewById(R.id.email);
        login_email = (ImageButton) findViewById(R.id.using_email);
        login_phone = (ImageButton) findViewById(R.id.using_phone);
        TextView email_text = (TextView) findViewById(R.id.email_text);
        TextView phone_text = (TextView) findViewById(R.id.phone_text);
        TextView enter_email = (TextView) findViewById(R.id.enter_email);
        Email = (EditText) findViewById(R.id.email);
        remember = (CheckBox) findViewById(R.id.remember);

        //применяем шрифт к тексту
        email_text.setTypeface(regular);
        phone_text.setTypeface(regular);
        enter_email.setTypeface(regular);
        Email.setTypeface(regular);
        remember.setTypeface(regular);

        //обработчики нажатий кнопок
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

    class AuthorizationMethods implements View.OnClickListener
    {
        @Override
        public void onClick(final View v)
        {
            switch (v.getId())
            {
                case R.id.using_phone:
                {
                    //великолепные решения (илиточка)
                    digitsButton.performClick();
                }
                break;

                case R.id.using_email:
                {
                    //пока так, будет просто переход с данными

                    intent.putExtra("phone", "empty");
                    intent.putExtra("email", Email.getText().toString());
                    startActivity(intent);
                }
                break;
            }
        }
    }
}
