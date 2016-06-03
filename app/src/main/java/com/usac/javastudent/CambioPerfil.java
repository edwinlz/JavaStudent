package com.usac.javastudent;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.usac.clasesjava.Estatica;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class CambioPerfil extends AppCompatActivity {
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
        setContentView(R.layout.activity_cambio_perfil);

        nombre_view = (EditText)findViewById(R.id.name_regPerfil);
        correo_view = (EditText)findViewById(R.id.email_regPerfil);
        pass_view = (EditText)findViewById(R.id.pass_regPerfil);

        nombre_view.setText(Estatica.usuario_actual.getNombre());
        correo_view.setText(Estatica.usuario_actual.getEmail());
        pass_view.setText(Estatica.usuario_actual.getPassword());
        prepararAvatares();
        marcarRadioButton(Estatica.usuario_actual.getImagen());


    }

    private void marcarRadioButton(int id) {
        if(id==1){
            avaImagen1.setChecked(true);
        }else if (id==2){
            avaImagen2.setChecked(true);
        }else if (id==3){
            avaImagen3.setChecked(true);
        }else if (id==4){
            avaImagen4.setChecked(true);
        }else if (id==5){
            avaImagen5.setChecked(true);
        }else if (id==6){
            avaImagen6.setChecked(true);
        }
        else{
            avaImagen1.setChecked(true);
        }
    }

    public void cancelar(View view){
        Intent salir = new Intent(CambioPerfil.this,Principal.class);
        startActivity(salir);
        finish();
    }

    public void modificar(View view){
        boolean cancel = false;
        View focusView = null;

        nombre_view.setError(null);
        correo_view.setError(null);
        pass_view.setError(null);

        String email = correo_view.getText().toString();
        String name = nombre_view.getText().toString();
        String password = pass_view.getText().toString();

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
            new CallCambiarDatosUsuario().execute();
        }
    }


    public void prepararAvatares()
    {
        avaImagen1 = (RadioButton)findViewById(R.id.radioAvatar1Perfil);
        avaImagen1.setOnClickListener(seleccionarAvatar);
        avaImagen1.setTag(1);

        avaImagen2 = (RadioButton)findViewById(R.id.radioAvatar2Perfil);
        avaImagen2.setOnClickListener(seleccionarAvatar);
        avaImagen2.setTag(2);

        avaImagen3 = (RadioButton)findViewById(R.id.radioAvatar3Perfil);
        avaImagen3.setOnClickListener(seleccionarAvatar);
        avaImagen3.setTag(3);

        avaImagen4 = (RadioButton)findViewById(R.id.radioAvatar4Perfil);
        avaImagen4.setOnClickListener(seleccionarAvatar);
        avaImagen4.setTag(4);

        avaImagen5 = (RadioButton)findViewById(R.id.radioAvatar5Perfil);
        avaImagen5.setOnClickListener(seleccionarAvatar);
        avaImagen5.setTag(5);

        avaImagen6 = (RadioButton)findViewById(R.id.radioAvatar6Perfil);
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

    private boolean isValidEmail(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL="http://felipeantonio.somee.com/ServicioJavaStudent.asmx?WSDL";
    private static final String METHOD_NAME = "cambiarDatosUsuario";
    private static final String SOAP_ACTION ="http://tempuri.org/cambiarDatosUsuario";
    private SoapObject request=null;
    private SoapSerializationEnvelope envelope=null;
    private SoapPrimitive resultsRequestSOAP=null;
    private String respuesta = null;
    private ProgressDialog dialogo;

    public Boolean invocarWS() {
        Boolean bandera = true;
        try{
            request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("username",Estatica.usuario_actual.getUsername());
            request.addProperty("nombre",nombre_view.getText().toString());
            request.addProperty("correo",correo_view.getText().toString());
            request.addProperty("password",pass_view.getText().toString());
            request.addProperty("imagen",avatarUser);
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
        modificarUsuarioBDLocal();
    }

    private void modificarUsuarioBDLocal() {
        String email = correo_view.getText().toString();
        String name = nombre_view.getText().toString();
        String password = pass_view.getText().toString();
        ConexionBD bd = new ConexionBD(this);
        bd.open();
        if (bd.getUsuario(Estatica.usuario_actual.getUsername()) != null) {
            bd.updateUsuario(Estatica.usuario_actual.getUsername(), email, password, name, 0, 0, 0, 1, 0, avatarUser);
            Estatica.usuario_actual = bd.getUsuario(Estatica.usuario_actual.getUsername());
        }
        bd.close(); //cerrar conexion bd
    }

    class CallCambiarDatosUsuario extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialogo = new ProgressDialog(CambioPerfil.this);
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
            else{
                Toast toast = Toast.makeText(getApplicationContext(),"No es posible modificar tu perfil, debes contar con acceso a internet.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        }
    }
}
