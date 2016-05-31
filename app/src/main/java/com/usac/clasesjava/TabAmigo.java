package com.usac.clasesjava;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;

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
                b.putString("usuario",Estatica.username_actual);
                Intent m = new Intent(getActivity(),Amigos.class);
                m.putExtras(b);
                startActivity(m);
            }
        });

        layout = (ViewGroup)aux.findViewById(R.id.contentFA);
        new CallGetAmistades().execute();
        return  aux;
    }

    private void procesarRespuesta(String respuesta) {
        String spliteo[] = respuesta.split("!");
        String datos[] = new String[spliteo.length-3];
        for (int i=3;i<spliteo.length;i++){
            datos[i-3] = spliteo[i];
        }

        ArrayList<Amigo> amigos = new ArrayList<Amigo>();
        Amigo amigo = null;
        for(int i=0;i<datos.length;i++){
            if(i%3==0){
                amigo = new Amigo();
                amigo.setUsername(datos[i]);
            }
            else if(i%3==1){
                amigo.setExperiencia(Integer.parseInt(datos[i]));
            }
            else{
                amigo.setAvatar(Integer.parseInt(datos[i]));
                amigos.add(amigo);
                amigo = null;
            }
        }
        pintarAmigos(amigos);
    }

    private void pintarAmigos(ArrayList<Amigo> amigos){
        for(Amigo mod:amigos){
            setAmigo(mod);
        }
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

    public int getIdImage(int imagen){
        switch (imagen){
            case 1: return R.drawable.user_boy_3;
            case 2: return R.drawable.user_man_1;
            default: return R.drawable.user_boy_3;
        }
    }



    private void respuestaSolicitudAmistad(String respuesta) {
        Toast toast = Toast.makeText(getContext(), respuesta, Toast.LENGTH_LONG);
        toast.show();
        new CallGetAmistades().execute();
    }

    public void elimarAmigo(String username){
        layout.removeAllViews();
        new CallEliminarAmistad(username).execute();
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


    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL="http://felipeantonio.somee.com/ServicioJavaStudent.asmx?WSDL";
    private static final String METHOD_NAME = "getAmistades";
    private static final String SOAP_ACTION ="http://tempuri.org/getAmistades";

    private static final String NAMESPACE2 = "http://tempuri.org/";
    private static final String URL2="http://felipeantonio.somee.com/ServicioJavaStudent.asmx?WSDL";
    private static final String METHOD_NAME2 = "eliminarAmistad";
    private static final String SOAP_ACTION2 ="http://tempuri.org/eliminarAmistad";
    private SoapObject request=null;
    private SoapSerializationEnvelope envelope=null;
    private SoapPrimitive resultsRequestSOAP=null;
    private String respuesta;
    private ProgressDialog dialogo;

    public Boolean invocarWSGetAmistades() {
        Boolean bandera = true;
        try{
            request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("username",Estatica.username_actual);

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

    public Boolean invocarWSEliminarAmistad(String usernameDueno, String usernameAmigo) {
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

    class CallGetAmistades extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialogo = new ProgressDialog(getContext());
            dialogo.setMessage("Procesando solicitud...");
            dialogo.setIndeterminate(false);
            dialogo.setCancelable(false);
            dialogo.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if(invocarWSGetAmistades()){
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
                procesarRespuesta(respuesta);
            }
        }
    }

    class CallEliminarAmistad extends AsyncTask<String,String,String> {
        private String usernameAmistad;

        public CallEliminarAmistad(String usernameAmistad){
            this.usernameAmistad = usernameAmistad;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialogo = new ProgressDialog(getContext());
            dialogo.setMessage("Procesando solicitud...");
            dialogo.setIndeterminate(false);
            dialogo.setCancelable(false);
            dialogo.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if(invocarWSEliminarAmistad(Estatica.username_actual,usernameAmistad)){
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
                respuestaSolicitudAmistad(respuesta);
            }
        }
    }


}