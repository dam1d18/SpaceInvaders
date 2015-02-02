package com.dam.spaceinvaders;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import java.util.ArrayList;

/**
 * Created by Victor on 03/01/2015.
 */
public class Nave extends Thread {

    private SurfaceHolder sh;
    private Vista view;
    public int x, y, diametro, velocidad, movimiento, minsize;
    public Canvas canvas;
    public Bitmap bmpship, bmprocket, bmpblastship, bmpblast, bttemp;
    public boolean mvright, mvleft, mvup, mvdown;
    public int id, widthbar;
    public double maxhealth, health;
    public double widthHealth;
    public ArrayList<Rocket> rockets;
    public int cdrocket = 0;
    public int cdgrocket = 15;
    public int lifes = 1;
    public boolean initpuntuaciones = false;

    /**
     * Constructor de la clase Nave.
     *
     * @param id - Recibe un id de nave que sirve para cargar distintos tipos de nave,
     *           en este caso es inútil ya que solo hay un único tipo de nave,
     *           pero con vistas a mejorar el videojuego ya está planteado.
     * @param sh - Recibe el SurfaceHolder donde se va a pintar.
     * @param view - Recibe el view que contiene el SurfaceHolder.
     */
    public Nave(int id, SurfaceHolder sh, Vista view) {
        this.id = id;
        this.sh = sh;
        this.view = view;
        minsize = view.getWidth() / 20;
        diametro = minsize * 4;
        velocidad = 10;
        movimiento = (view.getWidth() / 400) * 4;
        widthbar = view.getWidth();
        restartShip();
        mvright = false;
        mvleft = false;
        mvup = false;
        mvdown = false;
        canvas = null;
        switch (id) {
            default:
                bttemp = BitmapFactory.decodeResource(view.getResources(), R.drawable.ship);
                break;
        }
        bmpship = Bitmap.createScaledBitmap(bttemp, diametro, diametro, true);
        bttemp = BitmapFactory.decodeResource(view.getResources(), R.drawable.blast);
        bmpblastship = Bitmap.createScaledBitmap(bttemp, diametro, diametro, true);
        bttemp = BitmapFactory.decodeResource(view.getResources(), R.drawable.rocketup);
        bmprocket = Bitmap.createScaledBitmap(bttemp, diametro / 10, diametro / 5, true);
        bttemp = BitmapFactory.decodeResource(view.getResources(), R.drawable.blast);
        bmpblast = Bitmap.createScaledBitmap(bttemp, diametro / 5, diametro / 5, true);
        rockets = new ArrayList<>();
    }

    /**
     * Método que sirve para reiniciar la nave, en este caso solamente tiene una única vida
     * por tanto solo se utilizará una vez.
     */
    public void restartShip() {
        maxhealth = 1000;
        health = maxhealth;
        widthHealth = health / widthbar;
        x = (view.getWidth() / 2) - (diametro / 2);
        y = view.getHeight() - diametro;
    }

    /**
     * Método que sirve para mover la nave por la pantalla a través de las variables booleanas
     * que se cambian desde el sensor de inclinación del teléfono.
     */
    public void movement() {
        if (mvright) {
            if (x < view.getWidth() - diametro) {
                x += movimiento;
            } else {
                x = view.getWidth() - diametro;
            }
        }
        if (mvleft) {
            if (x > 0) {
                x -= movimiento;
            } else {
                x = 0;
            }
        }
        if (mvup) {
            if (y > 0) {
                y -= movimiento;
            } else {
                y = 0;
            }
        }

        if (mvdown) {
            if (y < view.getHeight() - diametro) {
                y += movimiento;
            } else {
                y = view.getHeight() - diametro;
            }
        }
    }

    /**
     * Método que sirve para cambiar de color la barra de vida de la nave.
     * @return
     */
    public int checkHealth() {
        int c;
        double percent = (health / maxhealth) * 100;
        if (percent > 60) {
            c = Color.GREEN;
        } else if (percent > 25) {
            c = Color.YELLOW;
        } else {
            c = Color.RED;
        }
        return c;
    }

