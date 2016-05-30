package com.usac.clasesjava;
import android.app.ActionBar;
import android.content.Intent;
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
        List<Modulo> lstModulos = getModuloLocal();
        for(Modulo mod:lstModulos){
            setModulo(mod);
        }
    }

    public List<Modulo> getModuloLocal(){

        ArrayList<Modulo> modulos_ = new ArrayList<Modulo>();
        modulos_.add(new Modulo(1,"Modulo 1", "Introduccion a Java", 5,0));
        modulos_.add(new Modulo(1,"Modulo 2", "Conceptos basicos de Java", 5,0));
        modulos_.add(new Modulo(1,"Modulo 3", "Programacion Orientada a Objetos", 5,0));

        return modulos_;
    }

    public void setModulo(Modulo mod){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        int id = R.layout.modulos_layout;

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

        ImageButton imgButton = (ImageButton)relativeLayout.findViewById(R.id.imageButton);
        imgButton.setTag(mod);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modulo aux = (Modulo)v.getTag();
                Toast.makeText(getActivity(),"Hola",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), Prueba.class);
                startActivity(intent);
            }
        });

        TextView textView_titulo = (TextView) relativeLayout.findViewById(R.id.amiUser);
        textView_titulo.setText(mod.getNombre());

        //TextView textView_desc = (TextView) relativeLayout.findViewById(R.id.modDesc);
        //textView_desc.setText(mod.getDescripcion());

        TextView textView_temas = (TextView) relativeLayout.findViewById(R.id.modTemas);
        textView_temas.setText("0/"+mod.getTemas());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        params.topMargin = 15;
        relativeLayout.setLayoutParams(params);
        layout.addView(relativeLayout);
    }

}
