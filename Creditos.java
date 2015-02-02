package com.dam.spaceinvaders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Victor on 28/01/2015.
 */
public class Creditos extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private String[] str;
    private int posy, fin, posybackground, contmovy;
    private Canvas canvas;
    private Paint pincel;
    private Bitmap background;
    private int textsize;
    private int espaciado;
    private int nTesters;

    /**
     * Constructor de la clase Créditos
     *
     * @param context - Recibe el contexto de la actividad.
     */
    public Creditos(Context context) {
        super(context);
        str = new String[29];
        nTesters = 12;
        for (int i = 0; i < str.length; i++) {
            str[i] = getCreditos(i);
        }
        pincel = new Paint();
        canvas = null;
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Bitmap tmp = BitmapFactory.decodeResource(getResources(), R.drawable.universe);
            background = Bitmap.createScaledBitmap(tmp, getWidth(), getHeight() + 1, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        espaciado = getWidth() / 7;
        posy = getHeight() + espaciado;
        textsize = getWidth() / 13;
        fin = (str.length * espaciado * 2) - espaciado * (nTesters + 1);
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void onDraw(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(background, 0, posybackground, null);
        canvas.drawBitmap(background, 0, posybackground + getHeight(), null);
        pincel.setAntiAlias(true);
        pincel.setTextSize(textsize);
        pincel.setFakeBoldText(true);
        pincel.setTypeface(MainActivity.typeFace);
        pincel.setColor(Color.WHITE);
        pincel.setTextAlign(Paint.Align.CENTER);
        int saltolinea = 0;
        boolean title = true;
        for (int i = 1; i <= str.length; i++) {
            if (title) {
                pincel.setUnderlineText(true);
            } else {
                pincel.setUnderlineText(false);
            }
            canvas.drawText(str[i - 1], getWidth() / 2, posy + ((i - 1) * espaciado) + saltolinea, pincel);
            title = false;
            if (i < 17) {
                if (i % 2 == 0) {
                    saltolinea += espaciado * 2;
                    title = true;
                }
            }
        }
    }

    /**
     * Función que devuelve una cadena en función del número que recibe.
     * Sirve para crear los textos de los créditos.
     *
     * @param i - Rcibe un parámetro i que sirve para obtener la cadena que se desee.
     * @return Devuelve una String con el texto de los créditos.
     */
    public String getCreditos(int i) {
        String str = "";
        switch (i) {
            case 0:
                str = "Director";
                break;
            case 1:
                str = "Víctor Jurado Usón";
                break;
            case 2:
                str = "Productor";
                break;
            case 3:
                str = "Víctor Jurado Usón";
                break;
            case 4:
                str = "Guionista";
                break;
            case 5:
                str = "Víctor Jurado Usón";
                break;
            case 6:
                str = "Diseñador Gráfico";
                break;
            case 7:
                str = "Víctor Jurado Usón";
                break;
            case 8:
                str = "Progamador Jefe";
                break;
            case 9:
                str = "Víctor Jurado Usón";
                break;
            case 10:
                str = "Música";
                break;
            case 11:
                str = "Víctor Jurado Usón";
                break;
            case 12:
                str = "Investigación y Desarrollo";
                break;
            case 13:
                str = "Víctor Jurado Usón";
                break;
            case 14:
                str = "Diseñador de créditos";
                break;
            case 15:
                str = "Víctor Jurado Usón";
                break;
            case 16:
                str = "Beta Testers";
                break;
            case 17:
                str = "Víctor Jurado Usón";
                break;
            case 18:
                str = "Alejandro Peña Florentín";
                break;
            case 19:
                str = "Mariluz Usón Cañadas";
                break;
            case 20:
                str = "José Manuel León Esquina";
                break;
            case 21:
                str = "Carlos Lizaga Anadón";
                break;
            case 22:
                str = "Antonio Patricio Prada Lara";
                break;
            case 23:
                str = "Félix Anadón Díaz";
                break;
            case 24:
                str = "Sergio Murillo Pérez";
                break;
            case 25:
                str = "Cristian Jimenez De Muñana";
                break;
            case 26:
                str = "Samuel Cebrián Laga";
                break;
            case 27:
                str = "Carlos Alberto Pérez Marín";
                break;
            case 28:
                str = "Carlos Cobos Gracia";
                break;
        }
        return str;
    }

    /**
     * Método para llamar a Thread.sleep más cómodamente.
     *
     * @param ms - Recibe los milisegundos que debe detenerse el hilo.
     */
    public void parar(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Creditos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (posy > -fin && ActivityCreditos.run) {
            try {
                canvas = getHolder().lockCanvas(null);
                synchronized (getHolder()) {
                    try {
                        onDraw(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                if (canvas != null)
                    getHolder().unlockCanvasAndPost(canvas);
            }
            if (posybackground == 0) {
                posybackground = -getHeight();
            }
            if (contmovy == 2) {
                contmovy = 0;
                posybackground++;
            }
            contmovy++;
            posy--;
            parar(7);
        }
        ((Activity) getContext()).finish();
    }
}
