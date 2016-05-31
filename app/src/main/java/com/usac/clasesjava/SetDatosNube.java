package com.usac.clasesjava;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

/**
 * Created by felip on 30/05/2016.
 */
public class SetDatosNube {
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL="http://felipeantonio.somee.com/ServicioJavaStudent.asmx?WSDL";
    private static final String METHOD_NAME = "setDatos";
    private static final String SOAP_ACTION ="http://tempuri.org/setDatos";
    private SoapObject request=null;
    private SoapSerializationEnvelope envelope=null;
    private SoapPrimitive resultsRequestSOAP=null;
    private String respuesta = null;
    private ProgressDialog dialogo;
    private Context contexto;

    //METODO IMPORTANTE QUE HACE LA FUNCIONALIDAD DEL WEBSERVICE
    public void setearDatos(String username,int experiencia,int nivel,int modulo,int tema,Context contexto){
        new CallSeteoDatos(username,experiencia,nivel,modulo,tema,contexto).execute();
    }

    public Boolean invocarWS(String username,int experiencia,int nivel,int modulo,int tema) {
        Boolean bandera = true;
        try{
            request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("username",username);
            request.addProperty("experiencia",experiencia);
            request.addProperty("nivel",nivel);
            request.addProperty("modulo",modulo);
            request.addProperty("tema",tema);
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

    class CallSeteoDatos extends AsyncTask<String,String,String> {
        private String username;
        private int experiencia,nivel,modulo,tema;
        private Context contexto;

        public CallSeteoDatos(String username,int experiencia,int nivel,int modulo,int tema,Context contexto){
            this.username=username;
            this.experiencia= experiencia;
            this.nivel = nivel;
            this.modulo = modulo;
            this.tema = tema;
            this.contexto = contexto;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialogo = new ProgressDialog(contexto);
            dialogo.setMessage("Procesando solicitud...");
            dialogo.setIndeterminate(false);
            dialogo.setCancelable(false);
            dialogo.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if(invocarWS(username,experiencia,nivel,modulo,tema)){
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

        private void mostrarRespuesta(String respuesta) {
            Toast toast = Toast.makeText(contexto,respuesta, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }
}
