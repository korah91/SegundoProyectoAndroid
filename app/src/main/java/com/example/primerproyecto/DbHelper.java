package com.example.primerproyecto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "universidades.db";
    public static final String T_UNIVERSIDADES = "t_universidades";
    public static final String T_USUARIOS = "t_usuarios";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    // Crea la base de datos
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Creamos la tabla de las universidades
        sqLiteDatabase.execSQL("CREATE TABLE " + T_UNIVERSIDADES + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "valoracion INTEGER NOT NULL," +
                "url TEXT" + ")");
        // Creamos la tabla de los usuarios
        sqLiteDatabase.execSQL("CREATE TABLE " + T_USUARIOS + "(" +
                "email TEXT PRIMARY KEY," +
                "password TEXT NOT NULL" + ")");
    }

    // No me ha sido necesario utilizar este metodo, sirve para actualizar la BD
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
