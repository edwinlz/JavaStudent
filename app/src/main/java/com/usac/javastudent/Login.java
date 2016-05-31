package com.usac.javastudent;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.usac.clasesjava.ConexionBD;
import com.usac.clasesjava.Estatica;
import com.usac.clasesjava.Usuario;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    //Tarea asincrona para comprobacion local
    private CallValidarLogeoLocal validacionLogeoLocal = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;
    private CheckBox mCheckSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*verificador de usuario activo*/
        ConexionBD bda = new ConexionBD(this);
        bda.open();

        ArrayList<Usuario> userActive = new ArrayList<Usuario>();
        userActive =(ArrayList<Usuario>) bda.getUsuarios();

        for(Usuario c:userActive){
            if(c.getSesion()==1){
                Intent m = new Intent(Login.this,Principal.class);
                Bundle infoUsuario = new Bundle();
                infoUsuario.putString("usuario",c.getUsername());
                Estatica.username_actual = c.getUsername();
                m.putExtras(infoUsuario);
                startActivity(m);
                finish();
            }
        }
        bda.close();
        /*fin verificar usuario activo*/

        mCheckSesion = (CheckBox)findViewById(R.id.checkSesion);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    intentarLogear();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intentarLogear();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void registro(View view){
        Intent i = new Intent(Login.this,Registro.class);
        startActivity(i);
    }

    private void intentarLogear() {
        if (validacionLogeoLocal != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        else {
            showProgress(true);
            validacionLogeoLocal = new CallValidarLogeoLocal(username, password);
            validacionLogeoLocal.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    //Shows the progress UI and hides the login form.
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    class CallValidarLogeoLocal extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private final String mPassword;
        private ConexionBD conexion;
        private Usuario usuarioActual;

        CallValidarLogeoLocal(String email, String password) {
            mUsername = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            conexion = new ConexionBD(Login.this);
            conexion.open();
            ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
            usuarios =(ArrayList<Usuario>) conexion.getUsuarios();
            for(Usuario c:usuarios){
                if(c.getUsername().equals(mUsername)){
                    usuarioActual = c;
                    Estatica.username_actual = c.getUsername();
                    return c.getPassword().equals(mPassword);
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            validacionLogeoLocal = null;
            showProgress(false);
            if (success) {
                if(mCheckSesion.isChecked()){
                    conexion.updateUsuarioSesion(usuarioActual.getUsername(),1);
                }
                conexion.close();

                Intent m = new Intent(Login.this,Principal.class);
                Bundle infoUsuario = new Bundle();
                infoUsuario.putString("usuario",usuarioActual.getUsername());
                m.putExtras(infoUsuario);
                startActivity(m);
                finish();
            } else {
                mPasswordView.setError("Credenciales locales invalidas");
                mPasswordView.requestFocus();
                new CallValidarLogeo().execute();
            }
        }

        @Override
        protected void onCancelled() {
            validacionLogeoLocal = null;
            showProgress(false);
        }
    }

    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL="http://felipeantonio.somee.com/ServicioJavaStudent.asmx?WSDL";
    private static final String METHOD_NAME = "validarLogeo";
    private static final String SOAP_ACTION ="http://tempuri.org/validarLogeo";
    private SoapObject request=null;
    private SoapSerializationEnvelope envelope=null;
    private SoapPrimitive resultsRequestSOAP=null;
    private String respuesta = null;
    private ProgressDialog dialogo;

    public Boolean invocarWS() {
        Boolean bandera = true;
        try{
            request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("username",mUsernameView.getText().toString());
            request.addProperty("password",mPasswordView.getText().toString());
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            transporte.call(SOAP_ACTION, envelope);
            resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
            respuesta = resultsRequestSOAP.toString();
            Log.e("Valor del response: ", resultsRequestSOAP.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.v("Error", "Error exception: "+e.toString());
            bandera = false;
        }
        return bandera;
    }

    private void validacionLogeo(String respuesta) {
        if(!respuesta.contains("Invalido, Credenciales")) {
            ConexionBD bda = new ConexionBD(this);
            bda.open();
            String[] valores = respuesta.split(",");
            if(mCheckSesion.isChecked()) {
                bda.insertUsuario(mUsernameView.getText().toString(), valores[1], mPasswordView.getText().toString(), valores[0], Integer.parseInt(valores[2]), Integer.parseInt(valores[3]),1,Integer.parseInt(valores[4]),Integer.parseInt(valores[5]),Integer.parseInt(valores[6]));
            }
            else{
                bda.insertUsuario(mUsernameView.getText().toString(), valores[1], mPasswordView.getText().toString(), valores[0], Integer.parseInt(valores[2]), Integer.parseInt(valores[3]),0,Integer.parseInt(valores[4]),Integer.parseInt(valores[5]),Integer.parseInt(valores[6]));
            }
            bda.close();

            Intent m = new Intent(Login.this, Principal.class);
            Bundle infoUsuario = new Bundle();
            infoUsuario.putString("usuario",mUsernameView.getText().toString());
            Estatica.username_actual = mUsernameView.getText().toString();
            m.putExtras(infoUsuario);
            startActivity(m);
            finish();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),respuesta, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }

    class CallValidarLogeo extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialogo = new ProgressDialog(Login.this);
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
            //super.onPostExecute(s);
            dialogo.dismiss();
            if(result.contains("ok")){
                validacionLogeo(respuesta);
            }
        }
    }
}

