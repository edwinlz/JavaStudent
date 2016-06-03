package com.usac.javastudent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.usac.clasesjava.Estatica;

public class Estadisticas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView exp = (TextView)findViewById(R.id.expStats);
        exp.setText("Experiencia acumulada: "+ Estatica.usuario_actual.getExperiencia());

        ImageView image = (ImageView)findViewById(R.id.imageView5);
        image.setImageResource(getIdImage(Estatica.usuario_actual.getImagen()));

        TextView niv = (TextView)findViewById(R.id.nivStats);
        niv.setText("Nivel Acual: "+Estatica.usuario_actual.getModulo());

        TextView user = (TextView)findViewById(R.id.userStats);
        user.setText(Estatica.usuario_actual.getUsername());

        int progreso = 300/(Estatica.usuario_actual.getExperiencia()+1);
        ProgressBar barra = (ProgressBar)findViewById(R.id.progressBar);
        barra.setProgress(progreso);

    }
    public int getIdImage(int imagen){
        switch (imagen){
            case 1: return R.drawable.user_boy_3;
            case 2: return R.drawable.user_man_1;
            case 3: return R.drawable.user_man_2;
            case 4: return R.drawable.user_girl_1;
            case 5: return R.drawable.user_girl_2;
            case 6: return R.drawable.user_girl_3;
            default: return R.drawable.user_boy_3;
        }
    }


}
