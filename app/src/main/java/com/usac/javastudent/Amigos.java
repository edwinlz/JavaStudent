package com.usac.javastudent;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;

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

    public void buscarAmigo(View view){
        /**
        * obtener el nombre de usuario de amigo
        * crear objeto AMIGO
        * setear amigo setAmigo(amigo)
        * para mostrarlo en pantalla
        * */
        new CallGetFiltro().execute();
    }

    private void procesarRespuesta(String respuesta) {
        String spliteo[] = respuesta.split("!");
        String datos[] = new String[spliteo.length-2];
        for (int i=2;i<spliteo.length;i++){
            datos[i-2] = spliteo[i];
        }

        ArrayList<Amigo> amigos = new ArrayList<Amigo>();
        Amigo amigo = null;
        for(int i=0;i<datos.length;i++){
            if(i%2!=0){
                amigo = new Amigo();
                amigo.setUsername(datos[i]);
            }
            else{
                amigo.setAvatar(Integer.parseInt(datos[i]));
                amigos.add(amigo);
                amigo = null;
            }
        }
        pintarAmigos(amigos);
    }

    public void pintarAmigos(ArrayList<Amigo> amigos){
        layout.removeAllViews();
        for (int i = 0;i<amigos.size();i++) {
            setAmigo(amigos.get(i));
        }
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



    public void addAmigo(String username){
        layout.removeAllViews();
        Toast.makeText(this, "Se ha agregado a "+username, Toast.LENGTH_LONG).show();
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


    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL="http://felipeantonio.somee.com/ServicioJavaStudent.asmx?WSDL";
    private static final String METHOD_NAME = "getFiltroUsuarios";
    private static final String SOAP_ACTION ="http://tempuri.org/getFiltroUsuarios";

    private static final String NAMESPACE2 = "http://tempuri.org/";
    private static final String URL2="http://felipeantonio.somee.com/ServicioJavaStudent.asmx?WSDL";
    private static final String METHOD_NAME2 = "registrarAmistad";
    private static final String SOAP_ACTION2 ="http://tempuri.org/registrarAmistad";
    private SoapObject request=null;
    private SoapSerializationEnvelope envelope=null;
    private SoapPrimitive resultsRequestSOAP=null;
    private String respuesta;
    private ProgressDialog dialogo;

    public Boolean invocarWSGetFiltro() {//getFiltroUsuarios
        Boolean bandera = true;
        try{
            request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("username",username_view.getText().toString());
            request.addProperty("usernameDueno",currentUser.getUsername());
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            transporte.call(SOAP_ACTION, envelope);
            resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
            respuesta = resultsRequestSOAP.toString();
            Log.e("Valor del response: ", resultsRequestSOAP.toString());
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.v("Error", "Error io: "+e.toString());
            bandera= false;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.v("Error", "Error exception: "+e.toString());
            bandera = false;
        }
        return bandera;
    }

    public Boolean invocarWSRegistrarAmistad(String usernameDueno, String usernameAmigo) {
        Boolean bandera = true;
        try{
            request = new SoapObject(NAMESPACE2, METHOD_NAME2);
            request.addProperty("usernameDueno",usernameDueno);
            request.addProperty("usernameAmigo",usernameAmigo);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL2);
            transporte.call(SOAP_ACTION2, envelope);
            resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
            respuesta = resultsRequestSOAP.toString();
            Log.e("Valor del response: ", resultsRequestSOAP.toString());
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.v("Error", "Error io: "+e.toString());
            bandera= false;
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.v("Error", "Error exception: "+e.toString());
            bandera = false;
        }
        return bandera;
    }



    private void mostrarRespuesta(String respuesta) {
        Toast toast = Toast.makeText(getApplicationContext(), respuesta, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }


    class CallGetFiltro extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialogo = new ProgressDialog(Amigos.this);
            dialogo.setMessage("Procesando solicitud...");
            dialogo.setIndeterminate(false);
            dialogo.setCancelable(false);
            dialogo.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if(invocarWSGetFiltro()){
                return "ok";
            }
            else{
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(s);
            dialogo.dismiss();
            if (result.contains("ok")) {
                procesarRespuesta(respuesta);
            }
        }
    }

    class CallRegistrarAmistad extends AsyncTask<String,String,String> {
        private String usernameDueno;
        private String usernameAmistad;

        public void setUsernameDueno(String usernameDueno){
            this.usernameDueno = usernameDueno;
        }
        public void setUsernameAmistad(String usernameAmistad){
            this.usernameAmistad = usernameAmistad;
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialogo = new ProgressDialog(Amigos.this);
            dialogo.setMessage("Procesando solicitud...");
            dialogo.setIndeterminate(false);
            dialogo.setCancelable(false);
            dialogo.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if(invocarWSRegistrarAmistad(usernameDueno, usernameAmistad)){
                return "ok";
            }
            else{
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(s);
            dialogo.dismiss();
            if(result.contains("ok")){
                mostrarRespuesta(respuesta);
            }
        }
    }
}
