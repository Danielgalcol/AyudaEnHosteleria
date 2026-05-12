package com.example.app;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.app.databinding.ActivityIniciadoBinding;
import com.example.app.fragments.*;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Iniciado extends AppCompatActivity  {

    private TextView correoLogin;
    private TextView correo;
    private ActivityIniciadoBinding bind;//para la barra lateral


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        bind = ActivityIniciadoBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_content_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        //navegacion entre fragments
        bind.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //se cargen los fragsment q mandemos visualizar
                boolean fragmentTransaction = false;
                Fragment fragment = null;
                int itemId = menuItem.getItemId();//id del menu seleccionado
                //barra lateral
                if(itemId == R.id.nav_home){
                    fragment = new HomeFragment();
                    fragmentTransaction = true;
                }
                if(itemId == R.id.nav_about){
                    fragment = new ProfileFragment();
                    fragmentTransaction = true;

                }
                //para ir al fragment
                if(fragmentTransaction){
                    for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount();++i){
                        getSupportFragmentManager().popBackStack();
                    }
                    //asignar nuevo fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment,fragment).commit();
                    menuItem.setCheckable(true);
                    Objects.requireNonNull(getSupportActionBar()).setTitle(menuItem.getTitle());
                    bind.activityMain.closeDrawers();//para q se cierre el menu

                }

                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment,new HomeFragment()).commit();
        bind.navView.setCheckedItem(R.id.nav_home);//la app empieza en inicio


        OnBackPressedCallback oBPCB = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                int entries = getSupportFragmentManager().getBackStackEntryCount();
                if(entries>0){
                    getSupportFragmentManager().popBackStack();
                }
                else if(entries == 0 && bind.activityMain.isDrawerOpen(GravityCompat.START)){
                    bind.activityMain.closeDrawers();
                } else if (entries == 0) {
                    bind.activityMain.openDrawer(GravityCompat.START);
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(oBPCB);

    }
    //para cuando se quite la vista
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            bind.activityMain.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        setToolBar();
    }



    private void setToolBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //obtenemos el correo de Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Objects.requireNonNull(getSupportActionBar()).setTitle(user.getEmail());
        }
    }
}