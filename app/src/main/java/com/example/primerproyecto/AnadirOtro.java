package com.example.primerproyecto;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class AnadirOtro extends AppCompatActivity {
    Button btn_ok, btn_cancel;
    ImageButton btn_imagen;
    List<Universidad> listaUniversidades;
    RatingBar rt_valoracionUniversidad;
    EditText et_nombreUniversidad, et_urlUniversidad;
    TextView tv_idUniversidad;
    ImageView iv_imagen;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_otro);

        // Obtenemos el objeto DB para universidades
        DbUniversidades dbUniversidades = new DbUniversidades(this);

        // Obtengo la lista de universidades de la BD; cargo BD en objetos
        listaUniversidades = dbUniversidades.mostrarUniversidades();

        // Botones para guardar y cancelar
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_imagen = findViewById(R.id.btn_imagen);
        rt_valoracionUniversidad = findViewById(R.id.rt_valoracionUniversidad);
        et_nombreUniversidad = findViewById(R.id.et_nombreUniversidad);
        et_urlUniversidad = findViewById(R.id.et_urlUniversidad);
        tv_idUniversidad = findViewById(R.id.tv_idUniversidad);
        iv_imagen = findViewById(R.id.iv_imagen);
        Intent intent = getIntent();
        // Si el intent no da un numero, devuelve -1
        // Si es -1, se presume que se esta creando un nuevo item
        id = intent.getIntExtra("id", -1);
        Universidad universidad = null;

        // Cargo el logo para acceder a internet con Glide.
        // Si no lo hago con Glide en API 26 salta un error diciendo que el bitmap es muy grande
        Glide.with(this).load(R.drawable.internetlogo).into(btn_imagen);

        // Se ha llegado a esta actividad para editar un item
        if (id >= 0){

            for(Universidad p: listaUniversidades){
                if (p.getId() == id){
                    // Si coincide el id se obtiene el item
                    universidad = p;
                }
            }
            // Se obtienen los datos del item
            et_nombreUniversidad.setText(universidad.getNombre());
            rt_valoracionUniversidad.setRating(universidad.getValoracion());
            et_urlUniversidad.setText(universidad.getUrl());
            tv_idUniversidad.setText(String.valueOf(id));

            // Utilizo la libreria Glide para coger imagenes de internet en una sola linea
            Glide.with(this).load(universidad.getUrl()).into(iv_imagen);

        }
        // Se ha llegado a esta actividad para crear un nuevo item
        else{
            tv_idUniversidad.setText("No creado");

        }


        // Cuando se pulsa el boton confirmar
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // El boton solo funciona si se ha escrito el nombre
                if(et_nombreUniversidad.getText().toString().trim().length() > 0){
                    String nombre = et_nombreUniversidad.getText().toString();
                    int valoracion = Math.round(rt_valoracionUniversidad.getRating());
                    String url = et_urlUniversidad.getText().toString();

                    if (id >= 0){
                        // Actualizo la universidad
                        dbUniversidades.editarUniversidad(id, nombre, valoracion, url);
                    }
                    else {
                        // Añado a la lista la universidad
                        dbUniversidades.insertarUniversidad(nombre, valoracion, url);
                    }


                    Intent intent = new Intent(AnadirOtro.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                // Si falta el nombre o la valoracion, se muestra un Toast
                else{
                    Toast.makeText(AnadirOtro.this, "¡Debes introducir nombre y valoracion!", Toast.LENGTH_LONG).show();
                }

            }
        });

        // Cuando se pulsa el boton cancelar se vuelve al menu principal
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnadirOtro.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Cuando se pulsa en el boton al lado de la url se abre el navegador con la direccion de la imagen
        btn_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(et_urlUniversidad.getText().toString()));
                startActivity(i);
            }
        });

    }

    // Cuando se pulsa el boton "back" se vuelve a la lista de universidades y no se sale de la aplicacion
    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}