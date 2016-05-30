package com.usac.clasesjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edwin on 23/05/2016.
 */
public class ConexionBD extends SQLiteOpenHelper {

    private static String BD_PATH = "/data/data/com.usac.javastudent/databases/";
    private static String BD_NAME = "bdjs.db";
    private SQLiteDatabase bdJStudent;
    private final Context bdContexto;


    public ConexionBD(Context context) {
        super(context, BD_NAME, null, 1);
        this.bdContexto = context;
    }

    public void open() throws SQLiteException{
        try {
            crearBaseDatos();
        }catch (IOException ex){
            Log.i("informacion","No se ha podido crear la base de datos");
        }
        String rutaBD = BD_PATH + BD_NAME;
        bdJStudent = SQLiteDatabase.openDatabase(rutaBD, null,SQLiteDatabase.OPEN_READWRITE );
    }


    private void crearBaseDatos()throws IOException{
        boolean existeBD = verificarBaseDatos();
        if(existeBD){
            Log.i("informacion","La base de datos existe, no se copiara");
        }else{
            this.getReadableDatabase();
            try {
                copiarBaseDatos();
            }catch (IOException ex){
                Log.i("informacion","La base de datos no existe, se copiara");
            }
        }
    }

    private boolean verificarBaseDatos(){
        SQLiteDatabase verificarBD = null;
        try{
            String rutaBD = BD_PATH + BD_NAME;
            verificarBD = SQLiteDatabase.openDatabase(rutaBD, null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException ex){
            Log.i("informacion","La base de datos no existe aun");
        }
        if(verificarBD != null){
            verificarBD.close();
        }
        return verificarBD != null ? true : false;
    }

    private void copiarBaseDatos()throws IOException{
        InputStream fileEntrada = bdContexto.getAssets().open(BD_NAME);
        String nuevaRuta = BD_PATH + BD_NAME;
        OutputStream fileSalida = new FileOutputStream(nuevaRuta);

        byte[] buffer = new byte[1024];
        int tamanio;
        while ((tamanio = fileEntrada.read(buffer))>0){
            fileSalida.write(buffer, 0, tamanio);
        }

        fileSalida.flush();
        fileSalida.close();
        fileEntrada.close();
    }

    @Override
    public synchronized void close(){
        if(bdJStudent != null){
            bdJStudent.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*Creacion de usuarios*/

    public final static String KEY_USUARIO_ID = "_id";
    public final static String KEY_USUARIO_USER = "username";
    public final static String KEY_USUARIO_EMAIL = "email";
    public final static String KEY_USUARIO_PASS = "password";
    public final static String KEY_USUARIO_NAME = "nombre";
    public final static String KEY_USUARIO_EXPE = "experiencia";
    public final static String KEY_USUARIO_NIVEL = "nivel";
    public final static String KEY_USUARIO_SESION = "sesion";
    public final static String KEY_USUARIO_MODULO = "modulo";
    public final static String KEY_USUARIO_TEMA = "tema";
    public final static String KEY_USUARIO_IMAG = "imagen";

    private static final String[] usuario = new String[] { KEY_USUARIO_ID, KEY_USUARIO_USER,
            KEY_USUARIO_EMAIL, KEY_USUARIO_PASS, KEY_USUARIO_NAME, KEY_USUARIO_EXPE,
            KEY_USUARIO_NIVEL, KEY_USUARIO_SESION, KEY_USUARIO_MODULO, KEY_USUARIO_TEMA, KEY_USUARIO_IMAG };


    public Usuario getUsuario(String username) {
        Usuario user = new Usuario();
        Cursor result = bdJStudent.query("usuario",usuario, KEY_USUARIO_USER + "='" +username+"'", null, null, null, null);
        if ((result.getCount() == 0) || !result.moveToFirst()) {
            user = null;
        } else {
            if (result.moveToFirst()) {
                user = new Usuario(
                        result.getInt(result.getColumnIndex(KEY_USUARIO_ID)),
                        result.getString(result.getColumnIndex(KEY_USUARIO_USER)),
                        result.getString(result.getColumnIndex(KEY_USUARIO_EMAIL)),
                        result.getString(result.getColumnIndex(KEY_USUARIO_PASS)),
                        result.getString(result.getColumnIndex(KEY_USUARIO_NAME)),
                        result.getInt(result.getColumnIndex(KEY_USUARIO_EXPE)),
                        result.getInt(result.getColumnIndex(KEY_USUARIO_NIVEL)),
                        result.getInt(result.getColumnIndex(KEY_USUARIO_SESION)),
                        result.getInt(result.getColumnIndex(KEY_USUARIO_MODULO)),
                        result.getInt(result.getColumnIndex(KEY_USUARIO_TEMA)),
                        result.getInt(result.getColumnIndex(KEY_USUARIO_IMAG))
                );
            }
        }
        return user;
    }

    public List<Usuario> getUsuarios() {
        ArrayList<Usuario> usuarios_ = new ArrayList<Usuario>();
        Cursor result = bdJStudent.query("usuario",usuario, null, null, null, null, KEY_USUARIO_ID);
        if (result.moveToFirst())
            do {
                usuarios_.add( new Usuario(
                                result.getInt(result.getColumnIndex(KEY_USUARIO_ID)),
                                result.getString(result.getColumnIndex(KEY_USUARIO_USER)),
                                result.getString(result.getColumnIndex(KEY_USUARIO_EMAIL)),
                                result.getString(result.getColumnIndex(KEY_USUARIO_PASS)),
                                result.getString(result.getColumnIndex(KEY_USUARIO_NAME)),
                                result.getInt(result.getColumnIndex(KEY_USUARIO_EXPE)),
                                result.getInt(result.getColumnIndex(KEY_USUARIO_NIVEL)),
                                result.getInt(result.getColumnIndex(KEY_USUARIO_SESION)),
                                result.getInt(result.getColumnIndex(KEY_USUARIO_MODULO)),
                                result.getInt(result.getColumnIndex(KEY_USUARIO_TEMA)),
                                result.getInt(result.getColumnIndex(KEY_USUARIO_IMAG))
                        )
                );
            } while(result.moveToNext());
        return usuarios_;
    }

    public long insertUsuario(String username, String email, String password, String name, Integer experiencia, Integer nivel, Integer sesion, int modulo, int tema, int imagen) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_USUARIO_USER, username);
        newValues.put(KEY_USUARIO_EMAIL, email);
        newValues.put(KEY_USUARIO_PASS, password);
        newValues.put(KEY_USUARIO_NAME, name);
        newValues.put(KEY_USUARIO_EXPE, ""+experiencia);
        newValues.put(KEY_USUARIO_NIVEL, ""+nivel);
        newValues.put(KEY_USUARIO_SESION, ""+sesion);
        newValues.put(KEY_USUARIO_MODULO, ""+modulo);
        newValues.put(KEY_USUARIO_TEMA, ""+tema);
        newValues.put(KEY_USUARIO_IMAG, ""+imagen);

        return bdJStudent.insert("usuario", null, newValues);
    }


    public boolean updateUsuario(String username, String email, String password, String name, Integer experiencia, Integer nivel, Integer sesion, int modulo, int tema, int imagen) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_USUARIO_USER, username);
        newValues.put(KEY_USUARIO_EMAIL, email);
        newValues.put(KEY_USUARIO_PASS, password);
        newValues.put(KEY_USUARIO_NAME, name);
        newValues.put(KEY_USUARIO_EXPE, ""+experiencia);
        newValues.put(KEY_USUARIO_NIVEL, ""+nivel);
        newValues.put(KEY_USUARIO_SESION, ""+sesion);
        newValues.put(KEY_USUARIO_MODULO, ""+modulo);
        newValues.put(KEY_USUARIO_TEMA, ""+tema);
        newValues.put(KEY_USUARIO_IMAG, ""+imagen);

