package com.laira.hias;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.laira.hias.classes.hias;
import com.laira.hias.classes.speech;

public class GeniSysAiActivity extends Activity {

    private speech speech;

    private Application application;
    private Context context;
    private View view;

    boolean listening = true;

    private FloatingActionButton toggleButton;
    private Button sendButton;
    private EditText uInput;

    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genisysai);

        application = getApplication();
        context = getApplicationContext();
        view = getWindow().getDecorView().getRootView();

        speech = new speech(application, this, view, context);
        speech.loadSpeechSynthesis(getString(R.string.nlu_help));
        speech.loadSpeechRecognition();

        toggleButton = findViewById(R.id.toggleButton1);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(listening){
                    speech.startToListen();
                }else if(!listening){
                    speech.endListening();
                }
            }
        });

        uInput = findViewById(R.id.uInput);
        sendButton = findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            String userInp = uInput.getText().toString();
            hias hiasCall = new hias(application, context, view, speech.tts, pb);
            hiasCall.nluCall(view, userInp);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}