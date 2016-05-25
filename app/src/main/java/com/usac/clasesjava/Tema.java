package com.usac.clasesjava;

/**
 * Created by Edwin on 23/05/2016.
 */
public class Tema {
    private int idenficador;
    private int modulo;
    private String nombre;
    private String descripcion;
    private String contenido;
    private int experiencia;

    public Tema(int idenficador, int modulo, String nombre, String descripcion, String contenido, int experiencia) {
        super();
        this.idenficador = idenficador;
        this.modulo = modulo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.contenido = contenido;
        this.experiencia = experiencia;
    }

    public Tema(){}

    public int getIdenficador() {
        return idenficador;
    }

    public void setIdenficador(int idenficador) {
        this.idenficador = idenficador;
    }

    public int getModulo() {
        return modulo;
    }

    public void setModulo(int modulo) {
        this.modulo = modulo;
    }

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

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

}
