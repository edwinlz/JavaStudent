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
import com.usac.clasesjava.Estatica;
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
    //private Usuario currentUser;

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


        View header = navigationView.getHeaderView(0);
        ImageView imageUser = (ImageView) header.findViewById(R.id.imageUser);
        imageUser.setImageResource(getIdImage(Estatica.usuario_actual.getImagen()));

        TextView nameUser = (TextView) header.findViewById(R.id.nameUser);
        nameUser.setText(Estatica.usuario_actual.getNombre());

        TextView emailUser = (TextView) header.findViewById(R.id.emailUser);
        emailUser.setText(Estatica.usuario_actual.getEmail());

        TextView expUser = (TextView) header.findViewById(R.id.userExp);
        expUser.setText(""+Estatica.usuario_actual.getExperiencia());

        /*INICIO*/
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Modulos"));
        tabLayout.addTab(tabLayout.newTab().setText("Amigos"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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

    }

    public int getIdImage(int imagen){
        switch (imagen){
            case 1: return R.drawable.user_boy_3;
            case 2: return R.drawable.user_man_1;
            case 3: return R.drawable.user_man_2;
            case 4: return R.drawable.user_girl_1;
            case 5: return R.drawable.user_girl_2;
            case 6: return R.drawable.user_girl_3;
            default: return R.drawable.user_boy_3;
        }
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

        if (id == R.id.nav_friends) {
            Intent m = new Intent(Principal.this, Amigos.class);
            Bundle infoUsuario = new Bundle();
            infoUsuario.putString("usuario", Estatica.username_actual);
            m.putExtras(infoUsuario);
            startActivity(m);
        } else if (id == R.id.nav_config) {
            Intent m = new Intent(Principal.this, CambioPerfil.class);
            startActivity(m);
        } else if (id == R.id.nav_stats) {
            Intent m = new Intent(Principal.this, Estadisticas.class);
            startActivity(m);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_close) {
            ConexionBD con = new ConexionBD(this);
            con.open();
            if(Estatica.usuario_actual != null)
                con.updateUsuarioSesion(Estatica.usuario_actual.getUsername(),0);
            con.close();
            //Intent intent = getIntent();
            finish();

            Intent log = new Intent(Principal.this,Login.class);
            startActivity(log);
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
