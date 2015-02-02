package com.dam.spaceinvaders;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityPuntuaciones extends ListActivity implements AdapterView.OnItemLongClickListener {

    private ListView lv;
    private AdapterPuntuaciones listAdapter;
    public static SQLiteDatabase db;
    public static final String nameDb = "puntuaciones.sqlite";
    private ItemLista[] datosregistro;
    private String nombre;
    private int impactosenemigo, meteoritos, puntuacion;
    public static MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuaciones);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        createBD();
        Bundle bundle = getIntent().getExtras();
        if (!bundle.isEmpty()) {
            nombre = bundle.getString("nombre");
            impactosenemigo = bundle.getInt("impactosenemigo");
            meteoritos = bundle.getInt("meteoritos");
            puntuacion = bundle.getInt("puntuacion");
            insertPlayer();
        }
        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setTypeface(MainActivity.typeFace);
        lv = (ListView) findViewById(android.R.id.list);
        lv.setOnItemLongClickListener(this);
        refreshListado();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mp = MediaPlayer.create(this, R.raw.whatislove);
        mp.setLooping(true);
        mp.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.stop();
    }

    @Override
    protected void onDestroy() {
        super.onResume();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_puntuaciones, menu);
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

    /**
     * Método que crea la base de datos.
     */
    public void createBD() {
        AdminSQLite admin = new AdminSQLite(this, nameDb, null, 1);
        db = admin.getWritableDatabase();
        // Si hemos abierto correctamente la base de datos
        if (db == null) {
            Toast.makeText(this, "Se encontró un error al conectar con la base de datos: " + nameDb, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Método que inserta un nuevo jugador o lo actualiza si este ya existe y ha superado su propio récord.
     */
    public void insertPlayer() {
        ContentValues registro = new ContentValues();
        registro.put("nombre", nombre);
        registro.put("impactosenemigo", impactosenemigo);
        registro.put("meteoritos", meteoritos);
        registro.put("puntuacion", puntuacion);
        if (!checkExistsPlayer()) {
            db.insert("estadisticas", null, registro);
        } else {
            if (getPuntuacion() < puntuacion) {
                int cant = db.update("estadisticas", registro, "LOWER(nombre)=LOWER('" + nombre + "')", null);
                if (cant > 0) {
                    Toast.makeText(this, "¡Récord personal superado!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Récord personal no superado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Función que comprueba la existencia de un usuario en la base de datos.
     *
     * @return Devuelve true si existe un jugador y flase en caso contrario.
     */
    public boolean checkExistsPlayer() {
        boolean exists = false;
        Cursor fila = db.rawQuery("SELECT nombre FROM estadisticas WHERE LOWER(nombre) = LOWER('" + nombre + "')", null);
        if (fila.getCount() > 0) {
            exists = true;
        }
        return exists;
    }

    /**
     * Función que obtiene la puntuación de un jugador ya existente en la base de datos.
     *
     * @return Devuelve la puntuación del jugador buscado.
     */
    public int getPuntuacion() {
        int puntuacion = 0;
        Cursor fila = db.rawQuery("SELECT puntuacion FROM estadisticas WHERE LOWER(nombre) = LOWER('" + nombre + "')", null);
        if (fila.moveToFirst()) {
            puntuacion = fila.getInt(0);
        }
        return puntuacion;
    }

    /**
     * Método que refresca el ListView de puntuaciones.
     */
    public void refreshListado() {
        Cursor fila =
                db.rawQuery(
                        "SELECT * FROM estadisticas ORDER BY puntuacion DESC",
                        null);
        datosregistro = new ItemLista[fila.getCount()];

        if (fila.moveToFirst()) {
            int i = 0;
            do {
                datosregistro[i] = (new ItemLista(i, fila.getString(0), fila.getInt(1), fila.getInt(2), fila.getInt(3)));
                i++;
            } while (fila.moveToNext());
            listAdapter = new AdapterPuntuaciones(this, datosregistro);
            lv.setAdapter(listAdapter);
        } else {
            lv.setAdapter(null);
        }
    }

    /**
     * Método que elimina el usuario de la base de datos pasado como parámetro
     *
     * @param name - Recibe un nombre de usuario.
     */
    public void removeRegister(String name) {
        int cant = db.delete("estadisticas", "LOWER(nombre) = LOWER('" + name + "')", null);
        if (cant > 0) {
            refreshListado();
        }
    }

    /**
     * Método que muestra un diálogo preguntando al usuario si desea borrar un registro de la base de datos.
     *
     * @param context - Recibe el contexto de la actividad.
     * @param item - Recibe el item seleccionado por el usuario.
     */
    public void callDialogRemove(final Context context, final ItemLista item) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        TextView tv = (TextView) dialog.findViewById(R.id.textView);
        tv.setTypeface(MainActivity.typeFace);
        tv.setText("¿Deseas borrar este récord?");
        Button buttonDialogYes = (Button) dialog.findViewById(R.id.button4);
        buttonDialogYes.setTypeface(MainActivity.typeFace);
        buttonDialogYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeRegister(item.getNombre());
                dialog.dismiss();
            }
        });

        Button buttonDialogNo = (Button) dialog.findViewById(R.id.button5);
        buttonDialogNo.setTypeface(MainActivity.typeFace);
        buttonDialogNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {

        //Toast.makeText(getActivity(), getListView().getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ItemLista item = listAdapter.lista[position];
        callDialogRemove(this, item);
        return false;
    }
}
