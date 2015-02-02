package com.dam.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Victor on 26/01/2015.
 */
public class Meteors {
    private Vista view;
    public int x, y, diametro, movimiento, minsize;
    public boolean impacto;
    public Canvas canvas;
    public Bitmap bmpmeteor, bttemp, bmpblast;
    public int id;
    public int contexplo = 0, size;
    public int impactosdectruccionmax, impactosdestrucccion;

    /**
     * Constructor de la clase Meteors
     *
     * @param id Recibe un id del meteorito que sirve para cargar distintos tipos de meteoritos.
     * @param view - Recibe la vista donde serán representados.
     */
    public Meteors(int id, Vista view) {
        this.id = id;
        this.view = view;
        size = (int) ((Math.random() * 25) + 10);
        impactosdectruccionmax = (36 - size) / 2;
        impactosdestrucccion = impactosdectruccionmax;
        minsize = view.getWidth() / size;
        diametro = minsize * 4;
        movimiento = (int) ((view.getWidth() / 400) * ((Math.random() * 6) + 2));
        x = (int) (Math.random() * (view.getWidth() - diametro));
        y = -diametro;
        canvas = null;
        switch (id) {
            case 0:
                bttemp = BitmapFactory.decodeResource(view.getResources(), R.drawable.meteor);
                break;
            case 1:
                bttemp = BitmapFactory.decodeResource(view.getResources(), R.drawable.meteor2);
                break;
            default:
                bttemp = BitmapFactory.decodeResource(view.getResources(), R.drawable.meteor3);
                break;
        }
        bmpmeteor = Bitmap.createScaledBitmap(bttemp, diametro, diametro, true);
        bttemp = BitmapFactory.decodeResource(view.getResources(), R.drawable.blast);
        bmpblast = Bitmap.createScaledBitmap(bttemp, diametro, diametro, true);
    }

    /**
     * Función que devuelve un rectángulo de las dimensiones del meteorito.
     * @return Devuelve un Rect.
     */
    public Rect leePerimetro() {
        return new Rect(x, y, x + diametro, y + diametro);
    }

    /**
     * Método que comprueba las colisiones de los meteoritos y la anve del jugador.
     */
    public void collision() {
        if (leePerimetro().intersect(view.nave.leePerimetro()) && !impacto && view.nave.Alive()) {
            view.nave.health -= 20 * impactosdestrucccion;
            view.puntuacion += impactosdectruccionmax * movimiento;
            view.meteoritos++;
            impacto = true;
        }
        if (impactosdestrucccion <= 0 && !impacto) {
            view.puntuacion += impactosdectruccionmax * movimiento;
            view.meteoritos++;
            impacto = true;
        }
        if (impacto) {
            contexplo++;
        }
    }

    /**
     * Método que mueve el meteorito de arriba a abajo en el eje de ordenadas.
     */
    public void movement() {
        y += movimiento;
    }
}

