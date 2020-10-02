package com.laira.hias;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.laira.hias.classes.global;
import com.laira.hias.classes.hias;
import com.laira.hias.classes.speech;

public class LoginActivity extends AppCompatActivity {

    private Application application;
    private Context context;
    private View view;
    private speech speech;

    EditText serverText;
    EditText userText;
    EditText passwordText;
    Button loginButton;

    ProgressBar pb;

    String serverInput;
    String userInput;
    String passInput;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        application = getApplication();
        context = getApplicationContext();
        view = getWindow().getDecorView().getRootView();

        speech = new speech(application, this, view, context);
        speech.loadSpeechSynthesis(getString(R.string.welcome));

        serverText = findViewById(R.id.server);
        userText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        pb = findViewById(R.id.loading);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pb.setVisibility(View.VISIBLE);

                if(checkForm() != false)
                {
                    global.setAPB(userInput);
                    global.setAPV(passInput);
                    hias hiasCall = new hias(application, context, view, speech.tts, pb);
                    hiasCall.loginCall(view);
                }

            }
        });
    }

    private boolean checkForm()
    {
        serverInput = serverText.getText().toString();
        global.setServer(serverInput);
        if(serverInput.equals("")){
            pb.setVisibility(View.GONE);
            speech.ConvertTextToSpeech(getString(R.string.invalid_server), speech.tts);
            Toast.makeText(context,getString(R.string.invalid_server),Toast.LENGTH_SHORT).show();
            return false;
        }

        userInput = userText.getText().toString();
        if(userInput.equals("")){
            pb.setVisibility(View.GONE);
            speech.ConvertTextToSpeech(getString(R.string.invalid_username), speech.tts);
            Toast.makeText(context,getString(R.string.invalid_username),Toast.LENGTH_SHORT).show();
            return false;
        }

        passInput = passwordText.getText().toString();
        if(passInput.equals("")){
            pb.setVisibility(View.GONE);
            speech.ConvertTextToSpeech(getString(R.string.invalid_password), speech.tts);
            Toast.makeText(context,getString(R.string.invalid_password),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
