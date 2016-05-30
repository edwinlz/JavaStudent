package com.usac.clasesjava;

/**
 * Created by Edwin on 29/05/2016.
 */
public class Amigo {
    private String username;
    private int experiencia;
    private int avatar;

    public Amigo(String username, int experiencia, int avatar){
        this.username = username;
        this.experiencia = experiencia;
        this.avatar = avatar;
    }

    public Amigo(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}
