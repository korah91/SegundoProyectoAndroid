package com.example.primerproyecto;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Universidad> listaUniversidades;
    Context context;

    public Adapter(List<Universidad> listaUniversidades, Context context){
        this.listaUniversidades = listaUniversidades;
        this.context = context;
    }


    // El ViewHolder controla las vistas, cada one_line.xml
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_imagen;
        TextView tv_nombreUniversidad;
        RatingBar rt_valoracionUniversidad;
        ConstraintLayout parentLayout;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            // Obtengo los valores
            iv_imagen = view.findViewById(R.id.iv_urlUniversidad);
            tv_nombreUniversidad = view.findViewById(R.id.tv_nombreUniversidad);
            rt_valoracionUniversidad = (RatingBar) view.findViewById(R.id.rt_evaluacionUniversidad);
            parentLayout = view.findViewById(R.id.oneLineLayout);
        }

    }



    // Cuando se crea una vista se le aplica el layout one_line.xml
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Se utiliza el archivo xml one_line para cada vista. Este tiene imagen, titulo y un ratingBar no editable
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.one_line, viewGroup, false);

        return new ViewHolder(view);
    }

    // Se consiguen los datos de cada universidad de la BD y se ponen en cada vista
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        // Se pone el nombre de la universidad en la vista
        viewHolder.tv_nombreUniversidad.setText(""+ listaUniversidades.get(position).getNombre());
        // Se pone la valoracion
        viewHolder.rt_valoracionUniversidad.setRating( (float) listaUniversidades.get(position).getValoracion());

        // Utilizo la libreria Glide para coger imagenes de internet en una sola linea
        Glide.with(context).load(listaUniversidades.get(position).getUrl()).into(viewHolder.iv_imagen);
        Log.d("firebase", "Se ha cargado la url "+listaUniversidades.get(position).getUrl());

        // Se ejecuta cuando se da click a un elemento
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AnadirOtro.class);
                // Paso el id de la universidad para mostrarlo cuando se edite
                intent.putExtra("id", listaUniversidades.get(position).getId());
                context.startActivity(intent);

                // Es necesario el casting para no apilar las actividades al clicar en un item
                ((Activity)context).finish();

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listaUniversidades.size();
    }
}


