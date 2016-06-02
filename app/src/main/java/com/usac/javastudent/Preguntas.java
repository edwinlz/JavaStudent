package com.usac.javastudent;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.usac.clasesjava.ConexionBD;
import com.usac.clasesjava.Modulo;
import com.usac.clasesjava.Prueba;
import com.usac.clasesjava.Tema;

import java.util.ArrayList;

public class Preguntas extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public String respuesta1;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static ArrayList<Prueba> lstPruebas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle recibido = getIntent().getExtras();
        int tema_data = recibido.getInt("tema");

        lstPruebas = getPruebas(tema_data);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    private ArrayList<Prueba> getPruebas(int tema){
        ConexionBD bd  = new ConexionBD(this);
        bd.open();
        ArrayList<Prueba> pruebas = new ArrayList<Prueba>();
        pruebas =(ArrayList<Prueba>) bd.getPruebas(tema);
        bd.close();
        return pruebas;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preguntas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ViewGroup layout;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int pruebaNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, pruebaNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_preguntas, container, false);
            layout = (ViewGroup)rootView.findViewById(R.id.preguntaNuevo);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            int nprueba = getArguments().getInt(ARG_SECTION_NUMBER);

            setPrueba(getPrueba(nprueba),nprueba);

            return rootView;
        }

        public void setPrueba(Prueba prueba,int num){

            LayoutInflater inflater = LayoutInflater.from(getActivity());
            if(prueba.tipo == 1){
                int id = R.layout.quiz_layout_directa;
                RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

                TextView textView_numero = (TextView) relativeLayout.findViewById(R.id.preguntaNum);
                textView_numero.setText(num+"/"+lstPruebas.size());

                TextView textView_titulo = (TextView) relativeLayout.findViewById(R.id.quizPregunta);
                textView_titulo.setText(prueba.pregunta);

                final EditText _respuesta = (EditText)relativeLayout.findViewById(R.id.respDirecta);


                final ImageButton button = (ImageButton)relativeLayout.findViewById(R.id.quizEnviar);
                button.setTag(prueba);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Prueba aux = (Prueba)v.getTag();
                        if(_respuesta.getText().toString().equals(aux.respuestas.get(0))){
                            //Toast.makeText(getActivity(), "Ha ganado "+ aux.experiencia +" puntos de experiencia", Toast.LENGTH_LONG).show();
                            getFeedback("La respuesta es correcta +"+aux.experiencia+"EXP");
                            button.setEnabled(false);
                            button.setImageResource(R.drawable.minus);
                        }else{
                            //Toast.makeText(getActivity(), "La respuesta es incorrecta", Toast.LENGTH_LONG).show();
                            getFeedback("La respuesta es incorrecta");
                            button.setEnabled(false);
                            button.setImageResource(R.drawable.minus);
                        }
                    }
                });


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

                params.topMargin = 15;
                relativeLayout.setLayoutParams(params);

                layout.addView(relativeLayout);





            }else if(prueba.tipo == 2){
                int id = R.layout.quiz_layout_multiple1;
                RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

                TextView textView_numero = (TextView) relativeLayout.findViewById(R.id.preguntaNum);
                textView_numero.setText(num+"/"+lstPruebas.size());

                TextView textView_titulo = (TextView) relativeLayout.findViewById(R.id.quizPregunta);
                textView_titulo.setText(prueba.pregunta);

                final RadioButton opcion1 = (RadioButton)relativeLayout.findViewById(R.id.radioButton);
                opcion1.setText(prueba.respuestas.get(0).getRespuesta());

                final RadioButton opcion2 = (RadioButton)relativeLayout.findViewById(R.id.radioButton2);
                opcion2.setText(prueba.respuestas.get(1).getRespuesta());

                final RadioButton opcion3 = (RadioButton)relativeLayout.findViewById(R.id.radioButton3);
                opcion3.setText(prueba.respuestas.get(2).getRespuesta());

                final ImageButton button = (ImageButton)relativeLayout.findViewById(R.id.imageButton4);
                button.setTag(prueba);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Prueba aux = (Prueba)v.getTag();
                        int respuestaUser = 1;
                        if(opcion1.isChecked()){
                            if(esVerdadera(1,aux)){
                                //Toast.makeText(getActivity(), "Ha ganado "+ aux.experiencia +" puntos de experiencia", Toast.LENGTH_LONG).show();
                                getFeedback("La respuesta es correcta +"+aux.experiencia+"EXP");
                                button.setEnabled(false);
                                button.setImageResource(R.drawable.minus);
                            }else{
                                //Toast.makeText(getActivity(), "La respuesta es incorrecta", Toast.LENGTH_LONG).show();
                                getFeedback("La respuesta es incorrecta");
                                button.setEnabled(false);
                                button.setImageResource(R.drawable.minus);
                            }
                        }else if(opcion2.isChecked()){
                            if(esVerdadera(2,aux)){
                                //Toast.makeText(getActivity(), "Ha ganado "+ aux.experiencia +" puntos de experiencia", Toast.LENGTH_LONG).show();
                                getFeedback("La respuesta es correcta +"+aux.experiencia+"EXP");
                                button.setEnabled(false);
                                button.setImageResource(R.drawable.minus);
                            }else{
                                //Toast.makeText(getActivity(), "La respuesta es incorrecta", Toast.LENGTH_LONG).show();
                                getFeedback("La respuesta es incorrecta");
                                button.setEnabled(false);
                                button.setImageResource(R.drawable.minus);
                            }
                        }else if (opcion3.isChecked()){
                            if(esVerdadera(3,aux)){
                                //Toast.makeText(getActivity(), "Ha ganado "+ aux.experiencia +" puntos de experiencia", Toast.LENGTH_LONG).show();
                                getFeedback("La respuesta es correcta +"+aux.experiencia+"EXP");
                                button.setEnabled(false);
                                button.setImageResource(R.drawable.minus);
                            }else{
                                //Toast.makeText(getActivity(), "La respuesta es incorrecta", Toast.LENGTH_LONG).show();
                                getFeedback("La respuesta es incorrecta");
                                button.setEnabled(false);
                                button.setImageResource(R.drawable.minus);
                            }
                        }
                    }
                });

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

                params.topMargin = 15;
                relativeLayout.setLayoutParams(params);

                layout.addView(relativeLayout);


            }else if(prueba.tipo == 3){
                int id = R.layout.quiz_layout_multiple2;
                RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

                TextView textView_numero = (TextView) relativeLayout.findViewById(R.id.preguntaNum);
                textView_numero.setText(num+"/"+lstPruebas.size());

                TextView textView_titulo = (TextView) relativeLayout.findViewById(R.id.quizPregunta);
                textView_titulo.setText(prueba.pregunta);

                final CheckBox opcion1 = (CheckBox) relativeLayout.findViewById(R.id.checkBox);
                opcion1.setText(prueba.respuestas.get(0).getRespuesta());

                final CheckBox opcion2 = (CheckBox) relativeLayout.findViewById(R.id.checkBox2);
                opcion2.setText(prueba.respuestas.get(1).getRespuesta());

                final CheckBox opcion3 = (CheckBox) relativeLayout.findViewById(R.id.checkBox3);
                opcion3.setText(prueba.respuestas.get(2).getRespuesta());

                final ImageButton button = (ImageButton)relativeLayout.findViewById(R.id.imageButton4);
                button.setTag(prueba);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Prueba aux = (Prueba)v.getTag();
                        int respuestaUser = 1;
                        boolean cancel = false;
                        if(opcion1.isChecked()){
                            if(esVerdadera(1,aux)){
                             cancel = true;
                            }
                        }
                        if(opcion2.isChecked()){
                            if(esVerdadera(2,aux)){
                                cancel = true;
                            }
                        }
                        if (opcion3.isChecked()){
                            if(esVerdadera(3,aux)){
                                cancel = true;
                            }
                        }

                        if(cancel == true){
                            //Toast.makeText(getActivity(), "Ha ganado "+ aux.experiencia +" puntos de experiencia", Toast.LENGTH_LONG).show();
                            getFeedback("La respuesta es correcta +"+aux.experiencia+"EXP");
                            button.setEnabled(false);
                            button.setImageResource(R.drawable.minus);
                        }else{
                            //Toast.makeText(getActivity(), "La respuesta es incorrecta", Toast.LENGTH_LONG).show();
                            getFeedback("La respuesta es incorrecta");
                            button.setEnabled(false);
                            button.setImageResource(R.drawable.minus);
                        }
                    }
                });

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

                params.topMargin = 15;
                relativeLayout.setLayoutParams(params);

                layout.addView(relativeLayout);

            }



        }

        public void getFeedback(String mensaje){
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Feedback");
            alert.setMessage(mensaje);
            alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });

            alert.show();
        }
    }



    public static boolean esVerdadera(int rUser, Prueba prueba){
        for(int a = 0; a<prueba.respuestas.size(); a++){
            if(prueba.respuestas.get(a).getValido()==1){
                if(a+1==rUser){
                    return true;
                }
            }
        }
        return false;
    }

    public static Prueba getPrueba(int id){
        Prueba aux = new Prueba();
        for(int i = 0; i<lstPruebas.size(); i++){
            if(i + 1 == id){
                aux= lstPruebas.get(i);
            }
        }
        /*
        for(Tema t:lstTemas){
            if(t.getIdenficador() == id){
                aux= t;
            }
        }
        */
        return aux;
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return lstPruebas.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 4";
                case 4:
                    return "SECTION 5";
                case 5:
                    return "SECTION 6";
            }
            return null;
        }
    }
}
