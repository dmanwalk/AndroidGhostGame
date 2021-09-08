package edu.davenport.cisp340.ghostgame;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static android.content.ContentValues.TAG;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.ghost_theme);
        mp.setLooping(true);
        mp.start();

        Button mPlayButton = (Button) this.findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mp.setLooping(false);
                mp.stop();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                //listen for intent back from options activity
                startActivity(intent);
                Log.d(TAG, "Opening Options Activity");

            }
        });


    }
}