    /**
     * Función que comprueba si la nave tiene al menos un punto de salud, devuelve true si es así,
     * en caso contrario devolverá false.
     * @return Devuelve true si la nave está viva, y false en caso opuesto.
     */
    public boolean Alive() {
        boolean alive = true;
        if (health <= 0) {
            health = 0;
            alive = false;
        }
        return alive;
    }

    /**
     * Método que comprueba si la nave colisiona con la nave enemiga.
     */
    public void collision() {
        if (leePerimetro().intersect(view.enemy.leePerimetro())) {
            health = 0;
        }
    }

    /**
     * Función que devuelve un rectángulo de las dimensiones de la nave.
     * @return Devuelve un Rect.
     */
    public Rect leePerimetro() {
        return new Rect(x, y, x + diametro, y + diametro);
    }

    /**
     * Método que sirve para añadir misiles cada cierto intervalo de tiempo, contabilizado por un contador.
     */
    public void addRocket() {
        if (cdrocket == cdgrocket) {
            rockets.add(new Rocket(x, y, this));
            cdrocket = 0;
        }
        cdrocket++;
    }

    /**
     * Método que simplemente sincroniza y llama al método onDraw de la vista.
     */
    public void canvas() {
        try {
            canvas = sh.lockCanvas(null);
            synchronized (sh) {
                try {
                    view.onDraw(canvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            if (canvas != null)
                sh.unlockCanvasAndPost(canvas);
        }
    }

    public void run() {
        while (MainActivity.run) {
            try {
                if (!MainActivity.pause) {
                    if (Alive()) {
                        addRocket();
                        collision();
                        movement();
                    } else {
                        int c = 60;
                        while (c > 0 && MainActivity.run) {
                            canvas();
                            sleep(velocidad);
                            c--;
                        }
                        lifes--;
                        if (lifes > 0) {
                            restartShip();
                        } else {
                            initpuntuaciones = true;
                            MainActivity.pause = true;
                            MainActivity.run = false;
                        }
                    }
                    canvas();
                } else {
                    Thread.interrupted();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Rocket {
        public int x, y, ancho, alto, movement;
        public boolean impacto = false;
        public int contexplo = 0;
        public Nave nave;

        /**
         * Constructor de la clase Rocket
         *
         * @param x - Recibe un parámetro x que será la posición de salida en el eje de abscisas.
         * @param y - Recibe un parámetro y que será la posición de salida en el eje de ordenadas.
         * @param nave - Recibe la nave que dispara el misil.
         */
        public Rocket(int x, int y, Nave nave) {
            ancho = nave.diametro / 10;
            alto = nave.diametro / 5;
            this.x = x + nave.diametro / 2 - (ancho / 2);
            this.y = y;
            movement = (view.getWidth() / 400) * 6;
            this.nave = nave;
        }

        /**
         * Función que devuelve un rectángulo de las dimensiones del misil.
         * @return Devuelve un Rect.
         */
        public Rect leePerimetro() {
            return new Rect(x, y, x + ancho, y + alto);
        }

        /**
         * Método que comprueba las colisiones de los misiles con la nave enemiga, con los misiles que esta lanza
         * y con los asteroides.
         */
        public void collision() {
            for (int i = 0; i < rockets.size(); i++) {
                if (!impacto) {
                    if (leePerimetro().intersect(view.enemy.leePerimetro())) {
                        view.puntuacion += Vista.puntuacionimpactoenemy;
                        view.impactosenemigo++;
                        impacto = true;
                    }
                    for (int k = 0; k < view.enemy.rockets.size(); k++) {
                        Enemy.Rocket rocketenemy = view.enemy.rockets.get(k);
                        if (leePerimetro().intersect(rocketenemy.leePerimetro()) && !rocketenemy.impacto) {
                            impacto = true;
                            rocketenemy.impacto = true;
                        }
                    }
                } else {
                    contexplo++;
                }
            }
            for (int i = 0; i < view.meteors.size(); i++) {
                if (!impacto) {
                    Meteors meteor = view.meteors.get(i);
                    if (leePerimetro().intersect(meteor.leePerimetro()) && !meteor.impacto) {
                        view.puntuacion += Vista.puntuacionimpactometeor;
                        impacto = true;
                        meteor.impactosdestrucccion--;
                    }
                }
            }
        }
    }
}
