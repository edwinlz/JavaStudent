package com.usac.clasesjava;

/**
 * Created by Edwin on 23/05/2016.
 */
public class Modulo {
    private int identificador;
    private String nombre;
    private String descripcion;
    private int temas; /*numero de temas*/
    private int experiencia; /*experiencia necesaria*/

    public Modulo(int indentificador, String nombre, String descripcion, int temas, int experiencia) {
        super();
        this.identificador = indentificador;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.temas = temas;
        this.experiencia = experiencia;
    }

    public Modulo(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTemas() {
        return temas;
    }

    public void setTemas(int temas) {
        this.temas = temas;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }


}
