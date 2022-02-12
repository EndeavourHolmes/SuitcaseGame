package com.example.koffer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class ShowRankingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ranking);

        SQLdb dbHelper = new SQLdb(ShowRankingActivity.this);
        List<String> listeRanking = dbHelper.getRanking();

        String ausgabe = "";
        for (String zeile : listeRanking) {
            ausgabe += zeile + "\n";
        }

        ((TextView)findViewById(R.id.textViewRanking)).setText("\n" + ausgabe); // TODO: Anzeige anpassen
    }
}