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

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity {

    private RelativeLayout viewGrPlayground; //ViewGroup viewGrSpielwiese;
    private  Pictures suitcase = new Pictures();

    private boolean recalcSize;
    private double dwBigger;
    private boolean gameOn;

    private int xDiffEventPict;
    private int yDiffEventPict;
    private int xCentreOfPicture;
    private int yCentreOfPicture;
    private int xMarginCentrePict;
    private int yMarginCentrePict;

    public List listPicturesOfPlayer = new ArrayList();
    public List listPicturesOfNpc = new ArrayList();

    public List<ImageView> listImageViewObjects = new ArrayList();
    public List<Pictures> listPictureObjects = new ArrayList();
    public List<String> listNamePictures = new ArrayList<>();
    public List<String> listLeftPictures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Intent i = getIntent();

        // Details shown on top
        String stUserName = i.getStringExtra("UserName");
        ((TextView)findViewById(R.id.WelcomeMessage)).setText("Hello " + stUserName);

        // Playground and images referred to objects
        viewGrPlayground = (RelativeLayout) findViewById(R.id.layoutPlayground);

        listImageViewObjects.add((ImageView) findViewById(R.id.imgHat)); // TODO: Methode schreiben
        listImageViewObjects.add((ImageView) findViewById(R.id.imgTeddy));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgNightTable));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgUmbrella));

        // Set name for pictures
        String namePicture = "";
        for (ImageView imgV : listImageViewObjects) {
            namePicture = imgV.toString();
            namePicture = namePicture.substring(namePicture.indexOf("img") + 3);
            namePicture = namePicture.substring(0, namePicture.indexOf("}"));
            listNamePictures.add(namePicture);
            listLeftPictures.add(namePicture);
        }

        // Suitcase-object
        ImageView imgViewSuitcasePlay = (ImageView)findViewById(R.id.imgSuitcasePlay);
        RelativeLayout.LayoutParams paramsSuitcase = (RelativeLayout.LayoutParams)imgViewSuitcasePlay.getLayoutParams();

        suitcase.setxStart(paramsSuitcase.leftMargin);
        suitcase.setyStart(paramsSuitcase.topMargin);
        suitcase.setxWidth(paramsSuitcase.width);
        suitcase.setyHeight(paramsSuitcase.height);
        suitcase.setxEnd((int)((suitcase.getxStart()+suitcase.getxWidth())*0.9));
        suitcase.setyEnd((int)((suitcase.getyStart()+suitcase.getyHeight())*0.9));

        // For every Image-object a picture-object with origin coordinates and size
        for(int j = 0; j < listImageViewObjects.size(); j++){
            listPictureObjects.add(new Pictures());
            RelativeLayout.LayoutParams paramsForEveryPicture = (RelativeLayout.LayoutParams)listImageViewObjects.get(j).getLayoutParams();
            listPictureObjects.get(j).setxOrigin(paramsForEveryPicture.leftMargin);
            listPictureObjects.get(j).setyOrigin(paramsForEveryPicture.topMargin);
            listPictureObjects.get(j).setxWidth(paramsForEveryPicture.width);
            listPictureObjects.get(j).setyHeight(paramsForEveryPicture.height);
        }

        // TODO: zuruecksetzten auf Original-Ort und Original-Groesse - Methode, wenn Spieler 2

        // Variables: picture when clicked bigger
        recalcSize = true;
        dwBigger = 1.2;

        gameOn = true;

        // Set on Touch Listener for every object with name
        /*
        for(int j = 0; j < listImageViewObjects.size(); j++){

            listImageViewObjects.get(j).setOnTouchListener(movePicture(listNamePictures.get(j)));
        }
        */

        gamePlay();
    }

    public void gamePlay(){
        if (gameOn){
            playerGameplay();
        }
        else {
            ((TextView)findViewById(R.id.WelcomeMessage)).setText("Else Zweig");
            // Next round
            npcGameplay();
        }
    }

    public void playerGameplay(){
        // Clear list before begin
        listPicturesOfPlayer.clear();

        // Set on Touch Listener for every object with name
        for(int i = 0; i < listImageViewObjects.size(); i++){
            listImageViewObjects.get(i).setOnTouchListener(movePicture(listNamePictures.get(i)));
        }

        ((TextView)findViewById(R.id.WelcomeMessage)).setText("Player1");

    }

    public void npcGameplay(){
        // Clear list before begin
        listPicturesOfNpc.clear();

        ((TextView)findViewById(R.id.WelcomeMessage)).setText("NPC");

        //Test TODO: Random einfuegen

        String testAuswahlNpc = listLeftPictures.get(0);
        ((TextView)findViewById(R.id.ausgabe)).setText(listNamePictures.get(0));

        for (int i=0; i < listNamePictures.size(); i++){
            if (listNamePictures.get(i) == testAuswahlNpc) {
                listImageViewObjects.get(i).bringToFront();
                listImageViewObjects.get(i).setColorFilter(Color.argb(180, 255, 146, 146));
                break;
            }
        }

        // Next round
        /*if (!(listLeftPictures.isEmpty())){
            playerGameplay();
        }

         */
    }

    public void resetAllPictures(View v){
        for(int i = 0; i < listImageViewObjects.size(); i++) {
            RelativeLayout.LayoutParams paramsForEveryPicture = (RelativeLayout.LayoutParams)listImageViewObjects.get(i).getLayoutParams();
            paramsForEveryPicture.leftMargin = listPictureObjects.get(i).getxOrigin();
            paramsForEveryPicture.topMargin = listPictureObjects.get(i).getyOrigin();
            paramsForEveryPicture.width = listPictureObjects.get(i).getxWidth();
            paramsForEveryPicture.height = listPictureObjects.get(i).getyHeight();
            listImageViewObjects.get(i).setLayoutParams(paramsForEveryPicture);
            listImageViewObjects.get(i).setColorFilter(Color.argb(0, 0, 0, 0));
            listImageViewObjects.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void checkPickedPicture(String removedPicture){
        listLeftPictures.remove(removedPicture);
        String strAusgabe = "";

        for (int i = 0; i< listLeftPictures.size(); i++){
            strAusgabe += listLeftPictures.get(i);
            strAusgabe += ", ";
        }

        ((TextView)findViewById(R.id.ausgabe)).setText(strAusgabe);

        // Set on touch listener switched off
        for(int i = 0; i < listImageViewObjects.size(); i++){
            listImageViewObjects.get(i).setEnabled(false);
        }
        gameOn = false; // TODO: noetig?
        npcGameplay();
    }

    public void resetListLeftPictures(){
        listLeftPictures.clear();
        for (int i=0; i<listNamePictures.size();i++){
            listLeftPictures.add(listNamePictures.get(i));
        }
    }

    private OnTouchListener movePicture(String pictureName){
        return new OnTouchListener(){
            @SuppressLint("ClickableViewAccessibility") //Indicates that Lint should ignore the specified warnings for the annotated element.
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int nXevent = (int) event.getRawX();
                final int nYevent = (int) event.getRawY();

                ImageView imgView = (ImageView) v;

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        // Difference between event and picture
                        RelativeLayout.LayoutParams paramsPicture = (RelativeLayout.LayoutParams)v.getLayoutParams();
                        xDiffEventPict = nXevent - paramsPicture.leftMargin;
                        yDiffEventPict = nYevent - paramsPicture.topMargin;

                        // Centre of picture
                        xCentreOfPicture = (int)(paramsPicture.width/2);
                        yCentreOfPicture = (int)(paramsPicture.height/2);

                        // Highlighting picture
                        imgView.bringToFront();
                        imgView.setColorFilter(Color.argb(40, 255, 255, 0));
                        if (recalcSize){
                            paramsPicture.height = (int)(paramsPicture.height* dwBigger);
                            paramsPicture.width = (int)(paramsPicture.width* dwBigger);
                            v.setLayoutParams(paramsPicture);
                            recalcSize = false;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                                v.getLayoutParams();
                        layoutParams.leftMargin = nXevent - xDiffEventPict;
                        layoutParams.topMargin = nYevent - yDiffEventPict;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        v.setLayoutParams(layoutParams);
                        break;

                    case MotionEvent.ACTION_UP:
                        paramsPicture = (RelativeLayout.LayoutParams)v.getLayoutParams();

                        // Check if picture within suitcase
                        xMarginCentrePict = paramsPicture.leftMargin + xCentreOfPicture;
                        yMarginCentrePict = paramsPicture.topMargin + yCentreOfPicture;

                        if (((suitcase.getxStart() < xMarginCentrePict) && (xMarginCentrePict < suitcase.getxEnd()))&&
                                ((suitcase.getyStart() < yMarginCentrePict) && (yMarginCentrePict < suitcase.getyEnd()))){
                            v.setVisibility(View.INVISIBLE); //GONE

                            listPicturesOfPlayer.add(pictureName);
                            String strAusgabe = "";

                            for (int i = 0; i< listPicturesOfPlayer.size(); i++){
                                strAusgabe += listPicturesOfPlayer.get(i);
                                strAusgabe += "\n";
                            }

                            //((TextView)findViewById(R.id.ausgabe)).setText(strAusgabe); // Kontrolle
                            checkPickedPicture(pictureName);
                            break;
                        }

                        // Reset picture before highlighting
                        imgView.setColorFilter(Color.argb(0, 0, 0, 0));
                        if (!recalcSize){
                            paramsPicture.height = (int)(paramsPicture.height/ dwBigger);
                            paramsPicture.width = (int)(paramsPicture.width/ dwBigger);
                            v.setLayoutParams(paramsPicture);
                            recalcSize = true;
                        }
                }
                viewGrPlayground.invalidate();
                return true;
            }
        };


        //protected void onVisibilityChanged (View changedView, int visibility)
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