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

    private boolean bSize;
    private double dwBigger;

    private int nXdiffEventPict;
    private int nYdiffEventPict;
    private int nXcentreOfPicture;
    private int nYcentreOfPicture;
    private int nXmarginCentrePict;
    private int nYmarginCentrePict;

    public List listPlayer1 = new ArrayList();
    public List listeSpieler2 = new ArrayList();

    public List<ImageView> listImageViewObjects = new ArrayList();
    public List<Pictures> listPictureObjects = new ArrayList();

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

        listImageViewObjects.add((ImageView) findViewById(R.id.imgHat));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgTeddy));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgNightTable));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgUmbrella));

        // Set on Touch Listener for every object
        for (ImageView imgV : listImageViewObjects) {
            imgV.setOnTouchListener(movePicture(imgV.toString()));
        }

        // Suitcase-object
        ImageView imgViewSuitcasePlay = (ImageView)findViewById(R.id.imgSuitcasePlay);
        RelativeLayout.LayoutParams paramsSuitcase = (RelativeLayout.LayoutParams)imgViewSuitcasePlay.getLayoutParams();

        suitcase.setnXstart(paramsSuitcase.leftMargin);
        suitcase.setnYstart(paramsSuitcase.topMargin);
        suitcase.setnXwidth(paramsSuitcase.width);
        suitcase.setnYheight(paramsSuitcase.height);
        suitcase.setnXend((int)((suitcase.getnXstart()+suitcase.getnXwidth())*0.9));
        suitcase.setnYend((int)((suitcase.getnYstart()+suitcase.getnYheight())*0.9));


        // For every Image-object a picture-object with origin coordinates
        int nCountPictureObjects = 0;

        for (ImageView imgV : listImageViewObjects) {
            listPictureObjects.add(new Pictures());
            RelativeLayout.LayoutParams paramsForEveryPicture = (RelativeLayout.LayoutParams)imgV.getLayoutParams();
            listPictureObjects.get(nCountPictureObjects).setnXorigin(paramsForEveryPicture.leftMargin);
            listPictureObjects.get(nCountPictureObjects).setnYorigin(paramsForEveryPicture.topMargin);
            nCountPictureObjects++;
        }

        //Test
        /*
        ((TextView)findViewById(R.id.ausgabe)).setText("Hut X:" + listPictureObjects.get(0).getnXorigin() +
               "Hut Y: " + listPictureObjects.get(0).getnYorigin() + "\n" +
                "Teddy X: " + listPictureObjects.get(1).getnXorigin() + "\n" +
                "Teddy Y: "+ listPictureObjects.get(1).getnYorigin());

        Pictures test = new Pictures();
        listPictureObjects.add(test);
        listPictureObjects.get(0).setnXorigin(2);
        test.setnXorigin(2);

         */

        // Variables: picture when clicked bigger
        bSize = true;
        dwBigger = 1.2;

    }

    private OnTouchListener movePicture(String pictureIdInformation){
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
                        nXdiffEventPict = nXevent - paramsPicture.leftMargin;
                        nYdiffEventPict = nYevent - paramsPicture.topMargin;

                        // Centre of picture
                        nXcentreOfPicture = (int)(paramsPicture.width/2);
                        nYcentreOfPicture = (int)(paramsPicture.height/2);

                        // Highlighting picture
                        imgView.bringToFront();
                        imgView.setColorFilter(Color.argb(40, 255, 255, 0));
                        if (bSize){
                            paramsPicture.height = (int)(paramsPicture.height* dwBigger);
                            paramsPicture.width = (int)(paramsPicture.width* dwBigger);
                            v.setLayoutParams(paramsPicture);
                            bSize = false;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                                v.getLayoutParams();
                        layoutParams.leftMargin = nXevent - nXdiffEventPict;
                        layoutParams.topMargin = nYevent - nYdiffEventPict;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        v.setLayoutParams(layoutParams);
                        break;

                    case MotionEvent.ACTION_UP:
                        paramsPicture = (RelativeLayout.LayoutParams)v.getLayoutParams();

                        // Check if picture within suitcase
                        nXmarginCentrePict = paramsPicture.leftMargin + nXcentreOfPicture;
                        nYmarginCentrePict = paramsPicture.topMargin + nYcentreOfPicture;

                        if (((suitcase.getnXstart() < nXmarginCentrePict) && (nXmarginCentrePict < suitcase.getnXend()))&&
                                ((suitcase.getnYstart() < nYmarginCentrePict) && (nYmarginCentrePict < suitcase.getnYend()))){
                            v.setVisibility(View.INVISIBLE); //GONE

                            listPlayer1.add(pictureIdInformation);
                            String strAusgabe = "";

                            for (int i = 0; i< listPlayer1.size(); i++){
                                strAusgabe += listPlayer1.get(i);
                                strAusgabe += "\n";
                            }

                            ((TextView)findViewById(R.id.ausgabe)).setText(strAusgabe); // Kontrolle //TODO: imgObjektName rausfiltern --> Liste
                            break;
                        }

                        // Reset picture before highlighting
                        imgView.setColorFilter(Color.argb(0, 0, 0, 0));
                        if (!bSize){
                            paramsPicture.height = (int)(paramsPicture.height/ dwBigger);
                            paramsPicture.width = (int)(paramsPicture.width/ dwBigger);
                            v.setLayoutParams(paramsPicture);
                            bSize = true;
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