package com.example.morga.gestionsimpledearticulos;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;


public class MainActivity extends Activity {

    private static final int ACTIVITY_SIMPLE = 0;
    private static final int ACTIVITY_FILTER = 1;
    private static final int ACTIVITY_TASK_ADD = 1;
    private static final int ACTIVITY_ICON = 2;
    private static final int ACTIVITY_MOVIMIENTOS= 3;

    private long idActual;

    Button btnListaCompleta;
    Button btnListadoFiltro;
    Button btnCrear;
    Button btnListadoConBotones;
    Button btnMovimientos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnListaCompleta= (Button)findViewById(R.id.buttonListadoCompleto);
        btnListadoFiltro = (Button) findViewById(R.id.buttonListadoFiltrado);
        btnCrear = (Button)findViewById(R.id.buttonCrearArticulo);
        btnListadoConBotones = (Button)findViewById(R.id.buttonListadoConBotones);
        btnMovimientos = (Button)findViewById(R.id.buttonMovimientos);
    }

    //******
    //Metodo para abrir la activity con el listado completo
    //******
    public void mostrarListaCompleta(View v)
    {
        Intent i = new Intent(this, ListaCompletaActivity.class);

        //startActivity(i);
        startActivityForResult(i, ACTIVITY_SIMPLE);
    }

    //******
    //Metodo para abrir la activity con el listado con filtro
    //******
    public void mostrarListaFiltrada(View v)
    {
        Intent i = new Intent(this, ListaFiltradaActivity.class);

        startActivityForResult(i, ACTIVITY_FILTER);
    }

    //******
    //Metodo para abrir la activity para crear articulo
    //******
    public void crearArticulo(View v)
    {

        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        idActual = -1;

        Intent i = new Intent(this, CrearActivity.class );
        i.putExtras(bundle);
        startActivityForResult(i,ACTIVITY_TASK_ADD);
    }

    //******
    //Metodo para abrir la activity con el listado completo con iconos
    //******
    public void mostrarListadoConBotones (View v)
    {
        Intent i = new Intent(this, ListaConIconActivity.class);

        startActivityForResult(i, ACTIVITY_ICON);

    }

    //******
    //Metodo para abrir la activity con el listado completo con iconos
    //******
    public void mostrarListadoMovimientos (View v)
    {
        Intent i = new Intent(this, ListaMovimientosActivity.class);

        startActivityForResult(i, ACTIVITY_MOVIMIENTOS);

    }

}



