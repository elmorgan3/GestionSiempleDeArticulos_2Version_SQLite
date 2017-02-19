package com.example.morga.gestionsimpledearticulos;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EntradaOSalidaActivity extends Activity {

    TextView txtCodigo;
    EditText etFecha;
    EditText etCantidad;
    EditText etTipo;

    TextView txtEstocActual;

    String codigo;
    String descripcion;
    Float pvp;

    private long idArticulo;
    private ArticuloDataSource bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada_osalida);

        txtCodigo = (TextView) findViewById(R.id.textViewCodigo);
        etFecha = (EditText) findViewById(R.id.editTextFecha);
        etCantidad = (EditText) findViewById(R.id.editTextCantidad);
        etTipo = (EditText) findViewById(R.id.editTextTipo);

        txtEstocActual= (TextView) findViewById(R.id.textViewEstoc);

        bd = new ArticuloDataSource(this);

        //Botones de aceptar y cancelar
        Button btnAceptar = (Button) findViewById(R.id.buttonAceptar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                aceptarCambios();
            }
        });

        // Boton cancelar
        Button btnCancel = (Button) findViewById(R.id.buttonCancelar);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelarCambios();
            }
        });


        // Busquem el id que estem modificant
        // si el el id es -1 vol dir que s'està creant
        idArticulo = this.getIntent().getExtras().getLong("id");

        if (idArticulo != -1) {
            // Si estem modificant carreguem les dades en pantalla
            cargarDatos();

        }

    }

    //**********
    //Funcion que carga los datos
    //**********
    private void cargarDatos()
    {



        // Demanem un cursor que retorna un sol registre amb les dades de la tasca
        // Això es podria fer amb un classe pero...
        Cursor datos = bd.articulo(idArticulo);
        datos.moveToFirst();

        // Carreguem les dades en la interfície
        txtCodigo.setText(datos.getString(datos.getColumnIndex(ArticuloDataSource.ARTICULO_CODIGO)));


        etFecha.setText(obtenerFecha());
//
//        etCantidad.setText(datos.getString(datos.getColumnIndex(ArticuloDataSource.ARTICULO_PVP)));
//
//        etTipo.setText(datos.getString(datos.getColumnIndex(ArticuloDataSource.ARTICULO_ESTOQUE)));

        txtEstocActual.setText(datos.getString(datos.getColumnIndex(ArticuloDataSource.ARTICULO_ESTOQUE)));


        descripcion= (datos.getString(datos.getColumnIndex(ArticuloDataSource.ARTICULO_DESCRIPCION)));
        pvp = (datos.getFloat(datos.getColumnIndex(ArticuloDataSource.ARTICULO_PVP)));
    }



    //**********
    //Funcion CREAR O ACTUALIZAR los datos
    //**********
    private void aceptarCambios() {

        // Validem les dades
        TextView tv;

        // El CODIGO
        tv = (TextView) findViewById(R.id.textViewCodigo);
        String codigo = tv.getText().toString();


        //La FECHA
        tv = (EditText) findViewById(R.id.editTextFecha);


        String fecha = tv.getText().toString();

        if (!(isValidDate(fecha))){
            MyDialog.showToast(this,"El formato de la FECHA, no es el correcto");
            return;
        }

        //La CANTIDAD
        tv = (EditText) findViewById(R.id.editTextCantidad);
        float cantidad;
        try
        {
            cantidad = Float.valueOf(tv.getText().toString());
        }
        catch (Exception e)
        {
            MyDialog.showToast(this,"La CANTIDAD, tiene que ser un valor de tipo real.");
            return;
        }

        if (cantidad < 0)
        {
            MyDialog.showToast(this,"La CANTIDAD tiene que ser mayor que 0.");
            return;
        }


        //El TIPO
        tv = (EditText) findViewById(R.id.editTextTipo);
        String tipo = tv.getText().toString();

        tv = (TextView) findViewById(R.id.textViewEstoc);
        float estoque = Float.valueOf(tv.getText().toString());

        if ((tipo.equals("E") ) || tipo.equals("S")){
            if (tipo.equals("E")){
                estoque = estoque + cantidad;
            }
            else if ((estoque - cantidad) <0) {
                MyDialog.showToast(this, "ERROR. Estas intentando sacar mas productos, de los que hay.");
                return;
            }
            else{
                estoque = estoque - cantidad;
            }
        }
        else{
            MyDialog.showToast(this,"El tipo tienen que ser 'S' de salida o 'E' de entrada.");
            return;
        }


        bd.insertMovimiento(codigo,fecha,cantidad,tipo);

        bd.update(idArticulo,codigo, descripcion, pvp, estoque);

        Intent mIntent = new Intent();
        mIntent.putExtra("id", idArticulo);
        setResult(RESULT_OK, mIntent);

        finish();

    }

    //*****
    //MEtodo para obtener el dia actual
    //*****
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

    //**********
    //Funcion CANCELAR
    //**********
    private void cancelarCambios() {
        Intent mIntent = new Intent();
        mIntent.putExtra("id", idArticulo);
        setResult(RESULT_CANCELED, mIntent);


        finish();
    }
}
