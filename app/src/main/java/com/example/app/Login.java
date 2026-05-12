package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private TextInputLayout correo;   // antes EditText
    private TextInputLayout contra;   // antes EditText
    private Button bAceptar;
    private Button bAtras;
    // ImageButton iB eliminado: el ojo lo gestiona el XML con passwordToggleEnabled

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        correo   = findViewById(R.id.eTCorreo);   // TextInputLayout
        contra   = findViewById(R.id.editText);   // TextInputLayout
        bAceptar = findViewById(R.id.bAcepLogin);
        bAtras   = findViewById(R.id.bAtrasLogin);

        // El ojo mostrar/ocultar ya no necesita código, lo maneja el XML

        bAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(Login.this, MainActivity.class);
                startActivity(addIntent);
            }
        });

        bAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email    = correo.getEditText().getText().toString().trim();
                String password = contra.getEditText().getText().toString().trim();

                // Comprueba que el email y contraseña no estén vacíos
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Rellena los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(Login.this, "Email: " + email, Toast.LENGTH_SHORT).show();

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> {
                            Intent intent = new Intent(Login.this, Iniciado.class);
                            intent.putExtra("correo", email);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }
        });
    }
}