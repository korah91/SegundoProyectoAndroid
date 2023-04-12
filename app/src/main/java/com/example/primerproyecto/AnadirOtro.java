package com.example.primerproyecto;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AnadirOtro extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    String currentPhotoPath;

    Button btn_ok, btn_cancel;
    ImageButton btn_imagen, btn_camara;
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
        btn_camara = findViewById(R.id.btn_camara);
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


        /*
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            Bitmap bitmap;
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    Uri uri = data.getData();

                    // Convertimos a BitMap
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        // Cambiamos la imagen mostrada
                        iv_imagen.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        */

        // Cuando se pulsa el botón de la camara se abre la camara para capturar el logo
        btn_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                askCameraPermissions();
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

    private void askCameraPermissions(){
        // Si no hay permisos de CAMARA y ALMACENAMIENTO, se piden
        if(ContextCompat.checkSelfPermission(AnadirOtro.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(AnadirOtro.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AnadirOtro.this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERM_CODE);
        }
        else{
            dispatchTakePictureIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Se necesita permiso para acceder a la camara", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Cuando se recibe la imagen desde la camara
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                iv_imagen.setImageURI(Uri.fromFile(f));
                Log.d("imagen", "URL Absoluta de la imagen: " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

            }
        }
    }

    private File createImageFile() throws IOException {
        // Se crea la imagen con un TimeStamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


}