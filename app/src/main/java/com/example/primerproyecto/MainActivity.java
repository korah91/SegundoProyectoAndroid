package com.example.primerproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LOG";


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    Button btn_anadir, btn_reset;
    List<Universidad> listaUniversidades;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtenemos el BD Helper
        DbHelper dbHelper = new DbHelper(MainActivity.this);
        // Le indicamos que se va a escribir sobre la BD
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Obtenemos el objeto DB para universidades
        DbUniversidades dbUniversidades = new DbUniversidades(this);

        // Obtengo la lista de universidades de la BD; cargo BD en objetos
        listaUniversidades = dbUniversidades.mostrarUniversidades();

        // Log que imprime todas las universidades
        Log.d(TAG, "onCreate: "+ listaUniversidades.toString());
        Toast.makeText(this, "Número de universidades: "+ listaUniversidades.size(), Toast.LENGTH_SHORT).show();

        // Boton para anadir una nueva universidad
        btn_anadir = findViewById(R.id.btn_anadir);
        btn_anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AnadirOtro.class);
                startActivity(intent);
                finish();
            }
        });

        // Boton para resetear la base de datos y anadir universidades por defecto
        btn_reset = findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Se crea un dialog para confirmar el reseteo
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.aviso));
                builder.setMessage(getString(R.string.dialog_resetBD));

                // Creo el boton para confirmar que se quiere reiniciar la BD
                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbUniversidades.reiniciarBaseDatos();

                        // Se han borrado los items, asi que cargo una lista vacia en el Adapter del RecyclerView
                        listaUniversidades = dbUniversidades.mostrarUniversidades();
                        mAdapter = new Adapter(listaUniversidades, MainActivity.this);
                        recyclerView.setAdapter(mAdapter);
                        }
                    });

                // Creo el boton para cancelar
                builder.setNegativeButton(getString(R.string.cancel),null);

                builder.show();
            }
        });



        // Obtengo el recyclerview
        recyclerView = findViewById(R.id.idRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Uso el Adapter
        mAdapter = new Adapter(listaUniversidades, MainActivity.this);
        recyclerView.setAdapter(mAdapter);

        // Cuando entro en la actividad principal se crea una notificacion para recordar al usuario que puntue
        notificacion();


        // Conocer el token FCM del dispositivo
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult();
                    }
                });

    }




    // Funcion para el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Obtengo el layout sort_menu.xml
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    // Funcion para el menu que trata el clickEvent
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Trato el click a cada item del menu
        switch (item.getItemId()){
            case R.id.menu_AZ:
                // ordenar de A a Z
                // Ordeno la lista de universidades utilizando un comparador diferente para cada opcion
                Collections.sort(listaUniversidades, Universidad.UniversidadNameAZComparator);
                Toast.makeText(MainActivity.this, "Ordenado de A a Z", Toast.LENGTH_SHORT).show();

                // Le aviso al adaptador que los datos de las vistas se han cambiado para que se actualice.
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_ZA:
                // ordenar de Z a A
                Collections.sort(listaUniversidades, Universidad.UniversidadNameZAComparator);
                Toast.makeText(MainActivity.this, "Ordenado de Z a A", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_dateASC:
                // ordenar por Date asc
                Collections.sort(listaUniversidades, Universidad.UniversidadValoracionASCComparator);
                Toast.makeText(MainActivity.this, "Ordenado de Valoracion ASC", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_dateDESC:
                // ordenar por Date desc
                Collections.sort(listaUniversidades, Universidad.UniversidadValoracionDESCComparator);
                Toast.makeText(MainActivity.this, "Ordenado de Valoracion DESC", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_changeLang:
                // cambiar idioma
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.eligeIdioma));
                CharSequence[] opciones = {"Castellano", "English"};
                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Locale locale;
                        Resources res;
                        DisplayMetrics dm;
                        Configuration conf;
                        Intent refresh;
                        switch (i){
                            // Español
                            case 0:
                                locale = new Locale("es-rEs");
                                res = getResources();
                                dm = res.getDisplayMetrics();
                                conf = res.getConfiguration();
                                conf.locale = locale;
                                res.updateConfiguration(conf, dm);

                                // Se reinicia la actividad con un nuevo nombre
                                refresh = new Intent(MainActivity.this, MainActivity.class);

                                // Se crea un toast
                                Toast.makeText(MainActivity.this, "Castellano", Toast.LENGTH_SHORT).show();
                                startActivity(refresh);
                                finish();

                                break;
                            case 1:
                                locale = new Locale("en");
                                res = getResources();
                                dm = res.getDisplayMetrics();
                                conf = res.getConfiguration();
                                conf.locale = locale;
                                res.updateConfiguration(conf, dm);

                                // Se reinicia la actividad con un nuevo nombre
                                refresh = new Intent(MainActivity.this, MainActivity.class);

                                // Se crea un toast
                                Toast.makeText(MainActivity.this, "English", Toast.LENGTH_SHORT).show();


                                startActivity(refresh);
                                finish();
                                break;

                        }

                    }
                });
                builder.show();

                mAdapter.notifyDataSetChanged();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    // Crea la notificacion
    public void notificacion(){

        // Pido permisos, se ejecuta solo cuando la version de API es 33
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)!=
                    PackageManager.PERMISSION_GRANTED) {
                        //PEDIR EL PERMISO
                        ActivityCompat.requestPermissions(this, new
                        String[]{Manifest.permission.POST_NOTIFICATIONS}, 11);
            }
        }
        NotificationManager elManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "IdCanal");

        // Necesario para API mayor o igual que la de la version Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("IdCanal", "canal", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("esElCanal");
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                    .setContentTitle("Recordatorio")
                    .setContentText("'Recuerda votar! Tu opinión nos es útil.")
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true);


            elManager.notify(1, elBuilder.build());
        }
    }
    // Cuando se pulsa el boton "back" se vuelve al login y no se cierra la aplicacion
    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
    }



}