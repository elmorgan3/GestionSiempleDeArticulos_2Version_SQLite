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

    public static final String TABLE_NAME = "articulos";
    public static final String ARTICULO_ID = "_id";
    public static final String ARTICULO_CODIGO = "codigo";
    public static final String ARTICULO_DESCRIPCION = "descripcion";
    public static final String ARTICULO_PVP = "pvp";
    public static final String ARTICULO_ESTOQUE = "estoque";

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
    public long insert (String codigo, String descripcion, float pvp, float estoque)
    {
        //Ponemos los valores que seran insertados en la bbdd
        ContentValues values = new ContentValues();

        values.put(ARTICULO_CODIGO, codigo);
        values.put(ARTICULO_DESCRIPCION, descripcion);
        values.put(ARTICULO_PVP, pvp);
        values.put(ARTICULO_ESTOQUE,estoque);

        //Insertamos el Articulo
        return dbW.insert(TABLE_NAME, null, values);
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

        dbW.update(TABLE_NAME,values, ARTICULO_ID + " = ?", new String[] { String.valueOf(id) });
    }

    //*********
    //Funcion para eliminar un Articulo con la PK id
    //*********
    public void delete(long id)
    {
        // Eliminem la Articulo amb clau primària "id"
        dbW.delete(TABLE_NAME, ARTICULO_ID + " = ?", new String[] { String.valueOf(id) });
    }



    //**********
    // Funciion que retorna todos los elementos la bbdd en un cursor
    //**********
    public Cursor getAllArticulos()
    {
        return dbR.query(TABLE_NAME, new String[] {ARTICULO_ID, ARTICULO_CODIGO, ARTICULO_DESCRIPCION, ARTICULO_PVP, ARTICULO_ESTOQUE},
                null, null,null,null, ARTICULO_ID);
    }

    //**********
    // Funciion que retorna solo el id pedido
    //**********
    public Cursor articulo(long id)
    {
        // Retorna un cursor només amb el id indicat
        // Retornem les tasques que el camp DONE = 1
        return dbR.query(TABLE_NAME, new String[] { ARTICULO_ID, ARTICULO_CODIGO, ARTICULO_DESCRIPCION,ARTICULO_PVP, ARTICULO_ESTOQUE},
                ARTICULO_ID+ "=?", new String[]{String.valueOf(id)},
                null, null, null);

    }



//    public Cursor comprobarCodigo(String codigo){
//            return dbR.query(TABLE_NAME, new String[] { "COUNT(*)"},
//                    ARTICULO_CODIGO + "=?", new String[]{String.valueOf(codigo)},
//                    null, null, null);
//
//    }

    //*********
    //Funcion que devuelve un boolean para saber si un codigo ya esxiste
    //*********
    public boolean comprovarCodi(String codi){
        Boolean encontrado;

        String[] args = new String[]{codi};
        Cursor cursor = dbR.rawQuery("SELECT * FROM articulos WHERE codigo=?", args);

        if (cursor.moveToFirst())
        {
            encontrado = true;
        }
        else
        {
            encontrado = false;
        }

        return encontrado;
    }
}
