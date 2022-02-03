package com.example.koffer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Intent i = getIntent();
        String stUserName = i.getStringExtra("UserName");
        ((TextView)findViewById(R.id.WelcomeMessage)).setText("Hello " + stUserName);
    }


}