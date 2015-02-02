package com.dam.spaceinvaders;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Victor on 30/01/2015.
 */
public class AdapterPuntuaciones extends BaseAdapter {
    private final Activity contexto;
    public final ItemLista[] lista;

    /**
     * Constructor de la clase AdapterPuntuaciones.
     *
     * @param contexto - Recibe el contexto de la actividad.
     * @param lista - Recibe la lista obtenida del select a la base de datos.
     */
    public AdapterPuntuaciones(Activity contexto, ItemLista[] lista) {
        super();
        this.contexto = contexto;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.length;
    }

    public Object getItem(int n) {
        return lista[n];
    }

    @Override
    public long getItemId(int position) {
        return lista[position].getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = contexto.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_item, null, true);

        ItemLista item = lista[position];
        TextView tvnombre = (TextView) view.findViewById(R.id.textView3);
        tvnombre.setTypeface(MainActivity.typeFace);
        tvnombre.setText(item.getNombre());
        TextView tvpuntuacion = (TextView) view.findViewById(R.id.textView4);
        tvpuntuacion.setTypeface(MainActivity.typeFace);
        tvpuntuacion.setText(String.valueOf(item.getPuntuacion()) + " p");
        TextView tvmeteoritos = (TextView) view.findViewById(R.id.textView5);
        tvmeteoritos.setTypeface(MainActivity.typeFace);
        tvmeteoritos.setText(String.valueOf(item.getMeteoritos()));
        TextView tvimpactos = (TextView) view.findViewById(R.id.textView6);
        tvimpactos.setTypeface(MainActivity.typeFace);
        tvimpactos.setText(String.valueOf(item.getImpactosEnemigo()));
        TextView tvnumero = (TextView) view.findViewById(R.id.textView7);
        tvnumero.setTypeface(MainActivity.typeFace);
        tvnumero.setText(String.valueOf(item.getId() + 1));
        if (item.getNombre().toLowerCase().equals(MainActivity.nameUser.toLowerCase())) {
            tvnombre.setTextColor(Color.BLUE);
            tvpuntuacion.setTextColor(Color.BLUE);
            tvmeteoritos.setTextColor(Color.BLUE);
            tvimpactos.setTextColor(Color.BLUE);
            tvnumero.setTextColor(Color.BLUE);
        }

        return view;
    }
}
