package com.example.koffer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    private int chosenLevel;
    SeekBar seekbarLevel;
    TextView textViewLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        chosenLevel = 2;
        seekbarLevel = (SeekBar)findViewById((R.id.seekBarLevel));
        textViewLevel = ((TextView)findViewById(R.id.textViewSeekbar));

        seekbarLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int level, boolean b) {

                switch (level){
                    case 1:
                        textViewLevel.setText("Level 1 \n\nChoose between 5 items to put into your suitcase.");
                        chosenLevel = level;
                        break;
                    case 2:
                        textViewLevel.setText("Level 2 \n\nChoose between 10 items to put into your suitcase.");
                        chosenLevel = level;
                        break;
                    case 3:
                        textViewLevel.setText("Level 3 \n\nChoose between 15 items to put into your suitcase.");
                        chosenLevel = level;
                        break;
                    case 4:
                        textViewLevel.setText("Level 4 \n\nChoose between 20 items to put into your suitcase.");
                        chosenLevel = level;
                        break;
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void saveLevel(View v){
        Pictures.level = chosenLevel;
        Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();
    }

}
