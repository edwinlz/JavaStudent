package com.usac.clasesjava;

/**
 * Created by Edwin on 24/05/2016.
 */
public class Respuesta {
    private int identificador;
    private int prueba;
    private String respuesta;

    public Respuesta(int identificador, int prueba, String respuesta) {
        this.identificador = identificador;
        this.prueba = prueba;
        this.respuesta = respuesta;
    }

    public Respuesta(){}

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public int getPrueba() {
        return prueba;
    }

    public void setPrueba(int prueba) {
        this.prueba = prueba;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
