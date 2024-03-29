package com.example.koffer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    private RelativeLayout viewGrPlayground; //ViewGroup
    private  Pictures suitcase = new Pictures();
    private String stUserName;

    private boolean recalcSize;
    private double dwBigger;
    private int score;
    private boolean continueGameplay;
    public ImageView actualImageView;

    private int xDiffEventPict;
    private int yDiffEventPict;
    private int xCentreOfPicture;
    private int yCentreOfPicture;
    private int xMarginCentrePict;
    private int yMarginCentrePict;

    public List<String> listPicturesOfPlayer = new ArrayList<>();
    public List<String> listPicturesOfNpc = new ArrayList<>();

    public List<ImageView> listImageViewObjects = new ArrayList<>();
    public List<Pictures> listPictureObjects = new ArrayList<>();
    public List<String> listNamePictures = new ArrayList<>();
    public List<String> listLeftPictures = new ArrayList<>();

    // Timer
    private boolean running;
    private Chronometer chronometerTimer;
    private long neededTime;
    private Button btnStopTimer;
    private Button btnStartTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Intent i_userName = getIntent();

        // Details shown on top
        stUserName = i_userName.getStringExtra("UserName");
        ((TextView)findViewById(R.id.Message)).setText("Hello " + stUserName);
        ((TextView)findViewById(R.id.textViewLevel)).setText("Level: " + Pictures.level);

        // Playground and images referred to objects
        viewGrPlayground = (RelativeLayout) findViewById(R.id.layoutPlayground);

        listImageViewObjects.add((ImageView) findViewById(R.id.imgHat)); // TODO: Methode schreiben, anpassen an Level
        listImageViewObjects.add((ImageView) findViewById(R.id.imgTeddy));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgNightTable));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgUmbrella));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgFlower));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgHeart));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgClock));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgToothbrush));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgPaintingBird));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgBook));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgPillow));
        listImageViewObjects.add((ImageView) findViewById(R.id.imgLamp));

        adjustNumberOfImagesToLevel();

        // Set name for pictures
        String namePicture = "";
        for (ImageView imgV : listImageViewObjects) {
            if (imgV.getVisibility()!=View.GONE){
                namePicture = imgV.toString();
                namePicture = namePicture.substring(namePicture.indexOf("img") + 3);
                namePicture = namePicture.substring(0, namePicture.indexOf("}"));
                listNamePictures.add(namePicture);
                listLeftPictures.add(namePicture);
            }
        }

        // Suitcase-object
        ImageView imgViewSuitcasePlay = (ImageView)findViewById(R.id.imgSuitcasePlay);
        RelativeLayout.LayoutParams paramsSuitcase = (RelativeLayout.LayoutParams)imgViewSuitcasePlay.getLayoutParams();

        suitcase.setxStart(paramsSuitcase.leftMargin);
        suitcase.setyStart(paramsSuitcase.topMargin);
        suitcase.setxWidth(paramsSuitcase.width);
        suitcase.setyHeight(paramsSuitcase.height);
        suitcase.setxEnd(suitcase.getxStart()+suitcase.getxWidth());
        suitcase.setyEnd(suitcase.getyStart()+suitcase.getyHeight());

        // For every Image-object a picture-object with origin coordinates and size
        for(int j = 0; j < listImageViewObjects.size(); j++){
            if (listImageViewObjects.get(j).getVisibility()!=View.GONE) {
                listPictureObjects.add(new Pictures());
                RelativeLayout.LayoutParams paramsForEveryPicture = (RelativeLayout.LayoutParams) listImageViewObjects.get(j).getLayoutParams();
                listPictureObjects.get(j).setxOrigin(paramsForEveryPicture.leftMargin);
                listPictureObjects.get(j).setyOrigin(paramsForEveryPicture.topMargin);
                listPictureObjects.get(j).setxWidth(paramsForEveryPicture.width);
                listPictureObjects.get(j).setyHeight(paramsForEveryPicture.height);
            }
        }

        // Variable: picture when clicked bigger
        dwBigger = 1.2;

        // Variables Timer/ Chronometer
        running = false;
        chronometerTimer = findViewById(R.id.chronometerTimer);
        btnStopTimer = findViewById(R.id.btnEndGame);
        btnStartTimer = findViewById(R.id.btnStartGame);
        btnStopTimer.setEnabled(false);
        btnStartTimer.setEnabled(true);
    }

    public void startGame(View v){
        //Start timer and game
        Toast.makeText(this,"Start",Toast.LENGTH_LONG).show();

        neededTime = 0;
        if (!running){
            chronometerTimer.setBase(SystemClock.elapsedRealtime());
            chronometerTimer.start();
            running = true;
            btnStopTimer.setEnabled(true);
            v.setEnabled(false);
        }

        // Clear lists before begin
        listPicturesOfPlayer.clear();
        listPicturesOfNpc.clear();
        resetAllPictures();
        resetListLeftPictures();

        // Reset variables
        score = 0;
        continueGameplay = true;
        recalcSize = true;

        // First round
        // Set on Touch Listener for every object with name
        for(int i = 0; i < listLeftPictures.size(); i++){
            listImageViewObjects.get(i).setOnTouchListener(movePicture(listNamePictures.get(i)));
        }

        playerGameplay();
    }

    // When user stops game manually
    public void endGame(View v){
        endGame();
    }

    // Game stops automatically when an error is made or no pictures are left
    public void endGame(){

        // SetOnTouchlistener switched OFF
        for(int i = 0; i < listImageViewObjects.size(); i++){
            listImageViewObjects.get(i).setEnabled(false);
        }

        // End Timer, save score
        if (running){
            chronometerTimer.stop();
            neededTime = SystemClock.elapsedRealtime() - chronometerTimer.getBase();
            running = false;
            btnStartTimer.setEnabled(true);
            btnStopTimer.setEnabled(false);
        }

        neededTime = neededTime/1000;
        int n_neededTime = (int) neededTime;

        SQLdb dbHelper = new SQLdb(PlayActivity.this);
        boolean successDB = dbHelper.addOne(stUserName, score, n_neededTime);
    }

    public void adjustNumberOfImagesToLevel(){
        switch (Pictures.level){
            case 1:
                for (int i = 5; i < listImageViewObjects.size(); i++){
                    listImageViewObjects.get(i).setVisibility(View.GONE);
                }
                break;
            case 2:
                for (int i = 8; i < listImageViewObjects.size(); i++){
                    listImageViewObjects.get(i).setVisibility(View.GONE);
                }
                break;
            case 3:
                for (int i = 10; i < listImageViewObjects.size(); i++){
                    listImageViewObjects.get(i).setVisibility(View.GONE);
                }
                break;
            case 4:
                break;
        }
    }


    public void playerGameplay(){
        ((TextView)findViewById(R.id.Message)).setText(stUserName);

        // Clear list before begin
        listPicturesOfPlayer.clear();
        resetAllPictures();
        resetListLeftPictures();

        // SetOnTouchlistener switched ON
        for(int i = 0; i < listImageViewObjects.size(); i++){
            listImageViewObjects.get(i).setEnabled(true);
        }
    }

    public void checkPickedPicture(String pickedPicture){
        listLeftPictures.remove(pickedPicture);
        listPicturesOfPlayer.add(pickedPicture);

        String strAusgabe = "Player: ";
        for(String testNPC: listPicturesOfPlayer){
            strAusgabe += testNPC + ", ";
        }
        strAusgabe += "\nleft: ";
        for(String testNPC: listLeftPictures){
            strAusgabe += testNPC + ", ";
        }
        ((TextView)findViewById(R.id.ausgabe)).setText(strAusgabe);

        // Comparison player's pictures with Npc's: Player has less pictures
        if (listPicturesOfPlayer.size()<=listPicturesOfNpc.size()){
            for (int i = 0; i < listPicturesOfPlayer.size(); i++){
                if (listPicturesOfPlayer.get(i) != listPicturesOfNpc.get(i)){
                    continueGameplay = false;
                    ((TextView) findViewById(R.id.Message)).setText("Gameover");
                    endGame();
                    break;
                }
            }
        }
        // Player has more pictures
        else{
            for (int i = 0; i < listPicturesOfNpc.size(); i++){
                if (listPicturesOfPlayer.get(i) != listPicturesOfNpc.get(i)){
                    continueGameplay = false;
                    ((TextView) findViewById(R.id.Message)).setText("Gameover");
                    endGame();
                    break;
                }
            }
        }

        if(continueGameplay){
            if (score<listPicturesOfPlayer.size()){
                score = listPicturesOfPlayer.size();
            }
        }

        if (continueGameplay && (listPicturesOfPlayer.size() == (listPicturesOfNpc.size()+1))){
            // Next round - at least one picture left
            if (0<listLeftPictures.size()){
                npcGameplay();
            }
            // Last Round: Player took last picture and NPC just repeated
            else {
                ((TextView) findViewById(R.id.Message)).setText("End of Game. You win!");
                endGame();
            }
        }

        // Last Round: NPC took last picture and player just repeated
        if (continueGameplay && (listPicturesOfPlayer.size() == listPicturesOfNpc.size())){
            // No pictures left
            if (0==listLeftPictures.size()){
                ((TextView) findViewById(R.id.Message)).setText("End of Game. You win!");
                endGame();
            }
        }
    }

    public void npcGameplay() {
        ((TextView) findViewById(R.id.Message)).setText("NPC");

        // SetOnTouchlistener switched OFF
        for(int i = 0; i < listImageViewObjects.size(); i++){
            listImageViewObjects.get(i).setEnabled(false);
        }

        // Clear list and reset before begin
        listPicturesOfNpc.clear();
        resetAllPictures();
        resetListLeftPictures();

        // Takes pictures of player first, followed by one which is left
        for (int i = 0; i < (listPicturesOfPlayer.size()+1); i++) {
            String choicePictureNpc = "";

            if (i < listPicturesOfPlayer.size()){
                choicePictureNpc = listPicturesOfPlayer.get(i);
            }
            else {
                Random rand = new Random();
                int randomInt = rand.nextInt(listLeftPictures.size());
                choicePictureNpc = listLeftPictures.get(randomInt);
            }

            for (int j = 0; j < listNamePictures.size(); j++) {
                if (listNamePictures.get(j) == choicePictureNpc) {
                    actualImageView = listImageViewObjects.get(j);
                    highlightPicture(actualImageView);
                    listPicturesOfNpc.add(choicePictureNpc);

                    /*
                    String strAusgabe = "NPC: ";
                    for(String testNPC: listPicturesOfNpc){
                        strAusgabe += testNPC + ", ";
                    }
                    */
                    ((TextView)findViewById(R.id.ausgabe)).setText("NPC-choice:" + choicePictureNpc);

                    actualImageView.setVisibility(View.INVISIBLE);

                    /*
                    // TODO: Animation anpassen

                    ObjectAnimator animation = ObjectAnimator.ofFloat(actualImageView, "translationY", 1000f);
                    animation.setDuration(2000);
                    animation.start();

                    actualImageView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            actualImageView.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);

                     */

                    listLeftPictures.remove(choicePictureNpc);
                    break;
                }
            }
        }
        // Next round
        if (0<(listLeftPictures.size()+1)){
            playerGameplay();
        }
        else {
            ((TextView) findViewById(R.id.Message)).setText("End of Game");
            endGame(); // Test
        }
    }

    public void highlightPicture(ImageView imgV){
        imgV.bringToFront();
        imgV.setColorFilter(Color.argb(40, 255, 255, 0));
        imgV.getLayoutParams().height = (int)(imgV.getLayoutParams().height*dwBigger);
        imgV.getLayoutParams().width = (int)(imgV.getLayoutParams().width*dwBigger);
    }

    public void resetListLeftPictures(){
        listLeftPictures.clear();
        for (int i=0; i<listNamePictures.size();i++){
            listLeftPictures.add(listNamePictures.get(i));
        }
    }

    public void resetAllPictures(){
        for(int i = 0; i < listImageViewObjects.size(); i++) {
            if (listImageViewObjects.get(i).getVisibility()!=View.GONE) {
                RelativeLayout.LayoutParams paramsForEveryPicture = (RelativeLayout.LayoutParams) listImageViewObjects.get(i).getLayoutParams();
                paramsForEveryPicture.leftMargin = listPictureObjects.get(i).getxOrigin();
                paramsForEveryPicture.topMargin = listPictureObjects.get(i).getyOrigin();
                paramsForEveryPicture.width = listPictureObjects.get(i).getxWidth();
                paramsForEveryPicture.height = listPictureObjects.get(i).getyHeight();
                listImageViewObjects.get(i).setLayoutParams(paramsForEveryPicture);
                listImageViewObjects.get(i).setColorFilter(Color.argb(0, 0, 0, 0));
                listImageViewObjects.get(i).setVisibility(View.VISIBLE);
            }
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
                        paramsPicture.height = (int)(paramsPicture.height* dwBigger);
                        paramsPicture.width = (int)(paramsPicture.width* dwBigger);
                        v.setLayoutParams(paramsPicture);
                        recalcSize = false;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                                v.getLayoutParams();
                        if (((nXevent - xDiffEventPict) > 0) && ((nXevent - xDiffEventPict + layoutParams.width) < viewGrPlayground.getWidth())){
                            layoutParams.leftMargin = nXevent - xDiffEventPict;
                        }
                        if (((nYevent - yDiffEventPict) > 0) && ((nYevent - yDiffEventPict + layoutParams.height) < viewGrPlayground.getHeight())){
                            layoutParams.topMargin = nYevent - yDiffEventPict;
                        }
                        //layoutParams.rightMargin = 0;
                        //layoutParams.bottomMargin = 0;

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

                            checkPickedPicture(pictureName);
                            break;

                        }

                        // Reset picture
                        imgView.setColorFilter(Color.argb(0, 0, 0, 0));
                        if (!recalcSize){
                            paramsPicture.height = (int)(paramsPicture.height/ dwBigger);
                            paramsPicture.width = (int)(paramsPicture.width/ dwBigger);
                            v.setLayoutParams(paramsPicture);
                            recalcSize = true;
                        }
                        break;
                }
                viewGrPlayground.invalidate();
                return true;
            }
        };
        //protected void onVisibilityChanged (View changedView, int visibility)
    }
}