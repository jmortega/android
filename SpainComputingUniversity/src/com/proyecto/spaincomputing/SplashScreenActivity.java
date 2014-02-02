package com.proyecto.spaincomputing;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

/**
 * Actividad que muestra splash screen
 */

public class SplashScreenActivity extends Activity 
{

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 3000;
    
    protected void onCreate(Bundle savedInstanceState) {
        
        
        super.onCreate(savedInstanceState);
 
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.splash);
        
        //another way to do it
        /*Thread splashThread = new Thread() {
        
            @Override
            public void run() {
            
                try {
                    Thread.sleep(SPLASH_SCREEN_DELAY);
                    handler_Splash.sendEmptyMessage(0);
                    
                } catch (InterruptedException e) {                  
                    e.printStackTrace();
                }
            }
        };
        
        splashThread.start();*/
        
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
 
                // Start the next activity
                Intent mainIntent = new Intent().setClass(SplashScreenActivity.this, PrincipalActivity.class);
                startActivity(mainIntent);
 
                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };
 
        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
        
    }
    
    /* Handler to show the new friend position */
    @SuppressLint("HandlerLeak")
	private Handler handler_Splash = new Handler() {
            @Override
            public void handleMessage(Message msg) 
            {
                myFinish();
            }
    };
    
    private void myFinish()
    {
        
        Intent i = new Intent(this,PrincipalActivity.class); 
        startActivity(i);
        this.finish();
        
    }
        
}

