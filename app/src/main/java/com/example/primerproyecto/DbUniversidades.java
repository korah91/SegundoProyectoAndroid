package com.example.primerproyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbUniversidades extends DbHelper{

    Context context;

    public DbUniversidades(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    // Inserta una nueva universidad en la BD
    public long insertarUniversidad(String pNombre, int pValoracion, String pUrl){
        long id = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Almacenamos los valores en un ContentValues
            ContentValues values = new ContentValues();
            values.put("nombre", pNombre);
            values.put("valoracion", pValoracion);
            values.put("url", pUrl);

            // Finalmente realizamos la query
            id = db.insert(T_UNIVERSIDADES, null, values);
        } catch (Exception exception){
            // Si falla imprime la excepcion
            exception.toString();
        }
        return id;
    }

    // Devuelve todas las universidades en un ArrayList
    public ArrayList<Universidad> mostrarUniversidades(){

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Universidad> listaUniversidades = new ArrayList<Universidad>();
        Universidad universidad = new Universidad();
        // Se crea el cursor
        Cursor cursorUniversidad = null;

        // Voy a ir guardando en listaUniversidades todas las universidades de la DB
        cursorUniversidad = db.rawQuery("SELECT * FROM " + T_UNIVERSIDADES, null);
        if(cursorUniversidad.moveToFirst()){
            do{
                // Se crea el objeto de clase Universidad
                universidad = new Universidad();
                universidad.setId(cursorUniversidad.getInt(0));
                universidad.setNombre(cursorUniversidad.getString(1));
                universidad.setValoracion(cursorUniversidad.getInt(2));
                universidad.setUrl(cursorUniversidad.getString(3));
                // Se añade a la lista
                listaUniversidades.add(universidad);
            }
            while (cursorUniversidad.moveToNext());
        }
        cursorUniversidad.close();
        return listaUniversidades;
    }

    // Editar una universidad
    public boolean editarUniversidad(int id, String nombre, int valoracion, String url){
        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + T_UNIVERSIDADES + " SET nombre = '" + nombre + "', valoracion =" + valoracion + ", url = '" + url + "' WHERE id='" + id + "'");
            correcto = true;
        } catch (Exception ex) {
            ex.toString();
            correcto = false;
        }
        // finally se ejecuta siempre
        finally {
            db.close();
        }

        return correcto;
    }

    // Borra las universidades y vuelve a crear las 13 por defecto.
    // No he hecho un reinicio de los usuarios porque no seria util
    public void reiniciarBaseDatos(){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + T_UNIVERSIDADES);
            Toast.makeText(context, context.getString(R.string.seHaReiniciadoBD), Toast.LENGTH_SHORT).show();

            this.insertarUniversidad("UPV/EHU", 3, "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.T1dgJjAqe-WOTiPtIzApWAHaHa%26pid%3DApi&f=1&ipt=a3a28a395244771c1dc829e0ea0df73143f47b2608c64bb5806b82a0d255f990&ipo=images");
            this.insertarUniversidad("Universidad Autónoma de Madrid", 5, "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.9e96CDDUQkKpVJ1LSg9wPwHaDw%26pid%3DApi&f=1&ipt=46d5ecb7cd0719c850185991fd068b4a1953272f66d9ff83519db71244ee9266&ipo=images");
            this.insertarUniversidad("Universidad de Deusto", 0, "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.explicit.bing.net%2Fth%3Fid%3DOIP.wqYXq_WpGpaz3WsBYnhxGQHaDo%26pid%3DApi&f=1&ipt=84b7af6598e0546a81298581f9d0d6094968faab4bbfaf0a6b91173f9684991e&ipo=images");
            this.insertarUniversidad("Universidad de Granada", 4, "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.explicit.bing.net%2Fth%3Fid%3DOIP.PFsZDOyllpij0VCsN7qUDAHaHa%26pid%3DApi&f=1&ipt=4442466916175b76fdbda2d49527cb9f631c2fc425cc8e36b29aff1a1dafc535&ipo=images");
            this.insertarUniversidad("Universidad Juan Carlos", 1, "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.explicit.bing.net%2Fth%3Fid%3DOIP.NfJAF0yT3MvwIFai8G-NKQHaEH%26pid%3DApi&f=1&ipt=e08b3ff7f3a361270d1a278ef3bb10c76cbeec63516c55e5cef3c551ab4728df&ipo=images");
            this.insertarUniversidad("Universidad Alcalá Henares", 4, "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.tebeosfera.com%2FT3content%2Fimg%2FT3_entidades%2F0%2F2%2Flogoalcala02.png&f=1&nofb=1&ipt=676e3c8d5ae83a277dc39bfa4df86882e5eb228fd055f2870c6c9f7ab75594a9&ipo=images");
            this.insertarUniversidad("Universidad Alfonso X El Sabio", 5, "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fbecasoij.structuralia.com%2Fhs-fs%2Fhubfs%2Fuax.png%3Fwidth%3D570%26name%3Duax.png&f=1&nofb=1&ipt=6fc73129227d65ae326e69770fb84d34cb78105580c0f8957895d9e6ba2c0e8f&ipo=images");
            this.insertarUniversidad("Universidad de Almería", 2, "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.liferegrow.eu%2Frecursos%2Fimagenes%2FSocios%2FLogo_UAL_centrado_2_lineas_560x416.jpg&f=1&nofb=1&ipt=3377cb3be9f0e0704fa6786b7cbfb90ecd05b2e2b212c98c49b261609f410d4f&ipo=images");
            this.insertarUniversidad("Universidad de Córdoba", 1, "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fliveadapt.eu%2Fwp-content%2Fuploads%2F2018%2F11%2Flogo_uco.png&f=1&nofb=1&ipt=83048d76f3d444e3a2dd91e9216682bd2874152a80198455f8c5df1a741cd747&ipo=images");
            this.insertarUniversidad("Universidad de León", 2, "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.stonybrook.edu%2Fcommcms%2Fstudyabroad%2F_images%2Funiversity-logos%2Fleon_logo.jpg&f=1&nofb=1&ipt=67042b73fce48a48884dff8341ae21a2b10bd8f2a8211c171f4e14c69e8de3fd&ipo=images");
        } catch(Exception ex) {
            ex.toString();
            Toast.makeText(context, "No se ha podido reiniciar la base de datos", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }

}
