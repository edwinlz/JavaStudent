package com.usac.javastudent;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.usac.clasesjava.Amigo;
import com.usac.clasesjava.ConexionBD;
import com.usac.clasesjava.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Amigos extends AppCompatActivity {

    private Usuario currentUser;
    private EditText username_view;
    private ViewGroup layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        username_view = (EditText)findViewById(R.id.newAmigo);

        Intent recLog = getIntent();
        Bundle b = recLog.getExtras();

        String username_ = b.getString("usuario");
        currentUser = userSesion(username_);

        layout = (ViewGroup)findViewById(R.id.resultado);
    }

    public void buscarAmigo(View view){
        /**
        * obtener el nombre de usuario de amigo
        * crear objeto AMIGO
        * setear amigo setAmigo(amigo)
        * para mostrarlo en pantalla
        * */
        pintarAmigo();
    }

    public void pintarAmigo(){
        layout.removeAllViews();
        setAmigo(getAmigo());
    }

    public void addAmigo(String username){
        layout.removeAllViews();
        Toast.makeText(this, "Se ha agregado a "+username, Toast.LENGTH_LONG).show();
    }

    public void setAmigo(Amigo am){
        LayoutInflater inflater = LayoutInflater.from(this);
        int id = R.layout.amigosnew_layout;

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

        ImageView imgView = (ImageView) relativeLayout.findViewById(R.id.aminewAvatar);
        imgView.setImageResource(getIdImage(am.getAvatar()));

        ImageButton imgButton = (ImageButton)relativeLayout.findViewById(R.id.aminewBoton);
        imgButton.setTag(am);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amigo add = (Amigo)v.getTag();
                preguntar(add.getUsername());
            }
        });


        TextView textView_user = (TextView) relativeLayout.findViewById(R.id.aminewUser);
        textView_user.setText(am.getUsername());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        params.topMargin = 15;
        relativeLayout.setLayoutParams(params);
        layout.addView(relativeLayout);
    }

    public int getIdImage(int imagen){
        switch (imagen){
            case 1: return R.drawable.user_boy_3;
            case 2: return R.drawable.user_man_1;
            default: return R.drawable.user_boy_3;
        }
    }

    public void preguntar(final String user){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("¿Desea agregar a "+user + " a sus amigos?");

        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                addAmigo(user);
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Toast.makeText(getApplicationContext(), "Cancelar", Toast.LENGTH_LONG).show();
            }
        });

        alert.show();
    }

    public Amigo getAmigo(){
        Amigo amigo = new Amigo("Edwin",100,1);
        return amigo;
    }

    public Usuario userSesion(String usernaname){
        Usuario respuesta = new Usuario();
        respuesta = null;

        ConexionBD con = new ConexionBD(this);
        con.open();

        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios =(ArrayList<Usuario>) con.getUsuarios();
        for(Usuario c:usuarios){
            if(c.getUsername().equals(usernaname)){
                respuesta = c;
            }
        }
        con.close();

        return respuesta;
    }
}
