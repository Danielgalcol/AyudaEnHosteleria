package com.example.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.app.Perfil;
import com.example.app.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {


    private TextInputEditText etNombre, etApellidos, etDireccion, etRol, etTelefono;
    private Switch switchActivo;
    private MaterialButton btnGuardar;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // inicializar vistas
        etNombre = view.findViewById(R.id.etNombre);
        etApellidos = view.findViewById(R.id.etApellidos);
        etDireccion = view.findViewById(R.id.etDireccion);
        etRol = view.findViewById(R.id.etRol);
        etTelefono = view.findViewById(R.id.etTelefono);
        switchActivo = view.findViewById(R.id.switchActivo);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        // obtener usuario actua lde firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String correo;
        String uid = user.getUid();//coger el uid del usuario

        // comprobar si esta logueado
        if (user != null) {
            correo = user.getEmail();
        } else {
            correo = "";
        }

        // guardar
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String apellidos = etApellidos.getText().toString().trim();
            String direccion = etDireccion.getText().toString().trim();
            String rol = etRol.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            boolean activo = switchActivo.isChecked();

            // q no esten vacios
            if (nombre.isEmpty() || apellidos.isEmpty()) {
                Toast.makeText(getContext(), "Rellena los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // creacion objeto y mandar a Firebase
            Perfil perfil = new Perfil(nombre, apellidos, correo, rol, telefono, activo, direccion);
            FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .document(uid)
                    .collection("perfil")
                    .add(perfil)
                    .addOnSuccessListener(documentReference ->
                            Toast.makeText(getContext(), "Guardado con id: " + documentReference.getId(), Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });
    }




}