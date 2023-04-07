package com.example.primerproyecto;

import java.util.Comparator;

public class Universidad {
    private int id;
    private String nombre;
    private int valoracion;
    private String url;

    public String toString() {
        return "Universidad{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", valoracion=" + valoracion +
                ", url='" + url + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // Comparador para nombre de A a Z
    public static Comparator<Universidad> UniversidadNameAZComparator = new Comparator<Universidad>() {
        @Override
        public int compare(Universidad u1, Universidad u2) {
            return u1.getNombre().compareTo(u2.getNombre());
        }
    };
    // Comparador para nombre de Z a A
    public static Comparator<Universidad> UniversidadNameZAComparator = new Comparator<Universidad>() {
        @Override
        public int compare(Universidad u1, Universidad u2) {
            return u2.getNombre().compareTo(u1.getNombre());
        }
    };

    // Comparador para valoracion ascendente
    public static Comparator<Universidad> UniversidadValoracionASCComparator = new Comparator<Universidad>() {
        @Override
        public int compare(Universidad u1, Universidad p2) {
            return u1.getValoracion() - p2.getValoracion();
        }
    };

    // Comparador para valoracion descendente
    public static Comparator<Universidad> UniversidadValoracionDESCComparator = new Comparator<Universidad>() {
        @Override
        public int compare(Universidad u1, Universidad u2) {
            return u2.getValoracion() - u1.getValoracion();
        }
    };

}
