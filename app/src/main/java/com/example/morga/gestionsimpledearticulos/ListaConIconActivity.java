package com.example.morga.gestionsimpledearticulos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ListaConIconActivity extends AppCompatActivity {

    private static int ACTIVITY_TASK_ADD = 1;
    private static int ACTIVITY_TASK_UPDATE = 2;
    private static int ACTIVITY_TASK_UPDATE_MOVE = 3;

    private ArticuloDataSource bd;
    private long idActual;
    private int posicionActual;
    private adapterListaConIcon scArticulos;
    //private filterKind filterActual;

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
        setContentView(R.layout.activity_lista_con_icon);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addArticulo();
            }
        });

        setTitle("Toolbar & Adapter Icon");

        bd = new ArticuloDataSource(this);
        loadArticulo();
    }

    //*****
    //Metodo que carga el listView
    //*****
    private void loadArticulo()
    {
        // Demanem totes les tasques
        Cursor cursorArticulo = bd.getAllArticulos();

        // Now create a simple cursor adapter and set it to display
        scArticulos = new adapterListaConIcon(this, R.layout.row_lista_icon, cursorArticulo, from, to, 1);
        scArticulos.oArticuloIcon = this;

        //filterActual = filterKind.FILTER_ALL;

        ListView lv = (ListView) findViewById(R.id.lvDades);
        lv.setAdapter(scArticulos);

        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
                    {
                        // modifiquem el id
                        updateArticulo(id);
                    }
                }
        );
        //setListAdapter(scArticulos);
    }

    //*****
    //Metodo para refrescar los datos de la listView
    //*****
    private void refreshArticulos() {

        // Demanem totes les tasques
        Cursor cursorArticulos = bd.getAllArticulos();

        // Now create a simple cursor adapter and set it to display
        // Notifiquem al adapter que les dades han canviat i que refresqui
        scArticulos.changeCursor(cursorArticulos);
        scArticulos.notifyDataSetChanged();
    }


    //*****
    //Metodo que llama a la activity para crear un nuevo articulo
    //*****
    public void addArticulo()
    {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        idActual = -1;

        Intent i = new Intent(this, CrearActivity.class );
        i.putExtras(bundle);
        startActivityForResult(i,ACTIVITY_TASK_ADD);
    }


    //*****
    //Metodo que llama a la activity de detalles para modificar un articulo
    //*****
    public void updateArticulo (long id) {
        //Llamamaos a la activity de detalles enviandole la id del
        // seleccionado para que cargue sus datos
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);

        idActual = id;

        Intent i = new Intent (this, EntradaOSalidaActivity.class);
        i.putExtras(bundle);
        startActivityForResult(i, ACTIVITY_TASK_UPDATE_MOVE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_TASK_ADD) {
            if (resultCode == RESULT_OK) {
                // Carreguem totes les tasques a lo bestia
                refreshArticulos();
            }
        }

        if (requestCode == ACTIVITY_TASK_UPDATE) {
            if (resultCode == RESULT_OK) {
                refreshArticulos();
            }
        }

        if (requestCode == ACTIVITY_TASK_UPDATE_MOVE) {
            if (resultCode == RESULT_OK) {
                refreshArticulos();
            }
        }

    }



//    protected void onListItemClick(ListView l, View v, int position, long id)
//    {
//        super.onListItemClick(l, v, position, id);
//
//        // modifiquem el id
//        updateArticulo(id);
//
//    }

    //*****
    //Metodo para sumar un articulo al estoc del producto clicado
    //*****
    public void plusArticulo(final int _id, final long estoque, final String codigo)
    {
        String fecha = obtenerFecha();
        float cantidad = 1;
        String tipo = "E";


        bd.plusArticulo(_id, codigo, estoque, fecha, cantidad, tipo);
        refreshArticulos();


    }

    //*****
    //Metodo para restar un articulo al estoc del producto clicado
    //*****
    public void lessArticulo(final int _id, final long estoque, final String codigo)
    {
        String fecha = obtenerFecha();
        float cantidad = 1;
        String tipo = "S";

        if (estoque <= 0)
        {
            Toast.makeText(ListaConIconActivity.this, "Estas al minimo del estoc", Toast.LENGTH_SHORT).show();
        }
        else
        {
            bd.lessArticulo(_id, codigo, estoque, fecha, cantidad, tipo);
            refreshArticulos();
        }
    }

    //Metodo para obtener la fecha de hoy
    public String obtenerFecha () {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(cal.getTime());
// Output "Wed Sep 26 14:23:28 EST 2012"

        String formatted = format1.format(cal.getTime());
        System.out.println(formatted);
// Output "2012-09-26"

        return formatted;
    }

}


class adapterListaConIcon extends android.widget.SimpleCursorAdapter  {
    private static final String colorArticuloCero = "#FFFF0000";
    private static final String colorArticuloMasCero = "#FFFFFFFF";

    public ListaConIconActivity oArticuloIcon;

    public adapterListaConIcon(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
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


        //*******
        //Asi capturamos los eventos de los botones dentro del listView
        //*******
        ImageView btnPlus = (ImageView)view.findViewById(R.id.buttonPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Busco la ROW
                View row = (View) v.getParent().getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);

                //Aqui cojo los dos datos que necesito que son la id para identificarlo y el estoc actual para poder sumarlo restarlo
                oArticuloIcon.plusArticulo((linia.getInt(linia.getColumnIndexOrThrow(ArticuloDataSource.ARTICULO_ID))),
                        (linia.getInt(linia.getColumnIndexOrThrow(ArticuloDataSource.ARTICULO_ESTOQUE))),
                        (linia.getString(linia.getColumnIndexOrThrow(ArticuloDataSource.ARTICULO_CODIGO))));

            }
        });


        ImageView btnLess = (ImageView)view.findViewById(R.id.buttonLess);
        btnLess.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //Busco la ROW
                View row = (View) v.getParent().getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);

                //Aqui cojo los dos datos que necesito que son la id para identificarlo y el estoc actual para poder sumarlo o restarlo
                oArticuloIcon.lessArticulo((linia.getInt(linia.getColumnIndexOrThrow(ArticuloDataSource.ARTICULO_ID))),
                        (linia.getInt(linia.getColumnIndexOrThrow(ArticuloDataSource.ARTICULO_ESTOQUE))),
                        (linia.getString(linia.getColumnIndexOrThrow(ArticuloDataSource.ARTICULO_CODIGO))));
            }
        });

        return view;
    }

}