package com.example.morga.gestionsimpledearticulos;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ListaMovimientosActivity extends AppCompatActivity {

    private ArticuloDataSource bd;
    private adapterMovimientoFilter scTasks;
    EditText tv;
    Toast toast;

    Button btnFiltrarFecha;
    Button btnFiltrarCodigo;

    private filterKind filterActual;

    private static String[] from = new String[] {
            ArticuloDataSource.MOVIMIENTO_CODIGO,
            ArticuloDataSource.MOVIMIENTO_DIA,
            ArticuloDataSource.MOVIMIENTO_CANTIDAD,
            ArticuloDataSource.MOVIMIENTO_TIPO };

    private static int[] to = new int[] {
            R.id.textViewCodigo,
            R.id.textViewFecha,
            R.id.textViewCantidad,
            R.id.textViewTipo };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_movimientos);


        btnFiltrarCodigo= (Button) findViewById(R.id.buttonFiltrarCodigo);
        btnFiltrarFecha= (Button) findViewById(R.id.buttonFiltrarFecha);

        bd = new ArticuloDataSource(this);

        loadMovimientos();
    }


    private void loadMovimientos () {
        // Demanem totes les tasques
        Cursor cursorMovimientos = bd.getAllMovimientos();
        filterActual = filterKind.FILTER_ALL;

        // Now create a simple cursor adapter and set it to display
        scTasks = new adapterMovimientoFilter(this, R.layout.row_lista_movimientos, cursorMovimientos, from, to, 1);
        ListView lista=(ListView)findViewById(R.id.list);
        lista.setAdapter(scTasks);
    }

    public void metodoFiltrarPorCodigo (View v) {

        try {
            tv = (EditText) findViewById(R.id.editTextCodigo);
            String codigo = tv.getText().toString();
            Cursor cursorTasks = bd.historicoCodigo(codigo);

            // Notifiquem al adapter que les dades han canviat i que refresqui
            scTasks.changeCursor(cursorTasks);
            scTasks.notifyDataSetChanged();

        }
        catch (Exception e) {
            MyDialog.showToast(this, "Ha habido algun problema con el campo de codigo");

            return;
        }
    }

    public void metodoFiltrarPorFecha (View v) {

        //La FECHA
        tv = (EditText) findViewById(R.id.editTextFecha);

        String fecha = tv.getText().toString();

        Cursor cursorTasks = bd.historicoCodigo(fecha);

        if (!(isValidDate(fecha))){
            MyDialog.showToast(this,"El formato de la FECHA, no es el correcto");
            return;
        }
        else {
            // Notifiquem al adapter que les dades han canviat i que refresqui
            scTasks.changeCursor(cursorTasks);
            scTasks.notifyDataSetChanged();
        }
    }

    //*****
    //MEtodo para saber si una fecha es valida
    //*****
    public boolean isValidDate(String dateString) {

        if (dateString == null || !dateString.matches("\\d{4}-[01]\\d-[0-3]\\d"))
            return false;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        try {
            df.parse(dateString);
            return true;
        } catch (ParseException ex) {
            return false;
        }

    }



}

class adapterMovimientoFilter extends android.widget.SimpleCursorAdapter {

    public adapterMovimientoFilter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        return view;
    }
}

