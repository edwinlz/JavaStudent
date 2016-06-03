package com.usac.javastudent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
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

        ImageButton button = (ImageButton)findViewById(R.id.verificar);
        button.setTag(currentTema);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tema aux = (Tema) v.getTag();
                preguntar(aux);
            }
        });

        web_contenido = (WebView)findViewById(R.id.conteTexto);
        web_contenido.setBackgroundColor(Color.TRANSPARENT);
        WebSettings webSettings = web_contenido.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String encabezado = "<html><head><style type=\"text/css\">@font-face {font-family: fuente;src: url('fonts/Pacifico.ttf'); }  @font-face {font-family: fuente2;src: url('fonts/FrederickatheGreat-Regular.ttf'); } @font-face {font-family: fuente3;src: url('fonts/JustAnotherHand.ttf'); }  body {font-family: fuente;font-size: 20px;text-align: justify;} h1{font-family: fuente2; font-size: 30px; text-align: center; } .nota{color: #616E14; border: solid 1px #BFD62F; background-color: #DAE691 -moz-border-radius: 6px; -webkit-border-radius: 6px; border-radius: 6px; padding: 14px 20px;} .nota:before{content:'Nota: ';}</style></head>";
        String cierre = "</html>";
        String total = encabezado+currentTema.getContenido()+cierre;
        web_contenido.loadDataWithBaseURL("file:///android_asset/",total, "text/html", "UTF-8", null);

    }

    public void preguntar(final Tema aux){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("¿Desea iniciar la prueba de conocimiento?");

        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Bundle datos = new Bundle();
                datos.putInt("tema",aux.getIdenficador());
                Intent nuevo = new Intent(Contenido.this,Preguntas.class);
                nuevo.putExtras(datos);
                startActivity(nuevo);
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }

    @Override
    protected void onPause() {
        web_contenido.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        web_contenido.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        web_contenido.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        web_contenido.destroy();
        web_contenido = null;
        super.onDestroy();
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
