package com.example.app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.credentials.GetCredentialRequest;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Registro extends AppCompatActivity {

    private Button bInicio;
    private Button bAtras;
    private TextInputLayout correo;   // antes EditText
    private TextInputLayout contra1;  // antes EditText
    private TextInputLayout contra2;  // antes EditText
    // ImageButton iB1 e iB2 eliminados: el ojo lo gestiona el XML con passwordToggleEnabled
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        bInicio  = findViewById(R.id.bAcepRegis);
        bAtras   = findViewById(R.id.bAtrasRegis);
        correo   = findViewById(R.id.eTCorreo);   // TextInputLayout
        contra1  = findViewById(R.id.eContra1);   // TextInputLayout
        contra2  = findViewById(R.id.eContra2);   // TextInputLayout

        // El ojo mostrar/ocultar ya no necesita código, lo maneja el XML

        bAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Registro.this, MainActivity.class);
                startActivity(i);
            }
        });

        bInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass1 = contra1.getEditText().getText().toString().trim();
                String pass2 = contra2.getEditText().getText().toString().trim();
                if (!pass1.equals(pass2)) {
                    Toast.makeText(Registro.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else {
                    newUsers();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "No hay ningun usuario registrado", Toast.LENGTH_SHORT).show();
        }
    }

    public void newUsers() {
        String email = correo.getEditText().getText().toString().trim();
        String pass  = contra2.getEditText().getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Registro.this, "Usuario creado con éxito. Procede a registrarte", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Registro.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(Registro.this, "Autentificación fallida.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}