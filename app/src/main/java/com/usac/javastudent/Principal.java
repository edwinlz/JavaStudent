package com.usac.javastudent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.ActionBar.LayoutParams;
import android.widget.Toast;

import com.usac.clasesjava.Amigo;
import com.usac.clasesjava.ConexionBD;
import com.usac.clasesjava.Modulo;
import com.usac.clasesjava.PagerAdapter;
import com.usac.clasesjava.Tema;
import com.usac.clasesjava.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewGroup layout;
    private ScrollView scrollView;
    private Usuario currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Intent recLog = getIntent();
        Bundle b = recLog.getExtras();

        String username_ = b.getString("usuario");
        currentUser = userSesion(username_);

        /*INICIO*/
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Modulos"));
        tabLayout.addTab(tabLayout.newTab().setText("Amigos"));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        /*FIN*/

        //layout = (ViewGroup)findViewById(R.id.content);
        //scrollView = (ScrollView) findViewById(R.id.scrollView);

        //pintarModulos();
        //pintarAmigos();
        //pintarContenido();

    }

    public List<Amigo> getAmigosLocal(){
        ArrayList<Amigo> amigo_ = new ArrayList<Amigo>();
        amigo_.add(new Amigo("Edwin", 100,1));
        amigo_.add(new Amigo("Raul", 1000,2));
        amigo_.add(new Amigo("Rony", 10000,1));
        amigo_.add(new Amigo("Crazy", 100000,2));
        return amigo_;
    }

    public List<Amigo> getAmigosLocal2(){
        ArrayList<Amigo> amigo_ = new ArrayList<Amigo>();
        amigo_.add(new Amigo("Edwin", 100,1));
        amigo_.add(new Amigo("Raul", 1000,2));
        return amigo_;
    }

    private void pintarContenido(){
        Tema tema = new Tema(1,1,"Variables","Uso de variables","<body style=\"text-align:justify;\"><h1>El lenguaje Java</h1><p> Como cualquier lenguaje de programación, el lenguaje <strong>Java</strong> tiene su propia estructura, reglas de sintaxis y paradigma de programación. El paradigma de programación del lenguaje Java se basa en el concepto de programación orientada a objetos (OOP) El lenguaje Java es un derivado del lenguaje C, por lo que sus reglas de sintaxis se parecen mucho</p></body>",0);
        setContenido(tema);
    }

    public void setContenido(Tema tema){
        LayoutInflater inflater = LayoutInflater.from(this);
        int id = R.layout.contenido_layout;

        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);

        ImageView imgView = (ImageView) relativeLayout.findViewById(R.id.conteAvatar);
        imgView.setImageResource(R.drawable.libro);

        TextView textView_titulo = (TextView) relativeLayout.findViewById(R.id.conteTitulo);
        textView_titulo.setText(tema.getNombre());

        TextView textView_desc = (TextView) relativeLayout.findViewById(R.id.conteDesc);
        textView_desc.setText(tema.getDescripcion());

        TextView textView_texto = (TextView) relativeLayout.findViewById(R.id.conteTexto);

        textView_texto.setText(Html.fromHtml(tema.getContenido(), null, null));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        params.topMargin = 15;
        relativeLayout.setLayoutParams(params);
        layout.addView(relativeLayout);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            ConexionBD con = new ConexionBD(this);
            con.open();
            if(currentUser != null)
                con.updateUsuarioSesion(currentUser.getUsername(),0);
            con.close();
            //Intent intent = getIntent();
            finish();
            //startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Usuario userSesion(String usernaname){
        Usuario respuesta = new Usuario();
        respuesta = null;

        ConexionBD con = new ConexionBD(this);
        con.open();

        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios =(ArrayList<Usuario>) con.getUsuarios();
        for(Usuario c:usuarios){
            if(c.getUsername().equals(usernaname)){
                respuesta = c;
            }
        }
        con.close();

        return respuesta;
    }
}
