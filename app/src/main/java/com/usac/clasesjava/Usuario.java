package com.usac.clasesjava;

/**
 * Created by Edwin on 23/05/2016.
 */
public class Usuario {

    private int id;
    private String username;
    private String email;
    private String password;
    private String nombre;
    private int experiencia;
    private int nivel;
    private int sesion;
    private int modulo;
    private int tema;

    public Usuario(int id, String username, String email, String password, String nombre, int experiencia, int nivel, int sesion, int modulo, int tema) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.experiencia = experiencia;
        this.nivel = nivel;
        this.sesion = sesion;
        this.modulo = modulo;
        this.tema = tema;
    }

    public Usuario(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getSesion() {
        return sesion;
    }

    public void setSesion(int sesion) {
        this.sesion = sesion;
    }

    public int getModulo() {
        return modulo;
    }

    public void setModulo(int moduloActual) {
        this.modulo = moduloActual;
    }

    public int getTema() {
        return tema;
    }

    public void setTema(int temaActual) {
        this.tema= temaActual;
    }
}
