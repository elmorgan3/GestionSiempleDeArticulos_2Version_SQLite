package com.example.morga.gestionsimpledearticulos;

/**
 * Created by morga on 27/01/2017.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CrearActivity extends AppCompatActivity {
    EditText etCodigo;
    EditText etDescripcion;
    EditText etPvp;
    EditText etEstoque;

    private long idArticulo;
    private ArticuloDataSource bd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        etCodigo = (EditText)findViewById(R.id.editTextCodigo);
        etDescripcion = (EditText)findViewById(R.id.editTextDescripcion);
        etPvp = (EditText)findViewById(R.id.editTextPvp);
        etEstoque = (EditText)findViewById(R.id.editTextEstoque);

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

        // Boton eliminar
        Button btnDelete = (Button) findViewById(R.id.buttonEliminar);

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteArticulo();
            }
        });


        // Busquem el id que estem modificant
        // si el el id es -1 vol dir que s'està creant
        idArticulo = this.getIntent().getExtras().getLong("id");

        if (idArticulo != -1) {
            // Si estem modificant carreguem les dades en pantalla
            cargarDatos();

            //Bloqueamos el editTextCodigo para que no se pueda modificar
            etCodigo.setEnabled(false);

        }
        else {
//            // Si estem creant amaguem el checkbox de finalitzada i el botó d'eliminar
//            CheckBox chk = (CheckBox) findViewById(R.id.chkCompleted);
//            chk.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);

            etCodigo.setEnabled(true);
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

        etCodigo.setText(datos.getString(datos.getColumnIndex(ArticuloDataSource.ARTICULO_CODIGO)));

        etDescripcion.setText(datos.getString(datos.getColumnIndex(ArticuloDataSource.ARTICULO_DESCRIPCION)));

        etPvp.setText(datos.getString(datos.getColumnIndex(ArticuloDataSource.ARTICULO_PVP)));

        etEstoque.setText(datos.getString(datos.getColumnIndex(ArticuloDataSource.ARTICULO_ESTOQUE)));
    }

    //**********
    //Funcion CREAR O ACTUALIZAR los datos
    //**********
    private void aceptarCambios() {
        // Validem les dades
        TextView tv;

        // El CODIGO no puede estar vacia
        tv = (TextView) findViewById(R.id.editTextCodigo);

        String codigo = tv.getText().toString();

        // La DESCRIPCION no puede estar vacia
        tv = (TextView) findViewById(R.id.editTextDescripcion);

        String descripcion = tv.getText().toString();

        if (descripcion.trim().equals(""))
        {
            MyDialog.showToast(this,"La descripción no puede estar vacia");

            return;
        }


        // El PVP tiene que ser real
        tv = (TextView) findViewById(R.id.editTextPvp);

        float pvp;

        try
        {
            pvp = Float.valueOf(tv.getText().toString());
        }
        catch (Exception e)
        {
            MyDialog.showToast(this,"El PVP, tiene que ser un valor de tipo real.");
            return;
        }

        if (pvp < 0)
        {
            MyDialog.showToast(this,"El PVP tiene que ser mayor que 0.");
            return;
        }

        //El ESTOQUE da= si es + o -
        tv = (TextView) findViewById(R.id.editTextEstoque);

        float estoque;

        try
        {
            estoque = Float.valueOf(tv.getText().toString());
        }
        catch (Exception e)
        {
            MyDialog.showToast(this,"El estoc tiene que ser un valor real.");

            return;
        }


        // Mirem si estem creant o estem guardant
        if (idArticulo == -1)
        {
            //Compruebo si el codigo ya esta existe
            if ((bd.comprovarCodi(codigo)) == false)
            {
                idArticulo = bd.insertArticulo(codigo, descripcion, pvp, estoque);
            }
            else
            {
                MyDialog.showToast(this,"Este codigo ya esta registrado.");

                return;
            }

        }
        else
        {
            bd.update(idArticulo,codigo,descripcion,pvp,estoque);

//            // ara indiquem si la tasca esta finalitzada o no
//            CheckBox chk = (CheckBox) findViewById(R.id.chkCompleted);
//            if (chk.isChecked()) {
//                bd.taskCompleted(idArticulo);
//            }
//            else {
//                bd.taskPending(idArticulo);
//            }
        }

        Intent mIntent = new Intent();
        mIntent.putExtra("id", idArticulo);
        setResult(RESULT_OK, mIntent);

        finish();
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


    //**********
    //Funcion para ELIMINAR un articulo
    //**********
    private void deleteArticulo() {

        // Pedimos confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Seguro que quiere eliminar el articulo?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                bd.delete(idArticulo);

                Intent mIntent = new Intent();
                mIntent.putExtra("id", -1);  // Devolvemos -1 indicant que s'ha eliminat
                setResult(RESULT_OK, mIntent);

                finish();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();

    }















//    public void metodoCrear (View v){

//        //MySQLiteHelper dbHelper = new MySQLiteHelper(this, "articulos", null, 1);
//        MySQLiteHelper dbHelper = new MySQLiteHelper(this);
//
//        SQLiteDatabase bd = dbHelper.getWritableDatabase();
//
//        String codigo = etCodigo.getText().toString();
//        String descripcion = etDescripcion.getText().toString();
//        String pvp = etPvp.getText().toString();
//        String estoque = etEstoque.getText().toString();
//
//
//        ContentValues registro = new ContentValues();
//
//        registro.put("codigo", codigo);
//        registro.put("descripcion", descripcion);
//        registro.put("pvp", pvp);
//        registro.put("estoque", estoque);
//
//        bd.insert("articulos", null, registro);
//        bd.close();
//
//        //Volvemos a poner los valores por defecto
//        etCodigo.setText("");
//        etDescripcion.setText("");
//        etPvp.setText("");
//        etEstoque.setText("");
//
//        Toast.makeText(this, "Se almacenaron los datos del artículo",
//                Toast.LENGTH_SHORT).show();
//    }


}


