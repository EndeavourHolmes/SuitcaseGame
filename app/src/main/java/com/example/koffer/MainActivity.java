package com.example.koffer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Pictures.level == 0){
            Pictures.level = 2;
        }
    }

    public void startGame(View v){
        // Check max. 20 Zeichen? + wenn leer -> "Anonym"
        String stUsername = ((EditText)findViewById(R.id.InputUsername)).getText().toString();
        int nUsernameLaenge = stUsername.length();

        if (nUsernameLaenge > 20){
            ((TextView) findViewById(R.id.textViewFehlermeldung)).setText("Please enter a Username of maximum 20 signs.");
        }
        else {
            if (nUsernameLaenge == 0){
                stUsername = "Anonym";
            }
            Intent i_Play = new Intent(this, PlayActivity.class);
            i_Play.putExtra("UserName", stUsername);
            startActivity(i_Play);
        }
    }

    public void showRanking(View v){
        Intent i_Ranking = new Intent(this, ShowRankingActivity.class);
        startActivity(i_Ranking);
    }

    public void showGameManual(View v){
        Intent i_GameManual = new Intent(this, GameManual.class);
        startActivity(i_GameManual);
    }

    public void showSettings(View v){
        Intent i_Settings = new Intent(this, Settings.class);
        startActivity(i_Settings);
    }

}