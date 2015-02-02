package com.dam.spaceinvaders;

/**
 * Created by Victor on 30/01/2015.
 */
public class ItemLista {

    private long id;
    private String nombre;
    private int impactosenemigo, meteoritos, puntuacion;

    /**
     * Contructor de la clase ItemLista.
     *
     * @param id - Recibe la posición en la lista.
     * @param nombre - Recibe el nombre del item.
     * @param impactosenemigo - Recibe los impactosenemigo del item.
     * @param meteoritos - Recibe los meteoritos del item.
     * @param puntuacion - Recibe la puntuacion del item.
     */
    public ItemLista(long id, String nombre, int impactosenemigo, int meteoritos, int puntuacion) {
        this.id = id;
        this.nombre = nombre;
        this.impactosenemigo = impactosenemigo;
        this.meteoritos = meteoritos;
        this.puntuacion = puntuacion;
    }

    /**
     *  Función que devuelve el id.
     *
     * @return Devuelve el id del item.
     */
    public long getId() {
        return id;
    }

    /**
     *  Función que devuelve el nombre.
     *
     * @return Devuelve el nombre del item.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *  Función que devuelve los impactos realizados a la nave enemiga.
     *
     * @return Devuelve los impactos realizados a la nave enemiga. del item.
     */
    public int getImpactosEnemigo() {
        return impactosenemigo;
    }

    /**
     *  Función que devuelve los meteoritos acertados por el jugador.
     *
     * @return Devuelve los meteoritos acertados por el jugador del item.
     */
    public int getMeteoritos() {
        return meteoritos;
    }

    /**
     * Función que devuelve la puntuacion.
     *
     * @return Deuelve la puntuación del item.
     */
    public int getPuntuacion() {
        return puntuacion;
    }

}
