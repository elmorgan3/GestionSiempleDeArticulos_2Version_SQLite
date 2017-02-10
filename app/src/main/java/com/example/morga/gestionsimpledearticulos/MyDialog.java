package com.example.morga.gestionsimpledearticulos;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by morga on 02/02/2017.
 */

public class MyDialog
{
    public static void showInformacion(Context ctx, String Texto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setMessage(Texto);
        builder.setPositiveButton("Aceptar", null);

        builder.show();
    }

    public static void showToast(Context cts, String Texto) {
        Toast.makeText(cts, Texto, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLargo(Context cts, String Texto) {
        Toast.makeText(cts, Texto, Toast.LENGTH_LONG).show();
    }
}
