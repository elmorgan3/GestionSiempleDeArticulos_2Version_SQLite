package com.example.morga.gestionsimpledearticulos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by morga on 20/01/2017.
 */

public class ArticuloDataSource {

    //Declaro dos objetos de la clase
    // SQLiteDatabase, una para leer y otro para escribir
    // y otro de mi clase MySQLiteHelper
    private SQLiteDatabase dbW, dbR;
    private MySQLiteHelper dbHelper;

    public static final String TABLE_ARTICULOS = "articulos";
    public static final String ARTICULO_ID = "_id";
    public static final String ARTICULO_CODIGO = "codigo";
    public static final String ARTICULO_DESCRIPCION = "descripcion";
    public static final String ARTICULO_PVP = "pvp";
    public static final String ARTICULO_ESTOQUE = "estoque";
    

    public static final String TABLE_MOVIMIENTOS = "movimientos";
    public static final String MOVIMIENTO_ID = "_id";
    public static final String MOVIMIENTO_CODIGO = "codigo";
    public static final String MOVIMIENTO_DIA = "dia";
    public static final String MOVIMIENTO_CANTIDAD = "cantidad";
    public static final String MOVIMIENTO_TIPO = "tipo";

    //Constructor
    public ArticuloDataSource(Context context)
    {
        //En el contructor directamente abro la comunicacion con la bbdd
        dbHelper = new MySQLiteHelper(context);

        //tambien construimos dos bbdd una para leer y la otra para modificarla
        open();
    }

    //Abro las bbdd
    public void open()
    {
        dbW = dbHelper.getWritableDatabase();
        dbR = dbHelper.getReadableDatabase();
    }

    //Destructor
    public void close()
    {
        dbW.close();
        dbR.close();
    }

    //*********
    //Funcion para insertar un Articulo
    //*********
    //Creamos un nuevo articulo y devolbemos el id si lo necesitamos
    public long insertArticulo (String codigo, String descripcion, float pvp, float estoque)
    {
        //Ponemos los valores que seran insertados en la bbdd
        ContentValues values = new ContentValues();

        values.put(ARTICULO_CODIGO, codigo);
        values.put(ARTICULO_DESCRIPCION, descripcion);
        values.put(ARTICULO_PVP, pvp);
        values.put(ARTICULO_ESTOQUE, estoque);



        //Insertamos el Articulo
        return dbW.insert(TABLE_ARTICULOS, null, values);
    }

    public long insertMovimiento (String codigo, String fecha, float cantidad, String tipo)
    {
        //Ponemos los valores que seran insertados en la bbdd
        ContentValues values = new ContentValues();

        values.put(MOVIMIENTO_CODIGO, codigo);
        values.put(MOVIMIENTO_DIA, fecha);
        values.put(MOVIMIENTO_CANTIDAD, cantidad);
        values.put(MOVIMIENTO_TIPO, tipo);



        //Insertamos el Articulo
        return dbW.insert(TABLE_MOVIMIENTOS, null, values);
    }


    //*********
    //Funcion para actualizar un Articulo
    //*********
    //Creamos un nuevo articulo y devolbemos el id si lo necesitamos
    public void update(long id, String codigo, String descripcion, float pvp, float estoque)
    {
        // Modifiquem els valors de las tasca amb clau primària "id"
        ContentValues values = new ContentValues();
        values.put(ARTICULO_CODIGO, codigo);
        values.put(ARTICULO_DESCRIPCION, descripcion);
        values.put(ARTICULO_PVP,pvp);
        values.put(ARTICULO_ESTOQUE,estoque);

        dbW.update(TABLE_ARTICULOS,values, ARTICULO_ID + " = ?", new String[] { String.valueOf(id) });
    }



    //*********
    //Funcion para eliminar un Articulo con la PK id
    //*********
    public void delete(long id)
    {
        // Eliminem la Articulo amb clau primària "id"
        dbW.delete(TABLE_ARTICULOS, ARTICULO_ID + " = ?", new String[] { String.valueOf(id) });
    }



    //**********
    // Funciion que retorna todos los articulos de la bbdd en un cursor
    //**********
    public Cursor getAllArticulos()
    {
        return dbR.query(TABLE_ARTICULOS, new String[] {ARTICULO_ID, ARTICULO_CODIGO, ARTICULO_DESCRIPCION, ARTICULO_PVP, ARTICULO_ESTOQUE},
                null, null,null,null, ARTICULO_ID);
    }

    //**********
    // Funciion que retorna todos los movimientos de la bbdd en un cursor
    //**********
    public Cursor getAllMovimientos()
    {
        return dbR.query(TABLE_MOVIMIENTOS, new String[] {MOVIMIENTO_ID, MOVIMIENTO_CODIGO, MOVIMIENTO_DIA, MOVIMIENTO_CANTIDAD, MOVIMIENTO_TIPO},
                null, null,null,null, MOVIMIENTO_ID);
    }

