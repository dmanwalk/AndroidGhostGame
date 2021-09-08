package edu.davenport.cisp340.ghostgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import java.util.Random;
public class CharacterSprite {
    private Random randomHeight = new Random();
    private Random randomWidth = new Random();
    private Bitmap image;
    private Bitmap image1;
    private int x,y;
    private int xVelocity = 9;
    private  int yVelocity = 6;
    private  int hitRegistered;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    //constrcutor
    public CharacterSprite(Bitmap bmp) {
        image = bmp;
        image1 = bmp;
        x= randomWidth.nextInt(screenWidth-200);
        y= randomHeight.nextInt(screenHeight-200);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);

    }

    public void update() {
        x+=xVelocity;
        y+=yVelocity;
        if ((x>screenWidth - image.getWidth()) || (x<0)){
            xVelocity = xVelocity*-1;
        }
        if ((y> screenHeight - image.getHeight()) || (y<0)){
            yVelocity = yVelocity*-1;
        }

    }

    public int[] getCoordinates(){
        int ghostH = image.getHeight();
        int ghostW = image.getWidth();
        //System.out.println(x);
        //System.out.println();
        //System.out.println(y);
        int[] coordinates = new int[4];
        coordinates[0] = x;
        coordinates[1] = x + ghostW;
        coordinates[2] = y;
        coordinates[3] = y + ghostH;
        return coordinates;
    }

    public void setNewLocation(int x,int y){
        this.x = x;
        this.y = y;
    }

    public void hitRegistered(Bitmap bmp){
        this.image = bmp;
        int x = 0;
        while (x<20){
            x++;
        }
        //this.image = image1;
    }
}

