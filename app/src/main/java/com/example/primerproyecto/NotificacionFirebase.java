package com.example.primerproyecto;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificacionFirebase extends Worker {

    public NotificacionFirebase(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {

        // Recojo el token
        String token = getInputData().getString("token");


        String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/jgarcia424/WEB/notificacionFirebase.php";
        // Conexion con el servidor
        HttpURLConnection urlConnection = null;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);


            // Se envian los parametros
            String parametros = "token=" + token;

            Log.d("conexion", "Parametros fcm: " + parametros);

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
                return ListenableWorker.Result.success(data);
            }
        } catch (Exception e) {
            Log.d("conexion", "Error: " + e);
        }
        return ListenableWorker.Result.failure();
    }
}
