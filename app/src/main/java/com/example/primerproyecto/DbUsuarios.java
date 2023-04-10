package com.example.primerproyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


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

public class DbUsuarios extends Worker{

    // Dependiendo del parametro se ejecuta una funcionalidad de las 3 disponibles en usuarios.php
    String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/jgarcia424/WEB/usuarios.php";
    public DbUsuarios(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        //Datos enviados desde InicioSesionActivity
        String username = getInputData().getString("nombre");

        //Se conecta con el servidor
        String direccion = "http://ec2-54-93-62-124.eu-central-1.compute.amazonaws.com/jgarcia424/WEB/usuarios.php";
        HttpURLConnection urlConnection = null;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            //Rellenamos los parametros
            String parametros = "nombre="+username;

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //Enviar los parametros al php
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            //Agregar esta línea para asegurarse de que los datos se envíen correctamente
            out.flush();

            int status = urlConnection.getResponseCode();
            Log.d("Prueba_Select", "Status --> " + status);

            //Si la respuesta es "200 OK" Entonces se realiza la recogida de datos
            if(status == 200){
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputStream, "UTF-8"));
                String line, result="";
                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                Log.d("Prueba_Select", "resultado --> " + result);
                inputStream.close();

                //Se parsean los datos como JSON
                JSONArray jsonArray = new JSONArray(result);
                ArrayList<String[]> lista = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i++){
                    String[] datos = {jsonArray.getJSONObject(i).getString("nombre"), jsonArray.getJSONObject(i).getString("password")};
                    lista.add(datos);
                }
                Log.d("Select_Prueba", "Usuario --> " + lista.get(0)[0]);
                String[] array = {lista.get(0)[0], lista.get(0)[1]}; //Nombre || Password

                Data data = new Data.Builder()
                        .putStringArray("array",array)
                        .build();
                return Result.success(data);
            }
        }
        catch (Exception e){
            Log.d("DAS","Error: " + e);
        }
        return Result.failure();
    }

    // Inserta un nuevo usuario en la DB
    public void insertarUsuario(String pEmail, String pPassword)  {
        try {
            HttpURLConnection urlConnection = null;

            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);


            // Creo los parametros
            String parametros = "parametro=registro" + "&email=" + pEmail + "&password=" + pPassword;

            // Necesario si se usa POST o PUT

            urlConnection.setRequestMethod("POST");



            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Para incluir los parámetros en la llamada se usa un objeto PrintWriter
            PrintWriter out = null;
            out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            // Al terminar de incluir los parámetros hay que cerrarlo
            out.close();

            // Se ejecuta la llamada al servicio web mediante:
            int statusCode = 0;
            statusCode = urlConnection.getResponseCode();

            // Si todo ha ido bien
            if (statusCode == 200) {
                BufferedInputStream inputStream = null;

                inputStream = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader bufferedReader = null;

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                String line, result = "";
                while (true) {
                    if (!((line = bufferedReader.readLine()) != null)) break;
                    // Vamos generando en la variable result el resultado final
                    result += line;
                }
                // Cierro el stream

                inputStream.close();
            }
        } catch (Exception e){

        }
    }

    // Dado email y contrasena, verifica que el login es correcto
    public boolean esLoginCorrecto(String pEmail, String pPassword)  {
        try {
            boolean esCorrecto = false;

            HttpURLConnection urlConnection = null;

            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

            // Creo los parametros
            String parametros = "parametro=identificacion" + "&email=" + pEmail + "&password=" + pPassword;

            // Necesario si se usa POST o PUT

            urlConnection.setRequestMethod("POST");

            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Para incluir los parámetros en la llamada se usa un objeto PrintWriter
            PrintWriter out = null;

            out = new PrintWriter(urlConnection.getOutputStream());

            out.print(parametros);
            // Al terminar de incluir los parámetros hay que cerrarlo
            out.close();

            // Se ejecuta la llamada al servicio web mediante:
            int statusCode = 0;

            statusCode = urlConnection.getResponseCode();

            // Si todo ha ido bien
            if (statusCode == 200) {
                BufferedInputStream inputStream = null;

                inputStream = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader bufferedReader = null;

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                String line, result = "";
                while (true) {

                    if (!((line = bufferedReader.readLine()) != null)) break;

                    // Vamos generando en la variable result el resultado final
                    result += line;
                }
                // Cierro el stream

                inputStream.close();

                // Si el usuario y contrasena son correctos
                if (result == "Bien") {
                    esCorrecto = true;
                } else {
                    esCorrecto = false;
                }
            }
            return esCorrecto;
        }
        catch (Exception e);
    }

    // Devuelve True si ya existe el email
    public boolean existeEmail(String pEmail)  {

        boolean yaExiste = false;

        HttpURLConnection urlConnection = null;
        try {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Creo los parametros
        String parametros = "parametro=existeEmail" + "&email=" + pEmail;

        // Necesario si se usa POST o PUT
        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Para incluir los parámetros en la llamada se usa un objeto PrintWriter
        PrintWriter out = null;
        try {
            out = new PrintWriter(urlConnection.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.print(parametros);
        // Al terminar de incluir los parámetros hay que cerrarlo
        out.close();

        // Se ejecuta la llamada al servicio web mediante:
        int statusCode = 0;
        try {
            statusCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Si todo ha ido bien
        if (statusCode == 200) {
            BufferedInputStream inputStream = null;
            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            String line, result = "";
            while (true) {
                try {
                    if (!((line = bufferedReader.readLine()) != null)) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Vamos generando en la variable result el resultado final
                result += line;
            }
            // Cierro el stream
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Si el usuario y contrasena son correctos
            if (result == "Ya existe el email") {
                yaExiste = true;
            } else {
                yaExiste = false;
            }
        }
        return yaExiste;
    }
}
