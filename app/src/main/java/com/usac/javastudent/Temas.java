package com.usac.javastudent;

import android.app.ActionBar;
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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.usac.clasesjava.ConexionBD;
import com.usac.clasesjava.Modulo;
import com.usac.clasesjava.Tema;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Temas extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public Modulo currentModulo;
    public static ArrayList<Tema> lstTemas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle recibido = getIntent().getExtras();
        int modulo_data = recibido.getInt("modulo");

        currentModulo = getModulo(modulo_data);

        lstTemas = getTemas(currentModulo.getIdentificador());

        TextView text_lstMod = (TextView)findViewById(R.id.lstemaMod);
        text_lstMod.setText(currentModulo.getNombre());

        TextView text_lstModDesc = (TextView)findViewById(R.id.lstModDesc);
        text_lstModDesc.setText(currentModulo.getDescripcion());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);



    }

    private Modulo getModulo(int id){
        Modulo aux = new Modulo();
        ConexionBD bd = new ConexionBD(this);
        bd.open();
        aux = bd.getModulo(id);
        bd.close();
        return aux;
    }

    private ArrayList<Tema> getTemas(int modulo){
        ConexionBD bd  = new ConexionBD(this);
        bd.open();
        ArrayList<Tema> temas = new ArrayList<Tema>();
        temas =(ArrayList<Tema>) bd.getTemas(modulo);
        bd.close();
        return temas;
    }

    public static Tema getTema(int id){
        Tema aux = new Tema();
        for(int i = 0; i<lstTemas.size(); i++){
            if(i+1 == id){
                aux= lstTemas.get(i);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_temas, menu);
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
        public static PlaceholderFragment newInstance(int numeroTema) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, numeroTema);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_temas, container, false);
            layout = (ViewGroup)rootView.findViewById(R.id.temaNuevo);

            int ntema = getArguments().getInt(ARG_SECTION_NUMBER);

            setTema(getTema(ntema));


            return rootView;
        }

        public void setTema(Tema tema){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            int id = R.layout.temas_layout;

            RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

            TextView textView_titulo = (TextView) relativeLayout.findViewById(R.id.nameTema);
            textView_titulo.setText(tema.getNombre());

            TextView textView_estado = (TextView) relativeLayout.findViewById(R.id.temaEstado);
            textView_estado.setText("Disponible");

            ImageButton imgButton = (ImageButton)relativeLayout.findViewById(R.id.imageButton);
            imgButton.setTag(tema);

            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tema aux = (Tema)v.getTag();
                    //Toast.makeText(getActivity(),"Hola",Toast.LENGTH_SHORT).show();
                    Bundle datos = new Bundle();
                    datos.putInt("tema",aux.getIdenficador());
                    Intent intent = new Intent(getActivity(), Contenido.class);
                    intent.putExtras(datos);
                    startActivity(intent);
                }
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

            params.topMargin = 15;
            relativeLayout.setLayoutParams(params);

            layout.addView(relativeLayout);
        }
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
            return currentModulo.getTemas();
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
