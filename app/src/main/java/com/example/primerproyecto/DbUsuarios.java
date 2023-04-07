package com.example.primerproyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbUsuarios extends DbHelper{

    Context context;

    public DbUsuarios(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    // Inserta un nuevo usuario en la DB
    public long insertarUsuario(String pEmail, String pPassword){
        long id = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Almacenamos los valores en un ContentValues
            ContentValues values = new ContentValues();
            values.put("email", pEmail);
            values.put("password", pPassword);

            // Finalmente realizamos la query
            id = db.insert(T_USUARIOS, null, values);

        } catch (Exception exception){
            // Si falla imprime la excepcion
            exception.toString();
        }
        return id;
    }

    // Dado email y contrasena, verifica que el login es correcto
    public boolean esLoginCorrecto(String pEmail, String pPassword){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        boolean esCorrecto = false;

        Cursor cursor = null;

        // Utilizo parametros para evitar SQL Injection
        cursor = db.rawQuery("SELECT email FROM " + T_USUARIOS  + " WHERE email=@pEmail AND password= @pPassword", new String[]{pEmail, pPassword});

        // Si existe el email con esa contrasena devuelve True
        if(cursor.getCount() > 0){
            esCorrecto = true;
        }
        // Si no es correcto devuelve False
        cursor.close();

        return esCorrecto;
    }

    // Devuelve True si ya existe el email
    public boolean existeEmail(String pEmail){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        boolean yaExiste = false;

        Cursor cursor = null;

        // Utilizo parametros para evitar SQL Injection
        cursor = db.rawQuery("SELECT email FROM " + T_USUARIOS  + " WHERE email=@pEmail", new String[]{pEmail});

        // Si existe el email devuelve True
        if(cursor.getCount() > 0){
            yaExiste = true;
        }
        // Si no es correcto devuelve False
        cursor.close();

        return yaExiste;
    }
}