    //busquem una row per la seu codi
    public Cursor historicoCodigo(String codigo) {

        // Retorna un cursor només amb el id indicat
        return  dbR.query(TABLE_MOVIMIENTOS, new String[]{MOVIMIENTO_ID, MOVIMIENTO_CODIGO, MOVIMIENTO_DIA, MOVIMIENTO_CANTIDAD, MOVIMIENTO_TIPO},
                MOVIMIENTO_CODIGO + "=?", new String[]{String.valueOf(codigo)},
                null, null, null);

        //
    }
    //busquem per la data!!!
    public Cursor historicData(String codigo) {

        // Retorna un cursor només amb el id indicat
        return  dbR.query(TABLE_MOVIMIENTOS, new String[]{MOVIMIENTO_ID, MOVIMIENTO_CODIGO, MOVIMIENTO_DIA, MOVIMIENTO_CANTIDAD, MOVIMIENTO_TIPO},
                MOVIMIENTO_CODIGO + "=?", new String[]{String.valueOf(codigo)},
                null, null, null);

        //
    }



    //**********
    // Funciion que retorna solo el id pedido
    //**********
    public Cursor articulo(long id)
    {
        // Retorna un cursor només amb el id indicat
        // Retornem les tasques que el camp DONE = 1
        return dbR.query(TABLE_ARTICULOS, new String[] { ARTICULO_ID, ARTICULO_CODIGO, ARTICULO_DESCRIPCION,ARTICULO_PVP, ARTICULO_ESTOQUE},
                ARTICULO_ID+ "=?", new String[]{String.valueOf(id)},
                null, null, null);

    }

    //*********
    //Funcion que devuelve un boolean para saber si un codigo ya esxiste
    //*********
    public boolean comprovarCodi(String codi){
        Boolean encontrado;

        String[] args = new String[]{codi};
        Cursor cursor = dbR.rawQuery("SELECT * FROM articulos WHERE codigo=?", args);

        if (cursor.moveToFirst()) {
            encontrado = true;
        }
        else {
            encontrado = false;
        }

        return encontrado;
    }

    //*****
    //Funcion que suma uno al estoc de un producto
    //*****
    public void plusArticulo (long id, String codigo, float estoque, String  fecha, float cantidad, String tipo){
        // Modifiquem els valors de las tasca amb clau primària "id"
        ContentValues valuesArticulo = new ContentValues();

        //Le sumo uno al estoc actual
        valuesArticulo.put(ARTICULO_ESTOQUE, estoque+1);

        dbW.update(TABLE_ARTICULOS, valuesArticulo, ARTICULO_ID + " = ?", new String[] { String.valueOf(id) });



        //Ponemos los valores que seran insertados en la bbdd
        ContentValues valuesMovimiento = new ContentValues();

        valuesMovimiento.put(MOVIMIENTO_CODIGO, codigo);
        valuesMovimiento.put(MOVIMIENTO_DIA, fecha);
        valuesMovimiento.put(MOVIMIENTO_CANTIDAD, cantidad);
        valuesMovimiento.put(MOVIMIENTO_TIPO, tipo);



        //Insertamos el Articulo
        dbW.insert(TABLE_MOVIMIENTOS, null, valuesMovimiento);


    }

    //*****
    //Funcion que suma uno al estoc de un producto
    //*****
    public void lessArticulo (long id, String codigo, float estoque, String  fecha, float cantidad, String tipo){
        // Modifiquem els valors de las tasca amb clau primària "id"
        ContentValues valuesArticulo = new ContentValues();

        //Le sumo uno al estoc actual
        valuesArticulo.put(ARTICULO_ESTOQUE, estoque-1);

        dbW.update(TABLE_ARTICULOS, valuesArticulo, ARTICULO_ID + " = ?", new String[] { String.valueOf(id) });



        //Ponemos los valores que seran insertados en la bbdd
        ContentValues valuesMovimiento = new ContentValues();

        valuesMovimiento.put(MOVIMIENTO_CODIGO, codigo);
        valuesMovimiento.put(MOVIMIENTO_DIA, fecha);
        valuesMovimiento.put(MOVIMIENTO_CANTIDAD, cantidad);
        valuesMovimiento.put(MOVIMIENTO_TIPO, tipo);



        //Insertamos el Articulo
        dbW.insert(TABLE_MOVIMIENTOS, null, valuesMovimiento);
    }




    //*****
    //*****
    //Funciones para ENTRA Y SALIDA DE MOVIMIENTOS
    //*****
    //*****
//    public Cursor NuevoMovimientoDeArticulo(long id)
//    {
//        // Retorna un cursor només amb el id indicat
//        // Retornem les tasques que el camp DONE = 1
//        return dbR.query(TABLE_MOVIMIENTOS, new String[] { MOVIMIENTO_ID, MOVIMIENTO_CODIGO, MOVIMIENTO_DIA ,MOVIMIENTO_CANTIDAD, MOVIMIENTO_TIPO},
//                MOVIMIENTO_ID+ "=?", new String[]{String.valueOf(id)},
//                null, null, null);
//
//    }

}
