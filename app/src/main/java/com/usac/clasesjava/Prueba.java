package com.usac.clasesjava;

import java.util.List;

/**
 * Created by Edwin on 23/05/2016.
 */
public class Prueba {
    public int id;
    public int tema;
    public String pregunta;
    public List<Respuesta> respuestas;
    public int experiencia;
    public int tipo;
    public int correcto; /*id respuesta correcta*/

    public Prueba(int id, int tema, String pregunta, List<Respuesta> respuestas, int experiencia, int tipo, int correcto ) {
        this.id = id;
        this.tema = tema;
        this.pregunta = pregunta;
        this.respuestas = respuestas;
        this.experiencia = experiencia;
        this.tipo = tipo;
        this.correcto = correcto;
    }

    public Prueba(){}
}
