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

public class SignUp extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_signup, btn_cambiarIdioma, btn_guest;
    TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_signup = findViewById(R.id.btn_signup);
        tv_login = findViewById(R.id.tv_login);
        btn_cambiarIdioma = findViewById(R.id.btn_cambiarIdioma);
        btn_guest = findViewById(R.id.btn_guest);

        // Cuando se pulsa el boton para registrar
        // Primero se comprueba que no esten vacios los campos
        // Luego se comprueba el login con la BD
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this, MainActivity.class);

                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                // Si el email esta vacio o no es un email valido
                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignUp.this, getString(R.string.debesIntroducirEmail), Toast.LENGTH_SHORT).show();
                }
                // Si la contrasena esta vacia
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUp.this, getString(R.string.debesIntroducirContraseña), Toast.LENGTH_SHORT).show();
                }

                // Si se han introducido email y contrasena
                else {
                    // Se comprueba que el email no exista ya
                    Data data = new Data.Builder()
                            .putString("email", email)
                            .putString("parametro", "existeEmail")
                            .putString("password", password).build();


                    OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(DbUsuarios.class)
                    .setInputData(data).build();
                    WorkManager.getInstance(SignUp.this).enqueue(otwr);
                    WorkManager.getInstance(SignUp.this).getWorkInfoByIdLiveData(otwr.getId()).observe(SignUp.this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(@Nullable WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState().isFinished()) {
                                String resultado = workInfo.getOutputData().getString("result");
                                // Si el php devuelve que ya existe el email
                                Log.d("conexion", "resultado: " + resultado);
                                if (resultado.equals("Existe")){
                                    Toast.makeText(SignUp.this, "El usuario ya existe", Toast.LENGTH_LONG).show();
                                }
                                // Si el php devuelve que no existe el email se registra
                                else{
                                    Data data = new Data.Builder()
                                    .putString("email", email)
                                    .putString("parametro", "registro")
                                    .putString("password", password).build();

                                    OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(DbUsuarios.class)
                                            .setInputData(data)
                                            .build();
                                    WorkManager.getInstance(SignUp.this).enqueue(otwr);
                                    // Paso el email
                                    i.putExtra("email", email);
                                    startActivity(i);
                                    // Termino esta actividad
                                    finish();
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
                Intent i = new Intent(SignUp.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        // Cuando se pulsa el boton de cambiar idioma se muestra un dialog con los idiomas disponibles
        btn_cambiarIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // cambiar idioma
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
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
                        switch (i) {
                            // Español
                            case 0:
                                locale = new Locale("es-rEs");
                                res = getResources();
                                dm = res.getDisplayMetrics();
                                conf = res.getConfiguration();
                                conf.locale = locale;
                                res.updateConfiguration(conf, dm);

                                // Se reinicia la actividad con un nuevo nombre
                                refresh = new Intent(SignUp.this, SignUp.class);

                                // Se crea un toast
                                Toast.makeText(SignUp.this, "Castellano", Toast.LENGTH_SHORT).show();
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
                                refresh = new Intent(SignUp.this, SignUp.class);

                                // Se crea un toast
                                Toast.makeText(SignUp.this, "English", Toast.LENGTH_SHORT).show();


                                startActivity(refresh);
                                finish();
                                break;

                        }

                    }
                });
                builder.show();
            }
        });
    }

    // Cuando se le da click a ¿Ya estas registrado? se navega al login
    public void onClickLogin(View view){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
    }
}