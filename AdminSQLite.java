package com.dam.spaceinvaders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Victor on 29/01/2015.
 */
public class AdminSQLite extends SQLiteOpenHelper {
    /**
     * Constructor de la clase AdminSQLite.
     *
     * @param context - Recibe el contexto de la actividad.
     * @param name - Recibe el nombre de la base de datos
     * @param factory - Recibe el CursorFactory.
     * @param version - Recibe la versión.
     */
    public AdminSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL("CREATE TABLE estadisticas (nombre TEXT primary key,  impactosenemigo INTEGER, meteoritos INTEGER, puntuacion INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

        // Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS estadisticas");
        // Se crea la nueva versión de la tabla
        db.execSQL("CREATE TABLE estadisticas (nombre TEXT primary key,  impactosenemigo INTEGER, meteoritos INTEGER, puntuacion INTEGER)");
    }
}
