package com.usac.javastudent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.usac.clasesjava.ConexionBD;

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
        registroLocal();
    }

    public void registroLocal(){

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
            correo_view.setError(getString(R.string.error_field_required));
            focusView = correo_view;
            cancel = true;
        } else if (!isValidUsername(username)) {
            correo_view.setError(getString(R.string.error_field_required));
            focusView = correo_view;
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
            correo_view.setError(getString(R.string.error_field_required));
            focusView = correo_view;
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

            //conectar con base de datos local
            ConexionBD bd = new ConexionBD(this);
            bd.open();
            if(bd.getUsuario(username)!= null){
                /*El usario ya esta registrado local*/

            }else{
                /*El usario se guardara localmente*/
                bd.insertUsuario(username, email, password, name,0,0,0,1,1,1);
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
}
