package com.usac.clasesjava;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.usac.javastudent.Amigos;
import com.usac.javastudent.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edwin on 30/05/2016.
 */
public class TabAmigo extends Fragment {

    private ViewGroup layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View aux = inflater.inflate(R.layout.fragment_amigos, container, false);
        Button btnAdd = (Button)aux.findViewById(R.id.addButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("usuario","usario");
                Intent m = new Intent(getActivity(),Amigos.class);
                m.putExtras(b);
                startActivity(m);
            }
        });

        layout = (ViewGroup)aux.findViewById(R.id.contentFA);
        pintarAmigos();

        return  aux;
    }



    public void setAmigo(Amigo am){
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        int id = R.layout.amigos_layout;

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

        ImageView imgView = (ImageView) relativeLayout.findViewById(R.id.amiAvatar);
        imgView.setImageResource(getIdImage(am.getAvatar()));


        ImageButton imgButton = (ImageButton)relativeLayout.findViewById(R.id.amiBoton);

        imgButton.setTag(am);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amigo elim = (Amigo)v.getTag();
                preguntar(elim.getUsername());
            }
        });


        TextView textView_user = (TextView) relativeLayout.findViewById(R.id.amiUser);
        textView_user.setText(am.getUsername());

        TextView textView_exp = (TextView) relativeLayout.findViewById(R.id.amiExp);
        textView_exp.setText(""+am.getExperiencia());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        params.topMargin = 15;
        relativeLayout.setLayoutParams(params);
        layout.addView(relativeLayout);
    }

    public void preguntar(final String user){

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("¿Desea eliminar a "+user + " de sus amigos?");

        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                elimarAmigo(user);
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Toast.makeText(getApplicationContext(), "Cancelar", Toast.LENGTH_LONG).show();
            }
        });

        alert.show();
    }

    public void elimarAmigo(String username){
        layout.removeAllViews();
        repintarAmigos();
        Toast.makeText(getActivity(), "Se ha eliminado", Toast.LENGTH_LONG).show();
    }

    public int getIdImage(int imagen){
        switch (imagen){
            case 1: return R.drawable.user_boy_3;
            case 2: return R.drawable.user_man_1;
            default: return R.drawable.user_boy_3;
        }
    }

    private void repintarAmigos(){
        List<Amigo> lstAmigos = getAmigosLocal2();
        for(Amigo mod:lstAmigos){
            setAmigo(mod);
        }
    }

    private void pintarAmigos(){
        List<Amigo> lstAmigos = getAmigosLocal();
        for(Amigo mod:lstAmigos){
            setAmigo(mod);
        }
    }

    public List<Amigo> getAmigosLocal(){
        ArrayList<Amigo> amigo_ = new ArrayList<Amigo>();
        amigo_.add(new Amigo("Edwin", 100,1));
        amigo_.add(new Amigo("Raul", 1000,2));
        amigo_.add(new Amigo("Rony", 10000,1));
        amigo_.add(new Amigo("Crazy", 100000,2));
        return amigo_;
    }

    public List<Amigo> getAmigosLocal2(){
        ArrayList<Amigo> amigo_ = new ArrayList<Amigo>();
        amigo_.add(new Amigo("Edwin", 100,1));
        amigo_.add(new Amigo("Raul", 1000,2));
        return amigo_;
    }

}