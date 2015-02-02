package com.dam.spaceinvaders;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Victor on 03/01/2015.
 */


public class Vista extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public static final int puntuacionimpactoenemy = 20;
    public static final int puntuacionimpactometeor = 10;
    public Nave nave;
    public Enemy enemy;
    public ArrayList<Meteors> meteors;
    public int meteorappear = 600, contmeteor = 0;
    public Bitmap background;
    public Paint pincel;
    private int contmovy, posy, heightbar;
    public int puntuacion = 0;
    public int meteoritos = 0;
    public int impactosenemigo = 0;

    /**
     * Constructor de la clase Vista.
     *
     * @param context - Recibe el contexto de la actividad.
     */
    public Vista(Context context) {
        super(context);
        meteors = new ArrayList<>();
        pincel = new Paint();
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        if (!MainActivity.pause) {
            posy = -getHeight();
            Bitmap tmp = BitmapFactory.decodeResource(getResources(), R.drawable.universe);
            background = Bitmap.createScaledBitmap(tmp, getWidth(), getHeight() + 1, false);
            nave = new Nave(0, getHolder(), this);
            enemy = new Enemy(0, this);
            heightbar = getHeight() / 30;
            nave.start();
            new Thread(this).start();
        } else {
            MainActivity.pause = false;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {

    }


    @Override
    public void onDraw(Canvas canvas) {
        try {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            canvas.drawBitmap(background, 0, posy, null);
            canvas.drawBitmap(background, 0, posy + getHeight(), null);

            pincel.setColor(Color.GRAY);
            pincel.setFakeBoldText(true);
            pincel.setTextSize(40);
            pincel.setAntiAlias(true);
            pincel.setTypeface(MainActivity.typeFace);
             /*canvas.drawText(String.valueOf(MainActivity.azimuth), 0, 50, pincel);
             canvas.drawText(String.valueOf(MainActivity.pitch), 0, 100, pincel);
             canvas.drawText(String.valueOf(MainActivity.roll), 0, 150, pincel);*/
            canvas.drawText(String.valueOf(puntuacion), 0, 40, pincel);
            pincel.setColor(nave.checkHealth());
            if (nave.Alive()) {
                canvas.drawRect(0, getHeight() - heightbar, (int) (nave.health / nave.widthHealth), getHeight(), pincel);
                canvas.drawBitmap(nave.bmpship, nave.x, nave.y, pincel);
            } else {
                canvas.drawBitmap(nave.bmpblastship, nave.x, nave.y, pincel);
            }
            enemy.draw(canvas);
            for (int i = 0; i < nave.rockets.size(); i++) {
                try {
                    Nave.Rocket rocket = nave.rockets.get(i);
                    rocket.collision();
                    rocket.y -= rocket.movement;
                    if (!rocket.impacto) {
                        canvas.drawBitmap(nave.bmprocket, rocket.x, rocket.y, pincel);
                    } else {
                        canvas.drawBitmap(nave.bmpblast, rocket.x, rocket.y, pincel);
                        if (rocket.contexplo > 20) {
                            nave.rockets.remove(rocket);
                        }
                    }
                    if (rocket.y < -rocket.alto) {
                        nave.rockets.remove(rocket);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (MainActivity.run) {
            try {
                if (!MainActivity.pause) {
                    if (posy == 0) {
                        posy = -getHeight();
                    }
                    if (contmovy == 2) {
                        contmovy = 0;
                        posy++;
                    }
                    contmovy++;
                    if (meteorappear == contmeteor) {
                        int n = (int) (Math.random() * 3);
                        meteors.add(new Meteors(n, this));
                        contmeteor = (int) (Math.random() * meteorappear);
                    } else {
                        contmeteor++;
                    }

                    Thread.sleep(nave.velocidad);
                } else {
                    Thread.interrupted();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (nave.initpuntuaciones) {
            Intent intent = new Intent(getContext(), ActivityPuntuaciones.class);
            Bundle bundle = new Bundle();
            bundle.putString("nombre", MainActivity.nameUser);
            bundle.putInt("impactosenemigo", impactosenemigo);
            bundle.putInt("meteoritos", meteoritos);
            bundle.putInt("puntuacion", puntuacion);
            intent.putExtras(bundle);
            ((MainActivity) getContext()).startActivityForResult(intent, 0);
        }
    }
}
