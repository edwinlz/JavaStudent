package com.usac.javastudent;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.usac.clasesjava.ConexionBD;
import com.usac.clasesjava.Estatica;

public class Resultados extends AppCompatActivity {

    public boolean aprobado = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        TextView correctas = (TextView)findViewById(R.id.resCorrectas);
        correctas.setText("Total de correctas: "+ Estatica.res_correctas);

        TextView fallidas = (TextView)findViewById(R.id.resFallidas);
        fallidas.setText("Total de fallidas: "+ Estatica.res_fallidas);

        TextView experiencia = (TextView)findViewById(R.id.expGanada);
        experiencia.setText(Estatica.exp_ganada + " EXP");


        TextView feed = (TextView)findViewById(R.id.recResultado);
        if(Estatica.res_totales > Estatica.res_correctas){
            feed.setText("Tema incompleto");
            feed.setTextColor(Color.RED);
            aprobado = false;
        }

        Button button = (Button)findViewById(R.id.buttonResultados);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Estatica.res_correctas = 0;
                Estatica.res_fallidas = 0;
                Estatica.res_totales = 0;
                int exp_actual = Estatica.usuario_actual.getExperiencia()+Estatica.exp_ganada;
                Estatica.usuario_actual.setExperiencia(exp_actual);

                if(aprobado){
                    int tema_actual = Estatica.tema_actual;
                    Estatica.usuario_actual.setTema(tema_actual);

                    Estatica.datos.setearDatos(Estatica.username_actual,exp_actual,Estatica.modulo_actual,Estatica.modulo_actual,tema_actual,getApplicationContext());
                    setearDatosLocal(exp_actual,Estatica.modulo_actual,tema_actual);
                }else{
                    Estatica.datos.setearDatos(Estatica.username_actual,exp_actual,Estatica.modulo_actual,Estatica.modulo_actual,Estatica.tema_actual,getApplicationContext());
                    setearDatosLocal(exp_actual,Estatica.modulo_actual,Estatica.tema_actual);
                }
                Estatica.exp_ganada =0;
                //Bundle datos = new Bundle();
                //datos.putInt("modulo",Estatica.modulo_actual);
                Intent nuevo = new Intent(Resultados.this,Principal.class);
                //nuevo.putExtras(datos);
                startActivity(nuevo);


            }
        });


    }

    @Override
    public void onBackPressed() {

        return;
    }

    public void setearDatosLocal(int exp, int mod, int tem){
        ConexionBD bd = new ConexionBD(this);
        bd.open();

        bd.updateUsuarioExperiencia(Estatica.username_actual,exp);
        bd.updateUsuarioModulo(Estatica.username_actual,mod);
        bd.updateUsuarioTema(Estatica.username_actual,tem);

        bd.close();
    }
}
