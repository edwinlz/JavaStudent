package com.usac.javastudent;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.usac.clasesjava.ConexionBD;
import com.usac.clasesjava.Tema;

public class Contenido extends AppCompatActivity {

    private WebView web_contenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido);

        Bundle recibido = getIntent().getExtras();
        int tema_data = recibido.getInt("tema");

        Tema currentTema = getTema(tema_data);

        TextView titulo = (TextView)findViewById(R.id.conteTitulo);
        titulo.setText(currentTema.getNombre());

        TextView descripcion = (TextView)findViewById(R.id.conteDesc);
        descripcion.setText(currentTema.getDescripcion());

        web_contenido = (WebView)findViewById(R.id.conteTexto);
        web_contenido.setBackgroundColor(Color.TRANSPARENT);
        WebSettings webSettings = web_contenido.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String encabezado = "<html><head><style type=\"text/css\">@font-face {font-family: fuente;src: url('fonts/Pacifico.ttf'); }  @font-face {font-family: fuente2;src: url('fonts/FrederickatheGreat-Regular.ttf'); } @font-face {font-family: fuente3;src: url('fonts/JustAnotherHand.ttf'); }  body {font-family: fuente;font-size: 20px;text-align: justify;} h1{font-family: fuente2; font-size: 30px; text-align: center; } </style></head>";
        String cierre = "</html>";
        String total = encabezado+currentTema.getContenido()+cierre;
        web_contenido.loadDataWithBaseURL("file:///android_asset/",total, "text/html", "UTF-8", null);

    }

    public Tema getTema(int tema){
        Tema tema1 = new Tema();
        ConexionBD bd = new ConexionBD(this);
        bd.open();
        tema1 = bd.getTema(tema);
        bd.close();
        return tema1;
    }
}
