package com.example.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ComandasFragment extends Fragment {

    private TextInputEditText etMesa;
    private TextView[] tvCantidades;
    private TextView tvPrecioTotal;
    private CheckBox cbGluten, cbLactosa, cbFrutosSec, cbMariscos, cbHuevo, cbSoja;

    // nombres y precios de los platos en orden
    private final String[] nombrePlatos = {
            "Ensalada César", "Espaguetis carbonara", "Paella Valenciana",
            "Chuletón a la brasa", "Secreto Ibérico", "Tarta de queso",
            "Tarta de trufas", "Radler", "Agua", "Coca Cola",
            "Fanta Naranja", "Fanta Limón", "Nestea"
    };
    private final double[] preciosPlatos = {
            8.50, 8.50, 14.00, 22.00, 25.00, 5.50, 6.50,
            2.50, 3.00, 2.80, 2.80, 2.80, 2.80
    };
    private int[] cantidades = new int[13];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comandas, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  mesa
        etMesa = view.findViewById(R.id.etMesa);

        //  total
        tvPrecioTotal = view.findViewById(R.id.tvPrecioTotal);

        //  alergias
        cbGluten = view.findViewById(R.id.cbGluten);
        cbLactosa = view.findViewById(R.id.cbLactosa);
        cbFrutosSec = view.findViewById(R.id.cbFrutosSec);
        cbMariscos = view.findViewById(R.id.cbMariscos);
        cbHuevo = view.findViewById(R.id.cbHuevo);
        cbSoja = view.findViewById(R.id.cbSoja);

        //  cantidades de cada plato
        tvCantidades = new TextView[]{
                view.findViewById(R.id.tvCantidad1),
                view.findViewById(R.id.tvCantidad2),
                view.findViewById(R.id.tvCantidad3),
                view.findViewById(R.id.tvCantidad4),
                view.findViewById(R.id.tvCantidad5),
                view.findViewById(R.id.tvCantidad6),
                view.findViewById(R.id.tvCantidad7),
                view.findViewById(R.id.tvCantidad8),
                view.findViewById(R.id.tvCantidad9),
                view.findViewById(R.id.tvCantidad10),
                view.findViewById(R.id.tvCantidad11),
                view.findViewById(R.id.tvCantidad12),
                view.findViewById(R.id.tvCantidad13)
        };

        //  botones + y -
        int[] btnMas = {
                R.id.btnMas1, R.id.btnMas2, R.id.btnMas3, R.id.btnMas4,
                R.id.btnMas5, R.id.btnMas6, R.id.btnMas7, R.id.btnMas8,
                R.id.btnMas9, R.id.btnMas10, R.id.btnMas11, R.id.btnMas12, R.id.btnMas13
        };
        int[] btnMenos = {
                R.id.btnMenos1, R.id.btnMenos2, R.id.btnMenos3, R.id.btnMenos4,
                R.id.btnMenos5, R.id.btnMenos6, R.id.btnMenos7, R.id.btnMenos8,
                R.id.btnMenos9, R.id.btnMenos10, R.id.btnMenos11, R.id.btnMenos12, R.id.btnMenos13
        };

        for (int i = 0; i < 13; i++) {
            final int index = i;
            view.findViewById(btnMas[i]).setOnClickListener(v -> {
                cantidades[index]++;
                tvCantidades[index].setText(String.valueOf(cantidades[index]));
                actualizarTotal();
            });
            view.findViewById(btnMenos[i]).setOnClickListener(v -> {
                if (cantidades[index] > 0) {
                    cantidades[index]--;
                    tvCantidades[index].setText(String.valueOf(cantidades[index]));
                    actualizarTotal();
                }
            });
        }

        // botón crear comanda
        view.findViewById(R.id.btnCrearComanda).setOnClickListener(v -> {
            guardarComanda();
        });
    }

    private void actualizarTotal() {
        double total = 0;
        for (int i = 0; i < 13; i++) {
            total += cantidades[i] * preciosPlatos[i];
        }
        tvPrecioTotal.setText(String.format("%.2f €", total));
    }

    private void guardarComanda() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String correo = user.getEmail();
        String uid = user.getUid();
        String mesa = etMesa.getText().toString().trim();

        if (user == null) {
            Toast.makeText(getContext(), "No hay usuario logueado.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mesa.isEmpty()) {
            Toast.makeText(getContext(), "Introduce el número de mesa:", Toast.LENGTH_SHORT).show();
            return;
        }

        // recoger platos con cantidad > 0
        Map<String, Integer> platosSeleccionados = new HashMap<>();
        for (int i = 0; i < 13; i++) {
            if (cantidades[i] > 0) {
                platosSeleccionados.put(nombrePlatos[i], cantidades[i]);
            }
        }

        if (platosSeleccionados.isEmpty()) {
            Toast.makeText(getContext(), "Añade al menos un plato", Toast.LENGTH_SHORT).show();
            return;
        }

        //  recoger alergias
        List<String> alergias = new ArrayList<>();
        if (cbGluten.isChecked()) alergias.add("Gluten");
        if (cbLactosa.isChecked()) alergias.add("Lactosa");
        if (cbFrutosSec.isChecked()) alergias.add("Frutos secos");
        if (cbMariscos.isChecked()) alergias.add("Mariscos");
        if (cbHuevo.isChecked()) alergias.add("Huevo");
        if (cbSoja.isChecked()) alergias.add("Soja");

        // calcular total
        double total = 0;
        for (int i = 0; i < 13; i++) {
            total += cantidades[i] * preciosPlatos[i];
        }

        // crear mapa para Firebase
        Map<String, Object> comanda = new HashMap<>();
        comanda.put("mesa", mesa);
        comanda.put("platos", platosSeleccionados);
        comanda.put("alergias", TextUtils.join(", ", alergias));
        comanda.put("total", total);
        comanda.put("correo",correo);

        // guardar en Firebase con id automático
        FirebaseFirestore.getInstance()
                .collection("usuarios")
                .document(uid)
                .collection("comandas")
                .add(comanda)
                .addOnSuccessListener(ref -> {
                    Toast.makeText(getContext(), "Comanda creada", Toast.LENGTH_SHORT).show();
                    resetFormulario();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void resetFormulario() {
        etMesa.setText("");
        for (int i = 0; i < 13; i++) {
            cantidades[i] = 0;
            tvCantidades[i].setText("0");
        }
        cbGluten.setChecked(false);
        cbLactosa.setChecked(false);
        cbFrutosSec.setChecked(false);
        cbMariscos.setChecked(false);
        cbHuevo.setChecked(false);
        cbSoja.setChecked(false);
        tvPrecioTotal.setText("0,00 €");
    }
}