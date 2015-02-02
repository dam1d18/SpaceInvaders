package com.dam.spaceinvaders;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by Victor on 25/01/2015.
 */
public class Enemy extends Thread {

    public int damagerocket = 30;
    private Vista view;
    public int x, y, diametro, velocidad, movimiento, minsize;
    public boolean run;
    public Canvas canvas;
    public Bitmap bmpship, bmprocket, bttemp;
    public int id;
    public ArrayList<Rocket> rockets;
    public int cdrocket = 0;
    public int cdgrocket = 150;
    public int totalrockets = 0;
    public int nmod = 5;

    /**
     * Constructor de la clase Enemy.
     *
     * @param id   - Recibe un id de nave que sirve para cargar distintos tipos de enemigo,
     *             en este caso es inútil ya que solo hay un único tipo de enemigo,
     *             pero con vistas a mejorar el videojuego ya está planteado.
     * @param view - Recibe el view que contiene el SurfaceHolder.
     */
    public Enemy(int id, Vista view) {
        this.id = id;
        this.view = view;
        minsize = view.getWidth() / 20;
        diametro = minsize * 4;
        velocidad = view.nave.velocidad;
        movimiento = view.getWidth() / 400;
        x = (view.getWidth() / 2) - (diametro / 2);
        y = 0;
        canvas = null;
        switch (id) {
            default:
                bttemp = BitmapFactory.decodeResource(view.getResources(), R.drawable.enemy);
                break;
        }
        bmpship = Bitmap.createScaledBitmap(bttemp, diametro, diametro, true);
        bttemp = BitmapFactory.decodeResource(view.getResources(), R.drawable.rocketdown);
        bmprocket = Bitmap.createScaledBitmap(bttemp, diametro / 10, diametro / 5, true);
        rockets = new ArrayList<>();
        start();
    }

    /**
     * Método que incrementa la dificultad del juego de una manera progresiva en base a los misiles
     * lanzados por la nave enemiga.
     */
    public void gamePhases() {
        if (totalrockets % nmod == 0) {
            if (cdgrocket - 5 > 75) {
                cdgrocket -= 5;
            } else {
                cdgrocket = 75;
            }
            damagerocket += 3;
            if (view.meteorappear - 5 > 50) {
                view.meteorappear -= 5;
                view.contmeteor = view.meteorappear - 5;
            } else {
                view.meteorappear = 50;
                view.contmeteor = 40;
            }
            nmod += 6;
        }
    }

    /**
     * Método que sirve para desplazar la nave enemiga hacia la derecha.
     */
    public void movementRight() {
        if (x < view.getWidth() - diametro) {
            x += movimiento;
        } else {
            x = view.getWidth() - diametro;
        }
    }

    /**
     * Método que sirve para desplazar la nave enemiga hacia la izquierda.
     */
    public void movementLeft() {
        if (x > 0) {
            x -= movimiento;
        } else {
            x = 0;
        }
    }

    /**
     * Método que sirve para añadir misiles cada cierto intervalo de tiempo, contabilizado por un contador.
     */
    public void addRocket() {
        if (cdrocket == cdgrocket) {
            rockets.add(new Rocket(x, y, this));
            totalrockets++;
            gamePhases();
            cdrocket = 0;
        }
        cdrocket++;
    }

    /**
     * Método que debe ser llamado desde onDraw, el cual pinta naves y meteoritos del videojuego y calcula colisiones.
     * @param canvas - Recibe el canvas del método onDraw.
     */
    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(view.enemy.bmpship, view.enemy.x, view.enemy.y, view.pincel);
            for (int j = 0; j < view.enemy.rockets.size(); j++) {
                try {
                    Enemy.Rocket rocketenemy = view.enemy.rockets.get(j);
                    rocketenemy.collision();
                    rocketenemy.y += rocketenemy.movement;
                    canvas.drawBitmap(view.enemy.bmprocket, view.enemy.rockets.get(j).x, view.enemy.rockets.get(j).y, view.pincel);
                    if (!rocketenemy.impacto) {
                        canvas.drawBitmap(view.enemy.bmprocket, rocketenemy.x, rocketenemy.y, view.pincel);
                    } else {
                        canvas.drawBitmap(view.nave.bmpblast, rocketenemy.x, rocketenemy.y, view.pincel);
                        if (rocketenemy.contexplo > 20) {
                            view.enemy.rockets.remove(rocketenemy);
                        }
                    }
                    if (rocketenemy.y > view.getHeight() + rocketenemy.alto) {
                        view.enemy.rockets.remove(rocketenemy);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < view.meteors.size(); i++) {
                try {
                    Meteors meteor = view.meteors.get(i);
                    meteor.movement();
                    meteor.collision();
                    if (!meteor.impacto && meteor.impactosdestrucccion > 0) {
                        canvas.drawBitmap(meteor.bmpmeteor, meteor.x, meteor.y, view.pincel);
                    } else {
                        meteor.impacto = true;
                        canvas.drawBitmap(meteor.bmpblast, meteor.x, meteor.y, view.pincel);
                        if (meteor.contexplo > 20) {
                            view.meteors.remove(meteor);
                        }
                    }
                    if (meteor.y > view.getHeight() + meteor.diametro) {
                        view.meteors.remove(meteor);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while (MainActivity.run) {
            try {
                if (!MainActivity.pause) {
                    if (view.enemy.x > view.nave.x) {
                        view.enemy.movementLeft();
                    } else {
                        view.enemy.movementRight();
                    }
                    view.enemy.addRocket();
                    Thread.sleep(velocidad);
                } else {
                    Thread.interrupted();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Función que devuelve un rectángulo de las dimensiones de la nave enemiga.
     * @return Devuelve un Rect.
     */
    public Rect leePerimetro() {
        return new Rect(x, y, x + diametro, y + diametro);
    }

    class Rocket {
        public int x, y, ancho, alto, movement;
        public boolean impacto = false;
        public int contexplo = 0;
        public Enemy enemy;
        /**
         * Constructor de la clase Rocket
         *
         * @param x - Recibe un parámetro x que será la posición de salida en el eje de abscisas.
         * @param y - Recibe un parámetro y que será la posición de salida en el eje de ordenadas.
         * @param enemy - Recibe la nave enemiga que dispara el misil.
         */
        public Rocket(int x, int y, Enemy enemy) {
            ancho = enemy.diametro / 10;
            alto = enemy.diametro / 5;
            this.x = x + enemy.diametro / 2 - (ancho / 2);
            this.y = y + diametro;
            movement = (view.getWidth() / 400) * 6;

            this.enemy = enemy;
        }

        /**
         * Función que devuelve un rectángulo de las dimensiones del misil.
         * @return Devuelve un Rect.
         */
        public Rect leePerimetro() {
            return new Rect(x, y, x + ancho, y + alto);
        }

        /**
         * Método que comprueba las colisiones de los misiles con la nave del jugador.
         */
        public void collision() {
            if (leePerimetro().intersect(view.nave.leePerimetro()) && !impacto) {
                impacto = true;
                view.nave.health -= enemy.damagerocket;
            }
            if (impacto) {
                contexplo++;
            }
        }
    }
}
