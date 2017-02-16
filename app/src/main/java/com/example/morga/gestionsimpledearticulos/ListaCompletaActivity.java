package com.example.morga.gestionsimpledearticulos;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class ListaCompletaActivity extends ListActivity {

    private static int ACTIVITY_TASK_ADD = 1;
    private static int ACTIVITY_TASK_UPDATE = 2;

    private ArticuloDataSource bd;
    private long idActual;
    private int positionActual;
    private adapterListaCompleta scArticulos;

    private static String[] from = new String[] {
            ArticuloDataSource.ARTICULO_CODIGO,
            ArticuloDataSource.ARTICULO_DESCRIPCION,
            ArticuloDataSource.ARTICULO_PVP,
            ArticuloDataSource.ARTICULO_ESTOQUE };

    private static int[] to = new int[] {
            R.id.textViewCodigo,
            R.id.textViewDescripcion,
            R.id.textViewPvp,
            R.id.textViewEstoque };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_completa);


        Button btnAdd = (Button) findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addArticulo();
            }
        });

        bd = new ArticuloDataSource(this);
        loadArticulo();
    }

    private void loadArticulo()
    {
        // Demanem totes les tasques
        Cursor cursorArticulo = bd.getAllArticulos();

        // Now create a simple cursor adapter and set it to display
        scArticulos = new adapterListaCompleta(this, R.layout.row_lista_completa, cursorArticulo, from, to, 1);
        setListAdapter(scArticulos);
    }

    private void refreshArticulos() {

        // Demanem totes les tasques
        Cursor cursorArticulos = bd.getAllArticulos();

        // Now create a simple cursor adapter and set it to display
        scArticulos.changeCursor(cursorArticulos);
        scArticulos.notifyDataSetChanged();
    }

    private void addArticulo()
    {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        idActual = -1;

        Intent i = new Intent(this, CrearActivity.class );
        i.putExtras(bundle);
        startActivityForResult(i,ACTIVITY_TASK_ADD);
    }

    private void updateArticulo(long id)
    {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",id);

        idActual = id;


        Intent i = new Intent(this, CrearActivity.class );
        i.putExtras(bundle);
        startActivityForResult(i,ACTIVITY_TASK_UPDATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == ACTIVITY_TASK_ADD)
        {
            if (resultCode == RESULT_OK)
            {
                // Carreguem totes les tasques a lo bestia
                refreshArticulos();
            }
        }

        if (requestCode == ACTIVITY_TASK_UPDATE)
        {
            if (resultCode == RESULT_OK)
            {
                refreshArticulos();
            }
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        // modifiquem el id
        updateArticulo(id);

    }
}


class adapterListaCompleta extends android.widget.SimpleCursorAdapter {
    private static final String colorArticuloCero = "#FFFF0000";
    private static final String colorArticuloMasCero = "#FFFFFFFF";

    public adapterListaCompleta(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        // Agafem l'objecte de la view que es una LINEA DEL CURSOR
        Cursor linia = (Cursor) getItem(position);
        int articuloEstoc = linia.getInt(linia.getColumnIndexOrThrow(ArticuloDataSource.ARTICULO_ESTOQUE));

        // Pintem el fons de la view segons està completada o no
        if (articuloEstoc > 0) {
            view.setBackgroundColor(Color.parseColor(colorArticuloMasCero));
        }
        else {
            view.setBackgroundColor(Color.parseColor(colorArticuloCero));
        }

        return view;
    }
}