package com.example.koffer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class ShowRankingActivity extends AppCompatActivity implements RequestDeleteRanking.RequestDeleteRankingListener{

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

    public void clearTable(View v){
        RequestDeleteRanking requestDeleteRanking = new RequestDeleteRanking();
        requestDeleteRanking.show(getSupportFragmentManager(), "example dialog"); // TODO: anpassen
    }

    @Override
    public void applyDelete() {
        SQLdb dbHelper = new SQLdb(ShowRankingActivity.this);
        dbHelper.deleteAll();

        List<String> listeRanking = dbHelper.getRanking();

        String ausgabe = "";
        for (String zeile : listeRanking) {
            ausgabe += zeile + "\n";
        }

        ((TextView)findViewById(R.id.textViewRanking)).setText("\n" + ausgabe);
    }
}