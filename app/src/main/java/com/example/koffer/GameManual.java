package com.example.koffer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class GameManual extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_manual);

        ((TextView)findViewById(R.id.textViewGameManual)).setText("" +
                "Step 1: Choose an item an put it by drag and drop into your bag\n\n" +
                "Step 2: The NPC will repeat your item and add a new one, which is shown on the bottom \"NPC-choice:\" \n\n" +
                "Step 3: Repeat putting items in the suitcase in exact the same order and add a new one\n\n" +
                "continues with step 2...\n\n\n" +
                "You win if no items are left to put into your suitcase.\n\n" +
                "You can decide on how many items are displayed in the settings area.");
    }
}
