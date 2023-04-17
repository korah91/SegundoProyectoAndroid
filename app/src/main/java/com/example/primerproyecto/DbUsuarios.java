package com.example.primerproyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.annotation.NonNull;
import androidx.work.Data;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class DbUsuarios extends Worker {

    // Dependiendo del parametro se ejecuta una funcionalidad de las 3 disponibles en usuarios.php
    String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/jgarcia424/WEB/usuarios.php";

    public DbUsuarios(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        // Recojo los datos. Siempre se llama a este metodo con un parametro que indica que funcion se va a realizar
        String email = getInputData().getString("email");
        String parametro = getInputData().getString("parametro");
        String password = getInputData().getString("password");


        String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/jgarcia424/WEB/usuarios.php";
        // Conexion con el servidor
        HttpURLConnection urlConnection = null;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);


            // Se envian los parametros
            String parametros = "email=" + email + "&parametro=" + parametro + "&password=" + password;

            Log.d("conexion", "Parametros: " + parametros);

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.flush();

            // Se obtiene la respuesta
            int status = urlConnection.getResponseCode();
            Log.d("conexion", "Codigo Respuesta " + status);
            Log.d("conexion", "Respuesta:" + urlConnection.getResponseMessage());


            // En caso de obtener 200
            if (status == 200) {
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();

                Data data = new Data.Builder()
                        .putString("result", result)
                        .build();
                // Se devuelve la respuesta
                return Result.success(data);
            }
        } catch (Exception e) {
            Log.d("conexion", "Error: " + e);
        }
        return Result.failure();
    }
}