        return bdJStudent.update("usuario", newValues, KEY_USUARIO_USER + "='" + username+"'", null) > 0;
    }

    public boolean updateUsuarioSesion(String username, Integer sesion) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_USUARIO_SESION,sesion);
        return bdJStudent.update("usuario", newValues, KEY_USUARIO_USER + "='" + username+"'", null) > 0;
    }

    public boolean updateUsuarioExperiencia(String username, Integer experiencia) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_USUARIO_EXPE,experiencia);
        return bdJStudent.update("usuario", newValues, KEY_USUARIO_USER + "='" + username+"'", null) > 0;
    }

    public boolean updateUsuarioEmail(String username, String email) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_USUARIO_EMAIL,email);
        return bdJStudent.update("usuario", newValues, KEY_USUARIO_USER + "='" + username+"'", null) > 0;
    }

    public boolean updateUsuarioName(String username, String name) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_USUARIO_NAME,name);
        return bdJStudent.update("usuario", newValues, KEY_USUARIO_USER + "='" + username+"'", null) > 0;
    }

    public boolean updateUsuarioPass(String username, String password) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_USUARIO_PASS, password);
        return bdJStudent.update("usuario", newValues, KEY_USUARIO_USER + "='" + username+"'", null) > 0;
    }

    public boolean updateUsuarioModulo(String username, Integer modulo) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_USUARIO_MODULO, ""+modulo);
        return bdJStudent.update("usuario", newValues, KEY_USUARIO_USER + "='" + username+"'", null) > 0;
    }

    public boolean updateUsuarioTema(String username, Integer tema) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_USUARIO_TEMA, ""+tema);
        return bdJStudent.update("usuario", newValues, KEY_USUARIO_USER + "='" + username+"'", null) > 0;
    }

    public boolean updateUsuarioImagen(String username, Integer imagen) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_USUARIO_IMAG, ""+imagen);
        return bdJStudent.update("usuario", newValues, KEY_USUARIO_USER + "='" + username+"'", null) > 0;
    }

    /*Manejo de modulos*/
    public static final String KEY_MODULO_ID = "_id";
    public final static String KEY_MODULO_NOMBRE = "nombre";
    public final static String KEY_MODULO_DESCRI = "descripcion";
    public final static String KEY_MODULO_TEMAS = "temas";
    public final static String KEY_MODULO_EXP = "experiencia";

    private static final String[] modulos = new String[] { KEY_MODULO_ID, KEY_MODULO_NOMBRE,
                                        KEY_MODULO_DESCRI, KEY_MODULO_TEMAS, KEY_MODULO_EXP};

    public List<Modulo> getModulos() {
        ArrayList<Modulo> modulos_ = new ArrayList<Modulo>();
        Cursor result = bdJStudent.query("modulo",modulos, null, null, null, null, KEY_MODULO_ID);
        if (result.moveToFirst())
            do {
                modulos_.add(new Modulo(
                                result.getInt(result.getColumnIndex(KEY_MODULO_ID)),
                                result.getString(result.getColumnIndex(KEY_MODULO_NOMBRE)),
                                result.getString(result.getColumnIndex(KEY_MODULO_DESCRI)),
                                result.getInt(result.getColumnIndex(KEY_MODULO_TEMAS)),
                                result.getInt(result.getColumnIndex(KEY_MODULO_EXP))
                        )
                );
            } while(result.moveToNext());
        return modulos_;
    }

    public Modulo getModulo(int _rowIndex) {
        Modulo alarm = new Modulo();
        Cursor result = bdJStudent.query("modulo",modulos, KEY_MODULO_ID + "=" +_rowIndex, null, null, null, null);
        if ((result.getCount() == 0) || !result.moveToFirst()) {
            alarm = null;
        } else {
            if (result.moveToFirst()) {
                alarm = new Modulo(
                        result.getInt(result.getColumnIndex(KEY_MODULO_ID)),
                        result.getString(result.getColumnIndex(KEY_MODULO_NOMBRE)),
                        result.getString(result.getColumnIndex(KEY_MODULO_DESCRI)),
                        result.getInt(result.getColumnIndex(KEY_MODULO_TEMAS)),
                        result.getInt(result.getColumnIndex(KEY_MODULO_EXP))
                );
            }
        }
        return alarm;
    }

     /*Manejo de temas*/
    public static final String KEY_TEMA_ID = "_id";
    public final static String KEY_TEMA_MOD = "modulo";
    public final static String KEY_TEMA_NOM = "nombre";
    public final static String KEY_TEMA_DES = "descripcion";
    public final static String KEY_TEMA_CONTE = "contenido";
    public final static String KEY_TEMA_EXP = "experiencia";


    private static final String[] temas = new String[] { KEY_TEMA_ID, KEY_TEMA_MOD, KEY_TEMA_NOM, KEY_TEMA_DES, KEY_TEMA_CONTE,KEY_TEMA_EXP};

    public List<Tema> getTemas(int modulo) {
        ArrayList<Tema> temas_ = new ArrayList<Tema>();
        Cursor result = bdJStudent.query("tema",temas, KEY_TEMA_MOD + "=" +modulo, null, null, null, null);
        if (result.moveToFirst())
            do {
                temas_.add(new Tema(
                                result.getInt(result.getColumnIndex(KEY_TEMA_ID)),
                                result.getInt(result.getColumnIndex(KEY_TEMA_MOD)),
                                result.getString(result.getColumnIndex(KEY_TEMA_NOM)),
                                result.getString(result.getColumnIndex(KEY_TEMA_DES)),
                                result.getString(result.getColumnIndex(KEY_TEMA_CONTE)),
                                result.getInt(result.getColumnIndex(KEY_TEMA_EXP))
                        )
                );
            } while(result.moveToNext());
        return temas_;
    }

    public Tema getTema(int _rowIndex) {
        Tema tema_ = new Tema();
        Cursor result = bdJStudent.query("tema",temas, KEY_TEMA_ID + "=" +_rowIndex, null, null, null, null);
        if ((result.getCount() == 0) || !result.moveToFirst()) {
            tema_ = null;
        } else {
            if (result.moveToFirst()) {
                tema_ = new Tema(
                        result.getInt(result.getColumnIndex(KEY_TEMA_ID)),
                        result.getInt(result.getColumnIndex(KEY_TEMA_MOD)),
                        result.getString(result.getColumnIndex(KEY_TEMA_NOM)),
                        result.getString(result.getColumnIndex(KEY_TEMA_DES)),
                        result.getString(result.getColumnIndex(KEY_TEMA_CONTE)),
                        result.getInt(result.getColumnIndex(KEY_TEMA_EXP))
                );
            }
        }
        return tema_;
    }

    /*Manejo de respuestas*/
    public final static String KEY_RESP_ID = "_id";
    public final static String KEY_RESP_PRUEBA = "prueba";
    public final static String KEY_RESP_RESP = "respuesta";

    private static final String[] respuestas = new String[] { KEY_RESP_ID, KEY_RESP_PRUEBA, KEY_RESP_RESP};


    public List<Respuesta> getRespuestas(int prueba) {
        ArrayList<Respuesta> respuestas_ = new ArrayList<Respuesta>();
        Cursor result = bdJStudent.query("respuesta",respuestas, KEY_RESP_PRUEBA + "=" +prueba, null, null, null, null);
        if (result.moveToFirst())
            do {
                respuestas_.add(new Respuesta(
                                result.getInt(result.getColumnIndex(KEY_RESP_ID)),
                                result.getInt(result.getColumnIndex(KEY_RESP_PRUEBA)),
                                result.getString(result.getColumnIndex(KEY_RESP_RESP))
                        )
                );
            } while(result.moveToNext());
        return respuestas_;
    }

     /*Manejo de pruebas*/

    public final static String KEY_PRU_ID = "_id";
    public final static String KEY_PRU_TEMA = "tema";
    public final static String KEY_PRU_PREG = "pregunta";
    public final static String KEY_PRU_EXP = "experiencia";
    public final static String KEY_PRU_TIPO = "tipo";
    public final static String KEY_PRU_CORR = "correcto";

    private static final String[] pruebas = new String[] { KEY_PRU_ID, KEY_PRU_TEMA, KEY_PRU_PREG,
                                            KEY_PRU_EXP, KEY_PRU_TIPO, KEY_PRU_CORR};


    public List<Prueba> getPruebas(int tema) {
        ArrayList<Prueba> pruebas_ = new ArrayList<Prueba>();
        Cursor result = bdJStudent.query("prueba",pruebas, KEY_PRU_TEMA + "=" +tema, null, null, null, null);
        if (result.moveToFirst())
            do {
                pruebas_.add(new Prueba(
                                result.getInt(result.getColumnIndex(KEY_PRU_ID)),
                                result.getInt(result.getColumnIndex(KEY_PRU_TEMA)),
                                result.getString(result.getColumnIndex(KEY_PRU_PREG)),
                                getRespuestas(result.getInt(result.getColumnIndex(KEY_PRU_ID))),
                                result.getInt(result.getColumnIndex(KEY_PRU_EXP)),
                                result.getInt(result.getColumnIndex(KEY_PRU_TIPO)),
                                result.getInt(result.getColumnIndex(KEY_PRU_CORR))
                        )
                );
            } while(result.moveToNext());
        return pruebas_;
    }
}
