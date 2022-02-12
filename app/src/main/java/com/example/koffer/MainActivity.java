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

    public List userListe = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userListe.add("leer");
    }

    public void startGame(View v){
        // Check max. 20 Zeichen? + wenn leer -> "Anonym"
        String stUsername = ((EditText)findViewById(R.id.InputUsername)).getText().toString();
        int nUsernameLaenge = stUsername.length();

        if (nUsernameLaenge > 20){
            Toast.makeText(this,"Please enter a Username of maximum 20 signs.",Toast.LENGTH_LONG).show();
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
        /*
        ((TextView)findViewById(R.id.textView3)).setText((String)userListe.get(0));
        */

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