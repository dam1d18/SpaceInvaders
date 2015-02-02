package com.dam.spaceinvaders;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by Victor on 29/01/2015.
 */
public class ActivityCreditos extends Activity {

    public static boolean run;
    public MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Creditos creditos = new Creditos(this);
        setContentView(creditos);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mp = MediaPlayer.create(this, R.raw.starwarsbso);
        mp.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        run = true;
    }

    @Override
    protected void onDestroy() {
        super.onResume();
        mp.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        run = false;
    }
}
