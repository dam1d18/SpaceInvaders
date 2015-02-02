package com.dam.spaceinvaders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener, SensorEventListener {

    public Vista view;
    public static boolean run, pause;
    public Creditos creditos;
    private SensorManager mSensorManager;
    float[] inR = new float[16];
    float[] I = new float[16];
    float[] gravity = new float[3];
    float[] geomag = new float[3];
    float[] orientVals = new float[3];
    public static double azimuth = 0;
    public static double pitch = 0;
    public static double roll = 0;
    public static MediaPlayer mp;
    public static String nameUser;
    public static Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/Lancester-Regular DB.ttf");
        int[] idbuttons = new int[]{R.id.button1, R.id.button2, R.id.button3, R.id.button4};
        for (int i = 0; i < idbuttons.length; i++) {
            Button b = (Button) findViewById(idbuttons[i]);
            b.setTypeface(typeFace);
            b.setOnClickListener(this);
        }
        pause = false;
        nameUser = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        run = true;
        int n = (int) (Math.random() * 3);
        //Toast.makeText(this, "N => " + n, Toast.LENGTH_SHORT).show();
        mp = MediaPlayer.create(this, chooseSong(n));
        mp.setLooping(true);
        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
        mp.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.stop();
    }

    @Override
    protected void onDestroy() {
        super.onResume();
        run = false;
        pause = true;
        mSensorManager.unregisterListener(this);
        //Esto limpia la memoria de la aplicación para evitar el memory leek de los bitmap
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public int chooseSong(int n) {
        int song;
        switch (n) {
            case 0:
                song = R.raw.imperialbso;
                break;
            case 1:
                song = R.raw.requiemforadream;
                break;
            default:
                song = R.raw.duelstawars;
                break;
        }
        return song;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        pause = true;
        callDialog(this);
    }

    /**
     * Hace aparecer un dialogo que pregunta al usuario si desea salir o no de la app.
     *
     * @param context - Recibe el contexto de la actividad.
     */
    public void callDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        TextView tv = (TextView) dialog.findViewById(R.id.textView);
        tv.setTypeface(typeFace);
        Button buttonDialogYes = (Button) dialog.findViewById(R.id.button4);
        buttonDialogYes.setTypeface(typeFace);
        buttonDialogYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        Button buttonDialogNo = (Button) dialog.findViewById(R.id.button5);
        buttonDialogNo.setTypeface(typeFace);
        buttonDialogNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pause = false;
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Hace aparecer un dialogo con un input para que el usuario introduzca su nombre.
     *
     * @param context - Recibe el contexto de la actividad.
     */
    public void callDialogName(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogname);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        TextView tv = (TextView) dialog.findViewById(R.id.textView);
        tv.setTypeface(typeFace);
        final EditText et = (EditText) dialog.findViewById(R.id.editTextNombre);
        et.post(new Runnable() {
            public void run() {
                et.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(et, 0);
            }
        });
        et.setTypeface(typeFace);
        Button buttonDialogYes = (Button) dialog.findViewById(R.id.buttonaceptar);
        buttonDialogYes.setTypeface(typeFace);
        buttonDialogYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText et = (EditText) dialog.findViewById(R.id.editTextNombre);
                nameUser = et.getText().toString();
                if (!nameUser.equals("")) {
                    if (nameUser.length() < 20) {
                        pause = false;
                        mSensorManager.registerListener((SensorEventListener) context, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                                SensorManager.SENSOR_DELAY_UI);
                        mSensorManager.registerListener((SensorEventListener) context, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                                SensorManager.SENSOR_DELAY_UI);
                        view = new Vista(context);
                        setContentView(view);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(context, "El nombre debe ser inferior a 20 carácteres", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Por favor no deje en blanco el campo", Toast.LENGTH_LONG).show();
                }
            }
        });
        Button buttonDialogNo = (Button) dialog.findViewById(R.id.buttoncancel);
        buttonDialogNo.setTypeface(typeFace);
        buttonDialogNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            //Iniciar
            case R.id.button1:
                callDialogName(this);
                break;
            //Puntuaciones
            case R.id.button2:
                intent = new Intent(this, ActivityPuntuaciones.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            //Créditos
            case R.id.button3:
                intent = new Intent(this, ActivityCreditos.class);
                startActivity(intent);
                break;
            //Salir
            case R.id.button4:
                callDialog(this);
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // If the sensor data is unreliable return
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;
        // Gets the value of the sensor that has been changed
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                gravity = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomag = event.values.clone();
                break;
        }
        // If gravity and geomag have values then find rotation matrix
        if (gravity != null && geomag != null) {
            // checks that the rotation matrix is found
            boolean success = SensorManager.getRotationMatrix(inR, I,
                    gravity, geomag);
            if (success && view.nave != null) {
                SensorManager.getOrientation(inR, orientVals);
                azimuth = Math.toDegrees(orientVals[0]);
                pitch = Math.toDegrees(orientVals[1]);
                roll = Math.toDegrees(orientVals[2]);
                if (roll < -10) {
                    view.nave.mvright = false;
                    view.nave.mvleft = true;
                } else if (roll > 10) {
                    view.nave.mvleft = false;
                    view.nave.mvright = true;
                } else {
                    view.nave.mvleft = false;
                    view.nave.mvright = false;
                }
                if (pitch < -30) {
                    view.nave.mvup = false;
                    view.nave.mvdown = true;
                } else if (pitch > -5) {
                    view.nave.mvdown = false;
                    view.nave.mvup = true;
                } else {
                    view.nave.mvdown = false;
                    view.nave.mvup = false;
                }
                //Toast.makeText(getApplicationContext(), azimuth + "|" + pitch + "|" + roll, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Su dispositivo no tiene acelerometro", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
