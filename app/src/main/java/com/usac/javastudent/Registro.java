package com.usac.javastudent;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.usac.clasesjava.ConexionBD;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Registro extends AppCompatActivity {
    /*Contenedores */
    private EditText username_view;
    private EditText correo_view;
    private EditText nombre_view;
    private EditText pass_view;
    private RadioButton avaImagen1;
    private RadioButton avaImagen2;
    private RadioButton avaImagen3;
    private RadioButton avaImagen4;
    private RadioButton avaImagen5;
    private RadioButton avaImagen6;
    private int avatarUser = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre_view = (EditText)findViewById(R.id.name_reg);
        correo_view = (EditText)findViewById(R.id.email_reg);
        pass_view = (EditText)findViewById(R.id.pass_reg);
        username_view = (EditText)findViewById(R.id.username_reg);

        prepararAvatares();
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
        }
    }


    public void prepararAvatares()
    {
        avaImagen1 = (RadioButton)findViewById(R.id.radioAvatar1);
        avaImagen1.setOnClickListener(seleccionarAvatar);
        avaImagen1.setTag(1);

        avaImagen2 = (RadioButton)findViewById(R.id.radioAvatar2);
        avaImagen2.setOnClickListener(seleccionarAvatar);
        avaImagen2.setTag(2);

        avaImagen3 = (RadioButton)findViewById(R.id.radioAvatar3);
        avaImagen3.setOnClickListener(seleccionarAvatar);
        avaImagen3.setTag(3);

        avaImagen4 = (RadioButton)findViewById(R.id.radioAvatar4);
        avaImagen4.setOnClickListener(seleccionarAvatar);
        avaImagen4.setTag(4);

        avaImagen5 = (RadioButton)findViewById(R.id.radioAvatar5);
        avaImagen5.setOnClickListener(seleccionarAvatar);
        avaImagen5.setTag(5);

        avaImagen6 = (RadioButton)findViewById(R.id.radioAvatar6);
        avaImagen6.setOnClickListener(seleccionarAvatar);
        avaImagen6.setTag(6);

        avaImagen1.setSelected(true);
    }

    View.OnClickListener seleccionarAvatar = new View.OnClickListener(){
        public void onClick(View v) {
            int idAvatar = (int)v.getTag();
            verificarCambio(idAvatar);
            avatarUser = idAvatar;
            //Toast toast = Toast.makeText(getApplicationContext(),"Se selecciono "+v.getTag(), Toast.LENGTH_LONG);
            //toast.show();
        }
    };

    public void verificarCambio(int id){
        if(id==1){
            avaImagen2.setChecked(false);
            avaImagen3.setChecked(false);
            avaImagen4.setChecked(false);
            avaImagen5.setChecked(false);
            avaImagen6.setChecked(false);
        }else if (id==2){
            avaImagen1.setChecked(false);
            avaImagen3.setChecked(false);
            avaImagen4.setChecked(false);
            avaImagen5.setChecked(false);
            avaImagen6.setChecked(false);
        }else if (id==3){
            avaImagen1.setChecked(false);
            avaImagen2.setChecked(false);
            avaImagen4.setChecked(false);
            avaImagen5.setChecked(false);
            avaImagen6.setChecked(false);
        }else if (id==4){
            avaImagen1.setChecked(false);
            avaImagen2.setChecked(false);
            avaImagen3.setChecked(false);
            avaImagen5.setChecked(false);
            avaImagen6.setChecked(false);
        }else if (id==5){
            avaImagen1.setChecked(false);
            avaImagen2.setChecked(false);
            avaImagen3.setChecked(false);
            avaImagen4.setChecked(false);
            avaImagen6.setChecked(false);
        }else if (id==6){
            avaImagen1.setChecked(false);
            avaImagen2.setChecked(false);
            avaImagen3.setChecked(false);
            avaImagen4.setChecked(false);
            avaImagen5.setChecked(false);
        }
    }

    public void guardarUsuarioBDLocal(String respuesta){
        if(!respuesta.contains("ya fue registrado")) {
            String email = correo_view.getText().toString();
            String name = nombre_view.getText().toString();
            String password = pass_view.getText().toString();
            String username = username_view.getText().toString();
            //conectar con base de datos local
            ConexionBD bd = new ConexionBD(this);
            bd.open();
            if (bd.getUsuario(username) == null) {
                /*El usuario se guardara localmente*/
                bd.insertUsuario(username, email, password, name, 0, 0, 0, 1, 1, avatarUser);
            }
            bd.close(); //cerrar conexion bd
            finish();
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
            request.addProperty("modulo",1);
            request.addProperty("tema",1);
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
        guardarUsuarioBDLocal(respuesta);
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
