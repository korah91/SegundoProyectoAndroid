package com.example.primerproyecto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Login extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_login, btn_guest, btn_cambiarIdioma;
    TextView tv_signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        tv_signup = findViewById(R.id.tv_login);
        btn_guest = findViewById(R.id.btn_guest);
        btn_cambiarIdioma = findViewById(R.id.btn_cambiarIdioma);


        // Cuando se pulsa el boton para iniciar sesion
        // Primero se comprueba que no esten vacios los campos
        // Luego se comprueba el login con la BD
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, MainActivity.class);

                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                // Si el email esta vacio o no es un email valido
                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(Login.this, getString(R.string.debesIntroducirEmail), Toast.LENGTH_SHORT).show();
                }
                // Si la contrasena esta vacia
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, getString(R.string.debesIntroducirContraseña), Toast.LENGTH_SHORT).show();
                }

                // Si se han introducido email y contrasena
                else {
                    // Se realiza la identificacion
                    Data data = new Data.Builder()
                            .putString("email", email)
                            .putString("parametro", "identificacion")
                            .putString("password", password).build();

                    OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(DbUsuarios.class)
                            .setInputData(data).build();
                    WorkManager.getInstance(Login.this).enqueue(otwr);
                    WorkManager.getInstance(Login.this).getWorkInfoByIdLiveData(otwr.getId()).observe(Login.this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState().isFinished()) {
                                String resultado = workInfo.getOutputData().getString("result");
                                // Si el php devuelve que se ha identificado CORRECTAMENTE
                                if (resultado.equals("Bien")) {
                                    // Paso el email
                                    i.putExtra("email", email);
                                    //i.putExtra("password", password);
                                    startActivity(i);
                                    // Termino esta actividad
                                    finish();
                                }
                                // Si el php devuelve que NO COINCIDEN usuario y contrasena se muestra un mensaje
                                else {
                                    Toast.makeText(Login.this, getString(R.string.loginFallido), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });

        // Boton para acceder como Invitado
        btn_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Cuando se pulsa el boton de cambiar idioma se muestra un dialog con los idiomas disponibles
        btn_cambiarIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // cambiar idioma
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
                                refresh = new Intent(Login.this, Login.class);

                                // Se crea un toast
                                Toast.makeText(Login.this, "Castellano", Toast.LENGTH_SHORT).show();
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
                                refresh = new Intent(Login.this, Login.class);

                                // Se crea un toast
                                Toast.makeText(Login.this, "English", Toast.LENGTH_SHORT).show();


                                startActivity(refresh);
                                finish();
                                break;

                        }

                    }
                });
                // Se muestra el builder
                builder.show();
            }
        });

    }

    // Cuando se le da click a ¿No estas registrado? se navega al registro
    public void onClickSignUp(View view){
        Intent i = new Intent(this, SignUp.class);
        startActivity(i);
        finish();
    }




}