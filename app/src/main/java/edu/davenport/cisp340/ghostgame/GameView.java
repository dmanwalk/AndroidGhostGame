package edu.davenport.cisp340.ghostgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.content.DialogInterface;
import java.io.Console;
import java.util.Random;
import java.util.logging.ConsoleHandler;

import static android.content.ContentValues.TAG;
import static edu.davenport.cisp340.ghostgame.R.drawable.ghost1;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private CharacterSprite characterSprite;
    private int touchedX;
    private int touchedY;
    public int[] spritePostion;
    public int hitTotal;
    public int missTotal;
    public double hitPercentage;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Random randomHeight = new Random();
    private Random randomWidth = new Random();
    Bitmap mFinalbitmap= BitmapFactory.decodeResource(getResources(), R.drawable.background1);
    long startTime = System.currentTimeMillis();
    long elapsedTime = System.currentTimeMillis() - startTime;
    //long elapsedSeconds = elapsedTime / 1000;
    private int timeToLive = (int)(Math.random() * (200 - 1 + 80) + 80);
    int hitRegistered;



    //constructor
    public GameView(Context context) {
        super(context);//calls surfaceview

        //Each time  class is called to make a new object it will create a new surface.
        getHolder().addCallback(this); //By adding Callback, we’re able to intercept events
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        this.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touchedX = (int) event.getX();
                touchedY = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        System.out.println(touchedX);
                        System.out.println(touchedY);
                        if (touchedX >= spritePostion[0] && touchedX <= spritePostion[1]){
                            if (touchedY >= spritePostion[2] && touchedY <= spritePostion[3]){
                                final MediaPlayer mp = MediaPlayer.create(getContext(),R.raw.hurt);
                                mp.start();
                                updateHit();
                                characterSprite.setNewLocation(randomWidth.nextInt(screenWidth-200),randomHeight.nextInt(screenHeight-200));
                                hitRegistered =1;

                            }
                        }
                        else{
                            final MediaPlayer mp = MediaPlayer.create(getContext(),R.raw.whoosh);
                            mp.start();
                            updateMiss();
                        }

                        break;
                }
                return true;
            }
        });

    }



    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread.setRunning(true);
        thread.start();
        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(), ghost1));
        //MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.ghostTheme);



    }//end surface created

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }//end surface changed

    @Override
    public  void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while (retry){
            try {
                thread.setRunning(false);
                thread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }
    }//end surafce destroyed

    public void update(){
        characterSprite.update();
        spritePostion = characterSprite.getCoordinates();

        elapsedTime++;


        //System.out.println(hitRegistered);
        if (hitRegistered == 1){
            characterSprite.hitRegistered(BitmapFactory.decodeResource(getResources(),R.drawable.ghost2));

        }
        if (hitRegistered == 1 && elapsedTime-60 > 110){
            characterSprite.hitRegistered(BitmapFactory.decodeResource(getResources(),R.drawable.ghost1));
            hitRegistered = 0;
            elapsedTime = 0;
        }
        //System.out.println("Elapsed seconds"+elapsedTime+"\n");
        //System.out.println("time to live"+timeToLive);
        if (elapsedTime > (long)timeToLive){
            updateMiss();
            characterSprite.setNewLocation(randomWidth.nextInt(screenWidth-200),randomHeight.nextInt(screenHeight-200));
            timeToLive = (int)(Math.random() * (200 - 1 + 80) + 80);
            elapsedTime = 0;
        }

    }//end update

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if (canvas != null){
            //canvas.drawBitmap;
            canvas.drawBitmap(mFinalbitmap, 0, 0, null);
            characterSprite.draw(canvas);
        }//end if

    }//end draw

    private void updateMiss(){
        missTotal++;
        if (missTotal+hitTotal >= 10){
            //System.out.println("End of game");
            try {
                System.out.println("hitTotal:"+hitTotal+"\n"+"MissTotal"+missTotal);
                hitPercentage = 100.00*( (double)hitTotal / ((double)hitTotal+ (double)missTotal));
                MainThread.sleep(10);
                if (hitTotal ==10){
                    final MediaPlayer mp = MediaPlayer.create(getContext(),R.raw.first_best);
                    mp.start();

                }
                else if(hitTotal >=6 && hitTotal <10){
                    final MediaPlayer mp = MediaPlayer.create(getContext(),R.raw.second_best);
                    mp.start();

                }
                else{
                    final MediaPlayer mp = MediaPlayer.create(getContext(),R.raw.third_best);
                    mp.start();

                }
                showPopup();
                MainThread.sleep(10);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    private void  updateHit(){
        hitTotal++;

        if (hitTotal+missTotal >=10){
            //System.out.println("End of game");
            try {
                System.out.println("hitTotal:"+hitTotal+"\n"+"MissTotal"+missTotal);
                hitPercentage = 100.00*( (double)hitTotal / ((double)hitTotal+ (double)missTotal));
                MainThread.sleep(10);
                if (hitTotal ==10){
                    final MediaPlayer mp = MediaPlayer.create(getContext(),R.raw.first_best);
                    mp.start();

                }
                else if(hitTotal >=6 && hitTotal <10){
                    final MediaPlayer mp = MediaPlayer.create(getContext(),R.raw.second_best);
                    mp.start();

                }
                else{
                    final MediaPlayer mp = MediaPlayer.create(getContext(),R.raw.third_best);
                    mp.start();

                }
                showPopup();
                MainThread.sleep(10);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void showPopup(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Set the message with string res
        builder.setMessage("Hits:"+hitTotal+"\n"+"Misses:"+missTotal+"\n"+"Hit Percentage:"+"%"+(hitPercentage));

        // Set Alert Title with resource string
        builder.setTitle("Game Over");
        //set positive button with name
        builder.setPositiveButton((R.string.retryButton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hitTotal = 0;
                missTotal = 0;

                dialog.cancel();
            }
        });
        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder.setNegativeButton((R.string.quitButton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                System.exit(0);
                            }
                        });


        //put together all pieces of the dialog made with the builder.
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }


}

