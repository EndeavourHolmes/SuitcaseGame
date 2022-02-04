package com.example.koffer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class PlayActivity extends AppCompatActivity {

    private RelativeLayout viewGrSpielwiese; //ViewGroup viewGrSpielwiese;
    private ImageView bildHut;
    private ImageView bildSchirm;
    private ImageView bildBall;
    private ImageView bildBrille;

    private boolean groesse;
    private double groesser;

    private int xKoffer;
    private int yKoffer;
    private int hoeheKoffer;
    private int breiteKoffer;
    private int xBis;
    private int yBis;

    private int xWert;
    private int yWert;
    private int xMittelpunktBild;
    private int yMittelpunktBild;
    private int xZielMittBild;
    private int yZielMittBild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Intent i = getIntent();

        viewGrSpielwiese = (RelativeLayout) findViewById(R.id.layoutSpielwiese);

        bildHut = (ImageView) findViewById(R.id.imgHut);
        bildSchirm = (ImageView) findViewById(R.id.imgSchirm);
        bildBall = (ImageView) findViewById(R.id.imgBall);
        bildBrille = (ImageView) findViewById((R.id.imgBrille));

        bildHut.setOnTouchListener(bewegBild());
        bildSchirm.setOnTouchListener(bewegBild());
        bildBall.setOnTouchListener(bewegBild());
        bildBrille.setOnTouchListener(bewegBild());

        groesse = true;
        groesser = 1.2;

        String stUserName = i.getStringExtra("UserName");
        ((TextView)findViewById(R.id.WelcomeMessage)).setText("Hello " + stUserName);

        ImageView bildKoffer = (ImageView)findViewById(R.id.ImageKofferPlay);
        RelativeLayout.LayoutParams paramsKoffer = (RelativeLayout.LayoutParams)bildKoffer.getLayoutParams();
        xKoffer = paramsKoffer.leftMargin;
        yKoffer = paramsKoffer.topMargin;
        breiteKoffer = paramsKoffer.height;
        hoeheKoffer = paramsKoffer.width;
    }

    private OnTouchListener bewegBild(){
        return new OnTouchListener(){
            @SuppressLint("ClickableViewAccessibility") //Indicates that Lint should ignore the specified warnings for the annotated element.
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                ImageView bild = (ImageView) v;

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams paramsBild = (RelativeLayout.LayoutParams)v.getLayoutParams();
                        xWert = x - paramsBild.leftMargin;
                        yWert = y - paramsBild.topMargin;

                        bild.bringToFront();
                        bild.setColorFilter(Color.argb(40, 255, 255, 0));

                        xMittelpunktBild = paramsBild.width/2;
                        yMittelpunktBild = paramsBild.height/2;

                        if (groesse){
                            paramsBild.height = (int)(paramsBild.height*groesser);
                            paramsBild.width = (int)(paramsBild.width*groesser);
                            v.setLayoutParams(paramsBild);
                            groesse = false;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                                v.getLayoutParams();
                        layoutParams.leftMargin = x - xWert;
                        layoutParams.topMargin = y - yWert;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        v.setLayoutParams(layoutParams);
                        break;

                    case MotionEvent.ACTION_UP:
                        paramsBild = (RelativeLayout.LayoutParams)v.getLayoutParams();

                        xZielMittBild = paramsBild.leftMargin + xMittelpunktBild;
                        yZielMittBild = paramsBild.topMargin + yMittelpunktBild;

                        xBis = xKoffer+breiteKoffer+100;
                        yBis = yKoffer+hoeheKoffer;

                        if (((xKoffer < xZielMittBild) && (xZielMittBild < xBis))&& ((yKoffer < yZielMittBild) && (yZielMittBild < yBis))){
                            paramsBild.height = (int)(paramsBild.height/20);
                            paramsBild.width = (int)(paramsBild.width/20);
                            v.setLayoutParams(paramsBild);
                            v.setVisibility(View.INVISIBLE);

                            ((TextView)findViewById(R.id.ausgabe1)).setText("iD: " + v.getId());
                            ((TextView)findViewById(R.id.ausgabe2)).setText("ImageHutID: " + (findViewById(R.id.imgHut).toString()));
                            break;
                        }

                        bild.setColorFilter(Color.argb(0, 0, 0, 0));
                        if (!groesse){
                            paramsBild.height = (int)(paramsBild.height/groesser);
                            paramsBild.width = (int)(paramsBild.width/groesser);
                            v.setLayoutParams(paramsBild);
                            groesse = true;
                        }
                }
                viewGrSpielwiese.invalidate();
                return true;
            }
        };
    }



    /*
        public void beendeSpiel (View v){
        if (userListe.get(0) == "leer"){
            userListe.set(0,"Nutzername");
        }
        else{
            userListe.set(0,"Nr.2");
        }
        }
        */

}