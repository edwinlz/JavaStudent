package com.usac.javastudent;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.usac.clasesjava.ConexionBD;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class Registro extends AppCompatActivity {
    /*Contenedores */
    private EditText username_view;
    private EditText correo_view;
    private EditText nombre_view;
    private EditText pass_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre_view = (EditText)findViewById(R.id.name_reg);
        correo_view = (EditText)findViewById(R.id.email_reg);
        pass_view = (EditText)findViewById(R.id.pass_reg);
        username_view = (EditText)findViewById(R.id.username_reg);
    }

    public void cancelar(View view){
        finish();
    }

    public void registar(View view){
        boolean cancel = false;
        View focusView = null;

        nombre_view.setError(null);
        correo_view.setError(null);
        pass_view.setError(null);
        username_view.setError(null);

        String email = correo_view.getText().toString();
        String name = nombre_view.getText().toString();
        String password = pass_view.getText().toString();
        String username = username_view.getText().toString();

        // Verificar username
        if (TextUtils.isEmpty(username)) {
            username_view.setError(getString(R.string.error_field_required));
            focusView = username_view;
            cancel = true;
        } else if (!isValidUsername(username)) {
            username_view.setError(getString(R.string.error_field_required));
            focusView = username_view;
            cancel = true;
        }
        // Verificar correo electronico
        if (TextUtils.isEmpty(email)) {
            correo_view.setError(getString(R.string.error_field_required));
            focusView = correo_view;
            cancel = true;
        } else if (!isValidEmail(email)) {
            correo_view.setError(getString(R.string.error_field_required));
            focusView = correo_view;
            cancel = true;
        }
        // Verificar nombre
        if (TextUtils.isEmpty(name)) {
            nombre_view.setError(getString(R.string.error_field_required));
            focusView = nombre_view;
            cancel = true;
        }
        // Validar contraseña
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            pass_view.setError(getString(R.string.error_invalid_password));
            focusView = pass_view;
            cancel = true;
        }

        /*GUARDAR EN BASE DE DATOS*/
        if(cancel){
            focusView.requestFocus();
        }else{

            /*
            * conectar con web service/ verifica si esta o no
            * Si se guarda en ws se guarda local
            * */
            new CallRegistro().execute();
            if(!respuesta.contains("ya fue registrado")) {
                //conectar con base de datos local
                ConexionBD bd = new ConexionBD(this);
                bd.open();
                if (bd.getUsuario(username) != null) {
                    /*El usuario ya esta registrado local*/

                } else {
                /*El usario se guardara localmente*/
                    bd.insertUsuario(username, email, password, name, 0, 0, 0, 1, 1, 1);
                }
                bd.close(); //cerrar conexion bd
                finish();
            }
        }

    }

    private boolean isValidEmail(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean isValidUsername(String username){
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL="http://felipeantonio.somee.com/ServicioJavaStudent.asmx?WSDL";
    private static final String METHOD_NAME = "registrarUsuario";
    private static final String SOAP_ACTION ="http://tempuri.org/registrarUsuario";
    private SoapObject request=null;
    private SoapSerializationEnvelope envelope=null;
    private SoapPrimitive resultsRequestSOAP=null;
    private String respuesta = null;
    private ProgressDialog dialogo;

    public Boolean invocarWS() {
        Boolean bandera = true;
        try{
            request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("username",username_view.getText().toString());
            request.addProperty("nombre",nombre_view.getText().toString());
            request.addProperty("correo",correo_view.getText().toString());
            request.addProperty("password",pass_view.getText().toString());
            request.addProperty("experiencia",0);
            request.addProperty("nivel",0);
            request.addProperty("modulo",0);
            request.addProperty("tema",0);
            request.addProperty("imagen",1);//cambiar por el numero de imagen que se selecciono
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            transporte.call(SOAP_ACTION, envelope);
            resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
            respuesta = resultsRequestSOAP.toString();
            Log.v("respuesta: ",respuesta);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.v("Error", "Error exception: "+e.toString());
            bandera = false;
        }
        return bandera;
    }

    private void mostrarRespuesta(String respuesta) {
        Toast toast = Toast.makeText(getApplicationContext(),respuesta, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    class CallRegistro extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialogo = new ProgressDialog(Registro.this);
            dialogo.setMessage("Procesando solicitud...");
            dialogo.setIndeterminate(false);
            dialogo.setCancelable(false);
            dialogo.show();
        }

        @Override
        protected String doInBackground(String... params) {
            if(invocarWS()){
                return "ok";
            }
            else{
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            dialogo.dismiss();
            if(result.contains("ok")){
                mostrarRespuesta(respuesta);
            }
        }
    }
}
