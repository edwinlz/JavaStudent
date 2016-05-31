package com.usac.javastudent;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Contenido extends AppCompatActivity {

    private WebView web_contenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido);
        web_contenido = (WebView)findViewById(R.id.conteTexto);
        web_contenido.setBackgroundColor(Color.TRANSPARENT);
        WebSettings webSettings = web_contenido.getSettings();
        webSettings.setJavaScriptEnabled(true);


        String encabezado = "<html><head><style type=\"text/css\">@font-face {font-family: fuente;src: url('fonts/Pacifico.ttf'); }  @font-face {font-family: fuente2;src: url('fonts/FrederickatheGreat-Regular.ttf'); } @font-face {font-family: fuente3;src: url('fonts/JustAnotherHand.ttf'); }  body {font-family: fuente;font-size: 20px;text-align: justify;} h1{font-family: fuente2; font-size: 30px; text-align: center; } </style></head><body>";
        String cuerpo = "<h1>El lenguaje Java</h1><p> Como cualquier lenguaje de programación, el lenguaje <strong>Java</strong> tiene su propia estructura, reglas de sintaxis y paradigma de programación. El paradigma de programación del lenguaje Java se basa en el concepto de programación orientada a objetos (OOP) El lenguaje Java es un derivado del lenguaje C, por lo que sus reglas de sintaxis se parecen mucho</p>"+"<h1>El lenguaje Java</h1><p> Como cualquier lenguaje de programación, el lenguaje <strong>Java</strong> tiene su propia estructura, reglas de sintaxis y paradigma de programación. El paradigma de programación del lenguaje Java se basa en el concepto de programación orientada a objetos (OOP) El lenguaje Java es un derivado del lenguaje C, por lo que sus reglas de sintaxis se parecen mucho</p><img width=\"100%\" src=\"http://www.dibol.de/vm/jrejvm.png\"></img> <a href='https://portal.ingenieria.usac.edu.gt/'>ir</a> <iframe width=\"100%\" height=\"300px\" src=\"https://www.youtube.com/embed/1l7nDIVReBU\" frameborder=\"0\" allowfullscreen></iframe>";
        String cierre = "</body></html>";
        String total = encabezado+cuerpo+cierre;

        web_contenido.loadDataWithBaseURL("file:///android_asset/",total, "text/html", "UTF-8", null);
    }
}
