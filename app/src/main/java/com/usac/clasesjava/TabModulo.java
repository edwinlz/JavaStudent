package com.usac.clasesjava;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.usac.javastudent.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edwin on 30/05/2016.
 */
public class TabModulo extends Fragment {

    private ViewGroup layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){

        View aux = inflater.inflate(R.layout.fragment_modulos, container, false);
        layout = (ViewGroup)aux.findViewById(R.id.contentFM);
        pintarModulos();
        return aux;
    }

    private void pintarModulos(){
        ConexionBD conection = new ConexionBD(getActivity());
        conection.open();
        List<Modulo> lstModulos = conection.getModulos();
        for(Modulo mod:lstModulos){
            setModulo(mod);
        }
    }

    public void setModulo(Modulo mod){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        int id = R.layout.modulos_layout;

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

        ImageButton imgButton = (ImageButton)relativeLayout.findViewById(R.id.imageButton);
        imgButton.setImageResource(getIdImage(mod.getIdentificador()));
        imgButton.setTag(mod);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modulo aux = (Modulo)v.getTag();
                if(Estatica.usuario_actual.getExperiencia()<aux.getExperiencia()){
                    Toast.makeText(getActivity(),"No tiene la experiencia suficiente",Toast.LENGTH_SHORT).show();
                }else{

                    Bundle datos = new Bundle();
                    datos.putInt("modulo",aux.getIdentificador());
                    Estatica.modulo_actual = aux.getIdentificador();
                    Intent intent = new Intent(getActivity(), Temas.class);
                    intent.putExtras(datos);
                    startActivity(intent);
                }
            }
        });

        TextView textView_titulo = (TextView) relativeLayout.findViewById(R.id.amiUser);
        textView_titulo.setText(mod.getNombre());

        TextView textView_temas = (TextView) relativeLayout.findViewById(R.id.modTemas);
        textView_temas.setText(getModulosTerminados(mod)+"/"+mod.getTemas());


        TextView textView_estado = (TextView) relativeLayout.findViewById(R.id.modEstado);
        if(Estatica.usuario_actual.getExperiencia()<mod.getExperiencia()){
            textView_estado.setText("Bloqueado");
            textView_estado.setTextColor(Color.RED);
        }else if(Estatica.usuario_actual.getModulo()>mod.getIdentificador()){
            textView_estado.setText("Terminado");
            textView_estado.setTextColor(Color.GRAY);
        }


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        params.topMargin = 15;
        relativeLayout.setLayoutParams(params);
        layout.addView(relativeLayout);
    }

    public int getIdImage(int imagen){
        switch (imagen){
            case 1: return R.drawable.learning;
            case 2: return R.drawable.modulo_1;
            case 3: return R.drawable.modulo_3;
            default: return R.drawable.learning;
        }
    }


    public int getModulosTerminados(Modulo modulo){
        int temaTerminado = 0;

        if(Estatica.usuario_actual.getModulo() == modulo.getIdentificador()){
            temaTerminado = Estatica.usuario_actual.getTema();
        }else if(Estatica.usuario_actual.getModulo()<modulo.getIdentificador()){
            temaTerminado = 0;
        }else if(Estatica.usuario_actual.getModulo() > modulo.getIdentificador()){
            temaTerminado = modulo.getTemas();
        }

        return temaTerminado;
    }

}
