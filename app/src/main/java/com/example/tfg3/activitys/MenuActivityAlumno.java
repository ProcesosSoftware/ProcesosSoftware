package com.example.tfg3.activitys;

import android.content.Intent;
import android.os.Bundle;

import com.example.tfg3.R;
import com.example.tfg3.activitys.adaptadores.AdaptadorFirebase;
import com.example.tfg3.activitys.dialogos.DialogoCalendario;
import com.example.tfg3.activitys.dialogos.DialogoNotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.tfg3.activitys.fragments.CalendarioAlumnoFragment;
import com.example.tfg3.activitys.fragments.EvaluacionesFragment;
import com.example.tfg3.activitys.fragments.FormularioAlumnosFragment;
import com.example.tfg3.activitys.holders.CicloHolder;
import com.example.tfg3.activitys.utils.Ciclos;
import com.example.tfg3.activitys.utils.Usuarios;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;

public class MenuActivityAlumno extends AppCompatActivity implements AdaptadorFirebase.OnAdaptadorListener, DialogoCalendario.OnDialogoPersoListener, NavigationView.OnNavigationItemSelectedListener {

    // Inicializo las variables que vaya a necesitar
    String currentUser, currentEmail;
    TextView nombreEdit, emailEdit;
    Toolbar toolbar;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_alumno);

        // Instancio las variables
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Gestión escolar CES");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Instancio las variables contenidas en la clase "nav_header_evaluaciones" llamando al
        // metodo getHeaderView desde el navigation creado
        View headView = navigationView.getHeaderView(0);
        nombreEdit = headView.findViewById(R.id.id_nombre_apellido_perfil);
        emailEdit = headView.findViewById(R.id.email_Perfil);

        // Llamo al metodo para cambiar el nombre del usuario y el email que estan en el navigation drawler
        cambiarNombre();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Metodo para cambiar el nombre y el email de la cabecera por los del usuario.
    private void cambiarNombre() {
        final String uid = getIntent().getExtras().getString("uid");
        DatabaseReference referenciaTipo = FirebaseDatabase.getInstance().getReference().child("usuarios").child(uid);
        referenciaTipo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);
                currentUser = usuarios.getNombre();
                currentEmail = usuarios.getEmail();

                nombreEdit.setText(currentUser);
                emailEdit.setText(currentEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cerrar_sesion, menu);
        return true;

    }

    @Override
    public void onAdaptadorSelected() {
        DialogoNotas dialogoNotas = DialogoNotas.newInstance((String) getIntent().getExtras().get("uid"));
        dialogoNotas.show(getSupportFragmentManager(), "perso");

    }

    @Override
    public void onDilagoloSelected(String informacion) {
        Toast.makeText(getApplicationContext(), informacion, Toast.LENGTH_LONG).show();
    }

    //Menu de arriba
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cerrar_sesion) {
            FirebaseAuth.getInstance().signOut();
            returnLogin();
        }

        return super.onOptionsItemSelected(item);
    }

    //Menu lateral
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_evaluaciones) {
            cargarFragment(EvaluacionesFragment.newInstance((String) getIntent().getExtras().get("uid")));
        } else if (id == R.id.nav_calendar) {
            cargarFragment(new CalendarioAlumnoFragment());
        } else if (id == R.id.nav_chat) {
            cargarFragment(FormularioAlumnosFragment.newInstance((String) getIntent().getExtras().get("uid")));
        } else if (id == R.id.nav_cerrarSesion) {
            FirebaseAuth.getInstance().signOut();
            returnLogin();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    //Metodo para cargar el fragment
    private void cargarFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedor_fragments, fragment).commit();
    }

    // Cuando se llama a este metodo se regresa a MainActivity
    private void returnLogin() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }


}
